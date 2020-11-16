/*
 * Filename	: JCProperties.java
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

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;

public class JCProperties extends Properties {
  protected static Logger logger = LoggerFactory.getLogger(JCProperties.class);

  public JCProperties() {
    super();
  }

  /**
   * File에 있는 내용을 key, value형태로 읽어들인다.
   *
   * @param filePath File의 전체경로 및 이름
   */
  public void loadFromFile(String filePath) {
    if (filePath != null && filePath.length() > 0) {
      try {
        FileInputStream fi = new FileInputStream(filePath);
        this.load(fi);
        fi.close();
      } catch (IOException e) {
        logger.error("loadFromFile fail", e);
      }
    }
  }

  /**
   * File로 key, value형태로 정보를 저장한다
   *
   * @param filePath 정보를 저장하기 위한 File의 전체경로 및 이름
   */
  public void saveToFile(String filePath) {
    if (filePath != null && filePath.length() > 0) {
      try {
        FileOutputStream fo = new FileOutputStream(filePath);
        this.store(fo, "Properties Save");
        fo.close();
      } catch (IOException e) {
        logger.error("saveToFile fail", e);
      }
    }
  }


  /**
   * key, value형태로 정보를 File로 저장하기 위한 것
   * key token중에 "hidden" 항목이 있는경우는 저장하지 않는다.
   *
   * @param filePath 정보를 저장하기 위한 file full path name
   */
  public void saveToFileExceptHidden(String filePath) {
    if (filePath != null && filePath.length() > 0) {

      try {
        DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(filePath)));
        String outStr = null;
        String keyToken = null;
        Enumeration enu = propertyNames();
        boolean validateToken = true;

        outer:
        while (enu.hasMoreElements()) {
          validateToken = true;
          keyToken = (String) enu.nextElement();

          StringTokenizer st = new StringTokenizer(keyToken, ".");
          String token = null;

          while (st.hasMoreTokens()) {
            token = (String) st.nextToken();
            // hidden 항목은 저장하지 않는다.
            if (token.toLowerCase().equals("hidden")) {
              continue outer;
            }
          }

          outStr = keyToken + "=" + getProperty(keyToken) + "\n";
          out.writeBytes(outStr);
        }
      } catch (Exception e) {
        logger.error("saveToFileExceptHidden error", e);
      }
    }
  }

  /**
   * key에 해당되는 value를 int로 얻는다
   *
   * @param key value를 얻기 위한 key
   */
  public int getInt(String key) {
    int rtnInt = -1;
    if (key != null && key.length() > 0) {
      String value = getProperty(key);
      if (value != null) {
        rtnInt = new Integer(value).intValue();
      }
    }
    return rtnInt;
  }

  /**
   * key에 해당되는 int value를 저장한다.
   *
   * @param key   value를 얻기 위한 key
   * @param value 저장할 value
   */
  public synchronized void setInt(String key, int value) {
    setProperty(key, Integer.toString(value));
  }

  /**
   * key에 해당되는 value를 String으로 얻는다
   *
   * @param key value를 얻기 위한 key
   */
  public String getString(String key) {
    return (key != null && key.length() > 0 ? getProperty(key) : null);
  }

  /**
   * key에 해당되는 String value를 저장한다.
   *
   * @param key   value를 얻기 위한 key
   * @param value 저장할 value
   */
  public synchronized void setString(String key, String value) {
    if (value != null) {
      setProperty(key, value);
    }
  }

  public boolean getBoolean(String key) {
    String val = getString(key);
    return "true".equals(val);
  }

  public synchronized void setBoolean(String key, boolean bool) {
    String value = String.valueOf(bool);
    if (value != null) {
      setProperty(key, value);
    }
  }

  /**
   * key에 해당되는 value를 Object로 얻는다.
   * 사용법 : Integer a = (Integer)JProperties.getObject("key");
   *
   * @param key value를 얻기 위한 key
   */
  public Object getObject(String key) {
    return (key != null && key.length() > 0 ? get(key) : null);
  }

  /**
   * key에 해당되는 value를 Object로 저장한다.
   *
   * @param key   value를 얻기 위한 key
   * @param value 저장할 value (AnyType object possible)
   */
  public synchronized void setObject(String key, Object value) {
    if (value != null) {
      put(key, value);
    }
  }

  /**
   * 현재 key가 존재하는지를 확인한다.
   *
   * @param key 조사하려는 key
   */
  public boolean isAvailable(String key) {
    return containsKey(key);
  }
}
