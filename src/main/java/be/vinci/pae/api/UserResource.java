package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.api.filters.FatalException;
import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.UserDTO;
import be.vinci.pae.domain.UserUCC;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents the authentication route for the user to register and login.
 */
@Singleton
@Path("/auths")
public class UserResource {

  private static ObjectNode publicUser;
  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = new ObjectMapper();
  @Inject
  private UserUCC userController;

  private DomainFactory domainFactory;


  /**
   * Login with user email and password.
   *
   * @param json A JSON object which contains the email and the password to login.
   * @return JSON object containing user infos.
   * @throws WebApplicationException If email or password are null or empty.
   */
  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode login(JsonNode json) {
    if (!json.hasNonNull("email") || !json.hasNonNull("password") || json.get("email").asText()
        .isEmpty() || json.get("password").asText().isEmpty()) {
      throw new WebApplicationException("You must enter an email and a password.",
          Status.BAD_REQUEST);
    }
    String email = json.get("email").asText();
    String password = json.get("password").asText();

    // Try to login
    UserDTO user = userController.login(email, password);

    String token;
    try {
      Date expirationToken = new Date(
          System.currentTimeMillis() + 1000 * 60 * 60 * 3); // 3 hours token expiration

      token = JWT.create().withIssuer("auth0") // Create a token for the user
          .withClaim("user", user.getId())
          .withExpiresAt(expirationToken)
          .sign(this.jwtAlgorithm);

      publicUser = toJson(user).put("token", token);
      return publicUser;

    } catch (FatalException e) {
      System.out.println("Can't create token");
      return null;
    }
  }

  /**
   * Get logged user infos and token.
   *
   * @return JSON object containing user infos.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public ObjectNode getLoggedUser() {
    return publicUser;
  }

  /**
   * Get all users. If the logged user is a student, it will throw a WebApplicationException.
   *
   * @return the list of all users.
   */
  @GET
  @Path("users")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public List<ObjectNode> getAllUsers() {
    if (publicUser.findValue("role").asText().equals("Etudiant")) {
      throw new WebApplicationException("You must be an admin or professor to access this route.",
          Status.FORBIDDEN);
    }
    List<UserDTO> users = userController.getAll(); // Get all users
    List<ObjectNode> usersJsonList = new ArrayList<>(); // Create a list of JSON objects to return
    //Convert all users to JSON
    for (UserDTO user : users) {
      ObjectNode userJson = toJson(user);
      usersJsonList.add(userJson);
    }
    return usersJsonList;
  }

  private ObjectNode toJson(UserDTO user) {
    return jsonMapper.createObjectNode()
        .put("id", user.getId())
        .put("role", user.getRole())
        .put("email", user.getEmail())
        .put("firstName", user.getFirstName())
        .put("lastName", user.getLastName())
        .put("phoneNumber", user.getTelephoneNumber())
        .put("registrationDate", user.getRegistrationDate().toString());
  }

}
