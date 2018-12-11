package com.cyh.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.cyh.mapper.WaterConsumptionMapper;
import com.cyh.pojo.Customer;
import com.cyh.pojo.MonthBill;
import com.cyh.pojo.WaterConsumption;
import com.cyh.service.WaterConsumptionService;

import java.util.List;
import java.util.Map;

public class WaterConsumptionServiceImpl implements WaterConsumptionService {
    @Autowired
    private WaterConsumptionMapper waterConsumptionMapper;

    public void addWBill(Customer customer) {
        // TODO Auto-generated method stub
        waterConsumptionMapper.addWBill(customer);
    }

    public void updateWBill(Customer customer) {
        // TODO Auto-generated method stub
        waterConsumptionMapper.updateWBill(customer);
    }

    public void updateWaterConsumptionByMonth(String month, int waterConsumption, int uid) {
        // TODO Auto-generated method stub
        waterConsumptionMapper.updateWaterConsumptionByMonth(month, waterConsumption, uid);
    }

    public MonthBill waterConsumptionAllCost(String year) {
        // TODO Auto-generated method stub
        return waterConsumptionMapper.waterConsumptionAllCost(year);
    }

    public WaterConsumption waterConsumptionAllAmount(String year) {
        // TODO Auto-generated method stub
        return waterConsumptionMapper.waterConsumptionAllAmount(year);
    }

    public MonthBill waterConsumptionUserCost(String year, int cid) {
        // TODO Auto-generated method stub
        return waterConsumptionMapper.waterConsumptionUserCost(year, cid);
    }

    public WaterConsumption waterConsumptionUserAmount(String year, int cid) {
        // TODO Auto-generated method stub
        return waterConsumptionMapper.waterConsumptionUserAmount(year, cid);
    }

    @Override
    public List<Map<String, Object>> getTopDataByMonths(String table, String month, String lastMonth, String top) {
        return waterConsumptionMapper.getTopDataByMonths(table,month,lastMonth,top);
    }

    @Override
    public List<Map<String, Object>> getTopDataByLastTable(String table, String lastTable, String month, String lastMonth, String top) {
        return waterConsumptionMapper.getTopDataByLastTable(table,lastTable,month,lastMonth,top);
    }

    @Override
    public List<Integer> getPieDataByMonths(String table, String month, String top) {
        return waterConsumptionMapper.getPieDataByMonths(table,month,top);
    }

}
