package be.vinci.pae.domain.contact;

import java.util.Objects;

/**
 * Implementation of Contact and contactDTO.
 */
public class ContactImpl implements Contact {

  private static final String[] POSSIBLESTATES = {"initié", "pris", "suspendu", "refusé",
      "non suivis", "accepté"};
  private int id;
  private String interviewMethod;
  private String tool;
  private String refusalReason;
  private String stateContact;
  private int studentId;
  private int enterpriseId;
  private int academicYear;
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
  public String[] getAllPossibleStates() {
    return POSSIBLESTATES;
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
    ContactImpl contact = (ContactImpl) o;
    return id == contact.id;
  }

  @Override
  public boolean checkContactStateUpdate(String state) {
    // if state is one of final states which cant be updated
    if (state.equals(POSSIBLESTATES[5]) || state.equals(POSSIBLESTATES[4])
        || state.equals(POSSIBLESTATES[3]) || state.equals(POSSIBLESTATES[2])) {
      return false;
    }
    // if updated state is initialised the previous state can only be initialised
    if (this.stateContact.equals(POSSIBLESTATES[0])) {
      return state.equals(POSSIBLESTATES[0]);
    }
    // if updated state is taken the previous state can only be initialised or taken
    if (this.stateContact.equals(POSSIBLESTATES[1])) {
      return state.equals(POSSIBLESTATES[0]) || state.equals(POSSIBLESTATES[1]);
    }
    // if updated state is suspended the previous state can only be initialised, taken or suspended
    if (this.stateContact.equals(POSSIBLESTATES[2])) {
      return state.equals(POSSIBLESTATES[0]) || state.equals(POSSIBLESTATES[1]);
    }
    // if updated state is refused the previous state can only be initialised or refused
    if (this.stateContact.equals(POSSIBLESTATES[3])) {
      return state.equals(POSSIBLESTATES[1]);
    }
    /* if updated state is not followed the previous state can only be initialised,
       taken or not followed
     */
    if (this.stateContact.equals(POSSIBLESTATES[4])) {
      return state.equals(POSSIBLESTATES[0]) || state.equals(POSSIBLESTATES[1]);
    }
    // if updated state is accepted the previous state can only be taken or accepted
    if (this.stateContact.equals(POSSIBLESTATES[5])) {
      return state.equals(POSSIBLESTATES[1]);
    }

    return false;
  }

  @Override
  public boolean checkInterviewMethodUpdate(String interviewMethodBeforeUpdate) {
    if (this.stateContact.equals(
        POSSIBLESTATES[0])) { // initial state can only have null interviewMethod
      return this.interviewMethod == null;
    } else if (this.stateContact.equals(
        POSSIBLESTATES[1])) { // taken state can update interviewMethod
      return this.interviewMethod != null;
    } else {
      // on other states cant update interviewMethod from previous value when it was on taken state
      return this.interviewMethod.equals(interviewMethodBeforeUpdate);
    }
  }

  @Override
  public boolean checkContactRefusalReasonUpdate() {
    //can only update contactRefusal on refused state
    if (this.stateContact.equals(POSSIBLESTATES[3])) {
      return this.refusalReason != null;
    } else {
      return this.refusalReason == null || this.refusalReason.isBlank();
    }
  }


  @Override
  public boolean checkContactState() {
    for (String state : POSSIBLESTATES) {
      if (this.stateContact.equals(state)) {
        return true;
      }
    }
    return false;
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
