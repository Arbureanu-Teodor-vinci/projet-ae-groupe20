package be.vinci.pae.services.userservices;

import be.vinci.pae.api.filters.FatalException;
import be.vinci.pae.domain.academicyear.AcademicYearDTO;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.domain.user.StudentDTO;
import be.vinci.pae.services.dal.DALServices;
import be.vinci.pae.utils.Logger;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of student services.
 */
public class StudentDAOImpl implements StudentDAO {

  @Inject
  private DomainFactory domainFactory;
  //using the DALService to establish a connection to the database
  @Inject
  private DALServices dalConn;

  @Override
  public StudentDTO getStudentById(int id) {
    Logger.logEntry("Student DAO - getStudentById" + id);
    StudentDTO studentDTO = domainFactory.getStudentDTO();
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT s.id_user,ay.id_academic_year, ay.academic_year"
              + " FROM InternshipManagement.student s, InternshipManagement.academic_year ay\n"
              + " WHERE s.academic_year = ay.id_academic_year\n"
              + " AND s.id_user = ?"
      );
      ps.setInt(1, id);
      try (ResultSet resultSet = ps.executeQuery()) {
        if (resultSet.next()) {
          studentDTO = getResultSet(resultSet);
        }
      }
      ps.close();
    } catch (SQLException e) {
      Logger.logEntry("Error in StudentDAOImpl getStudentById" + e);
      e.printStackTrace();
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return studentDTO;
  }

  @Override
  public StudentDTO addStudent(StudentDTO student) {
    Logger.logEntry("Student DAO - addStudent" + student);
    try {
      PreparedStatement ps = dalConn.getPS(
          "INSERT INTO InternshipManagement.student (id_user, academic_year) VALUES (?, ?)"
              + " RETURNING id_user, academic_year, "
              + " (SELECT academic_year FROM InternshipManagement.academic_year "
              + " WHERE id_academic_year = ?)"
      );
      ps.setInt(1, student.getId());
      ps.setInt(2, student.getStudentAcademicYear().getId());
      ps.setInt(3, student.getStudentAcademicYear().getId());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          student = getResultSet(rs);
        }
      }
      ps.close();
    } catch (SQLException e) {
      Logger.logEntry("Error in StudentDAOImpl addStudent" + e);
      e.printStackTrace();
      throw new FatalException(e);
    } finally {
      dalConn.closeConnection();
    }
    return student;
  }

  private StudentDTO getResultSet(ResultSet rs) throws SQLException {
    AcademicYearDTO academicYearDTO = domainFactory.getAcademicYearDTO();
    StudentDTO student = domainFactory.getStudentDTO();
    student.setId(rs.getInt(1));
    academicYearDTO.setId(rs.getInt(2));
    academicYearDTO.setYear(rs.getString(3));
    student.setAcademicYear(academicYearDTO);
    return student;
  }

}
