package com.bear.reseeding.eflink;


import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 请求下载航线
 */
public class EFLINK_MSG_3108 implements Serializable {

    /**
     * 消息ID
     */
    public final int EFLINK_MSG_ID = 3108;
    /**
     * 内容段数据长度
     */
    public final int EFLINK_MSG_LENGTH = 14;

    /**
     * 刷新数据时间
     */
    private long refreshTime = System.currentTimeMillis() - 60000;

    private int Tag;
    /**
     * 任务类型，默认0为翼飞任务，1为大疆任务
     */
    private int MissionType;
    private int WpsCount;
    private int Speed;
    private int MaxSpeed;
    private int MissionOnRCSignalLostEnabled;
    private int MissionFinishedAction;
    private int MissionFlightPathMode;
    private int MissionGotoWaypointMode;
    private int MissionHeadingMode;
    private int MissionRepeatTimes;


    public void unPacket(byte[] packet, int index) {
        if (packet != null && packet.length > 12) {
            refreshTime = System.currentTimeMillis();
            try {
                this.Tag = packet[index] & 0xff;
                index++;
                this.MissionType = packet[index] & 0xff;
                index++;
                this.WpsCount = BytesUtil.bytes2UShort(packet, index);
                index += 2;
                this.Speed = BytesUtil.bytes2Short(packet, index);
                index += 2;
                this.MaxSpeed = BytesUtil.bytes2Short(packet, index);
                index += 2;
                this.MissionOnRCSignalLostEnabled = packet[index] & 0xff;
                index++;
                this.MissionFinishedAction = packet[index] & 0xff;
                index++;
                this.MissionFlightPathMode = packet[index] & 0xff;
                index++;
                this.MissionGotoWaypointMode = packet[index] & 0xff;
                index++;
                this.MissionHeadingMode = packet[index] & 0xff;
                index++;
                this.MissionRepeatTimes = packet[index] & 0xff;
                index++;

            } catch (Exception ignored) {
            }
        }
    }

    public byte[] packet() {
        ByteBuffer buffer = ByteBuffer.allocate(EFLINK_MSG_LENGTH);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) Tag);
        buffer.put((byte) MissionType);
        buffer.put(BytesUtil.short2Bytes(WpsCount));
        buffer.put(BytesUtil.short2Bytes(Speed));
        buffer.put(BytesUtil.short2Bytes(MaxSpeed));
        buffer.put((byte) MissionOnRCSignalLostEnabled);
        buffer.put((byte) MissionFinishedAction);
        buffer.put((byte) MissionFlightPathMode);
        buffer.put((byte) MissionGotoWaypointMode);
        buffer.put((byte) MissionHeadingMode);
        buffer.put((byte) MissionRepeatTimes);
        return buffer.array();
    }


    public int getTag() {
        return Tag;
    }

    public void setTag(int tag) {
        Tag = tag;
    }

    public int getMissionType() {
        return MissionType;
    }

    public void setMissionType(int missionType) {
        MissionType = missionType;
    }

    public int getWpsCount() {
        return WpsCount;
    }

    public void setWpsCount(int wpsCount) {
        WpsCount = wpsCount;
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

    public int getMissionOnRCSignalLostEnabled() {
        return MissionOnRCSignalLostEnabled;
    }

    public void setMissionOnRCSignalLostEnabled(int missionOnRCSignalLostEnabled) {
        MissionOnRCSignalLostEnabled = missionOnRCSignalLostEnabled;
    }

    public int getMissionFinishedAction() {
        return MissionFinishedAction;
    }

    public void setMissionFinishedAction(int missionFinishedAction) {
        MissionFinishedAction = missionFinishedAction;
    }

    public int getMissionFlightPathMode() {
        return MissionFlightPathMode;
    }

    public void setMissionFlightPathMode(int missionFlightPathMode) {
        MissionFlightPathMode = missionFlightPathMode;
    }

    public int getMissionGotoWaypointMode() {
        return MissionGotoWaypointMode;
    }

    public void setMissionGotoWaypointMode(int missionGotoWaypointMode) {
        MissionGotoWaypointMode = missionGotoWaypointMode;
    }

    public int getMissionHeadingMode() {
        return MissionHeadingMode;
    }

    public void setMissionHeadingMode(int missionHeadingMode) {
        MissionHeadingMode = missionHeadingMode;
    }

    public int getMissionRepeatTimes() {
        return MissionRepeatTimes;
    }

    public void setMissionRepeatTimes(int missionRepeatTimes) {
        MissionRepeatTimes = missionRepeatTimes;
    }

    public void unPacket(byte[] packe) {
        unPacket(packe, 12);
    }

}
