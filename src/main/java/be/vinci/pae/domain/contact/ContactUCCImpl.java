package be.vinci.pae.domain.contact;

import be.vinci.pae.api.filters.BusinessException;
import be.vinci.pae.domain.academicyear.AcademicYearDTO;
import be.vinci.pae.domain.academicyear.AcademicYearUCC;
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
  private ContactDAO contactDAO;

  @Inject
  private DALTransactionServices dalServices;

  @Inject
  private AcademicYearUCC academicYearUCC;

  @Override
  public ContactDTO getOneContact(int id) {
    if (id <= 0) {
      throw new BusinessException("L'id doit être positif");
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
      throw new BusinessException("L'id doit être positif");
    }
    return contactDAO.getContactsByEnterprise(id);
  }

  @Override
  public List<ContactDTO> getContactsByUser(int id) {
    return contactDAO.getContactsByUser(id);
  }

  @Override
  public ContactDTO addContact(StudentDTO studentDTO, EnterpriseDTO enterpriseDTO) {
    ContactDTO contact;
    try {
      dalServices.startTransaction();
      List<ContactDTO> contactsExisting = contactDAO.getContactsByUser(studentDTO.getId());
      Student student = (Student) studentDTO;
      student.checkContactExists(enterpriseDTO, contactsExisting);
      student.checkContactAccepted(contactsExisting);
      //CHECK IF STUDENT ALREADY HAS A CONTACT WITH THIS ENTERPRISE AND ACADEMIC YEAR
      AcademicYearDTO academicYear = academicYearUCC.getOrAddActualAcademicYear();
      contact = contactDAO.addContact(studentDTO.getId(), enterpriseDTO.getId(),
          academicYear.getId());
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
      ContactDTO contactBeforeUpdate = contactDAO.getOneContactByid(contact.getId());
      contact.checkContactState();
      contact.checkContactStateUpdate(contactBeforeUpdate.getStateContact());
      contact.checkInterviewMethodUpdate(contactBeforeUpdate.getInterviewMethod());
      contact.checkContactRefusalReasonUpdate();
      contact.checkContactToolUpdate();
      contactDTO = contactDAO.updateContact(contact);

      //if contact id updated to accepted, all other contacts of the student are suspended
      if (contactDTO.getStateContact().equals("accepté")) {
        contactDAO.updateAllContactsOfStudentToSuspended(contactDTO.getStudent().getId());
      }
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    dalServices.commitTransaction();
    return contactDTO;
  }


}
