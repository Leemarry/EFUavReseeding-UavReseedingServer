package com.bear.reseeding.eflink;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 翼飞无人机心跳包
 * @author N.
 * @date 2023-12-12 11:11
 */
public class EFLINK_MSG_2000 implements Serializable {
    /**
     * 消息ID。
     */
    public final int EFLINK_MSG_ID = 2000;

    // 保留给hive ID。
    public int DeviceID;
    // 无人机当前纬度，以1E7度为单位。
    public int Lat;
    // 无人机当前经度，以1E7度为单位。
    public int Lng;
    // 相对高度，以厘米为单位。
    public int AltRel;
    // 相对海平面高度，以厘米为单位。
    public int AltAbs;
    // 水平速度，以厘米/秒为单位。
    public int XYSpeed;
    // 垂直速度，以厘米/秒为单位。
    public int ZSpeed;
    // 飞行模式，MAVLINK枚举类型。
    public int Mode;
    // 翻滚角度（-pi..+pi）。
    public float Roll;
    // 俯仰角度（-pi..+pi）。
    public float Pitch;
    // 偏航角度（-pi..+pi）。
    public float Yaw;
    // 电压值。
    public int BattPert;
    // 下行信号质量（百分比）。
    public int DownlinkSignalQuality;
    // 上行信号质量（百分比）。
    public int UplinkSignalQuality;
    // GPS定位状态，MAVLINK枚举类型。
    public int GpsStatus;
    // 可见卫星数量。
    public int SatelliteCount;
    // 水平精度（厘米）。
    public int GpsHdop;
    // 系统状态：
    // 0：未连接到无人机。
    // 1：已连接到无人机。
    // 3：已连接到无人机，但无控制权限。
    // 4：已连接到无人机并具有控制权限。
    public int SystemStatus;
    // 无人机是否解锁（1为解锁，0为上锁）。
    public int Armed;

    public void InitPacket(byte[] packet, int index) {
        try {
            DeviceID = (short)((packet[index] & 0xFF) | (packet[index + 1] & 0xFF) << 8);
            index += 2;
            Lat = ((packet[index] & 0xFF) | (packet[index + 1] & 0xFF) << 8 | (packet[index + 2] & 0xFF) << 16 | (packet[index + 3] & 0xFF) << 24);
            index += 4;
            Lng = ((packet[index] & 0xFF) | (packet[index + 1] & 0xFF) << 8 | (packet[index + 2] & 0xFF) << 16 | (packet[index + 3] & 0xFF) << 24);
            index += 4;
            AltRel = ((packet[index] & 0xFF) | (packet[index + 1] & 0xFF) << 8 | (packet[index + 2] & 0xFF) << 16 | (packet[index + 3] & 0xFF) << 24);
            index += 4;
            AltAbs = ((packet[index] & 0xFF) | (packet[index + 1] & 0xFF) << 8 | (packet[index + 2] & 0xFF) << 16 | (packet[index + 3] & 0xFF) << 24);
            index += 4;
            XYSpeed = (short)((packet[index] & 0xFF) | (packet[index + 1] & 0xFF) << 8);
            index += 2;
            ZSpeed = (short)((packet[index] & 0xFF) | (packet[index + 1] & 0xFF) << 8);
            index += 2;
            Mode = packet[index];
            index++;
            Roll = Float.intBitsToFloat(((packet[index] & 0xFF) | (packet[index + 1] & 0xFF) << 8 | (packet[index + 2] & 0xFF) << 16 | (packet[index + 3] & 0xFF) << 24));
            index += 4;
            Pitch = Float.intBitsToFloat(((packet[index] & 0xFF) | (packet[index + 1] & 0xFF) << 8 | (packet[index + 2] & 0xFF) << 16 | (packet[index + 3] & 0xFF) << 24));
            index += 4;
            Yaw = Float.intBitsToFloat(((packet[index] & 0xFF) | (packet[index + 1] & 0xFF) << 8 | (packet[index + 2] & 0xFF) << 16 | (packet[index + 3] & 0xFF) << 24));
            index += 4;
            BattPert = ((short)((packet[index] & 0xFF) | (packet[index + 1] & 0xFF) << 8));
            index += 2;
            DownlinkSignalQuality = packet[index];
            index++;
            UplinkSignalQuality = packet[index];
            index++;
            GpsStatus = packet[index];
            index++;
            SatelliteCount = packet[index];
            index++;
            GpsHdop = (short)((packet[index] & 0xFF) | (packet[index + 1] & 0xFF) << 8);
            index += 2;
            SystemStatus = packet[index];
            index++;
            Armed = packet[index];
            index++;
        } catch (Exception ignored) {
        }
    }
    @Override
    public String toString() {
        return "EFLINK_MSG_2000{" +
                "EFLINK_MSG_ID=" + EFLINK_MSG_ID +
                ", DeviceID=" + DeviceID +
                ", Lat=" + Lat +
                ", Lng=" + Lng +
                ", AltRel=" + AltRel +
                ", AltAbs=" + AltAbs +
                ", XYSpeed=" + XYSpeed +
                ", ZSpeed=" + ZSpeed +
                ", Mode=" + Mode +
                ", Roll=" + Roll +
                ", Pitch=" + Pitch +
                ", Yaw=" + Yaw +
                ", BattPert=" + BattPert +
                ", DownlinkSignalQuality=" + DownlinkSignalQuality +
                ", UplinkSignalQuality=" + UplinkSignalQuality +
                ", GpsStatus=" + GpsStatus +
                ", SatelliteCount=" + SatelliteCount +
                ", GpsHdop=" + GpsHdop +
                ", SystemStatus=" + SystemStatus +
                ", Armed=" + Armed +
                '}';
    }

