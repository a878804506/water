package com.cyh.service.impl;


import com.cyh.mapper.MenuPermissionMapper;
import com.cyh.pojo.MenuPermission;
import com.cyh.service.MenuPermissionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MenuPermissionServiceImpl implements MenuPermissionService {

    @Autowired
    private MenuPermissionMapper menuPermissionMapper;

    @Override
    public List<MenuPermission> getMenuByUserId(Integer uid) {
        return menuPermissionMapper.getMenuByUserId(uid);
    }

    @Override
    public List<MenuPermission> getPermissionListByUserId(Integer uid) {
        return menuPermissionMapper.getPermissionListByUserId(uid);
    }

    @Override
    public List<MenuPermission> getAllMenu() {
        return menuPermissionMapper.getAllMenu();
    }

    @Override
    public List<Integer> getUserMenuPermissionByUserId(int uid) {
        return menuPermissionMapper.getUserMenuPermissionByUserId(uid);
    }

    @Override
    public int deleteUserMenuPermissionByUserId(int uid) {
        return menuPermissionMapper.deleteUserMenuPermissionByUserId(uid);
    }

    @Override
    public int insertUserMenuPermissionByUserId(String condition) {
        return menuPermissionMapper.insertUserMenuPermissionByUserId(condition);
    }
}
