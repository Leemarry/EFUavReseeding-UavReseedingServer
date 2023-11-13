package com.bear.reseeding.model;

import java.lang.annotation.*;

/**
 * 自定义参数 User
 *
 * @Auther: bear
 * @Date: 2023/5/23 14:33
 * @Description: null
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {
}
