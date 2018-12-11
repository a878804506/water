package com.cyh.pojo;

public class Operator_log implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    private Integer id; // 自增长
    private Integer operator_id; // 操作员编号
    private String operator_name; // 操作员姓名
    private Integer user_id; // 被操作的用户编号
    private String user_name; // 被操作的用户姓名（存入修改后的姓名到这里）
    private String create_time; // 记录创建时间
    private Integer operate_type; // 操作类型
    // （1，添加用户 2，修改用户信息 3，报停/恢复用户 4，删除/恢复用户 5，录入账单记录 6，修改用户用水量）
    private String update_message; // 具体操作信息

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(Integer operator_id) {
        this.operator_id = operator_id;
    }

    public String getOperator_name() {
        return operator_name;
    }

    public void setOperator_name(String operator_name) {
        this.operator_name = operator_name;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Integer getOperate_type() {
        return operate_type;
    }

    public void setOperate_type(Integer operate_type) {
        this.operate_type = operate_type;
    }

    public String getUpdate_message() {
        return update_message;
    }

    public void setUpdate_message(String update_message) {
        this.update_message = update_message;
    }

}
