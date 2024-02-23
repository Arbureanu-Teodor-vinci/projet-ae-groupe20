package be.vinci.pae.domain;

/**
 * Interface factory pour chaque UCC.
 */
public interface DomainFactory {

  /**
   * Methode qui renvoie un nouvel utilisateur.
   *
   * @return UtilisateurDTO
   */
  UtilisateurDTO getUser();

}
