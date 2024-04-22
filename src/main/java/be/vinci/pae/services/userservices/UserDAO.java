package be.vinci.pae.services.userservices;

import be.vinci.pae.domain.user.UserDTO;
import java.util.List;

/**
 * Services of user.
 */
public interface UserDAO {

  /**
   * Get one user by id from DB.
   *
   * @param id integer
   * @return userDTO
   */
  UserDTO getOneUserByID(int id);

  /**
   * Get one user by email from DB.
   *
   * @param email String
   * @return userDTO
   */
  UserDTO getOneUserByEmail(String email);

  /**
   * Get all users from DB.
   *
   * @return list of users
   */
  List<UserDTO> getAllUsers();

  /**
   * Add user to DB.
   *
   * @param userDTO UserDTO
   * @return userDTO which was added
   */
  UserDTO addUser(UserDTO userDTO);

  /**
   * Update user in DB.
   *
   * @param userDTO UserDTO
   * @return userDTO
   */
  UserDTO updateUser(UserDTO userDTO);

  /**
   * Get number of students with an internship in all academic years from DB.
   *
   * @return integer
   */
  int getNumberOfStudentsWithInternshipAllAcademicYears();

  /**
   * Get number of students without an internship in all academic years from DB.
   *
   * @return integer
   */
  int getNumberOfStudentsWithoutInternshipAllAcademicYears();

  /**
   * Get number of students with an internship in a specific academic year from DB.
   *
   * @param academicYear String
   * @return integer
   */
  int getNumberOfStudentsWithInternship(String academicYear);

  /**
   * Get number of students without an internship in a specific academic year from DB.
   *
   * @param academicYear String
   * @return integer
   */
  int getNumberOfStudentsWithoutInternship(String academicYear);

  /**
   * Get all academic years from DB.
   *
   * @return list of academic years
   */
  List<String> getAllAcademicYears();
}
