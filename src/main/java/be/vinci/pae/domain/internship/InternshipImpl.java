package be.vinci.pae.domain.internship;

import be.vinci.pae.domain.academicyear.AcademicYearDTO;
import be.vinci.pae.domain.contact.ContactDTO;
import be.vinci.pae.domain.internshipsupervisor.SupervisorDTO;
import java.util.Objects;

/**
 * Implementation of Internship and InternshipDTO.
 */
public class InternshipImpl implements Internship {


  private int id;
  private String subject;
  private String signatureDate;
  private SupervisorDTO supervisor;
  private ContactDTO contact;
  private AcademicYearDTO academicYear;
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
  public SupervisorDTO getSupervisor() {
    return supervisor;
  }

  @Override
  public void setSupervisor(SupervisorDTO supervisor) {
    this.supervisor = supervisor;
  }

  @Override
  public ContactDTO getContact() {
    return contact;
  }

  @Override
  public void setContact(ContactDTO contact) {
    this.contact = contact;
  }

  @Override
  public AcademicYearDTO getAcademicYear() {
    return academicYear;
  }

  @Override
  public void setAcademicYear(AcademicYearDTO academicYear) {
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
    return id == that.id && version == that.version && Objects.equals(subject, that.subject)
        && Objects.equals(signatureDate, that.signatureDate) && Objects.equals(
        supervisor, that.supervisor) && Objects.equals(contact, that.contact)
        && Objects.equals(academicYear, that.academicYear);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, subject, signatureDate, supervisor, contact, academicYear, version);
  }

  @Override
  public String toString() {
    return "Internship{"
        + "id=" + id
        + ", subject='" + subject + '\''
        + ", signatureDate='" + signatureDate + '\''
        + ", supervisorId=" + supervisor
        + ", contractId=" + contact
        + ", academicYear=" + academicYear + '}';
  }

  @Override
  public void checkOnlySubjectUpdated(InternshipDTO previousInternship) {
    if (this.equals(previousInternship)) {
      throw new IllegalArgumentException("The internship has not been updated");
    }
    if (this.getSubject().equals(previousInternship.getSubject())) {
      throw new IllegalArgumentException("Subject has not been updated");
    }
    if (this.getSignatureDate() != null && !this.getSignatureDate()
        .equals(previousInternship.getSignatureDate())) {
      throw new IllegalArgumentException("Signature date can not be updated");
    }
  }

}
