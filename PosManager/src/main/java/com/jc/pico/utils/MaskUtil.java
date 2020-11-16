package com.jc.pico.utils;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
//import com.google.i18n.phonenumbers.NumberParseException;
//import beom.api.fileio.logging.Logger;

public class MaskUtil {
  
  public static String setMobileMask(String Mb){
    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    try {
      phoneNumberUtil.parse(Mb, "KR");

      if(Mb.contains("-")){
        String[] Mb_Sp = Mb.split("-");
        String f_Mb = Mb_Sp[0];
        String t_Mb = Mb_Sp[2];
        String c_Mb = "";
        for(int i = 0 ; i < Mb_Sp[1].length() -1 ; i++){
          c_Mb += "*";
        }
        c_Mb += Mb_Sp[1].substring(Mb_Sp[1].length()-1, Mb_Sp[1].length());      
        Mb = f_Mb+"-"+c_Mb+"-"+t_Mb;
      } else {
        String c_Mb = Mb.substring(3, Mb.length()-4);
        String c_Mb_Mask = "";
        for(int i = 0 ; i < c_Mb.length()-1 ; i++){
          c_Mb_Mask += "*";
        }
        c_Mb_Mask += c_Mb.substring(c_Mb.length()-1, c_Mb.length());
        Mb = Mb.substring(0, 3)+"-"+c_Mb_Mask+"-"+Mb.substring(Mb.length()-4, Mb.length());
      }
    } catch (Exception e) {
//    catch (NumberParseException e) {
      Mb = null;
    }
    
    return Mb;
  }
  
  public static String setUserNameMask(String Name){
    String name_mask = "";
    for(int i = 0 ; i < Name.length()-2 ; i++){
      name_mask += "*";
    }
    return Name.substring(0, 1)+name_mask+(Name.length() == 2 ? "*" : Name.substring(Name.length()-1, Name.length()));
  }  
  
	/**
	 * 암호화된 카드 번호를 복호화하여 16자리의 카드 번호중 앞뒤 4자리를 제외하고 마스킹 처리 한다.
	 * 
	 * @param enctypedCardNumber
	 * @return
	 */
	public static String getCardNumberFromEncryptedCardNumber(String enctypedCardNumber) {
		if (enctypedCardNumber == null || enctypedCardNumber.isEmpty()) {
			return "";
		}
		try {
			String plain = AES256Cipher.decodeAES256(enctypedCardNumber);

			final int end = Math.max(plain.length() - 4, 0);
			final char[] result = new char[plain.length()];
			for (int i = 0, len = plain.length(); i < len; i++) {
				if (i > 3 && i < end) {
					result[i] = '*';
				} else {
					result[i] = plain.charAt(i);
				}
			}
			return String.valueOf(result);
		} catch (Throwable tw) {
			return "";
		}
	}

}
