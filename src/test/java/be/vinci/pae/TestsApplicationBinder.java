package be.vinci.pae;

import be.vinci.pae.domain.AcademicYear.AcademicYearUCC;
import be.vinci.pae.domain.AcademicYear.AcademicYearUCCImpl;
import be.vinci.pae.domain.Enterprise.EnterpriseUCC;
import be.vinci.pae.domain.Enterprise.EnterpriseUCCImpl;
import be.vinci.pae.domain.Factory.DomainFactory;
import be.vinci.pae.domain.Factory.DomainFactoryImpl;
import be.vinci.pae.domain.User.StudentUCC;
import be.vinci.pae.domain.User.StudentUCCImpl;
import be.vinci.pae.domain.User.UserUCC;
import be.vinci.pae.domain.User.UserUCCImpl;
import be.vinci.pae.services.AcademicYearServices.AcademicYearDAO;
import be.vinci.pae.services.DAL.DALTransactionServices;
import be.vinci.pae.services.EnterpriseServices.EnterpriseDAO;
import be.vinci.pae.services.UserServices.StudentDAO;
import be.vinci.pae.services.UserServices.UserDAO;
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
    bind(StudentUCCImpl.class).to(StudentUCC.class).in(Singleton.class);
    bind(AcademicYearUCCImpl.class).to(AcademicYearUCC.class).in(Singleton.class);
    bind(Mockito.mock(AcademicYearDAO.class)).to(AcademicYearDAO.class);
    bind(Mockito.mock(UserDAO.class)).to(UserDAO.class);
    bind(Mockito.mock(StudentDAO.class)).to(StudentDAO.class);
    bind(EnterpriseUCCImpl.class).to(EnterpriseUCC.class).in(Singleton.class);
    bind(Mockito.mock(EnterpriseDAO.class)).to(EnterpriseDAO.class);
    bind(Mockito.mock(DALTransactionServices.class)).to(DALTransactionServices.class);
  }
}
