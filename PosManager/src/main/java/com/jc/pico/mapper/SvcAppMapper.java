package com.jc.pico.mapper;

import com.jc.pico.bean.SvcApp;
import com.jc.pico.bean.SvcAppExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface SvcAppMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_app
     *
     * @mbggenerated Wed Sep 07 09:34:22 KST 2016
     */
    int countByExample(SvcAppExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_app
     *
     * @mbggenerated Wed Sep 07 09:34:22 KST 2016
     */
    int deleteByExample(SvcAppExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_app
     *
     * @mbggenerated Wed Sep 07 09:34:22 KST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_app
     *
     * @mbggenerated Wed Sep 07 09:34:22 KST 2016
     */
    int insert(SvcApp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_app
     *
     * @mbggenerated Wed Sep 07 09:34:22 KST 2016
     */
    int insertSelective(SvcApp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_app
     *
     * @mbggenerated Wed Sep 07 09:34:22 KST 2016
     */
    List<SvcApp> selectByExampleWithRowbounds(SvcAppExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_app
     *
     * @mbggenerated Wed Sep 07 09:34:22 KST 2016
     */
    List<SvcApp> selectByExample(SvcAppExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_app
     *
     * @mbggenerated Wed Sep 07 09:34:22 KST 2016
     */
    SvcApp selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_app
     *
     * @mbggenerated Wed Sep 07 09:34:22 KST 2016
     */
    int updateByExampleSelective(@Param("record") SvcApp record, @Param("example") SvcAppExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_app
     *
     * @mbggenerated Wed Sep 07 09:34:22 KST 2016
     */
    int updateByExample(@Param("record") SvcApp record, @Param("example") SvcAppExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_app
     *
     * @mbggenerated Wed Sep 07 09:34:22 KST 2016
     */
    int updateByPrimaryKeySelective(SvcApp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_app
     *
     * @mbggenerated Wed Sep 07 09:34:22 KST 2016
     */
    int updateByPrimaryKey(SvcApp record);
}