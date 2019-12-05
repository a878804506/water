package com.cyh.util.zookeeperUtil;

import com.cyh.util.timerTaskUtil.job.backupAndResetTables_JOB;

public class ZookeeperConnectSessionWatcherUtil implements Runnable {

    @Override
    public void run() {
        try {
            ZookeeperUtil.zkInit();
            ZookeeperUtil.zkConnect();
            if(!ZookeeperUtil.zkExists(ZookeeperUtil.PATH)){
                ZookeeperUtil.zkCreate(ZookeeperUtil.PATH,"once create this node for user state.".getBytes());
            }
            if(!ZookeeperUtil.zkExists(backupAndResetTables_JOB.BACKUPANDRESETTABLESPATH)){
                ZookeeperUtil.zkCreate(backupAndResetTables_JOB.BACKUPANDRESETTABLESPATH,"once create this node for back.".getBytes());
            }
            ZookeeperUtil.zkNodeCache();
            ZookeeperUtil.zkPathChildrenCache(ZookeeperUtil.PATH);
            ZookeeperUtil.zkPathChildrenCache(backupAndResetTables_JOB.BACKUPANDRESETTABLESPATH);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("初次连接zookeeper服务失败！请注意！！");
            return;
        }
        /*while (true){
            try {
                switch (ZookeeperUtil.zoo.getState().toString()){
                    case CONNECTING:
                        System.out.println("["+CommonUtil.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss")+"] 正在连接!"+ZookeeperUtil.zoo.getState());
                        break;
                    case RECONNECTING:
                        System.out.println("["+CommonUtil.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss")+"] 正在重新连接!"+ZookeeperUtil.zoo.getState());
                        break;
                    case CONNECTED:
                        System.out.println("["+CommonUtil.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss")+"] 已经连接!"+ZookeeperUtil.zoo.getState());
                        break;
                    case RECONNECTED:
                        System.out.println("["+CommonUtil.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss")+"] 已经重新连接!"+ZookeeperUtil.zoo.getState());
                        break;
                    case CLOSED:
                        System.out.println("["+CommonUtil.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss")+"] 开始会话重连。。。");
                        ZookeeperUtil.zkConnect();
                        if(!ZookeeperUtil.zkExists()){
                            ZookeeperUtil.zkCreate("once create this node.".getBytes());
                        }
                        break;
                }
                Thread.sleep(30000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }
}