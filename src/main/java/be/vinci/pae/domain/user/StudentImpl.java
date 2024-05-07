package be.vinci.pae.domain.user;

import be.vinci.pae.api.filters.BusinessException;
import be.vinci.pae.domain.academicyear.AcademicYearDTO;
import be.vinci.pae.domain.contact.ContactDTO;
import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of Student interface.
 */
public class StudentImpl extends UserImpl implements Student {

  private int id;
  private AcademicYearDTO academicYear;

  @Override
  public AcademicYearDTO getStudentAcademicYear() {
    return academicYear;
  }

  @Override
  public void setAcademicYear(AcademicYearDTO academicYear) {
    this.academicYear = academicYear;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public void checkUniqueStudent(StudentDTO studentDTO) {
    if (studentDTO == null) {
      throw new BusinessException("Student already exists");
    }
  }

  @Override
  public void checkContactExists(EnterpriseDTO enterpriseDTO,
      List<ContactDTO> contactsExisting) {
    boolean contactExists = false;
    for (ContactDTO contact : contactsExisting) {
      if (contact.getStudent() == this
          && contact.getEnterprise() == enterpriseDTO
          && contact.getAcademicYear() == this.academicYear) {
        contactExists = true;
      }
    }
    if (contactExists) {
      throw new BusinessException(
          "Student already has a contact with this enterprise and academic year");
    }
  }

  @Override
  public void checkContactAccepted(List<ContactDTO> contactsExisting) {
    boolean contactAccepted = false;
    for (ContactDTO contact : contactsExisting) {
      if (contact.getStudent() == this
          && contact.getStateContact().equals("accepté")
          && contact.getAcademicYear() == this.academicYear) {
        contactAccepted = true;
      }
    }
    if (contactAccepted) {
      throw new BusinessException("Student already has an accepted contact for this academic year");
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
    if (!super.equals(o)) {
      return false;
    }
    StudentImpl student = (StudentImpl) o;
    return Objects.equals(academicYear, student.academicYear);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), academicYear);
  }

  @Override
  public String toString() {
    return "StudentImpl{" +
        "id=" + id +
        ", academicYear=" + academicYear +
        '}';
  }
}
