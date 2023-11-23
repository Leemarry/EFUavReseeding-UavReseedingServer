package com.bear.reseeding.config;

import com.bear.reseeding.entity.EfUser;
import com.bear.reseeding.model.CurrentUser;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


import javax.servlet.http.HttpServletRequest;

/**
 * CurrentUser 注解实现类
 *
 * @Auther: bear
 * @Date: 2023/5/23 14:41
 * @Description: null
 */
public class CurrentUserHandlerMethodArgReslover implements HandlerMethodArgumentResolver {

    /**
     * 判断是否支持使用@CurrentUser注解的参数
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //如果该参数注解有@CurrentUser且参数类型是User
        return methodParameter.getParameterAnnotation(CurrentUser.class) != null && methodParameter.getParameterType() == EfUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        //取得 HttpServletRequest
        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
        //取出session中的User
        return request.getSession().getAttribute("currentUser");
    }
}
