package com.example.aiylbank.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LoggingAspect{
    @Around("@annotation(com.example.aiylbank.annotations.Loggable)")
    public Object logExecution(org.aspectj.lang.ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("Executing method: {} with arguments: {}", methodName, args);

        long startTime = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("Method: {} threw exception: {}", methodName, throwable.getMessage());
            throw throwable;
        }
        long endTime = System.currentTimeMillis();

        log.info("Method: {} executed in {} ms with result: {}", methodName, (endTime - startTime), result);
        return result;
    }

}
