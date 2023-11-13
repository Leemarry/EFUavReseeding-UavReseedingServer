package com.bear.reseeding.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: bear
 * @Date: 2021/7/15 18:15
 * @Description: null
 */
@Component
public class RequestInterceptor implements HandlerInterceptor {

    /**
     * 这个方法是在访问接口之前执行的
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        LogUtil.logInfo(" RequestInterceptor 请求拦截器...");
        return true;
    }
}
