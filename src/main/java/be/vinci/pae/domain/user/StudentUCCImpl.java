package be.vinci.pae.domain.user;

import be.vinci.pae.domain.academicyear.AcademicYearUCC;
import be.vinci.pae.services.dal.DALTransactionServices;
import be.vinci.pae.services.userservices.StudentDAO;
import jakarta.inject.Inject;

/**
 * StudentUCCImpl class.
 */
public class StudentUCCImpl implements StudentUCC {

  @Inject
  private StudentDAO studentDAO;

  @Inject
  private DALTransactionServices dalServices;

  @Inject
  private AcademicYearUCC academicYearUCC;

  @Override
  public StudentDTO registerStudent(StudentDTO studentDTO) {
    Student student = (Student) studentDTO;
    try {
      dalServices.startTransaction();
      StudentDTO existingStudent = studentDAO.getStudentById(student.getId());
      student.checkUniqueStudent(existingStudent);
      student.setAcademicYear(academicYearUCC.getOrAddActualAcademicYear());
      studentDTO = studentDAO.addStudent(student);
      dalServices.commitTransaction(); //COMMIT TRANSACTION
    } catch (Throwable e) {
      dalServices.rollbackTransaction(); //ROLLBACK TRANSACTION
      throw e;
    }
    return studentDTO;
  }

  @Override
  public StudentDTO getStudentById(int id) {
    return studentDAO.getStudentById(id);
  }

}
