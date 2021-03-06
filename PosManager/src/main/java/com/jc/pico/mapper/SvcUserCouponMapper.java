package com.jc.pico.mapper;

import com.jc.pico.bean.SvcUserCoupon;
import com.jc.pico.bean.SvcUserCouponExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface SvcUserCouponMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_user_coupon
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    int countByExample(SvcUserCouponExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_user_coupon
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    int deleteByExample(SvcUserCouponExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_user_coupon
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_user_coupon
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    int insert(SvcUserCoupon record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_user_coupon
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    int insertSelective(SvcUserCoupon record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_user_coupon
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    List<SvcUserCoupon> selectByExampleWithRowbounds(SvcUserCouponExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_user_coupon
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    List<SvcUserCoupon> selectByExample(SvcUserCouponExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_user_coupon
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    SvcUserCoupon selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_user_coupon
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    int updateByExampleSelective(@Param("record") SvcUserCoupon record, @Param("example") SvcUserCouponExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_user_coupon
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    int updateByExample(@Param("record") SvcUserCoupon record, @Param("example") SvcUserCouponExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_user_coupon
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    int updateByPrimaryKeySelective(SvcUserCoupon record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_user_coupon
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    int updateByPrimaryKey(SvcUserCoupon record);
}