package be.vinci.pae.domain;

import org.mindrot.jbcrypt.BCrypt;

public class UtilisateurImpl implements Utilisateur {

  private static final String[] ROLES_POSSIBLES = {"étudiant", "professeur", "administratif"};
  private int id;
  private String nom;
  private String prenom;
  private String email;
  private int num_telephone;
  private String role;
  private String mot_de_passe;

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getNom() {
    return nom;
  }

  @Override
  public void setNom(String nom) {
    this.nom = nom;
  }

  @Override
  public String getPrenom() {
    return prenom;
  }

  @Override
  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public int getNum_telephone() {
    return num_telephone;
  }

  @Override
  public void setNum_telephone(int num_telephone) {
    this.num_telephone = num_telephone;
  }

  @Override
  public String getRole() {
    return role;
  }

  @Override
  public void setRole(String role) {
    this.role = role;
  }

  @Override
  public String getMot_de_passe() {
    return mot_de_passe;
  }

  @Override
  public void setMot_de_passe(String mot_de_passe) {
    this.mot_de_passe = mot_de_passe;
  }

  @Override
  public String hashMDP(String mdp) {
    return BCrypt.hashpw(mdp, BCrypt.gensalt());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UtilisateurImpl userDTO = (UtilisateurImpl) o;
    return id == userDTO.id;
  }

  @Override
  public int hashCode() {
    return id;
  }

  @Override
  public String toString() {
    return "UserDTO{" +
        "id_utilisateur=" + id +
        ", nom='" + nom + '\'' +
        ", prenom='" + prenom + '\'' +
        ", email='" + email + '\'' +
        ", num_telephone=" + num_telephone +
        ", role='" + role + '\'' +
        ", mot_de_passe='" + mot_de_passe + '\'' +
        '}';
  }

  @Override
  public boolean checkMDP(String mdp) {
    return BCrypt.checkpw(mdp, this.mot_de_passe);
  }
}
