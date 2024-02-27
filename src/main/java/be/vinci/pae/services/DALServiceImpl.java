package be.vinci.pae.services;

import jakarta.inject.Singleton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DALServiceImpl implements DALService {

  private Connection conn;

  @Singleton
  public DALServiceImpl() {
    String url = "jdbc:postgresql://coursinfo.vinci.be:5432/dbmatteo_erismann?user=matteo_erismann";
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      System.out.println("Driver PostgreSQL manquant !");
      System.exit(1);
    }
    try {
      conn = DriverManager.getConnection(url, "matteoerismann", "Bada!dan4751");
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
