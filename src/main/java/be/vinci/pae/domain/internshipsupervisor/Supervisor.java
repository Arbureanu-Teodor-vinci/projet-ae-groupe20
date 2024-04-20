package be.vinci.pae.domain.internshipsupervisor;

import be.vinci.pae.api.filters.BusinessException;

/**
 * Interface of Supervisor for business checks.
 */
public interface Supervisor extends SupervisorDTO {

  /**
   * Check if the email is unique.
   * 
   * @param supervisorDTO SupervisorDTO
   */
  void checkUniqueEmail(SupervisorDTO supervisorDTO) throws BusinessException;


}
