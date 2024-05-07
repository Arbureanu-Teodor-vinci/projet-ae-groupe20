package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.api.filters.BusinessException;
import be.vinci.pae.domain.contact.ContactDTO;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.domain.internship.InternshipDTO;
import be.vinci.pae.domain.internship.InternshipUCC;
import be.vinci.pae.domain.internshipsupervisor.SupervisorDTO;
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
  private ContactDTO validContact = domainFactory.getContactDTO();
  private SupervisorDTO supervisor = domainFactory.getSupervisorDTO();
  private InternshipDAO internshipDS = locator.getService(InternshipDAO.class);
  private ContactDAO contactDS = locator.getService(ContactDAO.class);
  private SupervisorDAO supervisorDS = locator.getService(SupervisorDAO.class);

  @BeforeEach
  void setUp() {
    validContact.setId(1);
    validContact.setStateContact("accepté");

    internshipDTO.setContactId(1);
    internshipDTO.setVersion(1);
    supervisor.setEnterpriseId(8);

    Mockito.when(internshipDS.getOneInternshipByStudentId(3)).thenReturn(internshipDTO);
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
    InternshipDTO internshipUpdate = internshipDTO;
    internshipUpdate.setSubject("new subject");

    Mockito.when(internshipDS.updateSubject(internshipUpdate)).thenReturn(internshipUpdate);

    assertEquals(internshipUCC.updateSubject(internshipUpdate),
        internshipUpdate);
  }

  @Test
  @DisplayName("Add an internship")
  void addInternship() {
    InternshipDTO internship = domainFactory.getInternshipDTO();
    internship.setContactId(1);
    internship.setSubject("subject");
    internship.setSignatureDate("2021-01-01");
    internship.setSupervisorId(1);
    internship.setAcademicYear(2021);
    internship.setVersion(1);

    validContact.setEnterprise(8);

    Mockito.when(contactDS.getOneContactByid(1)).thenReturn(validContact);
    Mockito.when(supervisorDS.getOneSupervisorById(internship.getSupervisorId()))
        .thenReturn(supervisor);
    Mockito.when(internshipDS.addInternship(internship)).thenReturn(internship);

    InternshipDTO actualInternship = internshipUCC.addInternship(internship);

    assertEquals(internship, actualInternship);
  }

  @Test
  @DisplayName("Trying to add a contact that is not in accepted state")
  void addInternship2() {
    ContactDTO contactDTO = domainFactory.getContactDTO();
    contactDTO.setStateContact("suspendu");
    contactDTO.setId(2);
    contactDTO.setEnterprise(8);

    InternshipDTO internship = domainFactory.getInternshipDTO();
    internship.setContactId(2);

    Mockito.when(contactDS.getOneContactByid(2)).thenReturn(contactDTO);
    Mockito.when(supervisorDS.getOneSupervisorById(internship.getSupervisorId()))
        .thenReturn(supervisor);
    Mockito.when(internshipDS.addInternship(internship)).thenReturn(internship);

    assertThrows(BusinessException.class, () -> internshipUCC.addInternship(internship));
  }

  @Test
  @DisplayName("Trying to add a an internship while the stuendent already has one")
  void addInternship3() {
    InternshipDTO internship = domainFactory.getInternshipDTO();
    internship.setContactId(1);

    InternshipDTO internshipExisting = domainFactory.getInternshipDTO();

    validContact.setStudent(6);

    Mockito.when(contactDS.getOneContactByid(1)).thenReturn(validContact);
    Mockito.when(internshipDS.getOneInternshipByStudentId(6)).thenReturn(internshipExisting);
    Mockito.when(supervisorDS.getOneSupervisorById(internship.getSupervisorId()))
        .thenReturn(supervisor);
    Mockito.when(internshipDS.addInternship(internship)).thenReturn(internship);

    assertThrows(BusinessException.class, () -> internshipUCC.addInternship(internship));
  }

  @Test
  @DisplayName("Trying to add a an internship while the supervisor is not from the enterprise")
  void addInternship4() {
    InternshipDTO internship = domainFactory.getInternshipDTO();
    internship.setContactId(1);

    validContact.setStudent(6);
    validContact.setEnterprise(8);
    supervisor.setEnterpriseId(9);

    Mockito.when(contactDS.getOneContactByid(1)).thenReturn(validContact);
    Mockito.when(supervisorDS.getOneSupervisorById(internship.getSupervisorId()))
        .thenReturn(supervisor);
    Mockito.when(internshipDS.addInternship(internship)).thenReturn(internship);

    assertThrows(BusinessException.class, () -> internshipUCC.addInternship(internship));
  }
}
