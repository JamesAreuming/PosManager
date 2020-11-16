/*
 * Filename	: PasswordSecurityTypeHandler.java
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class PasswordSecurityTypeHandler implements TypeHandler<String> {


  static BCryptPasswordEncoder passwordEncoder;

  public PasswordSecurityTypeHandler()
  {
    passwordEncoder = new BCryptPasswordEncoder();
  }

  @Override
  public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
    ps.setString(i, passwordEncoder.encode((String)parameter));
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
  
  public static void main (String[] agrs ){
    passwordEncoder = new BCryptPasswordEncoder();
    System.out.println("password : " + passwordEncoder.encode("q111111"));
  }
}
