package za.co.wethinkcode.robots.utilities;

import java.io.InputStream;
import java.util.Properties;

public class ReadPropertiesFile {

  private Properties properties;

  public ReadPropertiesFile(String filename) {

    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename)) {
      properties = new Properties();
      properties.load(inputStream);

    } catch (Exception e) {
      Ansi.print(Ansi.red("Error reading resources stream:" + e.toString()));
      LOG.addToServerLog("Error reading resources stream: " + e.toString());
      returnNull();
    }
  }

  private Void returnNull() {
    return null;
  }

  public String returnValue(String property) {
    return properties.getProperty(property);
  }

}
