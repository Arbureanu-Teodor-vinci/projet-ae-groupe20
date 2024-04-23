package be.vinci.pae.domain.academicyear;

import java.util.List;

/**
 * Interface of academic year with Business methods.
 */
public interface AcademicYearUCC {

  /**
   * Get all academic years.
   *
   * @return list of academic years.
   */
  List<String> getAllAcademicYears();

  /**
   * Get the actual academic year.
   *
   * @return the actual academic year.
   */
  AcademicYearDTO getOrAddActualAcademicYear();

  /**
   * Get the academic year by year.
   *
   * @param year -> year to get
   * @return the academic year.
   */
  AcademicYearDTO getAcademicYearByYear(String year);

  /**
   * Get the new academic year from the actual date.
   *
   * @return the new academic year.
   */
  String getNewAcademicYear();

}
