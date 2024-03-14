package com.bear.reseeding.eflink;

/**
 * 草原补种项目——确认上传
 *
 * @author N.
 * @date 2024-03-14 13:53
 */
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class EFLINK_MSG_19011 {
    public final int EFLINK_MSG_ID = 19011;
    /**
     * 内容段数据长度
     */
    public final int EFLINK_MSG_LENGTH = 6;
    /**
     * 版本
     */
    byte VersionInside;
    /**
     * 标识
     */
    byte Tag;
    /**
     * 处理编号
     */
    int HandleId;

    public EFLINK_MSG_19011() {
    }

    public EFLINK_MSG_19011(byte versionInside, byte tag, int handleId) {
        VersionInside = versionInside;
        Tag = tag;
        HandleId = handleId;
    }

    public EFLINK_MSG_19011(byte[] packet) {
        InitPacket(packet, 0);
    }

    public EFLINK_MSG_19011(byte[] packet, int index) {
        InitPacket(packet, index);
    }

    public void InitPacket(byte[] packet, int index) {
        if (packet != null && packet.length >= EFLINK_MSG_LENGTH + index) {
            ByteBuffer buffer = ByteBuffer.wrap(packet);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.position(index);
            VersionInside = buffer.get();
            Tag = buffer.get();
            HandleId = buffer.getInt();
        }
    }

    public byte[] packet() {
        ByteBuffer buffer = ByteBuffer.allocate(EFLINK_MSG_LENGTH);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(VersionInside);
        buffer.put(Tag);
        buffer.putInt(HandleId);
        return buffer.array();
    }

    public byte getVersionInside() {
        return VersionInside;
    }

    public void setVersionInside(byte versionInside) {
        VersionInside = versionInside;
    }

    public byte getTag() {
        return Tag;
    }

    public void setTag(byte tag) {
        Tag = tag;
    }

    public int getHandleId() {
        return HandleId;
    }

    public void setHandleId(int handleId) {
        HandleId = handleId;
    }
}

