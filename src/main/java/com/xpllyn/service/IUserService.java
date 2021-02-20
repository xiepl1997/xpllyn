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

}
