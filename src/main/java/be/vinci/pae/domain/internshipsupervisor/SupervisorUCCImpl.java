package be.vinci.pae.domain.internshipsupervisor;

import be.vinci.pae.api.filters.BusinessException;
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
      throw new BusinessException("id must be positive");
    }
    return supervisorDS.getOneSupervisorById(id);
  }

  @Override
  public List<SupervisorDTO> getSupervisorsByEnterprise(int idEnterprise) {
    if (idEnterprise <= 0) {
      throw new BusinessException("id must be positive");
    }
    return supervisorDS.getSupervisorsByEnterprise(idEnterprise);
  }

  @Override
  public SupervisorDTO addSupervisor(SupervisorDTO newSupervisor) {
    Supervisor supervisor = (Supervisor) newSupervisor;
    try {
      supervisor.checkPhoneNumberFormat(newSsupervisor.getPhoneNumber());
      supervisor.checkNamesFormat(newSsupervisor.getFirstName());
      supervisor.checkNamesFormat(newSsupervisor.getLastName());
      supervisor.checkEmailFormat(newSsupervisor.getEmail());
      dalServices.startTransaction(); // START TRANSACTION
      // check if email exists already
      SupervisorDTO supervisorDTO = supervisorDS.getOneSupervisorByEmail(
          newSupervisor.getEmail());
      supervisor.checkUniqueEmail(supervisorDTO);
      newSupervisor = supervisorDS.addSupervisor(newSupervisor);
    } catch (Throwable e) {
      dalServices.rollbackTransaction(); // ROLLBACK TRANSACTION
      throw e;
    }
    dalServices.commitTransaction(); // COMMIT TRANSACTION
    return newSupervisor;
  }

}
