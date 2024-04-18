package be.vinci.pae.domain.contact;

import be.vinci.pae.api.filters.BusinessException;
import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import be.vinci.pae.domain.user.Student;
import be.vinci.pae.domain.user.StudentDTO;
import be.vinci.pae.services.contactservices.ContactDAO;
import be.vinci.pae.services.dal.DALTransactionServices;
import jakarta.inject.Inject;
import java.util.List;

/**
 * Implementation of ContactUCC.
 */
public class ContactUCCImpl implements ContactUCC {

  @Inject
  private ContactDAO contactDS;

  @Inject
  private DALTransactionServices dalServices;

  @Override
  public ContactDTO getOneContact(int id) {
    if (id < 0) {
      throw new BusinessException("id must be positive");
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
    ContactDTO contact = null;
    try {
      dalServices.startTransaction();
      List<ContactDTO> contactsExisting = contactDS.getContactsByUser(studentDTO.getId());
      Student student = (Student) studentDTO;
      student.checkContactExists(enterpriseDTO, contactsExisting);
      student.checkContactAccepted(contactsExisting);
      contact = contactDS.addContact(studentDTO.getId(), enterpriseDTO.getId(),
          studentDTO.getStudentAcademicYear().getId());
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    dalServices.commitTransaction();
    return contact;
  }

  @Override
  public ContactDTO updateContact(ContactDTO contactDTO) {
    try {
      dalServices.startTransaction();
      Contact contact = (Contact) contactDTO;
      ContactDTO contactBeforeUpdate = contactDS.getOneContactByid(contact.getId());
    /*if (contactBeforeUpdate.getVersion() != contact.getVersion()) {
      dalServices.rollbackTransaction();
      throw new BiznessException(
          "This contact was updated in the meantime, refresh and try again.");
    }*/
      contact.checkContactState();
      contact.checkContactStateUpdate(contactBeforeUpdate.getStateContact());
      contact.checkInterviewMethodUpdate(contactBeforeUpdate.getInterviewMethod());
      contact.checkContactRefusalReasonUpdate();
      contactDTO = contactDS.updateContact(contact);
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    dalServices.commitTransaction();
    return contactDTO;
  }


}
