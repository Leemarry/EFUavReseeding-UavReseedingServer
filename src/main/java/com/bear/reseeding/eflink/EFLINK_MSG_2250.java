package com.bear.reseeding.eflink;

import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Android端服务后台的心跳包数据
 *
 * @Auther: bear
 * @Date: 2021/12/30 18:20
 * @Description: null
 */
public class EFLINK_MSG_2250 implements Serializable {

    public final int EFLINK_MSG_ID = 2250;

    int VersionInside;
    byte[] MachineCode;
    int IsGcsOnline;
    int IsUavOnline;
    int IsRomoteControlOnline;
    int IsAirLinkOnline;
    int IsCameraOnline;
    int HiveTemperature;
    int AirConditioningStatus;
    int Back1;
    int Back2;
    int Back3;
    int ChargeStatus; //0：未知，1：未充电，2：充电中，3：已充满
    int ChargeVoltage;
    int ChargeCurrent;
    int ChargePert;
    int ChargeNeedTime;

    long BootTime;

    public EFLINK_MSG_2250() {
    }

    @Override
    public String toString() {
        return "EFLINK_MSG_2250{" +
                "EFLINK_MSG_ID=" + EFLINK_MSG_ID +
                ", VersionInside=" + VersionInside +
                ", MachineCode=" + Arrays.toString(MachineCode) +
                ", IsGcsOnline=" + IsGcsOnline +
                ", IsUavOnline=" + IsUavOnline +
                ", IsRomoteControlOnline=" + IsRomoteControlOnline +
                ", IsAirLinkOnline=" + IsAirLinkOnline +
                ", IsCameraOnline=" + IsCameraOnline +
                ", HiveTemperature=" + HiveTemperature +
                ", AirConditioningStatus=" + AirConditioningStatus +
                ", Back1=" + Back1 +
                ", Back2=" + Back2 +
                ", Back3=" + Back3 +
                ", ChargeStatus=" + ChargeStatus +
                ", ChargeVoltage=" + ChargeVoltage +
                ", ChargeCurrent=" + ChargeCurrent +
                ", ChargePert=" + ChargePert +
                ", ChargeNeedTime=" + ChargeNeedTime +
                ", BootTime=" + BootTime +
                '}';
    }
//    public EFLINK_MSG_2250_V2(int isGcsOnline, int isUavOnline, long bootTime) {
//        IsGcsOnline = isGcsOnline;
//        IsUavOnline = isUavOnline;
//        BootTime = bootTime;
//    }
//
//    public EFLINK_MSG_2250_V2(byte[] machineCode, int isGcsOnline, int isUavOnline, long bootTime) {
//        MachineCode = machineCode;
//        IsGcsOnline = isGcsOnline;
//        IsUavOnline = isUavOnline;
//        BootTime = bootTime;
//    }
//
//    public EFLINK_MSG_2250_V2(byte[] packet) {
//        InitPacket(packet, 0);
//    }
//
//    public EFLINK_MSG_2250_V2(byte[] packet, int index) {
//        InitPacket(packet, index);
//    }

