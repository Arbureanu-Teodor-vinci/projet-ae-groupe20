package be.vinci.pae.services.internshipservices;

import be.vinci.pae.api.filters.FatalException;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.domain.internship.InternshipDTO;
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
          "SELECT * FROM InternshipManagement.internship WHERE id_internship = ?"
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
          "SELECT i.* "
              + "FROM InternshipManagement.internship AS i "
              + "INNER JOIN InternshipManagement.contacts AS c ON i.contact = c.id_contacts "
              + "INNER JOIN InternshipManagement.student AS s ON c.student = s.id_user "
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
          internship = getResultSet(resultSet);
        } else {
          PreparedStatement ps2 = dalConn.getPS(
              "SELECT * FROM InternshipManagement.internship WHERE id_internship = ?"
          );
          try (ResultSet resultSet2 = ps2.executeQuery()) {
            if (!resultSet2.next()) {
              throw new FatalException("Internship not found");
            }
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
          "INSERT INTO InternshipManagement.internship (subject, signature_date, internship_supervisor, contact, academic_year, version) "
              + "VALUES (?, ?, ?, ?, ?, 1) RETURNING *"
      );
      ps.setString(1, internshipDTO.getSubject());
      ps.setString(2, internshipDTO.getSignatureDate());
      ps.setInt(3, internshipDTO.getSupervisorId());
      ps.setInt(4, internshipDTO.getContactId());
      ps.setInt(5, internshipDTO.getAcademicYear());

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

  private InternshipDTO getResultSet(ResultSet resultSet) throws SQLException {
    InternshipDTO internship = domainFactory.getInternshipDTO();
    internship.setId(resultSet.getInt(1));
    internship.setSubject(resultSet.getString(2));
    internship.setSignatureDate(resultSet.getString(3));
    internship.setSupervisorId(resultSet.getInt(4));
    internship.setContactId(resultSet.getInt(5));
    internship.setAcademicYear(resultSet.getInt(6));
    internship.setVersion(resultSet.getInt(7));
    return internship;
  }
}
