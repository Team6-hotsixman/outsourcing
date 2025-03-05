package com.example.outsourcing.domain.common.aspect;

import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.user.dto.response.UserResponseDto;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.enums.UserRole;
import com.example.outsourcing.domain.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class OwnerAspect {

    private final HttpServletRequest request;

    private final UserService userService;

    @Around("@annotation(com.example.outsourcing.domain.common.annotation.Owner) ||" +
            "@within(com.example.outsourcing.domain.common.annotation.Owner)")
    public Object logOwnerApiAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        Long userId = (Long) request.getAttribute("userId");
        UserResponseDto userResponseDto = userService.getUser(userId);
        if (userResponseDto.getUserRole() != UserRole.OWNER) {
            throw new ApplicationException(ErrorCode.Unauthorized_User);
        }
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



