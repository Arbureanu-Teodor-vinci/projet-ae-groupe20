package be.vinci.pae.domain.contact;

import be.vinci.pae.api.filters.BusinessException;
import be.vinci.pae.domain.academicyear.AcademicYearDTO;
import be.vinci.pae.domain.academicyear.AcademicYearUCC;
import be.vinci.pae.domain.user.Student;
import be.vinci.pae.services.contactservices.ContactDAO;
import be.vinci.pae.services.dal.DALTransactionServices;
import jakarta.inject.Inject;
import java.util.List;

/**
 * Implementation of ContactUCC.
 */
public class ContactUCCImpl implements ContactUCC {

  @Inject
  private ContactDAO contactDAO;

  @Inject
  private DALTransactionServices dalServices;

  @Inject
  private AcademicYearUCC academicYearUCC;

  @Override
  public ContactDTO getOneContact(int id) {
    if (id <= 0) {
      throw new BusinessException("id must be positive");
    }

    return contactDAO.getOneContactByid(id);
  }

  @Override
  public List<ContactDTO> getAllContacts() {
    return contactDAO.getAllContacts();
  }

  @Override
  public List<ContactDTO> getContactsByEnterprise(int id) {
    if (id <= 0) {
      throw new BusinessException("id must be positive");
    }
    return contactDAO.getContactsByEnterprise(id);
  }

  @Override
  public List<ContactDTO> getContactsByUser(int id) {
    return contactDAO.getContactsByUser(id);
  }

  @Override
  public ContactDTO addContact(ContactDTO contactDTO) {
    try {
      // Start transaction.
      dalServices.startTransaction();
      // Check if contact already exists.
      List<ContactDTO> contactsExisting = contactDAO.getContactsByUser(
          contactDTO.getStudent().getId());
      // Cast studentDTO to student to acces the business checks methods.
      Student student = (Student) contactDTO.getStudent();
      AcademicYearDTO academicYear = academicYearUCC.getOrAddActualAcademicYear();
      student.checkContactExists(contactDTO.getEnterprise(), academicYear, contactsExisting);
      student.checkContactAccepted(contactsExisting, academicYear);
      // If the checks are good we can add the contact
      contactDTO = contactDAO.addContact(contactDTO.getStudent().getId(),
          contactDTO.getEnterprise().getId(),
          academicYear.getId());
    } catch (Throwable e) {
      // If an error occurs we rollback the transaction.
      dalServices.rollbackTransaction();
      throw e;
    }
    // If everything is good we commit the transaction.
    dalServices.commitTransaction();
    return contactDTO;
  }

  @Override
  public ContactDTO updateContact(ContactDTO contactDTO) {
    try {
      dalServices.startTransaction();
      // Cast contactDTO to contact to acces the business checks methods.
      Contact contact = (Contact) contactDTO;
      Contact contactBeforeUpdate = (Contact) contactDAO.getOneContactByid(contact.getId());
      // Multiple buisness checks on the contact.
      contact.checkContactState();
      contact.checkContactStateUpdate(contactBeforeUpdate.getStateContact());
      contact.checkInterviewMethodUpdate(contactBeforeUpdate.getInterviewMethod());
      contact.checkContactRefusalReasonUpdate();
      contactBeforeUpdate.checkContactAcademicYear();
      contactDTO = contactDAO.updateContact(contact);

      //if contact id updated to accepted, all other contacts of the student are suspended
      if (contactDTO.getStateContact().equals("accepté")) {
        contactDAO.updateAllContactsOfStudentToSuspended(contactDTO.getStudent().getId());
      }
    } catch (Throwable e) {
      // If an error occurs we rollback the transaction.
      dalServices.rollbackTransaction();
      throw e;
    }
    // If everything is good we commit the transaction.
    dalServices.commitTransaction();
    return contactDTO;
  }


}
