package be.vinci.pae.services.userservices;

import be.vinci.pae.api.filters.FatalException;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.services.dal.DALServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of user services.
 */
public class UserDAOImpl implements UserDAO {

  //using a domain factory to create object from the domain
  @Inject
  protected DomainFactory domainFactory;
  //using the DALService to establish a connection to the database
  @Inject
  protected DALServices dalConn;


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
          "SELECT u.*,ay.academic_year FROM InternshipManagement.users u"
              + " LEFT JOIN InternshipManagement.student s on u.id_user = s.id_user"
              + " Left Join InternshipManagement.academic_year ay on ay.id_academic_year=s.academic_year"
              + " WHERE u.id_user = ?"
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
          // user.setAcademicYear(resultSet.getString("academic_year"));
        }
      }
      // closing the prepared statement
      ps.close();
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
          "SELECT u.*,ay.academic_year FROM InternshipManagement.users u"
              + " LEFT JOIN InternshipManagement.student s on u.id_user = s.id_user"
              + " Left Join InternshipManagement.academic_year ay on ay.id_academic_year=s.academic_year"
              + " WHERE u.email = ?");
      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          user = getResultSet(rs);
          //user.setAcademicYear(rs.getString("academic_year"));
        }
      }
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e);
    }
    return user;
  }

  @Override
  public List<UserDTO> getAllUsers() {
    List<UserDTO> users = new ArrayList<>();
    UserDTO user;
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT u.*,ay.academic_year FROM InternshipManagement.users u"
              + " LEFT JOIN InternshipManagement.student s on u.id_user = s.id_user"
              + " Left Join InternshipManagement.academic_year ay on ay.id_academic_year=s.academic_year");
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          user = getResultSet(rs);
          // user.setAcademicYear(rs.getString("academic_year"));
          users.add(user);
        }
        ps.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e);
    }
    return users;
  }

  @Override
  public UserDTO addUser(UserDTO user) {
    try {
      PreparedStatement ps = dalConn.getPS(
          "INSERT INTO InternshipManagement.users (lastname_user, firstname_user, email, phone_number, registration_date, role_user, password_user)"
              + " VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id_user, role_user, email, firstname_user, lastname_user, phone_number, registration_date, password_user");
      ps.setString(1, user.getLastName());
      ps.setString(2, user.getFirstName());
      ps.setString(3, user.getEmail());
      ps.setString(4, user.getTelephoneNumber());
      ps.setDate(5, java.sql.Date.valueOf(user.getRegistrationDate()));
      ps.setString(6, user.getRole());
      ps.setString(7, user.getPassword());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          user = getResultSet(rs);
        }
      }
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e);
    }
    return user;
  }

  @Override
  public UserDTO updateUser(UserDTO user) {
    try {
      PreparedStatement ps = dalConn.getPS(
          "UPDATE InternshipManagement.users SET lastname_user = ?, firstname_user = ?, email = ?, phone_number = ?, password_user = ?"
              + " WHERE id_user = ?");
      ps.setString(1, user.getLastName());
      ps.setString(2, user.getFirstName());
      ps.setString(3, user.getEmail());
      ps.setString(4, user.getTelephoneNumber());
      ps.setString(5, user.getPassword());
      ps.setInt(6, user.getId());
      ps.executeUpdate();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e);
    }
    return getOneUserByID(user.getId());
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
    user.setId(resultSet.getInt("id_user"));
    user.setLastName(resultSet.getString("lastname_user"));
    user.setFirstName(resultSet.getString("firstname_user"));
    user.setEmail(resultSet.getString("email"));
    user.setTelephoneNumber(resultSet.getString("phone_number"));
    user.setRegistrationDate(resultSet.getDate("registration_date").toLocalDate());
    user.setRole(resultSet.getString("role_user"));
    user.setPassword(resultSet.getString("password_user"));
    return user;
  }


}
