package com.cyh.service.impl;

import java.util.List;
import java.util.Map;

import com.cyh.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyh.mapper.BillMapper;
import com.cyh.service.BillService;

public class BillServiceImpl implements BillService {
    @Autowired
    private BillMapper billMapper;

    public void addBill(Customer customer) {
        // TODO Auto-generated method stub
        billMapper.addBill(customer);
    }

    public Bill selectBill(Condition condition) {
        // TODO Auto-generated method stub
        return billMapper.selectBill(condition);
    }

    public void updateBill(Customer customer) {
        // TODO Auto-generated method stub
        billMapper.updateBill(customer);
    }

    public void updateeight(Condition condition) {
        // TODO Auto-generated method stub
        billMapper.updateeight(condition);
    }

    public void updatefive(Condition condition) {
        // TODO Auto-generated method stub
        billMapper.updatefive(condition);
    }

    public void updatefour(Condition condition) {
        // TODO Auto-generated method stub
        billMapper.updatefour(condition);
    }

    public void updateeleven(Condition condition) {
        // TODO Auto-generated method stub
        billMapper.updateeleven(condition);
    }

    public void updatenine(Condition condition) {
        // TODO Auto-generated method stub
        billMapper.updatenine(condition);
    }

    public void updateone(Condition condition) {
        // TODO Auto-generated method stub
        billMapper.updateone(condition);
    }

    public void updateseven(Condition condition) {
        // TODO Auto-generated method stub
        billMapper.updateseven(condition);
    }

    public void updatesix(Condition condition) {
        // TODO Auto-generated method stub
        billMapper.updatesix(condition);
    }

    public void updateten(Condition condition) {
        // TODO Auto-generated method stub
        billMapper.updateten(condition);
    }

    public void updatethree(Condition condition) {
        // TODO Auto-generated method stub
        billMapper.updatethree(condition);
    }

    public void updatetwelve(Condition condition) {
        // TODO Auto-generated method stub
        billMapper.updatetwelve(condition);
    }

    public void updatetwo(Condition condition) {
        // TODO Auto-generated method stub
        billMapper.updatetwo(condition);
    }

    public List<Bill> getAllBills(CustomerPage cp) {
        // TODO Auto-generated method stub
        return billMapper.getAllBills(cp);
    }

    public Bill getBillById(Integer id) {
        // TODO Auto-generated method stub
        return billMapper.getBillById(id);
    }

    public void updateBills(Bill bill) {
        // TODO Auto-generated method stub
        billMapper.updateBills(bill);
    }

    public List<Bill> getBB(CustomerPage cp) {
        // TODO Auto-generated method stub
        return billMapper.getBB(cp);
    }

    public void updateCdelete(Bill bill) {
        // TODO Auto-generated method stub
        billMapper.updateCdelete(bill);
    }

    public Integer getWaterMeterNumberByCustomerNumAndMonth(Condition condition) {
        // TODO Auto-generated method stub
        return billMapper.getWaterMeterNumberByCustomerNumAndMonth(condition);
    }

    @Override
    public int getSectionNum(String startDate, String endDate) {
        return billMapper.getSectionNum(startDate,endDate);
    }

    @Override
    public List<TransitionModel> getToDayDatasList(String startDate, String endDate, String limit) {
        return billMapper.getToDayDatasList(startDate,endDate,limit);
    }

}
