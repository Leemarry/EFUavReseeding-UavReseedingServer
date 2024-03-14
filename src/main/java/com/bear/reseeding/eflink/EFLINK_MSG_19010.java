package com.bear.reseeding.eflink;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 草原补种项目——开始处理数据
 *
 * @author N.
 * @date 2024-03-14 13:23
 */
public class EFLINK_MSG_19010 {
    public final int EFLINK_MSG_ID = 19010;
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
    int HandleId;
    /**
     * 原点纬度
     */
    int original_latitude;
    /**
     * 原点经度
     */
    int original_longitude;
    /**
     * 原点大地高
     */
    float orginal_height;
    /**
     * 补播无人机高度
     */
    float reseed_uav_height;
    /**
     * 补播机构参数
     */
    float reseed_machine_param;

    public EFLINK_MSG_19010() {
    }

    public EFLINK_MSG_19010(byte tag, byte versionInside, int handleId, int originalLatitude, int originalLongitude, float orginalHeight, float reseedUavHeight, float reseedMachineParam) {
        Tag = tag;
        VersionInside = versionInside;
        HandleId = handleId;
        original_latitude = originalLatitude;
        original_longitude = originalLongitude;
        orginal_height = orginalHeight;
        reseed_uav_height = reseedUavHeight;
        reseed_machine_param = reseedMachineParam;
    }

    public EFLINK_MSG_19010(byte[] packet) {
        InitPacket(packet, 0);
    }
    public EFLINK_MSG_19010(byte[] packet, int index) {
        InitPacket(packet, index);
    }

    public void InitPacket(byte[] packet, int index) {
        if (packet != null && packet.length >= index + 29) {
            ByteBuffer buffer = ByteBuffer.wrap(packet);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.position(index);
            VersionInside = buffer.get();
            Tag = buffer.get();
            HandleId = buffer.getInt();
            original_latitude = buffer.getInt();
            original_longitude = buffer.getInt();
            orginal_height = buffer.getFloat();
            reseed_uav_height = buffer.getFloat();
            reseed_machine_param = buffer.getFloat();
        }
    }


    public byte[] packet() {
        ByteBuffer buffer = ByteBuffer.allocate(EFLINK_MSG_LENGTH);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(VersionInside);
        buffer.put(Tag);
        buffer.putInt(HandleId);
        buffer.putInt(original_latitude);
        buffer.putInt(original_longitude);
        buffer.putFloat(orginal_height);
        buffer.putFloat(reseed_uav_height);
        buffer.putFloat(reseed_machine_param);
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

    public int getOriginal_latitude() {
        return original_latitude;
    }

    public void setOriginal_latitude(int original_latitude) {
        this.original_latitude = original_latitude;
    }

    public int getOriginal_longitude() {
        return original_longitude;
    }

    public void setOriginal_longitude(int original_longitude) {
        this.original_longitude = original_longitude;
    }

    public float getOrginal_height() {
        return orginal_height;
    }

    public void setOrginal_height(float orginal_height) {
        this.orginal_height = orginal_height;
    }

    public float getReseed_uav_height() {
        return reseed_uav_height;
    }

    public void setReseed_uav_height(float reseed_uav_height) {
        this.reseed_uav_height = reseed_uav_height;
    }

    public float getReseed_machine_param() {
        return reseed_machine_param;
    }

    public void setReseed_machine_param(float reseed_machine_param) {
        this.reseed_machine_param = reseed_machine_param;
    }
}
