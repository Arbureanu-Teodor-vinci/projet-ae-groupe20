package be.vinci.pae.services.internshipsupervisorservices;

import be.vinci.pae.api.filters.FatalException;
import be.vinci.pae.domain.enterprise.EnterpriseDTO;
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
          """
              SELECT isup.id_internship_supervisor, isup.last_name_supervisor,
                     isup.first_name_supervisor,
                     isup.phone_number as phone_number_supervisor, isup.email as email_supervisor,
                     e.id_enterprise, e.trade_name, e.designation, e.address,
                     e.phone_number as phone_number_enterprise, e.city, e.email as email_enterprise,
                     e.black_listed,e.black_listed_motivation, e.version as version_enterprise
              FROM InternshipManagement.enterprise e,
                   InternshipManagement.internship_supervisor isup
              WHERE e.id_enterprise = isup.enterprise"""
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
          """
              SELECT isup.id_internship_supervisor, isup.last_name_supervisor,
                     isup.first_name_supervisor,
                     isup.phone_number as phone_number_supervisor, isup.email as email_supervisor,
                     e.id_enterprise, e.trade_name, e.designation, e.address,
                     e.phone_number as phone_number_enterprise, e.city, e.email as email_enterprise,
                     e.black_listed,e.black_listed_motivation, e.version as version_enterprise
              FROM InternshipManagement.enterprise e,
                   InternshipManagement.internship_supervisor isup
              WHERE e.id_enterprise = isup.enterprise
              AND isup.id_internship_supervisor = ?"""
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
  public List<SupervisorDTO> getSupervisorsByEnterprise(int idEnterprise) {
    Logger.logEntry("Supervisor DAO - getSupervisorsByEnterprise" + idEnterprise);
    List<SupervisorDTO> internshipSupervisors = new ArrayList<>();
    try {
      PreparedStatement ps = dalConn.getPS(
          """
              SELECT isup.id_internship_supervisor, isup.last_name_supervisor,
                     isup.first_name_supervisor,
                     isup.phone_number as phone_number_supervisor, isup.email as email_supervisor,
                     e.id_enterprise, e.trade_name, e.designation, e.address,
                     e.phone_number as phone_number_enterprise, e.city, e.email as email_enterprise,
                     e.black_listed,e.black_listed_motivation, e.version as version_enterprise
              FROM InternshipManagement.enterprise e,
                   InternshipManagement.internship_supervisor isup
              WHERE e.id_enterprise = isup.enterprise
              AND isup.enterprise = ?"""
      );
      ps.setInt(1, idEnterprise);
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
  public SupervisorDTO getOneSupervisorByEmail(String email) {
    Logger.logEntry("Supervisor DAO - getOneSupervisorByEmail" + email);
    SupervisorDTO internshipSupervisor = null;
    try {
      PreparedStatement ps = dalConn.getPS(
          """
              SELECT isup.id_internship_supervisor, isup.last_name_supervisor,
                     isup.first_name_supervisor,
                     isup.phone_number as phone_number_supervisor, isup.email as email_supervisor,
                     e.id_enterprise, e.trade_name, e.designation, e.address,
                     e.phone_number as phone_number_enterprise, e.city, e.email as email_enterprise,
                     e.black_listed,e.black_listed_motivation, e.version as version_enterprise
              FROM InternshipManagement.enterprise e,
                   InternshipManagement.internship_supervisor isup
              WHERE e.id_enterprise = isup.enterprise
              AND isup.email = ?"""
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
      ps.setInt(5, supervisor.getEnterprise().getId());
      try (ResultSet resultSet = ps.executeQuery()) {
        if (resultSet.next()) {
          supervisor = getOneSupervisorById(resultSet.getInt("id_internship_supervisor"));
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
    EnterpriseDTO enterprise = domainFactory.getEnterpriseDTO();
    enterprise.setId(resultSet.getInt("id_enterprise"));
    enterprise.setTradeName(resultSet.getString("trade_name"));
    enterprise.setDesignation(resultSet.getString("designation"));
    enterprise.setAddress(resultSet.getString("address"));
    enterprise.setPhoneNumber(resultSet.getString("phone_number_enterprise"));
    enterprise.setCity(resultSet.getString("city"));
    enterprise.setEmail(resultSet.getString("email_enterprise"));
    enterprise.setBlackListed(resultSet.getBoolean("black_listed"));
    enterprise.setBlackListMotivation(resultSet.getString("black_listed_motivation"));
    enterprise.setVersion(resultSet.getInt("version_enterprise"));

    SupervisorDTO internshipSupervisor = domainFactory.getSupervisorDTO();
    internshipSupervisor.setId(resultSet.getInt("id_internship_supervisor"));
    internshipSupervisor.setFirstName(resultSet.getString("last_name_supervisor"));
    internshipSupervisor.setLastName(resultSet.getString("first_name_supervisor"));
    internshipSupervisor.setEmail(resultSet.getString("email_supervisor"));
    internshipSupervisor.setPhoneNumber(resultSet.getString("phone_number_supervisor"));
    internshipSupervisor.setEnterprise(enterprise);
    return internshipSupervisor;
  }
}
