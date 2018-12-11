package com.cyh.util.timerTaskUtil;

import com.cyh.common.Constants;
import com.cyh.service.UtilService;
import com.cyh.util.SpringContextUtil;
import com.cyh.util.timerTaskUtil.job.backupAndResetTables_JOB;

import javax.management.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Set;

/**
 *  如果有任务需要定时调度 则把任务放到调度器定时执行
 */

public class TimerRun {

    public void run(){
        //调度备份重置数据库表任务
        if(Constants.backupAndResetTables_JOB_Switch){
            //0 59 23 1/1 * ?  每天23点59分执行
            //*/30 * * * * ?  每30秒执行一次
            new BaseTimer().schedulerJob(backupAndResetTables_JOB.class,"backupAndResetTables_JOB","xxx","0 59 23 1/1 * ?");
        }
    }

    /**
     *
     * @param taskName  任务名称
     * @param thisDate  当前时间
     * @return
     */
    public Boolean checkStateForTask(String taskName, String thisDate ,String nextDate,String execute_ip){
        UtilService utilService = (UtilService) SpringContextUtil.getBean("utilService");
        int updateState = utilService.checkStateForTask(taskName,thisDate,nextDate,execute_ip);
        return 1 == updateState;
    }

    /**
     * 任务执行完毕 修改数据库中任务状态
     * @param taskName  任务名称
     * @return
     */
    public void updateTaskState(String taskName){
        UtilService utilService = (UtilService) SpringContextUtil.getBean("utilService");
        utilService.updateTaskState(taskName);
    }

    /**
     * 获取该项目所在tomcat的端口号
     * @return
     */
    public static String getPortByMBean() {
        String portStr = null;
        MBeanServer mBeanServer = null;
        ArrayList<MBeanServer> mBeanServers = MBeanServerFactory.findMBeanServer(null);
        if (mBeanServers.size() > 0) {
            mBeanServer = mBeanServers.get(0);
        }
        if (mBeanServer == null) {
            throw new IllegalStateException("没有发现JVM中关联的MBeanServer.");
        }
        Set<ObjectName> objectNames = null;
        try {
            objectNames = mBeanServer.queryNames(new ObjectName("Catalina:type=Connector,*"), null);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (objectNames == null || objectNames.size() <= 0) {
            throw new IllegalStateException("没有发现JVM中关联的MBeanServer : " + mBeanServer.getDefaultDomain() + " 中的对象名称.");
        }
        try {
            for (ObjectName objectName : objectNames) {
                String protocol = (String) mBeanServer.getAttribute(objectName, "protocol");
                if (protocol.equals("HTTP/1.1")) {
                    portStr = String.valueOf(mBeanServer.getAttribute(objectName, "port"));
                    break;
                }

            }
        } catch (AttributeNotFoundException e) {
            e.printStackTrace();
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        } catch (MBeanException e) {
            e.printStackTrace();
        } catch (ReflectionException e) {
            e.printStackTrace();
        }
        return portStr;
    }
}
