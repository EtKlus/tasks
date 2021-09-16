package com.getklus.task.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

    @Autowired
    TaskService taskService;

    @Scheduled(cron = "0 0 7 * * Mon-Sun")
    public void scheduleTaskWithCronExpression() throws Exception {
        taskService.sendMail();
    }
}
