package com.example.guaranty.bootstrap;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Servlet init
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/04/15
 **/
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(GuarantyAdminApplication.class);
    }

}
