package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.api.filters.BusinessException;
import be.vinci.pae.domain.academicyear.AcademicYear;
import be.vinci.pae.domain.academicyear.AcademicYearDTO;
import be.vinci.pae.domain.academicyear.AcademicYearUCC;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.services.academicyear.AcademicYearDAO;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * AcademicYearUCC test class.
 */
public class AcademicYearUCCTest {

  ServiceLocator locator = ServiceLocatorUtilities.bind(new TestsApplicationBinder());
  private AcademicYearUCC academicYearUCC = locator.getService(AcademicYearUCC.class);
  private AcademicYearDAO academicYearDAO = locator.getService(AcademicYearDAO.class);
  private DomainFactory domainFactory = locator.getService(DomainFactory.class);
  private AcademicYearDTO academicYearDTO = domainFactory.getAcademicYearDTO();

  /**
   * Initialize academicYearDTO before each test.
   */
  @BeforeEach
  public void setUp() {
    academicYearDTO.setId(1);
    academicYearDTO.setYear("2023-2024");
    AcademicYear academicYear = (AcademicYear) academicYearDTO;
    //Mockito.when(academicYear.isActual()).thenReturn(true);
  }

  @Test
  @DisplayName("Get academic year if it is actual.")
  public void testgetOrAddActualAcademicYear() {
    Mockito.when(academicYearDAO.getActualAcademicYear()).thenReturn(academicYearDTO);
    assertEquals(academicYearDTO, academicYearUCC.getOrAddActualAcademicYear());
  }


  @Test
  @DisplayName("Get or add actual academic year when academic year is not actual")
  void getOrAddActualAcademicYearWhenNotActual() {
    AcademicYearDTO academicYearDTONotValid = domainFactory.getAcademicYearDTO();
    academicYearDTO.setYear("2022-2023");

    Mockito.when(academicYearDAO.getActualAcademicYear()).thenReturn(academicYearDTO);

    Mockito.when(academicYearDAO.addAcademicYear(Mockito.anyString())).thenReturn(academicYearDTO);

    AcademicYearDTO actualAcademicYearDTO = academicYearUCC.getOrAddActualAcademicYear();

    assertEquals(academicYearDTO, actualAcademicYearDTO);
  }

  @Test
  @DisplayName("Get academic year by valid year")
  void getAcademicYearByValidYear() {
    String year = "2023-2024";
    AcademicYearDTO expectedAcademicYearDTO = domainFactory.getAcademicYearDTO();
    expectedAcademicYearDTO.setYear(year);

    Mockito.when(academicYearDAO.getAcademicYearByAcademicYear(year))
        .thenReturn(expectedAcademicYearDTO);

    AcademicYearDTO actualAcademicYearDTO = academicYearUCC.getAcademicYearByYear(year);

    assertEquals(expectedAcademicYearDTO, actualAcademicYearDTO);
  }

  @Test
  @DisplayName("Get academic year by null year")
  void getAcademicYearByNullYear() {
    String year = null;

    assertThrows(BusinessException.class, () -> academicYearUCC.getAcademicYearByYear(year));
  }

  @Test
  @DisplayName("Get academic year by empty year")
  void getAcademicYearByEmptyYear() {
    String year = "";

    assertThrows(BusinessException.class, () -> academicYearUCC.getAcademicYearByYear(year));
  }

  @Test
  @DisplayName("Get academic year by invalid year format")
  void getAcademicYearByInvalidYearFormat() {
    String year = "2023";

    assertThrows(BusinessException.class, () -> academicYearUCC.getAcademicYearByYear(year));
  }


}
