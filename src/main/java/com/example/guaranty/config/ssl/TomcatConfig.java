//package com.example.guaranty.config.ssl;
//
//import org.apache.catalina.Context;
//import org.apache.catalina.connector.Connector;
//import org.apache.coyote.http11.Http11NioProtocol;
//import org.apache.tomcat.util.descriptor.web.SecurityCollection;
//import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
//import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
//import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author ming
// * @version 1.0.0
// * @date 2020/12/2 15:11
// **/
//@Configuration
//public class TomcatConfig {
//    @Bean
//    public ServletWebServerFactory servletContainer() {
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint securityConstraint = new SecurityConstraint();
//                securityConstraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection = new SecurityCollection();
//                //设置所有路径都配置https
//                collection.addPattern("/*");
//                securityConstraint.addCollection(collection);
//                context.addConstraint(securityConstraint);
//            }
//        };
//        tomcat.addAdditionalTomcatConnectors(getHttpConnector());
//        return tomcat;
//    }
//
//    private Connector getHttpConnector() {
//        Connector connector = new Connector(Http11NioProtocol.class.getName());
//        connector.setScheme("http");
//        connector.setPort(8888);
//        connector.setSecure(false);
//        connector.setRedirectPort(7000);
//        return connector;
//    }
//
//}
