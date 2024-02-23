package com.bear.reseeding.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.KeyPair;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局记录控制器
 */
@Controller
@RequestMapping("/efapi/static")
public class EfStaticController {

    /**
     * 调试模式
     */
    public static final boolean ISPrintAll = false;

    /**
     * 调试模式，调试模式时不会储存数据到数据库中
     */
    public static final boolean ISDEBUG = false;

    /**
     * 系统TAG，用于记录日志
     */
    public static final String TAGSYSTEM = "EFUAV：";

    /**
     * AES 加密 Key
     */
    public static String AesKey = "";
    /**
     * AES 加密 IV
     */
    public static String AesIV = "";
    /**
     * RSA 加密密匙
     */
    public static KeyPair RsaKeyPair = null;

    //记录客户端公钥
    public static ConcurrentHashMap<String, String> MapClientPublicKeys = new ConcurrentHashMap<>();

    /**
     * 无人机视频和照片储存路径：  C:/efuav/EfUavSystem/photo/uav/(images|thumbnail)/20210507/xxx.jpg
     * 停机坪视频和照片储存路径：  C:/efuav/EfUavSystem/photo/hive/(images|thumbnail)/20210507/xxx.jpg
     */
    public static final String FILE_BASEPATH_MEDIA = "C:/efuav/UavSystem/photo/";
    /**
     * 用户头像
     */
    public static final String ICO_BASEPATH = "C:/efuav/UavSystem/";
    /**
     * 公司头像
     */
    public static final String LOGOICO_BASEPATH = "C:/efuav/UavSystem/";
    /**
     * 分组封面
     */
    public static final String GROUPICO_BASEPATH = "C:/efuav/UavSystem/";

    /**
     * 腾讯云服务器 ,音视频通话SDK
     * SdkAppId
     * ExpireTime
     * SecretKey
     */
    public static long TxyTrtcSdkAppId = 1400739024;
    public static int TxyTrtcExpireTime = 604800;
    public static String TxyTrtcSecretKey = "e364909a702f53e1702412a4e130d98187f66a2ecca22dd4f382de1e2e6c276d";
}
