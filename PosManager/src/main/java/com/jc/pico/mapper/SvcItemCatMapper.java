package com.jc.pico.mapper;

import com.jc.pico.bean.SvcItemCat;
import com.jc.pico.bean.SvcItemCatExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface SvcItemCatMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_ITEM_CAT
     *
     * @mbggenerated Wed Jul 06 14:52:10 KST 2016
     */
    int countByExample(SvcItemCatExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_ITEM_CAT
     *
     * @mbggenerated Wed Jul 06 14:52:10 KST 2016
     */
    int deleteByExample(SvcItemCatExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_ITEM_CAT
     *
     * @mbggenerated Wed Jul 06 14:52:10 KST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_ITEM_CAT
     *
     * @mbggenerated Wed Jul 06 14:52:10 KST 2016
     */
    int insert(SvcItemCat record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_ITEM_CAT
     *
     * @mbggenerated Wed Jul 06 14:52:10 KST 2016
     */
    int insertSelective(SvcItemCat record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_ITEM_CAT
     *
     * @mbggenerated Wed Jul 06 14:52:10 KST 2016
     */
    List<SvcItemCat> selectByExampleWithRowbounds(SvcItemCatExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_ITEM_CAT
     *
     * @mbggenerated Wed Jul 06 14:52:10 KST 2016
     */
    List<SvcItemCat> selectByExample(SvcItemCatExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_ITEM_CAT
     *
     * @mbggenerated Wed Jul 06 14:52:10 KST 2016
     */
    SvcItemCat selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_ITEM_CAT
     *
     * @mbggenerated Wed Jul 06 14:52:10 KST 2016
     */
    int updateByExampleSelective(@Param("record") SvcItemCat record, @Param("example") SvcItemCatExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_ITEM_CAT
     *
     * @mbggenerated Wed Jul 06 14:52:10 KST 2016
     */
    int updateByExample(@Param("record") SvcItemCat record, @Param("example") SvcItemCatExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_ITEM_CAT
     *
     * @mbggenerated Wed Jul 06 14:52:10 KST 2016
     */
    int updateByPrimaryKeySelective(SvcItemCat record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_ITEM_CAT
     *
     * @mbggenerated Wed Jul 06 14:52:10 KST 2016
     */
    int updateByPrimaryKey(SvcItemCat record);
}