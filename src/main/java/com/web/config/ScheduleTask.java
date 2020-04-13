package com.web.config;

import cn.hutool.core.date.DateTime;
import com.web.controller.UserDeviceController;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @description:
 * @author: raven
 * @create: 2020-04-13 22:13
 **/
@Configuration
public class ScheduleTask {
    @Scheduled(fixedRate=10000)
    private void configureTasks() {
        System.err.println("执行定时任务时间: " + LocalDateTime.now());
        DateTime now = new DateTime();
        for(Map.Entry entry:UserDeviceController.OnlineDevice.entrySet()){
            DateTime last = (DateTime) entry.getValue();
            if( now.getTime()- last.getTime() >= 1000*60){
                System.out.println(entry.getKey()+"设备离线了！");
                UserDeviceController.OnlineDevice.remove(entry.getKey());
            }
        }
    }
}
