package be.vinci.pae.domain.user;

import be.vinci.pae.domain.academicyear.AcademicYear;
import be.vinci.pae.domain.factory.DomainFactory;
import be.vinci.pae.services.dal.DALTransactionServices;
import be.vinci.pae.services.userservices.UserDAO;
import jakarta.inject.Inject;
import java.util.List;

/**
 * Implementation of UserUCC.
 */
public class UserUCCImpl implements UserUCC {

  @Inject
  private UserDAO userDAO;
  @Inject
  private DALTransactionServices dalServices;
  @Inject
  private DomainFactory domainFactory;

  @Override
  public UserDTO getUserById(int id) {
    return userDAO.getOneUserByID(id);
  }

  @Override
  public List<UserDTO> getAll() {
    return userDAO.getAllUsers();
  }

  @Override
  public UserDTO login(String email, String password) {
    UserDTO userDTO = userDAO.getOneUserByEmail(email);

    User user = (User) userDTO;
    if (user == null) {
      throw new NullPointerException("Invalid email");
    }
    user.checkPassword(password);

    return userDTO;
  }

  @Override
  public UserDTO register(UserDTO registeredUser) {
    User user = (User) registeredUser; // cast to User to access Bizness methods
    // check if email is a vinci email
    try {
      user.checkVinciEmail(registeredUser.getEmail());
      // check if role is valid
      user.checkRole(registeredUser.getRole());
      // check if phone number is valid
      user.checkPhoneNumberFormat(registeredUser.getTelephoneNumber());
      // check if names are valid
      user.checkNamesFormat(registeredUser.getFirstName());
      user.checkNamesFormat(registeredUser.getLastName());

      dalServices.startTransaction(); //START TRANSACTION
      // check if email exists already
      UserDTO userDTO = userDAO.getOneUserByEmail(registeredUser.getEmail());
      user.checkUniqueEmail(userDTO);

      //set role, hashed password and set today registration date
      registeredUser.hashPassword();
      registeredUser.setRegistrationDateToNow();

      // add user to DB
      registeredUser = userDAO.addUser(registeredUser);
      dalServices.commitTransaction(); //COMMIT TRANSACTION

    } catch (Throwable e) {
      dalServices.rollbackTransaction(); //ROLLBACK TRANSACTION
      throw e;
    }

    return registeredUser;
  }

  @Override
  public UserDTO updateProfile(UserDTO userDTO) {
    try {
      User user = (User) userDTO;
      user.checkNamesFormat(userDTO.getFirstName());
      user.checkNamesFormat(userDTO.getLastName());
      user.checkPhoneNumberFormat(userDTO.getTelephoneNumber());
      dalServices.startTransaction();
      User userFound = (User) userDAO.getOneUserByID(userDTO.getId());
      userFound.checkNotNull();

      if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
        userDTO.setPassword(userFound.getPassword());
      } else {
        userDTO.hashPassword();
      }

      userDTO = userDAO.updateUser(userDTO);

      dalServices.commitTransaction();
    } catch (Throwable e) {
      dalServices.rollbackTransaction();
      throw e;
    }
    return userDTO;
  }

  @Override
  public int getNumberOfStudentsWithInternshipAllAcademicYears() {
    return userDAO.getNumberOfStudentsWithInternshipAllAcademicYears();
  }

  @Override
  public int getNumberOfStudentsWithoutInternshipAllAcademicYears() {
    return userDAO.getNumberOfStudentsWithoutInternshipAllAcademicYears();
  }

  @Override
  public int getNumberOfStudentsWithInternship(String academicYear) {
    int count = 0;

    List<String> academicYears = userDAO.getAllAcademicYears();
    AcademicYear yearCheck = (AcademicYear) domainFactory.getAcademicYearDTO();
    yearCheck.checkAcademicYear(academicYear, academicYears);
    count = userDAO.getNumberOfStudentsWithInternship(academicYear);

    return count;
  }

  @Override
  public int getNumberOfStudentsWithoutInternship(String academicYear) {
    int count = 0;

    List<String> academicYears = userDAO.getAllAcademicYears();
    AcademicYear yearCheck = (AcademicYear) domainFactory.getAcademicYearDTO();
    yearCheck.checkAcademicYear(academicYear, academicYears);
    count = userDAO.getNumberOfStudentsWithoutInternship(academicYear);

    return count;
  }

}
