package be.vinci.pae.domain;

import be.vinci.pae.api.filters.BiznessException;
import be.vinci.pae.services.ContactDAO;
import jakarta.inject.Inject;
import java.util.List;

public class ContactUCCImpl implements ContactUCC {

  @Inject
  ContactDAO contactDS;

  @Override
  public ContactDTO getOneContact(int id) {
    if (id < 0) {
      throw new BiznessException("id must be positive");
    }

    return contactDS.getOneContactByid(id);
  }

  @Override
  public List<ContactDTO> getAllContacts() {
    return contactDS.getAllContacts();
  }

  @Override
  public List<ContactDTO> getContactsByUser(int id) {
    return contactDS.getContactsByUser(id);
  }
  
}
