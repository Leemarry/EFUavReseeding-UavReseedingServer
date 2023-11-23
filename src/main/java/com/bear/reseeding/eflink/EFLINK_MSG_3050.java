package com.bear.reseeding.eflink;



import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 控制命令
 * 普通控制命令，如果是全自动命令发送后，客户端会实时回复确认信息，如果回复了成功确认，后续会通过#3052实时发送状态进展。
 */
public class EFLINK_MSG_3050 implements Serializable {

    /**
     * 消息ID
     */
    public final int EFLINK_MSG_ID = 3050;

    /**
     * 内容段数据长度
     */
    public final int EFLINK_MSG_LENGTH = 19;
    /**
     * 刷新数据时间
     */
    private long refreshTime = System.currentTimeMillis() - 60000;
    private int Tag;
    private int Command;
    private int Parm1;
    private int Parm2;
    private int Parm3;
    private int Parm4;

    public void unPacket(byte[] packet, int index) {
        if (packet != null && packet.length > 12) {
            refreshTime = System.currentTimeMillis();
            try {
                this.Tag = packet[index] & 0xff;
                index++;
                this.Command = BytesUtil.bytes2UShort(packet, index);
                index += 2;
                this.Parm1 = BytesUtil.bytes2Int(packet, index);
                index += 4;
                this.Parm2 = BytesUtil.bytes2Int(packet, index);
                index += 4;
                this.Parm3 = BytesUtil.bytes2Int(packet, index);
                index += 4;
                this.Parm4 = BytesUtil.bytes2Int(packet, index);
                index += 4;
            } catch (Exception ignored) {
            }
        }
    }

    public byte[] packet() {
        ByteBuffer buffer = ByteBuffer.allocate(EFLINK_MSG_LENGTH);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) Tag);
        buffer.put(BytesUtil.short2Bytes(Command));
        buffer.put(BytesUtil.int2Bytes(Parm1));
        buffer.put(BytesUtil.int2Bytes(Parm2));
        buffer.put(BytesUtil.int2Bytes(Parm3));
        buffer.put(BytesUtil.int2Bytes(Parm4));
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

    public int getParm1() {
        return Parm1;
    }

    public void setParm1(int parm1) {
        Parm1 = parm1;
    }

    public int getParm2() {
        return Parm2;
    }

    public void setParm2(int parm2) {
        Parm2 = parm2;
    }

    public int getParm3() {
        return Parm3;
    }

    public void setParm3(int parm3) {
        Parm3 = parm3;
    }

    public int getParm4() {
        return Parm4;
    }

    public void setParm4(int parm4) {
        Parm4 = parm4;
    }
}
