package com.bear.reseeding.eflink;

import com.alibaba.fastjson.JSONObject;
import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * UPS操作相关数据包
 */
public class EFLINK_MSG_3800 implements Serializable {
    public final int EFLINK_MSG_ID = 3800;

    /**
     * 内容段数据长度
     */
    public final int EFLINK_MSG_LENGTH = 7;

    int VersionInside;
    int Tag;
    int Command;
    int P1;
    int P2;

    public EFLINK_MSG_3800() {
    }

    public EFLINK_MSG_3800(int versionInside, int tag, int command, int p1, int p2) {
        VersionInside = versionInside;
        Tag = tag;
        Command = command;
        P1 = p1;
        P2 = p2;
    }

    public EFLINK_MSG_3800(byte[] packet) {
        InitPacket(packet, 0);
    }

    public EFLINK_MSG_3800(byte[] packet, int index) {
        InitPacket(packet, index);
    }

    void InitPacket(byte[] packet, int index) {
        VersionInside = packet[index] & 0xFF;  //当前载荷段数据版本，协议升级后可根据版本号区分载荷段内容。
        index++;
        Tag = packet[index] & 0xFF; //标记
        index += 1;
        Command = packet[index] & 0xFF;  //命令
        index++;
        P1 = BytesUtil.bytes2UShort(packet, index);//P1
        index += 2;
        P2 = BytesUtil.bytes2UShort(packet, index);//P2
        index += 2;
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

    public int getVersionInside() {
        return VersionInside;
    }

    public void setVersionInside(int versionInside) {
        VersionInside = versionInside;
    }

    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(EFLINK_MSG_LENGTH);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) (VersionInside));
        buffer.put((byte) (Tag));
        buffer.put((byte) (Command));
        buffer.put(BytesUtil.short2Bytes(P1));
        buffer.put(BytesUtil.short2Bytes(P2));
        return buffer.array();
    }

    public String toJsonArray() {
        return JSONObject.toJSONString(this);
    }
}
