package com.jc.pico.mapper;

import com.jc.pico.bean.SvcFranchise;
import com.jc.pico.bean.SvcFranchiseExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface SvcFranchiseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_FRANCHISE
     *
     * @mbggenerated Tue Jul 12 09:25:37 KST 2016
     */
    int countByExample(SvcFranchiseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_FRANCHISE
     *
     * @mbggenerated Tue Jul 12 09:25:37 KST 2016
     */
    int deleteByExample(SvcFranchiseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_FRANCHISE
     *
     * @mbggenerated Tue Jul 12 09:25:37 KST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_FRANCHISE
     *
     * @mbggenerated Tue Jul 12 09:25:37 KST 2016
     */
    int insert(SvcFranchise record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_FRANCHISE
     *
     * @mbggenerated Tue Jul 12 09:25:37 KST 2016
     */
    int insertSelective(SvcFranchise record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_FRANCHISE
     *
     * @mbggenerated Tue Jul 12 09:25:37 KST 2016
     */
    List<SvcFranchise> selectByExampleWithRowbounds(SvcFranchiseExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_FRANCHISE
     *
     * @mbggenerated Tue Jul 12 09:25:37 KST 2016
     */
    List<SvcFranchise> selectByExample(SvcFranchiseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_FRANCHISE
     *
     * @mbggenerated Tue Jul 12 09:25:37 KST 2016
     */
    SvcFranchise selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_FRANCHISE
     *
     * @mbggenerated Tue Jul 12 09:25:37 KST 2016
     */
    int updateByExampleSelective(@Param("record") SvcFranchise record, @Param("example") SvcFranchiseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_FRANCHISE
     *
     * @mbggenerated Tue Jul 12 09:25:37 KST 2016
     */
    int updateByExample(@Param("record") SvcFranchise record, @Param("example") SvcFranchiseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_FRANCHISE
     *
     * @mbggenerated Tue Jul 12 09:25:37 KST 2016
     */
    int updateByPrimaryKeySelective(SvcFranchise record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_SVC_FRANCHISE
     *
     * @mbggenerated Tue Jul 12 09:25:37 KST 2016
     */
    int updateByPrimaryKey(SvcFranchise record);
}