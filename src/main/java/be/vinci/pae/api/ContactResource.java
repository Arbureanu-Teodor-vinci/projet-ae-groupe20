package be.vinci.pae.api;


import be.vinci.pae.domain.contact.ContactDTO;
import be.vinci.pae.domain.contact.ContactUCC;
import be.vinci.pae.utils.Config;
import be.vinci.pae.utils.Logger;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

/**
 * Resource route for the contacts requests.
 */
@Singleton
@Path("/contacts")
public class ContactResource {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private ContactUCC contactUCC;

  /**
   * Verify the JWT token.
   *
   * @param token The JWT token to verify.
   * @throws WebApplicationException If the token is invalid.
   */
  private void verifyToken(String token) {
    if (token == null || token.isEmpty()) {
      Logger.logEntry("tries to access without token.");
      throw new WebApplicationException("Authorization header must be provided",
          Status.UNAUTHORIZED);
    }
    try {
      JWT.require(jwtAlgorithm).build().verify(token);
    } catch (Exception e) {
      throw new WebApplicationException("Invalid token", Status.UNAUTHORIZED);
    }
  }

  /**
   * Get 1 contact.
   *
   * @param id The id of the contact to get.
   * @return JSON object containing all contacts.
   * @throws WebApplicationException If id is null or the token is invalid.
   */
  @GET
  @Path("getOne:{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode getOneContact(@PathParam("id") Integer id, @Context HttpHeaders headers) {
    Logger.logEntry("GET /contacts/getOne:" + id);
    // Verify the token
    verifyToken(headers.getHeaderString(HttpHeaders.AUTHORIZATION));

    // if the id is null, throw an exception
    if (id == null) {
      Logger.logEntry("id is missing.");
      throw new WebApplicationException("Id must be provided", Status.BAD_REQUEST);
    }

    // Try to get the contact
    ContactDTO contact = contactUCC.getOneContact(id);
    // if the contact is null, throw an exception
    if (contact == null) {
      Logger.logEntry("Contact not found.");
      throw new WebApplicationException("Contact not found", Status.NOT_FOUND);
    }

    return contactNodeMaker(contact);
  }

  /**
   * Get all contacts.
   *
   * @return JSON object containing all contacts.
   * @throws WebApplicationException If the token is invalid.
   */
  @GET
  @Path("getAll")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ArrayNode getAllContacts(@Context HttpHeaders headers) {
    Logger.logEntry("GET /contacts/getAll");
    // Verify the token
    verifyToken(headers.getHeaderString(HttpHeaders.AUTHORIZATION));

    // Try to get all contacts
    ArrayNode contactsListNode = jsonMapper.createArrayNode();
    for (ContactDTO contact : contactUCC.getAllContacts()) {
      contactsListNode.add(contactNodeMaker(contact));
    }
    return contactsListNode;
  }

  /**
   * Get contacts by user.
   *
   * @param id The id of the user to get the contacts from.
   * @return JSON object containing all contacts.
   * @throws WebApplicationException If id is null or the token is invalid.
   */
  @GET
  @Path("getByUser:{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ArrayNode getContactsByUser(@PathParam("id") Integer id, @Context HttpHeaders headers) {
    Logger.logEntry("GET /contacts/getByUser:" + id);

    // Verify the token
    verifyToken(headers.getHeaderString(HttpHeaders.AUTHORIZATION));

    // if the id is null, throw an exception
    if (id == null) {
      Logger.logEntry("id is missing.");
      throw new WebApplicationException("Id must be provided", Status.BAD_REQUEST);
    }

    // Try to get the contacts by user
    ArrayNode contactsListNode = jsonMapper.createArrayNode();
    for (ContactDTO contact : contactUCC.getContactsByUser(id)) {
      contactsListNode.add(contactNodeMaker(contact));
    }
    return contactsListNode;
  }

  /**
   * Create a JSON object with the contact information.
   *
   * @param contact The contact to create the JSON object with.
   * @return JSON object containing the contact infos.
   */
  private ObjectNode contactNodeMaker(ContactDTO contact) {
    try {
      // Create a JSON object with the contact information
      ObjectNode contactNode = jsonMapper.createObjectNode()
          .put("id", contact.getId())
          .put("interViewMethod", contact.getInterviewMethod())
          .put("tool", contact.getTool())
          .put("refusalReason", contact.getRefusalReason())
          .put("stateContact", contact.getStateContact())
          .put("studentId", contact.getStudentId())
          .put("enterpriseId", contact.getEnterpriseId())
          .put("academicYear", contact.getAcademicYear());
      return contactNode;
    } catch (Exception e) {
      System.out.println("Can't create contact");
      return null;
    }
  }

}
