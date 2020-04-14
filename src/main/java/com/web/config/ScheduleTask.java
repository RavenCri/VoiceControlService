package com.web.config;

import cn.hutool.core.date.DateTime;
import com.web.controller.UserDeviceController;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

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
        DateTime now = new DateTime();
        //System.out.println(now.getTime());
        for(Map.Entry entry:UserDeviceController.OnlineDevice.entrySet()){
            DateTime last = (DateTime) entry.getValue();
            // 如果一分钟该硬件都没有相应心跳数据包，说明离线了。删除即可。
            if( now.getTime()- last.getTime() >= 1000*6){
                System.out.println(entry.getKey()+"设备离线了！");
                UserDeviceController.OnlineDevice.remove(entry.getKey());
            }
        }
    }
}
