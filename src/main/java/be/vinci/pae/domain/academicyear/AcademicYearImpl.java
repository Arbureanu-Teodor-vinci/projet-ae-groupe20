package be.vinci.pae.domain.academicyear;

import be.vinci.pae.api.filters.BusinessException;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Class that represents an academic year.
 */
public class AcademicYearImpl implements AcademicYear {

  private int id;
  private String year;

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getYear() {
    return year;
  }

  @Override
  public void setYear(String year) {
    this.year = year;
  }


  @Override
  public boolean isActual() {
    LocalDate date = LocalDate.now();
    //Split the year to get the end year
    String[] years = this.year.split("-");
    int startYear = Integer.parseInt(years[0]);
    int endYear = Integer.parseInt(years[1]);
    //Check if the year is the actual academic year
    if (date.getYear() > endYear || date.getYear()
        < startYear) { //If the actual year is not in the years of the academic year
      return false;
    } else if (date.getYear() == endYear) {  //If the actual year is the end year
      if (date.getMonthValue() >= 9) { //After September, the academic year is not the actual one
        return false;
      } else {
        return true;
      }
    } else if (date.getYear() == startYear) { //IF the actual year is the start year
      if (date.getMonthValue() < 9) { //Before September, the academic year is not the actual one
        return false;
      } else {
        return true;
      }

    } else {
      return true;
    }
  }

  @Override
  public void checkUniqueAcademicYear(String academicYear) {
    //Check if the academic year is unique
    if (academicYear.equals(this.year)) {
      throw new BusinessException("Academic year already exists.");
    }
  }

  @Override
  public boolean checkAcademicYear(String academicYear) {
    //Check if the academic year is a valid format
    return academicYear.matches("\\d{4}-\\d{4}");
  }

  @Override
  public String toString() {
    return "[id=" + id + ", year=" + year + "]";
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AcademicYearImpl that = (AcademicYearImpl) o;
    return id == that.id && Objects.equals(year, that.year);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, year);
  }
}
