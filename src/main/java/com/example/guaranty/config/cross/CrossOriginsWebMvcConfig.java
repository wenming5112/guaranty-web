package com.example.guaranty.config.cross;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global CrossOrigin Filter
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/4/15 18:06
 **/
@Configuration
public class CrossOriginsWebMvcConfig implements WebMvcConfigurer {

    @Bean
    public WebMvcConfigurer crossOriginsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").
                        allowCredentials(true). // 带上cookie信息
                        allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"). // 允许任何方法（post、get等）
                        allowedHeaders("Origin", "X-Requested-With", "Content-Type", "Accept", "Authorization"). // 允许任何请求头
                        allowedOrigins("*"). // 允许跨域的域名，可以用*表示允许任何域名使用
                        exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(3600L); // maxAge(3600)表明在3600秒内，不需要再发送预检验请求，可以缓存该结果
            }
        };
    }
}
