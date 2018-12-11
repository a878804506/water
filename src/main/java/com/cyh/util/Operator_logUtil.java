package com.cyh.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import com.cyh.pojo.Bill;
import com.cyh.pojo.Customer;
import com.cyh.pojo.Middle;
import com.cyh.pojo.Operator_log;
import com.cyh.pojo.User;
import com.cyh.service.BillService;
import com.cyh.service.CustomerService;
import com.cyh.service.Operator_logService;

public class Operator_logUtil {
    /*
     * object : 活对象 可以是Bill 可以是 Customer type: 操作类型，可拓展 session ：里面有操作人信息
     * customerService： mi ： 录入账单记录时，会用到 billService ： operator_logService ： 插入日志表
     */
    public static void insertOperator_log(Object object, int type, HttpSession session, CustomerService customerService,
            Middle mi, BillService billService, Operator_logService operator_logService) {
        // 获取session中的登录信息，并且添加操作日志
        User loginuser = (User) session.getAttribute("user");
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date();
        Operator_log operator_log = new Operator_log();
        operator_log.setOperator_id(loginuser.getId());
        operator_log.setOperator_name(loginuser.getName());
        operator_log.setCreate_time(date.format(d));
        switch (type) {
        case 1:
            Customer customer = (Customer) object;
            operator_log.setUser_id(customer.getCid());
            operator_log.setUser_name(customer.getCname());
            operator_log.setOperate_type(1);
            operator_log.setUpdate_message("添加用户:" + customer.getCname() + "，水费定价:" + customer.getCprice() + "，地址:"
                    + customer.getCaddress() + "，入户时间:" + customer.getCstartime() + "。");
            break;
        case 2:
            Customer customer2 = (Customer) object;
            Customer c = customerService.getCustomerById(customer2.getCid()); // 原用户信息
            operator_log.setUser_id(customer2.getCid());
            operator_log.setUser_name(customer2.getCname());
            operator_log.setOperate_type(2);
            operator_log.setUpdate_message("修改前:" + c.getCname() + "，水费定价:" + c.getCprice() + "，地址:" + c.getCaddress()
                    + "，入户时间:" + c.getCstartime() + "；<br />" + "修改后:" + customer2.getCname() + "，水费定价:"
                    + customer2.getCprice() + "，地址:" + customer2.getCaddress() + "，入户时间:" + customer2.getCstartime()
                    + "。");
            break;
        case 3:
            Customer customer3 = (Customer) object;
            operator_log.setUser_id(customer3.getCid());
            operator_log.setUser_name(customer3.getCname());
            operator_log.setOperate_type(3);
            if (customer3.getCstatus() == 0) {
                operator_log.setUpdate_message("报停用户:" + customer3.getCname() + "。");
            } else if (customer3.getCstatus() == 1) {
                operator_log.setUpdate_message("恢复报停用户:" + customer3.getCname() + "。");
            }
            break;

        case 4:
            Customer customer4 = (Customer) object;
            operator_log.setUser_id(customer4.getCid());
            operator_log.setUser_name(customer4.getCname());
            operator_log.setOperate_type(4);
            if (customer4.getCdelete() == 0) {
                operator_log.setUpdate_message("删除用户：" + customer4.getCname() + "。");
            } else if (customer4.getCdelete() == 1) {
                operator_log.setUpdate_message("恢复删除用户：" + customer4.getCname() + "。");
            }
            break;
        case 5:
            Customer customer5 = (Customer) object;
            operator_log.setUser_id(customer5.getCid());
            operator_log.setUser_name(customer5.getCname());
            operator_log.setOperate_type(5);
            operator_log.setUpdate_message("已为" + customer5.getCname() + "录入" + mi.getMonth() + "月水费！起码:"
                    + mi.getFirstNumber() + "，止码:" + mi.getLastNumber() + "，当月水费:" + mi.getFirstTotal() + "元。");
            break;
        case 6:
            Bill bill = (Bill) object;
            Bill b = billService.getBillById(bill.getUid()); // 修改之前用户起止码
            operator_log.setUser_id(bill.getUid());
            operator_log.setUser_name(bill.getUname());
            operator_log.setOperate_type(6);
            operator_log.setUpdate_message("修改前:" + b.getUname() + "的用水量:" + "一月:" + b.getOne() + "，二月:" + b.getTwo()
                    + "，三月:" + b.getThree() + "，四月:" + b.getFour() + "，五月:" + b.getFive() + "，六月:" + b.getSix() + "，"
                    + "七月:" + b.getSeven() + "，八月:" + b.getEight() + "，九月:" + b.getNine() + "，十月:" + b.getTen()
                    + "，十一月:" + b.getEleven() + "，十二月:" + b.getTwelve() + "；<br />" + "修改后:" + bill.getUname() + "的用水量:"
                    + "一月:" + bill.getOne() + "，二月:" + bill.getTwo() + "，三月:" + bill.getThree() + "，四月:"
                    + bill.getFour() + "，五月:" + bill.getFive() + "，六月:" + bill.getSix() + "，" + "七月:" + bill.getSeven()
                    + "，八月:" + bill.getEight() + "，九月:" + bill.getNine() + "，十月:" + bill.getTen() + "，十一月:"
                    + bill.getEleven() + "，十二月:" + bill.getTwelve() + "。");
            break;
        default:
            break;
        }
        operator_logService.insertOperator_log(operator_log);
    }

}
