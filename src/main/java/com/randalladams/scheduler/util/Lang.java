package com.randalladams.scheduler.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Utility class for working with languages
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/01/2021
 */
public class Lang {
  private static String lang = System.getProperty("user.language");
  private static Locale locale = new Locale(lang, lang.toUpperCase());
  private static ResourceBundle bundle = ResourceBundle.getBundle("i18n", locale);

  /**
   * getter for a lang string
   * @param key string
   * @return string
   */
  public static String getString(String key) {
    return bundle.getString(key);
  }
}
