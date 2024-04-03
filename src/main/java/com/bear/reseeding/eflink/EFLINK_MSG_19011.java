package com.bear.reseeding.eflink;

/**
 * 草原补种项目——确认上传
 *
 * @author N.
 * @date 2024-03-14 13:53
 */
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class EFLINK_MSG_19011 {
    public final int EFLINK_MSG_ID = 19011;
    /**
     * 内容段数据长度
     */
    public final int EFLINK_MSG_LENGTH = 26;
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
    String HandleId;

    public EFLINK_MSG_19011() {
    }

    public EFLINK_MSG_19011(byte versionInside, byte tag, String handleId) {
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

            // 读取 String 类型的数据，假设是按照 UTF-8 编码的字符串
            byte[] handleIdBytes = new byte[4]; // 假设长度为4
            buffer.get(handleIdBytes);
            HandleId = new String(handleIdBytes, StandardCharsets.UTF_8); // 使用 UTF-8 编码转换为字符串
        }
    }

    public byte[] packet() {
        ByteBuffer buffer = ByteBuffer.allocate(EFLINK_MSG_LENGTH);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(VersionInside);
        buffer.put(Tag);

        // 将 String 类型的数据转换为字节数组，假设使用 UTF-8 编码
        byte[] handleIdBytes = HandleId.getBytes(StandardCharsets.UTF_8);
        buffer.put(handleIdBytes);

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

    public String getHandleId() {
        return HandleId;
    }

    public void setHandleId(String handleId) {
        HandleId = handleId;
    }
}

