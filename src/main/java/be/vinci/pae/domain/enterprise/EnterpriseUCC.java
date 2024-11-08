package be.vinci.pae.domain.enterprise;

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

  /**
   * Add an enterprise.
   *
   * @param enterprise EnterpriseDTO
   * @return EnterpriseDTO
   */
  EnterpriseDTO addEnterprise(EnterpriseDTO enterprise);

  /**
   * Blacklist an enterprise.
   *
   * @param enterprise EnterpriseDTO
   * @return boolean
   */
  EnterpriseDTO blacklistEnterprise(EnterpriseDTO enterprise);
}
