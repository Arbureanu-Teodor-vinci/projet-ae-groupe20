package be.vinci.pae.services.enterpriseservices;

import be.vinci.pae.domain.enterprise.EnterpriseDTO;
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

  /**
   * Get the number of interships in an enterprise.
   *
   * @param id integer
   * @return integer
   */
  int getNbInternships(int id);

  /**
   * Add an enterprise to the DB.
   *
   * @param enterprise EnterpriseDTO
   * @return EnterpriseDTO
   */
  EnterpriseDTO addEnterprise(EnterpriseDTO enterprise);
}
