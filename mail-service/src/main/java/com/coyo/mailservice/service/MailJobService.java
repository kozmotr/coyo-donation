package com.coyo.mailservice.service;

import com.coyo.mailservice.aop.QueueAspect;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MailJobService {

    @Scheduled(fixedDelay = 1000)
    @SneakyThrows
    public void processUrgentTask() {
        ProceedingJoinPoint job = QueueAspect.urgentTask.poll(1 , TimeUnit.SECONDS);
        if(job!=null) job.proceed();
        log.info("processUrgentTask");
    }

    @Scheduled(fixedDelay = 3000)
    @SneakyThrows
    public void processImportantTask() {
        ProceedingJoinPoint job = QueueAspect.importantTask.poll(1 , TimeUnit.SECONDS);
        if(job!=null) job.proceed();
    }

    @Scheduled(fixedDelay = 5000)
    @SneakyThrows
    public void processNotImportant() {
        ProceedingJoinPoint job = QueueAspect.notImportantTask.poll(1 , TimeUnit.SECONDS);
        if(job!=null) job.proceed();
    }


}
