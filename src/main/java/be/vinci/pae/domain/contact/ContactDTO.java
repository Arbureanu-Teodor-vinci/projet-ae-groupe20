package be.vinci.pae.domain.contact;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Interface of contact with setters and getters.
 */
@JsonDeserialize(as = ContactImpl.class)
public interface ContactDTO {

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
   * Get interviewMethod.
   *
   * @return interviewMethod String
   */
  String getInterviewMethod();

  /**
   * Set interviewMethod.
   *
   * @param interviewMethod String
   */
  void setInterviewMethod(String interviewMethod);

  /**
   * Get tool.
   *
   * @return tool String
   */
  String getTool();

  /**
   * Set tool.
   *
   * @param tool String
   */
  void setTool(String tool);

  /**
   * Get refusalReason.
   *
   * @return refusalReason String
   */
  String getRefusalReason();

  /**
   * Set refusalReason.
   *
   * @param refusalReason String
   */
  void setRefusalReason(String refusalReason);

  /**
   * Get stateContact.
   *
   * @return stateContact String
   */
  String getStateContact();

  /**
   * Set stateContact.
   *
   * @param stateContact String
   */
  void setStateContact(String stateContact);

  /**
   * Get studentId.
   *
   * @return studentId integer
   */
  int getStudentId();

  /**
   * Set studentId.
   *
   * @param studentId integer
   */
  void setStudentId(int studentId);

  /**
   * Get enterpriseId.
   *
   * @return enterpriseId integer
   */
  int getEnterpriseId();

  /**
   * Set enterpriseId.
   *
   * @param enterpriseId integer
   */
  void setEnterpriseId(int enterpriseId);

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
   * Get all possible states of a contact.
   *
   * @return all possible states of a contact
   */
  String[] getAllPossibleStates();

  @Override
  boolean equals(Object o);

  @Override
  int hashCode();

  @Override
  String toString();
}
