package com.cyh.service;

import com.cyh.pojo.Customer;
import com.cyh.pojo.MonthBill;
import com.cyh.pojo.WaterConsumption;

import java.util.List;
import java.util.Map;

public interface WaterConsumptionService {
    void addWBill(Customer customer); // 创建用户时，就创建月账单

    void updateWBill(Customer customer);// 修改了个人信息表后 月账单表里的信息也要改

    void updateWaterConsumptionByMonth(String month, int waterConsumption, int uid); // 更新用户用水量

    // 查询全镇水费
    MonthBill waterConsumptionAllCost(String year);

    // 查询全镇用水量
    WaterConsumption waterConsumptionAllAmount(String year);

    // 查询个人全年/全季度水费
    MonthBill waterConsumptionUserCost(String year, int cid);

    // 查询个人全年/全季度用水量
    WaterConsumption waterConsumptionUserAmount(String year, int cid);

    //查询前top个用户的当月和上月的用水量数据
    List<Map<String, Object>> getTopDataByMonths(String table,String month,String lastMonth,String top);

    //查询前top个用户的当月和上月的用水量数据   跨年查询/跨表查询
    List<Map<String, Object>> getTopDataByLastTable(String table,String lastTable,String month,String lastMonth,String top);

    //查询前top个用户的用水量占比
    List<Integer> getPieDataByMonths(String table,String month,String top);
}
