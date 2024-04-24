package be.vinci.pae.domain.internshipsupervisor;

import be.vinci.pae.api.filters.BusinessException;

/**
 * Interface of Supervisor for business checks.
 */
public interface Supervisor extends SupervisorDTO {

  /**
   * Check if the email is unique.
   *
   * @param supervisorDTO SupervisorDTO
   */
  void checkUniqueEmail(SupervisorDTO supervisorDTO) throws BusinessException;

  /**
   * Check if the phone number is valid.
   *
   * @param phoneNumber String
   */
  void checkPhoneNumberFormat(String phoneNumber) throws BusinessException;

  /**
   * Check if the names start with capitals letters and don't contain special characters.
   *
   * @param name String
   */
  void checkNamesFormat(String name) throws BusinessException;

  /**
   * Check if the email is a valid email.
   *
   * @param email String
   */
  void checkEmailFormat(String email) throws BusinessException;
}
