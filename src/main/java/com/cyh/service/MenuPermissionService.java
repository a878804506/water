package com.cyh.service;

import com.cyh.pojo.*;

import java.util.List;

public interface MenuPermissionService {
    //登录时根据用户id获取菜单
    List<MenuPermission> getMenuByUserId(Integer uid);

    //登录时根据用户id获取所有授权
    List<MenuPermission> getPermissionListByUserId(Integer uid);

    //获取所有菜单列表
    List<MenuPermission> getAllMenu();

    //获取角色权限
    List<Integer> getMenuPermissionByRoleId(int rid);

    //删除角色菜单权限
    int deleteMenuPermissionByRoleId(int rid);

    //为角色添加菜单权限
    int insertMenuPermissionByRoleId(int rid,int[] permissionsId);
}
