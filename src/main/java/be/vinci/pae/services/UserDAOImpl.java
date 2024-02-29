package be.vinci.pae.services;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.UserDTO;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of user services.
 */
public class UserDAOImpl implements UserDAO {

  //using a domain factory to create object from the domain
  @Inject
  private DomainFactory domainFactory;
  //using the DALService to establish a connection to the data base
  @Inject
  private DALService dalConn;


  @Override
  public UserDTO getOneUserByID(int id) {
    //creating a user DTO to stock the information
    UserDTO user = domainFactory.getUserDTO();
    try {
      /*
      Creating a preparedstatement using the DALService method, passing the sql querry
      as the parameter
       */
      PreparedStatement ps = dalConn.getPS(
          "SELECT id,lastname,"
              + "firstname, email, telephonenumber,"
              + "_role, _password FROM project.users WHERE id = ?"
      );
      ps.setInt(1, id);
      // executing the query
      try (ResultSet resultSet = ps.executeQuery()) {
        // checking for a result
        if (resultSet.next()) {
          // if result -> seting the userDTO attribut with the given result
          user.setId(resultSet.getInt(1));
          user.setLastName(resultSet.getString(2));
          user.setFistName(resultSet.getString(3));
          user.setEmail(resultSet.getString(4));
          user.setTelephoneNumber(resultSet.getString(5));
          user.setRole(resultSet.getString(6));
          user.setPassword(resultSet.getString(7));
        } else {
          // if no result -> user is null
          user = null;
        }
      }
      // catching exeptions
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
    // returning the result, either a userDTO with information or null
    return user;
  }

  @Override
  public UserDTO getOneUserByEmail(String email) {
    // refere to the commentary of the getOneUserByID method
    UserDTO user = domainFactory.getUserDTO();
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT id,lastname,"
              + "firstname, email, telephonenumber, "
              + "_role, _password FROM project.users WHERE email = ?");
      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {

          user.setId(rs.getInt(1));
          user.setLastName(rs.getString(2));
          user.setFistName(rs.getString(3));
          user.setEmail(rs.getString(4));
          user.setTelephoneNumber(rs.getString(5));
          user.setRole(rs.getString(6));
          user.setPassword(rs.getString(7));

        } else {
          user = null;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
    return user;
  }


}
