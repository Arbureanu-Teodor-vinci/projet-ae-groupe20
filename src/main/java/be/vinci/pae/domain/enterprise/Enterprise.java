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

  /**
   * Check if the enterprise is blacklisted.
   *
   * @throws BusinessException if the enterprise is blacklisted.
   */
  void checkIsBlackListed() throws BusinessException;

  /**
   * Check if the enterprise is blacklisted and has a motivation.
   *
   * @throws BusinessException if the enterprise is blacklisted and has no motivation.
   */
  void checkBlackListMotivation() throws BusinessException;

  /**
   * Check if the enterprise can be updated to blacklisted.
   *
   * @param enterprise EnterpriseDTO
   * @throws BusinessException if other data are changed than the blacklisted status and the
   *                           motivation.
   */
  void checkUpdateBlacklist(EnterpriseDTO enterprise) throws BusinessException;
}
