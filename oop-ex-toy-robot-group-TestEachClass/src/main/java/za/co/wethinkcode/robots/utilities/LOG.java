package za.co.wethinkcode.robots.utilities;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LOG {

  private static final Logger logger = Logger.getLogger(LOG.class.getName());
  private static FileHandler fileHandler;
  private static final String SERVER = "src\\main\\java\\za\\co\\wethinkcode\\robots\\LOGS\\ServerLogs.log";
  private static final String CLIENT = "src\\main\\java\\za\\co\\wethinkcode\\robots\\LOGS\\ClientLog.log";

  private static synchronized void addToLog(String message, String file) {
    SimpleFormatter formatter = new SimpleFormatter();
    try {
      fileHandler = new FileHandler(file, true);
      fileHandler.setFormatter(formatter);
    } catch (IOException | SecurityException e) {
    }

    logger.addHandler(fileHandler);
    logger.setUseParentHandlers(false);

    String[] msgComponents = message.split("\\?");

    if (message.contains("ERROR?")) {
      logger.log(CustomLevels.ERROR, "{0}\n", msgComponents[1]);
    } else if (message.contains("WARNING?")) {
      logger.log(Level.WARNING, "{0}\n", msgComponents[1]);
    } else if (message.contains("ISSUE?")) {
      logger.log(CustomLevels.ISSUE, "{0}\n", msgComponents[1]);
    } else if (message.contains("UNKNOWN?")) {
      logger.log(CustomLevels.UNKNOWN, "{0}\n", msgComponents[1]);
    } else {
      logger.log(Level.INFO, "{0}\n", msgComponents[0]);
    }

    try {
      fileHandler.close();
    } catch (SecurityException e) {
    }
  }

  public static synchronized void addToClientLog(String message) {
    addToLog(message, CLIENT);
  }

  public static synchronized void addToServerLog(String message) {
    addToLog(message, SERVER);
  }
}