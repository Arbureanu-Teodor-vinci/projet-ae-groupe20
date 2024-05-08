package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.api.filters.BusinessException;
import be.vinci.pae.domain.academicyear.AcademicYearDTO;
import be.vinci.pae.domain.contact.ContactDTO;
import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.domain.internship.InternshipDTO;
import be.vinci.pae.domain.internship.InternshipUCC;
import be.vinci.pae.domain.internshipsupervisor.SupervisorDTO;
import be.vinci.pae.domain.user.StudentDTO;
import be.vinci.pae.services.contactservices.ContactDAO;
import be.vinci.pae.services.internshipservices.InternshipDAO;
import be.vinci.pae.services.internshipsupervisorservices.SupervisorDAO;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class InternshipUCCTest {

  ServiceLocator locator = ServiceLocatorUtilities.bind(new TestsApplicationBinder());
  private InternshipUCC internshipUCC = locator.getService(InternshipUCC.class);

  private DomainFactory domainFactory = locator.getService(DomainFactory.class);
  private InternshipDTO internshipDTO = domainFactory.getInternshipDTO();
  private AcademicYearDTO academicYearDTO = domainFactory.getAcademicYearDTO();
  private EnterpriseDTO enterpriseDTO = domainFactory.getEnterpriseDTO();
  private ContactDTO validContact = domainFactory.getContactDTO();
  private SupervisorDTO supervisor = domainFactory.getSupervisorDTO();
  private StudentDTO student = domainFactory.getStudentDTO();

  private InternshipDAO internshipDAO = locator.getService(InternshipDAO.class);
  private ContactDAO contactDAO = locator.getService(ContactDAO.class);
  private SupervisorDAO supervisorDAO = locator.getService(SupervisorDAO.class);

  @BeforeEach
  void setUp() {
    student.setId(6);
    enterpriseDTO.setId(8);

    validContact.setId(1);
    validContact.setStateContact("accepté");
    validContact.setStudent(student);
    validContact.setEnterprise(enterpriseDTO);

    internshipDTO.setId(1);
    internshipDTO.setContact(validContact);
    internshipDTO.setVersion(1);
    internshipDTO.setSubject("subject");

    supervisor.setId(1);
    supervisor.setEnterprise(enterpriseDTO);

    academicYearDTO.setId(1);
    academicYearDTO.setYear("2021-2022");

    Mockito.when(internshipDAO.getOneInternshipByStudentId(3)).thenReturn(internshipDTO);
  }

  @Test
  @DisplayName("Get one internship with student id 3")
  void getOneInternship() {
    InternshipDTO actualInternship = internshipUCC.getOneInternshipByStudentId(3);

    assertEquals(internshipDTO, actualInternship);
  }

  @Test
  @DisplayName("Get one internship with student id -1")
  void getOneInternship2() {

    assertThrows(BusinessException.class, () -> internshipUCC.getOneInternshipByStudentId(-1));
  }

  @Test
  @DisplayName("Update subject of an internship")
  void updateSubject() {
    InternshipDTO internshipUpdate = domainFactory.getInternshipDTO();
    internshipUpdate.setId(internshipDTO.getId());
    internshipUpdate.setSubject("new subject");

    Mockito.when(internshipDAO.getOneInternshipById(internshipUpdate.getId()))
        .thenReturn(internshipDTO);
    Mockito.when(internshipDAO.updateSubject(internshipUpdate)).thenReturn(internshipUpdate);

    assertEquals(internshipUCC.updateSubject(internshipUpdate),
        internshipUpdate);
  }

  @Test
  @DisplayName("Add an internship")
  void addInternship() {
    InternshipDTO internship = domainFactory.getInternshipDTO();
    internship.setContact(validContact);
    internship.setSubject("subject");
    internship.setSignatureDate("2021-01-01");
    internship.setSupervisor(supervisor);
    internship.setAcademicYear(academicYearDTO);
    internship.setVersion(1);

    validContact.setEnterprise(enterpriseDTO);

    Mockito.when(contactDAO.getOneContactByid(1)).thenReturn(validContact);
    Mockito.when(supervisorDAO.getOneSupervisorById(internship.getSupervisor().getId()))
        .thenReturn(supervisor);
    Mockito.when(internshipDAO.addInternship(internship)).thenReturn(internship);

    InternshipDTO actualInternship = internshipUCC.addInternship(internship);

    assertEquals(internship, actualInternship);
  }

  @Test
  @DisplayName("Trying to add a contact that is not in accepted state")
  void addInternship2() {
    ContactDTO contactDTO = domainFactory.getContactDTO();
    contactDTO.setStateContact("suspendu");
    contactDTO.setId(2);
    contactDTO.setEnterprise(enterpriseDTO);

    InternshipDTO internship = domainFactory.getInternshipDTO();
    internship.setContact(contactDTO);
    internship.setSupervisor(supervisor);

    Mockito.when(contactDAO.getOneContactByid(2)).thenReturn(contactDTO);
    Mockito.when(supervisorDAO.getOneSupervisorById(internship.getSupervisor().getId()))
        .thenReturn(supervisor);
    Mockito.when(internshipDAO.addInternship(internship)).thenReturn(internship);

    assertThrows(BusinessException.class, () -> internshipUCC.addInternship(internship));
  }

  @Test
  @DisplayName("Trying to add a an internship while the student already has one")
  void addInternship3() {
    InternshipDTO internship = domainFactory.getInternshipDTO();
    internship.setContact(validContact);
    internship.setSupervisor(supervisor);

    InternshipDTO internshipExisting = domainFactory.getInternshipDTO();

    validContact.setStudent(student);

    Mockito.when(contactDAO.getOneContactByid(1)).thenReturn(validContact);
    Mockito.when(internshipDAO.getOneInternshipByStudentId(6)).thenReturn(internshipExisting);
    Mockito.when(supervisorDAO.getOneSupervisorById(internship.getSupervisor().getId()))
        .thenReturn(supervisor);
    Mockito.when(internshipDAO.addInternship(internship)).thenReturn(internship);

    assertThrows(BusinessException.class, () -> internshipUCC.addInternship(internship));
  }

  @Test
  @DisplayName("Trying to add a an internship while the supervisor is not from the enterprise")
  void addInternship4() {
    InternshipDTO internship = domainFactory.getInternshipDTO();
    internship.setContact(validContact);
    EnterpriseDTO supervisorEnterprise = domainFactory.getEnterpriseDTO();
    supervisorEnterprise.setId(9);

    validContact.setStudent(student);
    validContact.setEnterprise(enterpriseDTO);
    supervisor.setEnterprise(supervisorEnterprise);
    internship.setSupervisor(supervisor);

    Mockito.when(contactDAO.getOneContactByid(1)).thenReturn(validContact);
    Mockito.when(supervisorDAO.getOneSupervisorById(internship.getSupervisor().getId()))
        .thenReturn(supervisor);
    Mockito.when(internshipDAO.addInternship(internship)).thenReturn(internship);

    assertThrows(BusinessException.class, () -> internshipUCC.addInternship(internship));
  }
}
