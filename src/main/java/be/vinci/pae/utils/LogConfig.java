package be.vinci.pae.utils;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;


/**
 * Class for LogConfig.
 */
public class LogConfig {

  private static Logger LOGGER = Logger.getLogger(LogConfig.class.getName());

  private static void setUpLogger() {
    try {
      // Load properties
      Properties properties = new Properties();
      // get the path of the log file from the properties file
      properties.load(new FileInputStream("dev.properties"));
      String logPath = properties.getProperty("logPath");

      // Create a new log file every day
      String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
      FileHandler fileHandler = new FileHandler(logPath + "log-" + date + ".log", true);

      // create a new formatter
      fileHandler.setFormatter(new CustomLogFormatter());

      // add the fileHandler to the LOGGER
      LOGGER.addHandler(fileHandler);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }


}
