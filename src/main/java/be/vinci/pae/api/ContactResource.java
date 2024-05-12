package be.vinci.pae.api;


import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.contact.ContactDTO;
import be.vinci.pae.domain.contact.ContactUCC;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.utils.Logger;
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
      throw new WebApplicationException("Vous n'avez pas accès à cette ressource.",
          Status.UNAUTHORIZED);
    }

    // if the id is null, throw an exception
    if (id == null) {
      Logger.logEntry("id is missing.");
      throw new WebApplicationException("L'id doit être fourni."
          , Status.BAD_REQUEST);
    }

    // Try to get the contact
    ContactDTO contact = contactUCC.getOneContact(id);
    // if the contact is null, throw an exception
    if (contact == null || contact.getStudent().getId() != authentifiedUser.getId()
        && authentifiedUser.getRole().equals("Etudiant")) {
      Logger.logEntry("Contact not found.");
      throw new WebApplicationException("Le contact n'a pas été trouvé."
          , Status.NOT_FOUND);
    }

    // Create a JSON object with the contact information
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
      throw new WebApplicationException("Vous n'avez pas accès à cette ressource.",
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
   * Get contacts by enterprise.
   *
   * @param id The id of the enterprise.
   * @return JSON object containing all contacts.
   * @throws WebApplicationException If id is null or the token is invalid.
   */
  @GET
  @Path("getByEnterprise:{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Administratif", "Professeur"})
  public ArrayNode getContactsByEnterprise(@PathParam("id") Integer id) {
    Logger.logEntry("GET /contacts/getByEnterprise:" + id);

    // if the id is null, throw an exception
    if (id == null) {
      Logger.logEntry("id is missing.");
      throw new WebApplicationException("L'id doit être fourni.", Status.BAD_REQUEST);
    }

    // Try to get the contacts by enterprise
    ArrayNode contactsListNode = jsonMapper.createArrayNode();
    // Try to get the contacts by enterprise id
    for (ContactDTO contact : contactUCC.getContactsByEnterprise(id)) {
      contactsListNode.add(contactNodeMaker(contact));
    }
    return contactsListNode;
  }

  /**
   * Get contacts by user.
   *
   * @param id      The id of the user.
   * @param request The request.
   * @return JSON object containing all contacts.
   * @throws WebApplicationException If id is null or the token is invalid.
   */
  @GET
  @Path("getByUser:{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Administratif", "Professeur", "Etudiant"})
  public ArrayNode getContactsByUser(@PathParam("id") Integer id,
      @Context ContainerRequest request) {
    Logger.logEntry("GET /contacts/getByUser:" + id);

    UserDTO authentifiedUser = (UserDTO) request.getProperty("user");
    if (authentifiedUser.getRole().equals("Etudiant") && !id.equals(authentifiedUser.getId())) {
      Logger.logEntry("Tried to access another student's contacts.");
      throw new WebApplicationException(
          "Vous ne pouvez pas accéder aux contacts d'un autre étudiant."
          , Status.FORBIDDEN);
    }

    // if the id is null, throw an exception
    if (id == null) {
      Logger.logEntry("id is missing.");
      throw new WebApplicationException("L'id doit être fourni.", Status.BAD_REQUEST);
    }

    // Try to get the contacts by user id
    ArrayNode contactsListNode = jsonMapper.createArrayNode();
    for (ContactDTO contact : contactUCC.getContactsByUser(id)) {
      contactsListNode.add(contactNodeMaker(contact));
    }
    return contactsListNode;
  }

  /**
   * Add a contact.
   *
   * @param contact The contact DTO containing the student and enterprise infos.
   * @param request The request.
   * @return JSON object containing the contact infos.
   */
  @POST
  @Path("add")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Etudiant"})
  public ObjectNode addContact(ContactDTO contact,
      @Context ContainerRequest request) {
    UserDTO authentifiedUser = (UserDTO) request.getProperty("user");
    Logger.logEntry("POST /contacts/add");
    // Verify the token

    //EnterpriseDTO enterprise = contact.getEnterprise();
    //StudentDTO student = contact.getStudent();

    if (contact.getEnterprise() == null) {
      throw new WebApplicationException("Veuillez entrer les informations de l'entreprise.", Status.BAD_REQUEST);
    }

    if (contact.getStudent().getId() != authentifiedUser.getId()) {
      throw new WebApplicationException("Vous ne pouvez pas ajouter un contact pour un autre étudiant.",
          Status.FORBIDDEN);
    }
    // Try to add the contact
    ContactDTO addedContact = contactUCC.addContact(contact);
    // if the contact is null, throw an exception
    if (addedContact == null) {
      throw new WebApplicationException("Le contact n'a pas pu être ajouté."
          , Status.NOT_FOUND);
    }

    return contactNodeMaker(addedContact);
  }

  /**
   * Update a contact.
   *
   * @param contactDTO The contact DTO containing the infos about the changes.
   * @param request    The request.
   * @return JSON object containing the contact infos.
   */
  @PATCH
  @Path("update")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Etudiant"})
  public ObjectNode updateContact(ContactDTO contactDTO, @Context ContainerRequest request) {
    Logger.logEntry("POST /contacts/update");

    if (contactDTO == null || contactDTO.getId() == 0) {
      Logger.logEntry("Tried to update contact without id.");
      throw new WebApplicationException("Veuillez entrer l'id du contact à modifier."
          , Status.BAD_REQUEST);
    }

    ContactDTO updatedContact = contactUCC.updateContact(contactDTO);
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
      // Create a JSON object with the contact information.
      // Create a JSON object of the academic year of the student of the contact.
      ObjectNode studentAcademicYearNode = jsonMapper.createObjectNode()
          .put("id", contact.getStudent().getStudentAcademicYear().getId())
          .put("year", contact.getStudent().getStudentAcademicYear().getYear());

      // Create a JSON object of the student of the contact.
      ObjectNode studentNode = jsonMapper.createObjectNode()
          .put("id", contact.getStudent().getId())
          .put("firstName", contact.getStudent().getFirstName())
          .put("lastName", contact.getStudent().getLastName())
          .put("email", contact.getStudent().getEmail())
          .put("telephoneNumber", contact.getStudent().getTelephoneNumber())
          .put("registrationDate", contact.getStudent().getRegistrationDate().toString())
          .put("role", contact.getStudent().getRole())
          .put("version", contact.getStudent().getVersion());
      studentNode.set("academicYear", studentAcademicYearNode);

      // Create a JSON object of the enterprise of the contact.
      ObjectNode enterpriseNode = jsonMapper.createObjectNode()
          .put("id", contact.getEnterprise().getId())
          .put("tradeName", contact.getEnterprise().getTradeName())
          .put("designation", contact.getEnterprise().getDesignation())
          .put("address", contact.getEnterprise().getAddress())
          .put("phoneNumber", contact.getEnterprise().getPhoneNumber())
          .put("city", contact.getEnterprise().getCity())
          .put("email", contact.getEnterprise().getEmail())
          .put("blackListed", contact.getEnterprise().isBlackListed())
          .put("blackListMotivation", contact.getEnterprise().getBlackListMotivation())
          .put("version", contact.getEnterprise().getVersion());

      // Create a JSON object of the academic year of the contact.
      ObjectNode contactAcademicYearNode = jsonMapper.createObjectNode()
          .put("id", contact.getAcademicYear().getId())
          .put("year", contact.getAcademicYear().getYear());

      // Create a JSON object of the contact.
      ObjectNode contactNode = jsonMapper.createObjectNode()
          .put("id", contact.getId())
          .put("interviewMethod", contact.getInterviewMethod())
          .put("tool", contact.getTool())
          .put("refusalReason", contact.getRefusalReason())
          .put("stateContact", contact.getStateContact())
          .put("version", contact.getVersion());

      // Set the contact fields with the JSON objects previously created.
      contactNode.set("student", studentNode);
      contactNode.set("enterprise", enterpriseNode);
      contactNode.set("academicYear", contactAcademicYearNode);

      // Return the contact node.
      return contactNode;
    } catch (Exception e) {
      System.out.println("Impossible de créer le contact");
      return null;
    }
  }

}
