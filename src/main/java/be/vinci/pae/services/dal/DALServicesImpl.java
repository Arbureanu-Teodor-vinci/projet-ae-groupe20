package be.vinci.pae.services.dal;

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

  static {
    dataSourcePool.setDriverClassName("org.postgresql.Driver");
    dataSourcePool.setUrl(Config.getProperty("DatabaseFilePath"));
    dataSourcePool.setUsername(Config.getProperty("DatabaseUser"));
    dataSourcePool.setPassword(Config.getProperty("DatabasePassword"));
    dataSourcePool.setMinIdle(5);
    dataSourcePool.setMaxIdle(10);
    dataSourcePool.setMaxTotal(5);
  }

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
  }

  @Override
  public PreparedStatement getPS(String request) {
    try {
      return getConnection().prepareStatement(request);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
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
      // If an error occurs, close the connection before throwing the exception
      closeConnection();
      throw new FatalException(e);
    }
    return connection;
  }

  @Override
  public void closeConnection() {
    Connection connection = threadConnection.get();
    try {
      if (connection != null && !connection.isClosed() && connection.getAutoCommit()) {
        connection.close();
        threadConnection.remove();
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
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
    Connection connection = threadConnection.get();
    try {
      connection.commit();
      connection.setAutoCommit(true);
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      closeConnection();
    }
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
      } finally {
        closeConnection();
      }
    }
  }
}
