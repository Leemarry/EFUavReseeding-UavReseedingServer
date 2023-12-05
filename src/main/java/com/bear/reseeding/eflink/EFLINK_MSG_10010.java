package com.bear.reseeding.eflink;

import com.alibaba.fastjson.JSONObject;
import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 客户端实时照片推送到服务器后，服务器通过该MSGID通过websocket推送进度给前台显示实时照片，推送内容为Result，数据存放于Result-Data中。
 * Data数据字段如下 (#10010)
 */
public class EFLINK_MSG_10010 implements Serializable {

    public final int EFLINK_MSG_ID = 10010;

    /**
     * 内容段数据长度
     */
    public final int EFLINK_MSG_LENGTH = 18;

    String uavId;
    long time;
    String url;
    double lat;
    double lng;
    double alt;
    double altAbs;
    double yaw;

    public EFLINK_MSG_10010() {
    }

    @Override
    public String toString() {
        return "EFLINK_MSG_10010{" +
                "uavId='" + uavId + '\'' +
                ", time=" + time +
                ", url='" + url + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", alt=" + alt +
                ", altAbs=" + altAbs +
                ", yaw=" + yaw +
                '}';
    }

    public String getUavId() {
        return uavId;
    }

    public void setUavId(String uavId) {
        this.uavId = uavId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public double getAlt() {
        return alt;
    }

    public void setAlt(double alt) {
        this.alt = alt;
    }

    public double getAltAbs() {
        return altAbs;
    }

    public void setAltAbs(double altAbs) {
        this.altAbs = altAbs;
    }

    public double getYaw() {
        return yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }
}
