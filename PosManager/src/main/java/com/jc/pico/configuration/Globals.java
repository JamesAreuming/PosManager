/*
 * Filename	: Globals.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.configuration;

import com.jc.pico.utils.Config;

import java.io.File;

public class Globals {
  public static final Config config = Config.getInstance();

  // PICO ID's
  public static final long PICO_PLATFORM_ID = 1;
  public static final long PICO_TENANT_ID = 1;
  public static final long PICO_SERVICE_ID = 1;
  public static final long PICO_BRAND_ID = 3;   // TODO 제거 후.. 에러나는곳 처리 할것.

  // PATH
  public static final String USER_FILE_BASE;
  public static final String UPLOAD_TEMP;
  public static final String APP_RESOURCE;
  public static final String IMG_RESOURCE;
  public static final String LOG_RESOURCE;
  public static final String BIN_RESOURCE;
  public static final String ITEM_RESOURCE;
  public static final String BRAND_RESOURCE;
  public static final String STORE_RESOURCE;
  public static final String LOGIN_URL;

  // DB
  public static final String DB_DRIVER;
  public static final String DB_URL;
  public static final String DB_USER;
  public static final String DB_PASSWORD;
  public static final String DB_TEST_QUERY;
  public static final String DB_RESOURCE_PATH;
  public static final String DB_CONFIGURATION_PATH;
  public static final int DB_TIME_BETWEEN_EVICTION_RUNS_MILLIS;
  public static final int DB_MAX_ACTIVE;
  public static final int DB_MAX_IDLE;
  public static final int DB_MAX_WAIT;
  public static final int DB_MIN_IDLE;

  public static final String AMQP_HOST;
  public static final int AMQP_PORT;
  public static final String AMQP_USERNAME;
  public static final String AMQP_PASSWORD;

  public static final String MQTT_HOST;
  public static final int MQTT_PORT;

  public static final String JSON_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  public static final String LOG_DIR;
  
  public static final String ERROR_LOGIN_FAIL = "ESO0017";
  public static final String ERROR_LOGIN_RESTRICTION = "ESO0018";
  public static final String ERROR_LOGIN_RESTRICTION_FULL = "ESO0019";
  
  /**
   * Common Error : 로그인 인증 실패
   */
  public static final String ERROR_UNAUTHORIZED = "ESX0001";
  
  /**
   * Common Error : 로그인 시도 횟수 초과
   */
  public static final String ERROR_USER_LOGIN_LIMIT_EXCEEDED = "ESX0002";

  //---------------------------------------------------------------------------
  // static
  //---------------------------------------------------------------------------
  static {
    USER_FILE_BASE = config.getString("user.file.base");
    UPLOAD_TEMP = USER_FILE_BASE + File.separator + config.getString("upload.temp");
    APP_RESOURCE = USER_FILE_BASE + File.separator + config.getString("app.resource");
    IMG_RESOURCE = USER_FILE_BASE + File.separator + config.getString("image.resource");
    LOG_RESOURCE = USER_FILE_BASE + File.separator + config.getString("log.resource");
    BIN_RESOURCE = USER_FILE_BASE + File.separator + config.getString("bin.resource");
    ITEM_RESOURCE = File.separator + config.getString("item.resource");
    BRAND_RESOURCE = File.separator + config.getString("brand.resource");
    STORE_RESOURCE = File.separator + config.getString("store.resource");
    LOGIN_URL = config.getString("login.url");

    DB_DRIVER = config.getString("db.driver");
    DB_URL = config.getString("db.url");
    DB_USER = config.getString("db.user");
    DB_PASSWORD = config.getString("db.password");
    DB_MAX_ACTIVE = config.getInt("db.max.active");
    DB_MAX_IDLE = config.getInt("db.max.idle");
    DB_MAX_WAIT = config.getInt("db.max.wait");
    DB_MIN_IDLE = config.getInt("db.min.idle");
    DB_TEST_QUERY = config.getString("db.test.query");
    DB_TIME_BETWEEN_EVICTION_RUNS_MILLIS = config.getInt("db.time.between.eviction.runs.millis");
    DB_RESOURCE_PATH = config.getString("db.resources.classpath");
    DB_CONFIGURATION_PATH = config.getString("db.configuration.classpath");

    AMQP_HOST = config.getString("amqp.host");
    AMQP_PORT = config.getInt("amqp.port");
    AMQP_USERNAME = config.getString("amqp.username");
    AMQP_PASSWORD = config.getString("amqp.password");

    MQTT_HOST = config.getString("mqtt.host");
    MQTT_PORT = config.getInt("mqtt.port");

    LOG_DIR = config.getString("log.dir");
  }
}
