package be.vinci.pae.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import be.vinci.pae.TestsApplicationBinder;
import be.vinci.pae.services.EnterpriseDAO;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EnterpriseUCCTest {

  ServiceLocator locator = ServiceLocatorUtilities.bind(new TestsApplicationBinder());
  private EnterpriseUCC enterpriseUCC = locator.getService(EnterpriseUCC.class);
  private EnterpriseDAO enterpriseDAO = locator.getService(EnterpriseDAO.class);
  private DomainFactory domainFactory = locator.getService(DomainFactory.class);
  private EnterpriseDTO enterpriseDTO = domainFactory.getEnterpriseDTO();


  @BeforeEach
  void setUp() {
    enterpriseDTO.setId(1);

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
}