package com.xpllyn.service;

import com.xpllyn.pojo.User;

public interface IUserService {
    User findByUserId(String id);

    boolean addNewUser(String user_email, String user_name, String user_pw, String user_sex);

    boolean updateLoginTime(int id);

    boolean updatePhone(int id, String user_phone);

    User findByEmailAndPwd(String user_email, String user_pw);

    User findByEmail(String user_email);
}
