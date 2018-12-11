package com.cyh.common;

/**
 *
 *  1、常量
 *  2、解析配置文件中的属性保存于此
 */
public class Constants {
    //定时任务调度总开关
    public static final Boolean timedTaskSwitch = "true".equalsIgnoreCase(Configuration.propMap.get("system.timedTaskSwitch"))?true:false;
    //备份重置数据库表任务开关
    public static final Boolean backupAndResetTables_JOB_Switch = "true".equalsIgnoreCase(Configuration.propMap.get("system.backupAndResetTables_JOB_Switch"))?true:false;

    //ftp 主机名
    public static final String ftp_hostname = Configuration.propMap.get("ftp.hostname");
    //ftp 端口
    public static final int ftp_port = Integer.valueOf(Configuration.propMap.get("ftp.port"));
    //ftp 用户名
    public static final String ftp_username = Configuration.propMap.get("ftp.username");
    //ftp 密码
    public static final String ftp_password = Configuration.propMap.get("ftp.password");

    //nginx 代理用户头像所在服务器
    public static String staticPictureHost = Configuration.propMap.get("ftp.staticPictureHost");

    // temp 文件夹绝对路径
    public static final String filePath = "/"+Constants.class.getResource("/").getPath().replaceFirst("/", "").replaceAll("WEB-INF/classes/", "temp");

    /*项目的聊天功能*/
    public static boolean webSocketChatSwitch = Boolean.parseBoolean(Configuration.propMap.get("system.webSocketChatSwitch"));

    //webSocketChat程序所在服务器  用户登录后页面js连接webSocket时所用  基于system.webSocketChatSwitch为true时有用
    public static final String webSocketChatAddress = Configuration.propMap.get("system.webSocketChatAddress");
}
