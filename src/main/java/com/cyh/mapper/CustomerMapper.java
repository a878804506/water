package com.cyh.mapper;

import java.util.List;
import java.util.Map;

import com.cyh.pojo.Customer;
import com.cyh.pojo.CustomerPage;
import org.apache.ibatis.annotations.Param;

public interface CustomerMapper {
    void addCustomer(Customer customer); // 添加用户

    List<Customer> showCustomer(CustomerPage cp); // 查询所有(按条件查询)

    void delete(Customer customer); // 报停，启用

    Customer getCustomerById(Integer cid); // 根据编号查询对象

    void deletes(Customer customer); // 删除用户 不是真正的删除

    int getCustomerCount(CustomerPage cp); // 查询用户条数

    List<Customer> selectDelete(CustomerPage cp); // 查询已经删除的用户

    int getDCustomerCount(CustomerPage cp); // 查询用户条数

    void updateCustomer(Customer customer); // 修改用户

    List<Customer> getCustomers(); // 获取账单

    List<Customer> getCustomerByName(String name); // 模糊查询

    List<Customer> getCustomerByLikeId(int id); // 模糊查询

    List<Customer> getBT(CustomerPage cp); // 查看报停的用户

    int getBTC(CustomerPage cp); // 查询报停用户的数量

    int getCustomerCountById(CustomerPage cp); // 查询用户条数

    int getAllCustomerCount(); // 查询用户总条数

    List<Map<String,Object>> getNotImportCustomerByMonth(@Param("month")String month);//当月未录入用户详情
}
