package be.vinci.pae.services;

import be.vinci.pae.domain.UserDTO;

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
}
