package com.cyh.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.cyh.mapper.Operator_logMapper;
import com.cyh.pojo.Operator_log;
import com.cyh.pojo.Operator_logPage;
import com.cyh.service.Operator_logService;

public class Operator_logServiceImpl implements Operator_logService {
    @Autowired
    private Operator_logMapper operator_logMapper;

    public void insertOperator_log(Operator_log operator_log) {
        operator_logMapper.insertOperator_log(operator_log);
    }

    public List<Operator_log> showOperator_log(Operator_logPage op) {
        // TODO Auto-generated method stub
        return operator_logMapper.showOperator_log(op);
    }

    public Integer getoperator_logCount(Operator_logPage op) {
        // TODO Auto-generated method stub
        return operator_logMapper.getoperator_logCount(op);
    }
}
