package be.vinci.pae.domain.enterprise;

import java.util.Objects;

/**
 * Implementation of Enterprise and enterpriseDTO.
 */
public class EnterpriseImpl implements Enterprise {

  private int id;
  private String tradeName;
  private String designation;
  private String adresse;
  private String phoneNumber;
  private String city;
  private String email;
  private boolean blackListed;
  private String blackListMotivation;

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getTradeName() {
    return tradeName;
  }

  @Override
  public void setTradeName(String tradeName) {
    this.tradeName = tradeName;
  }

  @Override
  public String getDesignation() {
    return designation;
  }

  @Override
  public void setDesignation(String designation) {
    this.designation = designation;
  }

  @Override
  public String getAdresse() {
    return adresse;
  }

  @Override
  public void setAdresse(String adresse) {
    this.adresse = adresse;
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
  public String getCity() {
    return city;
  }

  @Override
  public void setCity(String city) {
    this.city = city;
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
  public boolean isBlackListed() {
    return blackListed;
  }

  @Override
  public void setBlackListed(boolean blackListed) {
    this.blackListed = blackListed;
  }

  @Override
  public String getBlackListMotivation() {
    return blackListMotivation;
  }

  @Override
  public void setBlackListMotivation(String blackListMotivation) {
    this.blackListMotivation = blackListMotivation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnterpriseImpl that = (EnterpriseImpl) o;
    return id == that.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Enterprise{"
        + "id=" + id
        + ", tradeName='" + tradeName + '\''
        + ", designation='" + designation + '\''
        + ", adresse='" + adresse + '\''
        + ", phoneNumber='" + phoneNumber + '\''
        + ", email='" + email + '\''
        + ", blackListed=" + blackListed
        + ", blackListMotivation='" + blackListMotivation + '\''
        + '}';
  }

  @Override
  public boolean checkEnterpriseAdd(){
    if(tradeName != null && designation != null && adresse != null && phoneNumber != null && city != null && email != null){
     return true;
    }
    return false;
  }
}
