package com.jc.pico.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SvcCctvLogDeviceMapping {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_cctv_log_device_mapping.ID
     *
     * @mbggenerated Fri Aug 19 21:24:05 KST 2016
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_cctv_log_device_mapping.LOG_ID
     *
     * @mbggenerated Fri Aug 19 21:24:05 KST 2016
     */
    private Long logId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_cctv_log_device_mapping.DEVICE_ID
     *
     * @mbggenerated Fri Aug 19 21:24:05 KST 2016
     */
    private Long deviceId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_cctv_log_device_mapping.IS_DEL
     *
     * @mbggenerated Fri Aug 19 21:24:05 KST 2016
     */
    private Boolean isDel;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_cctv_log_device_mapping.IS_READ
     *
     * @mbggenerated Fri Aug 19 21:24:05 KST 2016
     */
    private Boolean isRead;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_cctv_log_device_mapping.CREATED
     *
     * @mbggenerated Fri Aug 19 21:24:05 KST 2016
     */
    private Date created;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_cctv_log_device_mapping.UPDATED
     *
     * @mbggenerated Fri Aug 19 21:24:05 KST 2016
     */
    private Date updated;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_cctv_log_device_mapping.ID
     *
     * @return the value of tb_svc_cctv_log_device_mapping.ID
     *
     * @mbggenerated Fri Aug 19 21:24:05 KST 2016
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_cctv_log_device_mapping.ID
     *
     * @param id the value for tb_svc_cctv_log_device_mapping.ID
     *
     * @mbggenerated Fri Aug 19 21:24:05 KST 2016
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_cctv_log_device_mapping.LOG_ID
     *
     * @return the value of tb_svc_cctv_log_device_mapping.LOG_ID
     *
     * @mbggenerated Fri Aug 19 21:24:05 KST 2016
     */
    public Long getLogId() {
        return logId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_cctv_log_device_mapping.LOG_ID
     *
     * @param logId the value for tb_svc_cctv_log_device_mapping.LOG_ID
     *
     * @mbggenerated Fri Aug 19 21:24:05 KST 2016
     */
    public void setLogId(Long logId) {
        this.logId = logId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_cctv_log_device_mapping.DEVICE_ID
     *
     * @return the value of tb_svc_cctv_log_device_mapping.DEVICE_ID
     *
     * @mbggenerated Fri Aug 19 21:24:05 KST 2016
     */
    public Long getDeviceId() {
        return deviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_cctv_log_device_mapping.DEVICE_ID
     *
     * @param deviceId the value for tb_svc_cctv_log_device_mapping.DEVICE_ID
     *
     * @mbggenerated Fri Aug 19 21:24:05 KST 2016
     */
    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_cctv_log_device_mapping.IS_DEL
     *
     * @return the value of tb_svc_cctv_log_device_mapping.IS_DEL
     *
     * @mbggenerated Fri Aug 19 21:24:05 KST 2016
     */
    public Boolean getIsDel() {
        return isDel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_cctv_log_device_mapping.IS_DEL
     *
     * @param isDel the value for tb_svc_cctv_log_device_mapping.IS_DEL
     *
     * @mbggenerated Fri Aug 19 21:24:05 KST 2016
     */
    public void setIsDel(Boolean isDel) {
        this.isDel = isDel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_cctv_log_device_mapping.IS_READ
     *
     * @return the value of tb_svc_cctv_log_device_mapping.IS_READ
     *
     * @mbggenerated Fri Aug 19 21:24:05 KST 2016
     */
    public Boolean getIsRead() {
        return isRead;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_cctv_log_device_mapping.IS_READ
     *
     * @param isRead the value for tb_svc_cctv_log_device_mapping.IS_READ
     *
     * @mbggenerated Fri Aug 19 21:24:05 KST 2016
     */
    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_cctv_log_device_mapping.CREATED
     *
     * @return the value of tb_svc_cctv_log_device_mapping.CREATED
     *
     * @mbggenerated Fri Aug 19 21:24:05 KST 2016
     */
    public Date getCreated() {
        return created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_cctv_log_device_mapping.CREATED
     *
     * @param created the value for tb_svc_cctv_log_device_mapping.CREATED
     *
     * @mbggenerated Fri Aug 19 21:24:05 KST 2016
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_cctv_log_device_mapping.UPDATED
     *
     * @return the value of tb_svc_cctv_log_device_mapping.UPDATED
     *
     * @mbggenerated Fri Aug 19 21:24:05 KST 2016
     */
    public Date getUpdated() {
        return updated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_cctv_log_device_mapping.UPDATED
     *
     * @param updated the value for tb_svc_cctv_log_device_mapping.UPDATED
     *
     * @mbggenerated Fri Aug 19 21:24:05 KST 2016
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}