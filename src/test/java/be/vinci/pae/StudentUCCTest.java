package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.api.filters.BiznessException;
import be.vinci.pae.domain.AcademicYear.AcademicYearDTO;
import be.vinci.pae.domain.Factory.DomainFactory;
import be.vinci.pae.domain.User.Student;
import be.vinci.pae.domain.User.StudentDTO;
import be.vinci.pae.domain.User.StudentUCC;
import be.vinci.pae.services.UserServices.StudentDAO;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class StudentUCCTest {

  ServiceLocator locator = ServiceLocatorUtilities.bind(new TestsApplicationBinder());
  private StudentUCC studentUCC = locator.getService(StudentUCC.class);
  private StudentDAO studentDAO = locator.getService(StudentDAO.class);
  private DomainFactory domainFactory = locator.getService(DomainFactory.class);
  private StudentDTO studentDTO = domainFactory.getStudentDTO();

  @BeforeEach
  void initEach() {
    AcademicYearDTO academicYearDTO = domainFactory.getAcademicYearDTO();
    academicYearDTO.setYear("2023-2024");
    academicYearDTO.setId(1);

    studentDTO.setAcademicYear(academicYearDTO);
    studentDTO.setId(1);
    studentDTO.setEmail("john@student.vinci.be");
    studentDTO.setRole("Etudiant");
    Student student = (Student) studentDTO;

    Mockito.when(studentDAO.getStudentById(1)).thenReturn(null);
    Mockito.when(studentDAO.getStudentById(1)).thenReturn(null);
    Mockito.when(studentDAO.addStudent(student)).thenReturn(studentDTO);

  }

  @Test
  @DisplayName("Register a valid student")
  void testRegisterStudent1() {
    Student student = (Student) studentDTO;

    assertAll(
        () -> assertNotNull(studentUCC.registerStudent(studentDTO)),
        () -> assertTrue(student.checkUniqueStudent(studentDAO.getStudentById(1)))
    );
  }

  @Test
  @DisplayName("Register a student that already exists")
  void testRegisterStudent2() {
    Student student = (Student) studentDTO;
    Mockito.when(studentDAO.getStudentById(1)).thenReturn(studentDTO);
    assertAll(
        () -> assertThrows(BiznessException.class, () -> studentUCC.registerStudent(studentDTO)),
        () -> assertFalse(student.checkUniqueStudent(studentDAO.getStudentById(1)))
    );
  }

  @Test
  @DisplayName("Register a student with no academic year")
  void testRegisterStudent3() {
    Student student = (Student) studentDTO;
    student.setAcademicYear(null);
    assertThrows(BiznessException.class, () -> studentUCC.registerStudent(studentDTO));
  }

  @Test
  @DisplayName("Register a student with null id")
  void testRegisterStudent4() {
    Student student = (Student) studentDTO;
    student.setId(0);
    assertThrows(BiznessException.class, () -> studentUCC.registerStudent(studentDTO));
  }
}
