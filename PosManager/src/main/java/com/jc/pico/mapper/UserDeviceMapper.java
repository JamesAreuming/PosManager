package com.jc.pico.mapper;

import com.jc.pico.bean.UserDevice;
import com.jc.pico.bean.UserDeviceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface UserDeviceMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_device
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    int countByExample(UserDeviceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_device
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    int deleteByExample(UserDeviceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_device
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_device
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    int insert(UserDevice record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_device
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    int insertSelective(UserDevice record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_device
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    List<UserDevice> selectByExampleWithRowbounds(UserDeviceExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_device
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    List<UserDevice> selectByExample(UserDeviceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_device
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    UserDevice selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_device
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    int updateByExampleSelective(@Param("record") UserDevice record, @Param("example") UserDeviceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_device
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    int updateByExample(@Param("record") UserDevice record, @Param("example") UserDeviceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_device
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    int updateByPrimaryKeySelective(UserDevice record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_device
     *
     * @mbggenerated Thu Oct 13 16:37:57 MSK 2016
     */
    int updateByPrimaryKey(UserDevice record);
}