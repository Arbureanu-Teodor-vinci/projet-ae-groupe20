package be.vinci.pae.api;


import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import be.vinci.pae.domain.enterprise.EnterpriseUCC;
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
 * Resource route for the enterprises requests.
 */
@Singleton
@Path("/enterprises")
public class EnterpriseResource {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private EnterpriseUCC enterpriseUCC;

  /**
   * Get 1 enterprises.
   *
   * @param id      The id of the enterprise to get.
   * @param headers The headers of the request.
   * @return JSON object containing all enterprises.
   * @throws WebApplicationException If id is null or the token is invalid.
   */
  @GET
  @Path("getOne:{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode getOneEnterprise(@PathParam("id") Integer id, @Context HttpHeaders headers) {
    Logger.logEntry("GET /enterprises/getOne:" + id);
    // get the user token from the headers
    String token = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
    // if the token is null or empty, throw an exception
    if (token == null || token.isEmpty()) {
      Logger.logEntry("tries to access without token.");
      throw new WebApplicationException("Authorization header must be provided",
          Status.UNAUTHORIZED);
    }
    // verify the token
    try {
      JWT.require(jwtAlgorithm).build().verify(token);
    } catch (Exception e) {
      Logger.logEntry("Invalid token", e);
      throw new WebApplicationException("Invalid token", Status.UNAUTHORIZED);
    }

    // if the id is null, throw an exception
    if (id == null) {
      Logger.logEntry("id is missing.");
      throw new WebApplicationException("You must enter an id.", Status.BAD_REQUEST);
    }

    // Try to get the enterprise
    EnterpriseDTO enterprise = enterpriseUCC.getOneEnterprise(id);
    // if the enterprise is null, throw an exception
    if (enterprise == null) {
      Logger.logEntry("No enterprise found with this id");
      throw new WebApplicationException("No enterprise found with this id",
          Status.NOT_FOUND);
    }

    return enterpriseNodeMaker(enterprise);

  }

  /**
   * Get all enterprises.
   *
   * @param headers The headers of the request.
   * @return JSON array containing the enterprises.
   * @throws WebApplicationException If the token is invalid.
   */
  @GET
  @Path("getAll")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ArrayNode getAllEnterprises(@Context HttpHeaders headers) {
    Logger.logEntry("GET /enterprises/getAll");
    // get the user token from the headers
    String token = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
    // if the token is null or empty, throw an exception
    if (token == null || token.isEmpty()) {
      Logger.logEntry("tries to access without token.");
      throw new WebApplicationException("Authorization header must be provided",
          Status.UNAUTHORIZED);
    }
    // verify the token
    try {
      JWT.require(jwtAlgorithm).build().verify(token);
    } catch (Exception e) {
      Logger.logEntry("Invalid token", e);
      throw new WebApplicationException("Invalid token", Status.UNAUTHORIZED);
    }

    // Try to get all enterprises
    ArrayNode enterprisesListNode = jsonMapper.createArrayNode();
    for (EnterpriseDTO enterprise : enterpriseUCC.getAllEnterprises()) {
      enterprisesListNode.add(enterpriseNodeMaker(enterprise));
    }
    return enterprisesListNode;
  }

  /**
   * Create a JSON object with the enterprise infos.
   *
   * @param enterprise The enterprise to create the JSON object with.
   * @return JSON object containing the enterprise infos.
   */
  private ObjectNode enterpriseNodeMaker(EnterpriseDTO enterprise) {
    try {
      // Create a JSON object with the enterprise infos
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
      Logger.logEntry("Can't create enterprise", e);
      System.out.println("Can't create enterprise");
      return null;
    }
  }

}
