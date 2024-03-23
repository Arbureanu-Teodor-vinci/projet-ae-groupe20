package be.vinci.pae.domain;

import be.vinci.pae.api.filters.BiznessException;
import be.vinci.pae.services.UserDAO;
import jakarta.inject.Inject;
import java.util.List;

/**
 * Implementation of UserUCC.
 */
public class UserUCCImpl implements UserUCC {

  @Inject
  UserDAO userDAO;

  @Override
  public List<UserDTO> getAll() {
    return userDAO.getAllUsers();
  }

  @Override
  public UserDTO login(String email, String password) {

    UserDTO userDTO = userDAO.getOneUserByEmail(email);
    User user = (User) userDTO;
    if (user == null) {
      throw new NullPointerException("Not found");
    }
    if (!user.checkPassword(password)) {
      throw new BiznessException("Password is incorrect.");
    }
    return userDTO;
  }
}
