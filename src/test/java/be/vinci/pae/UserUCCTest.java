package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

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

    assertAll(() -> assertNotNull(userDAO.getOneUserByEmail("admin")),
        () -> assertEquals(userDTO, userDAO.getOneUserByEmail("admin")),
        () -> assertTrue(BCrypt.checkpw("admin", userDTO.getPassword())),
        () -> assertNotNull(result),
        () -> assertEquals("admin", result.getEmail())
    );
  }

  @Test
  @DisplayName("Login with invalid credentials")
  void testLogin2() {
    UserDTO result = userUCC.login("admin", "test");
    assertAll(() -> assertNotNull(userDAO.getOneUserByEmail("admin")),
        () -> assertNotEquals(result, userDAO.getOneUserByEmail("admin")),
        () -> assertFalse(BCrypt.checkpw("test", userDTO.getPassword())),
        () -> assertNull(result));
  }

  @Test
  @DisplayName("Login with user not found")
  void testLogin3() {
    Mockito.when(userDAO.getOneUserByEmail(anyString())).thenReturn(null);
    UserDTO result = userUCC.login("nonexistent@example.com", "password");
    assertNull(result);
  }

}
