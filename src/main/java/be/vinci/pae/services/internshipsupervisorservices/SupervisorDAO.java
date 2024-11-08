package be.vinci.pae.services.internshipsupervisorservices;

import be.vinci.pae.domain.internshipsupervisor.SupervisorDTO;
import java.util.List;

/**
 * Interface of SupervisorDAO.
 */
public interface SupervisorDAO {

  /**
   * Get all supervisors from the database.
   *
   * @return List of InternshipSupervisorDTO
   */
  List<SupervisorDTO> getAllSupervisors();

  /**
   * Get one supervisor by id.
   *
   * @param id the id of the supervisor
   * @return SupervisorDTO
   */
  SupervisorDTO getOneSupervisorById(int id);

  /**
   * Get one supervisor by email.
   *
   * @param email the email of the supervisor
   * @return SupervisorDTO
   */
  SupervisorDTO getOneSupervisorByEmail(String email);

  /**
   * Add a supervisor to the database.
   *
   * @param supervisor the supervisor to add
   * @return SupervisorDTO
   */
  SupervisorDTO addSupervisor(SupervisorDTO supervisor);


  /**
   * Get supervisors by enterprise.
   *
   * @param idEnterprise the id of the enterprise
   * @return List of SupervisorDTO
   */
  List<SupervisorDTO> getSupervisorsByEnterprise(int idEnterprise);
}
