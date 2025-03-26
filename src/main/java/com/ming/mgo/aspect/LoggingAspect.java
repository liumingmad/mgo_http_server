package com.ming.mgo.aspect;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonNull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.util.Collections;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoggingAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private static final Gson gson = new Gson();

    @Around("execution(* com.ming.mgo.controller..*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = attributes.getRequest();
        ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
        
        // 记录请求信息
        logger.info("收到请求: {} {}", request.getMethod(), request.getRequestURI());
        logger.info("请求头: {}", Collections.list(request.getHeaderNames()).stream()
                .map(headerName -> headerName + ": " + request.getHeader(headerName))
                .collect(Collectors.joining(", ")));
        logger.info("查询参数: {}", request.getQueryString());
        
        // 记录请求体
        String requestBody = new String(requestWrapper.getContentAsByteArray(), requestWrapper.getCharacterEncoding());
        if (!requestBody.isEmpty()) {
            logger.info("请求体: {}", requestBody);
        }

        // 执行目标方法
        Object result = joinPoint.proceed();

        // 记录响应信息
        HttpServletResponse response = attributes.getResponse();
        if (response != null) {
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
            String responseBody = new String(responseWrapper.getContentAsByteArray());
            
            // 构建标准响应结构
            JsonObject standardResponse = new JsonObject();
            standardResponse.addProperty("code", response.getStatus());
            standardResponse.addProperty("message", "success");
            
            // 处理响应数据
            JsonElement dataElement;
            if (responseBody.isEmpty()) {
                dataElement = JsonNull.INSTANCE;
            } else {
                try {
                    dataElement = gson.fromJson(responseBody, JsonElement.class);
                } catch (Exception e) {
                    dataElement = gson.toJsonTree(responseBody);
                }
            }
            standardResponse.add("data", dataElement);
            
            logger.info("响应数据: {}", gson.toJson(standardResponse));
            
            // 重要：将响应体写回响应流
            responseWrapper.copyBodyToResponse();
        }

        return result;
    }
}
