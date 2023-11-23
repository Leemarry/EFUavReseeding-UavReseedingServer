package com.bear.reseeding.eflink;


import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 请求上传航线 kmz
 */
public class EFLINK_MSG_3121 implements Serializable {

    /**
     * 消息ID
     */
    public final int EFLINK_MSG_ID = 3121;
    /**
     * 内容段数据长度 ，变长 11 + n
     */
    public final int EFLINK_MSG_LENGTH = 11;

    /**
     * 刷新数据时间
     */
    private long refreshTime = System.currentTimeMillis() - 60000;

    private int tag;

    private long size;

    private int urlLength;

    private String url;

    public void unPacket(byte[] packe) {
        unPacket(packe, 12);
    }

    public void unPacket(byte[] packet, int index) {
        if (packet != null && packet.length > 12) {
            refreshTime = System.currentTimeMillis();
            try {
                this.tag = packet[index] & 0xff;
                index++;
                this.size = BytesUtil.bytes2Long(packet, index);
                index += 8;
                this.urlLength = BytesUtil.bytes2UShort(packet, index);
                index += 2;
                byte[] path = new byte[this.urlLength];
                for (int i = 0; i < this.urlLength; i++) {
                    path[i] = packet[index];
                    index++;
                }
                this.url = new String(path);
            } catch (Exception ignored) {
            }
        }
    }

    public byte[] packet() {
        this.urlLength = 0;
        byte[] pathArray = new byte[0];
        if (url != null) {
            pathArray = url.getBytes();
            this.urlLength = pathArray.length;
        }
        ByteBuffer buffer = ByteBuffer.allocate(EFLINK_MSG_LENGTH + this.urlLength);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) tag);
        buffer.put(BytesUtil.long2Bytes(size));
        buffer.put(BytesUtil.short2Bytes(this.urlLength));
        for (int i = 0; i < this.urlLength; i++) {
            buffer.put(pathArray[i]);
        }
        return buffer.array();
    }


    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getUrlLength() {
        return urlLength;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
