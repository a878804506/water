package com.cyh.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.cyh.mapper.UserMapper;
import com.cyh.pojo.User;
import com.cyh.service.UserService;

import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    public User CUser(User user) {
        return userMapper.CUser(user);
    }

    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    @Override
    public int updateUserStrutsByUid(int uid, int struts) {
        return userMapper.updateUserStrutsByUid(uid,struts);
    }

    @Override
    public User getUserById(int uid) {
        return userMapper.getUserById(uid);
    }

    @Override
    public int updateUserPwdByUid(int uid, String newPwd) {
        return userMapper.updateUserPwdByUid(uid,newPwd);
    }

    @Override
    public int createUser(User user) {
        return userMapper.createUser(user);
    }

    @Override
    public List<Map<String, Object>> getAllStrutsUsers() {
        return userMapper.getAllStrutsUsers();
    }

    @Override
    public void updateUserInfoByUserId(User user) {
        userMapper.updateUserInfoByUserId(user);
    }
}