package be.vinci.pae.services.userservices;

import be.vinci.pae.api.filters.FatalException;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.services.dal.DALServices;
import be.vinci.pae.utils.Logger;
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
  private DomainFactory domainFactory;
  //using the DALService to establish a connection to the database
  @Inject
  private DALServices dalConn;


  @Override
  public UserDTO getOneUserByID(int id) {
    Logger.logEntry("User DAO - getOneUserByID" + id);
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
              + " Left Join InternshipManagement.academic_year ay on "
              + " ay.id_academic_year=s.academic_year"
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
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    // returning the result, either a userDTO with information or null if no result set
    return user;
  }

  @Override
  public UserDTO getOneUserByEmail(String email) {
    Logger.logEntry("User DAO - getOneUserByEmail" + email);
    // refere to the commentary of the getOneUserByID method
    UserDTO user = null;
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT u.*,ay.academic_year FROM InternshipManagement.users u"
              + " LEFT JOIN InternshipManagement.student s on u.id_user = s.id_user"
              + " Left Join InternshipManagement.academic_year ay on "
              + " ay.id_academic_year=s.academic_year"
              + " WHERE u.email = ?");
      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          user = getResultSet(rs);
        }
      }
      ps.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return user;
  }

  @Override
  public List<UserDTO> getAllUsers() {
    Logger.logEntry("User DAO - getAllUsers");
    List<UserDTO> users = new ArrayList<>();
    UserDTO user;
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT u.*,ay.academic_year FROM InternshipManagement.users u"
              + " LEFT JOIN InternshipManagement.student s on u.id_user = s.id_user"
              + " Left Join InternshipManagement.academic_year ay "
              + " on ay.id_academic_year=s.academic_year");
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          user = getResultSet(rs);
          // user.setAcademicYear(rs.getString("academic_year"));
          users.add(user);
        }
        ps.close();
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return users;
  }

  @Override
  public UserDTO addUser(UserDTO user) {
    Logger.logEntry("User DAO - addUser" + user.getEmail());
    try {
      PreparedStatement ps = dalConn.getPS(
          "INSERT INTO InternshipManagement.users "
              + " (lastname_user, firstname_user, email, phone_number, registration_date, "
              + " role_user, password_user, version)"
              + " VALUES (?, ?, ?, ?, ?, ?, ?, 1) "
              + " RETURNING id_user, role_user, email, firstname_user, lastname_user, "
              + " phone_number, registration_date, password_user");
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
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return user;
  }

  @Override
  public UserDTO updateUser(UserDTO user) {
    Logger.logEntry("User DAO - updateUser" + user.getEmail());
    try {
      PreparedStatement ps = dalConn.getPS(
          "UPDATE InternshipManagement.users SET "
              + " lastname_user = ?, firstname_user = ?, email = ?, phone_number = ?,"
              + " password_user = ?, version = ?"
              + " WHERE id_user = ? AND version = ?"
              + " RETURNING *");
      ps.setString(1, user.getLastName());
      ps.setString(2, user.getFirstName());
      ps.setString(3, user.getEmail());
      ps.setString(4, user.getTelephoneNumber());
      ps.setString(5, user.getPassword());
      ps.setInt(6, user.getVersion() + 1);
      ps.setInt(7, user.getId());
      ps.setInt(8, user.getVersion());
      try (ResultSet resultSet = ps.executeQuery()) {
        if (resultSet.next()) {
          user = getResultSet(resultSet);
        } else {
          user = null;
        }
      }
      ps.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return user;
  }

  @Override
  public int getNumberOfStudentsWithInternship(String academicYear) {
    Logger.logEntry("User DAO - getNumberOfStudentsWithInternship" + academicYear);
    int nbStudents = 0;
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT COUNT(DISTINCT s.id_user) as number_of_students, ay.academic_year "
              + "FROM InternshipManagement.internship i "
              + "JOIN InternshipManagement.contacts c "
              + "ON i.contact = c.id_contacts "
              + "JOIN InternshipManagement.student s "
              + "ON c.student = s.id_user "
              + "JOIN InternshipManagement.academic_year ay "
              + "ON s.academic_year = ay.id_academic_year "
              + "WHERE ay.academic_year = ? "
              + "GROUP BY ay.academic_year;");
      ps.setString(1, academicYear);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          nbStudents = rs.getInt(1);
        }
      }
      ps.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return nbStudents;
  }

  @Override
  public int getNumberOfStudentsWithoutInternship(String academicYear) {
    Logger.logEntry("User DAO - getNumberOfStudentsWithoutInternship" + academicYear);
    int nbStudents = 0;
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT COUNT(*) as number_of_students_without_internship, ay.academic_year "
              + "FROM InternshipManagement.student s "
              + "JOIN InternshipManagement.academic_year ay "
              + "ON s.academic_year = ay.id_academic_year "
              + "WHERE ay.academic_year = ? "
              + "AND s.id_user NOT IN ("
              + "    SELECT DISTINCT c.student "
              + "    FROM InternshipManagement.internship i "
              + "    JOIN InternshipManagement.contacts c ON i.contact = c.id_contacts "
              + ") "
              + "GROUP BY ay.academic_year;");
      ps.setString(1, academicYear);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          nbStudents = rs.getInt(1);
        }
      }
      ps.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return nbStudents;
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
    user.setVersion(resultSet.getInt("version"));
    return user;
  }


}
