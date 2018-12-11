package com.cyh.service;

import java.util.List;
import java.util.Map;

import com.cyh.pojo.Customer;
import com.cyh.pojo.Middle;
import com.cyh.pojo.MonthBill;

public interface MonthBillService {
    void addMBill(Customer customer); // 创建用户时，就创建月账单

    void updateMBill(Customer customer);// 修改了个人信息表后 月账单表里的信息也要改

    // 根据编号和月份来修改用户的止码
    void updatemone(Middle m); // 如果是第一个月

    void updatemtwo(Middle m); // 如果是第二个月

    void updatemthree(Middle m); // 如果是第三个月

    void updatemfour(Middle m);

    void updatemfive(Middle m);

    void updatemsix(Middle m);

    void updatemseven(Middle m);

    void updatemeight(Middle m);

    void updatemnine(Middle m);

    void updatemten(Middle m);

    void updatemeleven(Middle m);

    void updatemtwelve(Middle m);

    List<MonthBill> getMBill(); // 查询所有

    // 根据月份查询录入用户总数
    int selectmone(); // 如果是第一个月

    int selectmtwo(); // 如果是第二个月

    int selectmthree(); // 如果是第三个月

    int selectmfour();

    int selectmfive();

    int selectmsix();

    int selectmseven();

    int selectmeight();

    int selectmnine();

    int selectmten();

    int selectmeleven();

    int selectmtwelve();
}
