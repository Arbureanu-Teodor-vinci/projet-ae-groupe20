package be.vinci.pae.domain.internshipsupervisor;

import be.vinci.pae.api.filters.BusinessException;
import be.vinci.pae.domain.contact.ContactDTO;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.services.contactservices.ContactDAO;
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
  ContactDAO contactDS;

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
  public SupervisorDTO addSupervisor(SupervisorDTO supervisorDTO, UserDTO user) {
    ContactDTO contactDTO = null;
    if (user.getRole().equals("Etudiant")) {
      List<ContactDTO> contacts = contactDS.getContactsByUser(user.getId());
      for (ContactDTO contact : contacts) {
        if (contact.getEnterprise().getId() == supervisorDTO.getEnterprise().getId()) {
          contactDTO = contact;
          break;
        }
      }
      if (contactDTO == null) {
        throw new BusinessException("You don't have a contact with this enterprise");
      }
      if (!contactDTO.getStateContact().equals("accepté")) {
        throw new BusinessException("The student must be accepted by the enterprise");
      }
    }
    try {
      dalServices.startTransaction(); // START TRANSACTION
      Supervisor supervisorCast = (Supervisor) supervisorDTO;
      supervisorCast.checkPhoneNumberFormat(supervisorDTO.getPhoneNumber());
      supervisorCast.checkNamesFormat(supervisorDTO.getFirstName());
      supervisorCast.checkNamesFormat(supervisorDTO.getLastName());
      supervisorCast.checkEmailFormat(supervisorDTO.getEmail());
      // check if email exists already
      SupervisorDTO supervisorCheckEmail = supervisorDS.getOneSupervisorByEmail(
          supervisorDTO.getEmail());
      supervisorCast.checkUniqueEmail(supervisorCheckEmail);
      supervisorDTO = supervisorDS.addSupervisor(supervisorDTO);
    } catch (Throwable e) {
      dalServices.rollbackTransaction(); // ROLLBACK TRANSACTION
      throw e;
    }
    dalServices.commitTransaction(); // COMMIT TRANSACTION
    return supervisorDTO;
  }

}
