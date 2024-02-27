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
      PreparedStatement preparedStatement = dalConn.getPS(
          "SELECT id_user, lastname_user, firstname_user, email, phone_number"
              + "role_user FROM InternshipManagement.users WHERE id_user = ?"
      );
      // executing the query
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        // checking for a result
        if (resultSet.next()) {
          // if result -> seting the userDTO attribut with the given result
          user.setId(resultSet.getInt("id_user"));
          user.setLastName(resultSet.getString("lastname_user"));
          user.setFistName(resultSet.getString("firstname_user"));
          user.setEmail(resultSet.getString("email"));
          user.setTelephoneNumber(resultSet.getString("phone_number"));
          user.setRole(resultSet.getString("role_user"));
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
          "SELECT id_user,lastname_user,"
              + "firstname_user, email, phone_number, "
              + "role_user FROM InternshipManagement.users WHERE email = ?");
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {

          user.setId(rs.getInt("id_user"));
          user.setLastName(rs.getString("lastname_user"));
          user.setFistName(rs.getString("firstname_user"));
          user.setEmail(rs.getString("email"));
          user.setTelephoneNumber(rs.getString(""));
          user.setRole(rs.getString("role_user"));

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
