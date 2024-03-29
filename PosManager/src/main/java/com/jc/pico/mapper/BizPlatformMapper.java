package com.jc.pico.mapper;

import com.jc.pico.bean.BizPlatform;
import com.jc.pico.bean.BizPlatformExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface BizPlatformMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_BIZ_PLATFORM
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int countByExample(BizPlatformExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_BIZ_PLATFORM
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int deleteByExample(BizPlatformExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_BIZ_PLATFORM
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_BIZ_PLATFORM
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int insert(BizPlatform record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_BIZ_PLATFORM
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int insertSelective(BizPlatform record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_BIZ_PLATFORM
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    List<BizPlatform> selectByExampleWithRowbounds(BizPlatformExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_BIZ_PLATFORM
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    List<BizPlatform> selectByExample(BizPlatformExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_BIZ_PLATFORM
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    BizPlatform selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_BIZ_PLATFORM
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByExampleSelective(@Param("record") BizPlatform record, @Param("example") BizPlatformExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_BIZ_PLATFORM
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByExample(@Param("record") BizPlatform record, @Param("example") BizPlatformExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_BIZ_PLATFORM
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByPrimaryKeySelective(BizPlatform record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_BIZ_PLATFORM
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByPrimaryKey(BizPlatform record);
}