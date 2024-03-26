package be.vinci.pae.domain.factory;

import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import be.vinci.pae.domain.user.StudentDTO;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.academicyear.AcademicYearDTO;

/**
 * Interface factory for each UCC.
 */
public interface DomainFactory {

  /**
   * Method returning a new user.
   *
   * @return UserDTO
   */
  UserDTO getUserDTO();

  /**
   * Method returning a new enterprise.
   *
   * @return a new EnterpriseDTO
   */
  EnterpriseDTO getEnterpriseDTO();

  /**
   * Method returning a new academic year.
   *
   * @return a new AcademicYearDTO
   */
  AcademicYearDTO getAcademicYearDTO();

  /**
   * Method returning a new student.
   *
   * @return a new StudentDTO
   */
  StudentDTO getStudentDTO();
}
