package be.vinci.pae.domain.enterprise;

import be.vinci.pae.api.filters.BusinessException;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of Enterprise and enterpriseDTO.
 */
public class EnterpriseImpl implements Enterprise {

  private int id;
  private String tradeName;
  private String designation;
  private String address;
  private String phoneNumber;
  private String city;
  private String email;
  private boolean blackListed;
  private String blackListMotivation;
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
  public String getAddress() {
    return address;
  }

  @Override
  public void setAddress(String address) {
    this.address = address;
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
  public int getVersion() {
    return version;
  }

  @Override
  public void setVersion(int version) {
    this.version = version;
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
    return id == that.id && blackListed == that.blackListed && version == that.version
        && Objects.equals(tradeName, that.tradeName) && Objects.equals(
        designation, that.designation) && Objects.equals(address, that.address)
        && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(city,
        that.city) && Objects.equals(email, that.email) && Objects.equals(
        blackListMotivation, that.blackListMotivation);
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
        + ", adresse='" + address + '\''
        + ", phoneNumber='" + phoneNumber + '\''
        + ", email='" + email + '\''
        + ", blackListed=" + blackListed
        + ", blackListMotivation='" + blackListMotivation + '\''
        + '}';
  }


  @Override
  public void checkDesignationExistsOrIsNull(String tradeName, String designation,
      List<EnterpriseDTO> listEnterprise) {
    for (EnterpriseDTO enterprise : listEnterprise) {
      if (enterprise.getTradeName().trim().equalsIgnoreCase(tradeName.trim()) && designation.trim()
          .isEmpty()) {
        throw new BusinessException(
            "L'appellation ne peut pas être car il existe déjà une entreprise avec ce nom.");
      }
      if (enterprise.getTradeName().trim().equalsIgnoreCase(tradeName.trim())
          && enterprise.getDesignation() != null && enterprise.getDesignation().trim()
          .equalsIgnoreCase(designation.trim())) {
        throw new BusinessException("Cette appellation existe déjà pour cette entreprise.");
      }
    }
  }

  @Override
  public void checkIsBlackListed() {
    if (blackListed) {
      throw new BusinessException("Cette entreprise est déjà blacklistée.");
    }
  }

  @Override
  public void checkBlackListMotivation() {
    if (blackListed && blackListMotivation.trim().isEmpty()) {
      throw new BusinessException("La raison de la mise en blacklist est obligatoire.");
    }
  }

  @Override
  public void checkUpdateBlacklist(EnterpriseDTO enterprise) {
    if (enterprise.getId() != id
        || !Objects.equals(enterprise.getTradeName(), tradeName)
        || !Objects.equals(enterprise.getDesignation(), designation)
        || !Objects.equals(enterprise.getAddress(), address)
        || !Objects.equals(enterprise.getPhoneNumber(), phoneNumber)
        || !Objects.equals(enterprise.getCity(), city)
        || !Objects.equals(enterprise.getEmail(), email)) {
      throw new BusinessException(
          "Vous ne pouvez pas modifier d'autres données que blacklisté et la raison.");
    }
  }

  @Override
  public void checkEmailFormat(String email) {
    if (email.trim().isEmpty()) {
      return;
    }
    int firstIndex = email.indexOf("@");
    int lastIndex = email.lastIndexOf("@");
    if (firstIndex != lastIndex || firstIndex == -1) {
      throw new BusinessException("Le format de l'email est invalide.");
    }
  }

  @Override
  public void checkPhoneNumberFormat(String phoneNumber) {
    if (phoneNumber.trim().isEmpty()) {
      return;
    }
    if (!phoneNumber.matches("^[0-9.\\s]{4,}$")) {
      throw new BusinessException("Minimum 4 chiffres pour le numéro de téléphone.");
    }
  }

  @Override
  public void checkIsNull() {
    if (this.getId() == 0
        && this.getTradeName() == null
        && this.getDesignation() == null
        && this.getAddress() == null
        && this.getPhoneNumber() == null
        && this.getCity() == null
        && this.getEmail() == null
        && this.getBlackListMotivation() == null
        && this.getVersion() == 0) {
      throw new BusinessException("This enterprise is null.");
    }
  }
}