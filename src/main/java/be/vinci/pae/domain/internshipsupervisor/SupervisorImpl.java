package be.vinci.pae.domain.internshipsupervisor;


import be.vinci.pae.api.filters.BusinessException;
import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import java.util.Objects;

/**
 * Implementation of SupervisorDTO.
 */
public class SupervisorImpl implements Supervisor {


  private int id;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private EnterpriseDTO enterprise;

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public void setFirstName(String firstName) {
    this.firstName = firstName;
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
  public String getEmail() {
    return email;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String getPhoneNumber() {
    return phoneNumber;
  }

  @Override
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  @Override
  public EnterpriseDTO getEnterprise() {
    return enterprise;
  }

  @Override
  public void setEnterprise(EnterpriseDTO enterprise) {
    this.enterprise = enterprise;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SupervisorImpl that = (SupervisorImpl) o;
    return id == that.id && Objects.equals(firstName, that.firstName)
        && Objects.equals(lastName, that.lastName) && Objects.equals(email,
        that.email) && Objects.equals(phoneNumber, that.phoneNumber)
        && Objects.equals(enterprise, that.enterprise);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName, email, phoneNumber, enterprise);
  }

  @Override
  public String toString() {
    return "SupervisorImpl{" +
        "id=" + id +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", email='" + email + '\'' +
        ", phoneNumber='" + phoneNumber + '\'' +
        ", enterprise=" + enterprise +
        '}';
  }

  @Override
  public void checkUniqueEmail(SupervisorDTO supervisorDTO) {
    if (supervisorDTO != null) {
      throw new BusinessException("Email for this supervisor already exists");
    }
  }

  @Override
  public void checkPhoneNumberFormat(String phoneNumber) {
    if (phoneNumber == null) {
      return;
    }
    if (!phoneNumber.matches("^[0-9]{10}$")) {
      throw new BusinessException("Phone number is invalid");
    }
  }

  @Override
  public void checkNamesFormat(String name) {
    if (!Character.isUpperCase(name.charAt(0)) || !name.matches("^[a-zA-Z-]{1,50}$")) {
      throw new BusinessException("Name is invalid");
    }
  }

  @Override
  public void checkEmailFormat(String email) {
    int firstIndex = email.indexOf("@");
    int lastIndex = email.lastIndexOf("@");
    if (firstIndex != lastIndex || firstIndex == -1) {
      throw new BusinessException("Email is invalid");
    }
  }

}
