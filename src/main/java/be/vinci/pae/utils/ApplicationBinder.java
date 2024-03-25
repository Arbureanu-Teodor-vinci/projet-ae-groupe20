package be.vinci.pae.utils;

import be.vinci.pae.domain.Factory.DomainFactory;
import be.vinci.pae.domain.Factory.DomainFactoryImpl;
import be.vinci.pae.domain.Enterprise.EnterpriseUCC;
import be.vinci.pae.domain.Enterprise.EnterpriseUCCImpl;
import be.vinci.pae.domain.User.UserUCC;
import be.vinci.pae.domain.User.UserUCCImpl;
import be.vinci.pae.services.DAL.DALServices;
import be.vinci.pae.services.DAL.DALServicesImpl;
import be.vinci.pae.services.DAL.DALTransactionServices;
import be.vinci.pae.services.EnterpriseServices.EnterpriseDAO;
import be.vinci.pae.services.EnterpriseServices.EnterpriseDAOImpl;
import be.vinci.pae.services.UserServices.UserDAO;
import be.vinci.pae.services.UserServices.UserDAOImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * Binds injection dependencies.
 */
@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bind(DomainFactoryImpl.class).to(DomainFactory.class).in(Singleton.class);
    bind(UserDAOImpl.class).to(UserDAO.class).in(Singleton.class);
    bind(UserUCCImpl.class).to(UserUCC.class).in(Singleton.class);
    bind(DALServicesImpl.class).to(DALTransactionServices.class)
        .to(DALServices.class).in(Singleton.class);
    bind(EnterpriseDAOImpl.class).to(EnterpriseDAO.class).in(Singleton.class);
    bind(EnterpriseUCCImpl.class).to(EnterpriseUCC.class).in(Singleton.class);
  }
}
