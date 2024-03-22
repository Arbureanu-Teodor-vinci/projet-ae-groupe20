package be.vinci.pae.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Services of database connection to send single SQL queries.
 */
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
  void closeConnection();

  /**
   * Get the connection to the database.
   *
   * @return the connection
   * @throws SQLException if the connection is not correct
   */
  Connection getConnection();
}
