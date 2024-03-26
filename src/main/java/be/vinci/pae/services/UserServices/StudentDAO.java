package be.vinci.pae.services.UserServices;

import be.vinci.pae.domain.User.StudentDTO;

/**
 * Interface of studentDAO.
 */
public interface StudentDAO {

  /**
   * Get student by id.
   *
   * @param id int
   * @return StudentDTO
   */
  StudentDTO getStudentById(int id);

  /**
   * Add student in database.
   *
   * @param student StudentDTO
   * @return StudentDTO
   */
  StudentDTO addStudent(StudentDTO student);

}
