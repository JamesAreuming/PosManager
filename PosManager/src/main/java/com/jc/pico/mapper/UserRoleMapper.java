package com.jc.pico.mapper;

import com.jc.pico.bean.UserRole;
import com.jc.pico.bean.UserRoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface UserRoleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_USER_ROLE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int countByExample(UserRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_USER_ROLE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int deleteByExample(UserRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_USER_ROLE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_USER_ROLE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int insert(UserRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_USER_ROLE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int insertSelective(UserRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_USER_ROLE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    List<UserRole> selectByExampleWithRowbounds(UserRoleExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_USER_ROLE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    List<UserRole> selectByExample(UserRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_USER_ROLE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    UserRole selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_USER_ROLE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByExampleSelective(@Param("record") UserRole record, @Param("example") UserRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_USER_ROLE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByExample(@Param("record") UserRole record, @Param("example") UserRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_USER_ROLE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByPrimaryKeySelective(UserRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TB_USER_ROLE
     *
     * @mbggenerated Mon Jul 04 21:07:11 KST 2016
     */
    int updateByPrimaryKey(UserRole record);
}