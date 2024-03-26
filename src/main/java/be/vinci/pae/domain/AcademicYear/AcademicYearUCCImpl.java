package be.vinci.pae.domain.AcademicYear;

import be.vinci.pae.api.filters.BiznessException;
import be.vinci.pae.services.AcademicYearServices.AcademicYearDAO;
import be.vinci.pae.services.DAL.DALTransactionServices;
import jakarta.inject.Inject;
import java.time.LocalDate;

public class AcademicYearUCCImpl implements AcademicYearUCC {

  @Inject
  AcademicYearDAO academicYearDAO;

  @Inject
  DALTransactionServices dalServices;

  public AcademicYearDTO getOrAddActualAcademicYear() {

    AcademicYearDTO academicYearDTO = academicYearDAO.getThisAcademicYear();
    AcademicYear academicYear = (AcademicYear) academicYearDTO;

    if (academicYear == null || !academicYear.isActual()) {
      dalServices.startTransaction();
      academicYear = (AcademicYear) academicYearDAO.getThisAcademicYearByAcademicYear(
          getNewAcademicYear());
      academicYearDTO = academicYearDAO.addThisNewAcademicYear(getNewAcademicYear());

      if (academicYear.checkUniqueAcademicYear(academicYearDTO.getYear())) {
        dalServices.rollbackTransaction();
        throw new BiznessException("Academic year already exists.");
      }

      dalServices.commitTransaction();
    }

    return academicYearDTO;

  }

  private String getNewAcademicYear() {
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
