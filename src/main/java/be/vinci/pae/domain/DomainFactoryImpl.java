package be.vinci.pae.domain;

/**
 * Implementation of factory for each UCC.
 */
public class DomainFactoryImpl implements DomainFactory {

  @Override
  public UserDTO getUserDTO() {
    return new UserImpl();
  }

}
