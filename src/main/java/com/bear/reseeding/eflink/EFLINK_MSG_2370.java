package com.bear.reseeding.eflink;

import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * 基站心跳包
 *
 * @Auther: bear
 * @Date: 2021/12/30 18:20
 * @Description: null
 */
public class EFLINK_MSG_2370 implements Serializable {

    public final int EFLINK_MSG_ID = 2370;

    private long deviceId;
    private Timestamp dataDate;
    private double lat;
    private double lng;
    private double altRel;
    private double altAbs;
    private int gpsStatus;
    private int satelliteCount;

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getAltRel() {
        return altRel;
    }

    public void setAltRel(double altRel) {
        this.altRel = altRel;
    }

    public double getAltAbs() {
        return altAbs;
    }

    public void setAltAbs(double altAbs) {
        this.altAbs = altAbs;
    }

    public int getGpsStatus() {
        return gpsStatus;
    }

    public void setGpsStatus(int gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    public int getSatelliteCount() {
        return satelliteCount;
    }

    public void setSatelliteCount(int satelliteCount) {
        this.satelliteCount = satelliteCount;
    }

    public void InitPacket(byte[] packet, int Index) {
        try {
            Timestamp time1 = new Timestamp(System.currentTimeMillis());//获取系统当前时间
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String timeStr1 = df1.format(time1);
            dataDate = (Timestamp.valueOf(timeStr1));

            deviceId = (BytesUtil.bytes2UShort(packet, Index)); // 蜂巢编号
            Index += 2;
            lat = (BytesUtil.bytes2Int(packet, Index)) / 1e7d;
            Index += 4;
            lng = (BytesUtil.bytes2Int(packet, Index)) / 1e7d;
            Index += 4;
            altRel = (BytesUtil.bytes2Int(packet, Index)) / 100D;
            Index += 4;
            altAbs = (BytesUtil.bytes2Int(packet, Index)) / 100D;
            Index += 4;
            gpsStatus = packet[Index] & 0xFF;
            Index++;
            satelliteCount = packet[Index] & 0xFF;
            Index++;
        } catch (Exception ignored) {
        }
    }

    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(16 + 9);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
//        buffer.put((byte) (IsGcsOnline));
//        buffer.put((byte) (IsUavOnline));
//        buffer.put((byte) (IsRomoteControlOnline));
//        buffer.put((byte) (IsAirLinkOnline));
//        buffer.put((byte) (IsCameraOnline));
//        buffer.put(BytesUtil.int2Bytes((int) BootTime));
        return buffer.array();
    }
}
