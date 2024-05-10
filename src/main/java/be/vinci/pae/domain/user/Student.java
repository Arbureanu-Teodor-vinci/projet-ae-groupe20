package be.vinci.pae.domain.user;

import be.vinci.pae.api.filters.BusinessException;
import be.vinci.pae.domain.academicyear.AcademicYearDTO;
import be.vinci.pae.domain.contact.ContactDTO;
import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

/**
 * Interface of student.
 */
@JsonDeserialize(as = StudentImpl.class)
public interface Student extends StudentDTO, User {

  /**
   * Check if student is unique.
   *
   * @param studentDTO StudentDTO
   */
  void checkUniqueStudent(StudentDTO studentDTO) throws BusinessException;

  /**
   * Check if student has a contact with a same enterprise in the actual academic year.
   *
   * @param enterpriseDTO      EnterpriseDTO
   * @param actualAcademicYear AcademicYearDTO
   * @param contactsExisting   List of ContactDTO
   */
  void checkContactExists(EnterpriseDTO enterpriseDTO, AcademicYearDTO actualAcademicYear,
      List<ContactDTO> contactsExisting) throws BusinessException;

  /**
   * Check if the student has a contact accepted in the actual academic year.
   *
   * @param contactsExisting   List of ContactDTO
   * @param actualAcademicYear AcademicYearDTO
   */
  void checkContactAccepted(List<ContactDTO> contactsExisting, AcademicYearDTO actualAcademicYear)
      throws BusinessException;
}
