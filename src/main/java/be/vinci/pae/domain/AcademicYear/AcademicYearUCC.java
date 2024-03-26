package be.vinci.pae.domain.AcademicYear;

/**
 * Interface of academic year with Business methods.
 */
public interface AcademicYearUCC {

  /**
   * Get the actual academic year.
   *
   * @return the actual academic year.
   */
  AcademicYearDTO getOrAddActualAcademicYear();


}
