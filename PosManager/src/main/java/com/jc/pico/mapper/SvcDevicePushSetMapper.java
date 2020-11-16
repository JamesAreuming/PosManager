package com.jc.pico.mapper;

import com.jc.pico.bean.SvcDevicePushSet;
import com.jc.pico.bean.SvcDevicePushSetExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface SvcDevicePushSetMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_device_push_set
     *
     * @mbggenerated Thu Oct 27 15:26:17 KST 2016
     */
    int countByExample(SvcDevicePushSetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_device_push_set
     *
     * @mbggenerated Thu Oct 27 15:26:17 KST 2016
     */
    int deleteByExample(SvcDevicePushSetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_device_push_set
     *
     * @mbggenerated Thu Oct 27 15:26:17 KST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_device_push_set
     *
     * @mbggenerated Thu Oct 27 15:26:17 KST 2016
     */
    int insert(SvcDevicePushSet record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_device_push_set
     *
     * @mbggenerated Thu Oct 27 15:26:17 KST 2016
     */
    int insertSelective(SvcDevicePushSet record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_device_push_set
     *
     * @mbggenerated Thu Oct 27 15:26:17 KST 2016
     */
    List<SvcDevicePushSet> selectByExampleWithRowbounds(SvcDevicePushSetExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_device_push_set
     *
     * @mbggenerated Thu Oct 27 15:26:17 KST 2016
     */
    List<SvcDevicePushSet> selectByExample(SvcDevicePushSetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_device_push_set
     *
     * @mbggenerated Thu Oct 27 15:26:17 KST 2016
     */
    SvcDevicePushSet selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_device_push_set
     *
     * @mbggenerated Thu Oct 27 15:26:17 KST 2016
     */
    int updateByExampleSelective(@Param("record") SvcDevicePushSet record, @Param("example") SvcDevicePushSetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_device_push_set
     *
     * @mbggenerated Thu Oct 27 15:26:17 KST 2016
     */
    int updateByExample(@Param("record") SvcDevicePushSet record, @Param("example") SvcDevicePushSetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_device_push_set
     *
     * @mbggenerated Thu Oct 27 15:26:17 KST 2016
     */
    int updateByPrimaryKeySelective(SvcDevicePushSet record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_device_push_set
     *
     * @mbggenerated Thu Oct 27 15:26:17 KST 2016
     */
    int updateByPrimaryKey(SvcDevicePushSet record);
}