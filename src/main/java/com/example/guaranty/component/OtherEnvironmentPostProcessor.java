package com.example.guaranty.component;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 自定义配置 加载器
 *
 * @author ming
 * @version 1.0.0
 * @date 2020/4/18 15:38
 **/
@Component
public class OtherEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private final YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String[] profiles = {
                "other-config.yml"
        };
        // 循环添加
        for (String profile : profiles) {
            // 从classpath路径下面查找文件
            Resource resource = new ClassPathResource(profile);
            // 加载成PropertySource对象，并添加到Environment环境中
            environment.getPropertySources().addLast(loadYml(resource));
        }
    }

    /**
     * 加载单个配置文件
     *
     * @param resource 资源
     * @return resource
     */
    private PropertySource<?> loadYml(Resource resource) {
        if (!resource.exists()) {
            throw new IllegalArgumentException("Resource " + resource + " does not exist");
        }
        try {
            // 从输入流中加载一个Properties对象
            // Objects.requireNonNull(resource.getFilename())
            return this.yamlPropertySourceLoader.load("custom-resource", resource).get(0);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to load yaml configuration from " + resource, ex);
        }
    }
}
