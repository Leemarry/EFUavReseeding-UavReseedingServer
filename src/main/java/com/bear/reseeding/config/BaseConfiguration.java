package com.bear.reseeding.config;

import com.bear.reseeding.interceptor.IpUrlLimitInterceptor;
import com.bear.reseeding.interceptor.RequestInterceptor;
import com.bear.reseeding.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * @Auther: bear
 * @Date: 2021/7/15 18:15
 * @Description: null
 * 注：此处需要注意，如果配置类是实现的WebMvcConfigurer类，要查一下代码中是否有WebMvcConfigurationSupport类，
 * 因为优先要使用WebMvcConfigurationSupport，否则addInterceptor无法加载。
 */
@Configuration
public class BaseConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Autowired
    private RequestInterceptor requestInterceptor;

    @Autowired
    private IpUrlLimitInterceptor ipUrlLimitInterceptor;

    /**
     * 接口调用拦截：不需要拦截的地址
     */
    String[] filterExcludePath = {
            "/debugger/**",
            "/websocket/**",
            "/user/test",
            "/user/login", "/user/appLogin","/efUser/login",
            "/user/connect",
            "/js/**", "/css/**", "/images/**", "/resource/**","/resourceminio/**", "/mapresource/**"
    };

    /**
     * IP拦截：不需要拦截的地址
     */
    String[] filterExcludePathIp = {
            "/websocket/**", "/efTimedTask/getTimedTask", "/js/**", "/css/**", "/images/**", "/resource/**", "/mapresource/**"
            , "/efTencent/**", "/efYunNGuangfu/**"
    };

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        //注册@CurrentUser注解的实现类
        argumentResolvers.add(new CurrentUserHandlerMethodArgReslover());
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //http://localhost:9529/images/efuav/usericos/admin.png
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**").excludePathPatterns(filterExcludePath);
        registry.addInterceptor(requestInterceptor).addPathPatterns("/**").excludePathPatterns(filterExcludePath);
        registry.addInterceptor(ipUrlLimitInterceptor).addPathPatterns("/**").excludePathPatterns(filterExcludePathIp);
        super.addInterceptors(registry);
    }

/*    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //和页面有关的静态目录都放在项目的static目录下 ,如http://localhost:8080/upload/1.png。
        registry.addResourceHandler("/images/**").
                addResourceLocations("file:C:/efuav/UavSystem/");
    }*/

    // 全局跨域
    // 如果有安全框架，需要在框架中启用CORS;还有一种基于"过滤器"的跨域设置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 允许跨域访问的路径
        registry.addMapping("/**");
        //         .allowedOrigins("http://domain2.com")                    //允许跨域访问的源
        //                .allowedMethods("PUT", "DELETE")                  //允许请求方法
        //                .allowedHeaders("header1", "header2", "header3")  //允许头部设置
        //                .exposedHeaders("header1", "header2")
        //                .allowCredentials(false).maxAge(3600);            //是否发送cookie，预检间隔时间
    }

    //添加放行静态资源
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = "C:/efuav/UavSystem/";
        String MapTilesPath = "C:/efuav/mapresource/";
        Properties prop = null;
        try {
            prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("application.properties"));
            path = prop.getProperty("BasePath");
            MapTilesPath = prop.getProperty("MapTilesPath");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //文件磁盘图片url 映射
        //配置server虚拟路径，handler为前台访问的目录，locations为files相对应的本地路径
        // 完整路径为 http://localhost:8765/efapi/uavsystem/efuav/UavSystem/efuav/usericos/admin.png，前台直接显示图片为：efuav/UavSystem/efuav/usericos/admin.png 即可访问
//        registry.addResourceHandler("/efuav/**").addResourceLocations("file:C:/efuav/");
//        C://efuav/reseeding/photo/uav/1ZNBJ5F00C008L/image/20231205/dji9700089_20231205190730.JPG
        // 完整路径为 http://localhost:8765/efapi/uavsystem/efuav/usericos/admin.png  ，前台直接显示图片为：efuav/usericos/admin.png 即可访问
        registry.addResourceHandler("/resource/**").addResourceLocations("file:" + path);
        registry.addResourceHandler("/mapresource/**").addResourceLocations("file:" + MapTilesPath);
        //配置静态文件路径，与上面并不冲突
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/");
    }
}