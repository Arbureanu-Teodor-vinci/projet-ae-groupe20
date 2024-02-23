package be.vinci.pae.domain;

/**
 * Controlleur de l'utilisateur.
 */
public interface UtilisateurUCC {

  /**
   * Methode pour se connecter.
   *
   * @param email String
   * @param mdp   String
   * @return ObjectNode
   */
  UtilisateurDTO seConnecter(String email, String mdp);
}
