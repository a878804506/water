package com.cyh.mapper;

import org.apache.ibatis.annotations.Param;

import com.cyh.pojo.Customer;
import com.cyh.pojo.MonthBill;
import com.cyh.pojo.WaterConsumption;

import java.util.List;
import java.util.Map;

public interface WaterConsumptionMapper {
    void addWBill(Customer customer); // 创建用户时，就创建月账单

    void updateWBill(Customer customer);// 修改了个人信息表后 月账单表里的信息也要改

    void updateWaterConsumptionByMonth(@Param("month") String month, @Param("waterConsumption") int waterConsumption,
            @Param("uid") int uid); // 更新用户用水量

    // 查询全镇水费
    MonthBill waterConsumptionAllCost(@Param("year") String year);

    // 查询全镇用水量
    WaterConsumption waterConsumptionAllAmount(@Param("year") String year);

    // 查询个人全年/全季度水费
    MonthBill waterConsumptionUserCost(@Param("year") String year, @Param("cid") int cid);

    // 查询个人全年/全季度用水量
    WaterConsumption waterConsumptionUserAmount(@Param("year") String year, @Param("cid") int cid);

    //查询前top个用户的当月和上月的用水量数据
    List<Map<String, Object>> getTopDataByMonths(@Param("table") String table, @Param("month") String month, @Param("lastMonth") String lastMonth, @Param("top") String top);

    //查询前top个用户的当月和上月的用水量数据   跨年查询/跨表查询
    List<Map<String, Object>> getTopDataByLastTable(@Param("table")String table,@Param("lastTable") String lastTable,@Param("month") String month,@Param("lastMonth")String lastMonth,@Param("top")String top);

    //查询前top个用户的用水量占比
    List<Integer> getPieDataByMonths(@Param("table")String table,@Param("month")String month,@Param("top")String top);
}
