package com.cyh.util;

import com.cyh.common.Constants;
import com.cyh.service.UserService;
import com.cyh.util.zookeeperUtil.ZookeeperConnectSessionWatcherUtil;
import redis.clients.jedis.Jedis;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommonUtil {

    /**
     *
     * @param date  传入的时间
     * @param format 需要格式化的 标准字符串 如 yyyy-MM-dd
     * @return 格式化后的时间字符串
     */
    public static String DateToString(Date date,String format){
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 启动加载用户信息到redis中
     */
    public static void getAllStrutsUsersToRedis() {
        if(Constants.webSocketChatSwitch){ //开启项目的聊天功能
            Jedis jedis = null;
            try{
                jedis = RedisDB.getJedis();
                jedis.select(RedisDB.dbSelectedForSystem);
                UserService userService = (UserService) SpringContextUtil.getBean("userService");
                List<Map<String,Object>> allStrutsUsers = userService.getAllStrutsUsers();
                //用户列表都加上图片服务器路径
                for (Map temp : allStrutsUsers) {
                    temp.put("img",Constants.staticPictureHost + temp.get("img").toString());
                    temp.put("grayImg",Constants.staticPictureHost + temp.get("grayImg").toString());
                }
                //按照昵称排序
                Collections.sort(allStrutsUsers, (o1, o2) -> {
                    String nickName1 = (String)o1.get("nickName");
                    String nickName2 = (String)o2.get("nickName");
                    return nickName1.compareTo(nickName2);
                });
                jedis.set(RedisDB.systemUsers.getBytes(), SerializeUtil.serialize(allStrutsUsers));

                //启动zookeeper保活线程
                ZookeeperConnectSessionWatcherUtil zcsw = new ZookeeperConnectSessionWatcherUtil();
                Thread zookeeperReConnectThread = new Thread(zcsw);
                zookeeperReConnectThread.start();
            }catch (Exception e){
                e.printStackTrace();
                RedisDB.returnBrokenResource(jedis);
            }finally {
                RedisDB.returnResource(jedis);
            }
        }else{
            System.out.println("WebSocketChat聊天功能模块没有开启使用");
        }
    }
}
