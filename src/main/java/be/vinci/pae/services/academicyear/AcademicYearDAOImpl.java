package be.vinci.pae.services.academicyear;

import be.vinci.pae.api.filters.FatalException;
import be.vinci.pae.domain.academicyear.AcademicYearDTO;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.services.dal.DALServices;
import be.vinci.pae.utils.Logger;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * AcademicYearDAOImpl class.
 */
public class AcademicYearDAOImpl implements AcademicYearDAO {

  @Inject
  private DomainFactory domainFactory;
  @Inject
  private DALServices dalConn;

  @Override
  public List<String> getAllAcademicYears() {
    Logger.logEntry("AcademicYear DAO - getAcademicYears");
    List<String> academicYears = new ArrayList<>();
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT academic_year FROM InternshipManagement.academic_years");
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          academicYears.add(rs.getString(1));
        }
      }
      ps.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }

    return academicYears;
  }

  @Override
  public AcademicYearDTO getActualAcademicYear() {
    Logger.logEntry("AcademicYearDAOImpl - getActualAcademicYear");
    AcademicYearDTO academicYearDTO = domainFactory.getAcademicYearDTO();
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT * FROM InternshipManagement.academic_years"
              + " ORDER BY id_academic_year DESC LIMIT 1");
      try (ResultSet resultSet = ps.executeQuery()) {
        if (resultSet.next()) {
          academicYearDTO.setId(resultSet.getInt("id_academic_year"));
          academicYearDTO.setYear(resultSet.getString("academic_year"));
        }
      }
      ps.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return academicYearDTO;
  }

  @Override
  public AcademicYearDTO getAcademicYearByAcademicYear(String academicYear) {
    Logger.logEntry("AcademicYearDAOImpl - getAcademicYearByAcademicYear" + academicYear);
    AcademicYearDTO academicYearDTO = domainFactory.getAcademicYearDTO();
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT * FROM InternshipManagement.academic_years WHERE academic_year = ?");
      ps.setString(1, academicYear);
      try (ResultSet resultSet = ps.executeQuery()) {
        if (resultSet.next()) {
          academicYearDTO.setId(resultSet.getInt("id_academic_year"));
          academicYearDTO.setYear(resultSet.getString("academic_year"));
        }
      }
      ps.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return academicYearDTO;
  }

  @Override
  public AcademicYearDTO addAcademicYear(String academicYear) {
    Logger.logEntry("AcademicYearDAOImpl - addAcademicYear" + academicYear);
    AcademicYearDTO academicYearDTO = domainFactory.getAcademicYearDTO();
    try {
      PreparedStatement ps = dalConn.getPS(
          "INSERT INTO InternshipManagement.academic_years (academic_year)"
              + " VALUES (?) RETURNING id_academic_year, academic_year;");

      ps.setString(1, academicYear);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          academicYearDTO.setId(rs.getInt("id_academic_year"));
          academicYearDTO.setYear(rs.getString("academic_year"));
        }
      }
      ps.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return academicYearDTO;
  }


}
