package com.jc.pico.mapper;

import com.jc.pico.bean.SvcStoreSet;
import com.jc.pico.bean.SvcStoreSetExample;
import com.jc.pico.bean.SvcStoreSetWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface SvcStoreSetMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_set
     *
     * @mbggenerated Thu Jan 05 17:15:55 KST 2017
     */
    int countByExample(SvcStoreSetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_set
     *
     * @mbggenerated Thu Jan 05 17:15:55 KST 2017
     */
    int deleteByExample(SvcStoreSetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_set
     *
     * @mbggenerated Thu Jan 05 17:15:55 KST 2017
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_set
     *
     * @mbggenerated Thu Jan 05 17:15:55 KST 2017
     */
    int insert(SvcStoreSetWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_set
     *
     * @mbggenerated Thu Jan 05 17:15:55 KST 2017
     */
    int insertSelective(SvcStoreSetWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_set
     *
     * @mbggenerated Thu Jan 05 17:15:55 KST 2017
     */
    List<SvcStoreSetWithBLOBs> selectByExampleWithBLOBsWithRowbounds(SvcStoreSetExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_set
     *
     * @mbggenerated Thu Jan 05 17:15:55 KST 2017
     */
    List<SvcStoreSetWithBLOBs> selectByExampleWithBLOBs(SvcStoreSetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_set
     *
     * @mbggenerated Thu Jan 05 17:15:55 KST 2017
     */
    List<SvcStoreSet> selectByExampleWithRowbounds(SvcStoreSetExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_set
     *
     * @mbggenerated Thu Jan 05 17:15:55 KST 2017
     */
    List<SvcStoreSet> selectByExample(SvcStoreSetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_set
     *
     * @mbggenerated Thu Jan 05 17:15:55 KST 2017
     */
    SvcStoreSetWithBLOBs selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_set
     *
     * @mbggenerated Thu Jan 05 17:15:55 KST 2017
     */
    int updateByExampleSelective(@Param("record") SvcStoreSetWithBLOBs record, @Param("example") SvcStoreSetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_set
     *
     * @mbggenerated Thu Jan 05 17:15:55 KST 2017
     */
    int updateByExampleWithBLOBs(@Param("record") SvcStoreSetWithBLOBs record, @Param("example") SvcStoreSetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_set
     *
     * @mbggenerated Thu Jan 05 17:15:55 KST 2017
     */
    int updateByExample(@Param("record") SvcStoreSet record, @Param("example") SvcStoreSetExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_set
     *
     * @mbggenerated Thu Jan 05 17:15:55 KST 2017
     */
    int updateByPrimaryKeySelective(SvcStoreSetWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_set
     *
     * @mbggenerated Thu Jan 05 17:15:55 KST 2017
     */
    int updateByPrimaryKeyWithBLOBs(SvcStoreSetWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_store_set
     *
     * @mbggenerated Thu Jan 05 17:15:55 KST 2017
     */
    int updateByPrimaryKey(SvcStoreSet record);
}