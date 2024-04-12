package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.api.filters.FatalException;
import be.vinci.pae.domain.academicyear.AcademicYearDTO;
import be.vinci.pae.domain.academicyear.AcademicYearUCC;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.domain.user.StudentDTO;
import be.vinci.pae.domain.user.StudentUCC;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.user.UserUCC;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.Logger;
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
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.SecurityContext;
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

  @Inject
  private StudentUCC studentController;

  @Inject
  private AcademicYearUCC academicYearController;

  @Inject
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
    Logger.logEntry("POST /auths/login");
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

      //publicUser = toJson(user, null).put("token", token);
      if (user.getEmail().endsWith("@student.vinci.be")) {
        //If the user is a student, get his academic year
        StudentDTO student = studentController.getStudentById(user.getId());
        publicUser = toJson(user, student.getStudentAcademicYear()).put("token", token);
      } else {
        publicUser = toJson(user, null).put("token", token);
      }
      return publicUser;

    } catch (FatalException e) {
      Logger.logEntry("Can't create token", e, 2);
      System.out.println("Can't create token");
      return null;
    }
  }

  /**
   * Register a new user.
   *
   * @param user A DTO object containing the user infos.
   * @return JSON object containing user infos.
   */
  @POST
  @Path("register")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode register(UserDTO user) {
    Logger.logEntry("POST /auths/register");

    // Check if all fields are present
    if (user == null || user.getEmail() == null || user.getEmail().isBlank()
        || user.getPassword() == null || user.getPassword().isBlank()
        || user.getFirstName() == null || user.getFirstName().isBlank()
        || user.getLastName() == null || user.getLastName().isBlank()
        || user.getTelephoneNumber() == null || user.getTelephoneNumber().isBlank()
        || user.getRole() == null) {
      throw new WebApplicationException(
          "You must enter an email, a password, a first name, a last name and a phone number.",
          Status.BAD_REQUEST);
    }
    if (!user.getEmail().endsWith("@vinci.be") && !user.getEmail().endsWith("@student.vinci.be")) {
      throw new WebApplicationException("You must enter a vinci email address.",
          Status.BAD_REQUEST);
    }

    // Create a userDTO object from JSON to register with
    UserDTO encodedUser = domainFactory.getUserDTO();
    encodedUser.setEmail(user.getEmail());
    encodedUser.setPassword(user.getPassword());
    encodedUser.setFirstName(user.getFirstName());
    encodedUser.setLastName(user.getLastName());
    encodedUser.setTelephoneNumber(user.getTelephoneNumber());
    encodedUser.setRole(user.getRole());

    UserDTO newUser;
    StudentDTO studentDTO = domainFactory.getStudentDTO();
    // Try to register
    newUser = userController.register(encodedUser);
    //If the user is a student, register him as a student
    if (newUser.getEmail().endsWith("@student.vinci.be")) {
      studentDTO.setId(newUser.getId());
      studentDTO.setAcademicYear(academicYearController.getOrAddActualAcademicYear());
      studentDTO = studentController.registerStudent(studentDTO);
    }

    return toJson(newUser, studentDTO.getStudentAcademicYear());
  }

  /**
   * Get logged user infos and token.
   *
   * @return JSON object containing user infos.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Administratif", "Professeur", "Etudiant"})
  public ObjectNode getLoggedUser() {
    Logger.logEntry("GET /auths");
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
  @Authorize(rolesAllowed = {"Administratif", "Professeur"})
  public List<ObjectNode> getAllUsers(@Context SecurityContext securityContext) {
    Logger.logEntry("GET /auths/users");
    List<UserDTO> users = userController.getAll(); // Get all users
    List<ObjectNode> usersJsonList = new ArrayList<>(); // Create a list of JSON objects to return
    //Convert all users to JSON
    ObjectNode userJson;

    for (UserDTO user : users) {
      //If the user is a student, get his academic year
      if (user.getEmail().endsWith("@student.vinci.be")) {
        StudentDTO student = studentController.getStudentById(user.getId());
        userJson = toJson(user, student.getStudentAcademicYear());
      } else {
        userJson = toJson(user, null);
      }
      usersJsonList.add(userJson);
    }
    return usersJsonList;
  }


  private ObjectNode toJson(UserDTO user, AcademicYearDTO academicYear) {
    // StudentDTO student = (StudentDTO) user;
    ObjectNode json = jsonMapper.createObjectNode()
        .put("id", user.getId())
        .put("role", user.getRole())
        .put("email", user.getEmail())
        .put("firstName", user.getFirstName())
        .put("lastName", user.getLastName())
        .put("phoneNumber", user.getTelephoneNumber())
        .put("registrationDate", user.getRegistrationDate().toString());
    //.put("academicYear", user.getAcademicYearTEST());
    if (academicYear != null) {
      json.put("academicYear", academicYear.toString());
    } else {
      json.put("academicYear", "null");
    }

    return json;
  }

}
