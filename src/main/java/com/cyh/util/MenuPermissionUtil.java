package com.cyh.util;

import com.cyh.pojo.MenuPermission;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MenuPermissionUtil {
    public static List<MenuPermission> getMenuTree(List<MenuPermission> rootMenu) {
        // 最后返回的结果
        List<MenuPermission> menuList = new ArrayList<MenuPermission>();
        // 先找到所有的一级菜单
        for (int i = 0; i < rootMenu.size(); i++) {
            // 一级菜单没有parentId
            if (rootMenu.get(i).getParentId() == 1 && rootMenu.get(i).getStatus() != 0) {
                menuList.add(rootMenu.get(i));
            }
        }
        //排序一级菜单
        if (menuList.size() > 0) {
            Collections.sort(menuList, new Comparator<MenuPermission>() {
                @Override
                public int compare(MenuPermission o1, MenuPermission o2) {
                    if (o1.getOrderList() > o2.getOrderList()) {
                        return 1;
                    }
                    if (o1.getOrderList() == o2.getOrderList()) {
                        return 0;
                    }
                    return -1;
                }
            });
        }
        // 为一级菜单设置子菜单，getChild是递归调用的
        for (MenuPermission menu : menuList) {
            List<MenuPermission> menuListTemp = getChild(menu.getId(), rootMenu);
            if (menuListTemp != null ) {
                //排序二级菜单
                Collections.sort(menuListTemp, new Comparator<MenuPermission>() {
                    @Override
                    public int compare(MenuPermission o1, MenuPermission o2) {
                        if (o1.getOrderList() > o2.getOrderList()) {
                            return 1;
                        }
                        if (o1.getOrderList() == o2.getOrderList()) {
                            return 0;
                        }
                        return -1;
                    }
                });
            }
            menu.setChildMenus(menuListTemp);
        }
        return menuList;
    }

    /**
     * 递归查找子菜单
     *
     * @param id       当前菜单id
     * @param rootMenu 要查找的列表
     * @return
     */
    private static List<MenuPermission> getChild(int id, List<MenuPermission> rootMenu) {
        // 子菜单
        List<MenuPermission> childList = new ArrayList<>();
        for (MenuPermission menu : rootMenu) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (menu.getParentId() != 1) {
                if (menu.getParentId() == id && menu.getStatus() != 0) {
                    childList.add(menu);
                }
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (MenuPermission menu : childList) {// 没有url子菜单还有子菜单
            if (!menu.getType().equals("permission") || StringUtils.isBlank(menu.getUrl())) {
                // 递归
                menu.setChildMenus(getChild(menu.getId(), rootMenu));
            }
        } // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }
}
