package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.api.filters.BusinessException;
import be.vinci.pae.domain.academicyear.AcademicYearDTO;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.domain.user.User;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.user.UserUCC;
import be.vinci.pae.services.userservices.UserDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    userDTO.setId(1);
    userDTO.setEmail("admin");
    userDTO.setPassword("$2a$10$WYp2AihAECclbAeBQ9nwVu.8kw2yltBdJEwQTXKMI6qwOumku3bVy");
    userDTO.setFirstName("admin");
    userDTO.setLastName("admin");
    userDTO.setTelephoneNumber("09999999");
    userDTO.setRole("Administratif");
    userDTO.setRegistrationDate(LocalDate.now());

    Mockito.when(userDAO.getOneUserByID(1)).thenReturn(userDTO);
    Mockito.when(userDAO.getOneUserByEmail("admin")).thenReturn(userDTO);

    Mockito.when(userDAO.updateUser(Mockito.any(UserDTO.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
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
    newUser.setFirstName("George-leonidas");
    newUser.setLastName("Papadopoulos");
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
    newUser.setFirstName("Patrik");
    newUser.setLastName("Laine");
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
    newUser.setFirstName("Alina");
    newUser.setLastName("Koval");
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
    newUser.setFirstName("Alina");
    newUser.setLastName("Koval");
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

  @Test
  @DisplayName("Register with a phone number in a incorrect froamat")
  void testRegister9() {
    UserDTO newUser = domainFactory.getUserDTO();
    newUser.setEmail("test@student.vinci.be");
    newUser.setPassword("testPassword");
    newUser.setRole("Etudiant");
    newUser.setTelephoneNumber("test");

    assertThrows(BusinessException.class, () -> userUCC.register(newUser));
  }

  @Test
  @DisplayName("Register with a phone number in a correct format")
  void testRegister10() {
    UserDTO newUser = domainFactory.getUserDTO();
    newUser.setFirstName("Radu");
    newUser.setLastName("Popescu");
    newUser.setEmail("test@student.vinci.be");
    newUser.setPassword("testPassword");
    newUser.setRole("Etudiant");
    newUser.setTelephoneNumber("0499999999");

    Mockito.when(userDAO.getOneUserByEmail(newUser.getEmail())).thenReturn(null);
    Mockito.when(userDAO.addUser(newUser)).thenReturn(newUser);

    assertEquals(userUCC.register(newUser), newUser);
  }

  @Test
  @DisplayName("Register with first name that doesn't stat with a capital letter")
  void testRegister11() {
    UserDTO newUser = domainFactory.getUserDTO();
    newUser.setFirstName("alina");
    newUser.setLastName("Koval");
    newUser.setEmail("test@vinci.be");
    newUser.setPassword("testPassword");
    newUser.setRole("Administratif");

    assertThrows(BusinessException.class, () -> userUCC.register(newUser));
  }

  @Test
  @DisplayName("Register with last name that doesn't stat with a capital letter")
  void testRegister12() {
    UserDTO newUser = domainFactory.getUserDTO();
    newUser.setFirstName("Alina");
    newUser.setLastName("koval");
    newUser.setEmail("test@vinci.be");
    newUser.setPassword("testPassword");
    newUser.setRole("Administratif");

    assertThrows(BusinessException.class, () -> userUCC.register(newUser));

  }

  @Test
  @DisplayName("Update profile with valid inputs")
  void testUpdateProfile1() {
    UserDTO updatedUser = userDTO;
    updatedUser.setFirstName("Lina");
    updatedUser.setLastName("Koval");
    updatedUser.setPassword("newPassword");
    updatedUser.setTelephoneNumber("0444444444");

    assertEquals(userUCC.updateProfile(updatedUser), updatedUser);
  }

  @Test
  @DisplayName("Update profile with null password")
  void testUpdateProfile2() {
    UserDTO updatedUser = userDTO;
    updatedUser.setPassword(null);
    updatedUser.setFirstName("Lina");
    updatedUser.setLastName("Koval");
    updatedUser.setTelephoneNumber("0444444444");

    assertEquals(userUCC.updateProfile(updatedUser), updatedUser);
    assertEquals(userUCC.updateProfile(updatedUser).getPassword(), userDTO.getPassword());
  }

  @Test
  @DisplayName("Update profile with first name that doesn't start with a capital letter")
  void testUpdateProfile3() {
    UserDTO updatedUser = userDTO;
    updatedUser.setFirstName("lina");
    updatedUser.setLastName("Koval");
    updatedUser.setPassword("newPassword");
    updatedUser.setTelephoneNumber("0444444444");

    assertThrows(BusinessException.class, () -> userUCC.updateProfile(updatedUser));
  }

  @Test
  @DisplayName("Update profile with last name that doesn't start with a capital letter")
  void testUpdateProfile4() {
    UserDTO updatedUser = userDTO;
    updatedUser.setFirstName("Lina");
    updatedUser.setLastName("koval");
    updatedUser.setPassword("newPassword");
    updatedUser.setTelephoneNumber("0444444444");

    assertThrows(BusinessException.class, () -> userUCC.updateProfile(updatedUser));
  }

  @Test
  @DisplayName("Get number of students with internships for a specific academic year")
  void testGetNumberOfStudentsWithInternship() {
    String academicYear = "2023-2024";
    int mockNumberOfStudents = 2;

    // Mock the behavior of UserDAO
    Mockito.when(userDAO.getAllAcademicYears()).thenReturn(
        Arrays.asList("2021-2022", "2022-2023", "2023-2024"));
    Mockito.when(userDAO.getNumberOfStudentsWithInternship(academicYear))
        .thenReturn(mockNumberOfStudents);

    // Call the method under test
    int actualNumberOfStudents = userUCC.getNumberOfStudentsWithInternship(academicYear);

    // Assert that the returned number matches the mock number
    assertEquals(mockNumberOfStudents, actualNumberOfStudents);
  }

  @Test
  @DisplayName("Get number of students with internships for a non-existent academic year")
  void testGetNumberOfStudentsWithInternshipWithNonExistentYear() {
    List<String> list = new ArrayList<>();
    list.add("2021-2022");
    list.add("2022-2023");
    list.add("2023-2024");

    Mockito.when(userDAO.getAllAcademicYears()).thenReturn(list);

    String academicYear = "2099-2100"; // This academic year is not in the database
    // Call the method under test and expect a BusinessException
    assertThrows(BusinessException.class, () -> {
      userUCC.getNumberOfStudentsWithInternship(academicYear);
    });
  }

  @Test
  @DisplayName("Get number of students with internships for an existing academic year")
  void testGetNumberOfStudentsWithInternshipWithExistingYear() {
    AcademicYearDTO year1 = domainFactory.getAcademicYearDTO();
    year1.setId(1);
    year1.setYear("2023-2024");

    AcademicYearDTO year2 = domainFactory.getAcademicYearDTO();
    year2.setId(2);
    year2.setYear("2022-2023");

    List<String> list = new ArrayList<>();
    list.add(year1.getYear());
    list.add(year2.getYear());

    String academicYear = "2023-2024"; // This academic year exists in the database

    Mockito.when(userDAO.getAllAcademicYears()).thenReturn(list);

    // Call the method under test
    int actualNumberOfStudents = userUCC.getNumberOfStudentsWithInternship(academicYear);

    // Assert that the returned number is greater than or equal to 0
    assertTrue(actualNumberOfStudents >= 0);
  }

  @Test
  @DisplayName("Get number of students without internships for a specific academic year")
  void testGetNumberOfStudentsWithoutInternship() {
    List<String> list = new ArrayList<>();
    list.add("2021-2022");
    list.add("2022-2023");
    list.add("2023-2024");

    Mockito.when(userDAO.getAllAcademicYears()).thenReturn(list);

    String academicYear = "2099-2100"; // This academic year is not in the database
    // Call the method under test and expect a BusinessException
    assertThrows(BusinessException.class, () -> {
      userUCC.getNumberOfStudentsWithoutInternship(academicYear);
    });
  }

  @Test
  @DisplayName("Get number of students without internships for an existing academic year")
  void testGetNumberOfStudentsWithoutInternshipWithExistingYear() {
    AcademicYearDTO year1 = domainFactory.getAcademicYearDTO();
    year1.setId(1);
    year1.setYear("2023-2024");

    AcademicYearDTO year2 = domainFactory.getAcademicYearDTO();
    year2.setId(2);
    year2.setYear("2022-2023");

    List<String> list = new ArrayList<>();
    list.add(year1.getYear());
    list.add(year2.getYear());

    String academicYear = "2023-2024"; // This academic year exists in the database

    Mockito.when(userDAO.getAllAcademicYears()).thenReturn(list);

    // Call the method under test
    int actualNumberOfStudents = userUCC.getNumberOfStudentsWithoutInternship(academicYear);

    // Assert that the returned number is greater than or equal to 0
    assertTrue(actualNumberOfStudents >= 0);
  }

}

