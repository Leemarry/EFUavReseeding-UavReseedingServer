package com.bear.reseeding.eflink;

import com.bear.reseeding.utils.BytesUtil;
import com.bear.reseeding.utils.LogUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

/**
 * 大疆单个航点内容
 */
public class WaypointEf implements Serializable {
    /**
     * 内容段数据长度，最小为5，变长
     */
    private final int EFLINK_MSG_LENGTH = 32;

    //当前包总长度
    public int WaypointEfLength = EFLINK_MSG_LENGTH;

    private int WpNo;
    private int Command;
    private int Lat;
    private int Lng;
    private int AltRel;
    private float Parm1;
    private float Parm2;
    private float Parm3;
    private float Parm4;

    public byte[] packet() {
        ByteBuffer buffer = ByteBuffer.allocate(EFLINK_MSG_LENGTH);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(BytesUtil.short2Bytes(WpNo));
        buffer.put(BytesUtil.short2Bytes(Command));
        buffer.put(BytesUtil.int2Bytes(Lat));
        buffer.put(BytesUtil.int2Bytes(Lng));
        buffer.put(BytesUtil.int2Bytes(AltRel));
        buffer.put(BytesUtil.float2Bytes(Parm1));
        buffer.put(BytesUtil.float2Bytes(Parm2));
        buffer.put(BytesUtil.float2Bytes(Parm3));
        buffer.put(BytesUtil.float2Bytes(Parm4));
        return buffer.array();
    }

    public void unpacket(byte[] packet, int index) {
        try {
            WpNo = BytesUtil.bytes2UShort(packet, index);
            index += 2;
            Command = BytesUtil.bytes2Short(packet, index);
            index += 2;
            Lat = BytesUtil.bytes2Int(packet, index);
            index += 4;
            Lng = BytesUtil.bytes2Int(packet, index);
            index += 4;
            AltRel = BytesUtil.bytes2Int(packet, index);
            index += 4;
            Parm1 = BytesUtil.bytes2Float(packet,index);
            index +=4;

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

    public int getCommand() {
        return Command;
    }

    public void setCommand(int command) {
        Command = command;
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

    public float getParm1() {
        return Parm1;
    }

    public void setParm1(float parm1) {
        Parm1 = parm1;
    }

    public float getParm2() {
        return Parm2;
    }

    public void setParm2(float parm2) {
        Parm2 = parm2;
    }

    public float getParm3() {
        return Parm3;
    }

    public void setParm3(float parm3) {
        Parm3 = parm3;
    }

    public float getParm4() {
        return Parm4;
    }

    public void setParm4(float parm4) {
        Parm4 = parm4;
    }
}
