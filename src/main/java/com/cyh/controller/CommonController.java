package com.cyh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommonController {

    // 项目使用了frame框架，并且jsp都放在了 WEB-INF 下  ，所以frame所引用的jsp 都只能通过重定向来获取
    @RequestMapping("getJSPForFrame")
    public String CUser(String flag){
        String jsp = "redirect:";
        if("top".equals(flag)){
            jsp += "top";
        }else if("left".equals(flag)){
            jsp += "left";
        }else if("index".equals(flag)){
            jsp += "index";
        }else if("webSocketChat".equals(flag)){
            jsp += "webSocketChat";
        }else{
            jsp += "login";
        }
        return jsp;
    }
}
