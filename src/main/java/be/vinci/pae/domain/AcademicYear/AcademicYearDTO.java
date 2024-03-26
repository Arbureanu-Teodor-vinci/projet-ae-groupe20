package be.vinci.pae.domain.AcademicYear;

/**
 * Interface of academic year with getters and setters.
 */
public interface AcademicYearDTO {

  /**
   * Get the id of the academic year.
   *
   * @return the id of the academic year.
   */
  int getId();

  /**
   * Set the id of the academic year.
   *
   * @param id the id of the academic year.
   */
  void setId(int id);

  /**
   * Get the year of the academic year.
   *
   * @return the year of the academic year.
   */
  String getYear();

  /**
   * Set the year of the academic year.
   *
   * @param year the year of the academic year.
   */
  void setYear(String year);


}
