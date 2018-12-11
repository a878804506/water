package com.cyh.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cyh.pojo.ConfigureInfo;
import com.cyh.pojo.User;
import com.cyh.service.UtilService;

@Controller
public class UtilController {

    @Autowired
    private UtilService utilService;

    // 配置excel路径
    @ResponseBody
    @RequestMapping(value = "excelSrcSetting" , method = RequestMethod.POST, produces = "text/html;charset=utf-8")
    public String excelSrcSetting(String excelSrc, HttpSession session) {
        try {
            if(excelSrc.length() == 0 || excelSrc == null || excelSrc == ""){
                return "参数错误！";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            User loginuser = (User) session.getAttribute("user");
            ConfigureInfo configureInfo = new ConfigureInfo();
            configureInfo.setName("excel");
            configureInfo.setPath(excelSrc);
            configureInfo.setTime(sdf.format(new Date()));
            configureInfo.setUpdate_user(loginuser.getName());
            utilService.excelSrcSetting(configureInfo);
            return "保存成功！";
        }catch (Exception e){
            e.printStackTrace();
            return "保存失败！";
        }
    }

    // 查询excel路径
    @RequestMapping(value = "getExcelPath", produces = "text/html;charset=utf-8")
    public ModelAndView getExcelPathByName(){
        ModelAndView mv = new ModelAndView();
        ConfigureInfo excelSettings = utilService.getConfigureInfoByName("excel");
        mv.addObject("excelSettings", excelSettings);
        mv.setViewName("jsp/excelSettings.jsp");
        return mv;
    }

    //时间格式化
    public static String getDateFormat(Date date, String p){
        SimpleDateFormat sdf = new SimpleDateFormat(p);
        return sdf.format(date);
    }
}
