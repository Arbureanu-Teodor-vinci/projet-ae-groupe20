package be.vinci.pae.domain.Factory;

import be.vinci.pae.domain.Enterprise.EnterpriseDTO;
import be.vinci.pae.domain.Enterprise.EnterpriseImpl;
import be.vinci.pae.domain.User.UserDTO;
import be.vinci.pae.domain.User.UserImpl;

/**
 * Implementation of factory for each UCC.
 */
public class DomainFactoryImpl implements DomainFactory {

  @Override
  public UserDTO getUserDTO() {
    return new UserImpl();
  }

  @Override
  public EnterpriseDTO getEnterpriseDTO() {
    return new EnterpriseImpl();
  }

}
