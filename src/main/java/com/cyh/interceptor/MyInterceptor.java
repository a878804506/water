package com.cyh.interceptor;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cyh.exception.MyException;
import com.cyh.pojo.MenuPermission;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cyh.pojo.User;

//springMVC的拦截器就是实现了handlerInterceptor接口
//拦截器类将会在所有的action执行前被执行
public class MyInterceptor implements HandlerInterceptor {

    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {

    }

    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {

    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        // 从session中取出登录对象
        User user = (User) request.getSession().getAttribute("user");
        // 如果是登录，必须让他进入到登录的handler，（登录的handler应该放行）
        //url : http://192.168.6.6:9090/abc/CUser.action
        String url = request.getRequestURL().toString();
        if (null != user) {
            if (url.endsWith("/CUser.action")) {
                //刷新界面 应放行
                return true;
            }
            if (url.endsWith("/getJSPForFrame.action")) {
                //frame框架加载jsp  如果user不为空 就放行
                return true;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            /*
            已在用户登录的代码中设置session失效时间
            if (new Date().getTime() - sdf.parse(user.getTime()).getTime() > 1000 * 60 * 60 * 4) {
                request.getSession().removeAttribute("user");
                response.sendRedirect("login");
                return false; // 拦截
            }else */
                if(url.endsWith("/out.action")){
                //用户退出 全部放行
                return true;
            }else if(url.endsWith("/checkUserPermission.action")){
                //系统用户的权限检查  应当放行
                return true;
            } else {
                List<MenuPermission> permissionList = (List<MenuPermission>) request.getSession().getAttribute("permissionList");
                for (MenuPermission permission : permissionList){
                    if(StringUtils.isNotBlank(permission.getUrl())){
                        if(url.endsWith(permission.getUrl())){
                            return true;
                        }
                    }
                }
                throw new MyException("您没有相应的权限操作，请联系管理员！");
            }
        } else {
            if (url.endsWith("/CUser.action")) {
                //用户登录 全部放行
                return true;
            } else {
                response.sendRedirect("login");
                return false; // 拦截
            }
        }
    }

}
