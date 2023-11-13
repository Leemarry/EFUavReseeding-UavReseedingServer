package com.bear.reseeding.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * @Auther: bear
 * @Date: 2023/7/19 11:03
 * @Description: null
 */
public class RequestUtil {


    /**
     * 获取 HttpServletRequest 中的所有参数
     *
     * @param request HttpServletRequest请求流
     * @return HashMap<String, Object>
     */
    public static HashMap<String, Object> getRequestParam(HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            Enumeration names = request.getParameterNames();
            while (names.hasMoreElements()) {
                try {
                    String key = names.nextElement().toString();
                    map.put(key, request.getParameterValues(key)[0]);
                } catch (Exception ignored) {
                }
            }
        } catch (Exception e) {
            LogUtil.logError("读取HttpServletRequest请求流的参数异常：" + e.toString());
        }


         /* Enumeration names = request.getParameterNames();
            while (names.hasMoreElements()) {
                String key = names.nextElement().toString();
                if ("MediaName".equals(key)) {
                    MediaName = request.getParameterValues(key)[0];
                } else if ("UavID".equals(key)) {
                    UavID = request.getParameterValues(key)[0];
                } else if ("SavePath".equals(key)) {
                    SavePath = request.getParameterValues(key)[0];
                } else if ("CreatTime".equals(key)) {
                    MediaCreatTime = Long.parseLong(request.getParameterValues(key)[0]);   //媒体文件的时间戳
                } else if ("SuffixName".equals(key)) {
                    suffixName = request.getParameterValues(key)[0];   //后缀名 .JPEG
                } else if ("Type".equals(key)) {
                    type = request.getParameterValues(key)[0];   //后缀名 .JPEG
                } else if ("StreamSource".equals(key)) {
                    StreamSource = request.getParameterValues(key)[0];   //照片种类，zoom,wide...
                } else if ("GimbalRoll".equals(key)) {
                    GimbalRollStr = request.getParameterValues(key)[0];   //横滚值.
                } else if ("GimbalPitch".equals(key)) {
                    GimbalPitchStr = request.getParameterValues(key)[0];   //俯仰值.
                } else if ("GimbalYaw".equals(key)) {
                    GimbalYawStr = request.getParameterValues(key)[0];   //朝向.
                } else if ("Alt".equals(key)) {
                    AltStr = request.getParameterValues(key)[0];   //相对高度.
                } else if ("Altabs".equals(key)) {
                    AltabsStr = request.getParameterValues(key)[0];   //海拔高度.
                } else if ("Latitude".equals(key)) {
                    latStr = request.getParameterValues(key)[0];   //纬度.
                } else if ("Longitude".equals(key)) {
                    lngStr = request.getParameterValues(key)[0];   //经度.
                } else if ("ExceptionType".equals(key)) {
                    exceptionType = request.getParameterValues(key)[0];   //异常类型
                }
            }*/
        return map;
    }
}
