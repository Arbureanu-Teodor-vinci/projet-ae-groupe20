package be.vinci.pae.services;

import be.vinci.pae.domain.UserDTO;
import be.vinci.pae.services.utils.Json;

/**
 * Implementation of user services.
 */
public class UserDAOImpl implements UserDAO {

  private static final String COLLECTION_NAME = "users";

  // @Inject
  // private static DomainFactory myUserDTO;
  private static Json<UserDTO> jsonDB = new Json<>(UserDTO.class);


  @Override
  public UserDTO getOneUserByID(int id) {
    var users = jsonDB.parse(COLLECTION_NAME);
    return users.stream().filter(userDTO -> userDTO.getId() == id).findAny()
        .orElse(null);
  }

  @Override
  public UserDTO getOneUserByEmail(String email) {
    var users = jsonDB.parse(COLLECTION_NAME);
    return users.stream()
        .filter(userDTO -> userDTO.getEmail().equals(email))
        .findAny().orElse(null);

  }


}
