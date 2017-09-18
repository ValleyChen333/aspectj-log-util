package com.valley.log.aspect;

import com.alibaba.fastjson.JSON;
import com.valley.log.Interceptor.WebMvcLogInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;

/**
 * 记录Controller的调用日志
 *
 * @author Chenwl
 * @version 1.0.0
 * @date 2017/7/17
 */
@Slf4j
@Aspect
public class ControllerAspect {


    /**
     * 拦截所有Controller注解的方法，记录日志
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* *.*(..)) && ( @within(org.springframework.web.bind.annotation.RestController)|| @within(org.springframework.stereotype.Controller) )")
    public Object profile(ProceedingJoinPoint joinPoint) throws Throwable {
        /**
         *  是否需要记录日志标记
         * 由WebMvcLogInterceptor设置，所有被WebMvcLogInterceptor拦截的方法都会记录日志
         */
        Boolean logTag = WebMvcLogInterceptor.logTag.get();
        WebMvcLogInterceptor.logTag.remove();
        if (logTag == null || !logTag) {
            return joinPoint.proceed();
        }


        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Annotation[][] parameterAnnotations = signature.getMethod().getParameterAnnotations();
        boolean logRequestBody = false;
        Object[] args = joinPoint.getArgs();
        //记录@RequestBody中内容
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (int j = 0; j < parameterAnnotations[i].length; j++) {
                if (parameterAnnotations[i][j].annotationType().equals(RequestBody.class)) {
                    if (!logRequestBody)
                        log.info("--------------------请求body------------------------");
                    logRequestBody = true;
                    try {
                    log.info(JSON.toJSONString(args[i]));
                    } catch (Exception e) {
                        log.info("输出@RequestBody异常,无法输出.......");
                    }
                }
            }
        }
        if (logRequestBody)
            log.info("--------------------------------------------------------");


        //执行被代理方法
        Object result = joinPoint.proceed();

        RestController restController = joinPoint.getTarget().getClass().getAnnotation(RestController.class);
        ResponseBody responseBody = null;
        if (joinPoint.getSignature() instanceof MethodSignature) {
            responseBody = signature.getMethod().getAnnotation(ResponseBody.class);
        }

        //记录@responsebody内容
        if (restController != null || responseBody != null) {
            log.info("--------------------响应信息------------------------");
            try {
                log.info(JSON.toJSONString(result));
            } catch (Exception e) {
                log.info("输出@ResponseBody异常,无法输出.......");
            }
            log.info("--------------------------------------------------------");
        }
        return result;

    }
}
