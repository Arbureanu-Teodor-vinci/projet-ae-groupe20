package be.vinci.pae.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomLogFormatter extends Formatter {

  /**
   * Format the log message.
   *
   * @param record -> record
   * @return -> formatted message
   */
  public String format(LogRecord record) {
    StringBuilder builder = new StringBuilder();

    // log date and time
    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    builder.append(date).append(" - ");

    // log source class and method
    builder.append(record.getSourceClassName()).append(".").append(record.getSourceMethodName())
        .append(" - ");

    // log level
    builder.append(record.getLevel()).append(" - ");

    return record.getMessage();
  }
}
