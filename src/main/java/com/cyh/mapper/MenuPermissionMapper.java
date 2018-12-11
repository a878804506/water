package com.cyh.mapper;


import com.cyh.pojo.MenuPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuPermissionMapper {
    //登录时根据用户id获取菜单
    List<MenuPermission> getMenuByUserId(Integer uid);

    //登录时根据用户id获取所有授权
    List<MenuPermission> getPermissionListByUserId(Integer uid);

    //获取所有菜单列表
    List<MenuPermission> getAllMenu();

    //获取用户权限  查询usertomenupermissionmapping 表
    List<Integer> getUserMenuPermissionByUserId(int uid);

    //删除用户菜单权限
    int deleteUserMenuPermissionByUserId(int uid);

    //添加用户菜单权限
    int insertUserMenuPermissionByUserId(@Param(value="condition")String condition);
}
