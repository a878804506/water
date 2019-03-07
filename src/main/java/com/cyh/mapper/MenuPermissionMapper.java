package com.cyh.mapper;


import com.cyh.pojo.MenuPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuPermissionMapper {

    //获取所有菜单列表
    List<MenuPermission> getAllMenu();

    //获取用户权限
    List<Integer> getMenuPermissionByRoleId(int rid);

    //删除角色菜单权限
    int deleteMenuPermissionByRoleId(int rid);

    //为角色添加菜单权限
    int insertMenuPermissionByRoleId(@Param(value="rid")int rid,@Param(value="permissionsId")int[] permissionsId);

    // 根据角色id集合 获取菜单和页面内的权限
    List<MenuPermission> getAllMenusAndPermissionsByRoleIds(@Param(value="roleIds")List<Integer> roleIds);
}
