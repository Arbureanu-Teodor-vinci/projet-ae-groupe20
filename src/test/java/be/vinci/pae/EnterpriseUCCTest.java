package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.api.filters.BusinessException;
import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import be.vinci.pae.domain.enterprise.EnterpriseUCC;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.services.enterpriseservices.EnterpriseDAO;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * EnterpriseUCC test class.
 */
class EnterpriseUCCTest {

  ServiceLocator locator = ServiceLocatorUtilities.bind(new TestsApplicationBinder());
  private EnterpriseUCC enterpriseUCC = locator.getService(EnterpriseUCC.class);
  private EnterpriseDAO enterpriseDAO = locator.getService(EnterpriseDAO.class);
  private DomainFactory domainFactory = locator.getService(DomainFactory.class);
  private EnterpriseDTO enterpriseDTO = domainFactory.getEnterpriseDTO();
  private EnterpriseDTO enterpriseDTO2 = domainFactory.getEnterpriseDTO();


  @BeforeEach
  void setUp() {
    enterpriseDTO.setId(1);
    enterpriseDTO2.setId(2);

    Mockito.when(enterpriseDAO.getOneEnterpriseByid(1)).thenReturn(enterpriseDTO);
  }

  @Test
  @DisplayName("Get one enterprise with id 1")
  void getOneEnterprise() {
    EnterpriseDTO actualEnterprise = enterpriseUCC.getOneEnterprise(1);

    assertEquals(enterpriseDTO, actualEnterprise);
  }

  @Test
  @DisplayName("Get one enterprise with id -1")
  void getOneEnterprise2() {
    EnterpriseDTO actualEnterprise = enterpriseUCC.getOneEnterprise(-1);

    assertEquals(null, actualEnterprise);
  }

  @Test
  @DisplayName("Get one enterprise with an id not in the database")
  void getOneEnterprise3() {
    EnterpriseDTO actualEnterprise = enterpriseUCC.getOneEnterprise(999);

    assertEquals(null, actualEnterprise);
  }

  @Test
  @DisplayName("Get all enterprises")
  void getAllEnterprises() {
    List<EnterpriseDTO> expectedEnterprises = new ArrayList<>();
    expectedEnterprises.add(enterpriseDTO);
    expectedEnterprises.add(enterpriseDTO2);

    // Mocking the method call to the DAO layer to return the expected enterprises
    Mockito.when(enterpriseDAO.getAllEnterprises()).thenReturn(expectedEnterprises);

    // Getting the actual enterprises from the UCC layer
    List<EnterpriseDTO> actualEnterprises = enterpriseUCC.getAllEnterprises();

    // Asserting that the actual enterprises are equal to the expected enterprises
    assertEquals(expectedEnterprises, actualEnterprises);
  }

  @Test
  @DisplayName("Get all enterprises with no enterprises in the database")
  void getAllEnterprises2() {
    List<EnterpriseDTO> expectedEnterprises = new ArrayList<>();

    Mockito.when(enterpriseDAO.getAllEnterprises()).thenReturn(expectedEnterprises);

    List<EnterpriseDTO> actualEnterprises = enterpriseUCC.getAllEnterprises();

    assertEquals(expectedEnterprises, actualEnterprises);
  }

  @Test
  @DisplayName("Get number of internships for valid enterprise id")
  void getNbInternshipsValidId() {
    int id = 1;
    int expectedNbInternships = 5;

    Mockito.when(enterpriseDAO.getNbInternships(id)).thenReturn(expectedNbInternships);

    int actualNbInternships = enterpriseUCC.getNbInternships(id);

    assertEquals(expectedNbInternships, actualNbInternships);
  }

  @Test
  @DisplayName("Get number of internships for invalid enterprise id")
  void getNbInternshipsInvalidId() {
    int id = -1;

    int actualNbInternships = enterpriseUCC.getNbInternships(id);

    assertEquals(-1, actualNbInternships);
  }

