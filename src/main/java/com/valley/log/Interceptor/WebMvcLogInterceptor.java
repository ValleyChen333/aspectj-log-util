package com.valley.log.Interceptor;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

/**
 * mvc日志拦截器，记录请求信息和响应信息
 * 目前无法读取请求body中的内容
 * @author Chenwl
 * @version 1.0.0
 * @date 2017/7/14
 */
@Slf4j
public class WebMvcLogInterceptor implements HandlerInterceptor {

    public static final ThreadLocal<Boolean> logTag = new ThreadLocal();
    /**
     * 记录请求信息
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logTag.set(true);
        log.info("--------------------请求信息------------------------");
        log.info("uri " + request.getRequestURI());

        Set<Map.Entry<String, String[]>> params = request.getParameterMap().entrySet();
        for (Map.Entry<String, String[]> param : params) {
            try {
                String[] value = param.getValue();
                if (value != null && value.length == 1)
                    log.info(param.getKey() + "=" + value[0]);
                else
                    log.info(param.getKey() + "=" + Joiner.on(",").join(value));
            } catch (Exception e) {
                log.info("输出参数"+param.getKey()+"异常,无法输出.......");
            }
        }

        log.info("--------------------------------------------------------");
        return true;
    }

    /**
     * 记录响应信息
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            log.info("--------------------响应信息------------------------");
            log.info("请求uri " + request.getRequestURI());
            log.info("响应 view:" + modelAndView.getViewName());
            Map<String, Object> results = modelAndView.getModel();
            for (Map.Entry<String, Object> result : results.entrySet()) {
                try {
                    log.info(result.getKey() + "=" + result.getValue());
                } catch (Exception e) {
                    log.info("输出结果"+result.getKey()+"异常,无法输出.......");
                }
            }
            log.info("--------------------------------------------------------");
        }
    }

    /**
     * 异常还是用异常拦截栈记录
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        WebMvcLogInterceptor.logTag.remove();
    }

}