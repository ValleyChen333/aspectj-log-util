package com.valley.log.config;

import com.valley.log.Interceptor.WebMvcLogInterceptor;
import com.valley.log.aspect.ControllerAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.MappedInterceptor;

/**
 * 日志拦截器配置
 *
 * @author Chenwl
 * @version 1.0.0
 * @date 2017/7/14
 */
@Slf4j
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class WebMvcLogConfiguration extends WebMvcConfigurerAdapter {

    @Value("${mvc.log.path:/**}")
    private String mvcLogPath;

    @Value("${mvc.unlog.path}")
    private String mvcUnlogPath;


    @Bean
    public MappedInterceptor mappedInterceptor() {
        log.info("添加controller日志拦截器到" + mvcLogPath);
        if (StringUtils.isEmpty(mvcLogPath)) {
            return new MappedInterceptor(mvcLogPath.split(","), new WebMvcLogInterceptor());
        } else {
            return new MappedInterceptor(mvcLogPath.split(","), mvcUnlogPath.split(","), new WebMvcLogInterceptor());
        }

    }


    @Bean
    public ControllerAspect controllerAspect() {
        return new ControllerAspect();
    }


}
