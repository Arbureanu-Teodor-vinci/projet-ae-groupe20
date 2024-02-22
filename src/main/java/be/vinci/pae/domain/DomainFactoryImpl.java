package be.vinci.pae.domain;

public class DomainFactoryImpl implements DomainFactory {

  /**
   * Creation d'un nouveau UtilisateurDTO
   *
   * @return UtilisateurDTO
   */
  public UtilisateurDTO getUser() {
    return new UtilisateurImpl();
  }

}
