package be.vinci.pae.domain.factory;

import be.vinci.pae.domain.academicyear.AcademicYearDTO;
import be.vinci.pae.domain.contact.ContactDTO;
import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import be.vinci.pae.domain.internshipSupervisor.SupervisorDTO;
import be.vinci.pae.domain.user.StudentDTO;
import be.vinci.pae.domain.user.UserDTO;


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

  /**
   * Method returning a new contact.
   *
   * @return a new ContactDTO
   */
  ContactDTO getContactDTO();

  /**
   * Method returning a new supervisor.
   *
   * @return a new Supervisor
   */
  SupervisorDTO getSupervisorDTO();
}
