package com.bear.reseeding.eflink;


import com.bear.reseeding.utils.BytesUtil;
import com.bear.reseeding.utils.LogUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * 大疆单个航点内容
 */
public class WaypointDji implements Serializable {
    /**
     * 内容段数据长度，最小为5，变长
     */
    public final int EFLINK_MSG_LENGTH = 21;

    //当前包总长度
    public int WaypointDjiLength = EFLINK_MSG_LENGTH;

    private int WpNo;
    private int Lat;
    private int Lng;
    private int AltRel;
    private int AltAbs;
    private int Speed;
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
        return "WaypointDji{" +
                "EFLINK_MSG_LENGTH=" + EFLINK_MSG_LENGTH +
                ", WpNo=" + WpNo +
                ", Lat=" + Lat +
                ", Lng=" + Lng +
                ", AltRel=" + AltRel +
                ", AltAbs=" + AltAbs +
                ", Speed=" + Speed +
                ", ActionCount=" + ActionCount +
                ", waypointDjiActionList=" + waypointDjiActionListStr +
                '}';
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
            AltAbs = BytesUtil.bytes2Short(packet, index);
            index += 2;
            Speed = BytesUtil.bytes2Short(packet, index);
            //index += 2;
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
        buffer.put((byte) ActionCount);
        for (int i = 0; i < list.size(); i++) {
            buffer.put(list.get(i));
        }
        return buffer.array();
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

    public List<WaypointDjiAction> getWaypointDjiActionList() {
        return waypointDjiActionList;
    }

    public void setWaypointDjiActionList(List<WaypointDjiAction> waypointDjiActionList) {
        this.waypointDjiActionList = waypointDjiActionList;
    }
}
