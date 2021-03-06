package com.jc.pico.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SvcSmsAuth {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_sms_auth.ID
     *
     * @mbggenerated Tue Aug 30 19:57:04 KST 2016
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_sms_auth.SMS_LOG_ID
     *
     * @mbggenerated Tue Aug 30 19:57:04 KST 2016
     */
    private Long smsLogId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_sms_auth.MB_COUNTRY_CD
     *
     * @mbggenerated Tue Aug 30 19:57:04 KST 2016
     */
    private String mbCountryCd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_sms_auth.MB
     *
     * @mbggenerated Tue Aug 30 19:57:04 KST 2016
     */
    private String mb;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_sms_auth.AUTH_CODE
     *
     * @mbggenerated Tue Aug 30 19:57:04 KST 2016
     */
    private String authCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_sms_auth.AUTH_ST
     *
     * @mbggenerated Tue Aug 30 19:57:04 KST 2016
     */
    private String authSt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_sms_auth.CREATED
     *
     * @mbggenerated Tue Aug 30 19:57:04 KST 2016
     */
    private Date created;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_sms_auth.UPDATED
     *
     * @mbggenerated Tue Aug 30 19:57:04 KST 2016
     */
    private Date updated;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_sms_auth.ID
     *
     * @return the value of tb_svc_sms_auth.ID
     *
     * @mbggenerated Tue Aug 30 19:57:04 KST 2016
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_sms_auth.ID
     *
     * @param id the value for tb_svc_sms_auth.ID
     *
     * @mbggenerated Tue Aug 30 19:57:04 KST 2016
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_sms_auth.SMS_LOG_ID
     *
     * @return the value of tb_svc_sms_auth.SMS_LOG_ID
     *
     * @mbggenerated Tue Aug 30 19:57:04 KST 2016
     */
    public Long getSmsLogId() {
        return smsLogId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_sms_auth.SMS_LOG_ID
     *
     * @param smsLogId the value for tb_svc_sms_auth.SMS_LOG_ID
     *
     * @mbggenerated Tue Aug 30 19:57:04 KST 2016
     */
    public void setSmsLogId(Long smsLogId) {
        this.smsLogId = smsLogId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_sms_auth.MB_COUNTRY_CD
     *
     * @return the value of tb_svc_sms_auth.MB_COUNTRY_CD
     *
     * @mbggenerated Tue Aug 30 19:57:04 KST 2016
     */
    public String getMbCountryCd() {
        return mbCountryCd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_sms_auth.MB_COUNTRY_CD
     *
     * @param mbCountryCd the value for tb_svc_sms_auth.MB_COUNTRY_CD
     *
     * @mbggenerated Tue Aug 30 19:57:04 KST 2016
     */
    public void setMbCountryCd(String mbCountryCd) {
        this.mbCountryCd = mbCountryCd == null ? null : mbCountryCd.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_sms_auth.MB
     *
     * @return the value of tb_svc_sms_auth.MB
     *
     * @mbggenerated Tue Aug 30 19:57:04 KST 2016
     */
    public String getMb() {
        return mb;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_sms_auth.MB
     *
     * @param mb the value for tb_svc_sms_auth.MB
     *
     * @mbggenerated Tue Aug 30 19:57:04 KST 2016
     */
    public void setMb(String mb) {
        this.mb = mb == null ? null : mb.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_sms_auth.AUTH_CODE
     *
     * @return the value of tb_svc_sms_auth.AUTH_CODE
     *
     * @mbggenerated Tue Aug 30 19:57:04 KST 2016
     */
    public String getAuthCode() {
        return authCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_sms_auth.AUTH_CODE
     *
     * @param authCode the value for tb_svc_sms_auth.AUTH_CODE
     *
     * @mbggenerated Tue Aug 30 19:57:04 KST 2016
     */
    public void setAuthCode(String authCode) {
        this.authCode = authCode == null ? null : authCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_sms_auth.AUTH_ST
     *
     * @return the value of tb_svc_sms_auth.AUTH_ST
     *
     * @mbggenerated Tue Aug 30 19:57:04 KST 2016
     */
    public String getAuthSt() {
        return authSt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_sms_auth.AUTH_ST
     *
     * @param authSt the value for tb_svc_sms_auth.AUTH_ST
     *
     * @mbggenerated Tue Aug 30 19:57:04 KST 2016
     */
    public void setAuthSt(String authSt) {
        this.authSt = authSt == null ? null : authSt.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_sms_auth.CREATED
     *
     * @return the value of tb_svc_sms_auth.CREATED
     *
     * @mbggenerated Tue Aug 30 19:57:04 KST 2016
     */
    public Date getCreated() {
        return created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_sms_auth.CREATED
     *
     * @param created the value for tb_svc_sms_auth.CREATED
     *
     * @mbggenerated Tue Aug 30 19:57:04 KST 2016
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_sms_auth.UPDATED
     *
     * @return the value of tb_svc_sms_auth.UPDATED
     *
     * @mbggenerated Tue Aug 30 19:57:04 KST 2016
     */
    public Date getUpdated() {
        return updated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_sms_auth.UPDATED
     *
     * @param updated the value for tb_svc_sms_auth.UPDATED
     *
     * @mbggenerated Tue Aug 30 19:57:04 KST 2016
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}