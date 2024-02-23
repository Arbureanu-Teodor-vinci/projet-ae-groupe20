package be.vinci.pae.domain;

import be.vinci.pae.services.UtilisateurDAO;

/**
 * Implementation du controlleur utilisateur.
 */
public class UtilisateurUCCImpl implements UtilisateurUCC {

  UtilisateurDAO utilisateurDS;

  @Override
  public UtilisateurDTO seConnecter(String email, String mdp) {

    Utilisateur utilisateur = utilisateurDS.getUnUtilisateur(email);
    if (utilisateur == null || !utilisateur.checkMDP(mdp)) {
      return null;
    }
    return (UtilisateurDTO) utilisateur;
  }
}
