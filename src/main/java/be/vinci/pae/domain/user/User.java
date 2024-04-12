package be.vinci.pae.domain.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Interface of user for checking methods.
 */
@JsonDeserialize(as = UserImpl.class)
public interface User extends UserDTO {

  /**
   * Check if password is hashed.
   *
   * @param password -> password to check
   * @return true/false
   */
  boolean checkPassword(String password);

  /**
   * Check if email is a vinci email.
   *
   * @param email -> email to check
   * @return true/false
   */
  boolean checkVinciEmail(String email);

  /**
   * Check if email is unique. If empty, return true.
   *
   * @param userDTO -> user to check
   * @return true/false
   */
  boolean checkUniqueEmail(UserDTO userDTO);

  /**
   * Check if role is "Etudiant", "Professeur" or "Administratif".
   *
   * @param role -> role to check
   * @return true/false
   */
  boolean checkRole(String role);

}