  @Test
  @DisplayName("Add enterprise with valid credentials")
  void addEnterprise() {
    EnterpriseDTO enterpriseDTO = domainFactory.getEnterpriseDTO();
    enterpriseDTO.setTradeName("Trade Name");
    enterpriseDTO.setDesignation("Designation");
    enterpriseDTO.setAddress("Address");
    enterpriseDTO.setPhoneNumber("0123456789");
    enterpriseDTO.setEmail("Test@test.com");

    Mockito.when(enterpriseDAO.getAllEnterprises()).thenReturn(new ArrayList<>());
    Mockito.when(enterpriseDAO.addEnterprise(enterpriseDTO)).thenReturn(enterpriseDTO);

    assertEquals(enterpriseDTO, enterpriseUCC.addEnterprise(enterpriseDTO));
  }

  @Test
  @DisplayName("Add enterprise with invalid email")
  void addEnterprise2() {
    EnterpriseDTO enterpriseDTO = domainFactory.getEnterpriseDTO();
    enterpriseDTO.setTradeName("Trade Name");
    enterpriseDTO.setDesignation("Designation");
    enterpriseDTO.setAddress("Address");
    enterpriseDTO.setPhoneNumber("0123456789");
    enterpriseDTO.setEmail("Testtest.com");

    Mockito.when(enterpriseDAO.getAllEnterprises()).thenReturn(new ArrayList<>());
    Mockito.when(enterpriseDAO.addEnterprise(enterpriseDTO)).thenReturn(enterpriseDTO);

    assertThrows(BusinessException.class, () -> enterpriseUCC.addEnterprise(enterpriseDTO));
  }

  @Test
  @DisplayName("Add enterprise that already exists in the db with the same name and designation")
  void addEnterprise3() {
    EnterpriseDTO enterpriseDTO = domainFactory.getEnterpriseDTO();
    enterpriseDTO.setTradeName("Trade Name");
    enterpriseDTO.setDesignation("Designation");
    enterpriseDTO.setAddress("Address");
    enterpriseDTO.setPhoneNumber("0123456789");
    enterpriseDTO.setEmail("test@test.com");

    EnterpriseDTO enterpriseDTO2 = domainFactory.getEnterpriseDTO();
    enterpriseDTO2.setTradeName("Trade Name");
    enterpriseDTO2.setDesignation("Designation");
    enterpriseDTO2.setAddress("Address");
    enterpriseDTO2.setPhoneNumber("0123456789");
    enterpriseDTO2.setEmail("test2@gmail.com");

    List<EnterpriseDTO> enterprises = new ArrayList<>();
    enterprises.add(enterpriseDTO2);

    Mockito.when(enterpriseDAO.getAllEnterprises()).thenReturn(enterprises);
    Mockito.when(enterpriseDAO.addEnterprise(enterpriseDTO)).thenReturn(enterpriseDTO);

    assertThrows(BusinessException.class, () -> enterpriseUCC.addEnterprise(enterpriseDTO));
  }

  @Test
  @DisplayName("Add enterprise with invalid phone number")
  void addTest4() {
    EnterpriseDTO enterpriseDTO = domainFactory.getEnterpriseDTO();
    enterpriseDTO.setTradeName("Trade Name");
    enterpriseDTO.setDesignation("Designation");
    enterpriseDTO.setAddress("Address");
    enterpriseDTO.setPhoneNumber("0123456789");
    enterpriseDTO.setEmail("test@test.com");

    EnterpriseDTO enterpriseDTO2 = domainFactory.getEnterpriseDTO();
    enterpriseDTO2.setTradeName("Trade Name");
    enterpriseDTO2.setDesignation("Designation 2");
    enterpriseDTO2.setAddress("Address");
    enterpriseDTO2.setPhoneNumber("0123456789");
    enterpriseDTO2.setEmail("test2@gmail.com");

    List<EnterpriseDTO> enterprises = new ArrayList<>();
    enterprises.add(enterpriseDTO2);

    Mockito.when(enterpriseDAO.getAllEnterprises()).thenReturn(enterprises);
    Mockito.when(enterpriseDAO.addEnterprise(enterpriseDTO)).thenReturn(enterpriseDTO);

    assertEquals(enterpriseDTO, enterpriseUCC.addEnterprise(enterpriseDTO));
  }

