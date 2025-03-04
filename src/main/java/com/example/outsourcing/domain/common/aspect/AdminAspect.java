package com.example.outsourcing.domain.common.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AdminAspect {
    private final HttpServletRequest request;

    private final UserService userService;

        @Around("@annotation(com.example.outsourcing.domain.common.annotation.Admin) ||" +
                "@within(com.example.outsourcing.domain.common.annotation.Admin)")
        public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
            Long userId = (Long) request.getAttribute("userId");



            String url = request.getRequestURI();
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            log.info("Request: userId={}, URL={}", userId, url);

            Object result = joinPoint.proceed();

            stopWatch.stop();
            log.info("Response: time={}", stopWatch.getTotalTimeMillis());

            return result;
        }
}
