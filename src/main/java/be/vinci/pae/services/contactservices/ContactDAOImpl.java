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

  @Inject
  private DALServices dalConn;

  @Override
  public ContactDTO getOneContactByid(int id) {
    Logger.logEntry("Contact DAO - getOneContactByid" + id);
    ContactDTO contact = domainFactory.getContactDTO();

    try {

      PreparedStatement ps = dalConn.getPS(
          "SELECT c.id_contacts, c.interview_method, c.tool, c.refusal_reason, c.state_contact,\n"
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
              + "JOIN InternshipManagement.academic_year a1\n"
              + "    ON c.academic_year = a1.id_academic_year\n"
              + "JOIN InternshipManagement.users u ON c.student = u.id_user\n"
              + "JOIN InternshipManagement.enterprise e ON c.enterprise = e.id_enterprise\n"
              + "JOIN InternshipManagement.student s ON c.student = s.id_user\n"
              + "JOIN InternshipManagement.academic_year a2\n"
              + "    ON s.academic_year = a2.id_academic_year\n"
              + "WHERE c.id_contacts = ?"
      );
      ps.setInt(1, id);

      try (ResultSet resultSet = ps.executeQuery()) {
        if (resultSet.next()) {
          contact = getResultSet(resultSet);
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
  public List<ContactDTO> getAllContacts() {
    Logger.logEntry("Contact DAO - getAllContacts");
    List<ContactDTO> contacts = new ArrayList<>();
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT c.id_contacts, c.interview_method, c.tool, c.refusal_reason, c.state_contact,\n"
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
              + "JOIN InternshipManagement.academic_year a1\n"
              + "   ON c.academic_year = a1.id_academic_year\n"
              + "JOIN InternshipManagement.users u ON c.student = u.id_user\n"
              + "JOIN InternshipManagement.enterprise e ON c.enterprise = e.id_enterprise\n"
              + "JOIN InternshipManagement.student s ON c.student = s.id_user\n"
              + "JOIN InternshipManagement.academic_year a2\n"
              + "    ON s.academic_year = a2.id_academic_year"
      );

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
  public List<ContactDTO> getContactsByEnterprise(int id) {
    Logger.logEntry("Contact DAO - getContactsByEnterprise" + id);
    List<ContactDTO> contacts = new ArrayList<>();
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT c.id_contacts, c.interview_method, c.tool, c.refusal_reason, c.state_contact,\n"
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
              + "JOIN InternshipManagement.academic_year a1\n"
              + "   ON c.academic_year = a1.id_academic_year\n"
              + "JOIN InternshipManagement.users u ON c.student = u.id_user\n"
              + "JOIN InternshipManagement.enterprise e ON c.enterprise = e.id_enterprise\n"
              + "JOIN InternshipManagement.student s ON c.student = s.id_user\n"
              + "JOIN InternshipManagement.academic_year a2\n"
              + "    ON s.academic_year = a2.id_academic_year\n"
              + "WHERE c.enterprise = ?"
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
  public List<ContactDTO> getContactsByUser(int id) {
    Logger.logEntry("Contact DAO - getContactsByUser" + id);
    List<ContactDTO> contacts = new ArrayList<>();
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT c.id_contacts, c.interview_method, c.tool, c.refusal_reason, c.state_contact,\n"
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
              + "JOIN InternshipManagement.academic_year a1\n"
              + "   ON c.academic_year = a1.id_academic_year\n"
              + "JOIN InternshipManagement.users u ON c.student = u.id_user\n"
              + "JOIN InternshipManagement.enterprise e ON c.enterprise = e.id_enterprise\n"
              + "JOIN InternshipManagement.student s ON c.student = s.id_user\n"
              + "JOIN InternshipManagement.academic_year a2\n"
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
      PreparedStatement ps = dalConn.getPS(
          "INSERT INTO InternshipManagement.contacts"
              + "(state_contact, student, academic_year, enterprise, version)"
              + " VALUES ('initié', ?, ?, ?, 1)"
              + " RETURNING *"
      );
      ps.setInt(1, studentID);
      ps.setInt(2, academicYearID);
      ps.setInt(3, enterpriseID);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          contact = getOneContactByid(rs.getInt("id_contacts"));
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
              + " WHERE id_contacts = ? AND version = ? RETURNING *"
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
          contact = getOneContactByid(resultSet.getInt("id_contacts"));
        } else {
          if (getOneContactByid(contact.getId()) == null) {
            throw new NullPointerException("Contact not found");
          } else {
            throw new FatalException(
                "Contact has already been updated by someone else. Please try again.");
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
    contact.setId(resultSet.getInt("id_contacts"));
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
