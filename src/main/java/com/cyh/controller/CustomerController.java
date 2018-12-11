package com.cyh.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.cyh.pojo.*;
import com.cyh.service.*;
import com.cyh.util.CommonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cyh.util.GetTotalCountByMonth;
import com.cyh.util.Operator_logUtil;
import com.google.gson.Gson;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BillService billService;
    @Autowired
    private MonthBillService monthBillService;
    @Autowired
    private Operator_logService operator_logService;
    @Autowired
    private WaterConsumptionService waterConsumptionService;
    @Autowired
    private UtilService utilService;

    // 添加用户
    @ResponseBody
    @RequestMapping(value = "addCustomer" , method = RequestMethod.POST, produces = "text/html;charset=utf-8")
    public String addCustomer(Customer customer, HttpSession session) {
        try{
            int count = customerService.getAllCustomerCount(); // 添加之前，数据库最大cid
            customer.setCid(count+1);
            customerService.addCustomer(customer);
            billService.addBill(customer); // 添加账单
            monthBillService.addMBill(customer); // 添加账单
            waterConsumptionService.addWBill(customer);
            Operator_logUtil.insertOperator_log(customer, 1, session, customerService, null, null, operator_logService); // 添加日志
            return "添加成功！";
        }catch (Exception e){
            e.printStackTrace();
            return "添加失败！";
        }
    }

    // 查询所有用户
    @RequestMapping("showCustomer")
    public ModelAndView showCustomer(CustomerPage cp) throws Exception {
        if (cp.getPageNow() == null) {
            cp.setPageNow(1);
        }
        cp.setPageNow((cp.getPageNow() - 1) * cp.getPageSize());
        List<Customer> list = customerService.showCustomer(cp);
        ModelAndView mv = new ModelAndView();
        Gson gson = new Gson();
        mv.addObject("list", gson.toJson(list));
        cp.setCount(customerService.getCustomerCount(cp)); // 查询总的信息条数(如果有条件，按时按照条件查询后的总的条数)
        // 应该再次逆向计算当前的页码
        cp.setPageNow(cp.getPageNow() / cp.getPageSize() + 1);
        mv.addObject("page", cp);
        mv.setViewName("jsp/showCustomer.jsp");
        return mv;
    }

    // 启用、报停用户
    @ResponseBody
    @RequestMapping(value = "status" , method = RequestMethod.POST, produces = "text/html;charset=utf-8")
    public String delete(int cid,HttpSession session){
        try {
            Customer customer = customerService.getCustomerById(cid);
            if (customer.getCstatus() == 1) {
                customer.setCstatus(0);
            } else {
                customer.setCstatus(1);
            }
            Operator_logUtil.insertOperator_log(customer, 3, session, customerService, null, null, operator_logService); // 添加日志
            customerService.delete(customer);
            return "操作成功！";
        }catch (Exception e){
            e.printStackTrace();
            return "操作失败！";
        }

    }

    // 删除用户 删除时 不仅要修改customer表中的删除状态 还需要修改bill表中的 cdelete 字段
    @ResponseBody
    @RequestMapping(value = "delete" , method = RequestMethod.POST, produces = "text/html;charset=utf-8")
    public String deletes(int cid, HttpSession session){
        try {
            Customer customer = customerService.getCustomerById(cid);
            Bill bill = new Bill();
            bill.setUid(cid);
            if (customer.getCdelete() == 1) {
                bill.setCdelete(0);
                customer.setCdelete(0);
            } else {
                bill.setCdelete(1);
                customer.setCdelete(1);
            }
            billService.updateCdelete(bill); // 同时修改 bill表中的 Cdelete 字段
            customerService.deletes(customer);
            Operator_logUtil.insertOperator_log(customer, 4, session, customerService, null, null, operator_logService); // 添加日志
            return "操作成功！";
        }catch (Exception e){
            e.printStackTrace();
            return "操作失败！";
        }
    }

    // 查询所有已删除的用户（按分页查询）
    @RequestMapping("showDeleteCustomer")
    public ModelAndView showDeleteCustomer(CustomerPage cp) throws Exception {
        if (cp.getPageNow() == null) {
            cp.setPageNow(1);
        }
        cp.setPageNow((cp.getPageNow() - 1) * cp.getPageSize());
        List<Customer> list = customerService.selectDelete(cp);
        ModelAndView mv = new ModelAndView();
        Gson gson = new Gson();
        mv.addObject("list", gson.toJson(list));
        cp.setCount(customerService.getDCustomerCount(cp)); // 查询总的信息条数(如果有条件，按时按照条件查询后的总的条数)
        // 应该再次逆向计算当前的页码
        cp.setPageNow(cp.getPageNow() / cp.getPageSize() + 1);
        mv.addObject("page", cp);
        mv.setViewName("jsp/showDeleteCustomer.jsp");
        return mv;
    }

    // 修改用户信息
    @ResponseBody
    @RequestMapping(value = "toupdateCustomer" , method = RequestMethod.POST, produces = "text/html;charset=utf-8")
    public String toupdateCustomer(Customer customer, HttpSession session){
        try {
            Operator_logUtil.insertOperator_log(customer, 2, session, customerService, null, null, operator_logService); // 添加日志
            customerService.updateCustomer(customer);
            billService.updateBill(customer); // 修改了个人信息表后 账单表里的信息也要改
            monthBillService.updateMBill(customer);// 修改了个人信息表后 月账单表里的信息也要改
            waterConsumptionService.updateWBill(customer);// 修改了个人信息表后 月账单表里的信息也要改
            return "修改成功！";
        }catch (Exception e){
            e.printStackTrace();
            return "修改失败！";
        }
    }

    // 右下拉框的查询 方便用户点击
    @RequestMapping("idAndCustomer")
    // HttpServletRequest req 从左联框架的超链接中获取连接标识符，用于在 bill.jsp 页面的不同展现
    public ModelAndView showIdAndCustomer(HttpSession session, HttpServletRequest req)  {
        if (session.getAttribute("mm") == null) {// 默认第一次进去的时候 月份下拉框为一月份
            session.setAttribute("mm", 1);
        }
        if (req.getParameter("password").equals("2")) { // 如果点击 1-2月份个人水费账单 进来 设置默认月份为2月份
            session.setAttribute("mm", 2);
        }
        ModelAndView mv = new ModelAndView();

        // 统计的当天开票数量并展示
        TransitionModel tm = new TransitionModel(); // 记录客户各月用水量和水费 综合表
        tm.setCreate_time(CommonUtil.DateToString(new Date(),"yyyy-MM-dd"));
        int operate_cutomer_count_date = utilService.getOperateCutomerCountByToDate(tm);
        mv.addObject("operate_cutomer_count_date", operate_cutomer_count_date);

        // 获取当月录入用户总数
        mv.addObject("totalMonthCount",
                GetTotalCountByMonth.getTotalCountByMonth(monthBillService, (Integer) session.getAttribute("mm")));

        mv.addObject("pwd", req.getParameter("password"));
        mv.addObject("countSize", customerService.getAllCustomerCount()); // 用户总数 传到生成账单的页面上
        if (req.getParameter("symbol") == null || req.getParameter("symbol").equals("0")) {
            // 标识符号 用于判断点击‘导出Excel’按钮时，是否生成了收据 0：未点击’生成收据‘按钮 1：已点击
            mv.addObject("symbol", 0);
        } else {
            mv.addObject("symbol", 1);
        }
        mv.setViewName("jsp/bill.jsp");
        return mv;
    }

    // 表单提交前，根据输入的月份和用户编号--->校验输入止码的正确性
    @ResponseBody
    @RequestMapping(value = "checkWaterMeterLastNumber", produces = "text/html;charset=utf-8")
    public String checkWaterMeterLastNumber(int month, int customerNum){
        Condition condition = new Condition(); // 创建一个条件类 用于传递参数
        switch (month) {
        case 1:
            condition.setAB("thirteen");
            break;
        case 2:
            condition.setAB("one");
            break;
        case 3:
            condition.setAB("two");
            break;
        case 4:
            condition.setAB("three");
            break;
        case 5:
            condition.setAB("four");
            break;
        case 6:
            condition.setAB("five");
            break;
        case 7:
            condition.setAB("six");
            break;
        case 8:
            condition.setAB("seven");
            break;
        case 9:
            condition.setAB("eight");
            break;
        case 10:
            condition.setAB("nine");
            break;
        case 11:
            condition.setAB("ten");
            break;
        case 12:
            condition.setAB("eleven");
            break;
        case 13:
            condition.setAB("thirteen");
            break;
        }
        condition.setId(customerNum);
        int WaterMeterNumber = billService.getWaterMeterNumberByCustomerNumAndMonth(condition);
        return "{\"WaterMeterNumber\":\"" + WaterMeterNumber + "\"}";
    }

    // ajax
    @ResponseBody
    @RequestMapping(value = "ajax", produces = "text/html;charset=utf-8")
    public String ajax(String name){
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        if (StringUtils.isNumeric(name)) {
            List<Customer> ll = customerService.getCustomerByLikeId(Integer.valueOf(name));
            Gson gson = new Gson();
            String json = gson.toJson(ll);
            return json;
        }
        List<Customer> ll = customerService.getCustomerByName(name);
        Gson gson = new Gson();
        String json = gson.toJson(ll);
        return json;
    }

    // 查询月度水费情况
    @ResponseBody
    @RequestMapping(value = "zongshuifei", produces = "text/html;charset=utf-8")
    public String zongshuifei(Integer name){
        List<MonthBill> mb = monthBillService.getMBill();
        MonthBill monthBill = new MonthBill();
        int count = 0; // 统计实际录入水费了的用户数量
        Double sum = 0.0; // 总水费
        int countZong = mb.size(); // 统计总的用户数量
        switch (name) { // name ： 月份
        case 1:
            for (int i = 0; i < countZong; i++) {
                monthBill = mb.get(i);
                if (monthBill.getMone() != 0) {
                    count++;
                }
                sum += monthBill.getMone();
            }
            break;
        case 2:
            for (int i = 0; i < countZong; i++) {
                monthBill = mb.get(i);
                if (monthBill.getMtwo() != 0) {
                    count++;
                }
                sum += monthBill.getMtwo();
            }
            break;
        case 3:
            for (int i = 0; i < countZong; i++) {
                monthBill = mb.get(i);
                if (monthBill.getMthree() != 0) {
                    count++;
                }
                sum += monthBill.getMthree();
            }
            break;
        case 4:
            for (int i = 0; i < countZong; i++) {
                monthBill = mb.get(i);
                if (monthBill.getMfour() != 0) {
                    count++;
                }
                sum += monthBill.getMfour();
            }
            break;
        case 5:
            for (int i = 0; i < countZong; i++) {
                monthBill = mb.get(i);
                if (monthBill.getMfive() != 0) {
                    count++;
                }
                sum += monthBill.getMfive();
            }
            break;
        case 6:
            for (int i = 0; i < countZong; i++) {
                monthBill = mb.get(i);
                if (monthBill.getMsix() != 0) {
                    count++;
                }
                sum += monthBill.getMsix();
            }
            break;
        case 7:
            for (int i = 0; i < countZong; i++) {
                monthBill = mb.get(i);
                if (monthBill.getMseven() != 0) {
                    count++;
                }
                sum += monthBill.getMseven();
            }
            break;
        case 8:
            for (int i = 0; i < countZong; i++) {
                monthBill = mb.get(i);
                if (monthBill.getMeight() != 0) {
                    count++;
                }
                sum += monthBill.getMeight();
            }
            break;
        case 9:
            for (int i = 0; i < countZong; i++) {
                monthBill = mb.get(i);
                if (monthBill.getMnine() != 0) {
                    count++;
                }
                sum += monthBill.getMnine();
            }
            break;
        case 10:
            for (int i = 0; i < countZong; i++) {
                monthBill = mb.get(i);
                if (monthBill.getMten() != 0) {
                    count++;
                }
                sum += monthBill.getMten();
            }
            break;
        case 11:
            for (int i = 0; i < countZong; i++) {
                monthBill = mb.get(i);
                if (monthBill.getMeleven() != 0) {
                    count++;
                }
                sum += monthBill.getMeleven();
            }
            break;
        case 12:
            for (int i = 0; i < countZong; i++) {
                monthBill = mb.get(i);
                if (monthBill.getMtwelve() != 0) {
                    count++;
                }
                sum += monthBill.getMtwelve();
            }
            break;
        }
        // 处理double类型数据 防止出现 10036.100000000004 类似错误！
        sum = new BigDecimal(sum).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        // 获取报停的用户数
        int baoting = customerService.getBTC(new CustomerPage());
        // 获取被删除的用户数
        int delete = customerService.getDCustomerCount(new CustomerPage());
        // 应该录入数
        int yinggai = countZong - baoting - delete ;
        // 还未录入数
        int no = yinggai - count;
        Map<String,Object> results = new HashMap<>();
        results.put("shiji",count);
        results.put("cost",sum);
        results.put("total",countZong);
        results.put("baoting",baoting);
        results.put("delete",delete);
        results.put("yinggai",yinggai);
        results.put("no",no);
        //当月未录入用户详情
        List<Map<String,Object>> tempCustomer = customerService.getNotImportCustomerByMonth(EchartsController.getMonthbillColumn(name));
        results.put("notImportCustomer",tempCustomer);
        return JSONObject.toJSONString(results);
    }

    // 进入“查询月度录入水费情况”功能前，先查出所有用户数量，方便区间查询
    @RequestMapping("before")
    public ModelAndView before(){
        ModelAndView mv = new ModelAndView();
        mv.addObject("toMonth", new SimpleDateFormat("M").format(new Date()));
        mv.setViewName("jsp/zongshuifei.jsp");
        return mv;
    }

    // 查看已报停的用户
    @RequestMapping("baotingCustomer")
    public ModelAndView selectC(CustomerPage cp){
        cp.setName("辉辉");
        if (cp.getPageNow() == null) {
            cp.setPageNow(1);
        }
        cp.setPageNow((cp.getPageNow() - 1) * cp.getPageSize());
        List<Customer> list = customerService.getBT(cp);
        ModelAndView mv = new ModelAndView();
        cp.setCount(customerService.getBTC(cp)); // 查询总的信息条数(如果有条件，按时按照条件查询后的总的条数)
        // 应该再次逆向计算当前的页码
        cp.setPageNow(cp.getPageNow() / cp.getPageSize() + 1);
        mv.addObject("page", cp);
        Gson gson = new Gson();
        mv.addObject("list", gson.toJson(list));
        mv.setViewName("jsp/customerBT.jsp");
        return mv;
    }
}
