package com.bear.reseeding.eflink;


import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * 航线—下载航线航点(#3109)
 */
public class EFLINK_MSG_3119 implements Serializable {

    /**
     * 消息ID
     */
    public final int EFLINK_MSG_ID = 3119;
    /**
     * 内容段数据长度
     */
    private final int EFLINK_MSG_LENGTH = 10;

    /**
     * 刷新数据时间
     */
    private long refreshTime = System.currentTimeMillis() - 60000;


    private int Tag;
    private int MissionType;
    private int WpsCount;
    private int WpCount;
    private int PacketCount;
    private int PacketIndex;
    /**
     * 大疆 多个航点
     */
    private List<WaypointDjiV2> waypointDjiListV2;

    public EFLINK_MSG_3119() {
    }

    public void unpacket(byte[] packet) {
        unpacket(packet, 12);
    }

    public void unpacket(byte[] packet, int index) {
        if (packet != null && packet.length > 12) {
            refreshTime = System.currentTimeMillis();
            try {
                this.Tag = packet[index] & 0xff;
                index++;
                this.MissionType = packet[index] & 0xff;
                index++;
                this.WpsCount = BytesUtil.bytes2UShort(packet, index);
                index += 2;
                this.WpCount = BytesUtil.bytes2UShort(packet, index);
                index += 2;
                this.PacketCount = BytesUtil.bytes2UShort(packet, index);
                index += 2;
                this.PacketIndex = BytesUtil.bytes2UShort(packet, index);
                index += 2;
                waypointDjiListV2 = new ArrayList<>();
                if (this.WpCount > 0) {
                    for (int i = 0; i < WpCount; i++) {
                        switch (this.MissionType) {
                            case 0: // 翼飞任务
                                break;
                            case 1: // 大疆任务
                                WaypointDjiV2 waypointDjiV2 = new WaypointDjiV2();
                                waypointDjiV2.unpacket(packet, index);
                                waypointDjiListV2.add(waypointDjiV2);
                                index += waypointDjiV2.WaypointDjiLength;
                                break;
                        }
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }

    public byte[] packet() {
        ArrayList<Byte> list = new ArrayList<>();
        WpCount = 0;
        switch (this.MissionType) {
            case 1: // 大疆任务
                if (waypointDjiListV2 != null && waypointDjiListV2.size() > 0) {
                    WpCount = waypointDjiListV2.size();
                    for (WaypointDjiV2 waypoint : waypointDjiListV2) {
                        byte[] temp = waypoint.packet();
                        for (byte b : temp) {
                            list.add(b);
                        }
                    }
                }
                break;
        }

        ByteBuffer buffer = ByteBuffer.allocate(EFLINK_MSG_LENGTH + list.size());
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) Tag);
        buffer.put((byte) MissionType);
        buffer.put(BytesUtil.short2Bytes(WpsCount));
        buffer.put(BytesUtil.short2Bytes(WpCount));
        buffer.put(BytesUtil.short2Bytes(PacketCount));
        buffer.put(BytesUtil.short2Bytes(PacketIndex));
        for (Byte aByte : list) {
            buffer.put(aByte);
        }
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

    public int getWpCount() {
        return WpCount;
    }

    public void setWpCount(int wpCount) {
        WpCount = wpCount;
    }

    public int getPacketCount() {
        return PacketCount;
    }

    public void setPacketCount(int packetCount) {
        PacketCount = packetCount;
    }

    public int getPacketIndex() {
        return PacketIndex;
    }

    public void setPacketIndex(int packetIndex) {
        PacketIndex = packetIndex;
    }

    public List<WaypointDjiV2> getWaypointDjiListV2() {
        return waypointDjiListV2;
    }

    public void setWaypointDjiListV2(List<WaypointDjiV2> waypointDjiListV2) {
        this.waypointDjiListV2 = waypointDjiListV2;
    }
}
