package be.vinci.pae.services;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.EnterpriseDTO;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnterpriseDAOImpl implements EnterpriseDAO {

  @Inject
  private DomainFactory domainFactory;

  @Inject
  private DALServices dalConn;


  @Override
  public EnterpriseDTO getOneEnterpriseByid(int id) {
    EnterpriseDTO enterprise = null;

    try {

      PreparedStatement ps = dalConn.getPS(
          "SELECT * FROM InternshipManagement.enterprise WHERE id_enterprise = ?"
      );
      ps.setInt(1, id);

      try (ResultSet resultSet = ps.executeQuery()) {
        if (resultSet.next()) {
          enterprise = domainFactory.getEnterpriseDTO();
          enterprise.setId(resultSet.getInt(1));
          enterprise.setTradeName(resultSet.getString(2));
          enterprise.setDesignation(resultSet.getString(3));
          enterprise.setAdresse(resultSet.getString(4));
          enterprise.setPhoneNumber(resultSet.getString(5));
          enterprise.setEmail(resultSet.getString(6));
          enterprise.setBlackListed(resultSet.getBoolean(7));
          enterprise.setBlackListMotivation(resultSet.getString(8));
        }

      }
      ps.close();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return enterprise;
  }

}
