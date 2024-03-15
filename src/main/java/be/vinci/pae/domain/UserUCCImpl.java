package be.vinci.pae.domain;

import be.vinci.pae.api.filters.BiznessException;
import be.vinci.pae.services.UserDAO;
import jakarta.inject.Inject;

/**
 * Implementation of UserUCC.
 */
public class UserUCCImpl implements UserUCC {

  @Inject
  UserDAO userDS;

  @Override
  public UserDTO login(String email, String password) {
    if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
      throw new BiznessException("Email or password are incorrect.");
    }

    UserDTO userDTO = userDS.getOneUserByEmail(email);
    User user = (User) userDTO;
    if (user == null || !user.checkPassword(password)) {
      throw new BiznessException("Email or password are incorrect.");
    }
    return userDTO;
  }
}
