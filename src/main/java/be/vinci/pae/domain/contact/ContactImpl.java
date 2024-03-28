package be.vinci.pae.domain.contact;

import java.util.Objects;

/**
 * Implementation of Contact and contactDTO.
 */
public class ContactImpl implements ContactDTO {

  private int id;
  private String interviewMethod;
  private String tool;
  private String refusalReason;
  private String stateContact;
  private int studentId;
  private int enterpriseId;
  private int academicYear;

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getInterviewMethod() {
    return interviewMethod;
  }

  @Override
  public void setInterviewMethod(String interviewMethod) {
    this.interviewMethod = interviewMethod;
  }

  @Override
  public String getTool() {
    return tool;
  }

  @Override
  public void setTool(String tool) {
    this.tool = tool;
  }

  @Override
  public String getRefusalReason() {
    return refusalReason;
  }

  @Override
  public void setRefusalReason(String refusalReason) {
    this.refusalReason = refusalReason;
  }

  @Override
  public String getStateContact() {
    return stateContact;
  }

  @Override
  public void setStateContact(String stateContact) {
    this.stateContact = stateContact;
  }

  @Override
  public int getStudentId() {
    return studentId;
  }

  @Override
  public void setStudentId(int studentId) {
    this.studentId = studentId;
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
  public int getAcademicYear() {
    return academicYear;
  }

  @Override
  public void setAcademicYear(int academicYear) {
    this.academicYear = academicYear;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContactImpl contact = (ContactImpl) o;
    return id == contact.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "ContactImpl{" + "id=" + id + ", interviewMethod='" + interviewMethod + '\'' + ", tool='"
        + tool + '\'' + ", refusalReason='" + refusalReason + '\'' + ", stateContact='"
        + stateContact + '\'' + ", studentId=" + studentId + ", enterpriseId=" + enterpriseId
        + ", academicYear=" + academicYear + '}';
  }
}
