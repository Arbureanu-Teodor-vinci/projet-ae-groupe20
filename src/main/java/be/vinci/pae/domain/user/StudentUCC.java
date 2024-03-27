package be.vinci.pae.domain.user;

/**
 * Interface of studentUCC.
 */
public interface StudentUCC {

  /**
   * Register student in database.
   *
   * @param studentDTO studentDTO
   * @return studentDTO
   */
  StudentDTO registerStudent(StudentDTO studentDTO);

  /**
   * Get student by id.
   *
   * @param id int
   * @return StudentDTO
   */
  StudentDTO getStudentById(int id);

}
