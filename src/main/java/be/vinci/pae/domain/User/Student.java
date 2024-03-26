package be.vinci.pae.domain.User;

/**
 * Interface of student.
 */
public interface Student extends StudentDTO, User {

  /**
   * Check if student is unique.
   *
   * @param studentDTO StudentDTO
   * @return true if student is unique
   */
  boolean checkUniqueStudent(StudentDTO studentDTO);

}
