package com.bear.reseeding.config;

import com.bear.reseeding.filter.CrossDomainFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.servlet.Filter;

/**
 * @Auther: bear
 * @Date: 2021/7/16 09:22
 * @Description: null
 */
@Configuration
public class FilterConfig extends WebMvcConfigurationSupport {
    @Bean
    public Filter crossDomainFilter() {
        return new CrossDomainFilter();
    }

//    @Bean
//    public Filter filter2(){
//        return new Filter2();
//    }
    @Bean
    public FilterRegistrationBean setCrossDomainFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(crossDomainFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(1);   //order的数值越小，在所有的filter中优先级越高
        return filterRegistrationBean;
    }
//
//    @Bean
//    public FilterRegistrationBean setFilter2(){
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//        filterRegistrationBean.setFilter(filter2());
//        filterRegistrationBean.addUrlPatterns("/url2/*");
//        filterRegistrationBean.setOrder(2);   //order的数值越小，在所有的filter中优先级越高
//        return filterRegistrationBean;
//    }
}
