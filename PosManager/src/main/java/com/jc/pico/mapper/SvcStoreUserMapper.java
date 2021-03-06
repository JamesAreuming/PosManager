package com.jc.pico.mapper;

import com.jc.pico.bean.SvcStoreUser;
import com.jc.pico.bean.SvcStoreUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface SvcStoreUserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_user
     *
     * @mbggenerated Tue Oct 11 11:04:02 MSK 2016
     */
    int countByExample(SvcStoreUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_user
     *
     * @mbggenerated Tue Oct 11 11:04:02 MSK 2016
     */
    int deleteByExample(SvcStoreUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_user
     *
     * @mbggenerated Tue Oct 11 11:04:02 MSK 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_user
     *
     * @mbggenerated Tue Oct 11 11:04:02 MSK 2016
     */
    int insert(SvcStoreUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_user
     *
     * @mbggenerated Tue Oct 11 11:04:02 MSK 2016
     */
    int insertSelective(SvcStoreUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_user
     *
     * @mbggenerated Tue Oct 11 11:04:02 MSK 2016
     */
    List<SvcStoreUser> selectByExampleWithRowbounds(SvcStoreUserExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_user
     *
     * @mbggenerated Tue Oct 11 11:04:02 MSK 2016
     */
    List<SvcStoreUser> selectByExample(SvcStoreUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_user
     *
     * @mbggenerated Tue Oct 11 11:04:02 MSK 2016
     */
    SvcStoreUser selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_user
     *
     * @mbggenerated Tue Oct 11 11:04:02 MSK 2016
     */
    int updateByExampleSelective(@Param("record") SvcStoreUser record, @Param("example") SvcStoreUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_user
     *
     * @mbggenerated Tue Oct 11 11:04:02 MSK 2016
     */
    int updateByExample(@Param("record") SvcStoreUser record, @Param("example") SvcStoreUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_user
     *
     * @mbggenerated Tue Oct 11 11:04:02 MSK 2016
     */
    int updateByPrimaryKeySelective(SvcStoreUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_user
     *
     * @mbggenerated Tue Oct 11 11:04:02 MSK 2016
     */
    int updateByPrimaryKey(SvcStoreUser record);
}