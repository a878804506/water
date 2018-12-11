package com.cyh.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 做统一的异常处理
 * 
 * @author Administrator
 *
 */
public class MyExceptionResolver implements HandlerExceptionResolver {
    // 返回ModelAndView ex表示程序在运行过程中绑定的异常
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object arg2,
            Exception ex) {

        MyException ye = null; // 自定义的异常
        if (ex instanceof MyException) {
            ye = (MyException) ex;
        } else {
            // 出现的异常不是exception
            ye = new MyException("系统暂时智障，请重新刷新一哈子！");
        }
        ModelAndView mv = new ModelAndView();
        mv.addObject("error", ye.getMessage());
        mv.setViewName("jsp/error.jsp");
        return mv;
    }

}
