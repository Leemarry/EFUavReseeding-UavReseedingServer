package com.bear.reseeding.eflink;


import com.bear.reseeding.utils.BytesUtil;
import com.bear.reseeding.utils.LogUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * 大疆单个航点内容,V2版本
 */
public class WaypointDjiV2 implements Serializable {
    /**
     * 内容段数据长度，最小为5，变长
     */
    public final int EFLINK_MSG_LENGTH = 59;

    //当前包总长度
    public int WaypointDjiLength = EFLINK_MSG_LENGTH;

    private int WpNo;
    private int Lat;
    private int Lng;
    private int AltRel;
    private int AltAbs;
    private int Speed;

    private int MaxSpeed;
    private int UavTurnMode;
    private float UavYaw;
    private float GimbalPitch;
    private float GimbalRoll;
    private float GimbalYaw;
    private int ShootPhotoTimeInterval;
    private int ShootPhotoDistanceInterval;
    private int ActionRepeatTimes;
    private int isUsingWaypointAutoFlightSpeed;
    private int isUsingWaypointMaxFlightSpeed;
    private int PointOfInterestLat;
    private int PointOfInterestLng;
    private int PointOfInterestAlt;

    private int ActionCount;

    private List<WaypointDjiAction> waypointDjiActionList;

    @Override
    public String toString() {
        StringBuilder waypointDjiActionListStr = new StringBuilder("空");
        if (waypointDjiActionList != null && waypointDjiActionList.size() > 0) {
            waypointDjiActionListStr.setLength(0);
            for (int i = 0; i < waypointDjiActionList.size(); i++) {
                waypointDjiActionListStr.append(waypointDjiActionList.get(i).toString());
                waypointDjiActionListStr.append(" ");
            }
        }
        return "WaypointDjiV2{" +
                "EFLINK_MSG_LENGTH=" + EFLINK_MSG_LENGTH +
                ", WaypointDjiLength=" + WaypointDjiLength +
                ", WpNo=" + WpNo +
                ", Lat=" + Lat +
                ", Lng=" + Lng +
                ", AltRel=" + AltRel +
                ", AltAbs=" + AltAbs +
                ", Speed=" + Speed +
                ", MaxSpeed=" + MaxSpeed +
                ", UavTurnMode=" + UavTurnMode +
                ", UavYaw=" + UavYaw +
                ", GimbalPitch=" + GimbalPitch +
                ", GimbalRoll=" + GimbalRoll +
                ", GimbalYaw=" + GimbalYaw +
                ", ShootPhotoTimeInterval=" + ShootPhotoTimeInterval +
                ", ShootPhotoDistanceInterval=" + ShootPhotoDistanceInterval +
                ", ActionRepeatTimes=" + ActionRepeatTimes +
                ", isUsingWaypointAutoFlightSpeed=" + isUsingWaypointAutoFlightSpeed +
                ", isUsingWaypointMaxFlightSpeed=" + isUsingWaypointMaxFlightSpeed +
                ", PointOfInterestLat=" + PointOfInterestLat +
                ", PointOfInterestLng=" + PointOfInterestLng +
                ", PointOfInterestAlt=" + PointOfInterestAlt +
                ", ActionCount=" + ActionCount +
                ", waypointDjiActionList=" + waypointDjiActionListStr +
                '}';
    }

