package be.vinci.pae.domain.enterprise;

import be.vinci.pae.services.enterpriseservices.EnterpriseDAO;
import jakarta.inject.Inject;
import java.util.List;

/**
 * Implementation of enterpriseUCC.
 */
public class EnterpriseUCCImpl implements EnterpriseUCC {

  @Inject
  EnterpriseDAO enterpriseDS;

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
    return enterpriseDS.getNbInternships(id);
  }

  @Override
  public EnterpriseDTO addEnterprise(EnterpriseDTO enterprise) {
    return enterpriseDS.addEnterprise(enterprise);
  }

}