    public int getEFLINK_MSG_ID() {
        return EFLINK_MSG_ID;
    }

    public int getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(int deviceID) {
        DeviceID = deviceID;
    }

    public int getLat() {
        return Lat;
    }

    public void setLat(int lat) {
        Lat = lat;
    }

    public int getLng() {
        return Lng;
    }

    public void setLng(int lng) {
        Lng = lng;
    }

    public int getAltRel() {
        return AltRel;
    }

    public void setAltRel(int altRel) {
        AltRel = altRel;
    }

    public int getAltAbs() {
        return AltAbs;
    }

    public void setAltAbs(int altAbs) {
        AltAbs = altAbs;
    }

    public int getXYSpeed() {
        return XYSpeed;
    }

    public void setXYSpeed(int XYSpeed) {
        this.XYSpeed = XYSpeed;
    }

    public int getZSpeed() {
        return ZSpeed;
    }

    public void setZSpeed(int ZSpeed) {
        this.ZSpeed = ZSpeed;
    }

    public int getMode() {
        return Mode;
    }

    public void setMode(int mode) {
        Mode = mode;
    }

    public float getRoll() {
        return Roll;
    }

    public void setRoll(float roll) {
        Roll = roll;
    }

    public float getPitch() {
        return Pitch;
    }

    public void setPitch(float pitch) {
        Pitch = pitch;
    }

    public float getYaw() {
        return Yaw;
    }

    public void setYaw(float yaw) {
        Yaw = yaw;
    }

    public int getBattPert() {
        return BattPert;
    }

    public void setBattPert(int battPert) {
        BattPert = battPert;
    }

    public int getDownlinkSignalQuality() {
        return DownlinkSignalQuality;
    }

    public void setDownlinkSignalQuality(int downlinkSignalQuality) {
        DownlinkSignalQuality = downlinkSignalQuality;
    }

    public int getUplinkSignalQuality() {
        return UplinkSignalQuality;
    }

    public void setUplinkSignalQuality(int uplinkSignalQuality) {
        UplinkSignalQuality = uplinkSignalQuality;
    }

    public int getGpsStatus() {
        return GpsStatus;
    }

    public void setGpsStatus(int gpsStatus) {
        GpsStatus = gpsStatus;
    }

    public int getSatelliteCount() {
        return SatelliteCount;
    }

    public void setSatelliteCount(int satelliteCount) {
        SatelliteCount = satelliteCount;
    }

    public int getGpsHdop() {
        return GpsHdop;
    }

    public void setGpsHdop(int gpsHdop) {
        GpsHdop = gpsHdop;
    }

    public int getSystemStatus() {
        return SystemStatus;
    }

    public void setSystemStatus(int systemStatus) {
        SystemStatus = systemStatus;
    }

    public int getArmed() {
        return Armed;
    }

    public void setArmed(int armed) {
        Armed = armed;
    }
}
