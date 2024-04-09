package be.vinci.pae.domain.user;

import be.vinci.pae.domain.contact.ContactDTO;
import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import java.util.List;

/**
 * Interface of student.
 */
public interface Student extends StudentDTO, User {

  /**
   * Check if student is unique.
   *
   * @param studentDTO StudentDTO
   * @return true if student is unique
   */
  boolean checkUniqueStudent(StudentDTO studentDTO);

  /**
   * Check if student have a contact with a same enterprise and academic year.
   *
   * @param enterpriseDTO    EnterpriseDTO
   * @param contactsExisting List of ContactDTO
   * @return true if student have a contact with a same enterprise and academic year
   */
  boolean checkContactExists(EnterpriseDTO enterpriseDTO,
      List<ContactDTO> contactsExisting);

  /**
   * Check if a contact with an enterprise with the state accepted and the same academic year
   * already
   *
   * @param contactsExisting List of ContactDTO
   * @return boolean
   */
  boolean checkContactAccepted(List<ContactDTO> contactsExisting);
}
