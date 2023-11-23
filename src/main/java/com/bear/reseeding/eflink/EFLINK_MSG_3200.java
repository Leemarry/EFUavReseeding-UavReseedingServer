package com.bear.reseeding.eflink;

import com.alibaba.fastjson.JSONObject;
import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * 媒体下载进度(#3200)
 */
public class EFLINK_MSG_3200 implements Serializable {
    public final int EFLINK_MSG_ID = 3200;

    /**
     * 内容段数据长度
     */
    public final int EFLINK_MSG_LENGTH = 106;

    int VersionInside;
    int Tag;
    int Progress;
    int ProgressCount;
    byte[] Status;
    String StatusMessage = "";


    public EFLINK_MSG_3200() {
    }

    public EFLINK_MSG_3200(int versionInside, int tag, int progress, int progressCount, byte[] status) {
        VersionInside = versionInside;
        Tag = tag;
        Progress = progress;
        ProgressCount = progressCount;
        Status = status;
    }

    public EFLINK_MSG_3200(byte[] packet) {
        InitPacket(packet, 0);
    }

    public EFLINK_MSG_3200(byte[] packet, int index) {
        InitPacket(packet, index);
    }

    public void InitPacket(byte[] packet, int index) {
        VersionInside = packet[index] & 0xFF;  //当前载荷段数据版本，协议升级后可根据版本号区分载荷段内容。
        index++;
        Tag = packet[index] & 0xFF; //标记
        index++;
        Progress = BytesUtil.bytes2UShort(packet, index);
        index += 2;
        ProgressCount = BytesUtil.bytes2UShort(packet, index);
        index += 2;
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
        buffer.put(BytesUtil.short2Bytes(Progress));
        buffer.put(BytesUtil.short2Bytes(ProgressCount));

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

    public int getProgress() {
        return Progress;
    }

    public void setProgress(int progress) {
        Progress = progress;
    }

    public int getProgressCount() {
        return ProgressCount;
    }

    public void setProgressCount(int progressCount) {
        ProgressCount = progressCount;
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
