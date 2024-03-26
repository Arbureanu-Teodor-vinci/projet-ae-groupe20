package be.vinci.pae.services.userservices;

import be.vinci.pae.domain.user.StudentDTO;

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
