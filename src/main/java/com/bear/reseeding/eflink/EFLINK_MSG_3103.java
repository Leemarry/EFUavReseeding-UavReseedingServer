package com.bear.reseeding.eflink;


import com.bear.reseeding.utils.BytesUtil;
import com.bear.reseeding.utils.LogUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * 上传航线航点
 */
public class EFLINK_MSG_3103 implements Serializable {

    /**
     * 消息ID
     */
    public final int EFLINK_MSG_ID = 3103;
    /**
     * 内容段数据长度，最小为5，变长
     */
    private final int EFLINK_MSG_LENGTH = 10;

    /**
     * 刷新数据时间
     */
    private long refreshTime = System.currentTimeMillis() - 60000;
    /**
     * 标识
     */
    private int Tag;
    /**
     * 任务类型，默认0为翼飞任务，1为大疆任务
     */
    private int MissionType;
    /**
     * 任务航点总数
     */
    private int WpsCount;
    /**
     * 当前包的航点总数量
     */
    private int WpCount;
    /**
     * 分包总数量
     */
    private int PacketCount;
    /**
     * 当前包索引，从0开始
     */
    private int PacketIndex;
    /**
     * 大疆 多个航点
     */
    private List<WaypointDji> waypointDjiList;
    /**
     * 大疆 多个航点
     */
    private List<WaypointDjiV2> waypointDjiListV2;
    /**
     * 翼飞 多个航点
     */
    private List<WaypointEf> waypointEfList;

    @Override
    public String toString() {
        StringBuilder waypointDjiListStr = new StringBuilder("空");
        if (waypointDjiList != null && waypointDjiList.size() > 0) {
            waypointDjiListStr.setLength(0);
            for (int i = 0; i < waypointDjiList.size(); i++) {
                waypointDjiListStr.append(waypointDjiList.get(i).toString());
                waypointDjiListStr.append(" ");
            }
        }
        return "EFLINK_MSG_3103{" +
                "EFLINK_MSG_ID=" + EFLINK_MSG_ID +
                ", EFLINK_MSG_LENGTH=" + EFLINK_MSG_LENGTH +
                ", refreshTime=" + refreshTime +
                ", Tag=" + Tag +
                ", MissionType=" + MissionType +
                ", WpsCount=" + WpsCount +
                ", WpCount=" + WpCount +
                ", PacketCount=" + PacketCount +
                ", PacketIndex=" + PacketIndex +
                ", waypointDjiList=" + waypointDjiListStr +
                ", waypointEfList=" + waypointEfList +
                '}';
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
                waypointEfList = new ArrayList<>();
                waypointDjiList = new ArrayList<>();
                if (this.WpCount > 0) {
                    for (int i = 0; i < WpCount; i++) {
                        switch (this.MissionType) {
                            case 0: // 翼飞任务
                                WaypointEf waypointEf = new WaypointEf();
                                waypointEf.unpacket(packet, index);
                                waypointEfList.add(waypointEf);
                                index += waypointEf.WaypointEfLength;
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
            } catch (Exception e) {
                LogUtil.logError("解析航线航点数据包数据失败：" + e.getMessage());
            }
        }
    }

    public byte[] packet() {
        ArrayList<Byte> list = new ArrayList<>();
        WpCount = 0;
        switch (this.MissionType) {
            case 0: //翼飞任务
                if (waypointEfList != null && waypointEfList.size() > 0) {
                    WpCount = waypointEfList.size();
                    for (WaypointEf waypoint : waypointEfList) {
                        byte[] temp = waypoint.packet();
                        for (byte b : temp) {
                            list.add(b);
                        }
                    }
                }
                break;
            case 1: // 大疆任务
                if (waypointDjiList != null && waypointDjiList.size() > 0) {
                    WpCount = waypointDjiList.size();
                    for (WaypointDji waypoint : waypointDjiList) {
                        byte[] temp = waypoint.packet();
                        for (byte b : temp) {
                            list.add(b);
                        }
                    }
                } else if (waypointDjiListV2 != null && waypointDjiListV2.size() > 0) {
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


    public List<WaypointDji> getWaypointDjiList() {
        return waypointDjiList;
    }

    public void setWaypointDjiList(List<WaypointDji> waypointDjiList) {
        this.waypointDjiList = waypointDjiList;
    }

    public List<WaypointDjiV2> getWaypointDjiListV2() {
        return waypointDjiListV2;
    }

    public void setWaypointDjiListV2(List<WaypointDjiV2> waypointDjiListV2) {
        this.waypointDjiListV2 = waypointDjiListV2;
    }

    public int getWpCount() {
        return WpCount;
    }

    public void setWpCount(int wpCount) {
        WpCount = wpCount;
    }

    public List<WaypointEf> getWaypointEfList() {
        return waypointEfList;
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

    public void setWaypointEfList(List<WaypointEf> waypointEfList) {
        this.waypointEfList = waypointEfList;
    }
}
