package be.vinci.pae.domain;

/**
 * Interface de l'Utilisateur avec les setters et getters.
 */
public interface UtilisateurDTO {

  /**
   * Get ID.
   *
   * @return id integer
   */
  int getId();

  /**
   * Set ID.
   *
   * @param id integer
   */
  void setId(int id);

  /**
   * Get nom.
   *
   * @return String
   */
  String getNom();

  /**
   * Set nom.
   *
   * @param nom String
   */
  void setNom(String nom);

  /**
   * Get prenom.
   *
   * @return String
   */
  String getPrenom();

  /**
   * Set prenom.
   *
   * @param prenom String
   */
  void setPrenom(String prenom);

  /**
   * Get email.
   *
   * @return String
   */
  String getEmail();

  /**
   * Set eamil.
   *
   * @param email String
   */
  void setEmail(String email);

  /**
   * Get numero de telephone.
   *
   * @return integer
   */
  int getNumTelephone();

  /**
   * Set numero de telephone.
   *
   * @param numTelephone integer
   */
  void setNumTelephone(int numTelephone);

  /**
   * Get role.
   *
   * @return String
   */
  String getRole();

  /**
   * Set role.
   *
   * @param role String
   */
  void setRole(String role);

  /**
   * Get mot de passe.
   *
   * @return String
   */
  String getMotDePasse();

  /**
   * Set mot de passe.
   *
   * @param motDePasse String
   */
  void setMotDePasse(String motDePasse);

  /**
   * Crypter un mot de passe.
   *
   * @param mdp String
   * @return String
   */
  String hashMDP(String mdp);

  @Override
  boolean equals(Object o);

  @Override
  int hashCode();

  @Override
  String toString();
}
