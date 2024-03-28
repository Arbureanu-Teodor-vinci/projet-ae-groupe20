package be.vinci.pae.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;

/**
 * Logger classe to manage the logs.
 */
public class Logger {

  private static java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(
      Logger.class.getName());

  static {
    setUpLogger();
  }

  /**
   * Set up the logger.
   */
  private static void setUpLogger() {
    try {
      // Load properties
      String logPath = Config.getProperty("LogPath");

      // Check if the logPath is null or empty
      if (logPath == null || logPath.isEmpty()) {
        System.out.println("logPath is not correctly set in the dev.properties file.");
        return;
      }

      // Check if the directories in the logPath exist
      File logPathDir = new File(logPath);
      if (!logPathDir.exists()) {
        System.out.println("The directories in the logPath do not exist.");
        return;
      }

      // Create a new log file every day
      String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
      String fileName = logPath + File.separator + "log-" + date + ".txt";
      File logFile = new File(fileName);

      // Check if the log file exists, and create it if it does not
      if (!logFile.exists()) {
        boolean wasFileCreated = logFile.createNewFile();
        if (!wasFileCreated) {
          System.out.println("Failed to create the log file.");
        }
      }

      // Create a new file handler
      FileHandler fileHandler = new FileHandler(fileName, true);
      fileHandler.setFormatter(new CustomFormatter());
      LOGGER.addHandler(fileHandler);


    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * Log an entry.
   *
   * @param msg The message to log.
   */
  public static void logEntry(String msg) {
    /*StringBuilder builder = new StringBuilder();

    // log date and time
    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    builder.append(date).append(" - ");

    builder.append(" : ").append(msg);
    LOGGER.info(builder.toString());

     */
    LOGGER.info(msg);
  }

  /**
   * Log an entry with a throwable.
   *
   * @param msg       The message to log.
   * @param throwable The throwable to log.
   */
  public static void logEntry(String msg, Throwable throwable) {
    /*
    StringBuilder builder = new StringBuilder();

    // log date and time
    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    builder.append(date).append(" - ");

    builder.append(" : ").append(msg);
    builder.append(" - ").append(throwable.getMessage());
    LOGGER.log(Level.SEVERE, builder.toString(), throwable);

     */
    LOGGER.log(Level.SEVERE, msg, throwable);
  }
}
