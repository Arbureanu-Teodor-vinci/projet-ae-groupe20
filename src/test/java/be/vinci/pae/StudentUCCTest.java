package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.api.filters.BusinessException;
import be.vinci.pae.domain.academicyear.AcademicYearDTO;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.domain.user.Student;
import be.vinci.pae.domain.user.StudentDTO;
import be.vinci.pae.domain.user.StudentUCC;
import be.vinci.pae.services.userservices.StudentDAO;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * StudentUCC test class.
 */
public class StudentUCCTest {

  ServiceLocator locator = ServiceLocatorUtilities.bind(new TestsApplicationBinder());
  private StudentUCC studentUCC = locator.getService(StudentUCC.class);
  private StudentDAO studentDAO = locator.getService(StudentDAO.class);
  private DomainFactory domainFactory = locator.getService(DomainFactory.class);
  private StudentDTO studentDTO = domainFactory.getStudentDTO();

  /**
   * Initialize studentDTO before each test.
   */
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
    Mockito.when(studentDAO.addStudent(student)).thenReturn(studentDTO);

  }

  @Test
  @DisplayName("Register a valid student")
  void testRegisterStudent1() {
    assertEquals(studentDTO, studentUCC.registerStudent(studentDTO));
  }

  @Test
  @DisplayName("Register a student that already exists")
  void testRegisterStudent2() {
    Mockito.when(studentDAO.getStudentById(1)).thenReturn(studentDTO);
    assertThrows(BusinessException.class, () -> studentUCC.registerStudent(studentDTO));
  }
}
