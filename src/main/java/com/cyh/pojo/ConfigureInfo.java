package com.cyh.pojo;

public class ConfigureInfo implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private String path; // name = excel 时  path是指服务器端excel存放路径    name =  updateTables 时   path是指需要创建表的年份
    private String updateTables; //name =  updateTables 时 有用 ， 需要创建的表，以逗号隔开
    private String time;  // name = excel 时  time是指修改时间    name =  updateTables 时   time是指创建及初始化表的时机
    private String update_user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUpdateTables() {
        return updateTables;
    }

    public void setUpdateTables(String updateTables) {
        this.updateTables = updateTables;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUpdate_user() {
        return update_user;
    }

    public void setUpdate_user(String update_user) {
        this.update_user = update_user;
    }

}
