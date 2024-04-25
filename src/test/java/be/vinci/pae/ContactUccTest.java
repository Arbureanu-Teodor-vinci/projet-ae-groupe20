package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.api.filters.BusinessException;
import be.vinci.pae.domain.academicyear.AcademicYearDTO;
import be.vinci.pae.domain.contact.ContactDTO;
import be.vinci.pae.domain.contact.ContactUCC;
import be.vinci.pae.domain.enterprise.EnterpriseDTO;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.domain.user.StudentDTO;
import be.vinci.pae.services.contactservices.ContactDAO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * ContactUccTest.
 */
public class ContactUccTest {

  ServiceLocator locator = ServiceLocatorUtilities.bind(new TestsApplicationBinder());
  private ContactUCC contactUCC = locator.getService(ContactUCC.class);
  private ContactDAO contactDAO = locator.getService(ContactDAO.class);
  private DomainFactory domainFactory = locator.getService(DomainFactory.class);

  private ContactDTO contactDTO = domainFactory.getContactDTO();
  private ContactDTO contactDTO2 = domainFactory.getContactDTO();
  private ContactDTO contactAccepted = domainFactory.getContactDTO();
  private StudentDTO studentDTO = domainFactory.getStudentDTO();
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

    contactDTO.setStudentId(1);
    contactDTO2.setStudentId(2);
    contactAccepted.setStudentId(2);

    enterpriseDTO.setId(1);

