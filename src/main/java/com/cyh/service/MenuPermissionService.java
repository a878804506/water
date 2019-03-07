package com.cyh.service;

import com.cyh.pojo.*;

import java.util.List;

public interface MenuPermissionService {

    //获取所有菜单列表
    List<MenuPermission> getAllMenu();

    //获取角色权限
    List<Integer> getMenuPermissionByRoleId(int rid);

    //删除角色菜单权限
    int deleteMenuPermissionByRoleId(int rid);

    //为角色添加菜单权限
    int insertMenuPermissionByRoleId(int rid,int[] permissionsId);

    // 根据角色id集合 获取菜单和页面内的权限
    List<MenuPermission> getAllMenusAndPermissionsByRoleIds(List<Integer> roleIds);
}
