package be.vinci.pae.domain.user;

import be.vinci.pae.api.filters.BusinessException;
import java.time.LocalDate;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Implementation of User and UserDTO.
 */
public class UserImpl implements User {

  private static final String[] POSSIBLE_ROLES = {"Etudiant", "Professeur", "Administratif"};
  private int id;
  private String lastName;
  private String firstName;
  private String email;
  private String telephoneNumber;
  private String role;
  private String password;
  private LocalDate registrationDate;
  private int version; // version for optimistic lock


  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public void setFirstName(String fistName) {
    this.firstName = fistName;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String getTelephoneNumber() {
    return telephoneNumber;
  }

  @Override
  public void setTelephoneNumber(String telephoneNumber) {
    this.telephoneNumber = telephoneNumber;
  }

  @Override
  public String getRole() {
    return role;
  }

  @Override
  public void setRole(String role) {
    this.role = role;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public LocalDate getRegistrationDate() {
    return registrationDate;
  }

  @Override
  public void setRegistrationDate(LocalDate registrationDate) {
    this.registrationDate = registrationDate;
  }

  @Override
  public void setRegistrationDateToNow() {
    this.registrationDate = LocalDate.now();
  }

  @Override
  public int getVersion() {
    return version;
  }

  @Override
  public void setVersion(int version) {
    this.version = version;
  }

  @Override
  public void hashPassword() {
    this.password = BCrypt.hashpw(this.password, BCrypt.gensalt());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserImpl userDTO = (UserImpl) o;
    return id == userDTO.id;
  }

  @Override
  public int hashCode() {
    return id;
  }


  @Override
  public String toString() {
    return "UserImpl{"
        + "id=" + id
        + ", lastName='" + lastName + '\''
        + ", firstName='" + firstName + '\''
        + ", email='" + email + '\''
        + ", telephoneNumber=" + telephoneNumber
        + ", role='" + role + '\''
        + ", password='" + password + '\''
        + '}';
  }

  @Override
  public boolean checkPassword(String password) {
    if (!BCrypt.checkpw(password, this.password)) {
      throw new BusinessException("Password is incorrect.");
    }
    return true;
  }

  @Override
  public void checkVinciEmail(String email) {
    if (!email.endsWith("@vinci.be") && !email.endsWith("@student.vinci.be")) {
      throw new BusinessException("Email is not a vinci email.");
    }
  }

  @Override
  public void checkUniqueEmail(UserDTO userDTO) {
    if (userDTO != null) {
      throw new BusinessException("Email already exists.");
    }
  }

  @Override
  public void checkRole(String role) throws BusinessException {
    if (!role.equals(POSSIBLE_ROLES[0]) && !role.equals(POSSIBLE_ROLES[1]) && !role.equals(
        POSSIBLE_ROLES[2])) {
      throw new BusinessException("Role is not valid.");
    }

    if (!(role.equals(POSSIBLE_ROLES[0]) && email.endsWith("@student.vinci.be"))
        && !(role.equals(POSSIBLE_ROLES[1]) && email.endsWith("@vinci.be"))
        && !(role.equals(POSSIBLE_ROLES[2]) && email.endsWith("@vinci.be"))) {
      throw new BusinessException("Role and email combination is not valid.");
    }
  }

  @Override
  public void checkNotNull() {
    if (this.id == 0 && this.lastName == null && this.firstName == null && this.email == null
        && this.telephoneNumber == null && this.role == null && this.password == null
        && this.registrationDate == null) {
      throw new BusinessException("User is null.");
    }
  }

  @Override
  public void checkAcademicYear(String academicYear, List<String> academicYears) {
    if (!academicYears.contains(academicYear)) {
      throw new BusinessException("Academic year is not valid.");
    }
  }

}
