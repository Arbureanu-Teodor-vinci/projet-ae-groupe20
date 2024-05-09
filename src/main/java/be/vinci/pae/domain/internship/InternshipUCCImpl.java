package be.vinci.pae.domain.internship;

import be.vinci.pae.api.filters.BusinessException;
import be.vinci.pae.domain.academicyear.AcademicYear;
import be.vinci.pae.domain.contact.Contact;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.domain.internshipsupervisor.Supervisor;
import be.vinci.pae.services.academicyear.AcademicYearDAO;
import be.vinci.pae.services.contactservices.ContactDAO;
import be.vinci.pae.services.dal.DALTransactionServices;
import be.vinci.pae.services.internshipservices.InternshipDAO;
import be.vinci.pae.services.internshipsupervisorservices.SupervisorDAO;
import jakarta.inject.Inject;
import java.util.List;

/**
 * Implementation of InternshipUCC.
 */
public class InternshipUCCImpl implements InternshipUCC {


  @Inject
  private ContactDAO contactDAO;
  @Inject
  private InternshipDAO internshipDAO;

  @Inject
  private AcademicYearDAO academicYearDAO;

  @Inject
  private DALTransactionServices dalServices;
  @Inject
  private SupervisorDAO supervisorDAO;

  @Inject
  private DomainFactory domainFactory;

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

  @Override
  public int getNbInternships(int id) {
    if (id < 0) {
      return -1;
    }
    return internshipDAO.getNbInternships(id);
  }

  @Override
  public int getNbInternshipsPerAcademicYear(int id, String academicYear) {
    if (id < 0) {
      return -1;
    }
    int count = 0;

    List<String> academicYears = academicYearDAO.getAllAcademicYears();
    AcademicYear yearCheck = (AcademicYear) domainFactory.getAcademicYearDTO();
    yearCheck.checkAcademicYear(academicYear, academicYears);
    count = internshipDAO.getNbInternshipsPerAcademicYear(id, academicYear);
    return count;
  }
}
