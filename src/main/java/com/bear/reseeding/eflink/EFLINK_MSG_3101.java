package com.bear.reseeding.eflink;



import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 航线—上传、下载的进度
 */
public class EFLINK_MSG_3101 implements Serializable {

    /**
     * 消息ID
     */
    public final int EFLINK_MSG_ID = 3101;
    /**
     * 内容段数据长度
     */
    public final int EFLINK_MSG_LENGTH = 58;

    /**
     * 刷新数据时间
     */
    private long refreshTime = System.currentTimeMillis() - 60000;

    private int Tag;
    private int Result;
    private int MsgID;
    private int Progress;
    private int ProgressCount;
    private byte[] ProgressTextBytes = new byte[50];
    private String ProgressText;

    @Override
    public String toString() {
        return "EFLINK_MSG_3101{" +
                "refreshTime=" + refreshTime +
                ", Tag=" + Tag +
                ", Result=" + Result +
                ", MsgID=" + MsgID +
                ", Progress=" + Progress +
                ", ProgressCount=" + ProgressCount +
                ", ProgressTextBytes=" + Arrays.toString(ProgressTextBytes) +
                ", ProgressText='" + ProgressText + '\'' +
                '}';
    }

    public void unpacket(byte[] packe) {
        unpacket(packe, 12);
    }

    public void unpacket(byte[] packet, int index) {
        if (packet != null && packet.length > 12) {
            refreshTime = System.currentTimeMillis();
            try {
                this.Tag = packet[index] & 0xff;
                index++;
                this.Result = packet[index] & 0xff;
                index++;
                this.MsgID = BytesUtil.bytes2UShort(packet, index);
                index += 2;
                this.Progress = BytesUtil.bytes2UShort(packet, index);
                index += 2;
                this.ProgressCount = BytesUtil.bytes2UShort(packet, index);
                index += 2;
                this.ProgressTextBytes = new byte[50];
                for (int i = 0; i < 50; i++) {
                    this.ProgressTextBytes[i] = packet[index];
                    index++;
                }
                this.ProgressText = new String(this.ProgressTextBytes, StandardCharsets.UTF_8).trim();
            } catch (Exception ignored) {
            }
        }
    }

    public byte[] packet() {
        ByteBuffer buffer = ByteBuffer.allocate(EFLINK_MSG_LENGTH);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) Tag);
        buffer.put((byte) Result);
        buffer.put(BytesUtil.short2Bytes(MsgID));
        buffer.put(BytesUtil.short2Bytes(Progress));
        buffer.put(BytesUtil.short2Bytes(ProgressCount));

        byte[] temp = ProgressText.getBytes(StandardCharsets.UTF_8);
        int tempLengh = temp.length;
        for (int i = 0; i < 50; i++) {
            if (i < tempLengh) {
                buffer.put(temp[i]);
                ProgressTextBytes[i] = temp[i];
            } else {
                buffer.put((byte) 0);
                ProgressTextBytes[i] = 0;
            }
        }
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

    public int getMsgID() {
        return MsgID;
    }

    public void setMsgID(int msgID) {
        MsgID = msgID;
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

    public byte[] getProgressTextBytes() {
        return ProgressTextBytes;
    }

    public void setProgressTextBytes(byte[] progressTextBytes) {
        ProgressTextBytes = progressTextBytes;
    }

    public String getProgressText() {
        return ProgressText;
    }

    public void setProgressText(String progressText) {
        ProgressText = progressText;
    }
}
