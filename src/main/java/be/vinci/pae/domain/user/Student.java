package be.vinci.pae.domain.user;

import be.vinci.pae.api.filters.BusinessException;
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
   * Check if student has a contact with a same enterprise and academic year.
   *
   * @param enterpriseDTO    EnterpriseDTO
   * @param contactsExisting List of ContactDTO
   */
  void checkContactExists(EnterpriseDTO enterpriseDTO,
      List<ContactDTO> contactsExisting) throws BusinessException;

  /**
   * Check if a contact with an enterprise with the state accepted and the same academic year
   * already.
   *
   * @param contactsExisting List of ContactDTO
   */
  void checkContactAccepted(List<ContactDTO> contactsExisting) throws BusinessException;
}
