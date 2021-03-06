package com.jc.pico.mapper;

import com.jc.pico.bean.SvcItem;
import com.jc.pico.bean.SvcItemExample;
import com.jc.pico.utils.bean.SvcOrderItemExtended;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface SvcItemMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_item
     *
     * @mbggenerated Fri Sep 23 19:04:40 KST 2016
     */
    int countByExample(SvcItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_item
     *
     * @mbggenerated Fri Sep 23 19:04:40 KST 2016
     */
    int deleteByExample(SvcItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_item
     *
     * @mbggenerated Fri Sep 23 19:04:40 KST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_item
     *
     * @mbggenerated Fri Sep 23 19:04:40 KST 2016
     */
    int insert(SvcItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_item
     *
     * @mbggenerated Fri Sep 23 19:04:40 KST 2016
     */
    int insertSelective(SvcItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_item
     *
     * @mbggenerated Fri Sep 23 19:04:40 KST 2016
     */
    List<SvcItem> selectByExampleWithBLOBsWithRowbounds(SvcItemExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_item
     *
     * @mbggenerated Fri Sep 23 19:04:40 KST 2016
     */
    List<SvcItem> selectByExampleWithBLOBs(SvcItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_item
     *
     * @mbggenerated Fri Sep 23 19:04:40 KST 2016
     */
    List<SvcItem> selectByExampleWithRowbounds(SvcItemExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_item
     *
     * @mbggenerated Fri Sep 23 19:04:40 KST 2016
     */
    List<SvcItem> selectByExample(SvcItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_item
     *
     * @mbggenerated Fri Sep 23 19:04:40 KST 2016
     */
    SvcItem selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_item
     *
     * @mbggenerated Fri Sep 23 19:04:40 KST 2016
     */
    int updateByExampleSelective(@Param("record") SvcItem record, @Param("example") SvcItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_item
     *
     * @mbggenerated Fri Sep 23 19:04:40 KST 2016
     */
    int updateByExampleWithBLOBs(@Param("record") SvcItem record, @Param("example") SvcItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_item
     *
     * @mbggenerated Fri Sep 23 19:04:40 KST 2016
     */
    int updateByExample(@Param("record") SvcItem record, @Param("example") SvcItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_item
     *
     * @mbggenerated Fri Sep 23 19:04:40 KST 2016
     */
    int updateByPrimaryKeySelective(SvcItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_item
     *
     * @mbggenerated Fri Sep 23 19:04:40 KST 2016
     */
    int updateByPrimaryKeyWithBLOBs(SvcItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_item
     *
     * @mbggenerated Fri Sep 23 19:04:40 KST 2016
     */
    int updateByPrimaryKey(SvcItem record);

	List<SvcItem> selectByOrderItemList(List<SvcOrderItemExtended> svcOrderItemExtendeds);
}