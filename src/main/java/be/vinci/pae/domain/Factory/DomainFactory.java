package be.vinci.pae.domain.Factory;

import be.vinci.pae.domain.AcademicYear.AcademicYearDTO;
import be.vinci.pae.domain.Enterprise.EnterpriseDTO;
import be.vinci.pae.domain.User.UserDTO;

/**
 * Interface factory for each UCC.
 */
public interface DomainFactory {

  /**
   * Method returning a new user.
   *
   * @return UserDTO
   */
  UserDTO getUserDTO();

  /**
   * Method returning a new enterprise.
   *
   * @return a new EnterpriseDTO
   */
  EnterpriseDTO getEnterpriseDTO();

  /**
   * Method returning a new academic year.
   *
   * @return a new AcademicYearDTO
   */
  AcademicYearDTO getAcademicYearDTO();
}