    public byte[] packet() {
        ArrayList<Byte> list = new ArrayList<>();
        if (waypointDjiActionList != null && waypointDjiActionList.size() > 0) {
            for (WaypointDjiAction action : waypointDjiActionList) {
                byte[] temp = action.packet();
                for (byte b : temp) {
                    list.add(b);
                }
            }
        }
        ByteBuffer buffer = ByteBuffer.allocate(EFLINK_MSG_LENGTH + list.size());
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(BytesUtil.short2Bytes(WpNo));
        buffer.put(BytesUtil.int2Bytes(Lat));
        buffer.put(BytesUtil.int2Bytes(Lng));
        buffer.put(BytesUtil.int2Bytes(AltRel));
        buffer.put(BytesUtil.int2Bytes(AltAbs));
        buffer.put(BytesUtil.short2Bytes(Speed));

        buffer.put(BytesUtil.short2Bytes(MaxSpeed));
        buffer.put((byte) (UavTurnMode));
        buffer.put(BytesUtil.float2Bytes(UavYaw));
        buffer.put(BytesUtil.float2Bytes(GimbalPitch));
        buffer.put(BytesUtil.float2Bytes(GimbalRoll));
        buffer.put(BytesUtil.float2Bytes(GimbalYaw));
        buffer.put(BytesUtil.short2Bytes(ShootPhotoTimeInterval));
        buffer.put(BytesUtil.short2Bytes(ShootPhotoDistanceInterval));
        buffer.put((byte) ActionRepeatTimes);
        buffer.put((byte) (isUsingWaypointAutoFlightSpeed));
        buffer.put((byte) (isUsingWaypointMaxFlightSpeed));
        buffer.put(BytesUtil.int2Bytes(PointOfInterestLat));
        buffer.put(BytesUtil.int2Bytes(PointOfInterestLng));
        buffer.put(BytesUtil.int2Bytes(PointOfInterestAlt));

        buffer.put((byte) ActionCount);
        for (int i = 0; i < list.size(); i++) {
            buffer.put(list.get(i));
        }
        return buffer.array();
    }

    public void unpacket(byte[] packet, int index) {
        try {
            WpNo = BytesUtil.bytes2UShort(packet, index);
            index += 2;
            Lat = BytesUtil.bytes2Int(packet, index);
            index += 4;
            Lng = BytesUtil.bytes2Int(packet, index);
            index += 4;
            AltRel = BytesUtil.bytes2Int(packet, index);
            index += 4;
            AltAbs = BytesUtil.bytes2Int(packet, index);
            index += 4;
            Speed = BytesUtil.bytes2Short(packet, index);
            index += 2;
            // v2
            MaxSpeed = BytesUtil.bytes2Short(packet, index);
            index += 2;
            UavTurnMode = packet[index] & 0xFF;
            index += 1;
            UavYaw = BytesUtil.bytes2Float(packet, index);
            index += 4;
            GimbalPitch = BytesUtil.bytes2Float(packet, index);
            index += 4;
            GimbalRoll = BytesUtil.bytes2Float(packet, index);
            index += 4;
            GimbalYaw = BytesUtil.bytes2Float(packet, index);
            index += 4;
            ShootPhotoTimeInterval = BytesUtil.bytes2Short(packet, index);
            index += 2;
            ShootPhotoDistanceInterval = BytesUtil.bytes2Short(packet, index);
            index += 2;
            ActionRepeatTimes = packet[index] & 0xFF;
            index += 1;
            isUsingWaypointAutoFlightSpeed = packet[index] & 0xFF;
            index += 1;
            isUsingWaypointMaxFlightSpeed = packet[index] & 0xFF;
            index += 1;
            PointOfInterestLat = BytesUtil.bytes2Int(packet, index);
            index += 4;
            PointOfInterestLng = BytesUtil.bytes2Int(packet, index);
            index += 4;
            PointOfInterestAlt = BytesUtil.bytes2Int(packet, index);
            index += 4;
            ActionCount = packet[index] & 0xFF;
            index++;
            waypointDjiActionList = new ArrayList<>();
            if (ActionCount > 0) {
                for (int i = 0; i < ActionCount; i++) {
                    WaypointDjiAction action = new WaypointDjiAction();
                    action.unpacket(packet, index);
                    index += 5;
                    waypointDjiActionList.add(action);
                }
            }
            WaypointDjiLength = EFLINK_MSG_LENGTH + ActionCount * 5;
        } catch (Exception e) {
            LogUtil.logError("解析单个航点数据失败：" + e.getMessage());
        }
    }

