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
public class EFLINK_MSG_3109 implements Serializable {

    /**
     * 消息ID
     */
    public final int EFLINK_MSG_ID = 3109;
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
    private List<WaypointDji> waypointDjiList;

    public boolean isLastPacket() {
        // 判断是否是最后一个包
        return (PacketIndex + 1) * WpCount >= WpsCount;
    }

    public EFLINK_MSG_3109() {
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
                waypointDjiList = new ArrayList<>();
                if (this.WpCount > 0) {
                    for (int i = 0; i < WpCount; i++) {
                        switch (this.MissionType) {
                            case 0: // 翼飞任务
                                break;
                            case 1: // 大疆任务
                                WaypointDji waypointDji = new WaypointDji();
                                waypointDji.unpacket(packet, index);
                                waypointDjiList.add(waypointDji);
                                index += waypointDji.WaypointDjiLength;
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
                if (waypointDjiList != null && waypointDjiList.size() > 0) {
                    WpCount = waypointDjiList.size();
                    for (WaypointDji waypoint : waypointDjiList) {
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
        for (int i = 0; i < list.size(); i++) {
            buffer.put(list.get(i));
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

    public List<WaypointDji> getWaypointDjiList() {
        return waypointDjiList;
    }

    public void setWaypointDjiList(List<WaypointDji> waypointDjiList) {
        this.waypointDjiList = waypointDjiList;
    }
}
