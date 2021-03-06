package com.jc.pico.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserMenu {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user_menu.ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user_menu.USER_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Long userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user_menu.NAME
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user_menu.URL
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private String url;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user_menu.CREATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Date created;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user_menu.UPDATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Date updated;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user_menu.ID
     *
     * @return the value of tb_user_menu.ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user_menu.ID
     *
     * @param id the value for tb_user_menu.ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user_menu.USER_ID
     *
     * @return the value of tb_user_menu.USER_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user_menu.USER_ID
     *
     * @param userId the value for tb_user_menu.USER_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user_menu.NAME
     *
     * @return the value of tb_user_menu.NAME
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user_menu.NAME
     *
     * @param name the value for tb_user_menu.NAME
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user_menu.URL
     *
     * @return the value of tb_user_menu.URL
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public String getUrl() {
        return url;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user_menu.URL
     *
     * @param url the value for tb_user_menu.URL
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user_menu.CREATED
     *
     * @return the value of tb_user_menu.CREATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Date getCreated() {
        return created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user_menu.CREATED
     *
     * @param created the value for tb_user_menu.CREATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user_menu.UPDATED
     *
     * @return the value of tb_user_menu.UPDATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Date getUpdated() {
        return updated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user_menu.UPDATED
     *
     * @param updated the value for tb_user_menu.UPDATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}