package be.vinci.pae.domain.user;

import be.vinci.pae.services.dal.DALTransactionServices;
import be.vinci.pae.services.userservices.UserDAO;
import jakarta.inject.Inject;
import java.util.List;

/**
 * Implementation of UserUCC.
 */
public class UserUCCImpl implements UserUCC {

  @Inject
  private UserDAO userDAO;
  @Inject
  private DALTransactionServices dalServices;

  @Override
  public List<UserDTO> getAll() {
    return userDAO.getAllUsers();
  }

  @Override
  public UserDTO login(String email, String password) {
    UserDTO userDTO = userDAO.getOneUserByEmail(email);

    User user = (User) userDTO;
    if (user == null) {
      throw new NullPointerException("Invalid email");
    }
    user.checkPassword(password);

    return userDTO;
  }

  @Override
  public UserDTO register(UserDTO registeredUser) {
    User user = (User) registeredUser; // cast to User to access Bizness methods
    // check if email is a vinci email
    try {
      user.checkVinciEmail(registeredUser.getEmail());
      // check if role is valid
      user.checkRole(registeredUser.getRole());

      dalServices.startTransaction(); //START TRANSACTION
      // check if email exists already
      UserDTO userDTO = userDAO.getOneUserByEmail(registeredUser.getEmail());
      user.checkUniqueEmail(userDTO);

      //set role, hashed password and set today registration date
      registeredUser.hashPassword();
      registeredUser.setRegistrationDateToNow();

      // add user to DB
      registeredUser = userDAO.addUser(registeredUser);
      dalServices.commitTransaction(); //COMMIT TRANSACTION

    } catch (Throwable e) {
      dalServices.rollbackTransaction(); //ROLLBACK TRANSACTION
      throw e;
    }

    return registeredUser;
  }
}
