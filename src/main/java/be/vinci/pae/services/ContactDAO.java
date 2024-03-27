package be.vinci.pae.services;

import be.vinci.pae.domain.ContactDTO;
import java.util.List;

/**
 * Services of contact.
 */
public interface ContactDAO {

  /**
   * Get one contact by id from DB.
   *
   * @param id integer
   * @return contactDTO
   */
  ContactDTO getOneContactByid(int id);

  /**
   * Get all contacts from DB.
   *
   * @return List of ContactDTO
   */
  List<ContactDTO> getAllContacts();

  /**
   * Get contacts by user from DB.
   *
   * @param id integer
   * @return List of ContactDTO
   */
  List<ContactDTO> getContactsByUser(int id);
}
