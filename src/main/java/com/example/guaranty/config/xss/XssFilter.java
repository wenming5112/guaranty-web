package com.example.guaranty.config.xss;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Xss Filter
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/4/15 22:25
 **/
@Slf4j
@Configuration
@Order(2)
@WebFilter(urlPatterns = "*", filterName = "newXssFilter")
public class XssFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        log.debug("------ XssFilter init ------");
    }

    @Override
    public void destroy() {
        log.debug("------ XssFilter destroy ------");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        log.debug(">>>--- Custom filter --->>> XssFilter");
        chain.doFilter(new XssHttpServletRequestWrapper(
                (HttpServletRequest) request), response);
    }
}
