package be.vinci.pae.services.enterpriseservices;

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
      Logger.logEntry("Error in EnterpriseDAOImpl getOneEnterpriseByid");
      throw new RuntimeException(e);
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
      Logger.logEntry("Error in EnterpriseDAOImpl getAllEnterprises");
      throw new RuntimeException(e);
    }
    return enterprises;
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
    enterprise.setAdresse(resultSet.getString(4));
    enterprise.setPhoneNumber(resultSet.getString(5));
    enterprise.setEmail(resultSet.getString(6));
    enterprise.setBlackListed(resultSet.getBoolean(7));
    enterprise.setBlackListMotivation(resultSet.getString(8));
    return enterprise;
  }

}
