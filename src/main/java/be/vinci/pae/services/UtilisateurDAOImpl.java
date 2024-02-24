package be.vinci.pae.services;

import be.vinci.pae.domain.UtilisateurDTO;
import be.vinci.pae.services.utils.Json;

/**
 * Implementation des services utilisateur.
 */
public class UtilisateurDAOImpl implements UtilisateurDAO {

  private static final String NOM_COLLECTION = "utilisateurs";
  // @Inject
  // private static DomainFactory monUserDTO;
  private static Json<UtilisateurDTO> jsonDB = new Json<>(UtilisateurDTO.class);


  @Override
  public UtilisateurDTO getUnUtilisateur(int id) {
    var lesUtilisateurs = jsonDB.parse(NOM_COLLECTION);
    return lesUtilisateurs.stream().filter(utilisateur -> utilisateur.getId() == id).findAny()
        .orElse(null);
  }

  @Override
  public UtilisateurDTO getUnUtilisateurParEmail(String email) {
    var lesUtilisateurs = jsonDB.parse(NOM_COLLECTION);
    return lesUtilisateurs.stream()
        .filter(utilisateur -> utilisateur.getEmail().equals(email))
        .findAny().orElse(null);

  }


}
