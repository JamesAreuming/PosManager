/*
 * Filename	: Config.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Config extends JCProperties {
  protected static Logger logger = LoggerFactory.getLogger(Config.class);
  private static Config instance;

  protected Config() {
    instance = null;
  }

  public static Config getInstance() {
    if (instance == null) {
      init("");
    }
    return instance;
  }

  public static void init(String absPath) {
    logger.debug((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) + " Config : Config init... from [" + absPath + "]");
    try {
      if (instance == null) {
        instance = new Config();
      } else {
        instance.clear();
      }
      instance.loadFromFile(absPath);
    } catch (Exception e) {
      logger.warn("Config.init() fail!", e);
    }
  }
}