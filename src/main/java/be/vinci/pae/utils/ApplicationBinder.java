package be.vinci.pae.utils;

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
import be.vinci.pae.services.academicyear.AcademicYearDAOImpl;
import be.vinci.pae.services.contactservices.ContactDAO;
import be.vinci.pae.services.contactservices.ContactDAOImpl;
import be.vinci.pae.services.dal.DALServices;
import be.vinci.pae.services.dal.DALServicesImpl;
import be.vinci.pae.services.dal.DALTransactionServices;
import be.vinci.pae.services.enterpriseservices.EnterpriseDAO;
import be.vinci.pae.services.enterpriseservices.EnterpriseDAOImpl;
import be.vinci.pae.services.internshipSupervisorServices.SupervisorDAO;
import be.vinci.pae.services.internshipSupervisorServices.SupervisorDAOImpl;
import be.vinci.pae.services.userservices.StudentDAO;
import be.vinci.pae.services.userservices.StudentDAOImpl;
import be.vinci.pae.services.userservices.UserDAO;
import be.vinci.pae.services.userservices.UserDAOImpl;
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
    bind(AcademicYearUCCImpl.class).to(AcademicYearUCC.class).in(Singleton.class);
    bind(AcademicYearDAOImpl.class).to(AcademicYearDAO.class).in(Singleton.class);
    bind(StudentUCCImpl.class).to(StudentUCC.class).in(Singleton.class);
    bind(StudentDAOImpl.class).to(StudentDAO.class).in(Singleton.class);
    bind(ContactUCCImpl.class).to(ContactUCC.class).in(Singleton.class);
    bind(ContactDAOImpl.class).to(ContactDAO.class).in(Singleton.class);
    bind(SupervisorUCCImpl.class).to(SupervisorUCC.class).in(Singleton.class);
    bind(SupervisorDAOImpl.class).to(SupervisorDAO.class).in(Singleton.class);
  }
}
