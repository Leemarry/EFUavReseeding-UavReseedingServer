package com.bear.reseeding.utils;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 获取网络相关数据
 *
 * @Auther: bear
 * @Date: 2021/7/15 17:37
 * @Description: null
 */
public class NetworkUtil {

    /**
     * 获取公网ip
     *
     * @param request
     * @return
     */
    public static String getIpAddrBack(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            try {
                ip = request.getRemoteAddr();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            if (StringUtils.isBlank(ip) || "127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                if (null != inet) {
                    ip = inet.getHostAddress();
                }
            }
        }
        if (StringUtils.isNotBlank(ip) && ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        return StringUtils.isNotBlank(ip) ? ip.trim() : null;
    }

    /**
     * 获取公网ip
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     * 根据HttpServletRequest获取IP地址
     *
     * @param request 输入请求
     * @return Ip地址
     */
    public static String getRequestIP(HttpServletRequest request) {
        final String UnknownIP = "unknown";
        final String LoopbackIP = "127.0.0.1";
        String strAddr = null;
        try {
            strAddr = request.getHeader("x-forwarded-for");
            if (strAddr == null || strAddr.length() == 0 || UnknownIP.equalsIgnoreCase(strAddr)) {
                strAddr = request.getHeader("Proxy-Client-IP");
            }
            if (strAddr == null || strAddr.length() == 0 || UnknownIP.equalsIgnoreCase(strAddr)) {
                strAddr = request.getHeader("WL-Proxy-Client-IP");
            }
            if (strAddr == null || strAddr.length() == 0 || UnknownIP.equalsIgnoreCase(strAddr)) {
                strAddr = request.getRemoteAddr();
                if (LoopbackIP.equals(strAddr)) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    strAddr = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (strAddr != null && strAddr.length() > 15) { // "***.***.***.***".length()
                if (strAddr.indexOf(",") > 0) {
                    strAddr = strAddr.substring(0, strAddr.indexOf(","));
                }
            }
        } catch (Exception e) {
            strAddr = "127.0.0.1";
        }
        return strAddr;
    }
}
