package com.jc.pico.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDevice {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user_device.ID
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user_device.USER_ID
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    private Long userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user_device.IS_ALIVE
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    private Boolean isAlive;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user_device.IS_LAST_LOGIN
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    private Boolean isLastLogin;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user_device.OS
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    private String os;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user_device.DEVICE_ID
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    private String deviceId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user_device.UUID
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    private String uuid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user_device.PUSH_ID
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    private String pushId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user_device.LOCALE
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    private String locale;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user_device.CREATED
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    private Date created;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user_device.UPDATED
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    private Date updated;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user_device.ID
     *
     * @return the value of tb_user_device.ID
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user_device.ID
     *
     * @param id the value for tb_user_device.ID
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user_device.USER_ID
     *
     * @return the value of tb_user_device.USER_ID
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user_device.USER_ID
     *
     * @param userId the value for tb_user_device.USER_ID
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user_device.IS_ALIVE
     *
     * @return the value of tb_user_device.IS_ALIVE
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    public Boolean getIsAlive() {
        return isAlive;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user_device.IS_ALIVE
     *
     * @param isAlive the value for tb_user_device.IS_ALIVE
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    public void setIsAlive(Boolean isAlive) {
        this.isAlive = isAlive;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user_device.IS_LAST_LOGIN
     *
     * @return the value of tb_user_device.IS_LAST_LOGIN
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    public Boolean getIsLastLogin() {
        return isLastLogin;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user_device.IS_LAST_LOGIN
     *
     * @param isLastLogin the value for tb_user_device.IS_LAST_LOGIN
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    public void setIsLastLogin(Boolean isLastLogin) {
        this.isLastLogin = isLastLogin;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user_device.OS
     *
     * @return the value of tb_user_device.OS
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    public String getOs() {
        return os;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user_device.OS
     *
     * @param os the value for tb_user_device.OS
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    public void setOs(String os) {
        this.os = os == null ? null : os.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user_device.DEVICE_ID
     *
     * @return the value of tb_user_device.DEVICE_ID
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user_device.DEVICE_ID
     *
     * @param deviceId the value for tb_user_device.DEVICE_ID
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user_device.UUID
     *
     * @return the value of tb_user_device.UUID
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user_device.UUID
     *
     * @param uuid the value for tb_user_device.UUID
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user_device.PUSH_ID
     *
     * @return the value of tb_user_device.PUSH_ID
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    public String getPushId() {
        return pushId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user_device.PUSH_ID
     *
     * @param pushId the value for tb_user_device.PUSH_ID
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    public void setPushId(String pushId) {
        this.pushId = pushId == null ? null : pushId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user_device.LOCALE
     *
     * @return the value of tb_user_device.LOCALE
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    public String getLocale() {
        return locale;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user_device.LOCALE
     *
     * @param locale the value for tb_user_device.LOCALE
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    public void setLocale(String locale) {
        this.locale = locale == null ? null : locale.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user_device.CREATED
     *
     * @return the value of tb_user_device.CREATED
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    public Date getCreated() {
        return created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user_device.CREATED
     *
     * @param created the value for tb_user_device.CREATED
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user_device.UPDATED
     *
     * @return the value of tb_user_device.UPDATED
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    public Date getUpdated() {
        return updated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user_device.UPDATED
     *
     * @param updated the value for tb_user_device.UPDATED
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}