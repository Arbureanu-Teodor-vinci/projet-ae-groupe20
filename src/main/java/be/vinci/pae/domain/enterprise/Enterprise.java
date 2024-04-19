package be.vinci.pae.domain.enterprise;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

/**
 * Interface of enterprise for checking methods.
 */
@JsonDeserialize(as = EnterpriseImpl.class)
public interface Enterprise extends EnterpriseDTO {

  /**
   * Check if the trade name already exists.
   *
   * @param tradeName      String
   * @param listEnterprise List of EnterpriseDTO
   * @return true if the trade name already exists
   */
  boolean checkTradeNameExists(String tradeName, List<EnterpriseDTO> listEnterprise);

  /**
   * Check if the designation already exists if the trade name already exists.
   *
   * @param designation    String
   * @param listEnterprise List of EnterpriseDTO
   * @return true if the designation already exists
   */
  boolean checkDesignationExists(String designation, List<EnterpriseDTO> listEnterprise);
}
