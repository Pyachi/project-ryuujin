package com.cs321.team1.game;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {

  private boolean disabled = false;
  private final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  Log() {
    try {
      var logFolder = new File("logs");
      if (!logFolder.exists() && !logFolder.mkdirs()) {
        return;
      }
      var recentLog = new File("logs/latest.log");
      if (recentLog.exists()) {
        int i = 0;
        File logZip;
        do {
          i++;
          var date = new SimpleDateFormat("yyyy-MM-dd").format(recentLog.lastModified());
          logZip = new File("logs/" + date + "-" + i + ".log.zip");
        } while (logZip.exists());
        try (var system = FileSystems.newFileSystem(logZip.toPath(), new HashMap<>() {{
          put("create", "true");
        }})) {
          Files.copy(recentLog.toPath(), system.getPath("latest.log"),
              StandardCopyOption.REPLACE_EXISTING);
        }
      }
      logger.setUseParentHandlers(false);
      var formatter = new SimpleFormatter() {
        private static final String format = "[%1$tT] [%2$-4s] %3$s %n";

        @Override
        public String format(LogRecord record) {
          return String.format(format, new Date(record.getMillis()),
              record.getLevel().getLocalizedName(), record.getMessage());
        }
      };
      var fileHandler = new FileHandler("logs/latest.log");
      fileHandler.setFormatter(formatter);
      logger.addHandler(fileHandler);
      var consoleHandler = new ConsoleHandler();
      consoleHandler.setFormatter(formatter);
      logger.addHandler(consoleHandler);
    } catch (Exception e) {
      disabled = true;
    }
  }

  public void info(String message) {
    if (disabled) {
      return;
    }
    logger.log(Level.INFO, message);
  }

  public void warning(String message) {
    if (disabled) {
      return;
    }
    logger.log(Level.WARNING, message);
  }

  public void severe(String message) {
    if (disabled) {
      return;
    }
    logger.log(Level.SEVERE, message);
  }

  public void error(String message, Exception e) {
    if (disabled) {
      return;
    }
    logger.log(Level.SEVERE, message);
    var buffer = new StringWriter();
    var printer = new PrintWriter(buffer);
    e.printStackTrace();
    e.printStackTrace(printer);
    logger.log(Level.SEVERE, buffer.toString());
  }
}
