package com.bear.reseeding.datalink;

import com.alibaba.fastjson.JSONArray;
import com.bear.reseeding.utils.BytesUtil;
import com.bear.reseeding.utils.CrcUtil;
import com.bear.reseeding.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * EFLINK数据处理类
 */
public class EfLinkUtil {

    /**
     * 数据头
     */
    private static final byte HEADER = (byte) 0xFC;
    /**
     * 消息ID之前的数据长度，不包含内容段和校验
     */
    private static final int EFLINK_HEADER_LENGTH = 12;
    /**
     * 校验数组长度
     */
    private static final int EFLINK_CRC_LENGTH = 2;
    /**
     * 组件编号：服务器
     */
    private static final byte COMPENT_SERVER = (byte) 0xA1;
    /**
     * 协议版本：大疆协议
     */
    private static final byte VERSION_DJI = (byte) 0x70;

    /**
     * 协议版本：机巢
     */
    private static final byte VERSION_HIVE = (byte) 0x90;

    /**
     * 协议版本：EFUAV
     */
    private static final byte VERSION_EFUAV = (byte) 0x75;


    /**
     * 打包数据内容
     *
     * @param msgid   消息编号
     * @param payload 内容段字节数组
     * @return
     */
    public static byte[] Packet(int msgid, byte[] payload) {
        int index = 0;
        byte[] packet = new byte[EFLINK_HEADER_LENGTH + payload.length + EFLINK_CRC_LENGTH];
        packet[index] = HEADER;  //头
        index++;
        byte[] bytetemp = BytesUtil.short2Bytes(payload.length);
        for (int i = 0; i < bytetemp.length; i++) {
            packet[index] = bytetemp[i];  //长度
            index++;
        }
        byte seqid = MqttUtil.getSeqID(MqttUtil.Tag_Djiapp);
        packet[index] = seqid; //序列号
        index++;
        bytetemp = new byte[4];//TODO sysid 无人机编号或设备编号
        for (int i = 0; i < bytetemp.length; i++) {
            packet[index] = bytetemp[i]; //无人机编号或设备编号
            index++;
        }
        packet[index] = COMPENT_SERVER; //组件编号
        index++;
        packet[index] = VERSION_DJI; //协议版本 大疆
        index++;
        bytetemp = BytesUtil.short2Bytes(msgid);//msgid 2bytes
        for (int i = 0; i < bytetemp.length; i++) {
            packet[index] = bytetemp[i];  //消息ID
            index++;
        }
        index = 12;  //数据段开始填充
        for (int i = 0; i < payload.length; i++) {
            packet[index] = payload[i];
            index++;
        }
        bytetemp = CrcUtil.Crc16Packet(packet, packet.length - 2);
        for (int i = 0; i < 2; i++) {
            packet[index] = bytetemp[i]; //校验2位
            index++;
        }
        return packet;
    }

    /**
     * 翼飞无人机，打包数据内容
     *
     * @param msgid   消息编号
     * @param payload 内容段字节数组
     * @param tag     hive，dji，efuav，例：MqttUtil.Tag_Djiapp
     * @return
     */
    public static byte[] Packet_EFUAV(String tag, int msgid, byte[] payload) {
        int index = 0;
        byte[] packet = new byte[EFLINK_HEADER_LENGTH + payload.length + EFLINK_CRC_LENGTH];
        packet[index] = HEADER;  //头
        index++;
        byte[] bytetemp = BytesUtil.short2Bytes(payload.length);
        for (int i = 0; i < bytetemp.length; i++) {
            packet[index] = bytetemp[i];  //长度
            index++;
        }
        byte seqid = MqttUtil.getSeqID(tag);
        packet[index] = seqid; //序列号
        index++;
        bytetemp = new byte[4];//TODO sysid 无人机编号或设备编号
        for (int i = 0; i < bytetemp.length; i++) {
            packet[index] = bytetemp[i]; //无人机编号或设备编号
            index++;
        }
        packet[index] = COMPENT_SERVER; //组件编号
        index++;
        //协议版本
        if (tag.equals(MqttUtil.Tag_Djiapp)) {
            packet[index] = VERSION_DJI;
        } else if (tag.equals(MqttUtil.Tag_Hive)) {
            packet[index] = VERSION_HIVE;
        } else if (tag.equals(MqttUtil.Tag_efuavapp)) {
            packet[index] = VERSION_EFUAV;
        } else {
            packet[index] = 0;
        }

        index++;
        bytetemp = BytesUtil.short2Bytes(msgid);//msgid 2bytes
        for (int i = 0; i < bytetemp.length; i++) {
            packet[index] = bytetemp[i];  //消息ID
            index++;
        }
        index = 12;  //数据段开始填充
        for (int i = 0; i < payload.length; i++) {
            packet[index] = payload[i];
            index++;
        }
        bytetemp = CrcUtil.Crc16Packet(packet, packet.length - 2);
        for (int i = 0; i < 2; i++) {
            packet[index] = bytetemp[i]; //校验2位
            index++;
        }
        return packet;
    }

