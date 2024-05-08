package be.vinci.pae.domain.internship;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Interface of internship for checking methods.
 */
@JsonDeserialize(as = InternshipImpl.class)
public interface Internship extends InternshipDTO {

  /**
   * Check if only the subject of the internship has been updated.
   *
   * @param previousInternship the previous internship to compare with.
   */
  void checkOnlySubjectUpdated(InternshipDTO previousInternship);

}
