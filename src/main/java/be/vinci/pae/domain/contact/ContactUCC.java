package be.vinci.pae.domain.contact;

import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import be.vinci.pae.domain.user.StudentDTO;
import java.util.List;

/**
 * Interface of contactUCC.
 */
public interface ContactUCC {

  /**
   * Get one contact by id.
   *
   * @param id int
   * @return ContactDTO
   */
  ContactDTO getOneContact(int id);

  /**
   * Get all contacts.
   *
   * @return List of ContactDTO
   */
  List<ContactDTO> getAllContacts();

  /**
   * Get contacts by user.
   *
   * @param id int
   * @return List of ContactDTO
   */
  List<ContactDTO> getContactsByUser(int id);

  /**
   * Add a contact.
   *
   * @param studentDTO    StudentDTO
   * @param enterpriseDTO EnterpriseDTO
   * @return ContactDTO
   */
  ContactDTO addContact(StudentDTO studentDTO, EnterpriseDTO enterpriseDTO);

  /**
   * Update a contact.
   *
   * @param contact ContactDTO
   * @return ContactDTO
   */
  ContactDTO updateContact(ContactDTO contact);

}
