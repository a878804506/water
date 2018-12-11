package com.cyh.pojo;

public class User implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private String password;
    private String time;
    private String all_time;
    private String last_time;
    private String super_man;
    private String ip; // 用户上网ip
    private String userRegion; //用户ip所在省
    private String userCity; // 用户所在城市
    private String struts;//1:账号可用，0：账号不可用
    private String yzm; //用户登录验证码
    private String nickName;//昵称
    private String img;//头像
    private String grayImg;//头像

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAll_time() {
        return all_time;
    }

    public void setAll_time(String all_time) {
        this.all_time = all_time;
    }

    public String getLast_time() {
        return last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }

    public String getSuper_man() {
        return super_man;
    }

    public void setSuper_man(String super_man) {
        this.super_man = super_man;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getStruts() {
        return struts;
    }

    public void setStruts(String struts) {
        this.struts = struts;
    }

    public String getUserRegion() {
        return userRegion;
    }

    public void setUserRegion(String userRegion) {
        this.userRegion = userRegion;
    }

    public String getYzm() {
        return yzm;
    }

    public void setYzm(String yzm) {
        this.yzm = yzm;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getGrayImg() {
        return grayImg;
    }

    public void setGrayImg(String grayImg) {
        this.grayImg = grayImg;
    }
}
