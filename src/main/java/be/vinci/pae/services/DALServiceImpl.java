package be.vinci.pae.services;

import be.vinci.pae.utils.Config;
import jakarta.inject.Singleton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The Class DALServiceImpl.
 */
public class DALServiceImpl implements DALService {

  String url = Config.getProperty("DatabaseFilePath");
  String dataBaseUser = Config.getProperty("DatabaseUser");
  String dataBasePassword = Config.getProperty("DatabasePassword");
  private Connection conn;

  @Singleton
  public DALServiceImpl() {

    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      System.out.println("Driver PostgreSQL manquant !");
      System.exit(1);
    }
    try {
      conn = DriverManager.getConnection(url, dataBaseUser, dataBasePassword);
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  @Override
  public PreparedStatement getPS(String request) throws SQLException {
    return conn.prepareStatement(request);
  }
}
