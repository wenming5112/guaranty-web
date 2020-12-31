package com.example.guaranty.aspect;

import com.example.guaranty.annotation.RequireAuth;
import com.example.guaranty.common.exception.BusinessException;
import com.example.guaranty.shiro.JwtUtil;
import com.example.guaranty.vo.business.BackRoleVO;
import com.example.guaranty.vo.business.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author ming
 * @version 1.0.0
 * @date 2020/11/27 10:57
 **/
@Slf4j
@Aspect
@Component
public class RequireAuthAspect {

    @Pointcut("@annotation(com.example.guaranty.annotation.RequireAuth)")
    public void pointCut() {
    }

    @Before("pointCut() && @annotation(auth)")
    public void around(RequireAuth auth) throws Exception {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtils.isEmpty(servletRequestAttributes)) {
            throw new BusinessException("InValid request, ServletRequestAttributes should not null!!");
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String[] role = auth.roleName();

        UserInfoVO userInfoVO = JwtUtil.getUserFromRedis();
        List<BackRoleVO> roleVOList = userInfoVO.getRoles();
        Boolean flag = Boolean.TRUE;
        for (BackRoleVO roleVO : roleVOList) {
            for (String aRole : role) {
                if (roleVO.getRoleName().equals(aRole)) {
                    flag = Boolean.FALSE;
                }
            }
        }

        if (flag) {
            String uri = request.getRequestURI();
            log.debug("请求的接口是: " + uri);
            throw new BusinessException("您没有权限访问");
        }

        String userName = JwtUtil.getUsernameByRedisToken(request);
        if (StringUtils.isBlank(userName)) {
            throw new BusinessException("请登录!!");
        }
    }
}
