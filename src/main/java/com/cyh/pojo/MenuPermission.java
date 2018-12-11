package com.cyh.pojo;

import java.util.List;

public class MenuPermission implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String type;//menu/permission
    private String url;
    private int parentId;//父id
    private int orderList;//排序
    private String iconCls;//根节点图标
    private int status;//菜单、权限状态   0：停用    1：启用
    private String remark;
    private String updateUser;
    private String updateTime;
    private List<MenuPermission> childMenus;// 子菜单

    public List<MenuPermission> getChildMenus() {
        return childMenus;
    }

    public void setChildMenus(List<MenuPermission> childMenus) {
        this.childMenus = childMenus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getOrderList() {
        return orderList;
    }

    public void setOrderList(int orderList) {
        this.orderList = orderList;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
