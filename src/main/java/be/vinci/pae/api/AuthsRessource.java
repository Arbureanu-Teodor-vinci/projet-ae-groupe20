package be.vinci.pae.api;

import be.vinci.pae.domain.UtilisateurDTO;
import be.vinci.pae.domain.UtilisateurUCC;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

/**
 * Cette classe représente une ressource pour l'authentification et l'enregistrement des
 * utilisateurs. Elle fournit des méthodes pour permettre aux utilisateurs de se connecter et
 * s'enregistrer.
 */
@Singleton
@Path("/auths")
public class AuthsRessource {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  UtilisateurUCC utilisateurControleur;

  /**
   * Permet à un utilisateur de se connecter en fournissant un email et un mot de passe.
   *
   * @param json Un objet JSON contenant les informations d'authentification de l'utilisateur,
   *             comprenant son email et son mot de passe.
   * @return Un objet JSON représentant les informations de l'utilisateur connecté.
   * @throws WebApplicationException Si l'email ou le mot de passe est manquant ou incorrect, une
   *                                 exception est levée avec le statut correspondant.
   */
  @POST
  @Path("seconnecter")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode seConnecter(JsonNode json) {
    if (!json.hasNonNull("email") || !json.hasNonNull("motDePasse")) {
      throw new WebApplicationException("email ou mot de passe nécessaire",
          Response.Status.BAD_REQUEST);
    }
    String email = json.get("email").asText();
    String mdp = json.get("motDePasse").asText();

    // Essayer se connecter
    UtilisateurDTO utilisateur = utilisateurControleur.seConnecter(email, mdp);

    if (utilisateur == null) {
      throw new WebApplicationException("Email ou mot de passe incorrect",
          Response.Status.UNAUTHORIZED);
    }
    String token;
    try {
      token = JWT.create().withIssuer("auth0")
          .withClaim("utilisateurs", utilisateur.getId()).sign(this.jwtAlgorithm);
      ObjectNode publicUtilisateur = jsonMapper.createObjectNode()
          .put("token", token)
          .put("id", utilisateur.getId())
          .put("email", utilisateur.getEmail());
      return publicUtilisateur;

    } catch (Exception e) {
      System.out.println("Impossible de créer un token");
      return null;
    }
  }
}
