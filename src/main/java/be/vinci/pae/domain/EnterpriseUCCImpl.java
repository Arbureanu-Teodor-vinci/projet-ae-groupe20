package be.vinci.pae.domain;

import be.vinci.pae.services.EnterpriseDAO;
import jakarta.inject.Inject;

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

}
