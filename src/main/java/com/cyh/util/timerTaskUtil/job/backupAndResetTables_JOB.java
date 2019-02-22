package com.cyh.util.timerTaskUtil.job;

import com.cyh.common.Constants;
import com.cyh.pojo.ConfigureInfo;
import com.cyh.service.UtilService;
import com.cyh.util.SpringContextUtil;
import com.cyh.util.timerTaskUtil.TimerRun;
import com.cyh.util.zookeeperUtil.ZookeeperUtil;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *  定时任务  只会在12月31执行创表操作   创建表以及初始化表
 *  每天23点59分执行  并且只有在12月31号会成功初始化表和创建表
 */
public class backupAndResetTables_JOB implements Job {

    public static final String BACKUPANDRESETTABLESPATH = "/backupAndResetTablesPath";

    @Override
    public void execute(JobExecutionContext context) {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            String port = TimerRun.getPortByMBean();
            Date toDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(toDate);
            toDate = calendar.getTime();   //这个时间就是日期往后推一天的结果
            calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动

            //使用哪种竞锁方式：zk/数据库
            if(Constants.useZookeeperLock){
                if(ZookeeperUtil.zkCreateToPath(BACKUPANDRESETTABLESPATH+"/back_"+getDateFormat(toDate,"yyyy_MM_dd"),ip+port))
                    backupAndResetTables(ip,port,"zookeeper");
            }else{
                if(new TimerRun().checkStateForTask("backupAndResetTables_JOB",getDateFormat(toDate,"yyyy-MM-dd"),getDateFormat(calendar.getTime(),"yyyy-MM-dd HH:mm:ss"),ip+"_"+port))
                    backupAndResetTables(ip,port,"mysql");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(Constants.useZookeeperLock){
//                ZookeeperUtil.zkDeleteToPath(BACKUPANDRESETTABLESPATH);
            }else{
                //任务执行完毕或者异常时 修改数据库中任务状态
                new TimerRun().updateTaskState("backupAndResetTables_JOB");
            }
        }
    }

    /**
     *
     * @param ip 执行该方法的ip
     * @param port 执行该方法的port
     * @param byWay 通过何种方式执行  数据库/zk
     * @throws Exception
     */
    private void backupAndResetTables(String ip,String port,String byWay) throws Exception{
        UtilService utilService = (UtilService) SpringContextUtil.getBean("utilService");
        Date date = new Date();
        String nowDateStr = getDateFormat(date,"MM-dd");
        ConfigureInfo settings = utilService.getConfigureInfoByName("updateTables"); //获取创建表初始化表的配置
        if(settings.getTime().equals(nowDateStr)){
            String updateTables = settings.getUpdateTables();
            String [] tables = updateTables.split(",");
            for (String table: tables) {
                if(StringUtils.isBlank(utilService.checkedTableExist(table , settings.getPath()))){
                    ConfigureInfo upTab = utilService.getConfigureInfoByName(table);
                    if(upTab != null){
                        String tableStatement = upTab.getUpdateTables();  // 获取到创建表语句，创建表
                        tableStatement = tableStatement.replace("##",settings.getPath());
                        utilService.createTab(tableStatement); //创建表
                        int dataCount = utilService.importDateByTableName(table,table+settings.getPath()); // 导入数据
                        utilService.insertConfigureInfoLogs("["+getDateFormat(date,"yyyy-MM-dd HH:mm:ss") +"]创建表成功：" + table+settings.getPath() + ",并且成功导入数据："+dataCount +"条。",ip+"_"+port,byWay);
                        int dataCount1 = utilService.updateOldTabForZero(upTab.getPath()); //初始化数据表
                        utilService.insertConfigureInfoLogs("["+getDateFormat(date,"yyyy-MM-dd HH:mm:ss") +"]初始化数据表成功：" + table + ",并且成功初始化数据："+dataCount1 +"条。",ip+"_"+port,byWay);
                    }else{
                        utilService.insertConfigureInfoLogs("["+getDateFormat(date,"yyyy-MM-dd HH:mm:ss") +"]还没有为该表配置表创建语句：" + table+settings.getPath(),ip+"_"+port,byWay);
                    }
                }else{
                    utilService.insertConfigureInfoLogs("["+getDateFormat(date,"yyyy-MM-dd HH:mm:ss") +"]该表已存在：" + table+settings.getPath(),ip+"_"+port,byWay);
                }
            }
            //写入下一年的年份，下一年继续创建表
            int year = Integer.valueOf(getDateFormat(date,"yyyy"));
            utilService.updateYear("updateTables",year+1);
        }else{
            utilService.insertConfigureInfoLogs("["+getDateFormat(date,"yyyy-MM-dd HH:mm:ss") +"]不是创建表以及初始化表的时间（"+settings.getTime()+"）！",ip+"_"+port,byWay);
        }
    }

    //时间格式化
    public static String getDateFormat(Date date, String p){
        SimpleDateFormat sdf = new SimpleDateFormat(p);
        return sdf.format(date);
    }
}
