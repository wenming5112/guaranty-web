package com.example.guaranty.config.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger config
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/04/15
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${spring.profiles.active}")
    private String env;

    /**
     * http://192.168.1.191:8888/swagger-ui.html
     * http://192.168.1.191:8888/doc.html
     * https://testcoin.utools.club/swagger-ui.html
     * frps-consle (7000)
     * frp_0.34.3_linux_386 (aliyun centos 7)
     * url: http://gua.esbug.com:7000/
     * username: admin
     * password: GuaAdmin
     *
     * 客户端
     * frpc: frpc.exe -c frpc.ini
     *
     * http://gua.esbug.com:7000/swagger-ui.html
     */

    private String swaggerScanPackage = "None";
    private static final String DEV_ENV = "dev";
    private static final String API_VERSION = "1.0.0";
    private static final String ADMIN_GROUP_NAME = "Admin";

    @Bean
    public Docket createBusinessApi() {
        if (DEV_ENV.equals(env)) {
            swaggerScanPackage = "com.example.guaranty.controller.business";
        }
        List<Parameter> tokenParam = new ArrayList<>();
        ParameterBuilder ticket= new ParameterBuilder();
        ticket.name("Authorization")
                .description("登录令牌")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        tokenParam.add(ticket.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(tokenParam)
                // 分组名不支持中文
                .groupName(ADMIN_GROUP_NAME)
                .apiInfo(apiBusinessInfo())
                .pathMapping("/")
                .select()
                // 对所有api进行监控
                .apis(RequestHandlerSelectors.basePackage(swaggerScanPackage))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiBusinessInfo() {
        Contact contact = new Contact("tldollar", "http://www.tldollar.com", "tldollar@email");
        return new ApiInfoBuilder()
                // 设置文档的标题
                .title(ADMIN_GROUP_NAME)
                // 设置文档的描述->1.Overview
                .description("TLD-Admin")
                .contact(contact)
                .version(API_VERSION)
                .build();
    }

}
