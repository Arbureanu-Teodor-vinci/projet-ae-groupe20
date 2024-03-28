package be.vinci.pae.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Custom formatter for the logger.
 */
public class CustomFormatter extends Formatter {

  /**
   * Format the log record.
   *
   * @param record the log record
   * @return the formatted log record
   */
  @Override
  public String format(LogRecord record) {
    StringBuilder builder = new StringBuilder();

    // Append the level of the log
    builder.append(record.getLevel()).append(": ");

    // Append the date of the log
    builder.append(
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(record.getMillis())));

    // Append the message of the log
    builder.append(" -  : ").append(record.getMessage()).append("\n");

    // Append the stack trace of the log
    Throwable throwable = record.getThrown();
    if (throwable != null) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      throwable.printStackTrace(pw);
      String stackTrace = sw.toString();

      builder.append("\n").append(stackTrace);
    }

    return builder.toString();
  }

}
