/*
 * Filename	: BlowFish.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * <PRE>
 * Filename : BlowFish.java
 * Class    :
 * Function :
 * Comment  :
 * History  : 
 *
 * </PRE>
 * @version 1.0
 * @author  
 * @since   JDK 1.5
 */
public class BlowFish {
	final static String keyRaw = "FS5SyjH/IA==";

	public static String encrypt(String source) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(Base64.decodeBase64(keyRaw), "Blowfish");

		Cipher cipher = Cipher.getInstance("Blowfish");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(source.getBytes());
		
		return new String(Base64.encodeBase64(encrypted));
	}

	public static String decrypt(String source) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(Base64.decodeBase64(keyRaw), "Blowfish");

		Cipher cipher = Cipher.getInstance("Blowfish");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(Base64.decodeBase64(source));

		return new String(decrypted);
	}
	
	public static void main(String[] args) throws Exception {
		// test
		System.out.println(BlowFish.encrypt("test"));
		System.out.println(BlowFish.decrypt("aSQuSfZ9W4c="));
	}
}