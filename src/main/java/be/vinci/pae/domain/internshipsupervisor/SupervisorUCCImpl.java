package be.vinci.pae.domain.internshipsupervisor;

import be.vinci.pae.api.filters.BiznessException;
import be.vinci.pae.services.dal.DALTransactionServices;
import be.vinci.pae.services.internshipsupervisorservices.SupervisorDAO;
import jakarta.inject.Inject;
import java.util.List;

/**
 * Implementation of SupervisorUCC.
 */
public class SupervisorUCCImpl implements SupervisorUCC {

  @Inject
  SupervisorDAO supervisorDS;

  @Inject
  DALTransactionServices dalServices;


  @Override
  public List<SupervisorDTO> getAllInternshipSupervisors() {
    return supervisorDS.getAllSupervisors();
  }

  @Override
  public SupervisorDTO getOneSupervisorById(int id) {
    if (id <= 0) {
      throw new BiznessException("id must be positive");
    }
    return supervisorDS.getOneSupervisorById(id);
  }

  @Override
  public SupervisorDTO addSupervisor(SupervisorDTO newSsupervisor) {
    Supervisor supervisor = (Supervisor) newSsupervisor;

    dalServices.startTransaction(); // START TRANSACTION
    // check if email exists already
    SupervisorDTO supervisorDTO = supervisorDS.getOneSupervisorByEmail(
        newSsupervisor.getEmail());
    if (!supervisor.checkUniqueEmail(supervisorDTO)) {
      dalServices.rollbackTransaction(); // ROLLBACK TRANSACTION
      return null;
    }

    SupervisorDTO finalSupervisor = supervisorDS.addSupervisor(newSsupervisor);
    if (finalSupervisor == null) {
      dalServices.rollbackTransaction(); // ROLLBACK TRANSACTION
      return null;
    }
    dalServices.commitTransaction(); // COMMIT TRANSACTION
    return finalSupervisor;
  }

}
