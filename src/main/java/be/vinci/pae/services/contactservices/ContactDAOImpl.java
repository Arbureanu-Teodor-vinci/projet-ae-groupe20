package be.vinci.pae.services.contactservices;

import be.vinci.pae.api.filters.FatalException;
import be.vinci.pae.domain.academicyear.AcademicYearDTO;
import be.vinci.pae.domain.contact.ContactDTO;
import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.domain.user.StudentDTO;
import be.vinci.pae.services.dal.DALServices;
import be.vinci.pae.utils.Logger;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of contact services.
 */
public class ContactDAOImpl implements ContactDAO {

  @Inject
  private DomainFactory domainFactory;

  // The DALServices is used to get a connection to the database.
  @Inject
  private DALServices dalConn;

  @Override
  public ContactDTO getOneContactByid(int id) {
    Logger.logEntry("Contact DAO - getOneContactByid" + id);
    ContactDTO contact = domainFactory.getContactDTO();

    try {

      // Create the prepared statement to get the contact by id
      PreparedStatement ps = dalConn.getPS(
          "SELECT c.id_contact, c.interview_method, c.tool, c.refusal_reason, c.state_contact,\n"
              + "       c.academic_year as contact_academic_year_id,\n"
              + "       a1.academic_year as contact_academic_year,\n"
              + "       c.version as contact_version,\n"
              + "       u.id_user, u.lastname_user, u.firstname_user, u.email as student_email,\n"
              + "       u.phone_number as student_phoneNumber, u.registration_date, u.role_user,\n"
              + "       u.password_user, u.version as user_version,\n"
              + "       s.academic_year as student_academic_year_id,\n"
              + "       a2.academic_year as student_academic_year,\n"
              + "       e.id_enterprise, e.trade_name, e.designation,\n"
              + "       e.phone_number as enterprise_phoneNumber,\n"
              + "       e.address, e.city,e.email as enterprise_email,\n"
              + "       e.black_listed, e.black_listed_motivation,\n"
              + "       e.version as enterprise_version\n"
              + "FROM InternshipManagement.contacts c\n"
              + "JOIN InternshipManagement.academic_years a1\n"
              + "    ON c.academic_year = a1.id_academic_year\n"
              + "JOIN InternshipManagement.users u ON c.student = u.id_user\n"
              + "JOIN InternshipManagement.enterprises e ON c.enterprise = e.id_enterprise\n"
              + "JOIN InternshipManagement.students s ON c.student = s.id_user\n"
              + "JOIN InternshipManagement.academic_years a2\n"
              + "    ON s.academic_year = a2.id_academic_year\n"
              + "WHERE c.id_contact = ?"
      );
      // Set the id of the contact in the prepared statement
      ps.setInt(1, id);

      // Execute the query
      try (ResultSet resultSet = ps.executeQuery()) {
        if (resultSet.next()) {
          // Get the contact from the result set
          // call the getResultSet method to get the contact in an object form
          contact = getResultSet(resultSet);
        }

      }
      // close the prepared statement
      ps.close();

    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      // close the connection
      dalConn.closeConnection();
    }
    // return the contact
    return contact;
  }

  @Override
  public List<ContactDTO> getAllContacts() {
    Logger.logEntry("Contact DAO - getAllContacts");
    List<ContactDTO> contacts = new ArrayList<>();
    try {
      // Create the prepared statement to get all the contacts
      PreparedStatement ps = dalConn.getPS(
          "SELECT c.id_contact, c.interview_method, c.tool, c.refusal_reason, c.state_contact,\n"
              + "       c.academic_year as contact_academic_year_id,\n"
              + "       a1.academic_year as contact_academic_year,\n"
              + "       c.version as contact_version,\n"
              + "       u.id_user, u.lastname_user, u.firstname_user, u.email as student_email,\n"
              + "       u.phone_number as student_phoneNumber, u.registration_date, u.role_user,\n"
              + "       u.password_user, u.version as user_version,\n"
              + "       s.academic_year as student_academic_year_id,\n"
              + "       a2.academic_year as student_academic_year,\n"
              + "       e.id_enterprise, e.trade_name, e.designation,\n"
              + "       e.phone_number as enterprise_phoneNumber,\n"
              + "       e.address, e.city,e.email as enterprise_email,\n"
              + "       e.black_listed, e.black_listed_motivation,\n"
              + "       e.version as enterprise_version\n"
              + "FROM InternshipManagement.contacts c\n"
              + "JOIN InternshipManagement.academic_years a1\n"
              + "   ON c.academic_year = a1.id_academic_year\n"
              + "JOIN InternshipManagement.users u ON c.student = u.id_user\n"
              + "JOIN InternshipManagement.enterprises e ON c.enterprise = e.id_enterprise\n"
              + "JOIN InternshipManagement.students s ON c.student = s.id_user\n"
              + "JOIN InternshipManagement.academic_years a2\n"
              + "    ON s.academic_year = a2.id_academic_year"
      );

      // Execute the query
      try (ResultSet resultSet = ps.executeQuery()) {
        while (resultSet.next()) {
          // Add the contact to the list
          contacts.add(getResultSet(resultSet));
        }
      }
      // close the prepared statement
      ps.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      // close the connection
      dalConn.closeConnection();
    }
    return contacts;
  }

