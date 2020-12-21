package com.jc.pico.mapper;

import com.jc.pico.bean.SvcSales;
import com.jc.pico.bean.SvcSalesItem;
import com.jc.pico.bean.SvcSalesItemEx;
import com.jc.pico.bean.SvcSalesItemExample;
import com.jc.pico.utils.bean.SingleMap;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface SvcSalesItemMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales_item
     *
     * @mbggenerated Wed Jan 11 14:34:43 KST 2017
     */
    int countByExample(SvcSalesItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales_item
     *
     * @mbggenerated Wed Jan 11 14:34:43 KST 2017
     */
    int deleteByExample(SvcSalesItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales_item
     *
     * @mbggenerated Wed Jan 11 14:34:43 KST 2017
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales_item
     *
     * @mbggenerated Wed Jan 11 14:34:43 KST 2017
     */
    int insert(SvcSalesItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales_item
     *
     * @mbggenerated Wed Jan 11 14:34:43 KST 2017
     */
    int insertSelective(SvcSalesItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales_item
     *
     * @mbggenerated Wed Jan 11 14:34:43 KST 2017
     */
    List<SvcSalesItem> selectByExampleWithRowbounds(SvcSalesItemExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales_item
     *
     * @mbggenerated Wed Jan 11 14:34:43 KST 2017
     */
    List<SvcSalesItem> selectByExample(SvcSalesItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales_item
     *
     * @mbggenerated Wed Jan 11 14:34:43 KST 2017
     */
    SvcSalesItem selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales_item
     *
     * @mbggenerated Wed Jan 11 14:34:43 KST 2017
     */
    int updateByExampleSelective(@Param("record") SvcSalesItem record, @Param("example") SvcSalesItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales_item
     *
     * @mbggenerated Wed Jan 11 14:34:43 KST 2017
     */
    int updateByExample(@Param("record") SvcSalesItem record, @Param("example") SvcSalesItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales_item
     *
     * @mbggenerated Wed Jan 11 14:34:43 KST 2017
     */
    int updateByPrimaryKeySelective(SvcSalesItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales_item
     *
     * @mbggenerated Wed Jan 11 14:34:43 KST 2017
     */
    int updateByPrimaryKey(SvcSalesItem record);
    
    
    // 추가 :     
    //List<SvcSalesItemEx> selectBySalesItemList(HashMap searchItem);
    List<SvcSalesItem> selectBySalesItemList(HashMap searchItem);
   
    List<SvcSalesItem> selectBySalesDetailKioskTest(HashMap searchItem);



    
}