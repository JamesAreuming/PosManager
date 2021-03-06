package com.jc.pico.mapper;

import com.jc.pico.bean.SvcPushTemplate;
import com.jc.pico.bean.SvcPushTemplateExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface SvcPushTemplateMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PUSH_TEMPLATE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int countByExample(SvcPushTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PUSH_TEMPLATE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int deleteByExample(SvcPushTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PUSH_TEMPLATE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PUSH_TEMPLATE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int insert(SvcPushTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PUSH_TEMPLATE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int insertSelective(SvcPushTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PUSH_TEMPLATE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    List<SvcPushTemplate> selectByExampleWithBLOBsWithRowbounds(SvcPushTemplateExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PUSH_TEMPLATE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    List<SvcPushTemplate> selectByExampleWithBLOBs(SvcPushTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PUSH_TEMPLATE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    List<SvcPushTemplate> selectByExampleWithRowbounds(SvcPushTemplateExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PUSH_TEMPLATE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    List<SvcPushTemplate> selectByExample(SvcPushTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PUSH_TEMPLATE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    SvcPushTemplate selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PUSH_TEMPLATE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByExampleSelective(@Param("record") SvcPushTemplate record, @Param("example") SvcPushTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PUSH_TEMPLATE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByExampleWithBLOBs(@Param("record") SvcPushTemplate record, @Param("example") SvcPushTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PUSH_TEMPLATE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByExample(@Param("record") SvcPushTemplate record, @Param("example") SvcPushTemplateExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PUSH_TEMPLATE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByPrimaryKeySelective(SvcPushTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PUSH_TEMPLATE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByPrimaryKeyWithBLOBs(SvcPushTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PUSH_TEMPLATE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByPrimaryKey(SvcPushTemplate record);
}