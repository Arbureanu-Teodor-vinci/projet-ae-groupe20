package be.vinci.pae.services;

import be.vinci.pae.api.filters.FatalException;
import be.vinci.pae.utils.Config;
import jakarta.inject.Singleton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The Class DALServiceImpl.
 */
public class DALServicesImpl implements DALTransactionServices, DALServices {

  private final ThreadLocal<Connection> conn = new ThreadLocal<Connection>();
  String url = Config.getProperty("DatabaseFilePath");
  String dataBaseUser = Config.getProperty("DatabaseUser");
  String dataBasePassword = Config.getProperty("DatabasePassword");

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
  public PreparedStatement getPS(String request) throws SQLException {
    checkConnection();
    return conn.get().prepareStatement(request);
  }

  @Override
  public PreparedStatement startTransaction() {
    checkConnection();
    try {
      return conn.get().prepareStatement("BEGIN TRANSACTION");
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public PreparedStatement commitTransaction() {
    checkConnection();
    try {
      return conn.get().prepareStatement("COMMIT TRANSACTION");//changer auto commit
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public PreparedStatement rollbackTransaction() {
    checkConnection();
    try {
      return conn.get().prepareStatement("ROLLBACK TRANSACTION");
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public void closeConnection() {
    checkConnection();
    try {
      conn.get().close();
      // conn.remove();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private void checkConnection() {
    if (conn.get() == null) {
      try {
        conn.set(DriverManager.getConnection(url, dataBaseUser, dataBasePassword));
      } catch (SQLException e) {
        e.printStackTrace();
        throw new FatalException(e);
      }
    }
  }
}
