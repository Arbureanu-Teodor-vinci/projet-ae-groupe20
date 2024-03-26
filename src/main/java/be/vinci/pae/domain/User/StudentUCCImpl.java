package be.vinci.pae.domain.User;

import be.vinci.pae.services.DAL.DALTransactionServices;
import be.vinci.pae.services.UserServices.StudentDAO;
import jakarta.inject.Inject;

public class StudentUCCImpl implements StudentUCC {

  @Inject
  private StudentDAO studentDAO;

  @Inject
  private DALTransactionServices dalServices;

  @Override
  public StudentDTO registerStudent(StudentDTO studentDTO) {
    Student student = (Student) studentDTO;

    dalServices.startTransaction();
    StudentDTO existingStudent = studentDAO.getStudentById(student.getId());

    if (student.checkUniqueStudent(existingStudent)) {
      dalServices.rollbackTransaction();
      throw new RuntimeException("Student already exists");
    }

    studentDTO = studentDAO.addStudent(student);

    if (studentDTO == null) {
      dalServices.rollbackTransaction();
      throw new RuntimeException("Error while adding user.");
    }

    dalServices.commitTransaction(); //COMMIT TRANSACTION
    return studentDTO;
  }

  @Override
  public StudentDTO getStudentById(int id) {
    return studentDAO.getStudentById(id);
  }

}
