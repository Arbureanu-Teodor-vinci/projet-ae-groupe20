package be.vinci.pae.domain.internship;

/**
 * Interface of internshipUCC.
 */
public interface InternshipUCC {

  /**
   * Get one internship by student id.
   *
   * @param id the id of the student.
   * @return the internship.
   */
  InternshipDTO getOneInternshipByStudentId(int id);

  /**
   * Update the subject of an internship.
   *
   * @param internshipUpdated the internship to update.
   * @return the updated internship.
   */
  InternshipDTO updateSubject(InternshipDTO internshipUpdated);

  /**
   * Add an internship.
   *
   * @param internshipDTO the internship to add.
   * @return the added internship.
   */
  InternshipDTO addInternship(InternshipDTO internshipDTO);
}
