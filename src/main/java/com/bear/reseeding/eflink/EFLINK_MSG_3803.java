package com.bear.reseeding.eflink;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * UPS电源—密码数据包
 */
public class EFLINK_MSG_3803 implements Serializable {
    public final int EFLINK_MSG_ID = 3803;

    /**
     * 内容段数据长度
     */
    public final int EFLINK_MSG_LENGTH = 52;

    int VersionInside;
    int Tag;
    byte[] UpsPasswordBytes;
    String UpsPassword;

    public EFLINK_MSG_3803() {
    }

    public EFLINK_MSG_3803(int versionInside, int tag, byte[] upsPasswordBytes, String upsPassword) {
        VersionInside = versionInside;
        Tag = tag;
        UpsPasswordBytes = upsPasswordBytes;
        UpsPassword = upsPassword;
    }

    public EFLINK_MSG_3803(byte[] packet) {
        InitPacket(packet, 0);
    }

    public EFLINK_MSG_3803(byte[] packet, int index) {
        InitPacket(packet, index);
    }

    public void InitPacket(byte[] packet, int index) {
        VersionInside = packet[index] & 0xFF;  //当前载荷段数据版本，协议升级后可根据版本号区分载荷段内容。
        index++;
        Tag = packet[index] & 0xFF; //标记
        index++;
        UpsPasswordBytes = new byte[50];
        for (int i = 0; i < 50; i++) {
            UpsPasswordBytes[i] = packet[index];
            index++;
        }
        UpsPassword = new String(this.UpsPasswordBytes, StandardCharsets.UTF_8).trim();
    }


    public int getTag() {
        return Tag;
    }

    public void setTag(int tag) {
        Tag = tag;
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
        byte[] temp = UpsPassword.getBytes(StandardCharsets.UTF_8);
        int tempLengh = temp.length;
        for (int i = 0; i < 50; i++) {
            if (i < tempLengh) {
                buffer.put(temp[i]);
                UpsPasswordBytes[i] = temp[i];
            } else {
                buffer.put((byte) 0);
                UpsPasswordBytes[i] = 0;
            }
        }
        buffer.put(UpsPasswordBytes);
        return buffer.array();
    }

    public byte[] getUpsPasswordBytes() {
        return UpsPasswordBytes;
    }

    public void setUpsPasswordBytes(byte[] upsPasswordBytes) {
        UpsPasswordBytes = upsPasswordBytes;
    }

    public String getUpsPassword() {
        return UpsPassword;
    }

    public void setUpsPassword(String upsPassword) {
        UpsPassword = upsPassword;
    }

    public String toJsonArray() {
        return JSONObject.toJSONString(this);
    }
}
