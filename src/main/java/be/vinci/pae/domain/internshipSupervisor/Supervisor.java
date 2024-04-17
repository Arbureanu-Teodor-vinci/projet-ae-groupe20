package be.vinci.pae.domain.internshipSupervisor;

/**
 * Interface of Supervisor for business checks.
 */
public interface Supervisor extends SupervisorDTO {

  boolean checkUniqueEmail(SupervisorDTO supervisorDTO);


}
