package be.vinci.pae.domain.contact;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Interface of contact for checking methods.
 */
@JsonDeserialize(as = ContactImpl.class)
public interface Contact extends ContactDTO {

  /**
   * Check if the state is valid to update.
   *
   * @param state String
   */
  boolean checkContactStateUpdate(String state);

  /**
   * Check if the state is valid.
   *
   * @return boolean
   */
  boolean checkContactState();

}
