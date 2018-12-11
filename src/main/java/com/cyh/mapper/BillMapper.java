package com.cyh.mapper;

import java.util.List;
import java.util.Map;

import com.cyh.pojo.*;
import org.apache.ibatis.annotations.Param;

public interface BillMapper {
    void addBill(Customer customer); // 添加用户时添加账单

    Bill selectBill(Condition condition); // 根据编号，止码，月份 来查询 用户账单

    void updateBill(Customer customer); // 修改了个人信息表后 账单表里的信息也要改

    void updateCdelete(Bill bill); // 修改bill中的 Cdelete字段 随着 customer 一起做修改
    // 根据编号和月份来修改用户的止码

    void updateone(Condition condition); // 如果是第一个月

    void updatetwo(Condition condition); // 如果是第二个月

    void updatethree(Condition condition); // 如果是第三个月

    void updatefour(Condition condition);

    void updatefive(Condition condition);

    void updatesix(Condition condition);

    void updateseven(Condition condition);

    void updateeight(Condition condition);

    void updatenine(Condition condition);

    void updateten(Condition condition);

    void updateeleven(Condition condition);

    void updatetwelve(Condition condition);

    List<Bill> getAllBills(CustomerPage cp); // 查询所有用户起止码 分页

    Bill getBillById(Integer id); // 根据编号查询用户用水

    void updateBills(Bill bill); // 修改用户起止码

    List<Bill> getBB(CustomerPage cp); // 查询所有用户起止码 分页

    // 表单提交前，根据输入的月份和用户编号--->校验输入止码的正确性
    Integer getWaterMeterNumberByCustomerNumAndMonth(Condition condition);

    /**
     * 获取今日录入水费的用户数
     */
    int getSectionNum(@Param("startDate") String startDate,@Param("endDate") String endDate);

    /**
     *按 limit 获取今日录入用户的详情
     */
    List<TransitionModel> getToDayDatasList(@Param("startDate") String startDate,@Param("endDate") String endDate,@Param("limit") String limit);
}
