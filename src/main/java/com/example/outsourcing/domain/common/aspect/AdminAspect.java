package com.example.outsourcing.domain.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AdminAspect {

        @Around("@annotation(com.example.outsourcing.domain.common.annotation.Admin) ||" +
                "@within(com.example.outsourcing.domain.common.annotation.Admin)")
        public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
            long startTime = System.currentTimeMillis();
            String username = "example";
            log.info("USER : {}", username);
            log.info("METHOD : {}", joinPoint.getSignature().toShortString());//메서드를 축약해서 표현한 문장을 출력
            log.info("PARAMS : {}", joinPoint.getArgs());

            try {
                Object result = joinPoint.proceed();
                long endTime = System.currentTimeMillis();
                log.info("TIME : {} ms", endTime - startTime);
                log.info("RESULT : {}", result);
                return result;
            } catch (Exception e) {
                log.error("ERROR : {}", e);
                throw e;
            }

        }
}
