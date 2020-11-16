package com.jc.pico.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SvcMessageTemplate {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_message_template.ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_message_template.SERVICE_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Long serviceId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_message_template.MESSAGE_TP
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private String messageTp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_message_template.USED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Boolean used;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_message_template.TITLE
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private String title;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_message_template.CREATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Date created;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_message_template.UPDATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Date updated;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_message_template.ID
     *
     * @return the value of tb_svc_message_template.ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_message_template.ID
     *
     * @param id the value for tb_svc_message_template.ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_message_template.SERVICE_ID
     *
     * @return the value of tb_svc_message_template.SERVICE_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Long getServiceId() {
        return serviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_message_template.SERVICE_ID
     *
     * @param serviceId the value for tb_svc_message_template.SERVICE_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_message_template.MESSAGE_TP
     *
     * @return the value of tb_svc_message_template.MESSAGE_TP
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public String getMessageTp() {
        return messageTp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_message_template.MESSAGE_TP
     *
     * @param messageTp the value for tb_svc_message_template.MESSAGE_TP
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setMessageTp(String messageTp) {
        this.messageTp = messageTp == null ? null : messageTp.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_message_template.USED
     *
     * @return the value of tb_svc_message_template.USED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Boolean getUsed() {
        return used;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_message_template.USED
     *
     * @param used the value for tb_svc_message_template.USED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setUsed(Boolean used) {
        this.used = used;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_message_template.TITLE
     *
     * @return the value of tb_svc_message_template.TITLE
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_message_template.TITLE
     *
     * @param title the value for tb_svc_message_template.TITLE
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_message_template.CREATED
     *
     * @return the value of tb_svc_message_template.CREATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Date getCreated() {
        return created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_message_template.CREATED
     *
     * @param created the value for tb_svc_message_template.CREATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_message_template.UPDATED
     *
     * @return the value of tb_svc_message_template.UPDATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Date getUpdated() {
        return updated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_message_template.UPDATED
     *
     * @param updated the value for tb_svc_message_template.UPDATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}