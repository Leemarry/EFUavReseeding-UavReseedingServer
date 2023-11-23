package com.bear.reseeding.eflink;

import com.alibaba.fastjson.JSONObject;
import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 控制指令，带整型参数
 */
public class EFLINK_MSG_2302 implements Serializable {
    public final int EFLINK_MSG_ID = 2302;

    /**
     * 内容段数据长度
     */
    public final int EFLINK_MSG_LENGTH = 21;

    int DeviceId;
    int Command;
    int Tag;
    int P1;
    int P2;
    int P3;
    int P4;

    public EFLINK_MSG_2302() {
    }

    public EFLINK_MSG_2302(int deviceId, int command, int tag, int P1, int P2, int P3, int P4) {
        this.DeviceId = deviceId;
        this.Command = command;
        this.Tag = tag;
        this.P1 = P1;
        this.P2 = P2;
        this.P3 = P3;
        this.P4 = P4;
    }

    public EFLINK_MSG_2302(byte[] packet) {
        InitPacket(packet, 0);
    }

    public EFLINK_MSG_2302(byte[] packet, int index) {
        InitPacket(packet, index);
    }

    void InitPacket(byte[] packet, int index) {
        DeviceId = BytesUtil.bytes2UShort(packet, index);//设备编号
        index += 2;
        Command = packet[index] & 0xFF;  //命令
        index++;
        Tag = BytesUtil.bytes2UShort(packet, index);//标记
        index += 2;
        P1 = BytesUtil.bytes2Int(packet, index);//P1
        index += 4;
        P2 = BytesUtil.bytes2Int(packet, index);//P2
        index += 4;
        P3 = BytesUtil.bytes2Int(packet, index);//P3
        index += 4;
        P4 = BytesUtil.bytes2Int(packet, index);//P4
    }


    public int getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(int deviceId) {
        DeviceId = deviceId;
    }

    public int getCommand() {
        return Command;
    }

    public void setCommand(int command) {
        Command = command;
    }

    public int getTag() {
        return Tag;
    }

    public void setTag(int tag) {
        Tag = tag;
    }

    public int getP1() {
        return P1;
    }

    public void setP1(int p1) {
        P1 = p1;
    }

    public int getP2() {
        return P2;
    }

    public void setP2(int p2) {
        P2 = p2;
    }

    public int getP3() {
        return P3;
    }

    public void setP3(int p3) {
        P3 = p3;
    }

    public int getP4() {
        return P4;
    }

    public void setP4(int p4) {
        P4 = p4;
    }

    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(EFLINK_MSG_LENGTH);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(BytesUtil.short2Bytes(DeviceId));
        buffer.put((byte) (Command));
        buffer.put(BytesUtil.short2Bytes(Tag));
        buffer.put(BytesUtil.int2Bytes(P1));
        buffer.put(BytesUtil.int2Bytes(P2));
        buffer.put(BytesUtil.int2Bytes(P3));
        buffer.put(BytesUtil.int2Bytes(P4));
        return buffer.array();
    }

    public String toJsonArray() {
        return JSONObject.toJSONString(this);
    }
}
