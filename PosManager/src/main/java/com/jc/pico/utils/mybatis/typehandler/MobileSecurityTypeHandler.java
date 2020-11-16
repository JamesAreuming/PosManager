/*
 * Filename	: MobileSecurityTypeHandler.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils.mybatis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.validator.GenericValidator;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.salt.ZeroSaltGenerator;

import com.jc.pico.utils.PosUtil;

public class MobileSecurityTypeHandler implements TypeHandler<String> {

	String encryptionPass;

	public StandardPBEStringEncryptor textEncryptor;

	public MobileSecurityTypeHandler() {
		textEncryptor = new StandardPBEStringEncryptor();
		textEncryptor.setProvider(new BouncyCastleProvider());
		textEncryptor.setSaltGenerator(new ZeroSaltGenerator());
		textEncryptor.setAlgorithm("PBEWITHSHA256AND256BITAES-CBC-BC");
		textEncryptor.setPassword("377EE5CFA8B82EF2469CDE111BCBASD5");
	}

	@Override
	public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
		if(GenericValidator.isBlankOrNull(parameter)) {
			ps.setString(i, parameter);
		} else {
			ps.setString(i, textEncryptor.encrypt(parameter.replace("-", "")));
		}			
	}

	@Override
	public String getResult(ResultSet rs, String columnName) throws SQLException {
		return decrypt(rs.getString(columnName));
	}

	@Override
	public String getResult(ResultSet rs, int columnIndex) throws SQLException {
		return decrypt(rs.getString(columnIndex));
	}

	@Override
	public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
		return decrypt(cs.getString(columnIndex));
	}

	/**
	 * 문자열을 받아 Decrypt 한 문자열로 변환하여 리턴
	 * 
	 * @param org
	 *            암호화된 문자열
	 * @return 복호화된 문자열
	 * @author green, 2016.07.14
	 * @see 사용예: {@link PosUtil#decryptMobileNumber(String)}
	 */
	public String decrypt(String org) {
		return (!GenericValidator.isBlankOrNull(org) ? textEncryptor.decrypt(org) : org);
	}

	public static void main(String[] agrs) {
		StandardPBEStringEncryptor textEncryptor = new StandardPBEStringEncryptor();
		textEncryptor.setProvider(new BouncyCastleProvider());
		textEncryptor.setSaltGenerator(new ZeroSaltGenerator());
		textEncryptor.setAlgorithm("PBEWITHSHA256AND256BITAES-CBC-BC");
		textEncryptor.setPassword("377EE5CFA8B82EF2469CDE111BCBASD5");
		System.out.println("dd1 : " + textEncryptor.decrypt("UV4nGM3TTf/xlfsOHdpxug=="));
	}
}
