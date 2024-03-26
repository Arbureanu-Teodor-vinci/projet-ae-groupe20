package be.vinci.pae.domain.AcademicYear;

/**
 * Interface of academic year with Business methods.
 */
public interface AcademicYear extends AcademicYearDTO {

  /**
   * Check if the academic year is the actual academic year.
   *
   * @return true if the academic year is the actual academic year.
   */
  boolean isActual();

  /**
   * Check if the academic year is unique.
   *
   * @param academicYear the academic year to check.
   * @return true if the academic year to check is not unique.
   */
  boolean checkUniqueAcademicYear(String academicYear);

  /**
   * Check if the academic year is a valid format.
   *
   * @param academicYear the academic year to check.
   * @return true if the academic year is valid.
   */
  boolean checkAcademicYear(String academicYear);

}
