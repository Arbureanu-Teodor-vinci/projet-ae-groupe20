package be.vinci.pae.services;

import be.vinci.pae.api.filters.FatalException;
import be.vinci.pae.utils.Config;
import jakarta.inject.Singleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * The Class DALServiceImpl.
 */
public class DALServicesImpl implements DALTransactionServices, DALServices {

  private static final BasicDataSource dataSourcePool = new BasicDataSource();
  private static final ThreadLocal<Connection> threadConnection = new ThreadLocal<Connection>();

  /**
   * Instantiates a new DAL service impl and connect to the database.
   */
  @Singleton
  public DALServicesImpl() {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      System.out.println("Driver PostgreSQL manquant !");
      System.exit(1);
    }

    dataSourcePool.setDriverClassName("org.postgresql.Driver");
    dataSourcePool.setUrl(Config.getProperty("DatabaseFilePath"));
    dataSourcePool.setUsername(Config.getProperty("DatabaseUser"));
    dataSourcePool.setPassword(Config.getProperty("DatabasePassword"));
    dataSourcePool.setInitialSize(5);
    dataSourcePool.setMaxTotal(5);
  }

  @Override
  public PreparedStatement getPS(String request) throws SQLException {
    return getConnection().prepareStatement(request);
  }

  @Override
  public Connection getConnection() {
    Connection connection = threadConnection.get();
    try {
      if (connection == null || connection.isClosed()) {
        connection = dataSourcePool.getConnection();
        threadConnection.set(connection);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return connection;
  }

  @Override
  public void closeConnection() {
    Connection connection = threadConnection.get();
    try {
      if (connection != null && !connection.isClosed()) {
        connection.close();
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    threadConnection.remove();
  }

  @Override
  public void startTransaction() {
    Connection connection = getConnection();
    try {
      connection.setAutoCommit(false);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public void commitTransaction() {
    Connection connection = getConnection();
    try {
      connection.commit();
      connection.setAutoCommit(true);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    closeConnection();
  }

  @Override
  public void rollbackTransaction() {
    Connection connection = threadConnection.get();
    if (connection != null) {
      try {
        connection.rollback();
        connection.setAutoCommit(true);
      } catch (SQLException e) {
        throw new FatalException(e);
      }
    }
    closeConnection();
  }
}
