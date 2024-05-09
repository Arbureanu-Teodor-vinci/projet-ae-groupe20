package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.api.filters.BusinessException;
import be.vinci.pae.domain.contact.ContactDTO;
import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.domain.internshipsupervisor.SupervisorDTO;
import be.vinci.pae.domain.internshipsupervisor.SupervisorUCC;
import be.vinci.pae.domain.user.StudentDTO;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.services.contactservices.ContactDAO;
import be.vinci.pae.services.internshipsupervisorservices.SupervisorDAO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * SupervisorTest.
 */
public class SupervisorUCCTest {

  ServiceLocator locator = ServiceLocatorUtilities.bind(new TestsApplicationBinder());
  private SupervisorUCC supervisorUCC = locator.getService(SupervisorUCC.class);
  private SupervisorDAO supervisorDAO = locator.getService(SupervisorDAO.class);
  private DomainFactory domainFactory = locator.getService(DomainFactory.class);
  private SupervisorDTO supervisorDTO = domainFactory.getSupervisorDTO();
  private EnterpriseDTO enterpriseDTO = domainFactory.getEnterpriseDTO();
  private EnterpriseDTO blacklistedEnterpriseDTO = domainFactory.getEnterpriseDTO();
  private UserDTO userDTO = domainFactory.getUserDTO();
  private StudentDTO studentDTO = domainFactory.getStudentDTO();
  private ContactDTO contactDTO = domainFactory.getContactDTO();
  private ContactDAO contactDAO = locator.getService(ContactDAO.class);

  /**
   * Setup.
   */
  @BeforeEach
  public void setup() {
    supervisorDTO.setId(1);
    supervisorDTO.setEmail("John@gmail.com");
    userDTO.setId(1);
    userDTO.setRole("Professeur");

    studentDTO.setId(2);
    studentDTO.setRole("Etudiant");

    Mockito.when(supervisorDAO.getOneSupervisorById(1)).thenReturn(supervisorDTO);
    Mockito.when(supervisorDAO.getOneSupervisorByEmail("John@gmail.com")).thenReturn(supervisorDTO);

    enterpriseDTO.setId(1);
    blacklistedEnterpriseDTO.setId(2);

    supervisorDTO.setEnterprise(enterpriseDTO);

    contactDTO.setId(1);
    contactDTO.setEnterprise(enterpriseDTO);
    contactDTO.setStateContact("accepté");
    contactDTO.setStudent(studentDTO);

    List<ContactDTO> contacts = new ArrayList<>();
    contacts.add(contactDTO);

    Mockito.when(contactDAO.getContactsByUser(2)).thenReturn(contacts);

  }

  @Test
  @DisplayName("Test if the supervisor is returned when the id = 1 returns the supervisorDTO")
  public void testGetOneInternshipSupervisor() {
    assertEquals(supervisorDTO, supervisorUCC.getOneSupervisorById(1));
  }

  @Test
  @DisplayName("Test getOneInternshipSupervisor with an id <= 0")
  public void testGetOneInternshipSupervisorWithNegativeId() {
    assertAll(
        () -> assertThrows(BusinessException.class, () -> supervisorUCC.getOneSupervisorById(-1)),
        () -> assertThrows(BusinessException.class, () -> supervisorUCC.getOneSupervisorById(0))
    );
  }

  @Test
  @DisplayName("Test if the id is not in the database")
  public void testGetOneInternshipSupervisorWithIdNotInDatabase() {
    assertEquals(null, supervisorUCC.getOneSupervisorById(999));
  }

  @Test
  @DisplayName("Add a supervisor with valid data")
  public void testAddSupervisor1() {
    SupervisorDTO supervisorToAdd = domainFactory.getSupervisorDTO();
    supervisorToAdd.setEmail("test@test.gmail.com");
    supervisorToAdd.setFirstName("Test");
    supervisorToAdd.setLastName("Test");
    supervisorToAdd.setPhoneNumber("0123456789");

    Mockito.when(supervisorDAO.addSupervisor(supervisorToAdd))
        .thenReturn(supervisorToAdd);
    assertEquals(supervisorToAdd,
        supervisorUCC.addSupervisor(supervisorToAdd, userDTO));

  }

