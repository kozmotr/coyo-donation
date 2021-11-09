package com.coyoapp.donations.aop;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

@Configuration
@Aspect
@Log4j2

public class GeneralAspects {

    @Around("execution(* com.coyoapp.donations.mail.*.*(..))")
    @SneakyThrows
    public Object performanceOfMailService(ProceedingJoinPoint joinPoint){
        long beforeStart = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long finishTime = System.currentTimeMillis();
        //log.info("MailService component: {} took {} ms" , joinPoint,getClass().getSimpleName() , finishTime - beforeStart);
        return result;
    }

    /*inspect all Services and their return values*/
    @Around("bean(*Service)")
    @SneakyThrows
    public Object repositoryLogger(ProceedingJoinPoint joinPoint) {
        //log.info("{} is called " , joinPoint);
        Object result = joinPoint.proceed();
        //log.info("Return value {}" , result);
        return result;
    }

    @AfterReturning(returning = "result", value = "@annotation(com.coyoapp.donations.aop.TrackMe)")
    @SneakyThrows
    public void TrackedMethods(JoinPoint joinPoint , Object result) {
        //log.info("{} called result: {} ", joinPoint.getSignature().getName() , result);
    }


}
