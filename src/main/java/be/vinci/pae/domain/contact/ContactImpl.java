package be.vinci.pae.domain.contact;

import be.vinci.pae.api.filters.BusinessException;
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
  public void checkContactStateUpdate(String previousState) {
    // if state is one of final states which cant be updated
    if (previousState.equals(POSSIBLESTATES[5]) || previousState.equals(POSSIBLESTATES[4])
        || previousState.equals(POSSIBLESTATES[3]) || previousState.equals(POSSIBLESTATES[2])) {
      throw new BusinessException("Cant update contact from this final state");
    }
    // if updated state is initialised the previous state can only be initialised
    if (this.stateContact.equals(POSSIBLESTATES[0]) && !previousState.equals(POSSIBLESTATES[0])) {
      throw new BusinessException("Cant update contact to initialised state from previous state");
    }
    // if updated state is taken the previous state can only be initialised or taken
    if (this.stateContact.equals(POSSIBLESTATES[1]) && !previousState.equals(POSSIBLESTATES[0])
        && !previousState.equals(POSSIBLESTATES[1])) {
      throw new BusinessException("Cant update contact to taken state from previous state");
    }
    // if updated state is suspended the previous state can only be initialised, taken or suspended
    if (this.stateContact.equals(POSSIBLESTATES[2]) && !previousState.equals(POSSIBLESTATES[0])
        && !previousState.equals(POSSIBLESTATES[1])) {
      throw new BusinessException("Cant update contact to suspended state from previous state");
    }
    // if updated state is refused the previous state can only be taken
    if (this.stateContact.equals(POSSIBLESTATES[3]) && !previousState.equals(POSSIBLESTATES[1])) {
      throw new BusinessException("Cant update contact to refused state from previous state");
    }
    /* if updated state is not followed the previous state can only be initialised,
       taken or not followed
     */
    if (this.stateContact.equals(POSSIBLESTATES[4]) && !previousState.equals(POSSIBLESTATES[0])
        && !previousState.equals(POSSIBLESTATES[1])) {
      throw new BusinessException(
          "Cant update contact to not followed state from previous state");
    }
    // if updated state is accepted the previous state can only be taken
    if (this.stateContact.equals(POSSIBLESTATES[5]) &&
        !previousState.equals(POSSIBLESTATES[1])) {
      throw new BusinessException("Cant update contact to accepted state from previous state");
    }
  }

  @Override
  public void checkInterviewMethodUpdate(String interviewMethodBeforeUpdate) {
    // initial state can only have null interviewMethod
    if (this.stateContact.equals(POSSIBLESTATES[0])) {
      if (this.interviewMethod != null) {
        throw new BusinessException("Cant update interview method if state is initialised");
      }
    } else if (this.stateContact.equals(
        POSSIBLESTATES[1])) { // taken state can update interviewMethod
      if (this.interviewMethod == null || this.interviewMethod.isBlank()) {
        throw new BusinessException(
            "Cant update interview method to null or empty when state is taken");
      }
    } else {
      // on other states cant update interviewMethod from previous value when it was on taken state
      if (!this.interviewMethod.equals(interviewMethodBeforeUpdate)) {
        throw new BusinessException(
            "Cant update interview method from previous value when state is not taken");

      }
    }
  }

  @Override
  public void checkContactRefusalReasonUpdate() {
    //can only update contactRefusal on refused state
    if (this.stateContact.equals(POSSIBLESTATES[3])) {
      if (this.refusalReason == null) {
        throw new BusinessException("Refusal reason needs to be not null on refused state");
      }
    } else {
      if (this.refusalReason != null) {
        throw new BusinessException("Refusal reason needs to be updatable only on refused state");
      }
    }
  }


  @Override
  public void checkContactState() {
    boolean validState = false;
    for (String state : POSSIBLESTATES) {
      if (this.stateContact.equals(state)) {
        validState = true;
        break;
      }
    }
    if (!validState) {
      throw new BusinessException("Invalid state");
    }
  }

  @Override
  public void checkIfContactIsTaken() {
    boolean isTakenState = false;
    if (stateContact.equals(POSSIBLESTATES[1])) {
      isTakenState = true;
    }
    if (!isTakenState) {
      throw new BusinessException("The contact has to be in taken state.");
    }
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
