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
  private ContactDAO contactDAO;
  @Inject
  private InternshipDAO internshipDAO;
  @Inject
  private DALTransactionServices dalServices;
  @Inject
  private SupervisorDAO supervisorDAO;

  @Override
  public InternshipDTO getOneInternshipByStudentId(int id) {
    if (id <= 0) {
      throw new BusinessException("Id must be positive");
    }
    return internshipDAO.getOneInternshipByStudentId(id);
  }

  @Override
  public InternshipDTO updateSubject(InternshipDTO internshipUpdated) {
    try {
      dalServices.startTransaction();
      InternshipDTO internshipBeforeUpdate = internshipDAO.getOneInternshipById(
          internshipUpdated.getId());
      Internship internship = (Internship) internshipUpdated;
      internship.checkOnlySubjectUpdated(internshipBeforeUpdate);
      internshipUpdated = internshipDAO.updateSubject(internshipUpdated);
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
      Contact contact = (Contact) contactDAO.getOneContactByid(
          internshipToAdd.getContact().getId());
      contact.checkIfContactIsAccepted();

      Supervisor supervisor = (Supervisor) supervisorDAO.getOneSupervisorById(
          internshipDTO.getSupervisor().getId());
      if (supervisor.getEnterprise().getId() != contact.getEnterprise().getId()) {
        throw new BusinessException("Supervisor is not from the same enterprise");
      }

      if (internshipDAO.getOneInternshipByStudentId(contact.getStudent().getId()) != null) {
        throw new BusinessException("student already has an internship");
      }
      internship = internshipDAO.addInternship(internshipToAdd);
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    dalServices.commitTransaction();
    return internship;
  }
}
