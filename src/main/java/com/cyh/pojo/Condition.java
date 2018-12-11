package com.cyh.pojo;

//条件类
public class Condition implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    private Integer id; // 用户编号
    private Integer lastNumber; // 水表止码
    private Integer month; // 月份
    private String AB; // 大写

    public String getAB() {
        return AB;
    }

    public void setAB(String aB) {
        AB = aB;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLastNumber() {
        return lastNumber;
    }

    public void setLastNumber(Integer lastNumber) {
        this.lastNumber = lastNumber;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }
}
