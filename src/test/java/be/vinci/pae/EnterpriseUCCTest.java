package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}