  @Test
  @DisplayName("Add a supervisor with valid data and a student user")
  public void testAddSupervisorStudent() {
    SupervisorDTO supervisorToAdd = domainFactory.getSupervisorDTO();
    supervisorToAdd.setEmail("test@test.gmail.com");
    supervisorToAdd.setFirstName("Test");
    supervisorToAdd.setLastName("Test");
    supervisorToAdd.setPhoneNumber("0123456789");
    supervisorToAdd.setEnterprise(enterpriseDTO);

    Mockito.when(contactDAO.addContact(1, 1, 1)).thenReturn(contactDTO);
    Mockito.when(supervisorDAO.addSupervisor(supervisorToAdd))
        .thenReturn(supervisorToAdd);
    assertEquals(supervisorToAdd,
        supervisorUCC.addSupervisor(supervisorToAdd, studentDTO));

  }

  @Test
  @DisplayName("Add a supervisor with valid data and a student user but not accepted by the enterprise")
  public void testAddSupervisorStudentNotAccepted() {
    SupervisorDTO supervisorToAdd = domainFactory.getSupervisorDTO();
    supervisorToAdd.setEmail("test@test.gmail.com");
    supervisorToAdd.setFirstName("Test");
    supervisorToAdd.setLastName("Test");
    supervisorToAdd.setPhoneNumber("0123456789");
    supervisorToAdd.setEnterprise(enterpriseDTO);

    contactDTO.setStateContact("pris");

    Mockito.when(contactDAO.addContact(1, 1, 1)).thenReturn(contactDTO);
    Mockito.when(supervisorDAO.addSupervisor(supervisorToAdd))
        .thenReturn(supervisorToAdd);

    assertThrows(BusinessException.class, () ->
        supervisorUCC.addSupervisor(supervisorToAdd, studentDTO));

  }

  @Test
  @DisplayName("Add a supervisor with email that already exists")
  public void testAddSupervisor2() {
    SupervisorDTO supervisorToAdd = domainFactory.getSupervisorDTO();
    supervisorToAdd.setLastName("Test");
    supervisorToAdd.setFirstName("Test");
    supervisorToAdd.setEmail("test@test.gmail");
    supervisorToAdd.setEnterprise(enterpriseDTO);

    Mockito.when(supervisorDAO.getOneSupervisorByEmail("test@test.gmail"))
        .thenReturn(supervisorToAdd);

    assertThrows(BusinessException.class,
        () -> supervisorUCC.addSupervisor(supervisorToAdd, studentDTO));
  }

  @Test
  @DisplayName("Add a supervisor with a phone number that is not valid")
  public void testAddSupervisor3() {
    SupervisorDTO supervisorToAdd = domainFactory.getSupervisorDTO();
    supervisorToAdd.setEmail("test@test.gmail");
    supervisorToAdd.setFirstName("Test");
    supervisorToAdd.setLastName("Test");
    supervisorToAdd.setPhoneNumber("aaaaaaaaa");
    supervisorToAdd.setEnterprise(enterpriseDTO);

    assertThrows(BusinessException.class,
        () -> supervisorUCC.addSupervisor(supervisorToAdd, studentDTO));
  }

  @Test
  @DisplayName("Add a supervisor with a first name that is not valid")
  public void testAddSupervisor4() {
    SupervisorDTO supervisorToAdd = domainFactory.getSupervisorDTO();
    supervisorToAdd.setEmail("test@test.gmail");
    supervisorToAdd.setFirstName("test");
    supervisorToAdd.setLastName("Test");
    supervisorToAdd.setPhoneNumber("0123456789");
    supervisorToAdd.setEnterprise(enterpriseDTO);

    assertThrows(BusinessException.class,
        () -> supervisorUCC.addSupervisor(supervisorToAdd, studentDTO));
  }

  @Test
  @DisplayName("Add a supervisor with a last name that is not valid")
  public void testAddSupervisor5() {
    SupervisorDTO supervisorToAdd = domainFactory.getSupervisorDTO();
    supervisorToAdd.setEmail("test@test.gmail");
    supervisorToAdd.setFirstName("Test");
    supervisorToAdd.setLastName("test");
    supervisorToAdd.setPhoneNumber("0123456789");
    supervisorToAdd.setEnterprise(enterpriseDTO);

    assertThrows(BusinessException.class,
        () -> supervisorUCC.addSupervisor(supervisorToAdd, studentDTO));
  }

