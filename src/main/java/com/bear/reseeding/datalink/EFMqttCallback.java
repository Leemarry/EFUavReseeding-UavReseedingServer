package com.bear.reseeding.datalink;

import com.bear.reseeding.utils.LogUtil;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: bear
 * @Date: 2021/7/16 09:51
 * @Description: null
 */
public class EFMqttCallback implements MqttCallbackExtended {
    String TopicSubscribe = "";

    public EFMqttCallback(String _TopicSubscribe) {
        TopicSubscribe = _TopicSubscribe;
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 1; i <= 10; i++) {
            final int index = i;
            try {
                Thread.sleep(index * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    String threadName = Thread.currentThread().getName();
                    System.out.println("执行：" + index + "，线程名称：" + threadName);
                }
            });
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        LogUtil.logWarn(reconnect ? "重新连接" : "初次连接 " + serverURI + " 成功");
        if (reconnect) {
//            isMqttConnected = true;
//            if (client != null) {
//                try {
//                    client.subscribe(TopicSubscribe, 1);
//                    LogUtil.logWarn("订阅 [" + TopicSubscribe + "] 成功.");
//                } catch (Exception e) {
//                    LogUtil.logWarn("订阅 [" + TopicSubscribe + "] 失败：" + e.toString());
//                }
//            }
        }
    }
}
