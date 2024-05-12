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
    if (studentDTO.getId() != 0) {
      throw new BusinessException("L'étudiant existe déjà");
    }
  }

  @Override
  public void checkContactExists(EnterpriseDTO enterpriseDTO, AcademicYearDTO actualAcademicYear,
      List<ContactDTO> contactsExisting) {
    boolean contactExists = false;
    for (ContactDTO contact : contactsExisting) {
      if (contact.getStudent().equals(this)
          && contact.getEnterprise().equals(enterpriseDTO)
          && contact.getAcademicYear().equals(actualAcademicYear)) {
        contactExists = true;
      }
    }
    if (contactExists) {
      throw new BusinessException(
          "L'étudiant a déjà un contact avec cette entreprise pour cette année académique");
    }
  }

  @Override
  public void checkContactAccepted(List<ContactDTO> contactsExisting,
      AcademicYearDTO actualAcademicYear) {
    boolean contactAccepted = false;
    for (ContactDTO contact : contactsExisting) {
      if (contact.getStudent().equals(this)
          && contact.getStateContact().equals("accepté")
          && contact.getAcademicYear().equals(actualAcademicYear)) {
        contactAccepted = true;
      }
    }
    if (contactAccepted) {
      throw new BusinessException(
          "L'étudiant a déjà un contact accepté pour cette année académique");
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
    return "StudentImpl{"
        + "id=" + id
        + ", academicYear=" + academicYear
        + '}';
  }
}
