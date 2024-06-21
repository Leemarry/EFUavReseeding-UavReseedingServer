package com.bear.reseeding.eflink;

/**
 * 草原补种项目——确认上传
 *
 * @author N.
 * @date 2024-03-14 13:53
 */
import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class EFLINK_MSG_19015 implements Serializable {
    public final int EFLINK_MSG_ID = 19015;
    /**
     * 内容段数据长度
     */
    public final int EFLINK_MSG_LENGTH = 14;
    /**
     * 版本
     */
    byte VersionInside;
    /**
     * 标识
     */
    byte Tag;
    /**
     * 当前纬度
     */
    double Lat;
    /**
     * 当前经度
     */
    double Lng;
    /**
     * 当前相对高度
     */
    double AltRel;
    /**
     *已播种子数
     */
    int   SeededCount;
    /**
     * 种子数量
     */
    int SeedCount;
    /**
     * 已播点的唯一标号
     */
    int WpIndex;


    public void InitPacket(byte[] packet, int index) {
        try {
            VersionInside = (byte) (packet[index] & 0xFF);
            index += 1;
            Tag = (byte) (packet[index] & 0xFF);
            index++;
            Lat = (BytesUtil.bytes2Int(packet, index) / 1e7d); //lat
            index += 4;
            Lng = (BytesUtil.bytes2Int(packet, index) / 1e7d); //lng
            index += 4;
            AltRel = (BytesUtil.bytes2Int(packet, index) / 100d); //altabs
            index += 4;
            SeedCount = BytesUtil.bytes2UShort(packet, index);
            index += 2;
          SeededCount = BytesUtil.bytes2UShort(packet, index);
            index += 2;
            WpIndex =BytesUtil.bytes2UShort(packet, index);
            index += 2;
        } catch (Exception ignored) {

        }
    }
    @Override
    public String toString() {
        return "EFLINK_MSG_19015{" +
                "EFLINK_MSG_ID=" + EFLINK_MSG_ID +
                ", Lat=" + Lat +
                ", Lng=" + Lng +
                ", AltRel=" + AltRel +
                ", SeededCount=" + SeededCount +
                ", SeedCount=" + SeedCount +
                ", WpIndex=" + WpIndex +
                '}';
    }


    public byte getVersionInside() {
        return VersionInside;
    }

    public void setVersionInside(byte versionInside) {
        VersionInside = versionInside;
    }

    public byte getTag() {
        return Tag;
    }

    public void setTag(byte tag) {
        Tag = tag;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLng() {
        return Lng;
    }

    public void setLng(double lng) {
        Lng = lng;
    }

    public double getAltRel() {
        return AltRel;
    }

    public void setAltRel(double altRel) {
        AltRel = altRel;
    }

    public int getSeededCount() {
        return SeededCount;
    }

    public void setSeededCount(int seededCount) {
        SeededCount = seededCount;
    }

    public int getSeedCount() {
        return SeedCount;
    }

    public void setSeedCount(int seedCount) {
        SeedCount = seedCount;
    }

    public int getWpIndex() {
        return WpIndex;
    }

    public void setWpIndex(int wpIndex) {
        WpIndex = wpIndex;
    }
}

