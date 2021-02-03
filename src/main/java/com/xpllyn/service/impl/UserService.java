package com.xpllyn.service.impl;

import com.xpllyn.mapper.UserMapper;
import com.xpllyn.pojo.User;
import com.xpllyn.service.IUserService;
import com.xpllyn.utils.EncryptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUserId(String id) {
        return userMapper.findByUserId(id);
    }

    @Override
    public boolean addNewUser(String user_email, String user_name, String user_pw, String user_sex) {
        User user = userMapper.findByEmail(user_email);
        if (user != null) {
            return false;
        }
        EncryptionUtils encryptionUtils = new EncryptionUtils();
        String pwd = encryptionUtils.encryption(user_pw);
        userMapper.addNewUser(user_email, user_name, pwd, user_sex);
        return true;
    }

    @Override
    public boolean updateLoginTime(int id) {
        return userMapper.updateLoginTime(id);
    }

    @Override
    public boolean updatePhone(int id, String user_phone) {
        return updatePhone(id, user_phone);
    }

    @Override
    public User findByEmailAndPwd(String user_email, String user_pw) {
        return null;
    }

    @Override
    public User findByEmail(String user_email) {
        return userMapper.findByEmail(user_email);
    }
}
