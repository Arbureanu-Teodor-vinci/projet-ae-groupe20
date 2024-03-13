package be.vinci.pae.services;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Services to manage the connexion to the database a signle time.
 */
public interface DALService {

  /**
   * create a prepared statement using the query recieved as a parameter.
   *
   * @param request String
   * @return a prepared statement
   */
  PreparedStatement getPS(String request) throws SQLException;
}
