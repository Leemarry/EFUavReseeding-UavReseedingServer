package com.bear.reseeding.eflink;

import com.alibaba.fastjson.JSONObject;
import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 从无人机获取媒体文件(#3201)
 */
public class EFLINK_MSG_3201 implements Serializable {
    public final int EFLINK_MSG_ID = 3201;

    /**
     * 内容段数据长度
     */
    public final int EFLINK_MSG_LENGTH = 20;

    int VersionInside;
    int Tag;
    int StorageArea;
    int MediaType;
    long StartTimestamp;
    long EndTimestamp;


    public EFLINK_MSG_3201() {
    }

    public void InitPacket(byte[] packet) {
        InitPacket(packet, 12);
    }
    public EFLINK_MSG_3201(int versionInside, int tag, int storageArea, int mediaType, long startTimestamp,long endTimestamp) {
        VersionInside = versionInside;
        Tag = tag;
        StorageArea = storageArea;
        MediaType = mediaType;
        StartTimestamp = startTimestamp;
        EndTimestamp = endTimestamp;
    }

    public EFLINK_MSG_3201(byte[] packet) {
        InitPacket(packet, 0);
    }

    public EFLINK_MSG_3201(byte[] packet, int index) {
        InitPacket(packet, index);
    }

    public void InitPacket(byte[] packet, int index) {
        VersionInside = packet[index] & 0xFF;  //当前载荷段数据版本，协议升级后可根据版本号区分载荷段内容。
        index++;
        Tag = packet[index] & 0xFF; //标记
        index++;
        StorageArea = packet[index] & 0xFF;
        index++;
        MediaType = packet[index] & 0xFF;
        index++;
        StartTimestamp = BytesUtil.bytes2Long(packet, index);
        index += 8;
        EndTimestamp = BytesUtil.bytes2Long(packet, index);
        index += 8;
    }


    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(EFLINK_MSG_LENGTH);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) (VersionInside));
        buffer.put((byte) (Tag));
        buffer.put((byte)(StorageArea));
        buffer.put((byte)(MediaType));
        buffer.put(BytesUtil.long2Bytes(StartTimestamp));
        buffer.put(BytesUtil.long2Bytes(EndTimestamp));
        return buffer.array();
    }


    public String toJsonArray() {
        return JSONObject.toJSONString(this);
    }

    public int getVersionInside() {
        return VersionInside;
    }

    public void setVersionInside(int versionInside) {
        VersionInside = versionInside;
    }

    public int getTag() {
        return Tag;
    }

    public void setTag(int tag) {
        Tag = tag;
    }

    public int getStorageArea() {
        return StorageArea;
    }

    public void setStorageArea(int storageArea) {
        StorageArea = storageArea;
    }

    public int getMediaType() {
        return MediaType;
    }

    public void setMediaType(int mediaType) {
        MediaType = mediaType;
    }

    public long getStartTimestamp() {
        return StartTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        StartTimestamp = startTimestamp;
    }

    public long getEndTimestamp() {
        return EndTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        EndTimestamp = endTimestamp;
    }
}
