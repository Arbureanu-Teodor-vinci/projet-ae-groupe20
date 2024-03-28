package be.vinci.pae.services.contactservices;

import be.vinci.pae.domain.contact.ContactDTO;
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

  /**
   * Add a contact to DB.
   *
   * @param studentID integer, academicYearID integer, enterpriseID integer
   * @return ContactDTO
   */
  ContactDTO addContact(int studentID, int academicYearID, int enterpriseID);
}
