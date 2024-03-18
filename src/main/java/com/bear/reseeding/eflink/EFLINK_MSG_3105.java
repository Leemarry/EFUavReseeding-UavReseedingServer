package com.bear.reseeding.eflink;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 航线—上传ACK (#3105)
 */
public class EFLINK_MSG_3105 implements Serializable {

    /**
     * 消息ID
     */
    public final int EFLINK_MSG_ID = 3105;
    /**
     * 内容段数据长度
     */
    private final int EFLINK_MSG_LENGTH = 2;

    /**
     * 刷新数据时间
     */
    private long refreshTime = System.currentTimeMillis() - 60000;

    /**
     * tag byte
     */
    private int Tag;
    /**
     * 1：成功，0：失败  byte
     */
    private int Result;

    public EFLINK_MSG_3105() {
    }

    public EFLINK_MSG_3105(int tag, int result) {
        Tag = tag;
        Result = result;
    }

    public void unPacket(byte[] packet) {
        unPacket(packet, 12);
    }

    public void unPacket(byte[] packet, int index) {
        if (packet != null && packet.length > 12) {
            refreshTime = System.currentTimeMillis();
            try {
                this.Tag = packet[index] & 0xff;
                index++;
                this.Result = packet[index] & 0xff;
                index++;
            } catch (Exception ignored) {
            }
        }
    }

    public byte[] packet() {
        ByteBuffer buffer = ByteBuffer.allocate(EFLINK_MSG_LENGTH);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) Tag);
        buffer.put((byte) Result);
        return buffer.array();
    }


    public int getTag() {
        return Tag;
    }

    public void setTag(int tag) {
        Tag = tag;
    }

    public int getResult() {
        return Result;
    }

    public void setResult(int result) {
        Result = result;
    }


}
