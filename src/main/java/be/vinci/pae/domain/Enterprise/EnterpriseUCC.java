package be.vinci.pae.domain.Enterprise;

import java.util.List;

/**
 * Interface of enterpriseUCC.
 */
public interface EnterpriseUCC {

  /**
   * Get one enterprise by id.
   *
   * @param id int
   * @return EnterpriseDTO
   */
  EnterpriseDTO getOneEnterprise(int id);

  /**
   * Get all enterprises.
   *
   * @return List of EnterpriseDTO
   */
  List<EnterpriseDTO> getAllEnterprises();
}
