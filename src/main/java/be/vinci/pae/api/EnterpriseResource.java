package be.vinci.pae.api;


import be.vinci.pae.domain.EnterpriseDTO;
import be.vinci.pae.domain.EnterpriseUCC;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * Ressource route for the enterprises requests.
 */
@Singleton
@Path("/enterprises")
public class EnterpriseResource {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private EnterpriseUCC enterpriseUCC;

  @GET
  @Path("getOne:{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode getOneEnterprise(@PathParam("id") Integer id, @Context HttpHeaders headers) {
    // get the user token from the headers
    String token = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
    // if the token is null or empty, throw an exception
    if (token == null || token.isEmpty()) {
      throw new WebApplicationException("Authorization header must be provided",
          Status.UNAUTHORIZED);
    }
    // verify the token
    try {
      JWT.require(jwtAlgorithm).build().verify(token);
    } catch (Exception e) {
      throw new WebApplicationException("Invalid token", Status.UNAUTHORIZED);
    }

    // if the id is null, throw an exception
    if (id == null) {
      throw new WebApplicationException("You must enter an id.", Status.BAD_REQUEST);
    }

    // Try to get the enterprise
    EnterpriseDTO enterprise = enterpriseUCC.getOneEnterprise(id);
    // if the enterprise is null, throw an exception
    if (enterprise == null) {
      throw new WebApplicationException("No enterprise found with this id",
          Status.NOT_FOUND);
    }

    try {
      ObjectNode enterpriseNode = jsonMapper.createObjectNode()
          .put("id", enterprise.getId())
          .put("tradeName", enterprise.getTradeName())
          .put("designation", enterprise.getDesignation())
          .put("adresse", enterprise.getAdresse())
          .put("phoneNumber", enterprise.getPhoneNumber())
          .put("email", enterprise.getEmail())
          .put("blackListed", enterprise.isBlackListed())
          .put("blackListMotivation", enterprise.getBlackListMotivation());
      return enterpriseNode;
    } catch (Exception e) {
      System.out.println("Can't create enterprise");
      return null;
    }

  }

}
