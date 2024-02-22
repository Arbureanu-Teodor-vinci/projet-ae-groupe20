package be.vinci.pae.domain;

/**
 * Implementation de la factory des UCC
 */
public class DomainFactoryImpl implements DomainFactory {

  /**
   * Creation d'un nouveau UtilisateurDTO.
   *
   * @return UtilisateurDTO
   */
  public UtilisateurDTO getUser() {
    return new UtilisateurImpl();
  }

}
