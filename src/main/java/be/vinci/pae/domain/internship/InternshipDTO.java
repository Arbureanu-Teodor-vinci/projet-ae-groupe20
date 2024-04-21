package be.vinci.pae.domain.internship;

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
   * Get supervisorId.
   *
   * @return supervisorId integer
   */
  int getSupervisorId();

  /**
   * Set supervisorId.
   *
   * @param supervisorId integer
   */
  void setSupervisorId(int supervisorId);

  /**
   * Get contractId.
   *
   * @return contractId integer
   */
  int getContactId();

  /**
   * Set contractId.
   *
   * @param contactId integer
   */
  void setContactId(int contactId);

  /**
   * Get academicYear.
   *
   * @return academicYear integer
   */
  int getAcademicYear();

  /**
   * Set academicYear.
   *
   * @param academicYear integer
   */
  void setAcademicYear(int academicYear);

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
