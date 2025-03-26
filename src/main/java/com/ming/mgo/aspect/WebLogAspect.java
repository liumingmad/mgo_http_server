package com.ming.mgo.aspect;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
@Profile({"dev", "test"})  // 仅在开发/测试环境启用
public class WebLogAspect {
    private static final Logger log = LoggerFactory.getLogger(WebLogAspect.class);
    private final Gson gson = new Gson();

    // 拦截所有Controller方法
    @Pointcut("execution(* com.ming.mgo.controller.*.*(..))")
    public void webLog() {}

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 打印请求信息
        log.info("URL: {}", request.getRequestURL());
        log.info("HTTP Method: {}", request.getMethod());
        log.info("Class Method: {}.{}", 
            joinPoint.getSignature().getDeclaringTypeName(), 
            joinPoint.getSignature().getName());

        // 过滤特殊类型参数（如文件上传）
        Object[] args = Arrays.stream(joinPoint.getArgs())
            .filter(arg -> !(arg instanceof MultipartFile || arg instanceof HttpServletRequest))
            .toArray();

        log.info("Request Args: {}", gson.toJson(args));

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();  // 执行目标方法
        long endTime = System.currentTimeMillis();

        // 打印响应信息
        log.info("Response Args: {}", gson.toJson(result));
        log.info("Time-Consuming: {}ms", endTime - startTime);
        return result;
    }
}
