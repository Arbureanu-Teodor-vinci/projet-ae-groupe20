package be.vinci.pae.services.enterpriseservices;

import be.vinci.pae.api.filters.FatalException;
import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.services.dal.DALServices;
import be.vinci.pae.utils.Logger;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of enterprise services.
 */
public class EnterpriseDAOImpl implements EnterpriseDAO {

  @Inject
  private DomainFactory domainFactory;

  @Inject
  private DALServices dalConn;


  @Override
  public EnterpriseDTO getOneEnterpriseByid(int id) {
    Logger.logEntry("Enterprise DAO - getOneEnterpriseByid" + id);
    EnterpriseDTO enterprise = null;

    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT * FROM InternshipManagement.enterprise WHERE id_enterprise = ?"
      );
      ps.setInt(1, id);

      try (ResultSet resultSet = ps.executeQuery()) {
        if (resultSet.next()) {
          enterprise = getResultSet(resultSet);
        }

      }
      ps.close();

    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return enterprise;
  }

  @Override
  public List<EnterpriseDTO> getAllEnterprises() {
    Logger.logEntry("Enterprise DAO - getAllEnterprises");
    List<EnterpriseDTO> enterprises = new ArrayList<>();
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT * FROM InternshipManagement.enterprise"
      );
      try (ResultSet resultSet = ps.executeQuery()) {
        while (resultSet.next()) {
          enterprises.add(getResultSet(resultSet));
        }
      }
      ps.close();

    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return enterprises;
  }

  @Override
  public int getNbInternships(int id) {
    Logger.logEntry("Enterprise DAO - getNbInternships");
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
  public EnterpriseDTO addEnterprise(EnterpriseDTO enterprise) {
    Logger.logEntry("Enterprise DAO - addEnterprise");
    EnterpriseDTO nEnterprise = domainFactory.getEnterpriseDTO();

    try {
      PreparedStatement ps = dalConn.getPS(
          "INSERT INTO InternshipManagement.enterprise (trade_name, designation, address, "
              + "phone_number, city, email, version) VALUES (?, ?, ?, ?, ?, ?, 1) "
              + "RETURNING *"
      );
      ps.setString(1, enterprise.getTradeName());
      ps.setString(2, enterprise.getDesignation());
      ps.setString(3, enterprise.getAddress());
      ps.setString(4, enterprise.getPhoneNumber());
      ps.setString(5, enterprise.getCity());
      ps.setString(6, enterprise.getEmail());

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          nEnterprise = getResultSet(rs);
        }
      }
      ps.close();

    } catch (SQLException e) {

      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return nEnterprise;
  }

  @Override
  public EnterpriseDTO updateEnterprise(EnterpriseDTO enterprise) {
    Logger.logEntry("Enterprise DAO - updateEnterprise");
    try {
      PreparedStatement ps = dalConn.getPS(
          "UPDATE InternshipManagement.enterprise SET trade_name = ?, designation = ?,"
              + " address = ?, phone_number = ?, city = ?, email = ?, black_listed = ?,"
              + " black_listed_motivation = ?, version = ?"
              + " WHERE id_enterprise = ? AND version = ? RETURNING *"
      );
      ps.setString(1, enterprise.getTradeName());
      ps.setString(2, enterprise.getDesignation());
      ps.setString(3, enterprise.getAddress());
      ps.setString(4, enterprise.getPhoneNumber());
      ps.setString(5, enterprise.getCity());
      ps.setString(6, enterprise.getEmail());

      ps.setBoolean(7, enterprise.isBlackListed());
      ps.setString(8, enterprise.getBlackListMotivation());
      ps.setInt(9, enterprise.getVersion() + 1);
      ps.setInt(10, enterprise.getId());
      ps.setInt(11, enterprise.getVersion());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          enterprise = getResultSet(rs);
        } else {
          if (getOneEnterpriseByid(enterprise.getId()) == null) {
            throw new NullPointerException("Enterprise not found");
          } else {
            throw new FatalException("This enterprise has been updated by another user");
          }
        }
      }
      ps.close();

    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return enterprise;
  }

  /**
   * Get the result set.
   *
   * @param resultSet ResultSet
   * @return EnterpriseDTO
   * @throws SQLException SQLException
   */
  private EnterpriseDTO getResultSet(ResultSet resultSet) throws SQLException {
    // creating a new enterpriseDTO to stock the information
    EnterpriseDTO enterprise = domainFactory.getEnterpriseDTO();
    // using the result set, setting the attribut of the enterprise
    enterprise.setId(resultSet.getInt(1));
    enterprise.setTradeName(resultSet.getString(2));
    enterprise.setDesignation(resultSet.getString(3));
    enterprise.setAddress(resultSet.getString(4));
    enterprise.setPhoneNumber(resultSet.getString(5));
    enterprise.setCity(resultSet.getString(6));
    enterprise.setEmail(resultSet.getString(7));
    enterprise.setBlackListed(resultSet.getBoolean(8));
    enterprise.setBlackListMotivation(resultSet.getString(9));
    enterprise.setVersion(resultSet.getInt("version"));
    return enterprise;
  }

}
