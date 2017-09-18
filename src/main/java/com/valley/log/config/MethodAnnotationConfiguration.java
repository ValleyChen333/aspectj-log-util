package com.valley.log.config;

import com.valley.log.aspect.LogMethodAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @LogMethod aspect配置
 * @author Chenwl
 * @version 1.0.0
 * @date 2017/7/17
 */
@Slf4j
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class MethodAnnotationConfiguration {

    @Bean
    public LogMethodAspect logMethodAspect(){
        return new LogMethodAspect();
    }
}
