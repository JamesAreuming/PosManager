package com.jc.pico.mapper;

import com.jc.pico.bean.BaseCalendar;
import com.jc.pico.bean.BaseCalendarExample;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface BaseCalendarMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_base_calendar
     *
     * @mbggenerated Thu Aug 11 09:06:41 KST 2016
     */
    int countByExample(BaseCalendarExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_base_calendar
     *
     * @mbggenerated Thu Aug 11 09:06:41 KST 2016
     */
    int deleteByExample(BaseCalendarExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_base_calendar
     *
     * @mbggenerated Thu Aug 11 09:06:41 KST 2016
     */
    int deleteByPrimaryKey(Date ymd);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_base_calendar
     *
     * @mbggenerated Thu Aug 11 09:06:41 KST 2016
     */
    int insert(BaseCalendar record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_base_calendar
     *
     * @mbggenerated Thu Aug 11 09:06:41 KST 2016
     */
    int insertSelective(BaseCalendar record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_base_calendar
     *
     * @mbggenerated Thu Aug 11 09:06:41 KST 2016
     */
    List<BaseCalendar> selectByExampleWithRowbounds(BaseCalendarExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_base_calendar
     *
     * @mbggenerated Thu Aug 11 09:06:41 KST 2016
     */
    List<BaseCalendar> selectByExample(BaseCalendarExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_base_calendar
     *
     * @mbggenerated Thu Aug 11 09:06:41 KST 2016
     */
    BaseCalendar selectByPrimaryKey(Date ymd);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_base_calendar
     *
     * @mbggenerated Thu Aug 11 09:06:41 KST 2016
     */
    int updateByExampleSelective(@Param("record") BaseCalendar record, @Param("example") BaseCalendarExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_base_calendar
     *
     * @mbggenerated Thu Aug 11 09:06:41 KST 2016
     */
    int updateByExample(@Param("record") BaseCalendar record, @Param("example") BaseCalendarExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_base_calendar
     *
     * @mbggenerated Thu Aug 11 09:06:41 KST 2016
     */
    int updateByPrimaryKeySelective(BaseCalendar record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_base_calendar
     *
     * @mbggenerated Thu Aug 11 09:06:41 KST 2016
     */
    int updateByPrimaryKey(BaseCalendar record);
}