package be.vinci.pae.services.utils;

import be.vinci.pae.utils.Config;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui permet de transferer du Java vers Json
 *
 * @param <T> -> l'objet à transformer en Json
 */
public class Json<T> {

  private static final String BD_FICHIER_CHEMIN = Config.getProperty("cheminFichierBaseDeDonnees");
  private static final ObjectMapper jsonMapper = new ObjectMapper();
  private static Path cheminVersBD = Paths.get(BD_FICHIER_CHEMIN);
  private Class<T> type;

  // Java generics are mostly compile time, this means that the type information is lost at runtime.
  // To get the type information at runtime you have to add it as an argument of the constructor.
  public Json(Class<T> type) {
    this.type = type;
  }


  /**
   * @param items          -> la DB
   * @param collectionName -> nom de l'objet à ajouter/modifier/supprimer de la DB
   */
  public void serialize(List<T> items, String collectionName) {
    try {
      // if no DB file, write a new collection to a new db file
      if (!Files.exists(cheminVersBD)) {
        // Create an object and add a JSON array as POJO, e.g. { items:[...]}
        ObjectNode newCollection = jsonMapper.createObjectNode().putPOJO(collectionName, items);
        jsonMapper.writeValue(cheminVersBD.toFile(),
            newCollection); // write the JSON Object in the DB file
        return;
      }
      // get all collections : can be read as generic JsonNode, if it can be Object or Array;
      JsonNode allCollections = jsonMapper.readTree(
          cheminVersBD.toFile()); // e.g. { users:[...], items:[...]}
      // remove current collection, e.g. remove the array of items
      if (allCollections.has(collectionName)) {
        ((ObjectNode) allCollections).remove(collectionName); //e.g. it leaves { users:[...]}
      }
      // Prepare a JSON array from the list of POJOs for the collection to be updated,
      // e.g. [{"film1",...}, ...]
      ArrayNode updatedCollection = jsonMapper.valueToTree(items);
      // Add the JSON array in allCollections, e.g. : { users:[...], items:[...]}
      ((ObjectNode) allCollections).putArray(collectionName).addAll(updatedCollection);
      // write to the db file allCollections
      jsonMapper.writeValue(cheminVersBD.toFile(), allCollections);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * @param collectionName -> nom des objets
   * @return List des objets
   */
  public List<T> parse(String collectionName) {
    try {
      // get allCollections
      JsonNode node = jsonMapper.readTree(cheminVersBD.toFile());
      // accessing value of the specified field of an object node,
      // e.g. the JSON array within "items" field of { users:[...], items:[...]}
      JsonNode collection = node.get(collectionName);
      if (collection == null) { // Send an empty list if there is not the requested collection
        return (List<T>) new ArrayList<T>();
      }
      // convert the JsonNode to a List of POJOs & return it
      return jsonMapper.readerForListOf(type).readValue(collection);
    } catch (FileNotFoundException e) {
      return (List<T>) new ArrayList<T>(); // send an empty list if there is no db file
    } catch (IOException e) {
      e.printStackTrace();
      return (List<T>) new ArrayList<T>();
    }
  }

}