package be.vinci.pae.domain.internship;

import be.vinci.pae.domain.academicyear.AcademicYearDTO;
import be.vinci.pae.domain.contact.ContactDTO;
import be.vinci.pae.domain.internshipsupervisor.SupervisorDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Interface of internship with getters and setters.
 */
@JsonDeserialize(as = InternshipImpl.class)
public interface InternshipDTO {

  /**
   * Get ID.
   *
   * @return id integer
   */
  int getId();

  /**
   * set ID.
   *
   * @param id integer
   */
  void setId(int id);

  /**
   * Get subject.
   *
   * @return subject String
   */
  String getSubject();

  /**
   * Set subject.
   *
   * @param subject String
   */
  void setSubject(String subject);

  /**
   * Get signatureDate.
   *
   * @return signatureDate String
   */
  String getSignatureDate();

  /**
   * Set signatureDate.
   *
   * @param signatureDate String
   */
  void setSignatureDate(String signatureDate);

  /**
   * Get supervisorDTO.
   *
   * @return supervisor SupervisorDTO
   */
  SupervisorDTO getSupervisor();

  /**
   * Set supervisorId.
   *
   * @param supervisor integer
   */
  void setSupervisor(SupervisorDTO supervisor);

  /**
   * Get contactDTO.
   *
   * @return contact ContactDTO
   */
  ContactDTO getContact();

  /**
   * Set contactDTO.
   *
   * @param contact contactDTO
   */
  void setContact(ContactDTO contact);

  /**
   * Get academicYearDTO.
   *
   * @return academicYear integer
   */
  AcademicYearDTO getAcademicYear();

  /**
   * Set academicYearDTO.
   *
   * @param academicYear academicYearDTO
   */
  void setAcademicYear(AcademicYearDTO academicYear);

  /**
   * Get version.
   *
   * @return version integer
   */
  int getVersion();

  /**
   * Set version.
   *
   * @param version integer
   */
  void setVersion(int version);

  @Override
  boolean equals(Object o);

  @Override
  int hashCode();

  @Override
  String toString();
}
