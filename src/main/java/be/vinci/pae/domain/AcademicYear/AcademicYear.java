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

  boolean checkUniqueAcademicYear(String academicYear);

}
