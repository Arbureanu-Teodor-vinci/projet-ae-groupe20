package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.enterprise.EnterpriseUCC;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.domain.internshipSupervisor.SupervisorDTO;
import be.vinci.pae.domain.internshipSupervisor.SupervisorUCC;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/supervisors")
public class SupervisorResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();
  @Inject
  private SupervisorUCC supervisorUCC;
  @Inject
  private EnterpriseUCC EnterpriseUCC;
  @Inject
  private DomainFactory domainFactory;

  /**
   * Add a supervisor.
   *
   * @param supervisor The supervisor to add.
   * @return JSON object containing the new supervisor.
   * @throws WebApplicationException If the supervisor is missing.
   */
  @POST
  @Path("/add")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Administratif", "Professeur", "Etudiant"})
  public ObjectNode addSupervisor(SupervisorDTO supervisor) {
    Logger.logEntry("POST /supervisors/add" + supervisor.getEmail());

    if (supervisor == null || supervisor.getEmail() == null || supervisor.getFirstName() == null
        || supervisor.getLastName() == null || supervisor.getPhoneNumber() == null) {
      Logger.logEntry("Supervisor is missing.");
      throw new WebApplicationException("You must enter a supervisor.", Status.BAD_REQUEST);
    }

    SupervisorDTO encodedSupervisor = domainFactory.getSupervisorDTO();
    encodedSupervisor.setEmail(supervisor.getEmail());
    encodedSupervisor.setFirstName(supervisor.getFirstName());
    encodedSupervisor.setLastName(supervisor.getLastName());
    encodedSupervisor.setPhoneNumber(supervisor.getPhoneNumber());
    encodedSupervisor.setEnterpriseId(supervisor.getEnterpriseId());

    SupervisorDTO newSupervisor = supervisorUCC.addSupervisor(encodedSupervisor);
    if (newSupervisor == null) {
      Logger.logEntry("Error in SupervisorResource addSupervisor");
      throw new WebApplicationException("Error in SupervisorResource addSupervisor",
          Status.BAD_REQUEST);
    }

    return toJson(newSupervisor);
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
      throw new WebApplicationException("You must enter an id.", Status.BAD_REQUEST);
    }
    SupervisorDTO supervisor = supervisorUCC.getOneSupervisorById(id);
    if (supervisor == null) {
      Logger.logEntry("No supervisor found with this id");
      throw new WebApplicationException("No supervisor found with this id", Status.NOT_FOUND);
    }
    return toJson(supervisor);
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
    for (SupervisorDTO supervisor : supervisorUCC.getAllInternshipSupervisors()) {
      supervisorsListNode.add(toJson(supervisor));
    }
    return supervisorsListNode;
  }

  private ObjectNode toJson(SupervisorDTO supervisor) {
    ObjectNode node = jsonMapper.createObjectNode();
    node.put("id", supervisor.getId());
    node.put("email", supervisor.getEmail());
    node.put("firstName", supervisor.getFirstName());
    node.put("lastName", supervisor.getLastName());
    node.put("phoneNumber", supervisor.getPhoneNumber());
    node.put("enterpriseId", supervisor.getEnterpriseId());
    return node;
  }

}
