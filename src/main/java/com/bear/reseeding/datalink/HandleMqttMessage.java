package com.bear.reseeding.datalink;

import com.bear.reseeding.utils.RedisUtils;
import com.bear.reseeding.utils.BytesUtil;
import com.bear.reseeding.utils.CrcUtil;
import com.bear.reseeding.utils.LogUtil;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 处理接收到的MQTT消息
 *
 * @Auther: bear
 * @Date: 2021/7/16 10:01
 * @Description: null
 */
@Component
public class HandleMqttMessage {

    @Resource
    HandleMqttMessageDji handleMqttMessageDji;

    @Resource
    HandleMqttMessageEf handleMqttMessageEf;

    /**
     * 解析Mqtt数据包
     *
     * @param topic
     * @param message
     */
    public void unPacket(RedisUtils redisUtils, String topic, MqttMessage message) {
        String transferResult = null;
        try {
            byte[] packet = message.getPayload();
            transferResult = BytesUtil.printHexBinary(packet);// new String(packet, "utf-8");
            String checkResult = "√";
            String msgid = "";
            if (packet.length < 12) {
                checkResult = "ErrorLengthShort";
            } else if (packet[0] != (byte) 0xFC) {
                checkResult = "ErrorHeader";
            } else {
                if (CrcUtil.CheckCrc16Packet(packet)) {
                    // LogUtil.logInfo("MQTT：订阅[" + topic + "]数据：" + BytesUtil.printHexBinary(packet));
                    if (redisUtils == null) {
                        LogUtil.logError("MQTT：处理数据失败，Redis未开启！！！");
                    }
                    int Index = 1;
                    int loadLength = BytesUtil.bytes2UShort(packet, Index);
                    Index += 2;
                    if (loadLength + 14 != packet.length) {
                        checkResult = "ErrorLength";
                        return;
                    }
                    if (topic.startsWith("efuav/uavappC/")) {
                        /* * 翼飞无人机心跳数据
                         * 客户端到服务器发布主题：efuav/uavappC/无人机编号
                         * 服务器到客户端发布主题：efuav/uavappS/无人机编号
                         * */
                        String user = "";
                        String uavserid = "";
                        String[] strings = topic.split("/");
                        if (strings.length >= 3) {
                            //user = strings[2];
                            uavserid = strings[2];
                        }
                        if (packet.length >= 14) {
                            msgid = String.valueOf(BytesUtil.bytes2UShort(packet, 10));
                        }
                        handleMqttMessageEf.unPacket(redisUtils, topic, uavserid, packet);
                    } else if (topic.startsWith("efuav/djiappC/")) {
                        //Android dji 客户端推送
                        /**
                         *  客户端到服务器发布主题：efuav/djiappC/无人机编号
                         *  服务器到客户端发布主题：efuav/djiappS/无人机编号
                         */
                        String uavid = "";
                        String userid = "unknown";
                        String[] strings = topic.split("/");
                        if (strings.length >= 3) {
                            //userid = strings[2];
                            uavid = strings[2];
                        }
                        if (packet.length >= 14) {
                            msgid = String.valueOf(BytesUtil.bytes2UShort(packet, 10));
                        }
                        handleMqttMessageDji.unPacket(redisUtils, topic, uavid, packet);
//                        DoPacket(packet, userid, uavid);   //解析数据保存数据库
//                        DataLogUtil.log(uavid, packet);  //保存日志
                    }
                } else {
                    //System.out.println("校验不通过："+BytesUtil.bytes2Short(packet));
                    //DoPacketHive(packet, "2000"); bytes2Short
                    checkResult = "ERROR";
                    LogUtil.logError("MQTT：校验失败！！！订阅[" + topic + "]数据：" + BytesUtil.printHexBinary(packet));
//                    System.out.println(TAG + "Rec(crc:" + checkResult + ",msgid:" + msgid + ")：" + topic + " Qos:" + message.getQos() + " Payload : " + transferResult);
                }
            }
        } catch (Exception e) {
            LogUtil.logError("MQTT：解析MQTT数据内容异常: " + e.toString());
        }
    }
}