  @Test
  @DisplayName("Add enterprise with invalid phone number")
  void addEterprise5() {
    EnterpriseDTO enterpriseDTO = domainFactory.getEnterpriseDTO();
    enterpriseDTO.setTradeName("Trade Name");
    enterpriseDTO.setDesignation("Designation");
    enterpriseDTO.setAddress("Address");
    enterpriseDTO.setPhoneNumber("a123456789");
    enterpriseDTO.setEmail("Test@test.com");

    Mockito.when(enterpriseDAO.getAllEnterprises()).thenReturn(new ArrayList<>());
    Mockito.when(enterpriseDAO.addEnterprise(enterpriseDTO)).thenReturn(enterpriseDTO);

    assertThrows(BusinessException.class, () -> enterpriseUCC.addEnterprise(enterpriseDTO));
  }

  @Test
  @DisplayName("Blacklist a valid enterprise")
  void blacklistEnterprise1() {
    EnterpriseDTO enterpriseDTO = domainFactory.getEnterpriseDTO();
    enterpriseDTO.setId(1);
    enterpriseDTO.setBlackListed(false);
    EnterpriseDTO updatedEnterpriseDTO = domainFactory.getEnterpriseDTO();
    updatedEnterpriseDTO.setId(1);
    updatedEnterpriseDTO.setBlackListed(true);
    updatedEnterpriseDTO.setBlackListMotivation("Motivation");

    Mockito.when(enterpriseDAO.getOneEnterpriseByid(1)).thenReturn(enterpriseDTO);
    Mockito.when(enterpriseDAO.updateEnterprise(updatedEnterpriseDTO))
        .thenReturn(updatedEnterpriseDTO);

    assertNotEquals(enterpriseUCC.blacklistEnterprise(updatedEnterpriseDTO), enterpriseDTO);
    assertTrue(enterpriseUCC.blacklistEnterprise(updatedEnterpriseDTO).isBlackListed());
  }

  @Test
  @DisplayName("Blacklist an enterprise that is already blacklisted")
  void blacklistEnterprise2() {
    EnterpriseDTO enterpriseDTO = domainFactory.getEnterpriseDTO();
    enterpriseDTO.setId(1);
    enterpriseDTO.setBlackListed(true);

    Mockito.when(enterpriseDAO.getOneEnterpriseByid(1)).thenReturn(enterpriseDTO);

    assertThrows(BusinessException.class, () -> enterpriseUCC.blacklistEnterprise(enterpriseDTO));
  }

  @Test
  @DisplayName("Blacklist an enterprise with no blacklist motivation")
  void blacklistEnterprise3() {
    EnterpriseDTO enterpriseDTO = domainFactory.getEnterpriseDTO();
    enterpriseDTO.setId(1);
    enterpriseDTO.setBlackListed(true);
    enterpriseDTO.setBlackListMotivation(null);

    Mockito.when(enterpriseDAO.getOneEnterpriseByid(1)).thenReturn(enterpriseDTO);

    assertThrows(BusinessException.class, () -> enterpriseUCC.blacklistEnterprise(enterpriseDTO));
  }

  @Test
  @DisplayName("Blacklist an enterprise that does not exist")
  void blacklistEnterprise4() {
    EnterpriseDTO enterpriseDTO = domainFactory.getEnterpriseDTO();
    enterpriseDTO.setId(999);

    Mockito.when(enterpriseDAO.getOneEnterpriseByid(999)).thenReturn(null);

    assertThrows(NullPointerException.class,
        () -> enterpriseUCC.blacklistEnterprise(enterpriseDTO));
  }
}