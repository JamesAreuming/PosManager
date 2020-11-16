/*
 * Filename	: OnewaySecurityTypeHandler.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils.mybatis.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OnewaySecurityTypeHandler implements TypeHandler<String> {

  String encryptionPass;

  ConfigurablePasswordEncryptor passwordEncryptor;

  public OnewaySecurityTypeHandler()
  {
    passwordEncryptor = new ConfigurablePasswordEncryptor();
    passwordEncryptor.setAlgorithm("SHA-256");
    passwordEncryptor.setPlainDigest(true);
  }

  @Override
  public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
    ps.setString(i, passwordEncryptor.encryptPassword(((String)parameter)));
  }

  @Override
  public String getResult(ResultSet rs, String columnName) throws SQLException {
    return   rs.getString(columnName);
  }

  @Override
  public String getResult(ResultSet rs, int columnIndex) throws SQLException {
    return rs.getString(columnIndex);
  }

  @Override
  public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
    return cs.getString(columnIndex);
  }

}
