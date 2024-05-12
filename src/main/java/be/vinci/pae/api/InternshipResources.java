package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
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
  @Authorize(rolesAllowed = {"Administratif", "Professeur", "Etudiant"})
  public ObjectNode getOneInternshipByStudentId(@PathParam("id") int id) {
    Logger.logEntry("GET /internships/getOneInternshipByStudentId" + id);
    if (id <= 0) {
      Logger.logEntry("Id must be positive.");
      throw new WebApplicationException("Id must be positive.", Status.BAD_REQUEST);
    }
    // Try to get the internship with the correct id.
    InternshipDTO internship = internshipUCC.getOneInternshipByStudentId(id);
    if (internship == null) {
      Logger.logEntry("Internship not found.");
      throw new WebApplicationException("Internship not found.");
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
    if (internshipDTO == null || internshipDTO.getSupervisor().getId() <= 0
        || internshipDTO.getContact().getId() <= 0
        || internshipDTO.getSignatureDate() == null || internshipDTO.getSignatureDate().isEmpty()) {
      throw new WebApplicationException("parameters missing.",
          Status.BAD_REQUEST);
    }
    InternshipDTO internship = internshipUCC.addInternship(internshipDTO);
    return toJsonObject(internship);
  }


  /**
   * Get the number of internships in an enterprise.
   *
   * @param id The id of the enterprise.
   * @return the number of interships in an enterprise
   * @throws WebApplicationException If the token is invalid.
   */
  @GET
  @Path("getNbInternships:{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Administratif", "Professeur", "Etudiant"})
  public int getNbInternships(@PathParam("id") Integer id) {
    Logger.logEntry("GET /enterprises/getNbInternships:" + id);

    // if the id is null, throw an exception
    if (id == null) {
      Logger.logEntry("id is missing.");
      throw new WebApplicationException("You must enter an id.", Status.BAD_REQUEST);
    }

    // Try to get the number of internships in the enterprise
    int nbInternships = internshipUCC.getNbInternships(id);

    return nbInternships;

  }

  /**
   * Get the number of internships in an enterprise for a specific academic year.
   *
   * @param id The id of the enterprise.
   * @return the number of interships in an enterprise
   * @throws WebApplicationException If the token is invalid.
   */
  @GET
  @Path("getNbInternshipsPerAcademicYear:{id}:{academicYear}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Administratif", "Professeur", "Etudiant"})
  public int getNbInternshipsPerAcademicYear(@PathParam("id") Integer id,
      @PathParam("academicYear") String academicYear) {
    Logger.logEntry("GET /enterprises/getNbInternshipsPerAcademicYear:" + id + ":" + academicYear);

    // if the id or academicYear is null, throw an exception
    if (id == null || academicYear == null) {
      Logger.logEntry("id or academicYear is missing.");
      throw new WebApplicationException("You must enter an id and an academic year.",
          Status.BAD_REQUEST);
    }

    // Try to get the number of internships
    int nbInternships = internshipUCC.getNbInternshipsPerAcademicYear(id, academicYear);

    return nbInternships;
  }

  /**
   * Convert an InternshipDTO to a JSON object.
   *
   * @param internshipDTO The internship to convert.
   * @return The JSON object.
   */
  private ObjectNode toJsonObject(InternshipDTO internshipDTO) {

    // Create the JSON object of the student academic year.
    ObjectNode studentAcademicYearNode = jsonMapper.createObjectNode()
        .put("id", internshipDTO.getContact().getStudent().getStudentAcademicYear().getId())
        .put("year", internshipDTO.getContact().getStudent().getStudentAcademicYear().getYear());

    // Create the JSON object of the student.
    ObjectNode contactStudentNode = jsonMapper.createObjectNode()
        .put("id", internshipDTO.getContact().getStudent().getId())
        .put("firstName", internshipDTO.getContact().getStudent().getFirstName())
        .put("lastName", internshipDTO.getContact().getStudent().getLastName())
        .put("email", internshipDTO.getContact().getStudent().getEmail())
        .put("telephoneNumber", internshipDTO.getContact().getStudent().getTelephoneNumber())
        .put("registrationDate",
            internshipDTO.getContact().getStudent().getRegistrationDate().toString())
        .put("role", internshipDTO.getContact().getStudent().getRole())
        .put("version", internshipDTO.getContact().getStudent().getVersion());
    contactStudentNode.set("academicYear", studentAcademicYearNode);

    // Create the JSON object of the enterprise.
    ObjectNode contactEnterpriseNode = jsonMapper.createObjectNode()
        .put("id", internshipDTO.getContact().getEnterprise().getId())
        .put("tradeName", internshipDTO.getContact().getEnterprise().getTradeName())
        .put("designation", internshipDTO.getContact().getEnterprise().getDesignation())
        .put("address", internshipDTO.getContact().getEnterprise().getAddress())
        .put("phoneNumber", internshipDTO.getContact().getEnterprise().getPhoneNumber())
        .put("city", internshipDTO.getContact().getEnterprise().getCity())
        .put("email", internshipDTO.getContact().getEnterprise().getEmail())
        .put("blackListed", internshipDTO.getContact().getEnterprise().isBlackListed())
        .put("blackListMotivation",
            internshipDTO.getContact().getEnterprise().getBlackListMotivation())
        .put("version", internshipDTO.getContact().getEnterprise().getVersion());

    // Create the JSON object of the academic year.
    ObjectNode contactAcademicYearNode = jsonMapper.createObjectNode()
        .put("id", internshipDTO.getContact().getAcademicYear().getId())
        .put("year", internshipDTO.getContact().getAcademicYear().getYear());

    // Create the JSON object of the contact.
    ObjectNode contactNode = jsonMapper.createObjectNode()
        .put("id", internshipDTO.getContact().getId())
        .put("interviewMethod", internshipDTO.getContact().getInterviewMethod())
        .put("tool", internshipDTO.getContact().getTool())
        .put("refusalReason", internshipDTO.getContact().getRefusalReason())
        .put("stateContact", internshipDTO.getContact().getStateContact())
        .put("version", internshipDTO.getContact().getVersion());

    // Set the student, enterprise and academic year in the contact JSON object.
    contactNode.set("student", contactStudentNode);
    contactNode.set("enterprise", contactEnterpriseNode);
    contactNode.set("academicYear", contactAcademicYearNode);

    // Create the JSON object of the supervisor enterprise.
    ObjectNode supervisorEnterpriseNode = jsonMapper.createObjectNode();
    supervisorEnterpriseNode.put("id", internshipDTO.getSupervisor().getEnterprise().getId());
    supervisorEnterpriseNode.put("tradeName",
        internshipDTO.getSupervisor().getEnterprise().getTradeName());
    supervisorEnterpriseNode.put("designation",
        internshipDTO.getSupervisor().getEnterprise().getDesignation());
    supervisorEnterpriseNode.put("address",
        internshipDTO.getSupervisor().getEnterprise().getAddress());
    supervisorEnterpriseNode.put("phoneNumber",
        internshipDTO.getSupervisor().getEnterprise().getPhoneNumber());
    supervisorEnterpriseNode.put("city", internshipDTO.getSupervisor().getEnterprise().getCity());
    supervisorEnterpriseNode.put("email", internshipDTO.getSupervisor().getEnterprise().getEmail());
    supervisorEnterpriseNode.put("blackListed",
        internshipDTO.getSupervisor().getEnterprise().isBlackListed());
    supervisorEnterpriseNode.put("blackListMotivation",
        internshipDTO.getSupervisor().getEnterprise().getBlackListMotivation());
    supervisorEnterpriseNode.put("version",
        internshipDTO.getSupervisor().getEnterprise().getVersion());

    // Create the JSON object of the supervisor.
    ObjectNode supervisorNode = jsonMapper.createObjectNode();
    supervisorNode.put("id", internshipDTO.getSupervisor().getId());
    supervisorNode.put("email", internshipDTO.getSupervisor().getEmail());
    supervisorNode.put("firstName", internshipDTO.getSupervisor().getFirstName());
    supervisorNode.put("lastName", internshipDTO.getSupervisor().getLastName());
    supervisorNode.put("phoneNumber", internshipDTO.getSupervisor().getPhoneNumber());
    supervisorNode.set("enterprise", supervisorEnterpriseNode);

    // Create the JSON object of the internship academic year.
    ObjectNode internshipAcademicYearNode = jsonMapper.createObjectNode();
    internshipAcademicYearNode.put("id", internshipDTO.getAcademicYear().getId());
    internshipAcademicYearNode.put("year", internshipDTO.getAcademicYear().getYear());

    // Create the JSON object of the internship.
    ObjectNode internship = jsonMapper.createObjectNode();
    internship.put("id", internshipDTO.getId());
    internship.put("subject", internshipDTO.getSubject());
    internship.put("signatureDate", internshipDTO.getSignatureDate());
    internship.set("supervisor", supervisorNode);
    internship.set("contact", contactNode);
    internship.set("academicYear", internshipAcademicYearNode);
    internship.put("version", internshipDTO.getVersion());
    return internship;
  }

}
