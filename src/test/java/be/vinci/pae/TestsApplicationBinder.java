package be.vinci.pae;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.DomainFactoryImpl;
import be.vinci.pae.domain.UserUCC;
import be.vinci.pae.domain.UserUCCImpl;
import be.vinci.pae.services.UserDAO;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.mockito.Mockito;

/**
 * Binds injection dependencies for tests.
 */
@Provider
public class TestsApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bind(DomainFactoryImpl.class).to(DomainFactory.class).in(Singleton.class);
    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);
    bind(Mockito.mock(UserDAO.class)).to(UserDAO.class);
  }
}
