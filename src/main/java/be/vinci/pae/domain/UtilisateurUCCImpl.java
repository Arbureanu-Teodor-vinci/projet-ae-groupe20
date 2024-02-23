package be.vinci.pae.domain;

import be.vinci.pae.services.UtilisateurDAO;
import jakarta.inject.Inject;

/**
 * Implementation du controlleur utilisateur.
 */
public class UtilisateurUCCImpl implements UtilisateurUCC {

  @Inject
  UtilisateurDAO utilisateurDS;

  @Override
  public UtilisateurDTO seConnecter(String email, String mdp) {

    UtilisateurDTO utilisateurdto = utilisateurDS.getUnUtilisateurParEmail(email);
    Utilisateur utilisateur = (Utilisateur) utilisateurdto;
    if (utilisateur == null || !utilisateur.checkMDP(mdp)) {
      return null;
    }
    return utilisateurdto;
  }
}
