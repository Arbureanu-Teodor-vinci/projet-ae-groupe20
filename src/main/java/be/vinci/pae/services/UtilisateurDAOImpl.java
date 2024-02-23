package be.vinci.pae.services;

import be.vinci.pae.domain.DomainFactory;
import be.vinci.pae.domain.DomainFactoryImpl;
import be.vinci.pae.domain.Utilisateur;
import be.vinci.pae.services.utils.Json;
import jakarta.inject.Inject;

/**
 * Implementation des services utilisateur.
 */
public class UtilisateurDAOImpl implements UtilisateurDAO {

  private static final String NOM_COLLECTION = "utilisateurs";
  @Inject
  private static DomainFactory monUserDTO = (DomainFactory) new DomainFactoryImpl().getUser();
  private static Json<Utilisateur> jsonDB = new Json<>(Utilisateur.class);


  @Override
  public Utilisateur getUnUtilisateur(int id) {
    var lesUtilisateurs = jsonDB.parse(NOM_COLLECTION);
    return lesUtilisateurs.stream().filter(utilisateur -> utilisateur.getId() == id).findAny()
        .orElse(null);
  }

  @Override
  public Utilisateur getUnUtilisateur(String email) {
    var lesUtilisateurs = jsonDB.parse(NOM_COLLECTION);
    return lesUtilisateurs.stream().filter(utilisateur -> utilisateur.getEmail().equals(email))
        .findAny().orElse(null);
  }


}
