package be.vinci.pae.domain;

/**
 * Interface factory for each UCC.
 */
public interface DomainFactory {

  /**
   * Method returning a new user.
   *
   * @return UserDTO
   */
  UserDTO getUserDTO();

  /**
   * Method returning a new enterprise.
   *
   * @return a new EnterpriseDTO
   */
  EnterpriseDTO getEnterpriseDTO();
}
