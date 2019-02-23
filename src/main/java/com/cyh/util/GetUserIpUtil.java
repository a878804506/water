package com.cyh.util;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public final class GetUserIpUtil {

    //获取系统登录用户的IP,并返回用户ip相关信息
    public static JSONObject getUserIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String realIP = null;
        String tempIP1  = request.getParameter("ip1"); //从页面上获取的上网ip1
        String tempIP2  = request.getParameter("ip2"); //从页面上获取的上网ip2
        if(StringUtils.isBlank(tempIP1)){
            if(StringUtils.isBlank(tempIP2)){
                //页面获取的两个ip都为空
                realIP = ip;
            }else{
                realIP = tempIP2;
            }
        }else{
            if(StringUtils.isBlank(tempIP2)){
                realIP = tempIP1;
            }else{
                if(tempIP1.equals(tempIP2)){
                    realIP = tempIP1;
                }else{
                    if(tempIP1.equals(ip)){
                        realIP = tempIP1;
                    }else if(tempIP2.equals(ip)){
                        realIP = tempIP2;
                    }else{
                        System.out.println("三个ip都不相等！");
                        realIP = ip;
                    }
                }
            }
        }
        return getAddressByIp(realIP);
    }

    private static JSONObject getAddressByIp(String IP){
        try{
            String str = getJsonContent("http://ip.taobao.com/service/getIpInfo.php?ip="+IP);
            if(StringUtils.isNotBlank(str))
                return JSONObject.fromObject(str);
        }catch(Exception e){
            return JSONObject.fromObject("{\"code\":1,\"ip\":\""+IP+"\"}");
        }
        return JSONObject.fromObject("{\"code\":1,\"ip\":\""+IP+"\"}");
    }

    private static String getJsonContent(String urlStr) {
        String jsonStr = "";
        try {// 获取HttpURLConnection连接对象
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            // 设置连接属性
            httpConn.setConnectTimeout(3000);
            httpConn.setDoInput(true);
            httpConn.setRequestMethod("GET");
            // 获取相应码
            int respCode = httpConn.getResponseCode();
            if (respCode == 200) {
                // ByteArrayOutputStream相当于内存输出流
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                // 将输入流转移到内存输出流中
                while ((len = httpConn.getInputStream().read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, len);
                }
                // 将内存流转换为字符串
                jsonStr = new String(out.toByteArray(),"utf8");
                return jsonStr;
            }
        } catch (Exception e) {
            return jsonStr;
        }
        return jsonStr;
    }
}
