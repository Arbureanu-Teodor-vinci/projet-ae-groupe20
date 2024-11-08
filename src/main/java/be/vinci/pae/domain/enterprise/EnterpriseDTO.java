package be.vinci.pae.domain.enterprise;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Interface of enterprise with setters and getters.
 */
@JsonDeserialize(as = EnterpriseImpl.class)
public interface EnterpriseDTO {

  /**
   * Get ID.
   *
   * @return id integer
   */
  int getId();

  /**
   * set ID.
   *
   * @param id integer
   */
  void setId(int id);

  /**
   * Get tradeName.
   *
   * @return tradeName Sting
   */
  String getTradeName();

  /**
   * Set tradeName.
   *
   * @param tradeName String
   */
  void setTradeName(String tradeName);

  /**
   * Get designation.
   *
   * @return designation String
   */
  String getDesignation();

  /**
   * Set designation.
   *
   * @param designation String
   */
  void setDesignation(String designation);

  /**
   * Get address.
   *
   * @return adresse String
   */
  String getAddress();

  /**
   * Set address.
   *
   * @param address String
   */
  void setAddress(String address);

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
   * Get city.
   *
   * @return city String
   */
  String getCity();

  /**
   * Set city.
   *
   * @param city String
   */
  void setCity(String city);

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
   * Get isBlackListed.
   *
   * @return isBlackListed boolean
   */
  boolean isBlackListed();

  /**
   * Set isBlackListed.
   *
   * @param blackListed boolean
   */
  void setBlackListed(boolean blackListed);

  /**
   * Get blackListedMotivation.
   *
   * @return id integer
   */
  String getBlackListMotivation();

  /**
   * Set blackListMotivation.
   *
   * @param blackListMotivation integer
   */
  void setBlackListMotivation(String blackListMotivation);

  /**
   * Get version for optimistic lock.
   *
   * @return the version
   */
  int getVersion();

  /**
   * Set version for optimistic lock.
   *
   * @param version the version to set
   */
  void setVersion(int version);

  @Override
  boolean equals(Object o);

  @Override
  int hashCode();

  @Override
  String toString();
}
