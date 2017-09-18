# aspectj-log-util
一个使用aspectj记录Controller 和 普通方法的日志工具包，可以记录请求的uri，参数，body，返回值

### Controller日志记录方式（javaconfig方式）：
@EnableWebMvcLog注解在任意@configuration上就可以啦
```
@EnableWebMvcLog//开启记录Controller请求记录
@Configuration
public class LogConfig {
}
```
如需限定记录范围可以配置参数mvc.log.path=xxx,xxx,xxx。默认值为/**，拦截所以请求,忽略的范围mvc.unlog.path=xxx,xxx,xxx

**输出结果**
```
2017-09-18 16:14:54.428  INFO 22544 --- [nio-8080-exec-1] c.v.l.Interceptor.WebMvcLogInterceptor   : --------------------请求信息------------------------
2017-09-18 16:14:54.428  INFO 22544 --- [nio-8080-exec-1] c.v.l.Interceptor.WebMvcLogInterceptor   : uri /demo/get
2017-09-18 16:14:54.428  INFO 22544 --- [nio-8080-exec-1] c.v.l.Interceptor.WebMvcLogInterceptor   : id=1
2017-09-18 16:14:54.428  INFO 22544 --- [nio-8080-exec-1] c.v.l.Interceptor.WebMvcLogInterceptor   : --------------------------------------------------------
2017-09-18 16:14:54.446  INFO 22544 --- [nio-8080-exec-1] com.valley.log.aspect.ControllerAspect   : --------------------响应信息------------------------
2017-09-18 16:14:54.511  INFO 22544 --- [nio-8080-exec-1] com.valley.log.aspect.ControllerAspect   : {"id":1,"name":"valley"}
2017-09-18 16:14:54.511  INFO 22544 --- [nio-8080-exec-1] com.valley.log.aspect.ControllerAspect   : --------------------------------------------------------
```

### 方法日志记录(javaConfig+annotation)
配置:
``` 
@EnableMethodAnnotationLog //开启记录@LogMethod注解方法日志
@Configuration
public class LogConfig {
}
```
注解方法:
```
@Service
public class DemoService {

    @LogMethod("三数相加")
    public int logMethod(int i, int j, int k) {
        return i+j+k;
    }
}
```

**输出结果**
```
-------------------三数相加 开始------------------------
参数:[1,2,3]
结果:6
-------------------三数相加 结束------------------------

```

