package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;

import be.vinci.pae.domain.AcademicYear.AcademicYear;
import be.vinci.pae.domain.AcademicYear.AcademicYearDTO;
import be.vinci.pae.domain.AcademicYear.AcademicYearUCC;
import be.vinci.pae.domain.Factory.DomainFactory;
import be.vinci.pae.services.AcademicYearServices.AcademicYearDAO;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AcademicYearUCCTest {

  ServiceLocator locator = ServiceLocatorUtilities.bind(new TestsApplicationBinder());
  private AcademicYearUCC academicYearUCC = locator.getService(AcademicYearUCC.class);
  private AcademicYearDAO academicYearDAO = locator.getService(AcademicYearDAO.class);
  private DomainFactory domainFactory = locator.getService(DomainFactory.class);
  private AcademicYearDTO academicYearDTO = domainFactory.getAcademicYearDTO();

  @BeforeEach
  public void setUp() {
    academicYearDTO.setId(1);
    academicYearDTO.setYear("2023-2024");
    AcademicYear academicYear = (AcademicYear) academicYearDTO;
    //Mockito.when(academicYear.isActual()).thenReturn(true);
    Mockito.when(academicYearDAO.getActualAcademicYear())
        .thenReturn(academicYearDTO);
  }

  @Test
  @DisplayName("Get academic year if it is actual.")
  public void testgetOrAddActualAcademicYear() {
    Mockito.when(academicYearDAO.getActualAcademicYear()).thenReturn(academicYearDTO);
    assertEquals(academicYearDTO, academicYearUCC.getOrAddActualAcademicYear());
  }

}