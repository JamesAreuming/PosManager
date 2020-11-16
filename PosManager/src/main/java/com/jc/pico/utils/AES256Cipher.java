package com.jc.pico.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.spec.AlgorithmParameterSpec;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.validator.GenericValidator;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class AES256Cipher implements TypeHandler<String> {
  
  public static byte[] ivBytes = { 0x04, 0x11, 0x3A, 0x11, 0x10, 0x0A, 0x6E, 0x7B, 0x1F, 0x77, 0x24, 0x78, 0x6B, 0x13, 0x07, 0x00 };
  private static String key = "PBEWITHSHA256AND256BITAES-CBC-BC";
  
  public static String encodeAES256(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    byte[] textBytes = str.getBytes("UTF-8");
    AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
         SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
         Cipher cipher = null;
    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
    try {
      return Base64.encodeBase64String(cipher.doFinal(textBytes));  
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  public static String decodeAES256(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    
    byte[] textBytes = Base64.decodeBase64(str);
    //byte[] textBytes = str.getBytes("UTF-8");
    AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
    SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
    try {
      return new String(cipher.doFinal(textBytes), "UTF-8");  
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
  
  public static void main (String[] agrs) {
    try {
      StringBuffer cardInfo = new StringBuffer();
      String cardNo = "4111-1111-1111-1111";
      String secretCode = "3838";
      String expireMonth = "12";
      String expireYear = "24";
      String cardOwnerName = "김adsfasdfasdfasdfasdfasdafasdfasdfasfsdfsadfas";
      cardInfo.append(AES256Cipher.encodeAES256(cardNo));
      cardInfo.append("|");
      cardInfo.append(AES256Cipher.encodeAES256(secretCode));
      cardInfo.append("|");
      cardInfo.append(AES256Cipher.encodeAES256(expireMonth));
      cardInfo.append("|");
      cardInfo.append(AES256Cipher.encodeAES256(expireYear));
      cardInfo.append("|");
      cardInfo.append(AES256Cipher.encodeAES256(cardOwnerName));
      
      System.out.println("cardNo : " + cardNo + ", secretCode : " + secretCode + ", expireYear : " + expireYear + ", expireMonth : " + expireMonth + ", cardOwnerName : " + cardOwnerName);
      System.out.println("length : " + cardOwnerName.length());
      System.out.println("first Encoding : " + cardInfo.toString());
      String encryptCardInfo = AES256Cipher.encodeAES256(cardInfo.toString());
      System.out.println("second Encoding : " + encryptCardInfo.toString());
      System.out.println("encoding text length : " + encryptCardInfo.toString().length());
      
      String decryptCardInfo = AES256Cipher.decodeAES256(encryptCardInfo);
      System.out.println("order pay decode : " + decryptCardInfo);
      String[] cardInfos = decryptCardInfo.split("\\|");
      System.out.println("card : " + cardInfo);
      for (String d : cardInfos) {
        System.out.println("card 1 : " + d);
      }
    } catch (InvalidKeyException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (BadPaddingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
    if(GenericValidator.isBlankOrNull(parameter)) {
      ps.setString(i, parameter);
    } else {
      try {
        ps.setString(i, AES256Cipher.encodeAES256((parameter.replace("-", ""))));
      } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (InvalidAlgorithmParameterException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IllegalBlockSizeException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (BadPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }     
  }

  @Override
  public String getResult(ResultSet rs, String columnName) throws SQLException {
    // TODO Auto-generated method stub
    try {
      return AES256Cipher.decodeAES256(rs.getString(columnName));
    } catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
        | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public String getResult(ResultSet rs, int columnIndex) throws SQLException {
    // TODO Auto-generated method stub
    try {
      return AES256Cipher.decodeAES256(rs.getString(columnIndex));
    } catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
        | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
    // TODO Auto-generated method stub
    try {
      return AES256Cipher.decodeAES256(cs.getString(columnIndex));
    } catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
        | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }
}