package be.vinci.pae.domain;

/**
 * Interface of user for checking methods.
 */
public interface User extends UserDTO {

  /**
   * Check if password is hashed.
   *
   * @param password -> password to check
   * @return true/false
   */
  boolean checkPassword(String password);

}
