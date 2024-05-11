package be.vinci.pae.domain.contact;

import be.vinci.pae.api.filters.BusinessException;
import be.vinci.pae.domain.academicyear.AcademicYearDTO;
import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import be.vinci.pae.domain.user.StudentDTO;
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
  private StudentDTO student;
  private EnterpriseDTO enterprise;
  private AcademicYearDTO academicYear;
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
  public StudentDTO getStudent() {
    return student;
  }

  @Override
  public void setStudent(StudentDTO student) {
    this.student = student;
  }

  @Override
  public EnterpriseDTO getEnterprise() {
    return enterprise;
  }

  @Override
  public void setEnterprise(EnterpriseDTO enterprise) {
    this.enterprise = enterprise;
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
  public void checkContactStateUpdate(String previousState) {
    // if state is one of final states which cant be updated
    if (previousState.equals(POSSIBLESTATES[5]) || previousState.equals(POSSIBLESTATES[4])
        || previousState.equals(POSSIBLESTATES[3]) || previousState.equals(POSSIBLESTATES[2])) {
      throw new BusinessException("Impossible de mettre à jour le contact dans cet état");
    }
    // if updated state is initialised the previous state can only be initialised
    if (this.stateContact.equals(POSSIBLESTATES[0]) && !previousState.equals(POSSIBLESTATES[0])) {
      throw new BusinessException(
          "Vous ne pouvez pas mettre à jour le contact en état inité à partir de cet état.");
    }
    // if updated state is taken the previous state can only be initialised or taken
    if (this.stateContact.equals(POSSIBLESTATES[1]) && !previousState.equals(POSSIBLESTATES[0])
        && !previousState.equals(POSSIBLESTATES[1])) {
      throw new BusinessException(
          "Vous ne pouvez pas mettre à jour le contact en état pris à partir de cet état.");
    }
    // if updated state is suspended the previous state can only be initialised, taken or suspended
    if (this.stateContact.equals(POSSIBLESTATES[2]) && !previousState.equals(POSSIBLESTATES[0])
        && !previousState.equals(POSSIBLESTATES[1])) {
      throw new BusinessException(
          "Vous ne pouvez pas mettre à jour le contact en état suspendu à partir de cet état.");
    }
    // if updated state is refused the previous state can only be taken
    if (this.stateContact.equals(POSSIBLESTATES[3]) && !previousState.equals(POSSIBLESTATES[1])) {
      throw new BusinessException(
          "Vous ne pouvez pas mettre à jour le contact en état refusé à partir de cet état.");
    }
    /* if updated state is not followed the previous state can only be initialised,
       taken or not followed
     */
    if (this.stateContact.equals(POSSIBLESTATES[4]) && !previousState.equals(POSSIBLESTATES[0])
        && !previousState.equals(POSSIBLESTATES[1])) {
      throw new BusinessException(
          "Vous ne pouvez pas mettre à jour le contact en état non suivi à partir de cet état.");
    }
    // if updated state is accepted the previous state can only be taken
    if (this.stateContact.equals(POSSIBLESTATES[5]) && !previousState.equals(POSSIBLESTATES[1])) {
      throw new BusinessException(
          "Vous ne pouvez pas mettre à jour le contact en état accepté à partir de cet état.");
    }
  }

  @Override
  public void checkInterviewMethodUpdate(String interviewMethodBeforeUpdate) {
    // initial state can only have null interviewMethod
    if (this.stateContact.equals(POSSIBLESTATES[0])) {
      if (this.interviewMethod != null) {
        throw new BusinessException("Impossible de mettre à jour le moyen de contact si l'état est "
            + POSSIBLESTATES[0]);
      }
    } else if (this.stateContact.equals(
        POSSIBLESTATES[1])) { // taken state can update interviewMethod
      if (this.interviewMethod == null || this.interviewMethod.isBlank()) {
        throw new BusinessException(
            "Le moyen de contact est obligatoire si l'état est " + POSSIBLESTATES[1]);
      }
    } else {
      // on other states cant update interviewMethod from previous value when it was on taken state
      if (!Objects.equals(this.interviewMethod, interviewMethodBeforeUpdate)) {
        throw new BusinessException(
            "Impossible de mettre le moyen de contact à jour si l'état du contact est différent de pris.");
      }
    }
  }

  @Override
  public void checkContactToolUpdate() {
    // tool cannot be null if interviewMethod is 'A distance'
    if (Objects.equals(this.interviewMethod, "A distance") && (this.tool == null
        || this.tool.isBlank())) {
      throw new BusinessException(
          "L'outil est obligatoire si le moyen de contact est 'A distance'");
    }

  }

  @Override
  public void checkContactRefusalReasonUpdate() {
    //can only update contactRefusal on refused state
    if (this.stateContact.equals(POSSIBLESTATES[3])) {
      if (this.refusalReason == null) {
        throw new BusinessException("La raison du refus est obligatoire si l'état est refusé.");
      }
    } else {
      if (this.refusalReason != null && !this.refusalReason.isBlank()) {
        throw new BusinessException(
            "Impossible de mettre à jour la raison du refus si l'état n'est pas refusé.");
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
      throw new BusinessException("L'état du contact n'est pas valide.");
    }
  }

  @Override
  public void checkIfContactIsAccepted() {
    boolean isTakenState = false;
    if (stateContact.equals(POSSIBLESTATES[5])) {
      isTakenState = true;
    }
    if (!isTakenState) {
      throw new BusinessException("L'état du contact n'est pas 'accepté'");
    }
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
    return id == contact.id && version == contact.version && Objects.equals(interviewMethod,
        contact.interviewMethod) && Objects.equals(tool, contact.tool)
        && Objects.equals(refusalReason, contact.refusalReason) && Objects.equals(
        stateContact, contact.stateContact) && Objects.equals(student, contact.student)
        && Objects.equals(enterprise, contact.enterprise) && Objects.equals(
        academicYear, contact.academicYear);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, interviewMethod, tool, refusalReason, stateContact, student, enterprise,
        academicYear, version);
  }

  @Override
  public String toString() {
    return "ContactImpl{"
        + "id=" + id
        + ", interviewMethod='" + interviewMethod + '\''
        + ", tool='" + tool + '\''
        + ", refusalReason='" + refusalReason + '\''
        + ", stateContact='" + stateContact + '\''
        + ", student=" + student
        + ", enterprise=" + enterprise
        + ", academicYear=" + academicYear
        + ", version=" + version
        + '}';
  }

}
