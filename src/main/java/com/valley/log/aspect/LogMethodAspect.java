package com.valley.log.aspect;

import com.alibaba.fastjson.JSON;
import com.valley.log.annotation.LogMethod;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 记录@LogMethod注解方法的调用日志
 * @author Chenwl
 * @version 1.0.0
 * @date 2017/7/17
 */
@Slf4j
@Aspect
public class LogMethodAspect {


    @Around("execution(* *.*(..)) && @annotation(com.valley.log.annotation.LogMethod)")
    public Object profile(ProceedingJoinPoint joinPoint) throws Throwable {
        //方法名
        String target = joinPoint.getSignature().getName();
        if(joinPoint.getSignature() instanceof MethodSignature){
            MethodSignature signature = (MethodSignature)joinPoint.getSignature();
            LogMethod logMethod = signature.getMethod().getAnnotation(LogMethod.class);
            if (logMethod != null && logMethod.value() != null)
                target = logMethod.value();
        }
        log.info("-------------------" + target + " 开始------------------------");
        //参数
        try {
            if (joinPoint.getArgs() != null) {
                String argsJson = JSON.toJSONString(joinPoint.getArgs());
                log.info("参数:" + argsJson);
            }
        } catch (Exception e) {
            log.info("序列化方法参数异常，无法输出日志");
        }

        //执行
        Object result = joinPoint.proceed();

        //结果
        try {
            if (result != null) {
                String resultJson = JSON.toJSONString(result);
                log.info("结果:" + resultJson);
            }
            log.info("-------------------" + target + " 结束------------------------");
        } catch (Exception e) {
            log.info("序列化响应异常,无法输出日志");
        }

        return result;

    }
}
