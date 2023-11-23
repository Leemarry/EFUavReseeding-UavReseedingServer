package com.bear.reseeding.eflink;



import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 控制命令ACK
 */
public class EFLINK_MSG_3051 implements Serializable {

    /**
     * 消息ID
     */
    public final int EFLINK_MSG_ID = 3051;

    /**
     * 内容段数据长度
     */
    public final int EFLINK_MSG_LENGTH = 5;
    /**
     * 刷新数据时间
     */
    private long refreshTime = System.currentTimeMillis() - 60000;
    private int Tag;
    private int Command;
    private int Ack;

    public void unPacket(byte[] packet, int index) {
        if (packet != null && packet.length > 4) {
            refreshTime = System.currentTimeMillis();
            try {
                this.Tag = packet[index] & 0xff;
                index++;
                this.Command = BytesUtil.bytes2UShort(packet, index);
                index += 2;
                this.Ack = BytesUtil.bytes2Short(packet, index);
                index += 2;
            } catch (Exception ignored) {
            }
        }
    }

    public byte[] packet() {
        ByteBuffer buffer = ByteBuffer.allocate(EFLINK_MSG_LENGTH);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) Tag);
        buffer.put(BytesUtil.short2Bytes(Command));
        buffer.put(BytesUtil.short2Bytes(Ack));
        return buffer.array();
    }

    public int getTag() {
        return Tag;
    }

    public void setTag(int tag) {
        Tag = tag;
    }

    public int getCommand() {
        return Command;
    }

    public void setCommand(int command) {
        Command = command;
    }

    public int getAck() {
        return Ack;
    }

    public void setAck(int ack) {
        Ack = ack;
    }
}
