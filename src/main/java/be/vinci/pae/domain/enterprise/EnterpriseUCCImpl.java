package be.vinci.pae.domain.enterprise;

import be.vinci.pae.services.dal.DALTransactionServices;
import be.vinci.pae.services.enterpriseservices.EnterpriseDAO;
import jakarta.inject.Inject;
import java.util.List;

/**
 * Implementation of enterpriseUCC.
 */
public class EnterpriseUCCImpl implements EnterpriseUCC {

  @Inject
  EnterpriseDAO enterpriseDS;

  @Inject
  private DALTransactionServices dalServices;

  @Override
  public EnterpriseDTO getOneEnterprise(int id) {
    if (id < 0) {
      return null;
    }

    return enterpriseDS.getOneEnterpriseByid(id);
  }

  @Override
  public List<EnterpriseDTO> getAllEnterprises() {
    return enterpriseDS.getAllEnterprises();
  }

  @Override
  public EnterpriseDTO addEnterprise(EnterpriseDTO enterpriseDTO) {
    EnterpriseDTO enterprise;
    try {
      dalServices.startTransaction();
      List<EnterpriseDTO> enterprisesExisting = enterpriseDS.getAllEnterprises();
      Enterprise enterpriseCast = (Enterprise) enterpriseDTO;
      enterpriseCast.checkDesignationExistsOrIsNull(enterpriseDTO.getTradeName(),
          enterpriseDTO.getDesignation(),
          enterprisesExisting);
      enterprise = enterpriseDS.addEnterprise(enterpriseDTO);
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    dalServices.commitTransaction();
    return enterprise;
  }

}
