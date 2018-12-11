package com.cyh.pojo;

public class Operator_logPage implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    private Integer user_id; // 查询条件：用户编号
    private String user_name; // 查询条件：用户姓名
    private String create_date; // 查询条件：操作时间
    private Integer operate_type; // 查询条件：操作类型
    private Integer pageNow; // 当前显示的页码
    private Integer pageSize = 10; // 每页显示的信息条数
    private Integer count; // 总的信息条数
    private Integer pageCount; // 总的页数

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

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public Integer getOperate_type() {
        return operate_type;
    }

    public void setOperate_type(Integer operate_type) {
        this.operate_type = operate_type;
    }

    public Integer getPageNow() {
        return pageNow;
    }

    public void setPageNow(Integer pageNow) {
        this.pageNow = pageNow;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    // 计算总的页数
    public Integer getPageCount() {
        pageCount = count / pageSize;
        if (count % pageSize != 0) {
            pageCount++;
        }
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

}
