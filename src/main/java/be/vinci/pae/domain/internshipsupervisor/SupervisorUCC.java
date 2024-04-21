package be.vinci.pae.domain.internshipsupervisor;

import java.util.List;

/**
 * Interface of SupervisorUCC for checking methods.
 */
public interface SupervisorUCC {

  /**
   * Get all supervisors.
   *
   * @return List of SupervisorDTO
   */
  List<SupervisorDTO> getAllInternshipSupervisors();

  /**
   * Get one supervisor.
   *
   * @param id the id of the supervisor
   * @return SupervisorDTO
   */
  SupervisorDTO getOneSupervisorById(int id);

  /**
   * Get supervisors by enterprise.
   *
   * @param idEnterprise the id of the enterprise
   * @return List of SupervisorDTO
   */
  List<SupervisorDTO> getSupervisorsByEnterprise(int idEnterprise);

  /**
   * Add a supervisor.
   *
   * @param supervisor the supervisor to add
   * @return SupervisorDTO
   */
  SupervisorDTO addSupervisor(SupervisorDTO supervisor);


}
