package com.bear.reseeding.eflink;


import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 全自动命令后续回复 (#3052)
 */
public class EFLINK_MSG_3052 implements Serializable {

    /**
     * 消息ID
     */
    public final int EFLINK_MSG_ID = 3052;

    /**
     * 内容段数据长度
     */
    public final int EFLINK_MSG_LENGTH = 9;
    /**
     * 刷新数据时间
     */
    private long refreshTime = System.currentTimeMillis() - 60000;
    private int Tag;
    private int Command;
    private int ProgressText;
    private int Progress;
    private int Status;
    String ProgressTextDes;
    String ErrorText;

    @Override
    public String toString() {
        return "EFLINK_MSG_3052{" +
                "EFLINK_MSG_ID=" + EFLINK_MSG_ID +
                ", EFLINK_MSG_LENGTH=" + EFLINK_MSG_LENGTH +
                ", refreshTime=" + refreshTime +
                ", Tag=" + Tag +
                ", Command=" + Command +
                ", ProgressText=" + ProgressText +
                ", Progress=" + Progress +
                ", Status=" + Status +
                ", ProgressTextDes='" + ProgressTextDes + '\'' +
                ", ErrorText='" + ErrorText + '\'' +
                '}';
    }

    public void unPacket(byte[] packet, int index) {
        if (packet != null && packet.length > 4) {
            refreshTime = System.currentTimeMillis();
            try {
                this.Tag = packet[index] & 0xff;
                index++;
                this.Command = BytesUtil.bytes2UShort(packet, index);
                index += 2;
                this.ProgressText = BytesUtil.bytes2UShort(packet, index);
                index += 2;
                this.Progress = BytesUtil.bytes2UShort(packet, index);
                index += 2;
                this.Status = BytesUtil.bytes2Short(packet, index);
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
        buffer.put(BytesUtil.short2Bytes(ProgressText));
        buffer.put(BytesUtil.short2Bytes(Progress));
        buffer.put(BytesUtil.short2Bytes(Status));
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


    public int getProgressText() {
        return ProgressText;
    }

    public void setProgressText(int progressText) {
        ProgressText = progressText;
    }

    public int getProgress() {
        return Progress;
    }

    public void setProgress(int progress) {
        Progress = progress;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getProgressTextDes() {
        return ProgressTextDes;
    }

    public void setProgressTextDes(String progressTextDes) {
        ProgressTextDes = progressTextDes;
    }

    public String getErrorText() {
        return ErrorText;
    }

    public void setErrorText(String errorText) {
        ErrorText = errorText;
    }

}
