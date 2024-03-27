package be.vinci.pae.services.contactservices;

import be.vinci.pae.domain.contact.ContactDTO;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.services.dal.DALServices;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of contact services.
 */
public class ContactDAOImpl implements ContactDAO {

  @Inject
  private DomainFactory domainFactory;

  @Inject
  private DALServices dalConn;

  @Override
  public ContactDTO getOneContactByid(int id) {
    ContactDTO contact = domainFactory.getContactDTO();

    try {

      PreparedStatement ps = dalConn.getPS(
          "SELECT * FROM InternshipManagement.contacts WHERE id_contacts = ?"
      );
      ps.setInt(1, id);

      try (ResultSet resultSet = ps.executeQuery()) {
        if (resultSet.next()) {
          contact = getResultSet(resultSet);
        }

      }
      ps.close();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return contact;
  }

  @Override
  public List<ContactDTO> getAllContacts() {
    List<ContactDTO> contacts = new ArrayList<>();
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT * FROM InternshipManagement.contacts"
      );

      try (ResultSet resultSet = ps.executeQuery()) {
        while (resultSet.next()) {
          contacts.add(getResultSet(resultSet));
        }
      }
      ps.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return contacts;
  }

  @Override
  public List<ContactDTO> getContactsByUser(int id) {
    List<ContactDTO> contacts = new ArrayList<>();
    try {
      PreparedStatement ps = dalConn.getPS(
          "SELECT * FROM InternshipManagement.contacts WHERE student = ?"
      );
      ps.setInt(1, id);

      try (ResultSet resultSet = ps.executeQuery()) {
        while (resultSet.next()) {
          contacts.add(getResultSet(resultSet));
        }
      }
      ps.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return contacts;
  }

  /**
   * Get the result set.
   *
   * @param resultSet ResultSet
   * @return ContactDTO
   * @throws SQLException SQLException
   */
  private ContactDTO getResultSet(ResultSet resultSet) throws SQLException {
    // creating a new contactDTO to stock the information
    ContactDTO contact = domainFactory.getContactDTO();
    // using the result set, setting the attribut of the contact
    contact.setId(resultSet.getInt(1));
    contact.setInterviewMethod(resultSet.getString(2));
    contact.setTool(resultSet.getString(3));
    contact.setRefusalReason(resultSet.getString(4));
    contact.setStateContact(resultSet.getString(5));
    contact.setStudentId(resultSet.getInt(6));
    contact.setEnterpriseId(resultSet.getInt(7));
    contact.setAcademicYear(resultSet.getInt(8));
    return contact;
  }
}
