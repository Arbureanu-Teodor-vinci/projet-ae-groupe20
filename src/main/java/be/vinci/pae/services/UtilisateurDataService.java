package be.vinci.pae.services;

import be.vinci.pae.domain.Utilisateur;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface UtilisateurDataService {

  Utilisateur getUnUtilisateur(int id);

  Utilisateur getUnUtilisateur(String email);

  ObjectNode seConnecter(String email, String mdp);
}
