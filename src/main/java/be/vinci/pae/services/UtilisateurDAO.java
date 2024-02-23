package be.vinci.pae.services;

import be.vinci.pae.domain.UtilisateurDTO;

/**
 * Services pour l'utilisateur.
 */
public interface UtilisateurDAO {

  /**
   * Get un utilisateur de la DB par ID.
   *
   * @param id integer
   * @return utilisateur
   */
  UtilisateurDTO getUnUtilisateur(int id);

  /**
   * Get un utilisateur de la DB par email.
   *
   * @param email String
   * @return utilisateur
   */
  UtilisateurDTO getUnUtilisateurParEmail(String email);
}
