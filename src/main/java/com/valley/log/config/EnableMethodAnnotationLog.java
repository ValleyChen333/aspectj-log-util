package com.valley.log.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启记录@LogMehtod标注方法调用日志
 * @author Chenwl
 * @version 1.0.0
 * @date 2017/7/14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({MethodAnnotationConfiguration.class})
public @interface EnableMethodAnnotationLog {

}
