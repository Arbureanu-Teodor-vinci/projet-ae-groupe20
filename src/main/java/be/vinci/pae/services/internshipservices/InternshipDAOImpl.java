package be.vinci.pae.services.internshipservices;

import be.vinci.pae.api.filters.FatalException;
import be.vinci.pae.domain.academicyear.AcademicYearDTO;
import be.vinci.pae.domain.contact.ContactDTO;
import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.domain.internship.InternshipDTO;
import be.vinci.pae.domain.internshipsupervisor.SupervisorDTO;
import be.vinci.pae.domain.user.StudentDTO;
import be.vinci.pae.services.dal.DALServices;
import be.vinci.pae.utils.Logger;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of internship services.
 */
public class InternshipDAOImpl implements InternshipDAO {

  @Inject
  private DomainFactory domainFactory;

  @Inject
  private DALServices dalConn;

  @Override
  public InternshipDTO getOneInternshipById(int id) {
    Logger.logEntry("Internship DAO - getOneInternshipById" + id);
    InternshipDTO internship = null;

    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT i.id_internship, i.subject, i.signature_date, i.version as internship_version,\n"
              + "ai.id_academic_year as internship_id_academic_year, ai.academic_year as internship_academic_year,\n"
              + "isup.id_internship_supervisor, isup.last_name_supervisor, isup.first_name_supervisor, isup.phone_number as phone_number_supervisor,\n"
              + "isup.email as email_supervisor,\n"
              + "eisup.id_enterprise as supervisor_id_enterprise, eisup.trade_name as supervisor_trade_name_enterprise,\n"
              + "eisup.designation as supervisor_designation_enterprise, eisup.address as supervisor_address_enterprise,\n"
              + "eisup.phone_number as supervisor_phone_number_enterprise, eisup.city as supervisor_city_enterprise,\n"
              + "eisup.email as supervisor_email_enterprise, eisup.black_listed as supervisor_black_listed_enterprise,\n"
              + "eisup.black_listed_motivation as supervisor_black_listed_motivation_enterprise, eisup.version as supervisor_version_enterprise,\n"
              + "c.id_contacts, c.interview_method,c.tool, c.refusal_reason,c.state_contact, c.version as contact_version,\n"
              + "ec.id_enterprise as contact_id_enterprise, ec.trade_name as contact_trade_name_enterprise,\n"
              + "ec.designation as contact_designation_enterprise, ec.address as contact_address_enterprise,\n"
              + "ec.phone_number as contact_phone_number_enterprise, ec.city as contact_city_enterprise,\n"
              + "ec.email as contact_email_enterprise, ec.black_listed as contact_black_listed_enterprise,\n"
              + "ec.black_listed_motivation as contact_black_listed_motivation_enterprise, ec.version as contact_version_enterprise,\n"
              + "ac.id_academic_year as contact_id_academic_year, ac.academic_year as contact_academic_year,\n"
              + "s.id_user as contact_id_student, u.lastname_user as contact_lastname_student, u.firstname_user as contact_firstname_student,\n"
              + "u.email as contact_email_student, u.phone_number as contact_phone_number_student, u.role_user as contact_role_student,\n"
              + "u.registration_date as contact_registration_date_student, u.version as contact_version_student,\n"
              + "astu.id_academic_year as student_id_academic_year, astu.academic_year as student_academic_year\n"
              + "FROM InternshipManagement.internship i\n"
              + "JOIN internshipmanagement.academic_year ai on ai.id_academic_year = i.academic_year\n"
              + "JOIN InternshipManagement.internship_supervisor isup ON i.internship_supervisor = isup.id_internship_supervisor\n"
              + "JOIN InternshipManagement.enterprise eisup ON isup.enterprise = eisup.id_enterprise\n"
              + "JOIN InternshipManagement.contacts c ON i.contact = c.id_contacts\n"
              + "JOIN InternshipManagement.academic_year ac ON c.academic_year = ac.id_academic_year\n"
              + "JOIN InternshipManagement.enterprise ec ON c.enterprise = ec.id_enterprise\n"
              + "JOIN InternshipManagement.student s ON c.student = s.id_user\n"
              + "JOIN InternshipManagement.academic_year astu ON s.academic_year = astu.id_academic_year\n"
              + "JOIN InternshipManagement.users u ON s.id_user = u.id_user\n"
              + "WHERE i.id_internship = ?"
      );
      ps.setInt(1, id);

