package be.vinci.pae.services;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.DomainFactoryImpl;
import be.vinci.pae.domain.Utilisateur;
import be.vinci.pae.services.utils.Json;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;

/**
 * Implementation des services utilisateur.
 */
public class UtilisateurDataServiceImpl implements UtilisateurDataService {

  private static final String NOM_COLLECTION = "utilisateurs";
  @Inject
  private static DomainFactory maDomainFactory = new DomainFactoryImpl();
  private static Json<Utilisateur> jsonDB = new Json<>(Utilisateur.class);
  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = new ObjectMapper();


  @Override
  public Utilisateur getUnUtilisateur(int id) {
    var lesUtilisateurs = jsonDB.parse(NOM_COLLECTION);
    return lesUtilisateurs.stream().filter(utilisateur -> utilisateur.getId() == id).findAny()
        .orElse(null);
  }

  @Override
  public Utilisateur getUnUtilisateur(String email) {
    var lesUtilisateurs = jsonDB.parse(NOM_COLLECTION);
    return lesUtilisateurs.stream().filter(utilisateur -> utilisateur.getEmail().equals(email))
        .findAny().orElse(null);
  }

  @Override
  public ObjectNode seConnecter(String email, String mdp) {
    Utilisateur utilisateur = getUnUtilisateur(email);
    if (utilisateur == null || !utilisateur.checkMDP(mdp)) {
      return null;
    }
    String token;
    try {
      token = JWT.create().withIssuer("auth0")
          .withClaim("utilisateurs", utilisateur.getId()).sign(this.jwtAlgorithm);
      ObjectNode publicUser = jsonMapper.createObjectNode()
          .put("token", token)
          .put("id", utilisateur.getId())
          .put("email", utilisateur.getEmail());
      return publicUser;

    } catch (Exception e) {
      System.out.println("Impossible de créer un token");
      return null;
    }
  }


}
