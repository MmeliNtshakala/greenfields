package za.co.wethinkcode.robots.utilities;

import java.util.logging.Level;

public class CustomLevels extends Level {
  public static final Level ERROR = new CustomLevels("ERROR", 1000);
  public static final Level ISSUE = new CustomLevels("ISSUE", 1000);
  public static final Level UNKNOWN = new CustomLevels("UNKNOWN_COMMAND", 1000);

  protected CustomLevels(String name, int value) {
    super(name, value);
  }

}
