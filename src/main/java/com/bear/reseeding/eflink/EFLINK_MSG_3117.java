package com.bear.reseeding.eflink;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 航线—请求下载 (#3117)
 */
public class EFLINK_MSG_3117 implements Serializable {

    /**
     * 消息ID
     */
    public final int EFLINK_MSG_ID = 3117;
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
     * 保留
     */
    private int Back;

    public EFLINK_MSG_3117() {
    }

    public EFLINK_MSG_3117(int tag, int back) {
        Tag = tag;
        Back = back;
    }

    public void unpacket(byte[] packet) {
        unpacket(packet, 12);
    }

    public void unpacket(byte[] packet, int index) {
        if (packet != null && packet.length > 12) {
            refreshTime = System.currentTimeMillis();
            try {
                this.Tag = packet[index] & 0xff;
                index++;
                this.Back = packet[index] & 0xff;
                index++;

            } catch (Exception ignored) {
            }
        }
    }

    public byte[] packet() {
        ByteBuffer buffer = ByteBuffer.allocate(EFLINK_MSG_LENGTH);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) Tag);
        buffer.put((byte) Back);
        return buffer.array();
    }


    public int getTag() {
        return Tag;
    }

    public void setTag(int tag) {
        Tag = tag;
    }

    public int getBack() {
        return Back;
    }

    public void setBack(int back) {
        Back = back;
    }


}
