package com.cyh.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.cyh.mapper.MonthBillMapper;
import com.cyh.pojo.Customer;
import com.cyh.pojo.Middle;
import com.cyh.pojo.MonthBill;
import com.cyh.service.MonthBillService;

public class MonthBillServiceImpl implements MonthBillService {
    @Autowired
    private MonthBillMapper monthBillMapper;

    public void addMBill(Customer customer) {

        monthBillMapper.addMBill(customer);
    }

    public void updateMBill(Customer customer) {

        monthBillMapper.updateMBill(customer);
    }

    public void updatemeight(Middle m) {

        monthBillMapper.updatemeight(m);
    }

    public void updatemeleven(Middle m) {

        monthBillMapper.updatemeleven(m);
    }

    public void updatemfive(Middle m) {

        monthBillMapper.updatemfive(m);
    }

    public void updatemfour(Middle m) {

        monthBillMapper.updatemfour(m);
    }

    public void updatemnine(Middle m) {

        monthBillMapper.updatemnine(m);
    }

    public void updatemone(Middle m) {

        monthBillMapper.updatemone(m);
    }

    public void updatemseven(Middle m) {

        monthBillMapper.updatemseven(m);
    }

    public void updatemsix(Middle m) {

        monthBillMapper.updatemsix(m);
    }

    public void updatemten(Middle m) {

        monthBillMapper.updatemten(m);
    }

    public void updatemthree(Middle m) {

        monthBillMapper.updatemthree(m);
    }

    public void updatemtwo(Middle m) {

        monthBillMapper.updatemtwo(m);
    }

    public void updatemtwelve(Middle m) {

        monthBillMapper.updatemtwelve(m);
    }

    public List<MonthBill> getMBill() {

        return monthBillMapper.getMBill();
    }

    public void updateMBillCidByCid(Map map) {

        monthBillMapper.updateMBillCidByCid(map);
    }

    public void updateMBillIdfromRegular(Integer cid) {

        monthBillMapper.updateMBillIdfromRegular(cid);
    }

    public void updateMBillTargetIDfromRegular(Customer customer) {

        monthBillMapper.updateMBillTargetIDfromRegular(customer);
    }

    public int selectmone() {

        return monthBillMapper.selectmone();
    }

    public int selectmtwo() {

        return monthBillMapper.selectmtwo();
    }

    public int selectmthree() {

        return monthBillMapper.selectmthree();
    }

    public int selectmfour() {

        return monthBillMapper.selectmfour();
    }

    public int selectmfive() {

        return monthBillMapper.selectmfive();
    }

    public int selectmsix() {

        return monthBillMapper.selectmsix();
    }

    public int selectmseven() {

        return monthBillMapper.selectmseven();
    }

    public int selectmeight() {

        return monthBillMapper.selectmeight();
    }

    public int selectmnine() {

        return monthBillMapper.selectmnine();
    }

    public int selectmten() {

        return monthBillMapper.selectmten();
    }

    public int selectmeleven() {

        return monthBillMapper.selectmeleven();
    }

    public int selectmtwelve() {

        return monthBillMapper.selectmtwelve();
    }

}
