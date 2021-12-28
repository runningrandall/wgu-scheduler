package com.randalladams.scheduler.util;

/**
 * Utility class for using key value pairs in choiceboxes
 * @author Randall Adams
 * @version 1.0.0
 * @since 12/01/2021
 */
public class KeyValuePair {
  private final String key;
  private final String value;

  /**
   * constructor
   * @param key string
   * @param value string
   */
  public KeyValuePair(String key, String value) {
    this.key = key;
    this.value = value;
  }

  /**
   * getter for the key
   * @return string
   */
  public String getKey()   {    return key;    }

  /**
   * gets key value pair as a string
   * @return string
   */
  public String toString() {    return value;  }
}