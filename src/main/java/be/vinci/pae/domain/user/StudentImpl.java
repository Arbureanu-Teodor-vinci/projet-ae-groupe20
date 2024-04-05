package be.vinci.pae.domain.user;

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
  public boolean checkUniqueStudent(StudentDTO studentDTO) {
    return studentDTO.getId() == 0;
  }

  @Override
  public boolean checkContactExists(EnterpriseDTO enterpriseDTO,
      List<ContactDTO> contactsExisting) {
    for (ContactDTO contact : contactsExisting) {
      if (contact.getStudentId() == this.getId()
          && contact.getEnterpriseId() == enterpriseDTO.getId()
          && contact.getAcademicYear() == this.academicYear.getId()) {
        return true;
      }
    }
    return false;
  }

  public boolean checkContactAccepted(List<ContactDTO> contactsExisting) {
    for (ContactDTO contact : contactsExisting) {
      if (contact.getStudentId() == this.getId()
          && contact.getStateContact().equals("accepté")
          && contact.getAcademicYear() == this.academicYear.getId()) {
        return true;
      }
    }
    return false;
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
    return "StudentImpl{"
        + "academicYear=" + academicYear
        + '}';
  }
}
