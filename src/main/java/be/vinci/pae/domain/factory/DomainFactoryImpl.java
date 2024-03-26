package be.vinci.pae.domain.factory;

import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import be.vinci.pae.domain.enterprise.EnterpriseImpl;
import be.vinci.pae.domain.user.StudentDTO;
import be.vinci.pae.domain.user.StudentImpl;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.user.UserImpl;
import be.vinci.pae.domain.academicyear.AcademicYearDTO;
import be.vinci.pae.domain.academicyear.AcademicYearImpl;

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

  @Override
  public AcademicYearDTO getAcademicYearDTO() {
    return new AcademicYearImpl();
  }

  @Override
  public StudentDTO getStudentDTO() {
    return new StudentImpl();
  }

}