package be.vinci.pae.services.AcademicYearServices;

import be.vinci.pae.api.filters.FatalException;
import be.vinci.pae.domain.AcademicYear.AcademicYearDTO;
import be.vinci.pae.domain.Factory.DomainFactory;
import be.vinci.pae.services.DAL.DALServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * AcademicYearDAOImpl class.
 */
public class AcademicYearDAOImpl implements AcademicYearDAO {

  @Inject
  private DomainFactory domainFactory;
  @Inject
  private DALServices dalConn;

  @Override
  public AcademicYearDTO getThisAcademicYear() {
    AcademicYearDTO academicYearDTO = domainFactory.getAcademicYearDTO();
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT * FROM InternshipManagement.academic_year ORDER BY id_academic_year DESC LIMIT 1");
      try (ResultSet resultSet = ps.executeQuery()) {
        if (resultSet.next()) {
          academicYearDTO.setId(resultSet.getInt("id_academic_year"));
          academicYearDTO.setYear(resultSet.getString("academic_year"));
        }
      }
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e);
    }
    return academicYearDTO;
  }

  @Override
  public AcademicYearDTO getThisAcademicYearByAcademicYear(String academicYear) {
    AcademicYearDTO academicYearDTO = domainFactory.getAcademicYearDTO();
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT * FROM InternshipManagement.academic_year WHERE academic_year = ?");
      ps.setString(1, academicYear);
      try (ResultSet resultSet = ps.executeQuery()) {
        if (resultSet.next()) {
          academicYearDTO.setId(resultSet.getInt("id_academic_year"));
          academicYearDTO.setYear(resultSet.getString("academic_year"));
        }
      }
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e);
    }
    return academicYearDTO;
  }

  @Override
  public AcademicYearDTO addThisNewAcademicYear(String academicYear) {
    AcademicYearDTO academicYearDTO = domainFactory.getAcademicYearDTO();
    try {
      PreparedStatement ps = dalConn.getPS(
          "INSERT INTO InternshipManagement.academic_year (academic_year) VALUES (?) RETURNING id_academic_year, academic_year;");

      ps.setString(1, academicYear);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          academicYearDTO.setId(rs.getInt("id_academic_year"));
          academicYearDTO.setYear(rs.getString("academic_year"));
        }
      }
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e);
    }
    return academicYearDTO;
  }


}