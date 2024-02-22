package be.vinci.pae.services;

import be.vinci.pae.domain.Utilisateur;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Services pour l'utilisateur.
 */
public interface UtilisateurDataService {

  /**
   * Get un utilisateur de la DB par ID.
   *
   * @param id integer
   * @return utilisateur
   */
  Utilisateur getUnUtilisateur(int id);

  /**
   * Get un utilisateur de la DB par email.
   *
   * @param email String
   * @return utilisateur
   */
  Utilisateur getUnUtilisateur(String email);

  /**
   * Methode pour se connecter.
   *
   * @param email String
   * @param mdp   String
   * @return ObjectNode
   */
  ObjectNode seConnecter(String email, String mdp);
}
