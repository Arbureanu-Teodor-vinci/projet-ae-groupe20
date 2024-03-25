package be.vinci.pae.domain;

import be.vinci.pae.api.filters.BiznessException;
import be.vinci.pae.services.DALTransactionServices;
import be.vinci.pae.services.UserDAO;
import jakarta.inject.Inject;
import java.util.List;

/**
 * Implementation of UserUCC.
 */
public class UserUCCImpl implements UserUCC {

  @Inject
  UserDAO userDAO;
  @Inject
  DALTransactionServices dalServices;

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

  @Override
  public UserDTO register(UserDTO registeredUser) {
    User user = (User) registeredUser; // cast to User to access Bizness methods
    // check if email is a vinci email
    if (!user.checkVinciEmail(registeredUser.getEmail())) {
      throw new BiznessException("Email is not a vinci email.");
    }
    if (!user.checkRole(registeredUser.getRole())) {
      throw new BiznessException("Role is not valid.");
    }

    dalServices.startTransaction(); //START TRANSACTION
    // check if email exists already
    UserDTO userDTO = userDAO.getOneUserByEmail(registeredUser.getEmail());
    if (!user.checkUniqueEmail(userDTO)) {
      dalServices.rollbackTransaction(); //ROLLBACK TRANSACTION
      throw new BiznessException("Email already exists.");
    }
    //set role, hashed password and set today registration date
    registeredUser.hashPassword();
    registeredUser.setRoleByEmail();
    registeredUser.setRegistrationDateToNow();

    // add user to DB
    registeredUser = userDAO.addUser(registeredUser);
    if (registeredUser == null) {
      dalServices.rollbackTransaction(); //ROLLBACK TRANSACTION
      throw new BiznessException("Error while adding user.");
    }

    dalServices.commitTransaction(); //COMMIT TRANSACTION

    return registeredUser;
  }
}
