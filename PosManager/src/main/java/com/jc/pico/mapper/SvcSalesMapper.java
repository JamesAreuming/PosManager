package com.jc.pico.mapper;

import com.jc.pico.bean.SvcOrder;
import com.jc.pico.bean.SvcSales;
import com.jc.pico.bean.SvcSalesExample;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface SvcSalesMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales
     *
     * @mbggenerated Thu Nov 17 14:52:30 KST 2016
     */
    int countByExample(SvcSalesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales
     *
     * @mbggenerated Thu Nov 17 14:52:30 KST 2016
     */
    int deleteByExample(SvcSalesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales
     *
     * @mbggenerated Thu Nov 17 14:52:30 KST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales
     *
     * @mbggenerated Thu Nov 17 14:52:30 KST 2016
     */
    int insert(SvcSales record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales
     *
     * @mbggenerated Thu Nov 17 14:52:30 KST 2016
     */
    int insertSelective(SvcSales record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales
     *
     * @mbggenerated Thu Nov 17 14:52:30 KST 2016
     */
    List<SvcSales> selectByExampleWithRowbounds(SvcSalesExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales
     *
     * @mbggenerated Thu Nov 17 14:52:30 KST 2016
     */
    List<SvcSales> selectByExample(SvcSalesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales
     *
     * @mbggenerated Thu Nov 17 14:52:30 KST 2016
     */
    SvcSales selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales
     *
     * @mbggenerated Thu Nov 17 14:52:30 KST 2016
     */
    int updateByExampleSelective(@Param("record") SvcSales record, @Param("example") SvcSalesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales
     *
     * @mbggenerated Thu Nov 17 14:52:30 KST 2016
     */
    int updateByExample(@Param("record") SvcSales record, @Param("example") SvcSalesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales
     *
     * @mbggenerated Thu Nov 17 14:52:30 KST 2016
     */
    int updateByPrimaryKeySelective(SvcSales record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales
     *
     * @mbggenerated Thu Nov 17 14:52:30 KST 2016
     */
    int updateByPrimaryKey(SvcSales record);
    

    
    /**  
     * 추가 : 
     * 
     */
    // tb_svc_sales 추가
    int insertIntoSales(HashMap newOrder);

    // tb_svc_sales_item 추가
    int insertIntoSalesItem(HashMap newOrder);
    
    // tb_svc_sales_pay 추가
    int insertIntoSalesPay(HashMap newOrder);
    
    /**
     * 주문 취소(매출)
     *   ORDER_ST : 607005  --> ORDER_ST : 607003  변경
     * 추가 : 
     *   
     * @param sales
     * @return
     */
    int updateByOrderCancel(HashMap sales);
    
    SvcSales selectBySalesDetailKioskTest2(HashMap searchTest);    
    
    
}