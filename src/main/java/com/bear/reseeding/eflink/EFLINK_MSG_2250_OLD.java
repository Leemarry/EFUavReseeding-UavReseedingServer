package com.bear.reseeding.eflink;

import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Android端服务后台的心跳包数据
 *
 * @Auther: bear
 * @Date: 2021/12/30 18:20
 * @Description: null
 */
public class EFLINK_MSG_2250_OLD implements Serializable {

    public final int EFLINK_MSG_ID = 2250;

    byte[] MachineCode;
    int IsGcsOnline;
    int IsUavOnline;
    int IsRomoteControlOnline;
    int IsAirLinkOnline;
    int IsCameraOnline;

    long BootTime;

    public EFLINK_MSG_2250_OLD() {
    }

    public EFLINK_MSG_2250_OLD(int isGcsOnline, int isUavOnline, long bootTime) {
        IsGcsOnline = isGcsOnline;
        IsUavOnline = isUavOnline;
        BootTime = bootTime;
    }

    public EFLINK_MSG_2250_OLD(byte[] machineCode, int isGcsOnline, int isUavOnline, long bootTime) {
        MachineCode = machineCode;
        IsGcsOnline = isGcsOnline;
        IsUavOnline = isUavOnline;
        BootTime = bootTime;
    }

    public EFLINK_MSG_2250_OLD(byte[] packet) {
        InitPacket(packet, 0);
    }

    public EFLINK_MSG_2250_OLD(byte[] packet, int index) {
        InitPacket(packet, index);
    }

    public void InitPacket(byte[] packet, int index) {
        try {
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
            BootTime = BytesUtil.bytes2UInt(packet, index);  //启动时长
            index += 4;
        } catch (Exception ignored) {
        }
    }

    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(16 + 9);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        if (MachineCode == null || MachineCode.length != 16) {
            MachineCode = new byte[16];
        }
        buffer.put(MachineCode);
        buffer.put((byte) (IsGcsOnline));
        buffer.put((byte) (IsUavOnline));
        buffer.put((byte) (IsRomoteControlOnline));
        buffer.put((byte) (IsAirLinkOnline));
        buffer.put((byte) (IsCameraOnline));
        buffer.put(BytesUtil.int2Bytes((int) BootTime));
        return buffer.array();
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
}
