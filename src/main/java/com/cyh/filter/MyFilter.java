package com.cyh.filter;

import com.cyh.common.Constants;
import com.cyh.pojo.User;
import com.cyh.util.CommonUtil;
import com.cyh.util.timerTaskUtil.TimerRun;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class MyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        System.out.println("调用init方法");
        //项目启动加载用户信息到redis中
        CommonUtil.getAllStrutsUsersToRedis();

        if(Constants.timedTaskSwitch){ //定时任务总开关
            //进行任务调度
            new TimerRun().run();
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse rep = (HttpServletResponse) response;
        HttpServletRequest req =(HttpServletRequest) request;
        String url = ((HttpServletRequest)request).getRequestURI();
        System.out.println("["+CommonUtil.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss")+"]调用doFilter方法,url是："+url);
        //允许加载静态文件
        if(url.endsWith(".js") || url.endsWith(".jpg") || url.endsWith(".gif") || url.endsWith(".png") ||url.endsWith(".css")){
            chain.doFilter(request,response);
            return;
        }
        //登陆、登陆验证放行
        if(url.indexOf("login") != -1 || url.indexOf("CUser.action") != -1){
            chain.doFilter(request,response);
            return ;
        }else{
            User loginUser = (User) req.getSession().getAttribute("user");
            if(loginUser == null){
                rep.sendRedirect("login");
                return ;
            }else{
                chain.doFilter(request,response);
                return ;
            }
        }
    }

    @Override
    public void destroy() {
        System.out.println("调用destory方法");
    }
}