    public void InitPacket(byte[] packet, int index) {
        try {
            VersionInside = packet[index] & 0xFF;  //新增： 版本 v2
            index++;
            if (VersionInside == 2) {
                MachineCode = new byte[16];
                for (int i = 0; i < MachineCode.length; i++) {
                    MachineCode[i] = packet[index];
                    index++;
                }
                IsGcsOnline = packet[index] & 0xFF;  //地面站是否在线
                index++;
                IsUavOnline = packet[index] & 0xFF;  //无人机是否在线
                index++;
                IsRomoteControlOnline = packet[index] & 0xFF;  //遥控器是否已连接
                index++;
                IsAirLinkOnline = packet[index] & 0xFF;  //遥控器与无人机通讯是否正常
                index++;
                IsCameraOnline = packet[index] & 0xFF;  //无人机相机模块是否已连接
                index++;
                // 新增
                HiveTemperature = packet[index]; //机巢内部温度	0.1摄氏度
                index++;
                AirConditioningStatus = packet[index] & 0xFF; // 空调开关状态	0：未知, 1:自动模式， 2：已开启，2：制冷，3：制热，4：常规
                index++;
                Back1 = packet[index] & 0xFF;
                index++;
                Back2 = packet[index] & 0xFF;
                index++;
                Back3 = packet[index] & 0xFF;
                index++;
                ChargeStatus = packet[index] & 0xFF; //充电状态	0：未知，1：未充电，2：充电中，3：已充满
                index++;
                ChargeVoltage = BytesUtil.bytes2UShort(packet, index);   //充电电压	毫伏
                index += 2;
                ChargeCurrent = BytesUtil.bytes2UShort(packet, index);   //充电电流	毫安
                index += 2;
                ChargePert = packet[index] & 0xFF;  //充电百分比	%
                index++;
                ChargeNeedTime = BytesUtil.bytes2UShort(packet, index);  //剩余充满时间	秒
                index += 2;
                BootTime = BytesUtil.bytes2UInt(packet, index);  //启动时长
                index += 4;
            }
        } catch (Exception ignored) {
        }
    }

    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(16 + 23);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) (VersionInside));
        if (VersionInside == 2) {
            if (MachineCode == null || MachineCode.length != 16) {
                MachineCode = new byte[16];
            }
            buffer.put(MachineCode);
            buffer.put((byte) (IsGcsOnline));
            buffer.put((byte) (IsUavOnline));
            buffer.put((byte) (IsRomoteControlOnline));
            buffer.put((byte) (IsAirLinkOnline));
            buffer.put((byte) (IsCameraOnline));

            buffer.put((byte) (HiveTemperature));
            buffer.put((byte) (AirConditioningStatus));
            buffer.put((byte) (Back1));
            buffer.put((byte) (Back2));
            buffer.put((byte) (Back3));
            buffer.put((byte) (ChargeStatus));
            buffer.put(BytesUtil.short2Bytes(ChargeVoltage));
            buffer.put(BytesUtil.short2Bytes(ChargeCurrent));
            buffer.put((byte) (ChargePert));
            buffer.put(BytesUtil.short2Bytes(ChargeNeedTime));

            buffer.put(BytesUtil.int2Bytes((int) BootTime));
        }
        return buffer.array();
    }


    public int getVersionInside() {
        return VersionInside;
    }

    public void setVersionInside(int versionInside) {
        VersionInside = versionInside;
    }

    public byte[] getMachineCode() {
        return MachineCode;
    }

    public void setMachineCode(byte[] machineCode) {
        MachineCode = machineCode;
    }

    public int getIsGcsOnline() {
        return IsGcsOnline;
    }

    public void setIsGcsOnline(int isGcsOnline) {
        IsGcsOnline = isGcsOnline;
    }

    public int getIsUavOnline() {
        return IsUavOnline;
    }

    public void setIsUavOnline(int isUavOnline) {
        IsUavOnline = isUavOnline;
    }

    public long getBootTime() {
        return BootTime;
    }

    public void setBootTime(long bootTime) {
        BootTime = bootTime;
    }

    public int getIsRomoteControlOnline() {
        return IsRomoteControlOnline;
    }

    public void setIsRomoteControlOnline(int isRomoteControlOnline) {
        IsRomoteControlOnline = isRomoteControlOnline;
    }

    public int getIsAirLinkOnline() {
        return IsAirLinkOnline;
    }

    public void setIsAirLinkOnline(int isAirLinkOnline) {
        IsAirLinkOnline = isAirLinkOnline;
    }

    public int getIsCameraOnline() {
        return IsCameraOnline;
    }

    public void setIsCameraOnline(int isCameraOnline) {
        IsCameraOnline = isCameraOnline;
    }

    public int getHiveTemperature() {
        return HiveTemperature;
    }

    public void setHiveTemperature(int hiveTemperature) {
        HiveTemperature = hiveTemperature;
    }

    public int getAirConditioningStatus() {
        return AirConditioningStatus;
    }

    public void setAirConditioningStatus(int airConditioningStatus) {
        AirConditioningStatus = airConditioningStatus;
    }

    public int getBack1() {
        return Back1;
    }

    public void setBack1(int back1) {
        Back1 = back1;
    }

    public int getBack2() {
        return Back2;
    }

    public void setBack2(int back2) {
        Back2 = back2;
    }

    public int getBack3() {
        return Back3;
    }

    public void setBack3(int back3) {
        Back3 = back3;
    }

    public int getChargeStatus() {
        return ChargeStatus;
    }

    public void setChargeStatus(int chargeStatus) {
        ChargeStatus = chargeStatus;
    }

    public int getChargeVoltage() {
        return ChargeVoltage;
    }

    public void setChargeVoltage(int chargeVoltage) {
        ChargeVoltage = chargeVoltage;
    }

    public int getChargeCurrent() {
        return ChargeCurrent;
    }

    public void setChargeCurrent(int chargeCurrent) {
        ChargeCurrent = chargeCurrent;
    }

    public int getChargePert() {
        return ChargePert;
    }

    public void setChargePert(int chargePert) {
        ChargePert = chargePert;
    }

    public int getChargeNeedTime() {
        return ChargeNeedTime;
    }

    public void setChargeNeedTime(int chargeNeedTime) {
        ChargeNeedTime = chargeNeedTime;
    }
}
