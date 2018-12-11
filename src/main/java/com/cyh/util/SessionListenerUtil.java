package com.cyh.util;

import com.cyh.common.Constants;
import com.cyh.pojo.User;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SessionListenerUtil implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent htpSessionEvent) {

        String sessionId = htpSessionEvent.getSession().getId();
        //nginx 代理用户头像所在服务器
        htpSessionEvent.getSession().setAttribute("staticPictureHost", Constants.staticPictureHost);
        System.out.println("["+CommonUtil.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss")+"]有客户访问系统，sessionId为："+sessionId);
    }

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

    //将redis中得用户状态改为离线
    public static void updateUserStrutsToOffLine(User user){
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
        }catch (Exception e){
            e.printStackTrace();
            RedisDB.returnBrokenResource(jedis);
        }finally {
            RedisDB.returnResource(jedis);
        }
    }
}
