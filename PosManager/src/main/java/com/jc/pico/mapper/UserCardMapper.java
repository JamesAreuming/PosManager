package com.jc.pico.mapper;

import com.jc.pico.bean.UserCard;
import com.jc.pico.bean.UserCardExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface UserCardMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_card
     *
     * @mbggenerated Tue Oct 11 15:13:03 MSK 2016
     */
    int countByExample(UserCardExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_card
     *
     * @mbggenerated Tue Oct 11 15:13:03 MSK 2016
     */
    int deleteByExample(UserCardExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_card
     *
     * @mbggenerated Tue Oct 11 15:13:03 MSK 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_card
     *
     * @mbggenerated Tue Oct 11 15:13:03 MSK 2016
     */
    int insert(UserCard record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_card
     *
     * @mbggenerated Tue Oct 11 15:13:03 MSK 2016
     */
    int insertSelective(UserCard record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_card
     *
     * @mbggenerated Tue Oct 11 15:13:03 MSK 2016
     */
    List<UserCard> selectByExampleWithRowbounds(UserCardExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_card
     *
     * @mbggenerated Tue Oct 11 15:13:03 MSK 2016
     */
    List<UserCard> selectByExample(UserCardExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_card
     *
     * @mbggenerated Tue Oct 11 15:13:03 MSK 2016
     */
    UserCard selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_card
     *
     * @mbggenerated Tue Oct 11 15:13:03 MSK 2016
     */
    int updateByExampleSelective(@Param("record") UserCard record, @Param("example") UserCardExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_card
     *
     * @mbggenerated Tue Oct 11 15:13:03 MSK 2016
     */
    int updateByExample(@Param("record") UserCard record, @Param("example") UserCardExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_card
     *
     * @mbggenerated Tue Oct 11 15:13:03 MSK 2016
     */
    int updateByPrimaryKeySelective(UserCard record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_user_card
     *
     * @mbggenerated Tue Oct 11 15:13:03 MSK 2016
     */
    int updateByPrimaryKey(UserCard record);
}