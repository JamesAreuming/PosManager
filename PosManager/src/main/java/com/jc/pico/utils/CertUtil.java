/*
 * Filename	: CertUtil.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.jc.pico.exception.InvalidTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class CertUtil {
  protected static Logger logger = LoggerFactory.getLogger(CertUtil.class);

  private static final String ENCRYPT_KEY = "05e56ee3dd69208fc68e203cee0d417b4474582e";
  private final byte[] encryptKey;
  private Cache<String, String> certNoMap;
  private Cache<String, String> phoneNoMap;

  public CertUtil() {
    this(ENCRYPT_KEY);
  }

  public CertUtil(String encryptKey) {
    // 2분 동안 유효.(발행된 인증 번호)
    this.certNoMap = CacheBuilder.newBuilder()
            .expireAfterWrite(120, TimeUnit.SECONDS).build();

    // 20분 동안 유효.(인증된 휴대전화 번호를 조회하는 token)
    this.phoneNoMap = CacheBuilder.newBuilder()
            .expireAfterWrite(20, TimeUnit.MINUTES).build();
    this.encryptKey = encryptKey.getBytes();
  }

  public String putCertNumber(String phoneNo) {
    Random random = new Random(System.currentTimeMillis());
    String certNo = String.valueOf(Math.abs(random.nextInt()));
    while (true) {
      if (certNo.length() == 6) {
        break;
      }

      if (certNo.length() > 6) {
        certNo = certNo.substring(0, 6);
      } else {
        certNo += String.valueOf(Math.abs(random.nextInt()));
      }
    }

    this.certNoMap.put(phoneNo, certNo);
    return certNo;
  }

  public String getCertNumber(String phoneNo) {
    return this.certNoMap.getIfPresent(phoneNo);
  }

  public void removeCertNumber(String phoneNo) {
    this.certNoMap.invalidate(phoneNo);
  }

  public void putPhoneNumber(String key, String phoneNumber) {
    this.phoneNoMap.put(key, phoneNumber);
  }

  public String getPhoneNumber(String key) {
    return this.phoneNoMap.getIfPresent(key);
  }

  public void removePhoneNumber(String key) {
    this.phoneNoMap.invalidate(key);
  }

  public String makeSHA(String inputText) {
    try {
      String test = inputText;
      MessageDigest md = MessageDigest.getInstance("SHA-256");

      md.update(test.getBytes());
      byte[] digest = md.digest();

      //		System.out.println(md.getAlgorithm());
      //		System.out.println(digest.length);

      StringBuffer sb = new StringBuffer();
      for (byte b : digest) {
        //			System.out.print(Integer.toHexString(b & 0xff) + "");
        sb.append(Integer.toHexString(b & 0xff));
      }

      //		System.out.println("\n\nReturn String : " + sb.toString());
      return sb.toString();
    } catch (Exception e) {
      return null;
    }
  }

  public String decrypt(String text) throws InvalidTokenException {
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      byte[] keyBytes = new byte[16];
      byte[] b = this.encryptKey;
      int len = b.length;
      if (len > keyBytes.length) {
        len = keyBytes.length;
      }
      System.arraycopy(b, 0, keyBytes, 0, len);
      SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
      IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
      cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

      String src = URLDecoder.decode(text, "UTF-8");
      byte[] results = cipher.doFinal(Base64.decodeBase64(src));
      return new String(results, "UTF-8");
    } catch (Exception e) {
      logger.warn("decrypt fail", e);
      throw new InvalidTokenException(text);
    }
  }

  public String encrypt(String text) throws InvalidTokenException {
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      byte[] keyBytes = new byte[16];
      byte[] b = this.encryptKey;
      int len = b.length;
      if (len > keyBytes.length) {
        len = keyBytes.length;
      }
      System.arraycopy(b, 0, keyBytes, 0, len);
      SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
      IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
      cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

      byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
      String encoded = new String(Base64.encodeBase64(results));
      return URLEncoder.encode(encoded, "UTF-8");
    } catch (Exception e) {
      logger.warn("encrypt fail", e);
      throw new InvalidTokenException(text);
    }
  }
}
