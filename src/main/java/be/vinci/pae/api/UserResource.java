package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.api.filters.FatalException;
import be.vinci.pae.domain.academicyear.AcademicYearDTO;
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
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.glassfish.jersey.server.ContainerRequest;

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
      throw new WebApplicationException("Veuillez entrer un email et un mot de passe.",
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

      if (user.getEmail().endsWith("@student.vinci.be")) {
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
    if (user == null) {
      throw new WebApplicationException("L'utilisateur ne peut pas être null.", Status.BAD_REQUEST);
    }

    if (user.getFirstName() == null || user.getFirstName().isBlank()) {
      throw new WebApplicationException("Veuillez entrer un prénom.", Status.BAD_REQUEST);
    }

    if (user.getLastName() == null || user.getLastName().isBlank()) {
      throw new WebApplicationException("Veuillez entrer un nom de famille.", Status.BAD_REQUEST);
    }
    // get the email
    String email = user.getEmail();
    if (email == null || email.isBlank()) {
      throw new WebApplicationException("Veuillez entrer un email.", Status.BAD_REQUEST);
    } else {
      // parse the email to check if it's valid
      String[] emailParts = email.split("@");
      if (emailParts.length < 2 || emailParts[0].isBlank()) {
        throw new WebApplicationException("Veuillez entrer un nom d'utilisateur pour l'email.",
            Status.BAD_REQUEST);
      }
    }
    if (!user.getEmail().endsWith("@vinci.be") && !user.getEmail().endsWith("@student.vinci.be")) {
      throw new WebApplicationException("Veuillez saisir une adresse email Vinci.",
          Status.BAD_REQUEST);
    }

    if (user.getPassword() == null || user.getPassword().isBlank()) {
      throw new WebApplicationException("Veuillez entrer un mot de passe.", Status.BAD_REQUEST);
    }

    if (user.getTelephoneNumber() == null || user.getTelephoneNumber().isBlank()) {
      throw new WebApplicationException("Veuillez entrer un numéro de téléphone.",
          Status.BAD_REQUEST);
    }

    if (user.getRole() == null) {
      throw new WebApplicationException("Veuillez choisir un rôle.", Status.BAD_REQUEST);
    }

    StudentDTO studentDTO = domainFactory.getStudentDTO();
    // Try to register
    UserDTO newUser = userController.register(user);
    //If the user is a student, register him as a student
    if (newUser.getEmail().endsWith("@student.vinci.be")) {
      studentDTO.setId(newUser.getId());
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
   * Get the user infos.
   *
   * @param id The user id.
   * @return JSON object containing user infos.
   */
  @GET
  @Path("user:{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Administratif", "Professeur", "Etudiant"})
  public ObjectNode getUser(@PathParam("id") Integer id) {
    Logger.logEntry("GET /auths/user:" + id);
    UserDTO user = userController.getUserById(id);
    if (user.getEmail().endsWith("@student.vinci.be")) {
      StudentDTO student = studentController.getStudentById(user.getId());
      return toJson(user, student.getStudentAcademicYear());
    } else {
      return toJson(user, null);
    }
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

  /**
   * Update the user profile.
   *
   * @param user    The user to update.
   * @param request The request.
   * @return JSON object containing user infos.
   */
  @PUT
  @Path("updateProfile")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Etudiant", "Professeur", "Administratif"})
  public ObjectNode updateProfile(UserDTO user, @Context ContainerRequest request) {
    Logger.logEntry("PATCH /auths/updateProfile");
    UserDTO authentifiedUser = (UserDTO) request.getProperty("user");
    if (user == null) {
      throw new WebApplicationException("Vous devez être connecté.", Status.UNAUTHORIZED);
    }
    if (user.getId() != authentifiedUser.getId()) {
      throw new WebApplicationException("Vous ne pouvez pas modifier un autre utilisateur.",
          Status.UNAUTHORIZED);
    }

    if (user.getFirstName() == null || user.getFirstName().isBlank() || user.getFirstName()
        .isEmpty()) {
      throw new WebApplicationException("Veuillez indiquez votre prénom.",
          Status.BAD_REQUEST);
    }
    if (user.getLastName() == null || user.getLastName().isBlank() || user.getLastName()
        .isEmpty()) {
      throw new WebApplicationException("Veuillez indiquez votre nom.",
          Status.BAD_REQUEST);
    }
    if (!user.getEmail().equals(authentifiedUser.getEmail()) || !user.getRole()
        .equals(authentifiedUser.getRole()) || !user.getRegistrationDate()
        .equals(authentifiedUser.getRegistrationDate())) {
      throw new WebApplicationException(
          "Vous ne pouvez pas modifier votre email, votre rôle ou votre date d'inscription.",
          Status.BAD_REQUEST);
    }

    UserDTO updatedUser = userController.updateProfile(user);

    return toJson(updatedUser, null);
  }

  /**
   * Get number of students with internships for all academic years.
   *
   * @return int number of students with internships
   */
  @GET
  @Path("studentsWithInternship")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Administratif", "Professeur"})
  public int getNumberOfStudentsWithInternshipAllAcademicYears() {
    Logger.logEntry("GET /auths/studentsWithInternship");
    return userController.getNumberOfStudentsWithInternshipAllAcademicYears();
  }

  /**
   * Get number of students without internships for all academic years.
   *
   * @return int number of students without internships
   */
  @GET
  @Path("studentsWithoutInternship")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Administratif", "Professeur"})
  public int getNumberOfStudentsWithoutInternshipAllAcademicYears() {
    Logger.logEntry("GET /auths/studentsWithoutInternship");
    return userController.getNumberOfStudentsWithoutInternshipAllAcademicYears();
  }

  /**
   * Get number of students with internships for an academic year.
   *
   * @param academicYear String of the academic year.
   * @return int number of students with internships
   */
  @GET
  @Path("studentsWithInternship:{academicYear}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Administratif", "Professeur"})
  public int getNumberOfStudentsWithInternship(@PathParam("academicYear") String academicYear) {
    Logger.logEntry("GET /auths/studentsWithInternship:" + academicYear);
    if (academicYear == null || academicYear.isEmpty()) {
      throw new WebApplicationException("Veuillez saisir une année académique.",
          Status.BAD_REQUEST);
    }

    return userController.getNumberOfStudentsWithInternship(academicYear);
  }

  /**
   * Get number of students without internships for an academic year.
   *
   * @param academicYear String of the academic year.
   * @return int number of students without internships
   */
  @GET
  @Path("studentsWithoutInternship:{academicYear}")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Administratif", "Professeur"})
  public int getNumberOfStudentsWithoutInternship(@PathParam("academicYear") String academicYear) {
    Logger.logEntry("GET /auths/studentsWithoutInternship:" + academicYear);
    return userController.getNumberOfStudentsWithoutInternship(academicYear);
  }


  private ObjectNode toJson(UserDTO user, AcademicYearDTO academicYear) {
    // StudentDTO student = (StudentDTO) user;
    ObjectNode userJson = jsonMapper.createObjectNode()
        .put("id", user.getId())
        .put("role", user.getRole())
        .put("email", user.getEmail())
        .put("firstName", user.getFirstName())
        .put("lastName", user.getLastName())
        .put("telephoneNumber", user.getTelephoneNumber())
        .put("registrationDate", user.getRegistrationDate().toString())
        .put("version", user.getVersion());

    if (academicYear != null) {
      ObjectNode academicYearJson = jsonMapper.createObjectNode()
          .put("id", academicYear.getId())
          .put("year", academicYear.getYear());
      userJson.set("academicYear", academicYearJson);
    } else {
      userJson.putNull("academicYear");
    }

    return userJson;
  }

}
