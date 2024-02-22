package be.vinci.pae.domain;

/**
 * Interface de l'utilisateur qui comporte les méthodes de check pour le CC.
 */
public interface Utilisateur extends UtilisateurDTO {

  /**
   * Check si mot de passe est crypté.
   *
   * @param mdp -> mot de passe à checker
   * @return true/false
   */
  boolean checkMDP(String mdp);

}
