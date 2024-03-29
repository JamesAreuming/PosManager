package com.jc.pico.mapper;

import com.jc.pico.bean.Zipcode;
import com.jc.pico.bean.ZipcodeExample;
import com.jc.pico.bean.ZipcodeWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ZipcodeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_ZIPCODE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int countByExample(ZipcodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_ZIPCODE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int deleteByExample(ZipcodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_ZIPCODE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int insert(ZipcodeWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_ZIPCODE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int insertSelective(ZipcodeWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_ZIPCODE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    List<ZipcodeWithBLOBs> selectByExampleWithBLOBsWithRowbounds(ZipcodeExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_ZIPCODE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    List<ZipcodeWithBLOBs> selectByExampleWithBLOBs(ZipcodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_ZIPCODE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    List<Zipcode> selectByExampleWithRowbounds(ZipcodeExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_ZIPCODE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    List<Zipcode> selectByExample(ZipcodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_ZIPCODE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByExampleSelective(@Param("record") ZipcodeWithBLOBs record, @Param("example") ZipcodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_ZIPCODE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByExampleWithBLOBs(@Param("record") ZipcodeWithBLOBs record, @Param("example") ZipcodeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_ZIPCODE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByExample(@Param("record") Zipcode record, @Param("example") ZipcodeExample example);
}