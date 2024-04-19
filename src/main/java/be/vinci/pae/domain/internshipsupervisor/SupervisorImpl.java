package be.vinci.pae.domain.internshipsupervisor;


import be.vinci.pae.api.filters.BusinessException;
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
  private int enterpriseId;

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
  public int getEnterpriseId() {
    return enterpriseId;
  }

  @Override
  public void setEnterpriseId(int enterpriseId) {
    this.enterpriseId = enterpriseId;
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
    return id == that.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "InternshipSupervisor{"
        + "id=" + id
        + ", firstName='" + firstName + '\''
        + ", lastName='" + lastName + '\''
        + ", email='" + email + '\''
        + ", phoneNumber='" + phoneNumber + '\''
        + ", enterpriseId=" + enterpriseId + '}';
  }

  @Override
  public void checkUniqueEmail(SupervisorDTO supervisorDTO) {
    if (supervisorDTO != null) {
      throw new BusinessException("Email for this supervisor already exists");
    }
  }

}
