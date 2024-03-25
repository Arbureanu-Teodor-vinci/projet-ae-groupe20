package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.api.filters.BiznessException;
import be.vinci.pae.domain.Factory.DomainFactory;
import be.vinci.pae.domain.User.UserDTO;
import be.vinci.pae.domain.User.UserUCC;
import be.vinci.pae.services.UserServices.UserDAO;
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
    assertThrows(BiznessException.class, () -> {
      userUCC.login("admin", "test");
    });
  }

  @Test
  @DisplayName("Login with user email not found")
  void testLogin3() {
    assertThrows(NullPointerException.class, () -> {
      userUCC.login("nonexistent@example.com", "test");
    });
  }

  @Test
  @DisplayName("Login with null email")
  void testLogin4() {
    assertThrows(NullPointerException.class, () -> {
      userUCC.login(null, "admin");
    });
  }

  @Test
  @DisplayName("Login with null password")
  void testLogin5() {
    assertThrows(BiznessException.class, () -> {
      userUCC.login("admin", null);
    });
  }

  @Test
  @DisplayName("Login with empty email")
  void testLogin6() {
    assertThrows(NullPointerException.class, () -> {
      userUCC.login("", "admin");
    });
  }

  @Test
  @DisplayName("Login with empty password")
  void testLogin7() {
    assertThrows(BiznessException.class, () -> {
      userUCC.login("admin", "");
    });
  }

}
