package com.bear.reseeding;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bear.reseeding.config.AppConfig;
import com.bear.reseeding.datalink.MqttUtil;
import com.bear.reseeding.model.MqttItem;
import com.bear.reseeding.utils.LogUtil;
import com.bear.reseeding.utils.RSAUtil;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.util.text.BasicTextEncryptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
@MapperScan(value = "com.bear.reseeding.dao")
@Component
@EnableScheduling
@EnableEncryptableProperties
public class MyApplication extends SpringBootServletInitializer {

    private static MqttItem mqttDji;
    private static MqttItem mqttEf;
    public static ConcurrentHashMap<String, Object> keyObj = new ConcurrentHashMap<>();

    public static void main(String[] args) {
//        test();
        SpringApplication.run(MyApplication.class, args);
        Init();
    }

    @Autowired
    public void setMqttItemDji(MqttItem mqtt) {
        mqttDji = mqtt;
    }

    @Autowired
    public void setMqttItemEf(MqttItem mqtt) {
        mqttEf = mqtt;
    }

    @Autowired
    public MyApplication(AppConfig appConfig) {
        MyApplication.appConfig = appConfig;
    }

    public static void test() {
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        // application.properties, jasypt.encryptor.password 加密盐值
        encryptor.setPassword("efuav1603pvi");
        System.out.println("efuav_pv_server：" + encryptor.encrypt("efuav_pv_server"));
        System.out.println("gx13js!n34d5sd_a1xc：" + encryptor.encrypt("gx13js!n34d5sd_a1xc"));
        System.out.println("123456：" + encryptor.encrypt("123456"));

        // 加密数据库连接用户名
        System.out.println("数据库用户名 urc 加密：" + encryptor.encrypt("urc"));
        System.out.println("数据库用户名 root 加密：" + encryptor.encrypt("root"));
        System.out.println("数据库 efzn8888 密码加密：" + encryptor.encrypt("efzn8888"));
        System.out.println("minio账户：" + encryptor.encrypt("efsoft"));
        System.out.println("minio桶名：" + encryptor.encrypt("efuav"));
        System.out.println("minio密码：" + encryptor.encrypt("efuavminio76832gysc4s23324dsdsa"));

        // 加密数据库连接密码
        System.out.println("数据库密码加密：" + encryptor.encrypt("MpKybzDGz4BkD5L4"));
        System.out.println("本机 docker 数据库密码加密：" + encryptor.encrypt("ef76vh32a54x~!23"));

        // 加密ip地址
        System.out.println("IP加密：" + encryptor.encrypt("175.178.237.52"));
    }

    public static AppConfig appConfig;

    /**
     * RSA 加密密匙
     */
    public static KeyPair RsaKeyPair = null;

    // 为war包， 打包springboot项目
    // jar包直接：mvn clean package
    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }


    // 先执行static代码块，再执行该方法
    static void Init() {
        InitRsa();
        InitFastJsonParm();
        InitMqtt();
    }

    static void InitRsa() {
        try {
            RsaKeyPair = RSAUtil.getKeyPair();
//            LogUtil.logInfo("生成 RSA 密匙完成: \n 公匙：" + RSAUtil.getPublicKey(RsaKeyPair) + "\n 私匙：" + RSAUtil.getPrivateKey(RsaKeyPair));
        } catch (Exception e) {
            LogUtil.logError("生成 RSA 密匙失败：" + e.getMessage());
        }
    }

    //初始化 mqtt 连接
    public static void InitMqtt() {
        try {
            String hostAddress = appConfig.getHostAddress();
            String mqttpwd = appConfig.getMqttpwd();
            String version = appConfig.serviceVersion;

            mqttDji.init(hostAddress, "reseeding", mqttpwd, appConfig.getSubscribeTopicDjiUav(), appConfig.getPublishTopicDjiUav(), MqttUtil.Tag_Djiapp);
            mqttDji.StartMqtt();

            mqttEf.init(hostAddress, "reseeding", mqttpwd, appConfig.getSubscribeTopicEfuav(), appConfig.getPublishTopicEfuav(), MqttUtil.Tag_efuavapp);
            mqttEf.StartMqtt();

            MqttUtil.MapMqttItem.clear();
            MqttUtil.MapMqttItem.put(MqttUtil.Tag_Djiapp, mqttDji);
            MqttUtil.MapMqttItem.put(MqttUtil.Tag_efuavapp, mqttEf);
        } catch (Exception e) {
            LogUtil.logError("初始化Mqtt连接失败：" + e.toString());
        }
    }


    //初始化 fastjson 参数
    static void InitFastJsonParm() {
        // 是否输出值为null的字段,默认为false
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteMapNullValue.getMask();
        // 数值字段如果为null,输出为0,而非null
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteNullNumberAsZero.getMask();
        // List字段如果为null,输出为[],而非null
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteNullListAsEmpty.getMask();
        // 字符类型字段如果为null,输出为 "",而非null
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteNullStringAsEmpty.getMask();
    }
}
