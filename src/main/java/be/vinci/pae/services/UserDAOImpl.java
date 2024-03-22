package be.vinci.pae.services;

import be.vinci.pae.api.filters.FatalException;
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
  //using the DALService to establish a connection to the database
  @Inject
  private DALServices dalConn;


  @Override
  public UserDTO getOneUserByID(int id) {
    //creating a user DTO to stock the information, setting to null
    UserDTO user = null;
    try {
      /*
      Creating a preparedstatement using the DALService method, passing the sql querry
      as the parameter
       */
      PreparedStatement ps = dalConn.getPS(
          "SELECT id_user,lastname_user,"
              + "firstname_user, email, phone_number,"
              + "role_user, password_user FROM InternshipManagement.users WHERE id_user = ?"
      );
      ps.setInt(1, id);
      // executing the query
      try (ResultSet resultSet = ps.executeQuery()) {
        // checking for a result
        if (resultSet.next()) {
          /* if result -> calling the gerResultSet method
          to set the attributes of the user with the given results
           */
          user = getResultSet(resultSet);
        }
      }
      // closing the prepared statement
      ps.close();
      dalConn.closeConnection();
      // catching exeptions
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e);
    }
    // returning the result, either a userDTO with information or null if no result set
    return user;
  }

  @Override
  public UserDTO getOneUserByEmail(String email) {
    // refere to the commentary of the getOneUserByID method
    UserDTO user = null;
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT id_user,lastname_user,"
              + "firstname_user, email, phone_number, "
              + "role_user, password_user FROM InternshipManagement.users WHERE email = ?");
      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          user = getResultSet(rs);
        }
      }
      ps.close();
      dalConn.closeConnection();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e);
    }
    return user;
  }

  /**
   * Generalisation of the resultset treatment. Get the resultset of the calling method and create
   * an userDTO with the given information.
   *
   * @param resultSet Resultset
   * @return UserDTO
   */
  private UserDTO getResultSet(ResultSet resultSet) throws SQLException {
    // creating a user DTO to stock the information
    UserDTO user = domainFactory.getUserDTO();
    // using the result set, setting the attribut of the user
    user.setId(resultSet.getInt(1));
    user.setLastName(resultSet.getString(2));
    user.setFistName(resultSet.getString(3));
    user.setEmail(resultSet.getString(4));
    user.setTelephoneNumber(resultSet.getString(5));
    user.setRole(resultSet.getString(6));
    user.setPassword(resultSet.getString(7));
    return user;
  }


}
