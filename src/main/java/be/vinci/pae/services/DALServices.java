package be.vinci.pae.services;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface DALServices {

  /**
   * Create a prepared statement using the query recieved as a parameter.
   *
   * @param request String
   * @return a prepared statement
   * @throws SQLException if the query is not correct
   */
  PreparedStatement getPS(String request) throws SQLException;

  /**
   * Close the connection to the database.
   */
  public void closeConnection();
}
