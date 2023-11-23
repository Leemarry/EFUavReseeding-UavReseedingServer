package com.bear.reseeding.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 读取 application.yml 配置信息数据值
 *
 * @Auther: bear
 * @Date: 2021/7/16 11:16
 * @Description: null
 */
@Configuration
public class AppConfig {

    /**
     * 获取MQTT服务地址
     * tcp://119.45.227.52:1883
     *
     * @return
     */
    public String getHostAddress() {
        return protocol + "://" + host + ":" + port;
    }

    /**
     * 主机IP地址
     */
    @Value("${spring.mqtt.host:127.0.0.1}")
    private String host;
    /**
     * 端口
     */
    @Value("${spring.mqtt.port:1883}")
    private Integer port;


    /**
     * Mqtt连接密码
     */
    @Value("${spring.mqtt.pwd:123456}")
    private String mqttpwd;
    /**
     * 订阅机巢主题
     */
    @Value("${spring.mqtt.subscribeTopicHive:efuav/HiveC/#}")
    private String subscribeTopicHive;
    /**
     * 订阅翼飞无人机主题
     */
    @Value("${spring.mqtt.subscribeTopicEfuav:efuav/uavappC/#}")
    private String subscribeTopicEfuav;
    /**
     * 订阅大疆无人机主题
     */
    @Value("${spring.mqtt.subscribeTopicDjiUav:efuav/djiappC/#}")
    private String subscribeTopicDjiUav;
    /**
     * 发布公共机巢主题
     */
    @Value("${spring.mqtt.publishTopicHive:efuav/HiveS/#}")
    private String publishTopicHive;
    /**
     * 发布公共翼飞无人机主题
     */
    @Value("${spring.mqtt.publishTopicEfuav:efuav/uavappS/#}")
    private String publishTopicEfuav;
    /**
     * 发布公共大疆无人机主题
     */
    @Value("${spring.mqtt.publishTopicDjiUav:efuav/djiappS/#}")
    private String publishTopicDjiUav;

    @Value("${server.port:8080}")
    public String servicePort;

    /**
     * 当前版本
     */
    @Value("${BasePath:C://efuav/UavSystem/}")
    public String basePath;

    /**
     * 当前版本
     */
    @Value("${app.version:1.0}")
    public String serviceVersion;

    /**
     * 打包时间
     */
    @Value("${app.buildtime:---}")
    public String serviceBuildDate;

    /**
     * 协议类型，TCP/UDP
     */
    @Value("${spring.mqtt.protocol:tcp}")
    private String protocol;

    /**
     * 流媒体服务器，true:自定义,false:云服务器
     */
    @Value("${spring.config.videoStreamStorage:false}")
    private boolean videoStreamStorage;
    /**
     * #照片储存 true存本地 false存minio 查看 删除照片也是
     */
    @Value("${spring.config.PhotoStorage:false}")
    private boolean PhotoStorage;
    /**
     * #数据库储存 true 开始存
     */
    @Value("${spring.config.DatabaseStorage:true}")
    private boolean DatabaseStorage;
    /**
     * 腾讯云视频流播放地址
     */
    @Value("${tencent.TxyPlayName}")
    private String TxyPlayName;
    /**
     * 腾讯云视频流播放密钥
     */
    @Value("${tencent.TxySecretStreamKey}")
    private String TxySecretStreamKey;
    /**
     * 腾讯云视频流播放模板
     */
    @Value("${tencent.TxyTemplateid}")
    private String TxyTemplateid;
    /**
     * 腾讯云视频流播放域名
     */
    @Value("${tencent.TxyDomainName}")
    private String TxyDomainName;
    /**
     * # 腾讯云直播服务器签名
     */
    @Value("${tencent.TxySecretId}")
    private String TxySecretId;
    /**
     * # 腾讯云直播服务器签名
     */
    @Value("${tencent.TxySecretKey}")
    private String TxySecretKey;
    /**
     * 腾讯云短信 陶沙账户
     */
    @Value("${tencent.SMSSECREID}")
    private String SMSSECREID;
    /**
     * 腾讯云短信 陶沙账户秘钥
     */
    @Value("${tencent.SMSSECREKEY}")
    private String SMSSECREKEY;
    /**
     * 短信 appid
     */
    @Value("${tencent.SMSAPPID}")
    private String SMSAPPID;
    /**
     * 申请的模版ID
     */
    @Value("${tencent.TEMPLATEID}")
    private String TEMPLATEID;
    /**
     * 短信注册ID
     */
    @Value("${tencent.SMSREGISTERID}")
    private String SMSREGISTERID;
    /**
     * 音视频trtc的appid
     */
    @Value("${TxyTrtc.TxyTrtcSdkAppId}")
    private String TxyTrtcSdkAppId;
    /**
     * 音视频trtc的密钥
     */
    @Value("${TxyTrtc.TxyTrtcSecretKey}")
    private String TxyTrtcSecretKey;
    /**
     * 秸秆焚烧模板ID
     */
    @Value("${tencent.STRAWID}")
    private String STRAWID;

