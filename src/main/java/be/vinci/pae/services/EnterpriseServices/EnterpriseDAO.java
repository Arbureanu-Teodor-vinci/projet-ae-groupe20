package be.vinci.pae.services.EnterpriseServices;

import be.vinci.pae.domain.Enterprise.EnterpriseDTO;
import java.util.List;

/**
 * Services of enterprise.
 */
public interface EnterpriseDAO {

  /**
   * Get one enterprise by id from DB.
   *
   * @param id integer
   * @return enterpriseDTO
   */
  EnterpriseDTO getOneEnterpriseByid(int id);

  /**
   * Get all enterprises from DB.
   *
   * @return List of EnterpriseDTO
   */
  List<EnterpriseDTO> getAllEnterprises();
}
