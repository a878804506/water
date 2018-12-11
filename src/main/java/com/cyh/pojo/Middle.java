package com.cyh.pojo;

//过度条件类
public class Middle implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    private Integer uid; // 用户编号
    private String uname; // 用户姓名
    private Integer month; // 月份
    private Integer firstNumber; // 上个月止码（本月的起码）
    private Integer lastNumber; // 这个月的止码
    private Double price; // 单价
    private String address; // 用户住址
    private Double total; // 总价(没有加5)
    private Double firstTotal; // 总价
    private String daxie; // 存储大写金额
    private String wan;
    private String qian;
    private String bai;
    private String shi;
    private String yuan;
    private String wan1;
    private String qian1;
    private String bai1;
    private String shi1;
    private String yuan1;
    private String jiao;
    private String wu;
    private String ling;
    private Integer year; // 获取操作系统时当前年份
    private Integer twoandtwo; // 如果是连开两个月的票据，那么容量水费应该是10元
    private String lianyue; // 连着两个月开票的月份显示

    public String getLianyue() {
        return lianyue;
    }

    public void setLianyue(String lianyue) {
        this.lianyue = lianyue;
    }

    public Integer getTwoandtwo() {
        return twoandtwo;
    }

    public void setTwoandtwo(Integer twoandtwo) {
        this.twoandtwo = twoandtwo;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getWu() {
        return wu;
    }

    public void setWu(String wu) {
        this.wu = wu;
    }

    public String getLing() {
        return ling;
    }

    public void setLing(String ling) {
        this.ling = ling;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getFirstNumber() {
        return firstNumber;
    }

    public void setFirstNumber(Integer firstNumber) {
        this.firstNumber = firstNumber;
    }

    public Integer getLastNumber() {
        return lastNumber;
    }

    public void setLastNumber(Integer lastNumber) {
        this.lastNumber = lastNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWan() {
        return wan;
    }

    public void setWan(String wan) {
        this.wan = wan;
    }

    public String getQian() {
        return qian;
    }

    public void setQian(String qian) {
        this.qian = qian;
    }

    public String getBai() {
        return bai;
    }

    public void setBai(String bai) {
        this.bai = bai;
    }

    public String getShi() {
        return shi;
    }

    public void setShi(String shi) {
        this.shi = shi;
    }

    public String getYuan() {
        return yuan;
    }

    public void setYuan(String yuan) {
        this.yuan = yuan;
    }

    public String getJiao() {
        return jiao;
    }

    public void setJiao(String jiao) {
        this.jiao = jiao;
    }

    public String getWan1() {
        return wan1;
    }

    public void setWan1(String wan1) {
        this.wan1 = wan1;
    }

    public String getQian1() {
        return qian1;
    }

    public void setQian1(String qian1) {
        this.qian1 = qian1;
    }

    public String getBai1() {
        return bai1;
    }

    public void setBai1(String bai1) {
        this.bai1 = bai1;
    }

    public String getShi1() {
        return shi1;
    }

    public void setShi1(String shi1) {
        this.shi1 = shi1;
    }

    public String getYuan1() {
        return yuan1;
    }

    public void setYuan1(String yuan1) {
        this.yuan1 = yuan1;
    }

    public String getDaxie() {
        return daxie;
    }

    public void setDaxie(String daxie) {
        this.daxie = daxie;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getFirstTotal() {
        return firstTotal;
    }

    public void setFirstTotal(Double firstTotal) {
        this.firstTotal = firstTotal;
    }

}
