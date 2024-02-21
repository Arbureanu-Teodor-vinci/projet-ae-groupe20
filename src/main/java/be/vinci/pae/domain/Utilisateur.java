package be.vinci.pae.domain;

public interface Utilisateur extends UtilisateurDTO {

  boolean checkMDP(String mdp);

}
