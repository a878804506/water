package com.cyh.util.timerTaskUtil;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 *  任务总调度器
 */
public class BaseTimer{
	// 创建调度器
	public static Scheduler getScheduler() throws SchedulerException{
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		return schedulerFactory.getScheduler();
	}

	public JobDetail getJobDetail(Class<? extends Job> cla,String x,String y){
		return JobBuilder.newJob(cla).withIdentity(x,y).build();
	}

	public Trigger getTrigger(String corn,String x,String y){
		return TriggerBuilder.newTrigger().withIdentity(x,y).withSchedule(CronScheduleBuilder.cronSchedule(corn)).startNow().build();
	}

	public void schedulerJob(Class<? extends Job> cla,String job,String y,String cron){
		JobDetail jobDetail = getJobDetail(cla,job,y);
		Trigger trigger = getTrigger(cron,job,y);
		Scheduler scheduler = null;
		try{
			scheduler = getScheduler();
			// 将任务及其触发器放入调度器
			scheduler.scheduleJob(jobDetail,trigger);
			// 调度器开始调度任务
			scheduler.start();
			System.out.println(job+"调度成功");
		} catch (SchedulerException e){
			System.out.println(job+"调度失败");
			e.printStackTrace();
		}
	}
}
