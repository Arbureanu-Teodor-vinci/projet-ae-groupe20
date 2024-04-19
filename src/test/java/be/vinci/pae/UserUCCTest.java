package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.api.filters.BusinessException;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.domain.user.User;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.user.UserUCC;
import be.vinci.pae.services.userservices.UserDAO;
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
    userDTO.setFirstName("admin");
    userDTO.setLastName("admin");
    userDTO.setTelephoneNumber("09999999");

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
    assertThrows(BusinessException.class, () -> {
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
    assertThrows(BusinessException.class, () -> {
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
    assertThrows(BusinessException.class, () -> {
      userUCC.login("admin", "");
    });
  }

  @Test
  @DisplayName("Login with nonexistent email")
  void testLogin8() {
    Mockito.when(userDAO.getOneUserByEmail(Mockito.anyString())).thenReturn(null);
    assertThrows(NullPointerException.class, () -> {
      userUCC.login("eee", "admin");
    });
  }

  @Test
  @DisplayName("Register with valid student credentials")
  void testRegister1() {
    UserDTO newUser = domainFactory.getUserDTO();
    newUser.setEmail("test@student.vinci.be");
    newUser.setPassword("testPassword");
    newUser.setRole("Etudiant");

    Mockito.when(userDAO.getOneUserByEmail(newUser.getEmail())).thenReturn(null);
    Mockito.when(userDAO.addUser(newUser)).thenReturn(newUser);

    assertEquals(userUCC.register(newUser), newUser);
  }

  @Test
  @DisplayName("Register with valid teacher credentials")
  void testRegister2() {
    UserDTO newUser = domainFactory.getUserDTO();
    newUser.setEmail("test@vinci.be");
    newUser.setPassword("testPassword");
    newUser.setRole("Professeur");

    Mockito.when(userDAO.getOneUserByEmail(newUser.getEmail())).thenReturn(null);
    Mockito.when(userDAO.addUser(newUser)).thenReturn(newUser);

    assertEquals(userUCC.register(newUser), newUser);
  }

  @Test
  @DisplayName("Register with valid administrator credentials")
  void testRegister3() {
    UserDTO newUser = domainFactory.getUserDTO();
    newUser.setEmail("test@vinci.be");
    newUser.setPassword("testPassword");
    newUser.setRole("Administratif");

    Mockito.when(userDAO.getOneUserByEmail(newUser.getEmail())).thenReturn(null);
    Mockito.when(userDAO.addUser(newUser)).thenReturn(newUser);

    assertEquals(userUCC.register(newUser), newUser);
  }

  @Test
  @DisplayName("Register with wrong email")
  void testRegister4() {
    UserDTO newUser = domainFactory.getUserDTO();
    newUser.setEmail("test");
    newUser.setPassword("testPassword");
    newUser.setRole("Professeur");

    User user = (User) newUser;

    Mockito.when(userDAO.getOneUserByEmail(newUser.getEmail())).thenReturn(null);
    assertAll(
        () -> assertThrows(BusinessException.class, () -> userUCC.register(newUser))
    );
    ;
  }

  @Test
  @DisplayName("Register with wrong role")
  void testRegister5() {
    UserDTO newUser = domainFactory.getUserDTO();
    newUser.setEmail("test@student.vinci.be");
    newUser.setPassword("testPassword");
    newUser.setRole("Administratif");
    User user = (User) newUser;

    Mockito.when(userDAO.getOneUserByEmail(newUser.getEmail())).thenReturn(null);
    assertAll(
        () -> assertThrows(BusinessException.class, () -> userUCC.register(newUser))
    );
  }

  @Test
  @DisplayName("Register with existing email")
  void testRegister6() {
    UserDTO newUser = domainFactory.getUserDTO();
    newUser.setEmail("test@student.vinci.be");
    newUser.setPassword("testPassword");
    newUser.setRole("Professeur");
    User user = (User) newUser;

    Mockito.when(userDAO.getOneUserByEmail(newUser.getEmail())).thenReturn(newUser);
    assertAll(
        () -> assertThrows(BusinessException.class, () -> userUCC.register(newUser))
    );
  }

  @Test
  @DisplayName("Register with null email")
  void testRegister7() {
    UserDTO newUser = domainFactory.getUserDTO();
    newUser.setEmail(null);
    newUser.setPassword("testPassword");
    newUser.setRole("Professeur");

    assertThrows(NullPointerException.class, () -> userUCC.register(newUser));
  }

  @Test
  @DisplayName("Register with null password")
  void testRegister8() {
    UserDTO newUser = domainFactory.getUserDTO();
    newUser.setEmail("test@student.vinci.be");
    newUser.setPassword(null);
    newUser.setRole("Professeur");

    assertThrows(BusinessException.class, () -> userUCC.register(newUser));
  }
}

