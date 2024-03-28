package be.vinci.pae.domain.contact;

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

}
