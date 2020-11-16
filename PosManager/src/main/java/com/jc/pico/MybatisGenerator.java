/*
 * Filename	: MybatisGenerator.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico;

import com.google.common.collect.Lists;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.List;

/**
 * 
 */
public class MybatisGenerator {
  public static void main(String[] args) throws Exception {
    System.out.println("base dir = " + System.getProperty("user.dir"));
    List<String> warnings = Lists.newArrayList();
    boolean overwrite = true;
    File configFile = new File("src/main/resources/mybatis-generator.xml");
    ConfigurationParser cp = new ConfigurationParser(warnings);
    Configuration config = cp.parseConfiguration(configFile);
    DefaultShellCallback callback = new DefaultShellCallback(overwrite);
    MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
    myBatisGenerator.generate(null);
  }
}