      try (ResultSet resultSet = ps.executeQuery()) {
        if (resultSet.next()) {
          internship = getResultSet(resultSet);
        }
      }
      ps.close();

    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }

    return internship;
  }

  @Override
  public InternshipDTO getOneInternshipByStudentId(int id) {
    Logger.logEntry("Internship DAO - getOneInternshipByStudentId" + id);
    InternshipDTO internship = null;

    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT i.id_internship, i.subject, i.signature_date, i.version as internship_version,\n"
              + "ai.id_academic_year as internship_id_academic_year, ai.academic_year as internship_academic_year,\n"
              + "isup.id_internship_supervisor, isup.last_name_supervisor, isup.first_name_supervisor, isup.phone_number as phone_number_supervisor,\n"
              + "isup.email as email_supervisor,\n"
              + "eisup.id_enterprise as supervisor_id_enterprise, eisup.trade_name as supervisor_trade_name_enterprise,\n"
              + "eisup.designation as supervisor_designation_enterprise, eisup.address as supervisor_address_enterprise,\n"
              + "eisup.phone_number as supervisor_phone_number_enterprise, eisup.city as supervisor_city_enterprise,\n"
              + "eisup.email as supervisor_email_enterprise, eisup.black_listed as supervisor_black_listed_enterprise,\n"
              + "eisup.black_listed_motivation as supervisor_black_listed_motivation_enterprise, eisup.version as supervisor_version_enterprise,\n"
              + "c.id_contacts, c.interview_method,c.tool, c.refusal_reason,c.state_contact, c.version as contact_version,\n"
              + "ec.id_enterprise as contact_id_enterprise, ec.trade_name as contact_trade_name_enterprise,\n"
              + "ec.designation as contact_designation_enterprise, ec.address as contact_address_enterprise,\n"
              + "ec.phone_number as contact_phone_number_enterprise, ec.city as contact_city_enterprise,\n"
              + "ec.email as contact_email_enterprise, ec.black_listed as contact_black_listed_enterprise,\n"
              + "ec.black_listed_motivation as contact_black_listed_motivation_enterprise, ec.version as contact_version_enterprise,\n"
              + "ac.id_academic_year as contact_id_academic_year, ac.academic_year as contact_academic_year,\n"
              + "s.id_user as contact_id_student, u.lastname_user as contact_lastname_student, u.firstname_user as contact_firstname_student,\n"
              + "u.email as contact_email_student, u.phone_number as contact_phone_number_student, u.role_user as contact_role_student,\n"
              + "u.registration_date as contact_registration_date_student, u.version as contact_version_student,\n"
              + "astu.id_academic_year as student_id_academic_year, astu.academic_year as student_academic_year\n"
              + "FROM InternshipManagement.internship i\n"
              + "JOIN internshipmanagement.academic_year ai on ai.id_academic_year = i.academic_year\n"
              + "JOIN InternshipManagement.internship_supervisor isup ON i.internship_supervisor = isup.id_internship_supervisor\n"
              + "JOIN InternshipManagement.enterprise eisup ON isup.enterprise = eisup.id_enterprise\n"
              + "JOIN InternshipManagement.contacts c ON i.contact = c.id_contacts\n"
              + "JOIN InternshipManagement.academic_year ac ON c.academic_year = ac.id_academic_year\n"
              + "JOIN InternshipManagement.enterprise ec ON c.enterprise = ec.id_enterprise\n"
              + "JOIN InternshipManagement.student s ON c.student = s.id_user\n"
              + "JOIN InternshipManagement.academic_year astu ON s.academic_year = astu.id_academic_year\n"
              + "JOIN InternshipManagement.users u ON s.id_user = u.id_user\n"
              + "WHERE s.id_user = ?"
      );
      ps.setInt(1, id);

      try (ResultSet resultSet = ps.executeQuery()) {
        if (resultSet.next()) {
          internship = getResultSet(resultSet);
        }

      }
      ps.close();

    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return internship;
  }

  @Override
  public InternshipDTO updateSubject(InternshipDTO internshipToUpdate) {
    Logger.logEntry("Internship DAO - updateSubject" + internshipToUpdate);
    InternshipDTO internship = null;

    try {
      PreparedStatement ps = dalConn.getPS(
          "UPDATE InternshipManagement.internship SET subject = ?, version = ? "
              + "WHERE id_internship = ? AND version = ? RETURNING *"
      );
      ps.setString(1, internshipToUpdate.getSubject());
      ps.setInt(2, internshipToUpdate.getVersion() + 1);
      ps.setInt(3, internshipToUpdate.getId());
      ps.setInt(4, internshipToUpdate.getVersion());

      try (ResultSet resultSet = ps.executeQuery()) {
        if (resultSet.next()) {
          internship = getOneInternshipById(resultSet.getInt("id_internship"));
        } else {
          if (getOneInternshipById(internshipToUpdate.getId()) == null) {
            throw new NullPointerException("Internship not found");
          } else {
            throw new FatalException(
                "Internship has already been updated by someone else. Please try again.");
          }
        }
      }
      ps.close();

    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return internship;
  }

  @Override
  public InternshipDTO addInternship(InternshipDTO internshipDTO) {
    Logger.logEntry("Internship DAO - addInternship" + internshipDTO);
    InternshipDTO internship = null;

    try {
      PreparedStatement ps = dalConn.getPS(
          "INSERT INTO InternshipManagement.internship "
              + "(subject, signature_date, internship_supervisor, contact, academic_year, version) "
              + "VALUES (?, ?, ?, ?, ?, 1) RETURNING *"
      );
      ps.setString(1, internshipDTO.getSubject());
      ps.setString(2, internshipDTO.getSignatureDate());
      ps.setInt(3, internshipDTO.getSupervisor().getId());
      ps.setInt(4, internshipDTO.getContact().getId());
      ps.setInt(5, internshipDTO.getAcademicYear().getId());

      try (ResultSet resultSet = ps.executeQuery()) {
        if (resultSet.next()) {
          internship = getOneInternshipById(resultSet.getInt("id_internship"));
        }
      }
      ps.close();

    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return internship;
  }

  @Override
  public int getNbInternships(int id) {
    Logger.logEntry("Internship DAO - getNbInternships");
    int nbInternships = 0;
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT COUNT(*) FROM InternshipManagement.internship i "
              + " JOIN InternshipManagement.contacts c ON i.contact = c.id_contacts "
              + " WHERE c.enterprise = ?"
      );
      ps.setInt(1, id);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          nbInternships = rs.getInt(1);
        }
      }
      ps.close();

    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return nbInternships;
  }

  @Override
  public int getNbInternshipsPerAcademicYear(int id, String academicYear) {
    Logger.logEntry("Internship DAO - getNbInternshipsPerAcademicYear");
    int nbInternships = 0;
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT COUNT(*) AS number_of_internships "
              + "FROM InternshipManagement.internship i "
              + "JOIN InternshipManagement.contacts c ON i.contact = c.id_contacts "
              + "JOIN InternshipManagement.enterprise e ON c.enterprise = e.id_enterprise "
              + "JOIN InternshipManagement.academic_year a ON i.academic_year = a.id_academic_year "
              + "WHERE e.id_enterprise = ? AND a.academic_year = ?;"
      );
      ps.setInt(1, id);
      ps.setString(2, academicYear);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          nbInternships = rs.getInt(1);
        }
      }
      ps.close();

    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return nbInternships;
  }

  private InternshipDTO getResultSet(ResultSet resultSet) throws SQLException {
    AcademicYearDTO studentAcademicYear = domainFactory.getAcademicYearDTO();
    studentAcademicYear.setId(resultSet.getInt("student_id_academic_year"));
    studentAcademicYear.setYear(resultSet.getString("student_academic_year"));

    StudentDTO contactStudent = domainFactory.getStudentDTO();
    contactStudent.setId(resultSet.getInt("contact_id_student"));
    contactStudent.setLastName(resultSet.getString("contact_lastname_student"));
    contactStudent.setFirstName(resultSet.getString("contact_firstname_student"));
    contactStudent.setEmail(resultSet.getString("contact_email_student"));
    contactStudent.setTelephoneNumber(resultSet.getString("contact_phone_number_student"));
    contactStudent.setRole(resultSet.getString("contact_role_student"));
    contactStudent.setRegistrationDate(
        resultSet.getDate("contact_registration_date_student").toLocalDate());
    contactStudent.setVersion(resultSet.getInt("contact_version_student"));
    contactStudent.setAcademicYear(studentAcademicYear);

    EnterpriseDTO contactEnterprise = domainFactory.getEnterpriseDTO();
    contactEnterprise.setId(resultSet.getInt("contact_id_enterprise"));
    contactEnterprise.setTradeName(resultSet.getString("contact_trade_name_enterprise"));
    contactEnterprise.setDesignation(resultSet.getString("contact_designation_enterprise"));
    contactEnterprise.setAddress(resultSet.getString("contact_address_enterprise"));
    contactEnterprise.setPhoneNumber(resultSet.getString("contact_phone_number_enterprise"));
    contactEnterprise.setCity(resultSet.getString("contact_city_enterprise"));
    contactEnterprise.setEmail(resultSet.getString("contact_email_enterprise"));
    contactEnterprise.setBlackListed(resultSet.getBoolean("contact_black_listed_enterprise"));
    contactEnterprise.setBlackListMotivation(
        resultSet.getString("contact_black_listed_motivation_enterprise"));
    contactEnterprise.setVersion(resultSet.getInt("contact_version_enterprise"));

    AcademicYearDTO contactAcademicYear = domainFactory.getAcademicYearDTO();
    contactAcademicYear.setId(resultSet.getInt("contact_id_academic_year"));
    contactAcademicYear.setYear(resultSet.getString("contact_academic_year"));

    ContactDTO internshipContact = domainFactory.getContactDTO();
    internshipContact.setId(resultSet.getInt("id_contacts"));
    internshipContact.setInterviewMethod(resultSet.getString("interview_method"));
    internshipContact.setTool(resultSet.getString("tool"));
    internshipContact.setRefusalReason(resultSet.getString("refusal_reason"));
    internshipContact.setStateContact(resultSet.getString("state_contact"));
    internshipContact.setVersion(resultSet.getInt("contact_version"));
    internshipContact.setStudent(contactStudent);
    internshipContact.setEnterprise(contactEnterprise);
    internshipContact.setAcademicYear(contactAcademicYear);

    AcademicYearDTO internshipAcademicYear = domainFactory.getAcademicYearDTO();
    internshipAcademicYear.setId(resultSet.getInt("internship_id_academic_year"));
    internshipAcademicYear.setYear(resultSet.getString("internship_academic_year"));

    EnterpriseDTO supervisorEnterprise = domainFactory.getEnterpriseDTO();
    supervisorEnterprise.setId(resultSet.getInt("supervisor_id_enterprise"));
    supervisorEnterprise.setTradeName(resultSet.getString("supervisor_trade_name_enterprise"));
    supervisorEnterprise.setDesignation(resultSet.getString("supervisor_designation_enterprise"));
    supervisorEnterprise.setAddress(resultSet.getString("supervisor_address_enterprise"));
    supervisorEnterprise.setPhoneNumber(resultSet.getString("supervisor_phone_number_enterprise"));
    supervisorEnterprise.setCity(resultSet.getString("supervisor_city_enterprise"));
    supervisorEnterprise.setEmail(resultSet.getString("supervisor_email_enterprise"));
    supervisorEnterprise.setBlackListed(resultSet.getBoolean("supervisor_black_listed_enterprise"));
    supervisorEnterprise.setBlackListMotivation(
        resultSet.getString("supervisor_black_listed_motivation_enterprise"));
    supervisorEnterprise.setVersion(resultSet.getInt("supervisor_version_enterprise"));

    SupervisorDTO internshipSupervisor = domainFactory.getSupervisorDTO();
    internshipSupervisor.setId(resultSet.getInt("id_internship_supervisor"));
    internshipSupervisor.setFirstName(resultSet.getString("first_name_supervisor"));
    internshipSupervisor.setLastName(resultSet.getString("last_name_supervisor"));
    internshipSupervisor.setPhoneNumber(resultSet.getString("phone_number_supervisor"));
    internshipSupervisor.setEmail(resultSet.getString("email_supervisor"));
    internshipSupervisor.setEnterprise(supervisorEnterprise);

    InternshipDTO internship = domainFactory.getInternshipDTO();
    internship.setId(resultSet.getInt("id_internship"));
    internship.setSubject(resultSet.getString("subject"));
    internship.setSignatureDate(resultSet.getString("signature_date"));
    internship.setSupervisor(internshipSupervisor);
    internship.setContact(internshipContact);
    internship.setAcademicYear(internshipAcademicYear);
    internship.setVersion(resultSet.getInt("internship_version"));
    return internship;
  }
}
