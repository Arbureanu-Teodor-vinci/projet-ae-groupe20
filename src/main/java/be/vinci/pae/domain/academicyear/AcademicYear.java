package be.vinci.pae.domain.academicyear;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

/**
 * Interface of academic year with Business methods.
 */
@JsonDeserialize(as = AcademicYearImpl.class)
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
   */
  void checkUniqueAcademicYear(String academicYear);

  /**
   * Check if the academic year is a valid format.
   *
   * @param academicYear the academic year to check.
   * @return true if the academic year is valid.
   */
  boolean checkAcademicYearValue(String academicYear);

  /**
   * Check if the academic year is valid.
   *
   * @param academicYear -> academic year to check
   */
  void checkAcademicYear(String academicYear, List<String> academicYears);
}
