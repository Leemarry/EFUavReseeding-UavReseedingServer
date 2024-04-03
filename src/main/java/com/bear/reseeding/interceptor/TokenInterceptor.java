package com.bear.reseeding.interceptor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bear.reseeding.common.ResultUtil;
import com.bear.reseeding.entity.EfUser;
import com.bear.reseeding.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 自定义拦截器
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Value("${releaseIP}")
    private String[] releaseIPArray;


    @Autowired
    private RedisUtils redisUtils;

    public String getRequestParams(HttpServletRequest request) {
        StringBuilder parms = new StringBuilder();
        try {
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                String[] paramValues = request.getParameterValues(paramName);
                parms.append(paramName).append(": ");
                for (String paramValue : paramValues) {
                    parms.append(paramValue).append(" ");
                }
            }
        } catch (Exception ignored) {
        }
        return parms.toString();
    }

    /**
     * 这个方法在每个请求之前都会触发
     *
     * @param request
     * @param response
     * @param o
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) {
//        return true;
        String ipWww = NetworkUtil.getIpAddr(request);
        String ipLocal = request.getLocalAddr();
        LogUtil.logMessage("Token Interceptor," + ipWww + "(" + ipLocal + ")" + " 请求 " + request.getServletPath() + " 到达,参数 (" + getRequestParams(request) + ")...");
        response.setCharacterEncoding("utf-8");
        String token = request.getHeader("token");
        //String ClientId = request.getHeader("ClientId");
        //String jsonParam = getResultFromRequest(request);
        String error = "通讯异常!";
        for (String ip : releaseIPArray) {
            if (ip.equals(ipWww)) {
                LogUtil.logMessage("放行Ip:["+ipWww+"]");
                return true;
            }
        }

        if (token == null) {
            error = "验证信息失败，请重新登录！";
            LogUtil.logWarn("The token is null !!!");
        } else {
            //验证token是否正确
            boolean result = TokenUtil.verify(token);
            redisUtils.set(token + "_LastOpterTime", System.currentTimeMillis(), 1L, TimeUnit.HOURS); //上次操作时间
            if (result) {
                EfUser user = TokenUtil.getUser(token);
                request.getSession().setAttribute("currentUser", user);
                return true;
            }
            LogUtil.logWarn("The token was expired !!!");
            error = "验证信息失败，请重新登录！";
        }
        responseMessage(response, ResultUtil.error(error));
        LogUtil.logWarn("Token Interceptor ," + ipWww + "(" + ipLocal + ") cannot be accessed：" + error);
        return false;
        //当拦截器的preHandle返回false后的处理：
        //后续的拦截器以及请求处理方法等都不会再执行。当前返回false的拦截器之前的拦截器的afterCompletion方法会执行。
    }

    static String getRequestStr(HttpServletRequest request) {
        String temp = "";
        // 获取请求行的相关信息
        String method = request.getMethod();
//        StringBuffer url = request.getRequestURL();
//        String queryString = request.getQueryString();
//        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String remoteAddr = request.getRemoteAddr();
//        String remoteHost = request.getRemoteHost();
//        int remotePort = request.getRemotePort();
        String localAddr = request.getLocalAddr();
//        String localName = request.getLocalName();//有时卡半天
//        int localPort = request.getLocalPort();
//        String serverName = request.getServerName();
//        int serverPort = request.getServerPort();
//        StringBuffer requestURL = request.getRequestURL();

        temp += remoteAddr;
        temp += " ";
        temp += localAddr;
        temp += " ";
        temp += method;
        temp += " ";
        temp += servletPath;
        temp += " ";


//        System.out.println("getMethod : " + request.getMethod());
//        System.out.println("getRequestURI:" + request.getRequestURL());
//        System.out.println("getQueryString:" + request.getQueryString());
//        System.out.println("getContextPath:" + request.getContextPath());
//        System.out.println("getServletPath:" + request.getServletPath());
//        System.out.println("getRemoteAddr : " + request.getRemoteAddr());
//        System.out.println("getRemoteHost : " + request.getRemoteHost());
//        System.out.println("getRemotePort : " + request.getRemotePort());
//        System.out.println("getLocalAddr : " + request.getLocalAddr());
//        System.out.println("getLocalName : " + request.getLocalName());
//        System.out.println("getLocalPort : " + request.getLocalPort());
//        System.out.println("getServerName : " + request.getServerName());
//        System.out.println("getServerPort : " + request.getServerPort());
//        System.out.println("getRequestURL : " + request.getRequestURL());

        //getMethod : POST
        //getRequestURI:http://192.168.10.114:8080/efapi/pvinspect/user/test2
        //getQueryString:null
        //getContextPath:/efapi/pvinspect
        //getServletPath:/user/test2
        //getRemoteAddr : 192.168.10.114
        //getRemoteHost : 192.168.10.114
        //getRemotePort : 8281
        //getLocalAddr : 192.168.10.114
        //getLocalName : 192.168.10.114
        //getLocalPort : 8080
        //getServerName : 192.168.10.114
        //getServerPort : 8080
        //getRequestURL : http://192.168.10.114:8080/efapi/pvinspect/user/test2
        return temp;
    }

    /**
     * 将JSONArray 转换为map
     */
    public static Map<String, Object> convertJSONAryToMap(JSONArray jsonary) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < jsonary.size(); i++) {
            JSONObject json = jsonary.getJSONObject(i);
            map.put(json.getString("name"), json.get("value"));
        }
        return map;
    }

    /**
     * 从request中获取请求字符串
     */
    public String getResultFromRequest(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(req.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
//        LogUtil.logInfo("Token拦截器 postHandle...");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
//        LogUtil.logInfo("Token拦截器 afterCompletion...");

    }

    /**
     * 返回信息给客户端
     *
     * @param response
     * @param apiResponse
     */
    private void responseMessage(HttpServletResponse response, Object apiResponse) {
//        LogUtil.logInfo("Token拦截器，输出流...");
        PrintWriter writer = null;
        try {
            response.setContentType("application/json;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            writer = response.getWriter();
            writer.print(JSONObject.toJSONString(apiResponse));
            writer.flush();
            writer.close();
        } catch (Exception e) {
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 返回客户端数据
     */
    private void responseMessage2(HttpServletResponse response, String result) throws Exception {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(result);
        } catch (IOException e) {
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}