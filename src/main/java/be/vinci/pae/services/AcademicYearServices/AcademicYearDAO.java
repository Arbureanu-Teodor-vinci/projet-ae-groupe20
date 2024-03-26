package be.vinci.pae.services.AcademicYearServices;

import be.vinci.pae.domain.AcademicYear.AcademicYearDTO;

/**
 * Interface of academic year with Business methods.
 */
public interface AcademicYearDAO {

  /**
   * Get the actual academic year.
   *
   * @return the actual academic year.
   */
  AcademicYearDTO getThisAcademicYear();

  /**
   * Add the new actual academic year.
   *
   * @param academicYear the new academic year.
   * @return the new academic year.
   */
  AcademicYearDTO addThisNewAcademicYear(String academicYear);

  AcademicYearDTO getThisAcademicYearByAcademicYear(String academicYear);


}
