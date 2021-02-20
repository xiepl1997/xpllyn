package com.xpllyn.service;

import com.xpllyn.pojo.User;

import java.util.List;

public interface IUserService {
    User findByUserId(String id);

    boolean addNewUser(String user_email, String user_name, String user_pw, String user_sex, String user_icon);

    boolean updateLoginTime(int id);

    boolean updatePhone(int id, String user_phone);

    User findByEmailAndPwd(String user_email, String user_pw);

    User findByEmail(String user_email);

    List<User> findFriends(int id);

    List<User> findByIdOrEmail(String idOrEmail);

    List<Integer> findOnlineFriendIds(List<User> users);

    List<User> getSendAddRequestUsers(int id);

    boolean sendAddFriendRequest(int fromId, int toId);

    int getAddFriendRequest(int fromId, int toId);

    List<Integer> findFriendIds(int id);

    boolean agreeAddRequest(int fromId, int toId);

    boolean disagreeAddRequest(int fromId, int toId);
}
