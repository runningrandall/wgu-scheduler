package com.randalladams.scheduler.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogUtil {
  private final Logger logger = Logger.getLogger(LogUtil.class
    .getName());
  private FileHandler fh = null;;

  public LogUtil(String logfile) {
    SimpleFormatter formatter = new SimpleFormatter();
    try {
      fh = new FileHandler(logfile, true);
    } catch (Exception e) {
      e.printStackTrace();
    }

    fh.setFormatter(formatter);
    logger.addHandler(fh);
  }

  public void logInfo(String message) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String dateTime = format.format(Calendar.getInstance().getTime());
    logger.info(dateTime + ": " + message);
  }
}
