package com.xpllyn.mapper;

import com.xpllyn.pojo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserMapper {

    @Select("select * from user where id = #{id}")
    User findByUserId(@Param("id") String id);

    @Insert("insert into user(user_email, user_name, user_pw, user_sex, user_icon, user_last_login,create_time,update_time) values(#{user_email}, #{user_name},#{user_pw},#{user_sex},#{user_icon},current_timestamp,current_timestamp,current_timestamp)")
    boolean addNewUser(String user_email, String user_name, String user_pw, String user_sex, String user_icon);

    @Update("update user set user_last_login = current_timestamp, create_time = current_timestamp where id = #{id}")
    boolean updateLoginTime(int id);

    @Update("update user set user_phone = #{user_phone} where id = #{id}")
    boolean updatePhone(int id, String user_phone);

    @Select("select * from user where user_email = #{user_email} and user_password = #{user_pw}")
    User findByEmailAndPwd(String user_email, String user_pw);

    @Select("select * from user where user_email = #{user_email}")
    User findByEmail(String user_email);

    @Select("select b.* from friend a, user b where a.user_id = #{id} and a.friend_id = b.id")
    List<User> findFriends(int id);

    @Select("select * from user where user_email = #{idOrEmail} or user_name like '%${idOrEmail}%'")
    List<User> findByIdOrEmail(String idOrEmail);

    @Select("select send_user_id from add_request where receive_user_id = #{id}")
    List<Integer> getSendAddRequestIds(int id);
}
