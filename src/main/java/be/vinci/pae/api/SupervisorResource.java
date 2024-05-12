package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.internshipsupervisor.SupervisorDTO;
import be.vinci.pae.domain.internshipsupervisor.SupervisorUCC;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.utils.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
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
 * Resource route for the supervisors requests.
 */
@Singleton
@Path("/supervisors")
public class SupervisorResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();
  @Inject
  private SupervisorUCC supervisorUCC;

  /**
   * Add a supervisor.
   *
   * @param newSupervisor The supervisor to add.
   * @return JSON object containing the new supervisor.
   * @throws WebApplicationException If the supervisor is missing.
   */
  @POST
  @Path("/add")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Administratif", "Professeur", "Etudiant"})
  public ObjectNode addSupervisor(SupervisorDTO newSupervisor, @Context ContainerRequest request) {
    final UserDTO authentifiedUser = (UserDTO) request.getProperty("user");
    Logger.logEntry("POST /supervisors/add" + newSupervisor.getEmail());

    // if the supervisor is null, throw an exception
    if (newSupervisor.getEmail() == null || newSupervisor.getFirstName() == null
        || newSupervisor.getLastName() == null || newSupervisor.getPhoneNumber() == null) {
      Logger.logEntry("Supervisor is missing.");
      throw new WebApplicationException("Veuillez entrez un superviseur", Status.BAD_REQUEST);
    }
    if (newSupervisor.getEnterprise().getId() == 0) {
      throw new WebApplicationException("Veuillez entrez une entreprise", Status.BAD_REQUEST);
    }

    SupervisorDTO supervisorDTO = supervisorUCC.addSupervisor(newSupervisor, authentifiedUser);
    if (supervisorDTO == null) {
      Logger.logEntry("cannot add the supervisor");
      throw new WebApplicationException("Impossible d'ajouter le superviseur", Status.BAD_REQUEST);
    }

    return toJson(supervisorDTO);
  }

  /**
   * Get 1 supervisor.
   *
   * @param id The id of the supervisor to get.
   * @return JSON object containing the supervisor.
   * @throws WebApplicationException If id is null or the supervisor is not found.
   */
  @GET
  @Path("getOne:{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Administratif", "Professeur", "Etudiant"})
  public ObjectNode getOneSupervisor(@PathParam("id") Integer id) {
    Logger.logEntry("GET /supervisors/getOne:" + id);
    if (id == null) {
      Logger.logEntry("id is missing.");
      throw new WebApplicationException("Veuillez entrez un id", Status.BAD_REQUEST);
    }
    SupervisorDTO supervisor = supervisorUCC.getOneSupervisorById(id);
    if (supervisor == null) {
      Logger.logEntry("No supervisor found with this id");
      throw new WebApplicationException("Aucun superviseur trouvé avec cet id", Status.NOT_FOUND);
    }
    return toJson(supervisor);
  }

  /**
   * Get supervisors by enterprise.
   *
   * @param idEnterprise The id of the enterprise.
   * @param request      The request.
   * @return JSON array containing all supervisors of the enterprise.
   * @throws WebApplicationException If idEnterprise is null or the supervisors are not found.
   */
  @GET
  @Path("getByEnterprise:{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Administratif", "Professeur", "Etudiant"})
  public ArrayNode getSupervisorsByEnterprise(@PathParam("id") Integer idEnterprise,
      @Context ContainerRequest request) {
    Logger.logEntry("GET /supervisors/getByEnterprise:" + idEnterprise);
    // verify the token
    UserDTO authentifiedUser = (UserDTO) request.getProperty("user");
    if (authentifiedUser == null) {
      Logger.logEntry("tries to access without token.");
      throw new WebApplicationException("Impossible d'accéder sans être authentifié.",
          Status.UNAUTHORIZED);
    }
    // if the idEnterprise is null, throw an exception
    if (idEnterprise == null) {
      Logger.logEntry("idEnterprise is missing.");
      throw new WebApplicationException("Veuillez entrez l'id d'une entreprise.",
          Status.BAD_REQUEST);
    }
    // Try to get the supervisors by enterprise
    ArrayNode supervisorsListNode = jsonMapper.createArrayNode();
    for (SupervisorDTO supervisor : supervisorUCC.getSupervisorsByEnterprise(idEnterprise)) {
      supervisorsListNode.add(toJson(supervisor));
    }
    return supervisorsListNode;
  }

  /**
   * Get all supervisors.
   *
   * @return JSON array containing all supervisors.
   */
  @GET
  @Path("getAll")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Administratif", "Professeur", "Etudiant"})
  public ArrayNode getAllSupervisors() {
    Logger.logEntry("GET /supervisors/getAll");

    ArrayNode supervisorsListNode = jsonMapper.createArrayNode();
    // Iterate over all supervisors and add them to the array
    for (SupervisorDTO supervisor : supervisorUCC.getAllInternshipSupervisors()) {
      supervisorsListNode.add(toJson(supervisor));
    }
    return supervisorsListNode;
  }


  private ObjectNode toJson(SupervisorDTO supervisor) {
    // Create the enterprise node.
    ObjectNode enterpriseNode = jsonMapper.createObjectNode();
    enterpriseNode.put("id", supervisor.getEnterprise().getId());
    enterpriseNode.put("tradeName", supervisor.getEnterprise().getTradeName());
    enterpriseNode.put("designation", supervisor.getEnterprise().getDesignation());
    enterpriseNode.put("address", supervisor.getEnterprise().getAddress());
    enterpriseNode.put("phoneNumber", supervisor.getEnterprise().getPhoneNumber());
    enterpriseNode.put("city", supervisor.getEnterprise().getCity());
    enterpriseNode.put("email", supervisor.getEnterprise().getEmail());
    enterpriseNode.put("blackListed", supervisor.getEnterprise().isBlackListed());
    enterpriseNode.put("blackListMotivation", supervisor.getEnterprise().getBlackListMotivation());
    enterpriseNode.put("version", supervisor.getEnterprise().getVersion());

    // Create the supervisor node.
    ObjectNode node = jsonMapper.createObjectNode();
    node.put("id", supervisor.getId());
    node.put("email", supervisor.getEmail());
    node.put("firstName", supervisor.getFirstName());
    node.put("lastName", supervisor.getLastName());
    node.put("phoneNumber", supervisor.getPhoneNumber());
    // Add the enterprise node to the supervisor node.
    node.set("enterprise", enterpriseNode);
    return node;
  }

}
