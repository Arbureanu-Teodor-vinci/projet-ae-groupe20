package be.vinci.pae.services.internshipsupervisorservices;

import be.vinci.pae.api.filters.FatalException;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.domain.internshipsupervisor.SupervisorDTO;
import be.vinci.pae.services.dal.DALServices;
import be.vinci.pae.utils.Logger;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of supervisor service.
 */
public class SupervisorDAOImpl implements SupervisorDAO {

  @Inject
  private DomainFactory domainFactory;
  @Inject
  private DALServices dalConn;

  @Override
  public List<SupervisorDTO> getAllSupervisors() {
    Logger.logEntry("Supervisor DAO - getAllSupervisors");
    List<SupervisorDTO> internshipSupervisors = new ArrayList<>();
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT * FROM InternshipManagement.internship_supervisor"
      );
      try (ResultSet resultSet = ps.executeQuery()) {
        while (resultSet.next()) {
          internshipSupervisors.add(getResultSet(resultSet));
        }
      }
      ps.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return internshipSupervisors;
  }

  @Override
  public SupervisorDTO getOneSupervisorById(int id) {
    Logger.logEntry("Supervisor DAO - getOneSupervisorById" + id);
    SupervisorDTO internshipSupervisor = null;
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT * FROM InternshipManagement.internship_supervisor "
              + "WHERE id_internship_supervisor = ?"
      );
      ps.setInt(1, id);
      try (ResultSet resultSet = ps.executeQuery()) {
        if (resultSet.next()) {
          internshipSupervisor = getResultSet(resultSet);
        }
      }
      ps.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return internshipSupervisor;
  }

  @Override
  public SupervisorDTO getOneSupervisorByEmail(String email) {
    Logger.logEntry("Supervisor DAO - getOneSupervisorByEmail" + email);
    SupervisorDTO internshipSupervisor = null;
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT * FROM InternshipManagement.internship_supervisor WHERE email = ?"
      );
      ps.setString(1, email);
      try (ResultSet resultSet = ps.executeQuery()) {
        if (resultSet.next()) {
          internshipSupervisor = getResultSet(resultSet);
        }
      }
      ps.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return internshipSupervisor;
  }

  @Override
  public SupervisorDTO addSupervisor(SupervisorDTO supervisor) {
    Logger.logEntry("Supervisor DAO - addSupervisor");
    try {
      PreparedStatement ps = dalConn.getPS(
          "INSERT INTO InternshipManagement.internship_supervisor "
              + "(last_name_supervisor, first_name_supervisor, email, phone_number, enterprise) "
              + "VALUES (?, ?, ?, ?, ?) RETURNING *"
      );
      ps.setString(1, supervisor.getFirstName());
      ps.setString(2, supervisor.getLastName());
      ps.setString(3, supervisor.getEmail());
      ps.setString(4, supervisor.getPhoneNumber());
      ps.setInt(5, supervisor.getEnterpriseId());
      try (ResultSet resultSet = ps.executeQuery()) {
        if (resultSet.next()) {
          supervisor = getResultSet(resultSet);
        }
      }
      ps.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return supervisor;
  }

  /**
   * Get the result set of the corresponding supervisor.
   *
   * @param resultSet the result set
   * @return InternshipSupervisorDTO
   * @throws SQLException the SQL exception
   */
  private SupervisorDTO getResultSet(ResultSet resultSet) throws SQLException {
    SupervisorDTO internshipSupervisor = domainFactory.getSupervisorDTO();
    internshipSupervisor.setId(resultSet.getInt(1));
    internshipSupervisor.setFirstName(resultSet.getString(2));
    internshipSupervisor.setLastName(resultSet.getString(3));
    internshipSupervisor.setEmail(resultSet.getString(4));
    internshipSupervisor.setPhoneNumber(resultSet.getString(5));
    internshipSupervisor.setEnterpriseId(resultSet.getInt(6));
    return internshipSupervisor;
  }
}
