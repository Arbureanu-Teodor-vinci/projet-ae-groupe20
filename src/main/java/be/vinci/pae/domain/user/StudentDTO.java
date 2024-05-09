package be.vinci.pae.domain.user;

import be.vinci.pae.domain.academicyear.AcademicYearDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Interface of studentDTO.
 */
@JsonDeserialize(as = StudentImpl.class)
public interface StudentDTO extends UserDTO {


  /**
   * Get academic year.
   *
   * @return academic year.
   */
  AcademicYearDTO getStudentAcademicYear();

  /**
   * Set academic year.
   *
   * @param academicYear academic year.
   */
  void setAcademicYear(AcademicYearDTO academicYear);

  /**
   * Get id of student.
   *
   * @return id of student.
   */
  int getId();

  /**
   * Set id of student.
   *
   * @param id id of student.
   */
  void setId(int id);

}
