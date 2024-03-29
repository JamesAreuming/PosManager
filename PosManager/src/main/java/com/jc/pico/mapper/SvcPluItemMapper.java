package com.jc.pico.mapper;

import com.jc.pico.bean.SvcPluItem;
import com.jc.pico.bean.SvcPluItemExample;
import com.jc.pico.utils.bean.SingleMap;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface SvcPluItemMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PLU_ITEM
     *
     * @mbggenerated Wed Jul 06 14:52:10 KST 2016
     */
    int countByExample(SvcPluItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PLU_ITEM
     *
     * @mbggenerated Wed Jul 06 14:52:10 KST 2016
     */
    int deleteByExample(SvcPluItemExample example);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PLU_ITEM
     *
     * @mbggenerated Wed Jul 06 14:52:10 KST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PLU_ITEM
     *
     * @mbggenerated Wed Jul 06 14:52:10 KST 2016
     */
    int insert(SvcPluItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PLU_ITEM
     *
     * @mbggenerated Wed Jul 06 14:52:10 KST 2016
     */
    int insertSelective(SvcPluItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PLU_ITEM
     *
     * @mbggenerated Wed Jul 06 14:52:10 KST 2016
     */
    List<SvcPluItem> selectByExampleWithRowbounds(SvcPluItemExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PLU_ITEM
     *
     * @mbggenerated Wed Jul 06 14:52:10 KST 2016
     */
    List<SvcPluItem> selectByExample(SvcPluItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PLU_ITEM
     *
     * @mbggenerated Wed Jul 06 14:52:10 KST 2016
     */
    SvcPluItem selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PLU_ITEM
     *
     * @mbggenerated Wed Jul 06 14:52:10 KST 2016
     */
    int updateByExampleSelective(@Param("record") SvcPluItem record, @Param("example") SvcPluItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PLU_ITEM
     *
     * @mbggenerated Wed Jul 06 14:52:10 KST 2016
     */
    int updateByExample(@Param("record") SvcPluItem record, @Param("example") SvcPluItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PLU_ITEM
     *
     * @mbggenerated Wed Jul 06 14:52:10 KST 2016
     */
    int updateByPrimaryKeySelective(SvcPluItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_PLU_ITEM
     *
     * @mbggenerated Wed Jul 06 14:52:10 KST 2016
     */
    int updateByPrimaryKey(SvcPluItem record);

}