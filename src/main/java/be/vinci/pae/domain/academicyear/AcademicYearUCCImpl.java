package be.vinci.pae.domain.academicyear;

import be.vinci.pae.services.academicyear.AcademicYearDAO;
import be.vinci.pae.services.dal.DALTransactionServices;
import jakarta.inject.Inject;
import java.time.LocalDate;

/**
 * AcademicYearUCCImpl class.
 */
public class AcademicYearUCCImpl implements AcademicYearUCC {

  @Inject
  AcademicYearDAO academicYearDAO;

  @Inject
  DALTransactionServices dalServices;

  @Override
  public AcademicYearDTO getOrAddActualAcademicYear() {

    //Get the last added academic year in DB
    AcademicYearDTO academicYearDTO = academicYearDAO.getActualAcademicYear();

    AcademicYear academicYear = (AcademicYear) academicYearDTO;
    //If the academic year is null or not actual
    if (academicYear == null || !academicYear.isActual()) {
      dalServices.startTransaction();

      academicYear = (AcademicYear) academicYearDAO.getAcademicYearByAcademicYear(
          getNewAcademicYear());
      academicYearDTO = academicYearDAO.addAcademicYear(getNewAcademicYear());
      academicYear.checkUniqueAcademicYear(academicYearDTO.getYear());

      dalServices.commitTransaction();
    }

    return academicYearDTO;

  }

  @Override
  public AcademicYearDTO getAcademicYearByYear(String year) {
    return academicYearDAO.getAcademicYearByAcademicYear(year);
  }

  @Override
  public String getNewAcademicYear() {
    //Get the actual date
    LocalDate date = LocalDate.now();
    int startYear;
    int endYear;

    if (date.getMonthValue() < 9) { //If the actual date is before September
      startYear = date.getYear() - 1; //The academic year start is the year before the actual year
      endYear = date.getYear();     //The academic year end is the actual year
    } else {   //If the actual date is after or in September
      startYear = date.getYear();  //The academic year start is the actual year
      endYear = date.getYear() + 1;  //The academic year end is the year after the actual year
    }

    return startYear + "-" + endYear; //Return the academic year
  }

}
