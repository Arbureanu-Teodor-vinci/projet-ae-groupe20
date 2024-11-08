package be.vinci.pae.services.academicyear;

import be.vinci.pae.domain.academicyear.AcademicYearDTO;
import java.util.List;

/**
 * Interface of academic year with Business methods.
 */
public interface AcademicYearDAO {

  /**
   * Get all academic years from DB.
   *
   * @return list of academic years
   */
  List<String> getAllAcademicYears();

  /**
   * Get the actual academic year.
   *
   * @return the actual academic year.
   */
  AcademicYearDTO getActualAcademicYear();

  /**
   * Add the new actual academic year.
   *
   * @param academicYear the new academic year.
   * @return the new academic year.
   */
  AcademicYearDTO addAcademicYear(String academicYear);

  /**
   * Get an academic year by its academic year.
   *
   * @param academicYear the academic year to search.
   * @return an academic year.
   */
  AcademicYearDTO getAcademicYearByAcademicYear(String academicYear);


}
