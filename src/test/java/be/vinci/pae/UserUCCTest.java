package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.UserDTO;
import be.vinci.pae.domain.UserUCC;
import be.vinci.pae.services.UserDAO;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mockito;

/**
 * UserUCC tests.
 */
public class UserUCCTest {

  ServiceLocator locator = ServiceLocatorUtilities.bind(new TestsApplicationBinder());
  private UserUCC userUCC = locator.getService(UserUCC.class);
  private UserDAO userDAO = locator.getService(UserDAO.class);
  private DomainFactory domainFactory = locator.getService(DomainFactory.class);
  private UserDTO userDTO = domainFactory.getUserDTO();

  @BeforeEach
  void initEach() {
    userDTO.setEmail("admin");
    userDTO.setPassword("$2a$10$WYp2AihAECclbAeBQ9nwVu.8kw2yltBdJEwQTXKMI6qwOumku3bVy");

    Mockito.when(userDAO.getOneUserByEmail("admin")).thenReturn(userDTO);
  }

  @Test
  @DisplayName("Login with valid credentials")
  void testLogin1() {
    UserDTO result = userUCC.login("admin", "admin");
    assertAll(
        () -> assertTrue(BCrypt.checkpw("admin", userDTO.getPassword())),
        () -> assertNotNull(result)
    );
  }

  @Test
  @DisplayName("Login with invalid password")
  void testLogin2() {
    UserDTO result = userUCC.login("admin", "test");
    assertAll(
        () -> assertFalse(BCrypt.checkpw("test", userDTO.getPassword())),
        () -> assertNull(result)
    );
  }

  @Test
  @DisplayName("Login with user email not found")
  void testLogin3() {
    UserDTO result = userUCC.login("nonexistent@example.com", "password");
    assertNull(result);
  }

  @Test
  @DisplayName("Login with null email and password")
  void testLogin4() {
    UserDTO result = userUCC.login(null, null);
    assertNull(result);
  }

  @Test
  @DisplayName("Login with empty email and password")
  void testLogin5() {
    UserDTO result = userUCC.login("", "");
    assertNull(result);
  }

}
