package com.cyh.util;

import com.cyh.common.Constants;
import com.cyh.pojo.User;
import com.cyh.util.zookeeperUtil.ZookeeperUtil;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SessionListenerUtil implements HttpSessionListener,Runnable {

    private int updateToRedis_UserId; //即将更新最新列表状态时目标（登陆）用户的id

    public SessionListenerUtil(){
    }

    public SessionListenerUtil(int updateToRedis_UserId){
        this.updateToRedis_UserId = updateToRedis_UserId;
    }

    //创建session监听
    @Override
    public void sessionCreated(HttpSessionEvent htpSessionEvent) {
        String sessionId = htpSessionEvent.getSession().getId();
        //nginx 代理用户头像所在服务器
        htpSessionEvent.getSession().setAttribute("staticPictureHost", Constants.staticPictureHost);
        System.out.println("["+CommonUtil.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss")+"]有客户访问系统，sessionId为："+sessionId);
    }
    //销毁session监听
    @Override
    public void sessionDestroyed(HttpSessionEvent htpSessionEvent) {
        HttpSession session = htpSessionEvent.getSession();
        System.out.println("["+CommonUtil.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss")+"]有session失效，sessionId为："+ session.getId());
        User user = (User) session.getAttribute("user");
        if(null != user){
            System.out.println("["+CommonUtil.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss")+"]该用户为："+ user.getId()+ ",昵称为："+user.getNickName());
            session.removeAttribute("user");
            //redis 中该用户状态改为离线
            if(Constants.webSocketChatSwitch) { //开启了项目的聊天功能
                updateUserStrutsToOffLine(user);
            }
        }else{
            System.out.println("["+CommonUtil.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss")+"]未登录的游客！");
        }
    }

    //销毁session时将redis中得用户状态改为离线,，并且再zk的waterUserForOnline节点下删除一个有规律的节点标识
    private static void updateUserStrutsToOffLine(User user){
        Jedis jedis = null;
        try {
            jedis = RedisDB.getJedis();
            jedis.select(RedisDB.dbSelectedForSystem);
            //再次从redis获取联系人列表
            List<Map<String,Object>> redis_contactsList = SerializeUtil.unserializeForList(jedis.get(RedisDB.systemUsers.getBytes()));
            Iterator<Map<String,Object>> it = redis_contactsList.iterator();
            Map<String,Object> thisUser = null;
            while (it.hasNext()){
                Map<String,Object> temp = it.next();
                if(temp.get("id").equals(user.getId())){
                    thisUser = temp;
                    thisUser.put("isOnline",false);
                    it.remove();
                    break;
                }
            }
            redis_contactsList.add(thisUser);
            jedis.set(RedisDB.systemUsers.getBytes(), SerializeUtil.serialize(redis_contactsList));

            //移除相应的子节点
            ZookeeperUtil.zkDeleteSon(user.getId());
        }catch (Exception e){
            e.printStackTrace();
            RedisDB.returnBrokenResource(jedis);
        }finally {
            RedisDB.returnResource(jedis);
        }
    }

    //用户登陆时启动线程将redis中得用户状态改为在线，并且再zk的waterUserForOnline节点下增加一个有规律的节点标识
    @Override
    public void run() {
        Jedis jedis = null;
        try {
            jedis = RedisDB.getJedis();
            jedis.select(RedisDB.dbSelectedForSystem);
            //从redis获取联系人列表
            List<Map<String,Object>> redis_contactsList = SerializeUtil.unserializeForList(jedis.get(RedisDB.systemUsers.getBytes()));
            Iterator<Map<String,Object>> it = redis_contactsList.iterator();
            Map<String,Object> thisUser = null;
            while (it.hasNext()){
                Map<String,Object> temp = it.next();
                if(temp.get("id").equals(updateToRedis_UserId)){
                    thisUser = temp;
                    thisUser.put("isOnline",true);
                    it.remove();
                    break;
                }
            }
            redis_contactsList.add(0,thisUser);
            jedis.set(RedisDB.systemUsers.getBytes(), SerializeUtil.serialize(redis_contactsList));

            //在zookeeper上通知聊天架构程序及时更新最新的在线联系人列表
            ZookeeperUtil.zkCreateSon(updateToRedis_UserId);
        }catch (Exception e){
            e.printStackTrace();
            RedisDB.returnBrokenResource(jedis);
        }finally {
            RedisDB.returnResource(jedis);
        }
    }
}
