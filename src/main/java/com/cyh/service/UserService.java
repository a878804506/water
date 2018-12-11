package com.cyh.service;

import com.cyh.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserService {
    User CUser(User user);

    void updateUser(User user);

    //获取所有用户列表
    List<User> getAllUsers();

    //修改系统用户状态
    int updateUserStrutsByUid(@Param(value="uid")int uid , @Param(value="struts")int struts);

    User getUserById(int uid);

    //重置系统用户密码
    int updateUserPwdByUid(int uid,String newPwd);

    //新建用户
    int createUser(User user);

    //获取所有可登陆用户列表  struts=1
    List<Map<String,Object>> getAllStrutsUsers();

    //用户修改自己的信息
    void updateUserInfoByUserId(User user);
}
