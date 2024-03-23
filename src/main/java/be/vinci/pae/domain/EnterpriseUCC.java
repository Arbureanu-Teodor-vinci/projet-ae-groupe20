package be.vinci.pae.domain;

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
}
