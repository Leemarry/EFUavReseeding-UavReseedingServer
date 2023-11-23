package com.bear.reseeding.eflink;


import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 停机坪控制命令ACK
 */
public class EFLINK_MSG_2301 implements Serializable {

    /**
     * 消息ID
     */
    public final int EFLINK_MSG_ID = 2301;

    /**
     * 内容段数据长度
     */
    public final int EFLINK_MSG_LENGTH = 6;
    /**
     * 刷新数据时间
     */
    private long refreshTime = System.currentTimeMillis() - 60000;
    private int DeviceID;
    private int Command;
    private int Tag;
    private int Result;


    public void unPacket(byte[] packet, int index) {
        if (packet != null && packet.length > 4) {
            refreshTime = System.currentTimeMillis();
            try {
                this.DeviceID = BytesUtil.bytes2UShort(packet, index);
                index += 2;
                this.Command = packet[index] & 0xff;
                index++;
                this.Tag = BytesUtil.bytes2UShort(packet, index);
                index += 2;
                this.Result = packet[index] & 0xff;
                index += 1;
            } catch (Exception ignored) {
            }
        }
    }

    public byte[] packet() {
        ByteBuffer buffer = ByteBuffer.allocate(EFLINK_MSG_LENGTH);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(BytesUtil.short2Bytes(DeviceID));
        buffer.put((byte) Command);
        buffer.put(BytesUtil.short2Bytes(Tag));
        buffer.put((byte) Result);
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


    public int getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(int deviceID) {
        DeviceID = deviceID;
    }

    public int getResult() {
        return Result;
    }

    public void setResult(int result) {
        Result = result;
    }
}
