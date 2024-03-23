package be.vinci.pae.utils;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration of dev.properties.
 */
public class Config {

  private static Properties props;

  /**
   * Load the properties file.
   *
   * @param file -> the file
   */
  public static void load(String file) {
    props = new Properties();
    try (InputStream input = new FileInputStream(file)) {
      props.load(input);
    } catch (IOException e) {
      throw new WebApplicationException(
          Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).type("text/plain")
              .build());
    }
  }

  /**
   * Get the property.
   *
   * @param key -> the key
   * @return String property
   */
  public static String getProperty(String key) {
    return props.getProperty(key);
  }

  /**
   * Get the int property.
   *
   * @param key -> the key
   * @return Integer property
   */
  public static Integer getIntProperty(String key) {
    return Integer.parseInt(props.getProperty(key));
  }

  /**
   * Get the boolean property.
   *
   * @param key -> the key
   * @return boolean property
   */
  public static boolean getBoolProperty(String key) {
    return Boolean.parseBoolean(props.getProperty(key));
  }

}
