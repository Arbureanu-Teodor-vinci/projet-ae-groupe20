package be.vinci.pae.domain;

public interface UtilisateurDTO {

  int getId();

  void setId(int id);

  String getNom();

  void setNom(String nom);

  String getPrenom();

  void setPrenom(String prenom);

  String getEmail();

  void setEmail(String email);

  int getNum_telephone();

  void setNum_telephone(int num_telephone);

  String getRole();

  void setRole(String role);

  String getMot_de_passe();

  void setMot_de_passe(String mot_de_passe);

  String hashMDP(String mdp);

  @Override
  boolean equals(Object o);

  @Override
  int hashCode();

  @Override
  String toString();
}
