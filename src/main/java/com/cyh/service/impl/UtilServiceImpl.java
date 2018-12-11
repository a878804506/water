package com.cyh.service.impl;

import com.cyh.pojo.TransitionModel;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyh.mapper.UtilMapper;
import com.cyh.pojo.ConfigureInfo;
import com.cyh.service.UtilService;

public class UtilServiceImpl implements UtilService {

    @Autowired
    private UtilMapper utilMapper;

    public int excelSrcSetting(ConfigureInfo excelSettings) {
        // TODO Auto-generated method stub
        return utilMapper.excelSrcSetting(excelSettings);
    }

    public ConfigureInfo getConfigureInfoByName(String name) {
        // TODO Auto-generated method stub
        return utilMapper.getConfigureInfoByName(name);
    }

    @Override
    public String checkedTableExist(String table, String year) {
        return utilMapper.checkedTableExist(table,year);
    }

    @Override
    public void createTab(String tableStatement) {
        utilMapper.createTab(tableStatement);
    }

    @Override
    public int importDateByTableName(String oldTable, String newTable) {
        return utilMapper.importDateByTableName(oldTable,newTable);
    }

    @Override
    public int insertConfigureInfoLogs(String log) {
        return utilMapper.insertConfigureInfoLogs(log);
    }

    @Override
    public int updateOldTabForZero(String sql) {
        return utilMapper.updateOldTabForZero(sql);
    }

    @Override
    public int updateYear(String table, int year) {
        return utilMapper.updateYear(table,year);
    }

    @Override
    public int replaceTransitionModel(TransitionModel tm) {
        return utilMapper.replaceTransitionModel(tm);
    }

    @Override
    public int getOperateCutomerCountByToDate(TransitionModel tm) {
        return utilMapper.getOperateCutomerCountByToDate(tm);
    }

    @Override
    public int checkStateForTask(String taskName, String thisDate, String nextDate, String execute_ip) {
        return utilMapper.checkStateForTask(taskName,thisDate,nextDate,execute_ip);
    }

    @Override
    public int updateTaskState(String taskName) {
        return utilMapper.updateTaskState(taskName);
    }
}
