package be.vinci.pae;

import be.vinci.pae.domain.academicyear.AcademicYearUCC;
import be.vinci.pae.domain.academicyear.AcademicYearUCCImpl;
import be.vinci.pae.domain.contact.ContactUCC;
import be.vinci.pae.domain.contact.ContactUCCImpl;
import be.vinci.pae.domain.enterprise.EnterpriseUCC;
import be.vinci.pae.domain.enterprise.EnterpriseUCCImpl;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.domain.factory.DomainFactoryImpl;
import be.vinci.pae.domain.internshipSupervisor.SupervisorUCC;
import be.vinci.pae.domain.internshipSupervisor.SupervisorUCCImpl;
import be.vinci.pae.domain.user.StudentUCC;
import be.vinci.pae.domain.user.StudentUCCImpl;
import be.vinci.pae.domain.user.UserUCC;
import be.vinci.pae.domain.user.UserUCCImpl;
import be.vinci.pae.services.academicyear.AcademicYearDAO;
import be.vinci.pae.services.contactservices.ContactDAO;
import be.vinci.pae.services.dal.DALTransactionServices;
import be.vinci.pae.services.enterpriseservices.EnterpriseDAO;
import be.vinci.pae.services.internshipSupervisorServices.SupervisorDAO;
import be.vinci.pae.services.userservices.StudentDAO;
import be.vinci.pae.services.userservices.UserDAO;
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
    bind(ContactUCCImpl.class).to(ContactUCC.class).in(Singleton.class);
    bind(Mockito.mock(ContactDAO.class)).to(ContactDAO.class);
    bind(SupervisorUCCImpl.class).to(SupervisorUCC.class).in(Singleton.class);
    bind(Mockito.mock(SupervisorDAO.class)).to(SupervisorDAO.class);
  }
}