    Mockito.when(contactDAO.addContact(1, 1, 1)).thenReturn(contactDTO);
    //when contact is updated return the updated contact
    Mockito.when(contactDAO.updateContact(Mockito.any(ContactDTO.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

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
    assertThrows(BusinessException.class, () -> {
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
    assertEquals(contactDTO, contactUCC.addContact(studentDTO, enterpriseDTO));
  }

  @Test
  @DisplayName("Add contact for student 2 while he already has a contact accepted")
  void addContact2() {
    List<ContactDTO> contactsExisting = new ArrayList<>();
    contactsExisting.add(contactAccepted);

    Mockito.when(contactDAO.getContactsByUser(2)).thenReturn(contactsExisting);

    try {
      contactUCC.addContact(studentDTO, enterpriseDTO);
    } catch (Exception e) {
      assertEquals("Student already has a contact for this academic year", e.getMessage());
    }
  }

  @Test
  @DisplayName("Add contact for student 1 while he already has a contact with the same enterprise")
  void addContact3() {
    List<ContactDTO> contactsExisting = new ArrayList<>();
    contactsExisting.add(contactDTO);

    Mockito.when(contactDAO.getContactsByUser(1)).thenReturn(contactsExisting);

    try {
      contactUCC.addContact(studentDTO, enterpriseDTO);
    } catch (Exception e) {
      assertEquals("Contact already exists", e.getMessage());
    }
  }


  @Test
  @DisplayName("Update contact state with all valid possible states from initiated state")
  void updateContact1() {
    contactDTO.setStateContact("initié");
    contactDTO2.setId(1);
    assertAll(
        () -> {
          contactDTO2.setStateContact("pris");
          contactDTO2.setInterviewMethod("A distance");
          contactDTO2.setTool("outil");
          contactDTO.setInterviewMethod("A distance");
          assertEquals("pris", contactUCC.updateContact(contactDTO2).getStateContact());
        },
        () -> {
          contactDTO2.setStateContact("suspendu");
          assertEquals("suspendu", contactUCC.updateContact(contactDTO2).getStateContact());
        },
        () -> {
          contactDTO2.setStateContact("non suivis");
          assertEquals("non suivis", contactUCC.updateContact(contactDTO2).getStateContact());
        }
    );
  }

  @Test
  @DisplayName("Update contact state with all invalid states from initiated state")
  void updateContact2() {
    contactDTO.setStateContact("initié");
    contactDTO2.setId(1);
    assertAll(
        () -> {
          contactDTO2.setStateContact("refusé");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        },
        () -> {
          contactDTO2.setStateContact("accepté");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        }
    );
  }

  @Test
  @DisplayName("Update contact state with all valid possible states from taken state")
  void updateContact3() {
    contactDTO.setStateContact("pris");
    contactDTO.setInterviewMethod("A distance");
    contactDTO2.setId(1);
    contactDTO2.setInterviewMethod("A distance");
    contactDTO2.setTool("outil");
    assertAll(
        () -> {
          contactDTO2.setStateContact("suspendu");
          assertEquals("suspendu", contactUCC.updateContact(contactDTO2).getStateContact());
        },
        () -> {
          contactDTO2.setStateContact("non suivis");
          assertEquals("non suivis", contactUCC.updateContact(contactDTO2).getStateContact());
        },
        () -> {
          contactDTO2.setStateContact("accepté");
          assertEquals("accepté", contactUCC.updateContact(contactDTO2).getStateContact());
        },
        () -> {
          contactDTO2.setStateContact("refusé");
          contactDTO2.setRefusalReason("raison de refus");
          assertEquals("refusé", contactUCC.updateContact(contactDTO2).getStateContact());
        }
    );
  }

  @Test
  @DisplayName("Update contact state with all invalid states from taken state")
  void updateContact4() {
    contactDTO.setStateContact("pris");
    contactDTO2.setId(1);
    assertAll(
        () -> {
          contactDTO2.setStateContact("initié");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        }
    );
  }

  @Test
  @DisplayName("Update contact state with all invalid possible states from suspended state")
  void updateContact5() {
    contactDTO.setStateContact("suspendu");
    contactDTO2.setId(1);
    assertAll(
        () -> {
          contactDTO2.setStateContact("initié");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        },
        () -> {
          contactDTO2.setStateContact("pris");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        },
        () -> {
          contactDTO2.setStateContact("accepté");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        },
        () -> {
          contactDTO2.setStateContact("refusé");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        },
        () -> {
          contactDTO2.setStateContact("non suivis");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        }
    );
  }

  @Test
  @DisplayName("Update contact state with all invalid possible states from accepted state")
  void updateContact6() {
    contactDTO.setStateContact("accepté");
    contactDTO2.setId(1);
    assertAll(
        () -> {
          contactDTO2.setStateContact("initié");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        },
        () -> {
          contactDTO2.setStateContact("pris");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        },
        () -> {
          contactDTO2.setStateContact("suspendu");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        },
        () -> {
          contactDTO2.setStateContact("refusé");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        },
        () -> {
          contactDTO2.setStateContact("non suivis");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        }
    );
  }

  @Test
  @DisplayName("Update contact state with all invalid possible states from refused state")
  void updateContact7() {
    contactDTO.setStateContact("refusé");
    contactDTO2.setId(1);
    assertAll(
        () -> {
          contactDTO2.setStateContact("initié");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        },
        () -> {
          contactDTO2.setStateContact("pris");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        },
        () -> {
          contactDTO2.setStateContact("accepté");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        },
        () -> {
          contactDTO2.setStateContact("suspendu");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        },
        () -> {
          contactDTO2.setStateContact("non suivis");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        }
    );
  }

  @Test
  @DisplayName("Update contact state with all invalid possible states from suspended state")
  void updateContact8() {
    contactDTO.setStateContact("non suivis");
    contactDTO2.setId(1);
    assertAll(
        () -> {
          contactDTO2.setStateContact("initié");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        },
        () -> {
          contactDTO2.setStateContact("pris");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        },
        () -> {
          contactDTO2.setStateContact("accepté");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        },
        () -> {
          contactDTO2.setStateContact("refusé");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        },
        () -> {
          contactDTO2.setStateContact("suspendu");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        }
    );
  }

  @Test
  @DisplayName("Update contact with non valid state")
  void updateContact9() {
    contactDTO.setStateContact("initié");
    contactDTO2.setStateContact("état non valide");
    contactDTO2.setId(1);
    assertThrows(BusinessException.class, () -> {
      contactUCC.updateContact(contactDTO2);
    });
  }

  @Test
  @DisplayName("Update contact interview method when state is taken")
  void updateContact10() {
    contactDTO.setStateContact("initié");
    contactDTO2.setId(1);
    assertAll(
        () -> {
          contactDTO2.setStateContact("pris");
          contactDTO2.setInterviewMethod("A distance");
          contactDTO2.setTool("outil");
          assertEquals("A distance", contactUCC.updateContact(contactDTO2).getInterviewMethod());
        }
    );
  }

  @Test
  @DisplayName("Update contact interview method when state is not taken")
  void updateContact11() {
    contactDTO2.setStateContact("initié");
    contactDTO2.setInterviewMethod("A distance");
    contactDTO2.setId(1);
    assertAll(
        () -> {
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
          contactDTO2.setStateContact("suspendu");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        },
        () -> {
          contactDTO2.setStateContact("non suivis");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        },
        () -> {
          contactDTO2.setStateContact("accepté");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        },
        () -> {
          contactDTO2.setStateContact("refusé");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        }
    );
  }

  @Test
  @DisplayName("Update contact refusal reason when state is refused")
  void updateContact12() {
    contactDTO.setStateContact("pris");
    contactDTO.setInterviewMethod("A distance");
    contactDTO2.setInterviewMethod("A distance");
    contactDTO2.setTool("outil");
    contactDTO2.setId(1);
    assertAll(
        () -> {
          contactDTO2.setStateContact("refusé");
          contactDTO2.setRefusalReason("raison de refus");
          assertEquals("raison de refus", contactUCC.updateContact(contactDTO2).getRefusalReason());
        }
    );
  }

  @Test
  @DisplayName("Update contact refusal reason when state is not refused")
  void updateContact13() {
    contactDTO.setStateContact("initié");
    contactDTO2.setId(1);
    assertAll(
        () -> {
          contactDTO2.setStateContact("pris");
          contactDTO2.setInterviewMethod("A distance");
          contactDTO2.setRefusalReason("raison de refus");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        },
        () -> {
          contactDTO2.setStateContact("suspendu");
          contactDTO2.setRefusalReason("raison de refus");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        },
        () -> {
          contactDTO2.setStateContact("non suivis");
          contactDTO2.setRefusalReason("raison de refus");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        },
        () -> {
          contactDTO2.setStateContact("accepté");
          contactDTO2.setRefusalReason("raison de refus");
          assertThrows(BusinessException.class, () -> {
            contactUCC.updateContact(contactDTO2);
          });
        }
    );
  }

  @Test
  @DisplayName("Get contacts by enterprise returns list of contacts")
  public void getContactsByEnterprise1() {
    List<ContactDTO> expectedContacts = Arrays.asList(contactDTO, contactDTO2);
    Mockito.when(contactDAO.getContactsByEnterprise(1)).thenReturn(expectedContacts);

    List<ContactDTO> actualContacts = contactUCC.getContactsByEnterprise(1);

    assertEquals(expectedContacts, actualContacts);
  }

  @Test
  @DisplayName("Get contacts by enterprise returns empty list when no contacts")
  public void getContactsByEnterprise2() {
    List<ContactDTO> expectedContacts = new ArrayList<>();
    Mockito.when(contactDAO.getContactsByEnterprise(1)).thenReturn(expectedContacts);

    List<ContactDTO> actualContacts = contactUCC.getContactsByEnterprise(1);

    assertEquals(expectedContacts, actualContacts);
  }
  
}
