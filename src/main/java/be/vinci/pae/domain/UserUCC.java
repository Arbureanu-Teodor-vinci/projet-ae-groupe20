package be.vinci.pae.domain;

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
   * Get all users.
   *
   * @return list of users
   */
  List<UserDTO> getAll();
}
