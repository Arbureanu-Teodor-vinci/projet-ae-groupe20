package be.vinci.pae.domain;

/**
 * Interface de l'Utilisateur avec les setters et getters
 */
public interface UtilisateurDTO {

  int getId();

  void setId(int id);

  String getNom();

  void setNom(String nom);

  String getPrenom();

  void setPrenom(String prenom);

  String getEmail();

  void setEmail(String email);

  int getNumTelephone();

  void setNumTelephone(int numTelephone);

  String getRole();

  void setRole(String role);

  String getMotDePasse();

  void setMotDePasse(String motDePasse);

  String hashMDP(String mdp);

  @Override
  boolean equals(Object o);

  @Override
  int hashCode();

  @Override
  String toString();
}
