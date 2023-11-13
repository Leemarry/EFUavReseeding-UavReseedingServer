package com.bear.reseeding.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器，先于拦截器执行
 *
 * @Auther: bear
 * @Date: 2021/7/16 08:56
 * @Description: null
 * @WebFilter 注解千万别忘了，如果不想用这个注解，那么需要自己把Filter注解加入Bean容器。
 */
public class CrossDomainFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        //String ip = NetworkUtil.getRequestIP(request);
        String addr = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) {
            userAgent = "未知浏览器";
        } else if (userAgent.toLowerCase().contains("chrome")) {
            userAgent = "谷歌";
        } else if (userAgent.toLowerCase().contains("msie")) {
            userAgent = "IE";
        } else if (userAgent.toLowerCase().contains("firefox")) {
            userAgent = "火狐";
        }
//        LogUtil.logInfo("跨域处理中,地址：" + addr + ",代理" + userAgent);
        HttpServletResponse response = (HttpServletResponse) res;
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With,ClientId,token,Encryption");
        chain.doFilter(req, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }


    @Override
    public void destroy() {

    }

}
