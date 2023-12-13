package com.bear.reseeding.datalink;

import com.bear.reseeding.model.MqttItem;
import com.bear.reseeding.utils.BytesUtil;
import com.bear.reseeding.utils.LogUtil;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Auther: bear
 * @Date: 2021/7/16 09:52
 * @Description: null
 */
public class MqttUtil {

    //*********************  修改后的 start *********************
    public final static String Tag_Hive = "hive";
    public final static String Tag_Djiapp = "dji";
    public final static String Tag_efuavapp = "efuav";

    /**
     * 所有连接的MQTT客户端
     */
    public static ConcurrentMap<String, MqttItem> MapMqttItem = new ConcurrentHashMap<>();

    /**
     * 获取序列号
     *
     * @param mqtttag 用于寻找MQTT客户端的标记
     * @return 序列号
     */
    public static byte getSeqID(String mqtttag) {
        try {
            MqttItem mqttItem = MapMqttItem.getOrDefault(mqtttag, null);
            if (mqttItem != null) {
                return mqttItem.getSeqID();
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 推送消息
     *
     * @param mqtttag 用于寻找MQTT客户端的标记
     * @param packet  需要推送的完整数据包
     */
    @Deprecated
    public static void publishToClient(String mqtttag, byte[] packet) {
        try {
            MqttItem mqttItem = MapMqttItem.getOrDefault(mqtttag, null);
            if (mqttItem != null) {
                mqttItem.Publish(packet, 1);
            } else {
                LogUtil.logInfo("(发布) MQTT " + mqtttag + " 发布内容：" + BytesUtil.printHexBinary(packet) + "失败，未找到对应的MQTT客户端!");
            }
        } catch (Exception e) {
            LogUtil.logInfo("(发布) MQTT " + mqtttag + " 发布内容：" + BytesUtil.printHexBinary(packet) + "失败!" + e.getMessage());
        }
    }

    /**
     * 推送消息，自动生成主题
     *
     * @param mqtttag  用于寻找MQTT客户端的标记
     * @param packet   需要推送的完整数据包
     * @param deviceSn 设备的唯一编号，无人机编号。机巢编号
     */
    public static void publish(String mqtttag, byte[] packet, String deviceSn) {
        if (mqtttag.equals(MqttUtil.Tag_Djiapp)) {
            publishToClient(mqtttag, packet, "efuav/djiappS/" + deviceSn);
            //publishToClient(mqtttag, packet, "efuav/djiappC/" + userid + "/" + deviceSn);
        } else if (mqtttag.equals(MqttUtil.Tag_efuavapp)) {
            publishToClient(mqtttag, packet, "efuav/uavappS/" + deviceSn);
        } else if (mqtttag.equals(MqttUtil.Tag_Hive)) {
            publishToClient(mqtttag, packet, "efuav/HiveS/" + deviceSn);
        } else {
            //LogUtil.logInfo("(发布) MQTT " + mqtttag + " 发布内容：" + BytesUtil.printHexBinary(packet) + "失败，未找到对应的MQTT客户端!");
        }
    }

    /**
     * 推送消息
     *
     * @param mqtttag 用于寻找MQTT客户端的标记
     * @param packet  需要推送的完整数据包
     * @param topic   推送的主题
     */
    private static void publishToClient(String mqtttag, byte[] packet, String topic) {
        try {
            MqttItem mqttItem = MapMqttItem.getOrDefault(mqtttag, null);
            if (mqttItem != null) {
                mqttItem.Publish(packet, 1, topic);
            } else {
                LogUtil.logInfo("(发布) MQTT " + mqtttag + " 发布内容：" + BytesUtil.printHexBinary(packet) + "失败，未找到对应的MQTT客户端!");
            }
        } catch (Exception e) {
            LogUtil.logInfo("(发布) MQTT " + mqtttag + " 发布内容：" + BytesUtil.printHexBinary(packet) + "失败!" + e.getMessage());
        }
    }
    //*********************  修改后的 end *********************


    static final String HOST = "tcp://119.45.227.52:1883"; // 服务器地址

    // Qos 0:只发送一次，Broker可能没有接受到消息
    // Qos 1：确保发送一次成功，可能接受到重复消息
    // Qos 2：Broker肯定会收到消息，且只收到一次
    static String TopicSubscribe = "efuav/djiappS/#";// 订阅的主题 格式: efuav/djiappS/userid/uavid ,服务器订阅全部的efuav/djiappS/#
    static int TopicSubscribeQos = 0;
    static String clientId_sub = "uavserver_subscribe";// 订阅 客户端标识,在MQTT服务器显示的标识

    /**
     * 	客户端到服务器发布主题：efuav/djiappC/无人机编号
     * 	服务器到客户端发布主题：efuav/djiappS/无人机编号
     */
    //static String TopicPublish = "efuav/djiappC/userid/uavid";// 发布的主题,发送数据到客户端,获取到客户端的唯一编号后，格式： efuav/djiappC/userid/uavid
    static String TopicPublish = "efuav/djiappS/uavid";// 发布的主题,发送数据到客户端,获取到客户端的唯一编号后，格式： efuav/djiappC/userid/uavid
    static int TopicPublishQos = 1;
    static String clientId_pub = "uavserver_publish";// 发布 客户端标识,在MQTT服务器显示的标识

    // TopicSubscribe = "efuav/djiappC/user/clientId";//客户端订阅的主题
    // TopicPublish = "efuav/djiappS/user/clientId";//客户端发布的主题

    static String userName = "uavserver"; // 非必须
    static String passWord = "uavserver"; // 非必须

    static Properties prop;

    public static void StartMqtt() {
        StartMqtt_Dji();
        StartMqtt_Hive();
    }

    public static void StopMqtt() {
        DisconnMqtt();
        DisconnMqttHive();
    }

    public static void InitParm() {
        /**
         * mqtt broker 连接配置,填自己的mqtt地址,及账号密码,主题
         */
        try {
            prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("jdbc.properties"));
            String broker = prop.getProperty("brokerUrl");
            String username = prop.getProperty("ClientUsername");
            String password = prop.getProperty("ClientPassword");
            String topic = prop.getProperty("TOPIC_PREFIX");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //region DJI客户端订阅信息

    static MqttClient mqttClientPublish;
    static MqttClient mqttClientSubscribe;

    public static void StartMqtt_Dji() {
        InitParm();
        long timenow = System.currentTimeMillis();
        clientId_sub = "uavserver_subscribe_" + timenow;
        clientId_pub = "uavserver_publish_" + timenow;
        StartMqttPublish();
        StartMqttSubcribe();
        // mqttClientImpl = new MqttClientImpl();
        /*
         * while (true) { try { publish("hahahahah了消息：" + System.currentTimeMillis());
         * Thread.sleep(5678); } catch (InterruptedException e) { e.printStackTrace(); }
         * }
         */
    }

    public static void StartMqttPublish() {
        try {
            // 创建客户端
            mqttClientPublish = new MqttClient(HOST, clientId_pub, new MemoryPersistence());
            // 创建链接参数
            MqttConnectOptions connOpts = new MqttConnectOptions();
            // 在重新启动和重新连接时记住状态
            connOpts.setCleanSession(false);
            // 设置连接的用户名
            connOpts.setUserName(userName);
            connOpts.setPassword(passWord.toCharArray());
            // 建立连接
            mqttClientPublish.connect(connOpts);

            /*if (mqttClientPublish != null && !mqttClientPublish.isConnected())
                mqttClientPublish.connect(connOpts);*/
//			// 创建消息
//			MqttMessage message = new MqttMessage(content.getBytes());
//			// 设置消息的服务质量
//			message.setQos(qos);
//			// 发布消息
//			mqttClientPublish.publish(TopicPublish, message);
//			// 断开连接
//			mqttClientPublish.disconnect();
//			// 关闭客户端
//			mqttClientPublish.close();

            LogUtil.logInfo("(发布)创建MQTT连接成功，发布主题：" + TopicPublish);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            LogUtil.logInfo("(发布)创建MQTT连接失败！发布主题：" + TopicPublish);
            me.printStackTrace();
        }
    }

    public static void publish(String msg, byte[] packet, int i) {
        publish(msg, TopicPublish, "", "");
    }

    public static void publish(String msg, String userid, String uavid) {
        //publish(msg, "efuav/djiappC/" + userid + "/" + uavid, userid, uavid);
        publish(msg, "efuav/djiappS/" + uavid, userid, uavid);
    }

    public static void publish(byte[] packet, String userid, String uavid) {
        //publish(packet, "efuav/djiappC/" + userid + "/" + uavid, userid, uavid);
        publish(packet, "efuav/djiappS/" + uavid, userid, uavid);
    }

    public static void publishHive(byte[] packet, String userid, String hiveid) {
        publish(packet, "efuav/HiveS/" + hiveid, userid, hiveid);
    }

    //topic: efuav/djiappS/uavid
    public static void publish(String msg, String topic, String userid, String uavid) {
        try {
            publish(msg.getBytes("utf-8"), topic, userid, uavid);
        } catch (UnsupportedEncodingException e) {
            publish(msg.getBytes(), topic, userid, uavid);
        }
    }

    public static void publish(byte[] packet, String topic, String userid, String uavid) {
        try {
            MqttMessage message = new MqttMessage();
            message.setQos(TopicPublishQos); // 保证消息能到达一次
            message.setRetained(false); // 服务器是否保存消息
            try {
                message.setPayload(packet);
            } catch (Exception e) {
                message.setPayload(packet);
            }
            mqttClientPublish.publish(topic, message);
            LogUtil.logInfo("MQTT发布消息,主题：" + topic + ",用户:" + userid + ",无人机|机巢:" + uavid + "，内容:" + BytesUtil.printHexBinary(packet) + "成功");
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
            LogUtil.logInfo("MQTT发布消息,主题：" + topic + ",用户:" + userid + ",无人机|机巢:" + uavid + "，内容:" + BytesUtil.printHexBinary(packet) + " 异常：" + e.getMessage());
        } catch (MqttException e) {
            e.printStackTrace();
            LogUtil.logInfo("MQTT发布消息,主题：" + topic + ",用户:" + userid + ",无人机|机巢:" + uavid + "，内容:" + BytesUtil.printHexBinary(packet) + " 异常：" + e.getMessage());
        }
    }

    public static void StartMqttSubcribe() {
        try {
            // host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            mqttClientSubscribe = new MqttClient(HOST, clientId_sub, new MemoryPersistence());
            // MQTT的连接设置
            MqttConnectOptions options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            // 设置连接的用户名
            options.setUserName(userName);
            // 设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
            // 设置回调函数
            // mqttClientSubscribe.setCallback(new MqttRecieveCallback());
            mqttClientSubscribe.setCallback(new EFMqttCallback(TopicSubscribe));
            mqttClientSubscribe.connect(options);
            // 订阅消息
            mqttClientSubscribe.subscribe(TopicSubscribe, TopicSubscribeQos);

            LogUtil.logInfo("(订阅)创建MQTT连接成功，主题：" + TopicSubscribe);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logInfo("(订阅)创建MQTT连接失败！主题：" + TopicSubscribe);
        }
    }

    public static void DisconnMqtt() {
        try {
            // 断开连接
            mqttClientPublish.disconnect();
            // 关闭客户端
            mqttClientPublish.close();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        try {
            // 断开连接
            mqttClientSubscribe.disconnect();
            // 关闭客户端
            mqttClientSubscribe.close();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        LogUtil.logInfo("断开MQTT连接...");
    }
    //endregion DJI客户端订阅信息

    //region 蜂巢定义数据信息
    static MqttClient mqttHiveClientPublish;
    static MqttClient mqttHiveClientSubscribe;

    static String clientId_hive_sub = "uavserver_hive_subscribe";// 订阅 客户端标识,在MQTT服务器显示的标识
    static String clientId_hive_pub = "uavserver_hive_publish";// 发布 客户端标识,在MQTT服务器显示的标识

    public static void StartMqtt_Hive() {
        InitParm();
        long timenow = System.currentTimeMillis();
        String clientId_sub = "uavserver_subscribe_" + timenow;
        String clientId_pub = "uavserver_publish_" + timenow;
        //StartMqttHivePublish("efuav/HiveS/");
        StartMqttHivePublish("efuav/HiveS/#");  //推送到客户端
        StartMqttHiveSubcribe("efuav/HiveC/#");  //订阅客户端数据
    }

    public static void StartMqttHivePublish(String topic) {
        try {
            // 创建客户端
            mqttHiveClientPublish = new MqttClient(HOST, clientId_hive_pub, new MemoryPersistence());
            // 创建链接参数
            MqttConnectOptions connOpts = new MqttConnectOptions();
            // 在重新启动和重新连接时记住状态
            connOpts.setCleanSession(false);
            // 设置连接的用户名
            connOpts.setUserName(userName);
            connOpts.setPassword(passWord.toCharArray());
            // 建立连接
            mqttHiveClientPublish.connect(connOpts);
            LogUtil.logInfo("(发布)创建MQTT连接成功，发布主题：" + topic);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            LogUtil.logInfo("(发布)创建MQTT连接失败！发布主题：" + topic + " " + me.getMessage());
            me.printStackTrace();
        }
    }

    public static void StartMqttHiveSubcribe(String topic) {
        try {
            // host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            mqttHiveClientSubscribe = new MqttClient(HOST, clientId_hive_sub, new MemoryPersistence());
            // MQTT的连接设置
            MqttConnectOptions options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            // 设置连接的用户名
            options.setUserName(userName);
            // 设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
            // 设置回调函数
            // mqttClientSubscribe.setCallback(new MqttRecieveCallback());
            mqttHiveClientSubscribe.setCallback(new EFMqttCallback(topic));
            mqttHiveClientSubscribe.connect(options);
            // 订阅消息
            mqttHiveClientSubscribe.subscribe(topic, TopicSubscribeQos);
            LogUtil.logInfo("(订阅)创建MQTT连接成功，主题：" + topic);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logInfo("(订阅)创建MQTT连接失败！主题：" + topic + " " + e.getMessage());
        }
    }

    public static void DisconnMqttHive() {
        try {
            // 断开连接
            mqttHiveClientPublish.disconnect();
            // 关闭客户端
            mqttHiveClientPublish.close();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        try {
            // 断开连接
            mqttHiveClientSubscribe.disconnect();
            // 关闭客户端
            mqttHiveClientSubscribe.close();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        LogUtil.logInfo("断开MQTT连接...");
    }
    //endregion
}
