package be.vinci.pae.api;


import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.contact.ContactDTO;
import be.vinci.pae.domain.contact.ContactUCC;
import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import be.vinci.pae.domain.enterprise.EnterpriseUCC;
import be.vinci.pae.domain.user.StudentDTO;
import be.vinci.pae.domain.user.StudentUCC;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.utils.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * Resource route for the contacts requests.
 */
@Singleton
@Path("/contacts")
public class ContactResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private ContactUCC contactUCC;

  @Inject
  private EnterpriseUCC enterpriseUCC;

  @Inject
  private StudentUCC studentUCC;

  /**
   * Get 1 contact.
   *
   * @param id      The id of the contact to get.
   * @param request The request.
   * @return JSON object containing all contacts.
   * @throws WebApplicationException If id is null or the token is invalid.
   */
  @GET
  @Path("getOne:{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Administratif", "Professeur", "Etudiant"})
  public ObjectNode getOneContact(@PathParam("id") Integer id, @Context ContainerRequest request) {
    Logger.logEntry("GET /contacts/getOne:" + id);
    // Verify the token
    UserDTO authentifiedUser = (UserDTO) request.getProperty("user");
    if (authentifiedUser == null) {
      Logger.logEntry("tries to access without token.");
      throw new WebApplicationException("Authorization header must be provided",
          Status.UNAUTHORIZED);
    }

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
  @Authorize(rolesAllowed = {"Administratif", "Professeur"})
  public ArrayNode getAllContacts(@Context ContainerRequest request) {
    Logger.logEntry("GET /contacts/getAll");
    // Verify the token
    UserDTO authentifiedUser = (UserDTO) request.getProperty("user");
    if (authentifiedUser == null) {
      throw new WebApplicationException("Authorization header must be provided",
          Status.UNAUTHORIZED);
    }
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
   * @param request The request.
   * @return JSON object containing all contacts.
   * @throws WebApplicationException If id is null or the token is invalid.
   */
  @GET
  @Path("getByUser")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Etudiant"})
  public ArrayNode getContactsByUser(@Context ContainerRequest request) {
    UserDTO authentifiedUser = (UserDTO) request.getProperty("user");
    int id = authentifiedUser.getId();
    Logger.logEntry("GET /contacts/getByUser:" + id);

    // Try to get the contacts by user
    ArrayNode contactsListNode = jsonMapper.createArrayNode();
    for (ContactDTO contact : contactUCC.getContactsByUser(id)) {
      contactsListNode.add(contactNodeMaker(contact));
    }
    return contactsListNode;
  }

  /**
   * Add a contact.
   *
   * @param jsonIDs The JSON object containing the studentID, enterpriseID and academicYearID.
   * @param request The request.
   * @return JSON object containing the contact infos.
   */
  @POST
  @Path("add")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Etudiant"})
  public ObjectNode addContact(JsonNode jsonIDs, @Context ContainerRequest request) {
    UserDTO authentifiedUser = (UserDTO) request.getProperty("user");
    int studentID = authentifiedUser.getId();

    Logger.logEntry("POST /contacts/add");
    // Verify the token

    if (!jsonIDs.hasNonNull("enterpriseID")) {
      throw new WebApplicationException(
          "You must enter a enterpriseID.",
          Status.BAD_REQUEST);
    }
    int enterpriseID = jsonIDs.get("enterpriseID").asInt();

    StudentDTO studentDTO = studentUCC.getStudentById(studentID);
    EnterpriseDTO enterpriseDTO = enterpriseUCC.getOneEnterprise(enterpriseID);

    // Try to add the contact
    ContactDTO addedContact = contactUCC.addContact(studentDTO, enterpriseDTO);
    // if the contact is null, throw an exception
    if (addedContact == null) {
      throw new WebApplicationException("Contact not added", Status.NOT_FOUND);
    }

    return contactNodeMaker(addedContact);
  }

  /**
   * Update a contact.
   *
   * @param json    The JSON object containing the contact information.
   * @param request The request.
   * @return JSON object containing the contact infos.
   */
  @PATCH
  @Path("update")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Etudiant"})
  public ObjectNode updateContact(JsonNode json, @Context ContainerRequest request) {
    Logger.logEntry("POST /contacts/update");

    if (!json.hasNonNull("idContact") || json.get("idContact").asText().isEmpty()) {
      Logger.logEntry("Tried to update contact without id.");
      throw new WebApplicationException("You must enter a contact id.", Status.BAD_REQUEST);
    }
    int id = json.get("idContact").asInt();
    ContactDTO contact = contactUCC.getOneContact(id);
    if (contact == null) {
      Logger.logEntry("Contact not found.");
      throw new WebApplicationException("Contact not found", Status.NOT_FOUND);
    }
    if (json.hasNonNull("interviewMethod")) {
      contact.setInterviewMethod(json.get("interviewMethod").asText());
    }
    if (json.hasNonNull("tool")) {
      contact.setTool(json.get("tool").asText());
    }
    if (json.hasNonNull("refusalReason")) {
      contact.setRefusalReason(json.get("refusalReason").asText());
    }
    if (json.hasNonNull("stateContact")) {
      contact.setStateContact(json.get("stateContact").asText());
    }

    ContactDTO updatedContact = contactUCC.updateContact(contact);
    return contactNodeMaker(updatedContact);
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
