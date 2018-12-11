package com.cyh.pojo;

public class Customer implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    private Integer cid;
    private String cname;
    private Double cprice;
    private String caddress;
    private String cstartime;
    private Integer cstatus;
    private Integer cdelete;
    private Integer updateBeforeCid; // 修改用户信息之前的原用户编号

    public Integer getUpdateBeforeCid() {
        return updateBeforeCid;
    }

    public void setUpdateBeforeCid(Integer updateBeforeCid) {
        this.updateBeforeCid = updateBeforeCid;
    }

    public Integer getCdelete() {
        return cdelete;
    }

    public void setCdelete(Integer cdelete) {
        this.cdelete = cdelete;
    }

    public Integer getCstatus() {
        return cstatus;
    }

    public void setCstatus(Integer cstatus) {
        this.cstatus = cstatus;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Double getCprice() {
        return cprice;
    }

    public void setCprice(Double cprice) {
        this.cprice = cprice;
    }

    public String getCaddress() {
        return caddress;
    }

    public void setCaddress(String caddress) {
        this.caddress = caddress;
    }

    public String getCstartime() {
        return cstartime;
    }

    public void setCstartime(String cstartime) {
        this.cstartime = cstartime;
    }

}
