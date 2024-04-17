package be.vinci.pae.domain.internshipsupervisor;

/**
 * Interface of Supervisor for business checks.
 */
public interface Supervisor extends SupervisorDTO {

  boolean checkUniqueEmail(SupervisorDTO supervisorDTO);


}
