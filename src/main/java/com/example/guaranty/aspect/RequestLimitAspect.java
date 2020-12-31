package com.example.guaranty.aspect;

import com.example.guaranty.annotation.RequestLimit;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.common.exception.RequestLimitException;
import com.example.guaranty.common.utils.IpUtil;
import com.example.guaranty.config.redis.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 接口限流 切面类
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/04/16
 */
@Slf4j
@Aspect
@Component
@Order(-1)
public class RequestLimitAspect {

    @Resource
    private RedisCache redisCache;

    @Pointcut("@annotation(com.example.guaranty.annotation.RequestLimit)")
    public void pointCut() {
    }

    @Before("pointCut() && @annotation(limit)")
    public void around(final JoinPoint point, RequestLimit limit) throws Exception {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtils.isEmpty(servletRequestAttributes)) {
            throw new BusinessException("InValid request, ServletRequestAttributes should not null!!");
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String ip = IpUtil.getIp(request);
        // 获取请求url
        String uri = request.getRequestURI();
        // 记录下请求内容
        log.debug(String.format(">>>--- Request uri is %s", uri));
        log.debug(String.format(">>>--- Request ip is %s", ip));
        log.debug(String.format(">>>--- Request method is %s", (point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName())));
        log.debug(String.format(">>>--- Request type is %s", request.getMethod()));

        String tmp = point.getTarget().getClass().getName();
        String[] tmpS = tmp.split("\\.");
        String className = tmpS[tmpS.length - 1];

        MethodSignature methodName1 = (MethodSignature) point.getSignature();
        Method method = methodName1.getMethod();
        String s = className + "-" + method.getName();
        //这里的这个键规则要重新定义
        // 生成redis key
        String key = "req_limit-".concat(s).concat(ip == null ? "" : ("-" + ip));
        log.debug(">>>--- requestLimit: key " + key);
        // 0表示访问次数
        if (redisCache.get(key) == null || (Integer) redisCache.get(key) == 0) {
            redisCache.set(key, 1);
        } else {
            // 若缓存已存在，则加一次次数
            redisCache.set(key, (Integer) redisCache.get(key) + 1);
        }
        int count = (Integer) redisCache.get(key);
        if (count > 0) {
            ScheduledExecutorService scheduledThreadPool = new ScheduledThreadPoolExecutor(1,
                    new BasicThreadFactory.Builder().namingPattern("scheduled-pool-%d").daemon(true).build());
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        redisCache.delete(key);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            scheduledThreadPool.schedule(timerTask, limit.time(), TimeUnit.MILLISECONDS);
        }
        if (count > limit.count()) {
            log.warn("用户IP[" + ip + "]接口名[" + limit.apiName() + "]超过了限定的次数[" + limit.count() + "]");
            throw new RequestLimitException();
        }
    }
}
