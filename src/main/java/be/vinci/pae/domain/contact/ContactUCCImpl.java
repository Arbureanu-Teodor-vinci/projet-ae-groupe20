package be.vinci.pae.domain.contact;

import be.vinci.pae.api.filters.BiznessException;
import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import be.vinci.pae.domain.user.Student;
import be.vinci.pae.domain.user.StudentDTO;
import be.vinci.pae.services.contactservices.ContactDAO;
import be.vinci.pae.services.dal.DALTransactionServices;
import be.vinci.pae.utils.Logger;
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
    dalServices.startTransaction();
    List<ContactDTO> contactsExisting = contactDS.getContactsByUser(studentDTO.getId());
    Student student = (Student) studentDTO;
    if (student.checkContactExists(enterpriseDTO, contactsExisting)) {
      Logger.logEntry("Contact already exists");
      dalServices.rollbackTransaction();
      throw new BiznessException("Contact already exists");
    }
    if (student.checkContactAccepted(contactsExisting)) {
      Logger.logEntry("Student already has a contact for this academic year");
      dalServices.rollbackTransaction();
      throw new BiznessException("Student already has a contact for this academic year");
    }
    ContactDTO contact = contactDS.addContact(studentDTO.getId(), enterpriseDTO.getId(),
        studentDTO.getStudentAcademicYear().getId());
    if (contact == null) {
      Logger.logEntry("Contact not added");
      dalServices.rollbackTransaction();
      throw new BiznessException("Contact not added");
    }
    dalServices.commitTransaction();
    return contact;
  }

  @Override
  public ContactDTO updateContact(ContactDTO contactDTO) {
    dalServices.startTransaction();
    Contact contact = (Contact) contactDTO;
    ContactDTO contactBeforeUpdate = contactDS.getOneContactByid(contact.getId());
    if (contactBeforeUpdate.getVersion() != contact.getVersion()) {
      dalServices.rollbackTransaction();
      throw new BiznessException("Contact update, retry");
    }

    if (!contact.checkContactState()) {
      dalServices.rollbackTransaction();
      throw new BiznessException("Cant update contact state to this value");
    }
    if (!contact.checkContactStateUpdate(contactBeforeUpdate.getStateContact())) {
      dalServices.rollbackTransaction();
      throw new BiznessException("Cant update contact state to this value from previous state");
    }
    if (!contact.checkInterviewMethodUpdate(contactBeforeUpdate.getInterviewMethod())) {
      dalServices.rollbackTransaction();
      throw new BiznessException("Cant update interview method to this value");
    }
    if (!contact.checkContactRefusalReasonUpdate()) {
      dalServices.rollbackTransaction();
      throw new BiznessException("Refusal reason needs to be updatable only on refused state");
    }
    ContactDTO contactUpdated = contactDS.updateContact(contact);
    if (contactUpdated == null) {
      dalServices.rollbackTransaction();
      throw new BiznessException("Contact not updated");
    }

    dalServices.commitTransaction();
    return contactUpdated;
  }


}
