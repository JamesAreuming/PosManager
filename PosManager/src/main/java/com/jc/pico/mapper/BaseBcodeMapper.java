package com.jc.pico.mapper;

import com.jc.pico.bean.BaseBcode;
import com.jc.pico.bean.BaseBcodeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface BaseBcodeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_BASE_BCODE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int countByExample(BaseBcodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_BASE_BCODE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int deleteByExample(BaseBcodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_BASE_BCODE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int deleteByPrimaryKey(String baseCd);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_BASE_BCODE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int insert(BaseBcode record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_BASE_BCODE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int insertSelective(BaseBcode record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_BASE_BCODE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    List<BaseBcode> selectByExampleWithRowbounds(BaseBcodeExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_BASE_BCODE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    List<BaseBcode> selectByExample(BaseBcodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_BASE_BCODE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    BaseBcode selectByPrimaryKey(String baseCd);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_BASE_BCODE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByExampleSelective(@Param("record") BaseBcode record, @Param("example") BaseBcodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_BASE_BCODE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByExample(@Param("record") BaseBcode record, @Param("example") BaseBcodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_BASE_BCODE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByPrimaryKeySelective(BaseBcode record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_BASE_BCODE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByPrimaryKey(BaseBcode record);
}