    public String getTxyTrtcSdkAppId() {
        return TxyTrtcSdkAppId;
    }

    public void setTxyTrtcSdkAppId(String txyTrtcSdkAppId) {
        TxyTrtcSdkAppId = txyTrtcSdkAppId;
    }

    public String getTxyTrtcSecretKey() {
        return TxyTrtcSecretKey;
    }

    public void setTxyTrtcSecretKey(String txyTrtcSecretKey) {
        TxyTrtcSecretKey = txyTrtcSecretKey;
    }

    public String getSMSSECREID() {
        return SMSSECREID;
    }

    public void setSMSSECREID(String SMSSECREID) {
        this.SMSSECREID = SMSSECREID;
    }

    public String getSMSSECREKEY() {
        return SMSSECREKEY;
    }

    public void setSMSSECREKEY(String SMSSECREKEY) {
        this.SMSSECREKEY = SMSSECREKEY;
    }

    public String getSMSAPPID() {
        return SMSAPPID;
    }

    public void setSMSAPPID(String SMSAPPID) {
        this.SMSAPPID = SMSAPPID;
    }

    public String getTEMPLATEID() {
        return TEMPLATEID;
    }

    public void setTEMPLATEID(String TEMPLATEID) {
        this.TEMPLATEID = TEMPLATEID;
    }

    public String getSMSREGISTERID() {
        return SMSREGISTERID;
    }

    public void setSMSREGISTERID(String SMSREGISTERID) {
        this.SMSREGISTERID = SMSREGISTERID;
    }

    public String getTxySecretId() {
        return TxySecretId;
    }

    public void setTxySecretId(String txySecretId) {
        TxySecretId = txySecretId;
    }

    public String getTxySecretKey() {
        return TxySecretKey;
    }

    public void setTxySecretKey(String txySecretKey) {
        TxySecretKey = txySecretKey;
    }

    public String getTxyDomainName() {
        return TxyDomainName;
    }

    public void setTxyDomainName(String txyDomainName) {
        TxyDomainName = txyDomainName;
    }

    public String getTxyTemplateid() {
        return TxyTemplateid;
    }

    public void setTxyTemplateid(String txyTemplateid) {
        TxyTemplateid = txyTemplateid;
    }

    public String getTxySecretStreamKey() {
        return TxySecretStreamKey;
    }

    public void setTxySecretStreamKey(String txySecretStreamKey) {
        TxySecretStreamKey = txySecretStreamKey;
    }

    public String getTxyPlayName() {
        return TxyPlayName;
    }

    public void setTxyPlayName(String txyPlayName) {
        TxyPlayName = txyPlayName;
    }

    public String getMqttpwd() {
        return mqttpwd;
    }

    public void setMqttpwd(String mqttpwd) {
        this.mqttpwd = mqttpwd;
    }

    public String getSubscribeTopicEfuav() {
        return subscribeTopicEfuav;
    }

    public void setSubscribeTopicEfuav(String subscribeTopicEfuav) {
        this.subscribeTopicEfuav = subscribeTopicEfuav;
    }

    public String getSubscribeTopicDjiUav() {
        return subscribeTopicDjiUav;
    }

    public void setSubscribeTopicDjiUav(String subscribeTopicDjiUav) {
        this.subscribeTopicDjiUav = subscribeTopicDjiUav;
    }

    public String getPublishTopicDjiUav() {
        return publishTopicDjiUav;
    }

    public void setPublishTopicDjiUav(String publishTopicDjiUav) {
        this.publishTopicDjiUav = publishTopicDjiUav;
    }

    public String getPublishTopicEfuav() {
        return publishTopicEfuav;
    }

    public void setPublishTopicEfuav(String publishTopicEfuav) {
        this.publishTopicEfuav = publishTopicEfuav;
    }


    public boolean isDatabaseStorage() {
        return DatabaseStorage;
    }

    public void setDatabaseStorage(boolean databaseStorage) {
        DatabaseStorage = databaseStorage;
    }

    public boolean isPhotoStorage() {
        return PhotoStorage;
    }

    public void setPhotoStorage(boolean photoStorage) {
        PhotoStorage = photoStorage;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getSTRAWID() {
        return STRAWID;
    }

    public void setSTRAWID(String STRAWID) {
        this.STRAWID = STRAWID;
    }

    public boolean isVideoStreamStorage() {
        return videoStreamStorage;
    }

    public void setVideoStreamStorage(boolean videoStreamStorage) {
        this.videoStreamStorage = videoStreamStorage;
    }

    public String getProjectInfo() {
        return "\n================= project =================\n" +
                String.format("\nservice version:%s\n", serviceVersion) +
                String.format("\nservice build date:%s\n", serviceBuildDate) +
                "\n================= project =================\n";
    }
}
