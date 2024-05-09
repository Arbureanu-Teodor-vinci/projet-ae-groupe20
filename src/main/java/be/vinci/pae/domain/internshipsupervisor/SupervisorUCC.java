package be.vinci.pae.domain.internshipsupervisor;

import be.vinci.pae.domain.user.UserDTO;
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
   * @param newSupervisor the new supervisor
   * @param user          the user who wants to add the supervisor
   * @return SupervisorDTO
   */
  SupervisorDTO addSupervisor(SupervisorDTO newSupervisor, UserDTO user);


}
