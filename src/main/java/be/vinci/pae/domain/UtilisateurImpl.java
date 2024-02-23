package be.vinci.pae.domain;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Implementation de Utilisateur et UtilisateurDTO.
 */
public class UtilisateurImpl implements Utilisateur {

  private static final String[] ROLES_POSSIBLES = {"étudiant", "professeur", "administratif"};
  private int id;
  private String nom;
  private String prenom;
  private String email;
  private int numTelephone;
  private String role;
  private String motDePasse;

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
  public int getNumTelephone() {
    return numTelephone;
  }

  @Override
  public void setNumTelephone(int numTelephone) {
    this.numTelephone = numTelephone;
  }

  @Override
  public String getRole() {
    return role;
  }

  @Override
  public void setRole(String role) {
    if (role.equals(ROLES_POSSIBLES[0]) || role.equals(ROLES_POSSIBLES[1]) || role.equals(
        ROLES_POSSIBLES[2])) {
      this.role = role;
    }
  }

  @Override
  public String getMotDePasse() {
    return motDePasse;
  }

  @Override
  public void setMotDePasse(String motDePasse) {
    this.motDePasse = motDePasse;
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
    return "UserDTO{"
        + "id_utilisateur=" + id
        + ", nom='" + nom + '\''
        + ", prenom='" + prenom + '\''
        + ", email='" + email + '\''
        + ", num_telephone=" + numTelephone
        + ", role='" + role + '\''
        + ", mot_de_passe='" + motDePasse + '\''
        + '}';
  }

  @Override
  public boolean checkMDP(String mdp) {
    return BCrypt.checkpw(mdp, this.motDePasse);
  }
}
