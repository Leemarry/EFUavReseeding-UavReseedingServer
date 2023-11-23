package com.bear.reseeding.eflink;

import com.alibaba.fastjson.JSONObject;
import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * UPS操作相关数据包，回复ACK
 */
public class EFLINK_MSG_3801 implements Serializable {
    public final int EFLINK_MSG_ID = 3801;

    /**
     * 内容段数据长度
     */
    public final int EFLINK_MSG_LENGTH = 4;

    int VersionInside;
    int Tag;
    int Command;
    int Ack;


    public EFLINK_MSG_3801() {
    }


    public EFLINK_MSG_3801(int versionInside, int tag, int command, int ack) {
        VersionInside = versionInside;
        Tag = tag;
        Command = command;
        Ack = ack;
    }

    public EFLINK_MSG_3801(byte[] packet) {
        InitPacket(packet, 0);
    }

    public EFLINK_MSG_3801(byte[] packet, int index) {
        InitPacket(packet, index);
    }

    public void InitPacket(byte[] packet, int index) {
        VersionInside = packet[index] & 0xFF;  //当前载荷段数据版本，协议升级后可根据版本号区分载荷段内容。
        index++;
        Tag = packet[index] & 0xFF; //标记
        index += 1;
        Command = packet[index] & 0xFF;  //命令
        index++;
        Ack = packet[index];  //ack
        index++;
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

    public int getAck() {
        return Ack;
    }

    public void setAck(int ack) {
        Ack = ack;
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
        buffer.put((byte) (Ack));
        return buffer.array();
    }

    public String toJsonArray() {
        return JSONObject.toJSONString(this);
    }
}