  @Override
  public List<ContactDTO> getContactsByEnterprise(int id) {
    Logger.logEntry("Contact DAO - getContactsByEnterprise" + id);
    List<ContactDTO> contacts = new ArrayList<>();
    try {
      // Create the prepared statement to get the contacts by enterprise id
      PreparedStatement ps = dalConn.getPS(
          "SELECT c.id_contact, c.interview_method, c.tool, c.refusal_reason, c.state_contact,\n"
              + "       c.academic_year as contact_academic_year_id,\n"
              + "       a1.academic_year as contact_academic_year,\n"
              + "       c.version as contact_version,\n"
              + "       u.id_user, u.lastname_user, u.firstname_user, u.email as student_email,\n"
              + "       u.phone_number as student_phoneNumber, u.registration_date, u.role_user,\n"
              + "       u.password_user, u.version as user_version,\n"
              + "       s.academic_year as student_academic_year_id,\n"
              + "       a2.academic_year as student_academic_year,\n"
              + "       e.id_enterprise, e.trade_name, e.designation,\n"
              + "       e.phone_number as enterprise_phoneNumber,\n"
              + "       e.address, e.city,e.email as enterprise_email,\n"
              + "       e.black_listed, e.black_listed_motivation,\n"
              + "       e.version as enterprise_version\n"
              + "FROM InternshipManagement.contacts c\n"
              + "JOIN InternshipManagement.academic_years a1\n"
              + "   ON c.academic_year = a1.id_academic_year\n"
              + "JOIN InternshipManagement.users u ON c.student = u.id_user\n"
              + "JOIN InternshipManagement.enterprises e ON c.enterprise = e.id_enterprise\n"
              + "JOIN InternshipManagement.students s ON c.student = s.id_user\n"
              + "JOIN InternshipManagement.academic_years a2\n"
              + "    ON s.academic_year = a2.id_academic_year\n"
              + "WHERE c.enterprise = ?"
      );
      // Set the id of the enterprise in the prepared statement
      ps.setInt(1, id);

      // Execute the query
      try (ResultSet resultSet = ps.executeQuery()) {
        // For the amount of contacts
        while (resultSet.next()) {
          // Add the contact to the list
          contacts.add(getResultSet(resultSet));
        }
      }
      // close the prepared statement
      ps.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      // close the connection
      dalConn.closeConnection();
    }
    return contacts;
  }

  @Override
  public List<ContactDTO> getContactsByUser(int id) {
    Logger.logEntry("Contact DAO - getContactsByUser" + id);
    List<ContactDTO> contacts = new ArrayList<>();
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT c.id_contact, c.interview_method, c.tool, c.refusal_reason, c.state_contact,\n"
              + "       c.academic_year as contact_academic_year_id,\n"
              + "       a1.academic_year as contact_academic_year,\n"
              + "       c.version as contact_version,\n"
              + "       u.id_user, u.lastname_user, u.firstname_user, u.email as student_email,\n"
              + "       u.phone_number as student_phoneNumber, u.registration_date, u.role_user,\n"
              + "       u.password_user, u.version as user_version,\n"
              + "       s.academic_year as student_academic_year_id,\n"
              + "       a2.academic_year as student_academic_year,\n"
              + "       e.id_enterprise, e.trade_name, e.designation,\n"
              + "       e.phone_number as enterprise_phoneNumber,\n"
              + "       e.address, e.city,e.email as enterprise_email,\n"
              + "       e.black_listed, e.black_listed_motivation,\n"
              + "       e.version as enterprise_version\n"
              + "FROM InternshipManagement.contacts c\n"
              + "JOIN InternshipManagement.academic_years a1\n"
              + "   ON c.academic_year = a1.id_academic_year\n"
              + "JOIN InternshipManagement.users u ON c.student = u.id_user\n"
              + "JOIN InternshipManagement.enterprises e ON c.enterprise = e.id_enterprise\n"
              + "JOIN InternshipManagement.students s ON c.student = s.id_user\n"
              + "JOIN InternshipManagement.academic_years a2\n"
              + "    ON s.academic_year = a2.id_academic_year\n"
              + "WHERE c.student = ?"
      );
      ps.setInt(1, id);

