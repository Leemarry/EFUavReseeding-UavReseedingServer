package com.bear.reseeding.eflink;

import com.alibaba.fastjson.JSONObject;
import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * 媒体下载回复确认(#3207)
 */
public class EFLINK_MSG_3207 implements Serializable {
    public final int EFLINK_MSG_ID = 3207;

    /**
     * 内容段数据长度
     */
    public final int EFLINK_MSG_LENGTH = 103;

    int VersionInside;
    int Tag;
    int Ack;
    byte[] Status;
    String StatusMessage = "";


    public EFLINK_MSG_3207() {
    }

    public EFLINK_MSG_3207(int versionInside, int tag, int ack, byte[] status) {
        VersionInside = versionInside;
        Tag = tag;
        Ack = ack;
        Status = status;
    }

    public EFLINK_MSG_3207(byte[] packet) {
        InitPacket(packet, 0);
    }

    public EFLINK_MSG_3207(byte[] packet, int index) {
        InitPacket(packet, index);
    }

    public void InitPacket(byte[] packet, int index) {
        VersionInside = packet[index] & 0xFF;  //当前载荷段数据版本，协议升级后可根据版本号区分载荷段内容。
        index++;
        Tag = packet[index] & 0xFF; //标记
        index++;
        Ack = packet[index] & 0xFF;
        index++;
        Status = new byte[100];
        for (int i = 0; i < 100; i++) {
            Status[i] = packet[index];
            index++;
        }
        this.StatusMessage = new String(this.Status, StandardCharsets.UTF_8).trim();
    }


    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(EFLINK_MSG_LENGTH);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) (VersionInside));
        buffer.put((byte) (Tag));
        buffer.put((byte) (Ack));

        Status = new byte[100];
        if (StatusMessage != null) {
            byte[] temp = StatusMessage.getBytes(StandardCharsets.UTF_8);
            int tempLengh = temp.length;
            for (int i = 0; i < 100; i++) {
                if (i < tempLengh) {
                    buffer.put(temp[i]);
                    Status[i] = temp[i];
                }
            }
        }
        return buffer.array();
    }

    public void InitPacket(byte[] packet) {
        InitPacket(packet, 12);
    }

    public String toJsonArray() {
        return JSONObject.toJSONString(this);
    }

    public int getVersionInside() {
        return VersionInside;
    }

    public void setVersionInside(int versionInside) {
        VersionInside = versionInside;
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

    public byte[] getStatus() {
        return Status;
    }

    public void setStatus(byte[] status) {
        Status = status;
    }

    public String getStatusMessage() {
        return StatusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        StatusMessage = statusMessage;
    }
}
