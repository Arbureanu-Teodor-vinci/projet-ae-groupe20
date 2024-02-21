package be.vinci.pae.domain;

public class DomainFactoryImpl implements DomainFactory {

  public UtilisateurDTO getUser() {
    return new UtilisateurImpl();
  }

}
