package be.vinci.pae.api;

import be.vinci.pae.services.UtilisateurDataService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Singleton
@Path("/auths")
public class AuthsRessource {

  @Inject
  UtilisateurDataService maUtilisateurDataService;

  @POST
  @Path("seConnecter")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode seConnecter(JsonNode json) {
    if (!json.hasNonNull("email") || !json.hasNonNull("mot_de_passe")) {
      throw new WebApplicationException("email ou mot de passe nécessaire",
          Response.Status.BAD_REQUEST);
    }
    String email = json.get("email").asText();
    String mdp = json.get("mot_de_passe").asText();

    // Essayer se connecter
    ObjectNode lUtilisateur = maUtilisateurDataService.seConnecter(email, mdp);
    if (lUtilisateur == null) {
      throw new WebApplicationException("Email ou mot de passe incorrect",
          Response.Status.UNAUTHORIZED);
    }
    return lUtilisateur;

  }
}
