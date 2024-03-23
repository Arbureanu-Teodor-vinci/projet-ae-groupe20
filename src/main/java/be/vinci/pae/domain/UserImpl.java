package be.vinci.pae.domain;

import java.time.LocalDate;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Implementation of User and UserDTO.
 */
public class UserImpl implements User {

  private static final String[] POSSIBLE_ROLES = {"étudiant", "professeur", "administratif"};
  private int id;
  private String lastName;
  private String firstName;
  private String email;
  private String telephoneNumber;
  private String role;
  private String password;
  private LocalDate registrationDate;

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
  public void setFistName(String fistName) {
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
    if (role.equals(POSSIBLE_ROLES[0]) || role.equals(POSSIBLE_ROLES[1]) || role.equals(
        POSSIBLE_ROLES[2])) {
      this.role = role;
    }
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
  public String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
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
    return BCrypt.checkpw(password, this.password);
  }
}
