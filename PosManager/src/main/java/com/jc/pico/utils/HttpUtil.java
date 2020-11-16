/*
 * Filename	: HttpUtil.java
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

public class HttpUtil {
  protected static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

  private static final String CHARSET = "UTF-8";
  private static final int TIMEOUT = 5 * 1000;

  public static String request(final String strUrl, final Map<String, String> params) {
    try {
      return request(strUrl, params, CHARSET, TIMEOUT);
    } catch (UnsupportedEncodingException e) {
      return null; // not occured...
    }
  }

  public static String request(final String strUrl, final String body) {
    return request(strUrl, body, CHARSET, TIMEOUT);
  }

  public static String request(final String strUrl, final Map<String, String> params, final String charset, final int timeout) throws UnsupportedEncodingException {
    // set normal params
    StringBuffer urlParams = new StringBuffer();
    if (params != null && params.size() > 0) {
      Set<String> keys = params.keySet();
      for (String key : keys) {
        String val = params.get(key);

        if (urlParams.length() > 0) {
          urlParams.append("&");
        }

        urlParams.append(String.format("%s=%s", key, URLEncoder.encode(val, charset)));
      }
    }
    return request(strUrl, urlParams.toString(), charset, timeout);
  }

  public static String request(final String strUrl, final String body, final String charset, final int timeout) {
    BufferedReader br = null;
    try {
      URL url = new URL(strUrl);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setReadTimeout(timeout);
      conn.setConnectTimeout(timeout);
      conn.setRequestMethod("POST");
      conn.setUseCaches(false);
      conn.setDoOutput(true); // post
      conn.setDoInput(true);
      conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

      OutputStream out = conn.getOutputStream();
      DataOutputStream dout = new DataOutputStream(out);

      // set params
      dout.writeBytes(body);
      dout.close();
      logger.debug("url = {}", strUrl);
      logger.debug("body = {}", body);

      int resCode = conn.getResponseCode();
      logger.debug("respose Code : {}", resCode);
      if (resCode != 200) {
        String resMsg = conn.getResponseMessage();
        String errorMessage = "responseCode: " + resCode + "\nresponseMessage: " + resMsg;
        logger.debug(errorMessage);
      }

      InputStream in = conn.getInputStream();
      br = new BufferedReader(new InputStreamReader(in));
      StringBuffer buf = new StringBuffer();
      String line = null;
      while ((line = br.readLine()) != null) {
        buf.append(line);
        logger.debug(line);
      }

      logger.debug("response length = {}", buf.length());
      br.close();

      return buf.toString();
    } catch (NumberFormatException e) {
      logger.warn("NumberFormatException", e);
    } catch (UnsupportedEncodingException e) {
      logger.warn("UnsupportedEncodingException", e);
    } catch (IOException e) {
      logger.warn("IOException", e);
    } finally {
      try {
        if (br != null) {
          br.close();
        }
      } catch (IOException e) {}
    }

    return "";
  }
}
