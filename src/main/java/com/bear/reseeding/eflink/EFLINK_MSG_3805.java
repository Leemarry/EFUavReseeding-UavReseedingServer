package com.bear.reseeding.eflink;

import com.alibaba.fastjson.JSONObject;
import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * UPS电源—额定信息数据包(#3805)
 */
public class EFLINK_MSG_3805 implements Serializable {
    public final int EFLINK_MSG_ID = 3805;

    /**
     * 内容段数据长度
     */
    public final int EFLINK_MSG_LENGTH = 18;

    int VersionInside;
    int Tag;
    float OutputRatedVoltage;
    float OutputRatedCurrent;
    float BatteryVoltage;
    float OutputRatedFrequency;


    public EFLINK_MSG_3805() {
    }

    public EFLINK_MSG_3805(int versionInside, int tag, float outputRatedVoltage, float outputRatedCurrent, float batteryVoltage, float outputRatedFrequency) {
        VersionInside = versionInside;
        Tag = tag;
        OutputRatedVoltage = outputRatedVoltage;
        OutputRatedCurrent = outputRatedCurrent;
        BatteryVoltage = batteryVoltage;
        OutputRatedFrequency = outputRatedFrequency;
    }

    public EFLINK_MSG_3805(byte[] packet) {
        InitPacket(packet, 0);
    }

    public EFLINK_MSG_3805(byte[] packet, int index) {
        InitPacket(packet, index);
    }

    public void InitPacket(byte[] packet, int index) {
        VersionInside = packet[index] & 0xFF;  //当前载荷段数据版本，协议升级后可根据版本号区分载荷段内容。
        index++;
        Tag = packet[index] & 0xFF; //标记
        index++;
        OutputRatedVoltage = BytesUtil.bytes2Float(packet, index);
        index += 4;
        OutputRatedCurrent = BytesUtil.bytes2Float(packet, index);
        index += 4;
        BatteryVoltage = BytesUtil.bytes2Float(packet, index);
        index += 4;
        OutputRatedFrequency = BytesUtil.bytes2Float(packet, index);
        index += 4;
    }


    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(EFLINK_MSG_LENGTH);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) (VersionInside));
        buffer.put((byte) (Tag));
        buffer.put(BytesUtil.float2Bytes(OutputRatedVoltage));
        buffer.put(BytesUtil.float2Bytes(OutputRatedCurrent));
        buffer.put(BytesUtil.float2Bytes(BatteryVoltage));
        buffer.put(BytesUtil.float2Bytes(OutputRatedFrequency));
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

    public float getOutputRatedVoltage() {
        return OutputRatedVoltage;
    }

    public void setOutputRatedVoltage(float outputRatedVoltage) {
        OutputRatedVoltage = outputRatedVoltage;
    }

    public float getOutputRatedCurrent() {
        return OutputRatedCurrent;
    }

    public void setOutputRatedCurrent(float outputRatedCurrent) {
        OutputRatedCurrent = outputRatedCurrent;
    }

    public float getBatteryVoltage() {
        return BatteryVoltage;
    }

    public void setBatteryVoltage(float batteryVoltage) {
        BatteryVoltage = batteryVoltage;
    }

    public float getOutputRatedFrequency() {
        return OutputRatedFrequency;
    }

    public void setOutputRatedFrequency(float outputRatedFrequency) {
        OutputRatedFrequency = outputRatedFrequency;
    }
}
