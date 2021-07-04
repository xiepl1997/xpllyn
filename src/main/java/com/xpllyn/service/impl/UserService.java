package com.xpllyn.service.impl;

import com.xpllyn.mapper.UserMapper;
import com.xpllyn.pojo.User;
import com.xpllyn.service.IUserService;
import com.xpllyn.utils.im.Constant;
import com.xpllyn.utils.EncryptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUserId(String id) {
        return userMapper.findByUserId(id);
    }

    @Override
    public boolean addNewUser(String user_email, String user_name, String user_pw, String user_sex, String user_icon) {
        User user = userMapper.findByEmail(user_email);
        if (user != null) {
            return false;
        }
        EncryptionUtils encryptionUtils = new EncryptionUtils();
        String pwd = encryptionUtils.encryption(user_pw);
        userMapper.addNewUser(user_email, user_name, pwd, user_sex, user_icon);
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

    @Override
    public List<User> findFriends(int id) {
        return userMapper.findFriends(id);
    }

    @Override
    public List<User> findByIdOrEmail(String idOrEmail) {
        return userMapper.findByIdOrEmail(idOrEmail);
    }

    @Override
    public List<Integer> findOnlineFriendIds(List<User> users) {
        List<Integer> onlineFriendIds = new ArrayList<>();
        for (User user : users) {
            if (Constant.onlineUser.containsKey(String.valueOf(user.getId()))) {
                onlineFriendIds.add(user.getId());
            }
        }
        return onlineFriendIds;
    }

    @Override
    public List<User> getSendAddRequestUsers(int id) {
        return userMapper.getSendAddRequestUsers(id);
    }

    @Override
    public boolean sendAddFriendRequest(int fromId, int toId) {
        return userMapper.sendAddFriendRequest(fromId, toId);
    }

    @Override
    public int getAddFriendRequest(int fromId, int toId) {
        return userMapper.getAddFriendRequest(fromId, toId);
    }

    @Override
    @Transactional
    public boolean agreeAddRequest(int fromId, int toId) {
        userMapper.agreeAddRequest(fromId, toId);
        userMapper.addFriend(fromId, toId);
        return userMapper.addFriend(toId, fromId);
    }

    @Override
    public boolean disagreeAddRequest(int fromId, int toId) {
        return userMapper.disagreeAddRequest(fromId, toId);
    }

    @Override
    public List<Integer> findFriendIds(int id) {
        return userMapper.findFriendIds(id);
    }
}
