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

    @Update("update user set user_phone = #{user_phone}, update_time = current_timestamp where id = #{id}")
    boolean updatePhone(int id, String user_phone);

    @Select("select * from user where user_email = #{user_email} and user_password = #{user_pw}")
    User findByEmailAndPwd(String user_email, String user_pw);

    @Select("select * from user where user_email = #{user_email}")
    User findByEmail(String user_email);

    @Select("select b.* from friend a, user b where a.user_id = #{id} and a.friend_id = b.id")
    List<User> findFriends(int id);

    @Select("select * from user where user_email = #{idOrEmail} or user_name like '%${idOrEmail}%'")
    List<User> findByIdOrEmail(String idOrEmail);

    @Select("select b.* from add_request a, user b where a.receive_user_id = #{id} and a.status = 'send' and b.id = a.send_user_id")
    List<User> getSendAddRequestUsers(int id);

    @Insert("insert into add_request(send_user_id, receive_user_id, status, create_time, update_time) values(#{fromId}, #{toId}, 'send', current_timestamp,current_timestamp)")
    boolean sendAddFriendRequest(int fromId, int toId);

    @Select("select count(*) from add_request where send_user_id = #{fromId} and receive_user_id = #{toId} and status = 'send'")
    int getAddFriendRequest(int fromId, int toId);

    @Select("select friend_id from friend where user_id = #{id}")
    List<Integer> findFriendIds(int id);

    @Update("update add_request set status = 'agree', update_time = current_timestamp where send_user_id = #{fromId} and receive_user_id = #{toId}")
    boolean agreeAddRequest(int fromId, int toId);

    @Update("update add_request set status = 'disagree', update_time = current_timestamp where send_user_id = #{fromId} and receive_user_id = #{toId}")
    boolean disagreeAddRequest(int fromId, int toId);

    @Insert("insert into friend(friend_id, user_id, create_time, update_time) values(#{fromId}, #{toId}, current_timestamp,current_timestamp)")
    boolean addFriend(int fromId, int toId);

}
