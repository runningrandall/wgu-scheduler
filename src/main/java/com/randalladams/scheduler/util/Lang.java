package com.randalladams.scheduler.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Lang {
  private static String lang = System.getProperty("user.language");
  private static Locale locale = new Locale(lang, lang.toUpperCase());
  private static ResourceBundle bundle = ResourceBundle.getBundle("i18n", locale);

  public static String getString(String key) {
    return bundle.getString(key);
  }

  private void printLangException(MissingResourceException ex) {
    if (ex instanceof MissingResourceException) {
      System.err.println("Missing Key: " + ex.getKey());
      System.err.println("Caused by: " + ex.getCause());
      System.err.println("Message: " + ex.getMessage());
      ex.printStackTrace(System.err);
    }
  }
}
