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
  ContactDAO contactDS;
  @Inject
  private InternshipDAO internshipDS;

  @Inject
  private AcademicYearDAO academicYearDAO;

  @Inject
  private DALTransactionServices dalServices;
  @Inject
  private SupervisorDAO supervisorDS;

  @Inject
  private DomainFactory domainFactory;

  @Override
  public InternshipDTO getOneInternshipByStudentId(int id) {
    if (id <= 0) {
      throw new BusinessException("Id must be positive");
    }
    return internshipDS.getOneInternshipByStudentId(id);
  }

  @Override
  public InternshipDTO updateSubject(InternshipDTO internshipUpdated) {
    internshipUpdated = internshipDS.updateSubject(internshipUpdated);
    return internshipUpdated;
  }

  @Override
  public InternshipDTO addInternship(InternshipDTO internshipDTO) {
    InternshipDTO internship = null;
    try {
      dalServices.startTransaction();
      Internship internshipToAdd = (Internship) internshipDTO;
      Contact contact = (Contact) contactDS.getOneContactByid(internshipToAdd.getContactId());
      contact.checkIfContactIsAccepted();

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

  @Override
  public int getNbInternships(int id) {
    if (id < 0) {
      return -1;
    }
    return internshipDS.getNbInternships(id);
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
    count = internshipDS.getNbInternshipsPerAcademicYear(id, academicYear);
    return count;
  }
}
