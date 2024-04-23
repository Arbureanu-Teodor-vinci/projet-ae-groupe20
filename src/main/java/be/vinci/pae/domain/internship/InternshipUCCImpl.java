package be.vinci.pae.domain.internship;

import be.vinci.pae.api.filters.BusinessException;
import be.vinci.pae.domain.contact.Contact;
import be.vinci.pae.domain.internshipsupervisor.Supervisor;
import be.vinci.pae.services.contactservices.ContactDAO;
import be.vinci.pae.services.dal.DALTransactionServices;
import be.vinci.pae.services.internshipservices.InternshipDAO;
import be.vinci.pae.services.internshipsupervisorservices.SupervisorDAO;
import jakarta.inject.Inject;

public class InternshipUCCImpl implements InternshipUCC {


  @Inject
  ContactDAO contactDS;
  @Inject
  private InternshipDAO internshipDS;
  @Inject
  private DALTransactionServices dalServices;
  @Inject
  private SupervisorDAO supervisorDS;

  @Override
  public InternshipDTO getOneInternshipByStudentId(int id) {
    if (id <= 0) {
      throw new BusinessException("Id must be positive");
    }
    return internshipDS.getOneInternshipByStudentId(id);
  }

  @Override
  public InternshipDTO updateSubject(InternshipDTO internshipUpdated) {
    try {
      dalServices.startTransaction();
      internshipDS.updateSubject(internshipUpdated);
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    dalServices.commitTransaction();
    return internshipUpdated;
  }

  @Override
  public InternshipDTO addInternship(InternshipDTO internshipDTO) {
    InternshipDTO internship = null;
    try {
      dalServices.startTransaction();
      Internship internshipToAdd = (Internship) internshipDTO;
      Contact contact = (Contact) contactDS.getOneContactByid(internshipToAdd.getContactId());
      contact.checkIfContactIsTaken();

      Supervisor supervisor = (Supervisor) supervisorDS.getOneSupervisorById(
          internshipDTO.getSupervisorId());
      if (supervisor.getEnterpriseId() != contact.getEnterpriseId()) {
        throw new BusinessException("Supervisor is not from the enterprise");
      }

      if (internshipDS.getOneInternshipByStudentId(contact.getStudentId()) != null) {
        throw new BusinessException("student already has an internship");
      }
      internship = internshipDS.addInternship(internshipToAdd);
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    dalServices.commitTransaction();
    return internship;
  }
}
