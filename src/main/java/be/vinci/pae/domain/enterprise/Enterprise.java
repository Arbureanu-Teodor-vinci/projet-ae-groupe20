package be.vinci.pae.domain.enterprise;

import be.vinci.pae.api.filters.BusinessException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

/**
 * Interface of enterprise for checking methods.
 */
@JsonDeserialize(as = EnterpriseImpl.class)
public interface Enterprise extends EnterpriseDTO {

  /**
   * Check if the designation already exists if the trade name already exists.
   *
   * @param tradeName      String
   * @param designation    String
   * @param listEnterprise List of EnterpriseDTO
   */
  void checkDesignationExistsOrIsNull(String tradeName, String designation,
      List<EnterpriseDTO> listEnterprise) throws BusinessException;
}
