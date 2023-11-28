package com.bear.reseeding.model;

/**
 * @Auther: bear
 * @Date: 2021/7/16 09:49
 * @Description: null
 */

import com.bear.reseeding.datalink.HandleMqttMessage;
import com.bear.reseeding.utils.BytesUtil;
import com.bear.reseeding.utils.LogUtil;
import com.bear.reseeding.utils.RedisUtils;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 单个MQTT监听
 */
@Component
@Scope("prototype")
public class MqttItem {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    HandleMqttMessage handleMqttMessage;
    String mqtttag = "";
    MqttClient mqttAsyncClient;
    ExecutorService cachedThreadPool;

    String topic_subscribe = "efuav/#";// 订阅主题
    String topic_publish = "efuav/#";   // 发布主题

    int SeqID = 0;

    // Qos 0:只发送一次，Broker可能没有接受到消息
    // Qos 1：确保发送一次成功，可能接受到重复消息
    // Qos 2：Broker肯定会收到消息，且只收到一次
    int TopicSubscribeQos = 0;

    String HOST = "tcp://119.45.227.52:1883"; // 服务器地址
    String userName = "uavserver"; // 非必须
    String passWord = "uavserverpwd"; // 非必须
    String clientId_hive_sub = "uavserver_subscribe";// 订阅 客户端标识,在MQTT服务器显示的标识
    String clientId_hive_pub = "uavserver_publish";// 发布 客户端标识,在MQTT服务器显示的标识

    /**
     * 用于发送的序列号，递增
     *
     * @return
     */
    public byte getSeqID() {
        SeqID += 1;
        if (SeqID > 255) {
            SeqID = 0;
        }
        return (byte) SeqID;
    }

    public MqttItem() {
    }

    public boolean isConnect() {
        if (mqttAsyncClient == null) {
            return false;
        }
        return mqttAsyncClient.isConnected();
    }

    public void init(String host, String username, String passWord, String subscribe, String publish, String mqtttag) {
        this.mqtttag = mqtttag;
        this.HOST = host;
        this.userName = username;
        this.passWord = passWord;
        this.topic_subscribe = subscribe;
        this.topic_publish = publish;
        long timenow = System.currentTimeMillis();
        this.clientId_hive_sub = "uavserver_" + mqtttag + "_" + timenow;
        this.clientId_hive_pub = "uavserver_" + publish + "_" + timenow;
    }

    public void reStartMqtt() {
        LogUtil.logMessage("[" + mqtttag + "]" + "准备重新建立Mqtt连接...");
        DisconnMqtt();
        StartMqtt();
    }

    public void StartMqtt() {
        String topic = this.topic_subscribe;
        try {
            long timenow = System.currentTimeMillis();
            this.clientId_hive_sub = "reseeding_" + mqtttag + "_" + timenow;
            // host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            mqttAsyncClient = new MqttClient(HOST, clientId_hive_sub, new MemoryPersistence());
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
            // 自动重连
            options.setAutomaticReconnect(true);
            // 设置回调函数
            // mqttClientSubscribe.setCallback(new MqttRecieveCallback());

            // 这样当断开连接后，MqttClient内部会自动进行重连，每次连接成功后都会回调connectComplete()方法。
            mqttAsyncClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    LogUtil.logMessage("[" + mqtttag + "]" + (reconnect ? "重新连接" : "初次连接") + serverURI + " 成功。");
                    if (reconnect) {
                        if (mqttAsyncClient != null) {
                            try {
                                if (topic == null) {
                                    LogUtil.logMessage("[" + mqtttag + "]" + "无需订阅主题.");
                                } else {
                                    mqttAsyncClient.unsubscribe(topic);
                                    mqttAsyncClient.subscribe(topic, TopicSubscribeQos);
                                    LogUtil.logMessage("[" + mqtttag + "]" + "订阅主题 [" + topic + "] 成功.");
                                }
                            } catch (Exception e) {
                                LogUtil.logError("[" + mqtttag + "]" + "订阅主题 [" + topic + "] 失败：" + e.toString());
                            }
                        }
                    }
                }

                @Override
                public void connectionLost(Throwable throwable) {
                    LogUtil.logError("MQTT连接丢失： " + (throwable != null ? throwable.toString() : "") + " ...");
                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) {
                    if (cachedThreadPool == null) {
                        cachedThreadPool = Executors.newCachedThreadPool();
                    }
                    cachedThreadPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            //String threadName = Thread.currentThread().getName();
                            handleMqttMessage.unPacket(redisUtils, topic, mqttMessage);
                        }
                    });
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    //LogUtils.WriteLog("MQTT服务：推送数据完毕 " + (arg0 != null ? arg0.toString() : ""));
                }
            });
            mqttAsyncClient.connect(options);
            // 订阅消息
            mqttAsyncClient.subscribe(topic, TopicSubscribeQos);
            LogUtil.logInfo("(订阅)创建MQTT " + mqtttag + " 连接成功，订阅主题：" + topic);
        } catch (Exception e) {
            LogUtil.logError("(订阅)创建MQTT " + mqtttag + " 连接失败！订阅主题：" + topic + " " + e.toString());
        }
    }


    /**
     * 发布消息
     *
     * @param packet 完整数据包
     * @param qos    发送数据包质量,通常发送比较重要的设置为1
     *               Qos 0:只发送一次，Broker可能没有接受到消息
     *               Qos 1：确保发送一次成功，可能接受到重复消息
     *               Qos 2：Broker肯定会收到消息，且只收到一次
     * @return
     */
    public boolean Publish(byte[] packet, int qos) {
        return Publish(packet, qos, topic_publish);
    }

    /**
     * 发布消息
     *
     * @param packet 完整数据包
     * @param qos    发送数据包质量,通常发送比较重要的设置为1
     *               Qos 0:只发送一次，Broker可能没有接受到消息
     *               Qos 1：确保发送一次成功，可能接受到重复消息
     *               Qos 2：Broker肯定会收到消息，且只收到一次
     * @param topic  自定义主题
     * @return
     */
    public boolean Publish(byte[] packet, int qos, String topic) {
        try {
            if (mqttAsyncClient == null || !mqttAsyncClient.isConnected()) {
                LogUtil.logInfo("(发布) MQTT " + mqtttag + ",主题：" + topic + ",发布内容：" + BytesUtil.printHexBinary(packet) + "失败，未建立连接!");
            } else {
                MqttMessage message = new MqttMessage();
                message.setQos(qos); // 保证消息能到达一次
                message.setRetained(false); // 服务器是否保存消息
                try {
                    message.setPayload(packet);
                } catch (Exception e) {
                    message.setPayload(packet);
                }
                mqttAsyncClient.publish(topic, message);
                LogUtil.logInfo("(发布) MQTT " + mqtttag + ",主题：" + topic + ",发布内容：" + BytesUtil.printHexBinary(packet) + "成功.");
                return true;
            }
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
            LogUtil.logInfo("(发布) MQTT " + mqtttag + ",主题：" + topic + ",发布内容：" + BytesUtil.printHexBinary(packet) + " 异常：" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logInfo("(发布) MQTT " + mqtttag + ",主题：" + topic + ",发布内容：" + BytesUtil.printHexBinary(packet) + " 异常：" + e.getMessage());
        }
        return false;
    }

    public void DisconnMqtt() {
        try {
            if (mqttAsyncClient != null) {
                // 断开连接
                mqttAsyncClient.disconnect();
                // 关闭客户端
                mqttAsyncClient.close();
            }
        } catch (MqttException e) {
        }
        LogUtil.logError(" MQTT " + this.mqtttag + " 断开连接！！！");
    }
}
