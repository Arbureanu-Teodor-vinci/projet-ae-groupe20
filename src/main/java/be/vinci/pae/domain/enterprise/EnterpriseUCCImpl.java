package be.vinci.pae.domain.enterprise;

import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.services.dal.DALTransactionServices;
import be.vinci.pae.services.enterpriseservices.EnterpriseDAO;
import jakarta.inject.Inject;
import java.util.List;

/**
 * Implementation of enterpriseUCC.
 */
public class EnterpriseUCCImpl implements EnterpriseUCC {

  @Inject
  private EnterpriseDAO enterpriseDS;

  @Inject
  private DALTransactionServices dalServices;
  @Inject
  private DomainFactory domainFactory;

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
  public int getNbInternships(int id) {
    if (id < 0) {
      return -1;
    }
    return enterpriseDS.getNbInternships(id);
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
      enterpriseCast.checkEmailFormat(enterpriseDTO.getEmail());
      enterpriseCast.checkPhoneNumberFormat(enterpriseDTO.getPhoneNumber());
      enterprise = enterpriseDS.addEnterprise(enterpriseDTO);
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    dalServices.commitTransaction();
    return enterprise;
  }

  @Override
  public EnterpriseDTO blacklistEnterprise(EnterpriseDTO enterprise) {
    Enterprise enterpriseUpdated = (Enterprise) enterprise;
    try {
      dalServices.startTransaction();
      Enterprise enterpriseFound = (Enterprise) enterpriseDS.getOneEnterpriseByid(
          enterprise.getId());
      enterpriseFound.checkIsNull();
      enterpriseFound.checkIsBlackListed();
      enterpriseUpdated.checkBlackListMotivation();
      enterprise = enterpriseDS.updateEnterprise(enterprise);
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    dalServices.commitTransaction();
    return enterprise;
  }

}
