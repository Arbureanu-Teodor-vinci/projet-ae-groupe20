package be.vinci.pae.domain.contact;

/**
 * Interface of contact for checking methods.
 */
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

  /**
   * Check if the interview method is updatable from state.
   *
   * @param interviewMethodBeforeUpdate String before the update
   * @return boolean
   */
  boolean checkInterviewMethodUpdate(String interviewMethodBeforeUpdate);


  /**
   * Check if the refusal reason is updatable from state.
   *
   * @return boolean
   */
  boolean checkContactRefusalReasonUpdate();

}
