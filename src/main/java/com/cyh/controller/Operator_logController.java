package com.cyh.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cyh.pojo.Operator_log;
import com.cyh.pojo.Operator_logPage;
import com.cyh.service.Operator_logService;

@Controller
public class Operator_logController {
    @Autowired
    private Operator_logService operator_logService;

    @RequestMapping("showOperatot_log")
    public ModelAndView showOperatot_log(Operator_logPage op, int password) {
        if (op.getPageNow() == null) {
            op.setPageNow(1);
        }
        op.setPageNow((op.getPageNow() - 1) * op.getPageSize());
        List<Operator_log> list = operator_logService.showOperator_log(op);
        ModelAndView mv = new ModelAndView();
        mv.addObject("list", list);
        op.setCount(operator_logService.getoperator_logCount(op)); // 查询总的信息条数(如果有条件，按时按照条件查询后的总的条数)
        // 应该再次逆向计算当前的页码
        op.setPageNow(op.getPageNow() / op.getPageSize() + 1);
        mv.addObject("page", op);
        mv.setViewName("jsp/showOperator_log.jsp");
        mv.addObject("password", password);
        return mv;
    }
}
