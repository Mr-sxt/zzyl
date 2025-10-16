package com.zzyl.nursing.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyTask {
//    @Scheduled(cron = "0/5 * * * * *")
    public void myTask() {
        log.info("开始执行定时任务");
    }
}
