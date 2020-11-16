/*
 * Filename	: LombokPlugin.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.configuration.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * A MyBatis Generator plugin to use Lombok's @Data annoation
 * instead of getters and setters
 *
 * @author Paolo Predonzani (http://softwareloop.com/)
 */
public class LombokPlugin extends PluginAdapter {

    private FullyQualifiedJavaType dataAnnotation;

    /**
     * LombokPlugin contructor
     */
    public LombokPlugin() {
        dataAnnotation = new FullyQualifiedJavaType("lombok.Data");
    }

    /**
     * @param warnings
     * @return always true
     */
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * Intercepts base record class generation
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        addDataAnnotation(topLevelClass);
        return true;
    }

    /**
     * Intercepts primary key class generation
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        addDataAnnotation(topLevelClass);
        return true;
    }

    /**
     * Intercepts "record with blob" class generation
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelRecordWithBLOBsClassGenerated(
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addDataAnnotation(topLevelClass);
        return true;
    }

    /**
     * Adds the @Data lombok import and annotation to the class
     *
     * @param topLevelClass
     */
    protected void addDataAnnotation(TopLevelClass topLevelClass) {
        topLevelClass.addImportedType(new FullyQualifiedJavaType("com.fasterxml.jackson.annotation.JsonIgnoreProperties"));
        topLevelClass.addAnnotation("@JsonIgnoreProperties(ignoreUnknown = true)");
    }

}