    public int getWpNo() {
        return WpNo;
    }

    public void setWpNo(int wpNo) {
        WpNo = wpNo;
    }

    public int getActionCount() {
        return ActionCount;
    }

    public void setActionCount(int actionCount) {
        ActionCount = actionCount;
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

    public int getSpeed() {
        return Speed;
    }

    public void setSpeed(int speed) {
        Speed = speed;
    }

    public int getMaxSpeed() {
        return MaxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        MaxSpeed = maxSpeed;
    }

    public int getUavTurnMode() {
        return UavTurnMode;
    }

    public void setUavTurnMode(int uavTurnMode) {
        UavTurnMode = uavTurnMode;
    }

    public float getUavYaw() {
        return UavYaw;
    }

    public void setUavYaw(float uavYaw) {
        UavYaw = uavYaw;
    }

    public float getGimbalPitch() {
        return GimbalPitch;
    }

    public void setGimbalPitch(float gimbalPitch) {
        GimbalPitch = gimbalPitch;
    }

    public float getGimbalRoll() {
        return GimbalRoll;
    }

    public void setGimbalRoll(float gimbalRoll) {
        GimbalRoll = gimbalRoll;
    }

    public float getGimbalYaw() {
        return GimbalYaw;
    }

    public void setGimbalYaw(float gimbalYaw) {
        GimbalYaw = gimbalYaw;
    }

    public int getShootPhotoTimeInterval() {
        return ShootPhotoTimeInterval;
    }

    public void setShootPhotoTimeInterval(int shootPhotoTimeInterval) {
        ShootPhotoTimeInterval = shootPhotoTimeInterval;
    }

    public int getShootPhotoDistanceInterval() {
        return ShootPhotoDistanceInterval;
    }

    public void setShootPhotoDistanceInterval(int shootPhotoDistanceInterval) {
        ShootPhotoDistanceInterval = shootPhotoDistanceInterval;
    }

    public int getActionRepeatTimes() {
        return ActionRepeatTimes;
    }

    public void setActionRepeatTimes(int actionRepeatTimes) {
        ActionRepeatTimes = actionRepeatTimes;
    }

    public int getIsUsingWaypointAutoFlightSpeed() {
        return isUsingWaypointAutoFlightSpeed;
    }

    public void setIsUsingWaypointAutoFlightSpeed(int isUsingWaypointAutoFlightSpeed) {
        this.isUsingWaypointAutoFlightSpeed = isUsingWaypointAutoFlightSpeed;
    }

    public int getIsUsingWaypointMaxFlightSpeed() {
        return isUsingWaypointMaxFlightSpeed;
    }

    public void setIsUsingWaypointMaxFlightSpeed(int isUsingWaypointMaxFlightSpeed) {
        this.isUsingWaypointMaxFlightSpeed = isUsingWaypointMaxFlightSpeed;
    }

    public int getPointOfInterestLat() {
        return PointOfInterestLat;
    }

    public void setPointOfInterestLat(int pointOfInterestLat) {
        PointOfInterestLat = pointOfInterestLat;
    }

    public int getPointOfInterestLng() {
        return PointOfInterestLng;
    }

    public void setPointOfInterestLng(int pointOfInterestLng) {
        PointOfInterestLng = pointOfInterestLng;
    }

    public int getPointOfInterestAlt() {
        return PointOfInterestAlt;
    }

    public void setPointOfInterestAlt(int pointOfInterestAlt) {
        PointOfInterestAlt = pointOfInterestAlt;
    }

    public List<WaypointDjiAction> getWaypointDjiActionList() {
        return waypointDjiActionList;
    }

    public void setWaypointDjiActionList(List<WaypointDjiAction> waypointDjiActionList) {
        this.waypointDjiActionList = waypointDjiActionList;
    }
}
