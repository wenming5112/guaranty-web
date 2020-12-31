package com.example.guaranty.common.handle;

import com.example.guaranty.common.exception.BusinessException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * xxx
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/10/27 12:23
 **/
public class RequestHolder {

    public HttpServletRequest getRequest() throws BusinessException {
        // 获取request
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtils.isEmpty(servletRequestAttributes)) {
            throw new BusinessException("不合法的请求，ServletRequestAttributes 为空");
        }
        return servletRequestAttributes.getRequest();
    }
}
