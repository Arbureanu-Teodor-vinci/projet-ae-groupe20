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
}
