package com.example.guaranty.shiro;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ming
 */
@Configuration
public class ShiroConfig {

    /**
     * 自动创建代理的类，它会 cahce（缓存） 容器中所有注册的 advisor,
     * 然后搜索容器中所有的 bean ,
     * 如果某个 bean 满足 advisor 中的 Pointcut, 那么将会被自动代理。
     * 它会根据 bean 的类型判断该 bean 是否匹配。
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public SubjectFactory subjectFactory() {
        return new JwtDefaultSubjectFactory();
    }

    /**
     * 将Realm加入容器
     */
    @Bean("shiroRealm")
    public ShiroRealm shiroRealm() {
        return new ShiroRealm();
    }

    /**
     * 权限管理，配置主要是Realm的管理认证
     */
    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(@Qualifier("shiroRealm") ShiroRealm shiroRealm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        // 使用自定义的shiroRealm
        manager.setRealm(shiroRealm);
        // 关闭shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        manager.setSubjectDAO(subjectDAO);
        return manager;
    }

    /**
     * 过滤器
     *
     * @param securityManager securityManager
     * @return ShiroFilterFactoryBean
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 规则定义文档 http://shiro.apache.org/web.html#urls-
        // JWT Filter
        Map<String, Filter> filterMap = new HashMap<>(1);
        filterMap.put("anon", new AnonymousFilter());
        filterMap.put("jwt", new JwtFilter());
        filterMap.put("logout", new LogoutFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> filterRuleMap = new HashMap<>(1);
        // 登陆、登出
        filterRuleMap.put("/user/logout", "anon");
        filterRuleMap.put("/user/login", "anon");
        // Swagger-ui
        filterRuleMap.put("/swagger-ui.html", "anon");
        filterRuleMap.put("/doc.html", "anon");
        filterRuleMap.put("/static/**", "anon");
        filterRuleMap.put("/swagger-resources/**", "anon");
        filterRuleMap.put("/v2/**", "anon");
        filterRuleMap.put("/webjars/**", "anon");
        // 访问401和404页面不通过我们的Filter
        filterRuleMap.put("/401", "anon");
        filterRuleMap.put("/404", "anon");
        // 其他资源需要jwt认证
        filterRuleMap.put("/**", "jwt");
        // 页面跳转（未授权）
        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 加入注解的使用，不加入这个注解不生效
     * 有个PointCut，主要就是两个作用：
     * 1、匹配所有类 ClassFilter
     * 2、匹配所有加了认证注解的方法 getMethodMatcher
     *
     * @param securityManager securityManager
     * @return AuthorizationAttributeSourceAdvisor
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

//    /**
//     * 密码校验规则HashedCredentialsMatcher
//     * 这个类是为了对密码进行编码的 ,
//     * 防止密码在数据库里明码保存 , 当然在登陆认证的时候 ,
//     * 这个类也负责对form里输入的密码进行编码
//     * 处理认证匹配处理器：如果自定义需要实现继承HashedCredentialsMatcher
//     */
//    @Bean("hashedCredentialsMatcher")
//    public HashedCredentialsMatcher hashedCredentialsMatcher() {
//        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
//        //指定加密方式为MD5
//        credentialsMatcher.setHashAlgorithmName("MD5");
//        //加密次数
//        credentialsMatcher.setHashIterations(3);
//        credentialsMatcher.setStoredCredentialsHexEncoded(true);
//        return credentialsMatcher;
//    }

}
