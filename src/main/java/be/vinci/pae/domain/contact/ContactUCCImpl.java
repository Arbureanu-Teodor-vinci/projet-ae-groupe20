package be.vinci.pae.domain.contact;

import be.vinci.pae.api.filters.BiznessException;
import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import be.vinci.pae.domain.user.StudentDTO;
import be.vinci.pae.services.contactservices.ContactDAO;
import jakarta.inject.Inject;
import java.util.List;

/**
 * Implementation of ContactUCC.
 */
public class ContactUCCImpl implements ContactUCC {

  @Inject
  ContactDAO contactDS;

  @Override
  public ContactDTO getOneContact(int id) {
    if (id < 0) {
      throw new BiznessException("id must be positive");
    }

    return contactDS.getOneContactByid(id);
  }

  @Override
  public List<ContactDTO> getAllContacts() {
    return contactDS.getAllContacts();
  }

  @Override
  public List<ContactDTO> getContactsByUser(int id) {
    return contactDS.getContactsByUser(id);
  }

  @Override
  public ContactDTO addContact(StudentDTO studentDTO, EnterpriseDTO enterpriseDTO) {
    int academicYearId = studentDTO.getStudentAcademicYear().getId();
    List<ContactDTO> contactsExisting = contactDS.getContactsByUser(studentDTO.getId());
    for (ContactDTO contact : contactsExisting) {
      if (contact.getEnterpriseId() == enterpriseDTO.getId()
          && contact.getAcademicYear() == academicYearId) {
        throw new BiznessException("Contact already exists");
      }
    }
    return contactDS.addContact(studentDTO.getId(), enterpriseDTO.getId(), academicYearId);
  }

  @Override
  public ContactDTO updateContact(ContactDTO contact) {
    return contactDS.updateContact(contact);
  }


}
