package com.cyh.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.cyh.mapper.CustomerMapper;
import com.cyh.pojo.Customer;
import com.cyh.pojo.CustomerPage;
import com.cyh.service.CustomerService;

public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;

    public void addCustomer(Customer customer) {
        customerMapper.addCustomer(customer);
    }

    public List<Customer> showCustomer(CustomerPage cp) {
        return customerMapper.showCustomer(cp);
    }

    public void delete(Customer customer) {
        customerMapper.delete(customer);
    }

    public Customer getCustomerById(Integer cid) {
        return customerMapper.getCustomerById(cid);
    }

    public void deletes(Customer customer) {
        customerMapper.deletes(customer);
    }

    public int getCustomerCount(CustomerPage cp) {
        return customerMapper.getCustomerCount(cp);
    }

    public List<Customer> selectDelete(CustomerPage cp) {
        return customerMapper.selectDelete(cp);
    }

    public int getDCustomerCount(CustomerPage cp) {
        return customerMapper.getDCustomerCount(cp);
    }

    public void updateCustomer(Customer customer) {
        customerMapper.updateCustomer(customer);
    }

    public List<Customer> getCustomers() {
        return customerMapper.getCustomers();
    }

    public List<Customer> getCustomerByName(String name) {
        return customerMapper.getCustomerByName(name);
    }

    public List<Customer> getBT(CustomerPage cp) {
        return customerMapper.getBT(cp);
    }

    public int getBTC(CustomerPage cp) {
        return customerMapper.getBTC(cp);
    }

    public int getCustomerCountById(CustomerPage cp) {
        return customerMapper.getCustomerCountById(cp);
    }

    public int getAllCustomerCount() {
        return customerMapper.getAllCustomerCount();
    }

    @Override
    public List<Map<String, Object>> getNotImportCustomerByMonth(String month) {
        return customerMapper.getNotImportCustomerByMonth(month);
    }

    public List<Customer> getCustomerByLikeId(int id) {
        return customerMapper.getCustomerByLikeId(id);
    }

}
