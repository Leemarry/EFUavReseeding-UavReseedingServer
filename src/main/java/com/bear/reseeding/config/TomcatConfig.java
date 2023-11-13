package com.bear.reseeding.config;

import org.springframework.context.annotation.Configuration;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

/**
 * @Auther: bear
 * @Date: 2022/9/19 09:49
 * @Description: tomcat 配置 http 转 https
 */
@Configuration
public class TomcatConfig {
    @Value("${httpServer.port:8080}")
    private Integer httpServerPort; //http的端口

    @Value("${server.port:8081}")
    private Integer serverPort;//https的端口，也是配置文件中配置的端口


    // 同时兼容http与https协议
    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(httpServerPort);
        tomcat.addAdditionalTomcatConnectors(connector);
        return tomcat;
    }


    // http自动转https
//    @Bean
//    public ServletWebServerFactory servletContainer() {
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint securityConstraint = new SecurityConstraint();
//                securityConstraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection = new SecurityCollection();
//                collection.addPattern("/*");
//                securityConstraint.addCollection(collection);
//                context.addConstraint(securityConstraint);
//            }
//        };
//        tomcat.addAdditionalTomcatConnectors(redirectConnector());
//        return tomcat;
//    }

    // http自动转https
//    private Connector redirectConnector() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setScheme("http");
//        connector.setPort(httpServerPort);
//        connector.setSecure(false);
//        connector.setRedirectPort(serverPort);
//        return connector;
//    }
}
