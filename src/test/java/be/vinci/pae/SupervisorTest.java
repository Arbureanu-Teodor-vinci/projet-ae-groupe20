package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.api.filters.BiznessException;
import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.domain.internshipsupervisor.SupervisorDTO;
import be.vinci.pae.domain.internshipsupervisor.SupervisorUCC;
import be.vinci.pae.services.internshipSupervisorServices.SupervisorDAO;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class SupervisorTest {

  ServiceLocator locator = ServiceLocatorUtilities.bind(new TestsApplicationBinder());
  private SupervisorUCC supervisorUCC = locator.getService(SupervisorUCC.class);
  private SupervisorDAO supervisorDAO = locator.getService(SupervisorDAO.class);
  private DomainFactory domainFactory = locator.getService(DomainFactory.class);
  private SupervisorDTO supervisorDTO = domainFactory.getSupervisorDTO();
  private EnterpriseDTO enterpriseDTO = domainFactory.getEnterpriseDTO();
  private EnterpriseDTO blacklistedEnterpriseDTO = domainFactory.getEnterpriseDTO();

  @BeforeEach
  public void setup() {
    supervisorDTO.setId(1);
    supervisorDTO.setEmail("John@gmail.com");

    Mockito.when(supervisorDAO.getOneSupervisorById(1)).thenReturn(supervisorDTO);
    Mockito.when(supervisorDAO.getOneSupervisorByEmail("John@gmail.com")).thenReturn(supervisorDTO);

    enterpriseDTO.setId(1);
    blacklistedEnterpriseDTO.setId(2);

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
        () -> assertThrows(BiznessException.class, () -> supervisorUCC.getOneSupervisorById(-1)),
        () -> assertThrows(BiznessException.class, () -> supervisorUCC.getOneSupervisorById(0))
    );
  }

  @Test
  @DisplayName("Test if the id is not in the database")
  public void testGetOneInternshipSupervisorWithIdNotInDatabase() {
    assertEquals(null, supervisorUCC.getOneSupervisorById(999));
  }

  @Test
  @DisplayName("Add a supervisor")
  public void testAddSupervisor() {
    SupervisorDTO supervisorToAdd = domainFactory.getSupervisorDTO();
    supervisorToAdd.setEmail("test@test.gmail");
    supervisorToAdd.setFirstName("test");
    supervisorToAdd.setLastName("test");
    supervisorToAdd.setPhoneNumber("123456789");
    supervisorToAdd.setEnterpriseId(1);

    Mockito.when(supervisorDAO.addSupervisor(supervisorToAdd)).thenReturn(supervisorToAdd);

    SupervisorDTO actualSupervisor = supervisorUCC.addSupervisor(supervisorToAdd);
    assertEquals(supervisorToAdd, actualSupervisor);

  }

  @Test
  @DisplayName("Add a supervisor with email that already exists")
  public void testAddSupervisorWithExistingEmail() {
    SupervisorDTO supervisorToAdd = domainFactory.getSupervisorDTO();
    supervisorToAdd.setEmail("test@test.gmail");

    Mockito.when(supervisorDAO.getOneSupervisorByEmail("test@test.gmail"))
        .thenReturn(supervisorDTO);

    assertEquals(null, supervisorUCC.addSupervisor(supervisorToAdd));
  }

}
