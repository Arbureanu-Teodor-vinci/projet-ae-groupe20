package be.vinci.pae.domain.enterprise;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Interface of enterprise for checking methods.
 */
@JsonDeserialize(as = EnterpriseImpl.class)
public interface Enterprise extends EnterpriseDTO {

  /**
  * Check if the enterprise is valid to be added.
  * 
  * @return boolean true if the enterprise is valid to be added.
  */
  boolean checkEnterpriseAdd();
}
