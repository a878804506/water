package com.cyh.pojo;

public class CustomerPage implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    private Integer id; // 用户编号
    private String name; // 查询条件中的姓名
    private Integer pageNow; // 当前显示的页码
    private Integer pageSize = 10; // 每页显示的信息条数
    private Integer count; // 总的信息条数
    private Integer pageCount; // 总的页数

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
