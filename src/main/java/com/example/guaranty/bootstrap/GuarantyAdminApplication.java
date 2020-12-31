package com.example.guaranty.bootstrap;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Start up Class
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/04/15
 **/
@SpringBootApplication()
@ComponentScan("com.example.guaranty.*")
@EnableKnife4j
@ServletComponentScan
@EnableTransactionManagement
public class GuarantyAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuarantyAdminApplication.class, args);
    }

    /**
     * Jackson2 config (Avoid swagger Unable to render this definition)
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new CustomMapper());
        return converter;
    }

    /**
     * validator config
     */
    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .addProperty("hibernate.validator.fail_fast", "true")
                .buildValidatorFactory();
        return validatorFactory.getValidator();
    }

}
