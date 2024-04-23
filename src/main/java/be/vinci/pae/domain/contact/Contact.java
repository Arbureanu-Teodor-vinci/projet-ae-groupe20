package be.vinci.pae.domain.contact;

import be.vinci.pae.api.filters.BusinessException;
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
  void checkContactStateUpdate(String state) throws BusinessException;

  /**
   * Check if the state is valid.
   */
  void checkContactState() throws BusinessException;

  /**
   * Check if the interview method is updatable from state.
   *
   * @param interviewMethodBeforeUpdate String before the update
   */
  void checkInterviewMethodUpdate(String interviewMethodBeforeUpdate) throws BusinessException;

  /**
   * check if tool is valid for interview method.
   */
  void checkContactToolUpdate() throws BusinessException;

  /**
   * Check if the refusal reason is updatable from state.
   */
  void checkContactRefusalReasonUpdate() throws BusinessException;

  /**
   * Check if the contact is taken.
   */
  void checkIfContactIsAccepted() throws BusinessException;
}
