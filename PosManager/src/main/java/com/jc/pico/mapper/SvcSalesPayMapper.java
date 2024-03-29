package com.jc.pico.mapper;

import com.jc.pico.bean.SvcSalesPay;
import com.jc.pico.bean.SvcSalesPayExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface SvcSalesPayMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales_pay
     *
     * @mbggenerated Fri Jan 06 17:22:42 KST 2017
     */
    int countByExample(SvcSalesPayExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales_pay
     *
     * @mbggenerated Fri Jan 06 17:22:42 KST 2017
     */
    int deleteByExample(SvcSalesPayExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales_pay
     *
     * @mbggenerated Fri Jan 06 17:22:42 KST 2017
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales_pay
     *
     * @mbggenerated Fri Jan 06 17:22:42 KST 2017
     */
    int insert(SvcSalesPay record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales_pay
     *
     * @mbggenerated Fri Jan 06 17:22:42 KST 2017
     */
    int insertSelective(SvcSalesPay record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales_pay
     *
     * @mbggenerated Fri Jan 06 17:22:42 KST 2017
     */
    List<SvcSalesPay> selectByExampleWithRowbounds(SvcSalesPayExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales_pay
     *
     * @mbggenerated Fri Jan 06 17:22:42 KST 2017
     */
    List<SvcSalesPay> selectByExample(SvcSalesPayExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales_pay
     *
     * @mbggenerated Fri Jan 06 17:22:42 KST 2017
     */
    SvcSalesPay selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales_pay
     *
     * @mbggenerated Fri Jan 06 17:22:42 KST 2017
     */
    int updateByExampleSelective(@Param("record") SvcSalesPay record, @Param("example") SvcSalesPayExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales_pay
     *
     * @mbggenerated Fri Jan 06 17:22:42 KST 2017
     */
    int updateByExample(@Param("record") SvcSalesPay record, @Param("example") SvcSalesPayExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales_pay
     *
     * @mbggenerated Fri Jan 06 17:22:42 KST 2017
     */
    int updateByPrimaryKeySelective(SvcSalesPay record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_sales_pay
     *
     * @mbggenerated Fri Jan 06 17:22:42 KST 2017
     */
    int updateByPrimaryKey(SvcSalesPay record);
}