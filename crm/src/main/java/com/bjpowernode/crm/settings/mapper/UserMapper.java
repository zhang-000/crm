package com.bjpowernode.crm.settings.mapper;

import com.bjpowernode.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbggenerated Thu Jul 11 11:00:13 CST 2024
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbggenerated Thu Jul 11 11:00:13 CST 2024
     */
    int insert(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbggenerated Thu Jul 11 11:00:13 CST 2024
     */
    int insertSelective(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbggenerated Thu Jul 11 11:00:13 CST 2024
     */
    User selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbggenerated Thu Jul 11 11:00:13 CST 2024
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbggenerated Thu Jul 11 11:00:13 CST 2024
     */
    int updateByPrimaryKey(User record);

    //通过登录账号和密码查询对应的user的所有字段
    User selectUserByloginActAndPwd(Map<String, Object> map);

    //查询出所有lock_state未被锁定的user，封装成一个集合
    List<User> selectAllUsers();
}