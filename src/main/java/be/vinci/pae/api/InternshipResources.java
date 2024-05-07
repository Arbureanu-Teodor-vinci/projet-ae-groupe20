package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.api.filters.BusinessException;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.domain.internship.InternshipDTO;
import be.vinci.pae.domain.internship.InternshipUCC;
import be.vinci.pae.utils.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

/**
 * Resource route for the internships requests.
 */
@Singleton
@Path("/internships")
public class InternshipResources {

  private final ObjectMapper jsonMapper = new ObjectMapper();
  @Inject
  private InternshipUCC internshipUCC;
  @Inject
  private DomainFactory domainFactory;

  /**
   * Get 1 internship by student id.
   *
   * @param id The id of the student.
   * @return JSON object containing the internship.
   * @throws WebApplicationException If id is null or the internship is not found.
   */
  @GET
  @Path("/getOneInternshipByStudentId:{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Etudiant"})
  public ObjectNode getOneInternshipByStudentId(@PathParam("id") int id) {
    Logger.logEntry("GET /internships/getOneInternshipByStudentId" + id);
    if (id <= 0) {
      Logger.logEntry("Id must be positive.");
      throw new WebApplicationException("Id must be positive.", Status.BAD_REQUEST);
    }
    InternshipDTO internship = internshipUCC.getOneInternshipByStudentId(id);
    if (internship == null) {
      Logger.logEntry("Internship not found.");
      throw new BusinessException("Internship not found.");
    }
    return toJsonObject(internship);
  }

  /**
   * Update the subject of an internship.
   *
   * @param internshipDTO The internship to update.
   * @return JSON object containing the updated internship.
   * @throws WebApplicationException If the internship is null, the id is 0 or the subject is null.
   */
  @PATCH
  @Path("/updateSubject")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Etudiant"})
  public ObjectNode updateSubject(InternshipDTO internshipDTO) {
    Logger.logEntry("POST /internships/updateSubject id: " + internshipDTO.getId());
    if (internshipDTO == null || internshipDTO.getId() <= 0) {
      throw new WebApplicationException("Tried to update a contact without id.",
          Status.BAD_REQUEST);
    }
    if (internshipDTO.getSubject() == null) {
      throw new WebApplicationException("Subject is null.", Status.BAD_REQUEST);
    }
    InternshipDTO internship = internshipUCC.updateSubject(internshipDTO);
    return toJsonObject(internship);
  }

  /**
   * Add an internship.
   *
   * @param internshipDTO The internship to add.
   * @return JSON object containing the added internship.
   * @throws WebApplicationException if information are missing.
   */
  @POST
  @Path("/add")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Etudiant"})
  public ObjectNode addInternship(InternshipDTO internshipDTO) {
    Logger.logEntry("POST /internships/addInternship");
    if (internshipDTO == null || internshipDTO.getSupervisorId() <= 0
        || internshipDTO.getContactId() <= 0
        || internshipDTO.getSignatureDate() == null
        || internshipDTO.getAcademicYear() <= 0) {
      throw new WebApplicationException("parameters missing.",
          Status.BAD_REQUEST);
    }
    InternshipDTO internship = internshipUCC.addInternship(internshipDTO);
    return toJsonObject(internship);
  }

  /**
   * Convert an InternshipDTO to a JSON object.
   *
   * @param internshipDTO The internship to convert.
   * @return The JSON object.
   */
  private ObjectNode toJsonObject(InternshipDTO internshipDTO) {
    ObjectNode node = jsonMapper.createObjectNode();
    node.put("id", internshipDTO.getId());
    node.put("subject", internshipDTO.getSubject());
    node.put("signatureDate", internshipDTO.getSignatureDate());
    node.put("supervisorId", internshipDTO.getSupervisorId());
    node.put("contactId", internshipDTO.getContactId());
    node.put("academicYear", internshipDTO.getAcademicYear());
    node.put("version", internshipDTO.getVersion());
    return node;
  }

}
