package com.bear.reseeding.eflink;


import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * KMZ航线—回复 (#3123)
 */
public class EFLINK_MSG_3123 implements Serializable {

    /**
     * 消息ID
     */
    public final int EFLINK_MSG_ID = 3123;
    /**
     * 内容段数据长度
     */
    private final int EFLINK_MSG_LENGTH = 3;

    /**
     * 刷新数据时间
     */
    private long refreshTime = System.currentTimeMillis() - 60000;


    private int tag;
    private int progress;
    private int result;

    public EFLINK_MSG_3123() {
    }

    public void unpacket(byte[] packet) {
        unpacket(packet, 12);
    }

    public void unpacket(byte[] packet, int index) {
        if (packet != null && packet.length > 12) {
            refreshTime = System.currentTimeMillis();
            try {
                this.tag = packet[index] & 0xff;
                index++;
                this.progress = packet[index] & 0xff;
                index++;
                this.result = packet[index] & 0xff;
                index++;
            } catch (Exception ignored) {
            }
        }
    }

    public byte[] packet() {
        ByteBuffer buffer = ByteBuffer.allocate(EFLINK_MSG_LENGTH);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) tag);
        buffer.put((byte) progress);
        buffer.put((byte) result);
        return buffer.array();
    }


    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
