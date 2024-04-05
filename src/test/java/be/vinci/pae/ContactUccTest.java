package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.api.filters.BiznessException;
import be.vinci.pae.domain.academicyear.AcademicYearDTO;
import be.vinci.pae.domain.contact.ContactDTO;
import be.vinci.pae.domain.contact.ContactUCC;
import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.domain.user.StudentDTO;
import be.vinci.pae.services.contactservices.ContactDAO;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ContactUccTest {

  ServiceLocator locator = ServiceLocatorUtilities.bind(new TestsApplicationBinder());
  private ContactUCC contactUCC = locator.getService(ContactUCC.class);
  private ContactDAO contactDAO = locator.getService(ContactDAO.class);
  private DomainFactory domainFactory = locator.getService(DomainFactory.class);

  private ContactDTO contactDTO = domainFactory.getContactDTO();
  private ContactDTO contactDTO2 = domainFactory.getContactDTO();
  private ContactDTO contactAccepted = domainFactory.getContactDTO();
  private StudentDTO studentDTO = domainFactory.getStudentDTO();
  private StudentDTO studentDTO2 = domainFactory.getStudentDTO();
  private AcademicYearDTO academicYearDTO = domainFactory.getAcademicYearDTO();
  private EnterpriseDTO enterpriseDTO = domainFactory.getEnterpriseDTO();


  @BeforeEach
  void setUp() {
    contactDTO.setId(1);
    contactDTO2.setId(2);
    contactAccepted.setId(3);
    contactAccepted.setStateContact("accepted");
    contactDTO.setStateContact("pending");

    Mockito.when(contactDAO.getOneContactByid(1)).thenReturn(contactDTO);
    Mockito.when(contactDAO.getOneContactByid(2)).thenReturn(contactDTO2);

    academicYearDTO.setId(1);

    studentDTO.setId(1);
    studentDTO.setAcademicYear(academicYearDTO);
    studentDTO2.setId(2);
    studentDTO2.setAcademicYear(academicYearDTO);

    contactDTO.setStudentId(1);
    contactDTO2.setStudentId(2);
    contactAccepted.setStudentId(2);
    contactDTO.setAcademicYear(1);

    enterpriseDTO.setId(1);

    Mockito.when(contactDAO.addContact(1, 1, 1)).thenReturn(contactDTO);

  }

  @Test
  @DisplayName("Get one contact with id 1")
  void getOneContact() {
    ContactDTO actualContact = contactUCC.getOneContact(1);

    assertEquals(contactDTO, actualContact);
  }

  @Test
  @DisplayName("Get one contact with id -1")
  void getOneContact2() {
    assertThrows(BiznessException.class, () -> {
      contactUCC.getOneContact(-1);
    });
  }

  @Test
  @DisplayName("Get one contact with an id not in the database")
  void getOneContact3() {
    ContactDTO actualContact = contactUCC.getOneContact(999);

    assertEquals(null, actualContact);
  }

  @Test
  @DisplayName("Get all contacts")
  void getAllContacts() {
    List<ContactDTO> expectedContacts = new ArrayList<>();
    expectedContacts.add(contactDTO);
    expectedContacts.add(contactDTO2);

    Mockito.when(contactDAO.getAllContacts()).thenReturn(expectedContacts);

    List<ContactDTO> actualContacts = contactUCC.getAllContacts();

    assertEquals(expectedContacts, actualContacts);
  }

  @Test
  @DisplayName("Get all contacts with no contacts in the database")
  void getAllContacts2() {
    List<ContactDTO> expectedContacts = new ArrayList<>();

    Mockito.when(contactDAO.getAllContacts()).thenReturn(expectedContacts);

    List<ContactDTO> actualContacts = contactUCC.getAllContacts();

    assertEquals(expectedContacts, actualContacts);
  }

  @Test
  @DisplayName("Get contacts by user")
  void getContactsByUser() {
    List<ContactDTO> expectedContacts = new ArrayList<>();
    expectedContacts.add(contactDTO);

    Mockito.when(contactDAO.getContactsByUser(1)).thenReturn(expectedContacts);

    List<ContactDTO> actualContacts = contactUCC.getContactsByUser(1);

    assertEquals(expectedContacts, actualContacts);
  }

  @Test
  @DisplayName("Add contact for student 1")
  void addContact() {
    List<ContactDTO> contactsExisting = new ArrayList<>();
    Mockito.when(contactDAO.getContactsByUser(1)).thenReturn(contactsExisting);
    ContactDTO actualContact = contactUCC.addContact(studentDTO, enterpriseDTO);

    assertEquals(contactDTO, actualContact);
  }

  @Test
  @DisplayName("Add contact for student 2 while he already has a contact accepted")
  void addContact2() {
    List<ContactDTO> contactsExisting = new ArrayList<>();
    contactsExisting.add(contactAccepted);

    Mockito.when(contactDAO.getContactsByUser(2)).thenReturn(contactsExisting);

    assertThrows(BiznessException.class, () -> {
      contactUCC.addContact(studentDTO2, enterpriseDTO);
    });

  }

  @Test
  @DisplayName("Add contact for student 1 while he already has a contact with the same enterprise")
  void addContact3() {
    List<ContactDTO> contactsExisting = new ArrayList<>();
    contactsExisting.add(contactDTO);
    contactDTO.setEnterpriseId(1);

    Mockito.when(contactDAO.getContactsByUser(1)).thenReturn(contactsExisting);

    assertThrows(BiznessException.class, () -> {
      contactUCC.addContact(studentDTO, enterpriseDTO);
    });
  }


}
