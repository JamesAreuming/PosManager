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

import com.jc.pico.utils.MaskUtil;

public class MobileSecurityMaskTypeHandler implements TypeHandler<String> {
  private static StandardPBEStringEncryptor textEncryptor;

  private StandardPBEStringEncryptor encryptor;

  public MobileSecurityMaskTypeHandler() {
    encryptor = getTextEncryptor();
  }

  private StandardPBEStringEncryptor getTextEncryptor() {
    if (textEncryptor == null) {
      textEncryptor = new StandardPBEStringEncryptor();
      textEncryptor.setProvider(new BouncyCastleProvider());
      textEncryptor.setSaltGenerator(new ZeroSaltGenerator());
      textEncryptor.setAlgorithm("PBEWITHSHA256AND256BITAES-CBC-BC");
      textEncryptor.setPassword("377EE5CFA8B82EF2469CDE111BCBASD5");
    }
    return textEncryptor;
  }

  @Override
  public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
    if(parameter != null)
      ps.setString(i, encryptor.encrypt(parameter.replace("-", "")));
  }

  @Override
  public String getResult(ResultSet rs, String columnName) throws SQLException {
    return !GenericValidator.isBlankOrNull(rs.getString(columnName)) ? MaskUtil.setMobileMask(encryptor.decrypt(rs.getString(columnName))) : null;
  }

  @Override
  public String getResult(ResultSet rs, int columnIndex) throws SQLException {
    return !GenericValidator.isBlankOrNull(rs.getString(columnIndex)) ? MaskUtil.setMobileMask(encryptor.decrypt(rs.getString(columnIndex))) : null;
  } 

  @Override
  public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
    return !GenericValidator.isBlankOrNull(cs.getString(columnIndex)) ? MaskUtil.setMobileMask(encryptor.decrypt(cs.getString(columnIndex))) : null;
  }

}
