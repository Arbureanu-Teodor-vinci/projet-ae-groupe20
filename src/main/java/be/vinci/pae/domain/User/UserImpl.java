package be.vinci.pae.domain.User;

import java.time.LocalDate;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Implementation of User and UserDTO.
 */
public class UserImpl implements User {

  private final String[] POSSIBLE_ROLES = {"Etudiant", "Professeur", "Administratif"};
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
  public void setRoleByEmail() {
    if (this.email.endsWith("@student.vinci.be")) {
      setRole(POSSIBLE_ROLES[0]);
    } else if (this.email.endsWith("@vinci.be")) {
      setRole(role);
    }
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
    return BCrypt.checkpw(password, this.password);
  }

  @Override
  public boolean checkVinciEmail(String email) {
    return email.endsWith("@vinci.be") || email.endsWith("@student.vinci.be");
  }

  @Override
  public boolean checkUniqueEmail(UserDTO userDTO) {
    return userDTO == null;
  }

  @Override
  public boolean checkRole(String role) {
    for (String possibleRole : POSSIBLE_ROLES) {
      if (role.equals(possibleRole)) {
        if (role.equals(POSSIBLE_ROLES[0]) && email.endsWith("@student.vinci.be")) {
          return true;
        } else if (role.equals(POSSIBLE_ROLES[1]) && email.endsWith("@vinci.be")) {
          return true;
        } else if (role.equals(POSSIBLE_ROLES[2]) && email.endsWith("@vinci.be")) {
          return true;
        } else {
          return false;
        }
      }
    }
    return false;
  }

}
