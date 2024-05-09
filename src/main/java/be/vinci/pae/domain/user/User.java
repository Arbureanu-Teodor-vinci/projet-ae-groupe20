package be.vinci.pae.domain.user;

import be.vinci.pae.api.filters.BusinessException;
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
   * @return true if password is hashed
   */
  boolean checkPassword(String password) throws BusinessException;

  /**
   * Check if email is a vinci email.
   *
   * @param email -> email to check
   */
  void checkVinciEmail(String email) throws BusinessException;

  /**
   * Check if email is unique. If empty, return true.
   *
   * @param userDTO -> user to check
   */
  void checkUniqueEmail(UserDTO userDTO) throws BusinessException;

  /**
   * Check if role is "Etudiant", "Professeur" or "Administratif".
   *
   * @param role -> role to check
   */
  void checkRole(String role) throws BusinessException;

  /**
   * Check if object is not null.
   *
   * @throws BusinessException -> if not null
   */
  void checkNotNull() throws BusinessException;

  /**
   * Check if phone number is valid.
   *
   * @param phoneNumber -> phone number to check
   */
  void checkPhoneNumberFormat(String phoneNumber) throws BusinessException;

  /**
   * Check if names are valid.
   *
   * @param name -> name to check
   */
  void checkNamesFormat(String name) throws BusinessException;

  /**
   * Check if the user is a student.
   *
   * @return true if the user is a student
   */
  boolean checkIsStudent();
}
