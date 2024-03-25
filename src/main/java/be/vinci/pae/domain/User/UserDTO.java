package be.vinci.pae.domain.User;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDate;

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
  void setFirstName(String firstName);

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
  String getTelephoneNumber();

  /**
   * Set telephone number.
   *
   * @param telephoneNbr integer
   */
  void setTelephoneNumber(String telephoneNbr);

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
   * Get registration date.
   *
   * @return the registration date
   */
  LocalDate getRegistrationDate();

  /**
   * Set registration date.
   *
   * @param registrationDate the registration date to set
   */
  void setRegistrationDate(LocalDate registrationDate);

  /**
   * Set registration date to now.
   */
  void setRegistrationDateToNow();

  /**
   * Get academic year.
   *
   * @return the academicYear String
   */
  String getAcademicYear();

  /**
   * Set academic year.
   *
   * @param academicYear the academicYear to set
   */
  void setAcademicYear(String academicYear);

  /**
   * Hash user password.
   */
  void hashPassword();

  /**
   * Set automatically role to student if email is @student.vinci.be.
   */
  void setRoleByEmail();

  @Override
  boolean equals(Object o);

  @Override
  int hashCode();

  @Override
  String toString();
}
