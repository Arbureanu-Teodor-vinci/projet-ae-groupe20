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

    UserDTO userDTO = userDS.getOneUserByEmail(email);
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
