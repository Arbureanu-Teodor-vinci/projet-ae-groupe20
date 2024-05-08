package be.vinci.pae.services.internshipservices;

import be.vinci.pae.domain.internship.InternshipDTO;

/**
 * Service of internship.
 */
public interface InternshipDAO {

  /**
   * Get one internship by id.
   *
   * @param id the id of the internship.
   * @return the internship.
   */
  InternshipDTO getOneInternshipById(int id);

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
   * @param internshipToUpdate the internship to update.
   * @return the updated internship.
   */
  InternshipDTO updateSubject(InternshipDTO internshipToUpdate);

  /**
   * Add an internship.
   *
   * @param internshipDTO the internship to add.
   * @return the added internship.
   */
  InternshipDTO addInternship(InternshipDTO internshipDTO);

  /**
   * Get the number of interships in an enterprise.
   *
   * @param id int
   * @return int
   */
  int getNbInternships(int id);

  /**
   * Get the number of interships in an enterprise for a specific academic year.
   *
   * @param id           integer
   * @param academicYear String
   * @return integer
   */
  int getNbInternshipsPerAcademicYear(int id, String academicYear);
}
