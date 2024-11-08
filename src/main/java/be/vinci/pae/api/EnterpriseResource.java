package be.vinci.pae.api;


import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import be.vinci.pae.domain.enterprise.EnterpriseUCC;
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

/**
 * Resource route for the enterprises requests.
 */
@Singleton
@Path("/enterprises")
public class EnterpriseResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private EnterpriseUCC enterpriseUCC;

  /**
   * Get 1 enterprises.
   *
   * @param id The id of the enterprise to get.
   * @return JSON object containing all enterprises.
   * @throws WebApplicationException If id is null or the token is invalid.
   */
  @GET
  @Path("getOne:{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Administratif", "Professeur", "Etudiant"})
  public ObjectNode getOneEnterprise(@PathParam("id") Integer id) {
    Logger.logEntry("GET /enterprises/getOne:" + id);
    // get the user token from the headers

    // if the id is null, throw an exception
    if (id == null) {
      Logger.logEntry("id is missing.");
      throw new WebApplicationException("Veuillez entrez un id", Status.BAD_REQUEST);
    }

    // Try to get the enterprise
    EnterpriseDTO enterprise = enterpriseUCC.getOneEnterprise(id);
    // if the enterprise is null, throw an exception
    if (enterprise == null) {
      Logger.logEntry("No enterprise found with this id");
      throw new WebApplicationException("Aucune entreprise trouvée avec cet id.",
          Status.NOT_FOUND);
    }

    return enterpriseNodeMaker(enterprise);

  }

  /**
   * Get all enterprises.
   *
   * @return JSON array containing the enterprises.
   * @throws WebApplicationException If the token is invalid.
   */
  @GET
  @Path("getAll")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Administratif", "Professeur", "Etudiant"})
  public ArrayNode getAllEnterprises() {
    Logger.logEntry("GET /enterprises/getAll");

    // Try to get all enterprises
    ArrayNode enterprisesListNode = jsonMapper.createArrayNode();
    for (EnterpriseDTO enterprise : enterpriseUCC.getAllEnterprises()) {
      enterprisesListNode.add(enterpriseNodeMaker(enterprise));
    }
    return enterprisesListNode;
  }

  /**
   * Add an enterprise.
   *
   * @param enterpriseDTO The enterprise to add.
   * @return JSON object containing the enterprise infos.
   */
  @POST
  @Path("add")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Administratif", "Professeur", "Etudiant"})
  public ObjectNode addEnterprise(EnterpriseDTO enterpriseDTO) {
    Logger.logEntry("POST /enterprises/add");

    // if the enterprise is null, throw an exception
    if (enterpriseDTO == null) {
      Logger.logEntry("EnterpriseDTO is null.");
      throw new WebApplicationException("Veuillez entrer une entreprise", Status.BAD_REQUEST);
    }

    if (enterpriseDTO.getTradeName().trim().isEmpty()) {
      Logger.logEntry("Trade name is missing.");
      throw new WebApplicationException("Veuillez entrer un nom commercial", Status.BAD_REQUEST);
    }

    if (enterpriseDTO.getCity().trim().isEmpty()) {
      Logger.logEntry("City is missing.");
      throw new WebApplicationException("Veuillez entrer une ville", Status.BAD_REQUEST);
    }

    if (enterpriseDTO.getAddress().trim().isEmpty()) {
      Logger.logEntry("Address is missing.");
      throw new WebApplicationException("Veuillez entrer une adresse", Status.BAD_REQUEST);
    }
    
    if (enterpriseDTO.getPhoneNumber().trim().isEmpty()) {
      Logger.logEntry("Phone number is missing.");
      throw new WebApplicationException("Veuillez entrer un numéro de téléphone",
          Status.BAD_REQUEST);
    }

    // Try to add the enterprise
    EnterpriseDTO enterprise = enterpriseUCC.addEnterprise(enterpriseDTO);
    // if the enterprise is null, throw an exception
    if (enterprise == null) {
      Logger.logEntry("Can't add enterprise");
      throw new WebApplicationException("Impossible d'ajouter une entreprise", Status.NOT_FOUND);
    }

    return enterpriseNodeMaker(enterprise);
  }

  /**
   * blacklist an enterprise.
   *
   * @param enterpriseDTO The enterprise to blacklist.
   * @return JSON object containing the enterprise infos.
   */
  @POST
  @Path("blacklist")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize(rolesAllowed = {"Administratif", "Professeur"})
  public EnterpriseDTO blacklist(EnterpriseDTO enterpriseDTO) {
    Logger.logEntry("POST /enterprises/blacklist");

    // if the enterprise is null, throw an exception
    if (enterpriseDTO == null || enterpriseDTO.getId() == 0) {
      throw new WebApplicationException("Veuillez entrez une entreprise", Status.BAD_REQUEST);
    }
    if (enterpriseDTO.getBlackListMotivation().trim().isEmpty()
        || enterpriseDTO.getBlackListMotivation() == null) {
      throw new WebApplicationException("Veuillez entrez une motivation pour le blacklist",
          Status.BAD_REQUEST);
    }
    // Try to add the enterprise
    EnterpriseDTO enterpriseUpdated = enterpriseUCC.blacklistEnterprise(enterpriseDTO);
    // if the enterprise is null, throw an exception
    if (enterpriseUpdated == null) {
      Logger.logEntry("Can't blacklist enterprise");
      throw new WebApplicationException("Impossible de blacklist cette entreprise",
          Status.NOT_FOUND);
    }
    return enterpriseUpdated;
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
          .put("address", enterprise.getAddress())
          .put("phoneNumber", enterprise.getPhoneNumber())
          .put("city", enterprise.getCity())
          .put("email", enterprise.getEmail())
          .put("blackListed", enterprise.isBlackListed())
          .put("blackListMotivation", enterprise.getBlackListMotivation())
          .put("version", enterprise.getVersion());
      return enterpriseNode;
    } catch (Exception e) {
      Logger.logEntry("Can't create enterprise", e, 2);
      System.out.println("Impossible de créer l'entreprise");
      return null;
    }
  }

}
