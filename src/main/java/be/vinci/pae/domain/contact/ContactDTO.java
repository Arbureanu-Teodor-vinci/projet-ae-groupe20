package be.vinci.pae.domain.contact;

import be.vinci.pae.domain.academicyear.AcademicYearDTO;
import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import be.vinci.pae.domain.user.StudentDTO;
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
   * Get studentDTO
   *
   * @return studentDTO StudentDTO
   */
  StudentDTO getStudent();

  /**
   * Set studentDTO.
   *
   * @param student StudentDTO
   */
  void setStudent(StudentDTO student);

  /**
   * Get enterpriseDTO.
   *
   * @return enterpriseDTO EnterpriseDTO
   */
  EnterpriseDTO getEnterprise();

  /**
   * Set enterpriseDTO.
   *
   * @param enterprise EnterpriseDTO
   */
  void setEnterprise(EnterpriseDTO enterprise);

  /**
   * Get academicYear.
   *
   * @return academicYear AcademicYearDTO
   */
  AcademicYearDTO getAcademicYear();

  /**
   * Set academicYear.
   *
   * @param academicYear AcademicYearDTO
   */
  void setAcademicYear(AcademicYearDTO academicYear);

  /**
   * Get version of the contact update.
   *
   * @return the version
   */
  int getVersion();

  /**
   * Set version of the contact update.
   *
   * @param version the version to set
   */
  void setVersion(int version);

  /**
   * Get all possible states of a contact.
   *
   * @return all possible states of a contact
   */
  String[] getAllPossibleStates();
}