      try (ResultSet resultSet = ps.executeQuery()) {
        while (resultSet.next()) {
          contacts.add(getResultSet(resultSet));
        }
      }
      ps.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return contacts;
  }

  @Override
  public ContactDTO addContact(int studentID, int enterpriseID, int academicYearID) {
    Logger.logEntry("Contact DAO - addContact");
    ContactDTO contact = domainFactory.getContactDTO();
    try {
      // Create the prepared statement to add a contact
      PreparedStatement ps = dalConn.getPS(
          "INSERT INTO InternshipManagement.contacts"
              + "(state_contact, student, academic_year, enterprise, version)"
              + " VALUES ('initié', ?, ?, ?, 1)"
              + " RETURNING *"
      );
      // Set the student id, enterprise id and academic year id in the prepared statement
      ps.setInt(1, studentID);
      ps.setInt(2, academicYearID);
      ps.setInt(3, enterpriseID);
      // Execute the query
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          // Get the contact from the result set
          contact = getOneContactByid(rs.getInt("id_contact"));
        }
      }
      ps.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return contact;
  }

  @Override
  public ContactDTO updateContact(ContactDTO contact) {
    Logger.logEntry("Contact DAO - updateContact" + contact);
    try {
      PreparedStatement ps = dalConn.getPS(
          "UPDATE InternshipManagement.contacts SET interview_method = ?, tool = ?,"
              + " refusal_reason = ?, state_contact = ?, version = ?"
              + " WHERE id_contact = ? AND version = ? RETURNING *"
      );
      ps.setString(1, contact.getInterviewMethod());
      ps.setString(2, contact.getTool());
      ps.setString(3, contact.getRefusalReason());
      ps.setString(4, contact.getStateContact());
      ps.setInt(5, contact.getVersion() + 1);
      ps.setInt(6, contact.getId());
      ps.setInt(7, contact.getVersion());
      try (ResultSet resultSet = ps.executeQuery()) {
        if (resultSet.next()) {
          contact = getOneContactByid(resultSet.getInt("id_contact"));
        } else {
          if (getOneContactByid(contact.getId()) == null) {
            throw new NullPointerException("Contact not found");
          } else {
            throw new FatalException(
                "Le contact a déjà été mis à jour par quelqu'un d'autre. Veuillez réessayer.");
          }
        }
      }
      ps.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return contact;
  }

  @Override
  public void updateAllContactsOfStudentToSuspended(int studentID) {
    Logger.logEntry("Contact DAO - updateAllContactsOfStudentToSuspended" + studentID);
    try {
      PreparedStatement ps = dalConn.getPS(
          "UPDATE InternshipManagement.contacts SET state_contact = 'suspendu'"
              + " WHERE student = ? AND state_contact != 'accepté'"
      );
      ps.setInt(1, studentID);
      ps.executeUpdate();
      ps.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
  }

  /**
   * Get the result set.
   *
   * @param resultSet ResultSet
   * @return ContactDTO
   * @throws SQLException SQLException
   */
  private ContactDTO getResultSet(ResultSet resultSet) throws SQLException {
    // Create academic year for student
    AcademicYearDTO academicYearStudent = domainFactory.getAcademicYearDTO();
    academicYearStudent.setId(resultSet.getInt("student_academic_year_id"));
    academicYearStudent.setYear(resultSet.getString("student_academic_year"));
    // Create academic year for contact
    AcademicYearDTO academicYearContact = domainFactory.getAcademicYearDTO();
    academicYearContact.setId(resultSet.getInt("contact_academic_year_id"));
    academicYearContact.setYear(resultSet.getString("contact_academic_year"));
    // Create student
    StudentDTO student = domainFactory.getStudentDTO();
    student.setId(resultSet.getInt("id_user"));
    student.setLastName(resultSet.getString("lastname_user"));
    student.setFirstName(resultSet.getString("firstname_user"));
    student.setEmail(resultSet.getString("student_email"));
    student.setTelephoneNumber(resultSet.getString("student_phoneNumber"));
    student.setRegistrationDate(resultSet.getDate("registration_date").toLocalDate());
    student.setRole(resultSet.getString("role_user"));
    student.setPassword(resultSet.getString("password_user"));
    student.setVersion(resultSet.getInt("user_version"));
    student.setAcademicYear(academicYearStudent);
    // Create enterprise
    EnterpriseDTO enterprise = domainFactory.getEnterpriseDTO();
    enterprise.setId(resultSet.getInt("id_enterprise"));
    enterprise.setTradeName(resultSet.getString("trade_name"));
    enterprise.setDesignation(resultSet.getString("designation"));
    enterprise.setAddress(resultSet.getString("address"));
    enterprise.setPhoneNumber(resultSet.getString("enterprise_phoneNumber"));
    enterprise.setCity(resultSet.getString("city"));
    enterprise.setEmail(resultSet.getString("enterprise_email"));
    enterprise.setBlackListed(resultSet.getBoolean("black_listed"));
    enterprise.setBlackListMotivation(resultSet.getString("black_listed_motivation"));
    enterprise.setVersion(resultSet.getInt("enterprise_version"));
    // Create contact with all the information
    ContactDTO contact = domainFactory.getContactDTO();
    contact.setId(resultSet.getInt("id_contact"));
    contact.setInterviewMethod(resultSet.getString("interview_method"));
    contact.setTool(resultSet.getString("tool"));
    contact.setRefusalReason(resultSet.getString("refusal_reason"));
    contact.setStateContact(resultSet.getString("state_contact"));
    contact.setAcademicYear(academicYearContact);
    contact.setStudent(student);
    contact.setEnterprise(enterprise);
    contact.setVersion(resultSet.getInt("contact_version"));
    return contact;
  }
}
