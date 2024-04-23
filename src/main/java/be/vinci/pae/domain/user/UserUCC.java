package be.vinci.pae.domain.user;

import java.util.List;

/**
 * User controller.
 */
public interface UserUCC {

  /**
   * Login method.
   *
   * @param email    String
   * @param password String
   * @return ObjectNode
   */
  UserDTO login(String email, String password);

  /**
   * Register method.
   *
   * @param user UserDTO
   * @return new registered UserDTO
   */
  UserDTO register(UserDTO user);

  /**
   * Get user by id.
   *
   * @param id int
   * @return UserDTO
   */
  UserDTO getUserById(int id);

  /**
   * Get all users.
   *
   * @return list of users
   */
  List<UserDTO> getAll();

  /**
   * Update user profile.
   *
   * @param user UserDTO
   * @return UserDTO updated
   */
  UserDTO updateProfile(UserDTO user);

  /**
   * Get number of students with an internship in all academic years.
   *
   * @return integer number of students
   */
  int getNumberOfStudentsWithInternshipAllAcademicYears();

  /**
   * Get number of students without an internship in all academic years.
   *
   * @return integer number of students
   */
  int getNumberOfStudentsWithoutInternshipAllAcademicYears();

  /**
   * Get number of students with an internship in a specific academic year.
   *
   * @param academicYear String
   * @return integer number of students
   */
  int getNumberOfStudentsWithInternship(String academicYear);

  /**
   * Get number of students without an internship in a specific academic year.
   *
   * @param academicYear String
   * @return integer number of students
   */
  int getNumberOfStudentsWithoutInternship(String academicYear);
}
