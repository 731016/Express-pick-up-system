package com.xiaofei.config;

import com.xiaofei.quartz.UserQuartzJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO 类描述
 *
 * @date 2022/4/21 22:15
 */
@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail getJobDetail() {
        // 对应定时任务类
        return JobBuilder.newJob(UserQuartzJob.class).withIdentity("quartz").storeDurably().build();
    }
    @Bean
    public CronTrigger scheduleJob() {
        CronScheduleBuilder cronTrig = CronScheduleBuilder.cronSchedule("0/2 * * * * ?");
        CronTrigger trigger = TriggerBuilder.newTrigger().forJob(getJobDetail())
                // 创建的CronTrigger类型的对象
                .withIdentity("trigger").withDescription("这是cron触发器").startNow()
                // 创建的CronScheduleBuilder类型的对象
                .withSchedule(cronTrig).build();
        return trigger;
    }
}
