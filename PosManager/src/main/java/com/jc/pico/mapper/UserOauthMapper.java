package com.jc.pico.mapper;

import com.jc.pico.bean.UserOauth;
import com.jc.pico.bean.UserOauthExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface UserOauthMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_USER_OAUTH
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int countByExample(UserOauthExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_USER_OAUTH
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int deleteByExample(UserOauthExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_USER_OAUTH
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_USER_OAUTH
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int insert(UserOauth record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_USER_OAUTH
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int insertSelective(UserOauth record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_USER_OAUTH
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    List<UserOauth> selectByExampleWithRowbounds(UserOauthExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_USER_OAUTH
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    List<UserOauth> selectByExample(UserOauthExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_USER_OAUTH
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    UserOauth selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_USER_OAUTH
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByExampleSelective(@Param("record") UserOauth record, @Param("example") UserOauthExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_USER_OAUTH
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByExample(@Param("record") UserOauth record, @Param("example") UserOauthExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_USER_OAUTH
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByPrimaryKeySelective(UserOauth record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_USER_OAUTH
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByPrimaryKey(UserOauth record);
}