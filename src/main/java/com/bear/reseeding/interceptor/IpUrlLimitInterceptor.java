package com.bear.reseeding.interceptor;

import com.bear.reseeding.utils.LogUtil;
import com.bear.reseeding.utils.NetworkUtil;
import com.bear.reseeding.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Auther: bear
 * @Date: 2022/8/30 14:30
 * @description: ip+url重复请求现在拦截器
 * 拦截器，只有preHandle方法返回true，postHandle、afterCompletion才有可能被执行；
 * 如果preHandle方法返回false，则该拦截器的postHandle、afterCompletion必然不会被执行。
 */
@Component
public class IpUrlLimitInterceptor implements HandlerInterceptor {

    private static final String LOCK_IP_URL_KEY = "lock_ip_";

    private static final String IP_URL_REQ_TIME = "ip_url_times_";

    private static final long LIMIT_TIMES = 10; //1秒最多允许访问的次数

    private static final int IP_LOCK_TIME = 60;  // 限制时间 单位：秒

    @Autowired
    private RedisUtils redisUtils;

    int getMinutes() {
        return IP_LOCK_TIME / 60;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) {
        LogUtil.logInfo("Request uri " + httpServletRequest.getRequestURI() + ",ip " + NetworkUtil.getIpAddr(httpServletRequest));
//        if (ipIsLock(NetworkUtil.getIpAddr(httpServletRequest))) {
//            LogUtil.logWarn("ip access denied:" + NetworkUtil.getIpAddr(httpServletRequest));
//            Result result = ResultUtil.error("当前操作过于频繁，请" + getMinutes() + "分钟后再试!!!");
//            returnJson(httpServletResponse, JSON.toJSONString(result));
//            return false;
//        }
//        if (!addRequestTime(NetworkUtil.getIpAddr(httpServletRequest), httpServletRequest.getRequestURI())) {
//            Result result = ResultUtil.error("当前操作过于频繁，请" + getMinutes() + "分钟后再试!!!");
//            returnJson(httpServletResponse, JSON.toJSONString(result));
//            return false;
//        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

    }

    /**
     * @param ip
     * @return java.lang.Boolean
     * @Description: 判断ip是否被禁用
     * @author: shuyu.wang
     * @date: 2019-10-12 13:08
     */
    private Boolean ipIsLock(String ip) {
        if (redisUtils.hasKey(LOCK_IP_URL_KEY + ip)) {
            return true;
        }
        return false;
    }

//    public Result isRedisConnected() {
//        try {
//            Jedis jedis = new Jedis(host, port);
//            String ping = jedis.ping();
//            if (!ping.equalsIgnoreCase("PONG")) {
//                return ResultUtil.error("数据服务未启动!");
//            }
//            return ResultUtil.success();
//        } catch (Exception e) {
//            return ResultUtil.error("数据服务未启动!");
//        }
//    }

    /**
     * @param ip
     * @param uri
     * @return java.lang.Boolean
     * @Description: 记录请求次数
     * @author: shuyu.wang
     * @date: 2019-10-12 17:18
     */
    private Boolean addRequestTime(String ip, String uri) {
        String key = IP_URL_REQ_TIME + ip + uri;
        if (redisUtils.hasKey(key)) {
            long time = redisUtils.incr(key, (long) 1);
            if (time >= LIMIT_TIMES) {
                redisUtils.getLock(LOCK_IP_URL_KEY + ip, ip, IP_LOCK_TIME);
                return false;
            }
        } else {
            redisUtils.getLock(key, (long) 1, 1);
        }
        return true;
    }

    private void returnJson(HttpServletResponse response, String json) {
        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(json);
        } catch (IOException e) {
            LogUtil.logError("IpUrlLimitInterceptor response error ---> " + e.getMessage());
        }
    }
}

