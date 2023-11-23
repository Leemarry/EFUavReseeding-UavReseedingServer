package com.bear.reseeding.eflink;

import com.alibaba.fastjson.JSONObject;
import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * UPS电源—状态及参数数据包(#3804)
 */
public class EFLINK_MSG_3804 implements Serializable {
    public final int EFLINK_MSG_ID = 3804;

    /**
     * 内容段数据长度
     */
    public final int EFLINK_MSG_LENGTH = 31;

    int VersionInside;
    int Tag;
    float InputVoltage;
    float LastFaultInputVoltage;
    float OutputVoltage;
    float LoadPert;
    float OutFrequency;
    float BatteryVoltage;
    float Temperature;
    int UpsStatus;

    boolean StatusCityElectric; //b7 市电状态，0正常，1异常
    boolean StatusBatteryVoltage;  //b6 电池电压，0正常，1过低
    boolean StatusAvr;  //b5 AVR状态，0非AVR状态，1AVR状态
    boolean StatusFault;  //b4 fault模式，0非fault模式，1fault模式
    boolean StatusUpsType;  //b3 UPS模式，0在线UPS，1后备式UPS
    boolean StatusSelfCheck;  //b2 自检状态，0非自检模式，1自检模式
    boolean StatusShutdownSign;  //b1 shutdown信号状态，0无shutdown信号发出，1shutdown信号已发出
    boolean StatusBuzzer;  //b0 蜂鸣器状态，0蜂鸣器关闭，1蜂鸣器开

    public EFLINK_MSG_3804() {
    }

    public EFLINK_MSG_3804(byte[] packet) {
        InitPacket(packet, 0);
    }

    public EFLINK_MSG_3804(byte[] packet, int index) {
        InitPacket(packet, index);
    }

    public void InitPacket(byte[] packet, int index) {
        VersionInside = packet[index] & 0xFF;  //当前载荷段数据版本，协议升级后可根据版本号区分载荷段内容。
        index++;
        Tag = packet[index] & 0xFF; //标记
        index += 1;
        InputVoltage = BytesUtil.bytes2Float(packet, index);
        index += 4;
        LastFaultInputVoltage = BytesUtil.bytes2Float(packet, index);
        index += 4;
        OutputVoltage = BytesUtil.bytes2Float(packet, index);
        index += 4;
        LoadPert = BytesUtil.bytes2Float(packet, index);
        index += 4;
        OutFrequency = BytesUtil.bytes2Float(packet, index);
        index += 4;
        BatteryVoltage = BytesUtil.bytes2Float(packet, index);
        index += 4;
        Temperature = BytesUtil.bytes2Float(packet, index);
        index += 4;
        UpsStatus = packet[index] & 0xFF; //标记
        byte UpsStatusTemp = packet[index];
        index += 1;
        StatusCityElectric = ((UpsStatusTemp & 0x80) == 0x80);
        StatusBatteryVoltage = ((UpsStatusTemp & 0x40) == 0x40);
        StatusAvr = ((UpsStatusTemp & 0x20) == 0x20);
        StatusFault = ((UpsStatusTemp & 0x10) == 0x10);
        StatusUpsType = ((UpsStatusTemp & 0x08) == 0x08);
        StatusSelfCheck = ((UpsStatusTemp & 0x04) == 0x04);
        StatusShutdownSign = ((UpsStatusTemp & 0x02) == 0x02);
        StatusBuzzer = ((UpsStatusTemp & 0x01) == 0x01);
    }

    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(EFLINK_MSG_LENGTH);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) (VersionInside));
        buffer.put((byte) (Tag));
        buffer.put(BytesUtil.float2Bytes(InputVoltage));
        buffer.put(BytesUtil.float2Bytes(LastFaultInputVoltage));
        buffer.put(BytesUtil.float2Bytes(OutputVoltage));
        buffer.put(BytesUtil.float2Bytes(LoadPert));
        buffer.put(BytesUtil.float2Bytes(OutFrequency));
        buffer.put(BytesUtil.float2Bytes(BatteryVoltage));
        buffer.put(BytesUtil.float2Bytes(Temperature));
        buffer.put((byte) (UpsStatus));

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

    public float getInputVoltage() {
        return InputVoltage;
    }

    public void setInputVoltage(float inputVoltage) {
        InputVoltage = inputVoltage;
    }

    public float getLastFaultInputVoltage() {
        return LastFaultInputVoltage;
    }

    public void setLastFaultInputVoltage(float lastFaultInputVoltage) {
        LastFaultInputVoltage = lastFaultInputVoltage;
    }

    public float getOutputVoltage() {
        return OutputVoltage;
    }

    public void setOutputVoltage(float outputVoltage) {
        OutputVoltage = outputVoltage;
    }

    public float getLoadPert() {
        return LoadPert;
    }

    public void setLoadPert(float loadPert) {
        LoadPert = loadPert;
    }

    public float getOutFrequency() {
        return OutFrequency;
    }

    public void setOutFrequency(float outFrequency) {
        OutFrequency = outFrequency;
    }

    public float getBatteryVoltage() {
        return BatteryVoltage;
    }

    public void setBatteryVoltage(float batteryVoltage) {
        BatteryVoltage = batteryVoltage;
    }

    public float getTemperature() {
        return Temperature;
    }

    public void setTemperature(float temperature) {
        Temperature = temperature;
    }

    public int getUpsStatus() {
        return UpsStatus;
    }

    public void setUpsStatus(int upsStatus) {
        UpsStatus = upsStatus;
    }

    public boolean isStatusCityElectric() {
        return StatusCityElectric;
    }

    public void setStatusCityElectric(boolean statusCityElectric) {
        StatusCityElectric = statusCityElectric;
    }

    public boolean isStatusBatteryVoltage() {
        return StatusBatteryVoltage;
    }

    public void setStatusBatteryVoltage(boolean statusBatteryVoltage) {
        StatusBatteryVoltage = statusBatteryVoltage;
    }

    public boolean isStatusAvr() {
        return StatusAvr;
    }

    public void setStatusAvr(boolean statusAvr) {
        StatusAvr = statusAvr;
    }

    public boolean isStatusFault() {
        return StatusFault;
    }

    public void setStatusFault(boolean statusFault) {
        StatusFault = statusFault;
    }

    public boolean isStatusUpsType() {
        return StatusUpsType;
    }

    public void setStatusUpsType(boolean statusUpsType) {
        StatusUpsType = statusUpsType;
    }

    public boolean isStatusSelfCheck() {
        return StatusSelfCheck;
    }

    public void setStatusSelfCheck(boolean statusSelfCheck) {
        StatusSelfCheck = statusSelfCheck;
    }

    public boolean isStatusShutdownSign() {
        return StatusShutdownSign;
    }

    public void setStatusShutdownSign(boolean statusShutdownSign) {
        StatusShutdownSign = statusShutdownSign;
    }

    public boolean isStatusBuzzer() {
        return StatusBuzzer;
    }

    public void setStatusBuzzer(boolean statusBuzzer) {
        StatusBuzzer = statusBuzzer;
    }

    @Override
    public String toString() {
        return "EFLINK_MSG_3804{" +
                "EFLINK_MSG_ID=" + EFLINK_MSG_ID +
                ", EFLINK_MSG_LENGTH=" + EFLINK_MSG_LENGTH +
                ", VersionInside=" + VersionInside +
                ", Tag=" + Tag +
                ", InputVoltage=" + InputVoltage +
                ", LastFaultInputVoltage=" + LastFaultInputVoltage +
                ", OutputVoltage=" + OutputVoltage +
                ", LoadPert=" + LoadPert +
                ", OutFrequency=" + OutFrequency +
                ", BatteryVoltage=" + BatteryVoltage +
                ", Temperature=" + Temperature +
                ", UpsStatus=" + UpsStatus +
                ", StatusCityElectric=" + StatusCityElectric +
                ", StatusBatteryVoltage=" + StatusBatteryVoltage +
                ", StatusAvr=" + StatusAvr +
                ", StatusFault=" + StatusFault +
                ", StatusUpsType=" + StatusUpsType +
                ", StatusSelfCheck=" + StatusSelfCheck +
                ", StatusShutdownSign=" + StatusShutdownSign +
                ", StatusBuzzer=" + StatusBuzzer +
                '}';
    }
}
