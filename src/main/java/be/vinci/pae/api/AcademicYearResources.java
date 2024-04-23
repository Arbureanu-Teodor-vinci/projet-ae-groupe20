package be.vinci.pae.api;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.academicyear.AcademicYearDTO;
import be.vinci.pae.domain.academicyear.AcademicYearUCC;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.utils.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

/**
 * AcademicYearResources class.
 */
@Singleton
@Path("/academicYear")
public class AcademicYearResources {

  private final ObjectMapper jsonMapper = new ObjectMapper();
  @Inject
  private AcademicYearUCC academicYearUCC;

  @Inject
  private DomainFactory domainFactory;

  /**
   * Get all academic years.
   *
   * @return list of academic years.
   */
  @GET
  @Path("/all")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Administratif", "Professeur"})
  public List<String> getAllAcademicYears() {
    Logger.logEntry("GET /academicYear/all");
    return academicYearUCC.getAllAcademicYears();
  }


  /**
   * Get the actual academic year.
   *
   * @return the actual academic year.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode getAcademicYear() {
    Logger.logEntry("GET /academicYear");
    ObjectNode node = jsonMapper.createObjectNode();
    AcademicYearDTO academicYearDTO = domainFactory.getAcademicYearDTO();
    academicYearDTO = academicYearUCC.getOrAddActualAcademicYear();
    node.put("id", academicYearDTO.getId());
    node.put("year", academicYearDTO.getYear());
    return node;
  }

}
