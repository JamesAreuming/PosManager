package com.jc.pico.mapper;

import com.jc.pico.bean.SvcMailLog;
import com.jc.pico.bean.SvcMailLogExample;
import com.jc.pico.bean.SvcMailLogWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface SvcMailLogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_mail_log
     *
     * @mbggenerated Fri Nov 25 16:54:59 KST 2016
     */
    int countByExample(SvcMailLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_mail_log
     *
     * @mbggenerated Fri Nov 25 16:54:59 KST 2016
     */
    int deleteByExample(SvcMailLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_mail_log
     *
     * @mbggenerated Fri Nov 25 16:54:59 KST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_mail_log
     *
     * @mbggenerated Fri Nov 25 16:54:59 KST 2016
     */
    int insert(SvcMailLogWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_mail_log
     *
     * @mbggenerated Fri Nov 25 16:54:59 KST 2016
     */
    int insertSelective(SvcMailLogWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_mail_log
     *
     * @mbggenerated Fri Nov 25 16:54:59 KST 2016
     */
    List<SvcMailLogWithBLOBs> selectByExampleWithBLOBsWithRowbounds(SvcMailLogExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_mail_log
     *
     * @mbggenerated Fri Nov 25 16:54:59 KST 2016
     */
    List<SvcMailLogWithBLOBs> selectByExampleWithBLOBs(SvcMailLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_mail_log
     *
     * @mbggenerated Fri Nov 25 16:54:59 KST 2016
     */
    List<SvcMailLog> selectByExampleWithRowbounds(SvcMailLogExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_mail_log
     *
     * @mbggenerated Fri Nov 25 16:54:59 KST 2016
     */
    List<SvcMailLog> selectByExample(SvcMailLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_mail_log
     *
     * @mbggenerated Fri Nov 25 16:54:59 KST 2016
     */
    SvcMailLogWithBLOBs selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_mail_log
     *
     * @mbggenerated Fri Nov 25 16:54:59 KST 2016
     */
    int updateByExampleSelective(@Param("record") SvcMailLogWithBLOBs record, @Param("example") SvcMailLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_mail_log
     *
     * @mbggenerated Fri Nov 25 16:54:59 KST 2016
     */
    int updateByExampleWithBLOBs(@Param("record") SvcMailLogWithBLOBs record, @Param("example") SvcMailLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_mail_log
     *
     * @mbggenerated Fri Nov 25 16:54:59 KST 2016
     */
    int updateByExample(@Param("record") SvcMailLog record, @Param("example") SvcMailLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_mail_log
     *
     * @mbggenerated Fri Nov 25 16:54:59 KST 2016
     */
    int updateByPrimaryKeySelective(SvcMailLogWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_mail_log
     *
     * @mbggenerated Fri Nov 25 16:54:59 KST 2016
     */
    int updateByPrimaryKeyWithBLOBs(SvcMailLogWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_mail_log
     *
     * @mbggenerated Fri Nov 25 16:54:59 KST 2016
     */
    int updateByPrimaryKey(SvcMailLog record);
}