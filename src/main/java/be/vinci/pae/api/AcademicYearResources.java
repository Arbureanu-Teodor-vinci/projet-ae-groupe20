package be.vinci.pae.api;

import be.vinci.pae.domain.AcademicYear.AcademicYearDTO;
import be.vinci.pae.domain.AcademicYear.AcademicYearUCC;
import be.vinci.pae.domain.Factory.DomainFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Singleton
@Path("/academicYear")
public class AcademicYearResources {

  private final ObjectMapper jsonMapper = new ObjectMapper();
  @Inject
  private AcademicYearUCC academicYearUCC;

  @Inject
  private DomainFactory domainFactory;

  /**
   * Get the actual academic year.
   *
   * @return the actual academic year.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode getAcademicYear() {
    ObjectNode node = jsonMapper.createObjectNode();
    AcademicYearDTO academicYearDTO = domainFactory.getAcademicYearDTO();
    academicYearDTO = academicYearUCC.getOrAddActualAcademicYear();
    node.put("id", academicYearDTO.getId());
    node.put("year", academicYearDTO.getYear());
    return node;
  }

}
