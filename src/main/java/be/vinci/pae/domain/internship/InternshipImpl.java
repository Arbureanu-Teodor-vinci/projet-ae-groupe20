package be.vinci.pae.domain.internship;

import java.util.Objects;

/**
 * Implementation of Internship and InternshipDTO.
 */
public class InternshipImpl implements Internship {


  private int id;
  private String subject;
  private String signatureDate;
  private int supervisorId;
  private int contactId;
  private int academicYear;
  private int version;

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getSubject() {
    return subject;
  }

  @Override
  public void setSubject(String subject) {
    this.subject = subject;
  }

  @Override
  public String getSignatureDate() {
    return signatureDate;
  }

  @Override
  public void setSignatureDate(String signatureDate) {
    this.signatureDate = signatureDate;
  }

  @Override
  public int getSupervisorId() {
    return supervisorId;
  }

  @Override
  public void setSupervisorId(int supervisorId) {
    this.supervisorId = supervisorId;
  }

  @Override
  public int getContactId() {
    return contactId;
  }

  @Override
  public void setContactId(int contactId) {
    this.contactId = contactId;
  }

  @Override
  public int getAcademicYear() {
    return academicYear;
  }

  @Override
  public void setAcademicYear(int academicYear) {
    this.academicYear = academicYear;
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
    InternshipImpl that = (InternshipImpl) o;
    return id == that.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Internship{"
        + "id=" + id
        + ", subject='" + subject + '\''
        + ", signatureDate='" + signatureDate + '\''
        + ", supervisorId=" + supervisorId
        + ", contractId=" + contactId
        + ", academicYear=" + academicYear + '}';
  }


}
