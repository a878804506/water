package com.cyh.mapper;

import java.util.List;

import com.cyh.pojo.Operator_log;
import com.cyh.pojo.Operator_logPage;

public interface Operator_logMapper {

    void insertOperator_log(Operator_log operator_log); // 添加操作日志

    List<Operator_log> showOperator_log(Operator_logPage op); // 查询操作记录，按条件分页

    Integer getoperator_logCount(Operator_logPage op); // 查询操作记录条数，按条件分页
}
