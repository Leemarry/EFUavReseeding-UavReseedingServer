package com.bear.reseeding.utils;

import com.bear.reseeding.MyApplication;

import java.util.Date;

public class PlayUrlUtil {


//    webrtc://domain/AppName/StreamName?txSecret=Md5(key+StreamName+hex(time))&txTime=hex(time)
//    http://domain/AppName/StreamName.flv?txSecret=Md5(key+StreamName+hex(time))&txTime=hex(time)
//    rtmp://domain/AppName/StreamName?txSecret=Md5(key+StreamName+hex(time))&txTime=hex(time)
//    http://domain/AppName/StreamName.m3u8?txSecret=Md5(key+StreamName+hex(time))&txTime=hex(time)

    /**
     * 获取播放流地址工具类
     * 07JDE6E0020240-stream1
     * 07JDE6E0020240-stream2
     * 07JDE6E0020240-stream3
     * 07JDE6E0020240-stream4
     * 07JDE6E0020240-stream5
     * <p>
     * xznjhuieqwrwe-stream6
     * xznjhuieqwrwe-stream7
     * xznjhuieqwrwe-stream8
     * xznjhuieqwrwe-stream9
     * xznjhuieqwrwe-stream10
     *
     * @param StreamName
     * @return 播放流地址
     */
    public static String getPlayUrl(String StreamName) {
        try {
            String url = null;
            boolean videoStreamStorage = false;    //流媒体服务器，true:自定义,false:云服务器
            if (MyApplication.appConfig != null) {
                videoStreamStorage = MyApplication.appConfig.isVideoStreamStorage();
            }
            String appName = "live";
//            Properties prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("application.properties"));
//            String TxyPlayName = prop.getProperty("TxyPlayName");
            String TxyPlayName = MyApplication.appConfig.getTxyPlayName();
            if (videoStreamStorage) {
                url = TxyPlayName + "/" + appName + "/" + StreamName;
            } else {
//                String TxySecretKey = prop.getProperty("TxySecretKey");
//                String TxySecretStreamKey = prop.getProperty("TxySecretStreamKey");
                String TxySecretStreamKey = MyApplication.appConfig.getTxySecretStreamKey();
                String txTime = Base64Util.IntToHexString(Integer.parseInt(String.valueOf(DateUtil.getDate(new Date(System.currentTimeMillis())).getTime() / 1000)));
                String txSecret = MD5Util.md5Encode(TxySecretStreamKey + StreamName + txTime);
                url = TxyPlayName + "/" + appName + "/" + StreamName + "?" + "txSecret=" + txSecret + "&txTime=" + txTime;
            }
            return url;
        } catch (Exception e) {
            LogUtil.logError("获取播放流地址出错：" + e.toString());
            return "获取播放流地址失败！";
        }
    }

    public static String getPlayUrlFlv(String StreamName) {
        try {
            String url = null;
            boolean videoStreamStorage = false;    //流媒体服务器，true:自定义,false:云服务器
            if (MyApplication.appConfig != null) {
                videoStreamStorage = MyApplication.appConfig.isVideoStreamStorage();
            }
            String appName = "live";
//            Properties prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("application.properties"));
//            String TxyPlayName = prop.getProperty("TxyPlayName");
            String TxyPlayName = MyApplication.appConfig.getTxyPlayName();
            if (videoStreamStorage) {
                url = TxyPlayName + "/" + appName + "/" + StreamName;
            } else {
//                String TxySecretKey = prop.getProperty("TxySecretKey");
//                String TxySecretStreamKey = prop.getProperty("TxySecretStreamKey");
                String TxySecretStreamKey = MyApplication.appConfig.getTxySecretStreamKey();
                String txTime = Base64Util.IntToHexString(Integer.parseInt(String.valueOf(DateUtil.getDate(new Date(System.currentTimeMillis())).getTime() / 1000)));
                String txSecret = MD5Util.md5Encode(TxySecretStreamKey + StreamName + txTime);
                url = TxyPlayName + "/" + appName + "/" + StreamName + ".flv?" + "txSecret=" + txSecret + "&txTime=" + txTime;
            }
            return url;
        } catch (Exception e) {
            LogUtil.logError("获取播放流地址出错：" + e.toString());
            return "获取播放流地址失败！";
        }
    }

    public static String getPlayUrlM3u8(String StreamName) {
        try {
            String url = null;
            boolean videoStreamStorage = false;    //流媒体服务器，true:自定义,false:云服务器
            if (MyApplication.appConfig != null) {
                videoStreamStorage = MyApplication.appConfig.isVideoStreamStorage();
            }
            String appName = "live";
//            Properties prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("application.properties"));
//            String TxyPlayName = prop.getProperty("TxyPlayName");
            String TxyPlayName = MyApplication.appConfig.getTxyPlayName();
            if (videoStreamStorage) {
                url = TxyPlayName + "/" + appName + "/" + StreamName;
            } else {
//                String TxySecretKey = prop.getProperty("TxySecretKey");
//                String TxySecretStreamKey = prop.getProperty("TxySecretStreamKey");
                String TxySecretStreamKey = MyApplication.appConfig.getTxySecretStreamKey();
                String txTime = Base64Util.IntToHexString(Integer.parseInt(String.valueOf(DateUtil.getDate(new Date(System.currentTimeMillis())).getTime() / 1000)));
                String txSecret = MD5Util.md5Encode(TxySecretStreamKey + StreamName + txTime);
                url = TxyPlayName + "/" + appName + "/" + StreamName + ".m3u8?" + "txSecret=" + txSecret + "&txTime=" + txTime;
            }
            return url;
        } catch (Exception e) {
            LogUtil.logError("获取播放流地址出错：" + e.toString());
            return "获取播放流地址失败！";
        }
    }

    public static void main(String[] args) {
        PlayUrlUtil.getPlayUrl("123");
    }
}
