package com.coyo.mailservice.aop;

import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.*;

@Aspect
@Configuration
public class QueueAspect {
    /*for simplicity I use public static fields for now */
    public static BlockingQueue<ProceedingJoinPoint> urgentTask = new LinkedBlockingQueue<>();
    public static BlockingQueue<ProceedingJoinPoint> importantTask = new LinkedBlockingQueue<>();
    public static BlockingQueue<ProceedingJoinPoint> notImportantTask = new LinkedBlockingQueue<>();

    /*these joinpoints will be executed in separated thread in MailJobService class.*/
    @Around(value = "@annotation(com.coyo.mailservice.aop.Queue)")
    public Object TrackedMethods(ProceedingJoinPoint joinPoint ) {
        String interceptedMethod = joinPoint.getSignature().getName();
        for(Method method : joinPoint.getTarget().getClass().getDeclaredMethods()) {
            if(method.getName().equals(interceptedMethod) != true) continue;
            Queue annotation = method.getAnnotation(Queue.class);
            switch (annotation.importance()) {
                case IMPORTANT:
                    importantTask.add(joinPoint);
                    break;
                case URGENT:
                    urgentTask.add(joinPoint);
                    break;
                case LESS_IMPORTANT:
                    notImportantTask.add(joinPoint);
                    break;
                default:
                    throw new IllegalArgumentException("Not valid importance type");
            }
            break;
        }
        return null;
    }
}
