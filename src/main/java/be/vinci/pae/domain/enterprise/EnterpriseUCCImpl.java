package be.vinci.pae.domain.enterprise;

import be.vinci.pae.api.filters.BiznessException;
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
    dalServices.startTransaction();
    Enterprise enterprise = (Enterprise) enterpriseDTO;
    List<EnterpriseDTO> enterprisesExisting = enterpriseDS.getAllEnterprises();
    if (enterprise.checkTradeNameExists(enterprise.getTradeName(), enterprisesExisting)) {
      dalServices.rollbackTransaction();
      throw new BiznessException("Trade name already exists");
    }


  }

}
