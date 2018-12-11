package com.cyh.pojo;

/**
 *  记录客户各月用水量和水费 综合表
 */
public class TransitionModel implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    private int customer_id;
    private String customer_name;
    private String year;
    private String month;
    private String create_time;
    private Double water_consumption; // 客户用水量
    private Double month_bill_cost;  // 客户月水费

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Double getWater_consumption() {
        return water_consumption;
    }

    public void setWater_consumption(Double water_consumption) {
        this.water_consumption = water_consumption;
    }

    public Double getMonth_bill_cost() {
        return month_bill_cost;
    }

    public void setMonth_bill_cost(Double month_bill_cost) {
        this.month_bill_cost = month_bill_cost;
    }
}