    //region 将json或模型封装为eflink数据包 start
    public static byte[] PacketDownloadDjiMedia(JSONArray filenames, int tag, String uavid, int cmd) {
        try {
            byte[] bytetemp;
            List<Byte> list = new ArrayList<>();
            if (filenames != null) {
                for (int no = 0; no < filenames.size(); no++) {
                    bytetemp = BytesUtil.long2Bytes(Long.parseLong(filenames.get(no).toString()));
                    for (int i = 0; i < bytetemp.length; i++) {
                        list.add(bytetemp[i]);
                    }
                }
            } else {
                bytetemp = BytesUtil.long2Bytes(Long.MAX_VALUE);
                for (int i = 0; i < bytetemp.length; i++) {
                    //list.add((byte) 0xff);
                    list.add(bytetemp[i]);
                }
            }
            //打包数据内容
            int index = 0;
            byte[] packet = new byte[14 + 4 + list.size()];  //4为List<>之前的数据个数
            packet[index] = HEADER;  //头
            index++;
            short length = (short) (4 + list.size());
            bytetemp = BytesUtil.short2Bytes(length);
            for (int i = 0; i < bytetemp.length; i++) {
                packet[index] = bytetemp[i];  //长度
                index++;
            }
            byte seqid = MqttUtil.getSeqID(MqttUtil.Tag_Djiapp);
            packet[index] = seqid; //序列号
            index++;
            bytetemp = new byte[4];//TODO sysid
            for (int i = 0; i < bytetemp.length; i++) {
                packet[index] = bytetemp[i]; //无人机编号或设备编号
                index++;
            }
            packet[index] = COMPENT_SERVER; //组件编号  A1表示服务器
            index++;
            packet[index] = VERSION_DJI; //协议版本 大疆
            index++;
            bytetemp = BytesUtil.short2Bytes(2212);//msgid 2bytes
            for (int i = 0; i < bytetemp.length; i++) {
                packet[index] = bytetemp[i];  //消息ID
                index++;
            }
            index = 12;  //数据段开始填充
            bytetemp = BytesUtil.short2Bytes(0); //设备ID，蜂巢编号
            for (int i = 0; i < bytetemp.length; i++) {
                packet[index] = bytetemp[i];
                index++;
            }
            packet[index] = (byte) cmd;//指令内容
            index++;
            packet[index] = (byte) tag; //tag
            index++;
            for (int i = 0; i < list.size(); i++) {   //具体的媒体文件名称内容
                packet[index] = list.get(i);
                index++;
            }
            bytetemp = CrcUtil.Crc16Packet(packet, packet.length - 2);
            for (int i = 0; i < 2; i++) {
                packet[index] = bytetemp[i]; //校验2位
                index++;
            }
            return packet;
        } catch (Exception e) {
            LogUtil.logError("Method->PacketDownloadDjiMedia Error:" + e.toString());
            return null;
        }
    }
    //endregion 将json或模型封装为eflink数据包 end

    /**
     * 解析数据包
     *
     * @return
     */
    public static Object UnPacket() {
        return null;
    }
}

