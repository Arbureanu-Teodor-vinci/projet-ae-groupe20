package be.vinci.pae.domain.internshipsupervisor;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Interface of SupervisorDTO for getters and setters.
 */
@JsonDeserialize(as = SupervisorImpl.class)
public interface SupervisorDTO {

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
   * Get firstName.
   *
   * @return firstName String
   */
  String getFirstName();

  /**
   * Set firstName.
   *
   * @param firstName String
   */
  void setFirstName(String firstName);

  /**
   * Get lastName.
   *
   * @return lastName String
   */
  String getLastName();

  /**
   * Set lastName.
   *
   * @param lastName String
   */
  void setLastName(String lastName);

  /**
   * Get email.
   *
   * @return email String
   */
  String getEmail();

  /**
   * Set email.
   *
   * @param email String
   */
  void setEmail(String email);

  /**
   * Get phoneNumber.
   *
   * @return phoneNumber String
   */
  String getPhoneNumber();

  /**
   * Set phoneNumber.
   *
   * @param phoneNumber String
   */
  void setPhoneNumber(String phoneNumber);

  /**
   * Get enterpriseId.
   *
   * @return enterpriseId integer
   */
  int getEnterpriseId();

  /**
   * Set enterpriseId.
   *
   * @param enterpriseId integer
   */
  void setEnterpriseId(int enterpriseId);

  @Override
  boolean equals(Object o);

  @Override
  int hashCode();

  @Override
  String toString();
}