package be.vinci.pae.utils;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.DomainFactoryImpl;
import be.vinci.pae.domain.UtilisateurUCC;
import be.vinci.pae.domain.UtilisateurUCCImpl;
import be.vinci.pae.services.UtilisateurDAO;
import be.vinci.pae.services.UtilisateurDAOImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * Classe qui permet de créer une seule instance à injecter.
 */
@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bind(DomainFactoryImpl.class).to(DomainFactory.class).in(Singleton.class);
    bind(UtilisateurDAOImpl.class).to(UtilisateurDAO.class).in(Singleton.class);
    bind(UtilisateurUCCImpl.class).to(UtilisateurUCC.class).in(Singleton.class);
  }
}