  @Test
  @DisplayName("Add a supervisor with a email not valid")
  public void testAddSupervisor6() {
    SupervisorDTO supervisorToAdd = domainFactory.getSupervisorDTO();
    supervisorToAdd.setEmail("test");
    supervisorToAdd.setFirstName("Test");
    supervisorToAdd.setLastName("Test");
    supervisorToAdd.setPhoneNumber("0123456789");
    supervisorToAdd.setEnterprise(enterpriseDTO);

    assertThrows(BusinessException.class,
        () -> supervisorUCC.addSupervisor(supervisorToAdd, studentDTO));
  }

  @Test
  @DisplayName("Get all internship supervisors returns list of supervisors")
  public void getAllInternshipSupervisors1() {
    List<SupervisorDTO> expectedSupervisors = Arrays.asList(supervisorDTO);
    Mockito.when(supervisorDAO.getAllSupervisors()).thenReturn(expectedSupervisors);

    List<SupervisorDTO> actualSupervisors = supervisorUCC.getAllInternshipSupervisors();

    assertEquals(expectedSupervisors, actualSupervisors);
  }

  @Test
  @DisplayName("Get all internship supervisors returns empty list when no supervisors")
  public void getAllInternshipSupervisors2() {
    List<SupervisorDTO> expectedSupervisors = new ArrayList<>();
    Mockito.when(supervisorDAO.getAllSupervisors()).thenReturn(expectedSupervisors);

    List<SupervisorDTO> actualSupervisors = supervisorUCC.getAllInternshipSupervisors();

    assertEquals(expectedSupervisors, actualSupervisors);
  }

  @Test
  @DisplayName("Get all internship supervisors returns list of supervisors")
  public void getAllInternshipSupervisors3() {
    List<SupervisorDTO> expectedSupervisors = Arrays.asList(supervisorDTO);
    Mockito.when(supervisorDAO.getAllSupervisors()).thenReturn(expectedSupervisors);

    List<SupervisorDTO> actualSupervisors = supervisorUCC.getAllInternshipSupervisors();

    assertEquals(expectedSupervisors, actualSupervisors);
  }

  @Test
  @DisplayName("Get all internship supervisors returns empty list when no supervisors")
  public void getAllInternshipSupervisors4() {
    List<SupervisorDTO> expectedSupervisors = new ArrayList<>();
    Mockito.when(supervisorDAO.getAllSupervisors()).thenReturn(expectedSupervisors);

    List<SupervisorDTO> actualSupervisors = supervisorUCC.getAllInternshipSupervisors();

    assertEquals(expectedSupervisors, actualSupervisors);
  }

  @Test
  @DisplayName("Get supervisors by enterprise returns list of supervisors")
  public void getSupervisorsByEnterpriseReturnsListOfSupervisors() {
    List<SupervisorDTO> expectedSupervisors = Arrays.asList(supervisorDTO);
    Mockito.when(supervisorDAO.getSupervisorsByEnterprise(1)).thenReturn(expectedSupervisors);

    List<SupervisorDTO> actualSupervisors = supervisorUCC.getSupervisorsByEnterprise(1);

    assertEquals(expectedSupervisors, actualSupervisors);
  }

  @Test
  @DisplayName("Get supervisors by enterprise returns empty list when no supervisors")
  public void getSupervisorsByEnterpriseReturnsEmptyListWhenNoSupervisors() {
    List<SupervisorDTO> expectedSupervisors = new ArrayList<>();
    Mockito.when(supervisorDAO.getSupervisorsByEnterprise(1)).thenReturn(expectedSupervisors);

    List<SupervisorDTO> actualSupervisors = supervisorUCC.getSupervisorsByEnterprise(1);

    assertEquals(expectedSupervisors, actualSupervisors);
  }

  @Test
  @DisplayName("Get supervisors by enterprise throws exception for invalid enterprise id")
  public void getSupervisorsByEnterpriseThrowsExceptionForInvalidEnterpriseId() {
    assertAll(
        () -> assertThrows(BusinessException.class,
            () -> supervisorUCC.getSupervisorsByEnterprise(-1)),
        () -> assertThrows(BusinessException.class,
            () -> supervisorUCC.getSupervisorsByEnterprise(0))
    );
  }
}
