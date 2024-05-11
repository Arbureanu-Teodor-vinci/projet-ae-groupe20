package be.vinci.pae.domain.user;

import be.vinci.pae.api.filters.BusinessException;
import java.time.LocalDate;
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
      throw new BusinessException("Le mot de passe est incorrect.");
    }
    return true;
  }

  @Override
  public void checkVinciEmail(String email) {
    if (!email.endsWith("@vinci.be") && !email.endsWith("@student.vinci.be")) {
      throw new BusinessException("L'email n'est pas un email de la haute école (vinci.be).");
    }
  }

  @Override
  public void checkUniqueEmail(UserDTO userDTO) {
    if (userDTO != null) {
      throw new BusinessException("L'email est déjà utilisé.");
    }
  }

  @Override
  public void checkRole(String role) throws BusinessException {
    if (!role.equals(POSSIBLE_ROLES[0]) && !role.equals(POSSIBLE_ROLES[1]) && !role.equals(
        POSSIBLE_ROLES[2])) {
      throw new BusinessException("Le rôle n'est pas valide.");
    }

    if (!(role.equals(POSSIBLE_ROLES[0]) && email.endsWith("@student.vinci.be"))
        && !(role.equals(POSSIBLE_ROLES[1]) && email.endsWith("@vinci.be"))
        && !(role.equals(POSSIBLE_ROLES[2]) && email.endsWith("@vinci.be"))) {
      throw new BusinessException("Le rôle ne correspond pas à l'email.");
    }
  }

  @Override
  public void checkNotNull() {
    if (this.id == 0) {
      throw new BusinessException("L'id est vide.");
    }
    if (this.lastName == null) {
      throw new BusinessException("Le nom est vide.");
    }
    if (this.firstName == null) {
      throw new BusinessException("Le prénom est vide.");
    }
    if (this.email == null) {
      throw new BusinessException("L'email est vide.");
    }
    if (this.telephoneNumber == null) {
      throw new BusinessException("Le numéro de téléphone est vide.");
    }
    if (this.role == null) {
      throw new BusinessException("Le rôle est vide.");
    }
    if (this.password == null) {
      throw new BusinessException("Le mot de passe est vide.");
    }
    if (this.registrationDate == null) {
      throw new BusinessException("La date d'inscription est vide.");
    }
  }

  @Override
  public void checkPhoneNumberFormat(String phoneNumber) {
    if (phoneNumber == null) {
      return;
    }
    if (!phoneNumber.matches("^\\d(\\d| (?=\\d)){8,}$")) {
      throw new BusinessException("Le numéro de téléphone doit contenir au moins 9 chiffres et peut inclure des espaces.");
    }
  }

  @Override
  public void checkNamesFormat(String name) {
    if (!Character.isUpperCase(name.charAt(0)) || !name.matches("^[a-zA-Z-]+$")) {
      throw new BusinessException(
          "Le nom doit commencer par une majuscule et ne contenir que des lettres.");
    }
  }

  @Override
  public boolean checkIsStudent() {
    return this.role.equals(POSSIBLE_ROLES[0]);
  }


}
