package be.vinci.pae.domain.Enterprise;

import be.vinci.pae.services.EnterpriseServices.EnterpriseDAO;
import jakarta.inject.Inject;
import java.util.List;

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

}
