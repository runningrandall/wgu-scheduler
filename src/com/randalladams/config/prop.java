package config;

import java.util.Properties;
import org.apache.log4j.Logger;

public class prop {
  final static Logger log = Logger.getLogger(prop.class);
  private static Properties properties;
  /**
   * Private constructor so this class cannot be instantiated only by it self.
   */
  private prop()
  {
  }
  /**
   * Lazy init for this Singleton Class.
   *
   * @return The Properties object.
   */
  public static Properties getInstance()
  {
    if (properties == null)
    {
      log.debug("Trying to create Properties...");
      try
      {
        properties = new Properties();

        // load  app.properties :
        properties.load(PROP.class.getClassLoader().getResourceAsStream("app.properties"));
        log.debug("Success add app.properties file.");

      } catch (IOException e)
      {
        log.error("Error creating Properties object from properties file : " + e);
        System.exit(0);
      }
    }

    return properties;
  }

  /**
   * Method to get the properties value for a given key.
   *
   * @param key
   * @return The String value.
   */
  public static String getProperty(String key)
  {
    return PROP.getInstance().getProperty(key);
  }

  /**
   * Create the instance for the first time.
   */
  public static void init()
  {
    PROP.getInstance();
  }

}
