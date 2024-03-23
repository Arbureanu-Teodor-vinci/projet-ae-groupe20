package be.vinci.pae.services;

import be.vinci.pae.domain.EnterpriseDTO;

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
}
