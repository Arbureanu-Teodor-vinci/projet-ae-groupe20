package be.vinci.pae.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Interface of user with setters and getters.
 */
@JsonDeserialize(as = UserImpl.class)
public interface UserDTO {

  /**
   * Get ID.
   *
   * @return id integer
   */
  int getId();

  /**
   * Set ID.
   *
   * @param id integer
   */
  void setId(int id);

  /**
   * Get last name.
   *
   * @return String
   */
  String getLastName();

  /**
   * Set name.
   *
   * @param lastName String
   */
  void setLastName(String lastName);

  /**
   * Get first name.
   *
   * @return String
   */
  String getFirstName();

  /**
   * Set first name.
   *
   * @param firstName String
   */
  void setFistName(String firstName);

  /**
   * Get email.
   *
   * @return String
   */
  String getEmail();

  /**
   * Set email.
   *
   * @param email String
   */
  void setEmail(String email);

  /**
   * Get telephone number.
   *
   * @return integer
   */
  int getTelephoneNumber();

  /**
   * Set telephone number.
   *
   * @param telephoneNbr integer
   */
  void setTelephoneNumber(int telephoneNbr);

  /**
   * Get role.
   *
   * @return String
   */
  String getRole();

  /**
   * Set role.
   *
   * @param role String
   */
  void setRole(String role);

  /**
   * Get password.
   *
   * @return String
   */
  String getPassword();

  /**
   * Set password.
   *
   * @param password String
   */
  void setPassword(String password);

  /**
   * Hash password.
   *
   * @param password String
   * @return String
   */
  String hashPassword(String password);

  @Override
  boolean equals(Object o);

  @Override
  int hashCode();

  @Override
  String toString();
}
