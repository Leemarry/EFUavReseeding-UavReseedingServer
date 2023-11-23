package com.bear.reseeding.eflink;

import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * 停机坪心跳包
 *
 * @Auther: bear
 * @Date: 2021/12/30 18:20
 * @Description: null
 */
public class EFLINK_MSG_2300 implements Serializable {

    public final int EFLINK_MSG_ID = 2300;

    private long hiveId;
    private Timestamp dataDate;
    private double lat;
    private double lng;
    private double alt;
    private double altabs;
    private double gpsStatus;
    private long sateCount;
    private int hiveSwitchStatus; //0：未知，1：已开启，2：已关闭，3：开启中，4：关闭中
    private int doorSwitchStatus; //0：未知，1：已开启，2：已关闭，3：开启中，4：关闭中
    private int upDownSwitchStatus;//0：未知，1：已开启，2：已关闭，3：开启中，4：关闭中
    private int pushSwitchStatus;//0：未知，1：已开启，2：已关闭，3：开启中，4：关闭中
    private int chargeStatus; //0：未知，1：未充电，2：充电中，3：已充满
    private long runTime;//总运行时长	单位：秒
    private int useTime;//总开启次数	单位：次
    private int limitSwitchHealth; //16个限位设备的健康状态	对应16个限位设备的健康状态
    private int hiveHealth; //机巢的运行状态	按位表示，0表示正常，1表示异常，见描述EF_HIVE_HEALTH

    private int backup1;
    private int backup2;
    private int backup3;

    private double chargeVoltage;//充电电压.mv转v
    private double chargeCurrent;//充电电流,ma转a
    private int chargePert;//充电百分比
    private double chargeNeedTime;//剩余充满时间,s转min

    private double hiveTemperature; // 机巢内部温度
    private double hiveHumidity; // 机巢内部湿度


    public long getHiveId() {
        return hiveId;
    }

    public void setHiveId(long hiveId) {
        this.hiveId = hiveId;
    }

    public Timestamp getDataDate() {
        return dataDate;
    }

    public void setDataDate(Timestamp dataDate) {
        this.dataDate = dataDate;
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

    public double getAltabs() {
        return altabs;
    }

    public void setAltabs(double altabs) {
        this.altabs = altabs;
    }

    public double getGpsStatus() {
        return gpsStatus;
    }

    public void setGpsStatus(double gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    public long getSateCount() {
        return sateCount;
    }

    public void setSateCount(long sateCount) {
        this.sateCount = sateCount;
    }

    public int getHiveSwitchStatus() {
        return hiveSwitchStatus;
    }

    public void setHiveSwitchStatus(int hiveSwitchStatus) {
        this.hiveSwitchStatus = hiveSwitchStatus;
    }

    public int getDoorSwitchStatus() {
        return doorSwitchStatus;
    }

    public void setDoorSwitchStatus(int doorSwitchStatus) {
        this.doorSwitchStatus = doorSwitchStatus;
    }

    public int getUpDownSwitchStatus() {
        return upDownSwitchStatus;
    }

    public void setUpDownSwitchStatus(int upDownSwitchStatus) {
        this.upDownSwitchStatus = upDownSwitchStatus;
    }

    public int getPushSwitchStatus() {
        return pushSwitchStatus;
    }

    public void setPushSwitchStatus(int pushSwitchStatus) {
        this.pushSwitchStatus = pushSwitchStatus;
    }

    public int getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(int chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public long getRunTime() {
        return runTime;
    }

    public void setRunTime(long runTime) {
        this.runTime = runTime;
    }

    public int getUseTime() {
        return useTime;
    }

    public void setUseTime(int useTime) {
        this.useTime = useTime;
    }

    public int getLimitSwitchHealth() {
        return limitSwitchHealth;
    }

    public void setLimitSwitchHealth(int limitSwitchHealth) {
        this.limitSwitchHealth = limitSwitchHealth;
    }

    public int getHiveHealth() {
        return hiveHealth;
    }

    public void setHiveHealth(int hiveHealth) {
        this.hiveHealth = hiveHealth;
    }

    public double getHiveTemperature() {
        return hiveTemperature;
    }

    public void setHiveTemperature(double hiveTemperature) {
        this.hiveTemperature = hiveTemperature;
    }

    public double getHiveHumidity() {
        return hiveHumidity;
    }

    public void setHiveHumidity(double hiveHumidity) {
        this.hiveHumidity = hiveHumidity;
    }

    public int getBackup1() {
        return backup1;
    }

    public void setBackup1(int backup1) {
        this.backup1 = backup1;
    }

    public int getBackup2() {
        return backup2;
    }

    public void setBackup2(int backup2) {
        this.backup2 = backup2;
    }

    public int getBackup3() {
        return backup3;
    }

    public void setBackup3(int backup3) {
        this.backup3 = backup3;
    }

    public double getChargeVoltage() {
        return chargeVoltage;
    }

    public void setChargeVoltage(double chargeVoltage) {
        this.chargeVoltage = chargeVoltage;
    }

    public double getChargeCurrent() {
        return chargeCurrent;
    }

    public void setChargeCurrent(double chargeCurrent) {
        this.chargeCurrent = chargeCurrent;
    }

    public int getChargePert() {
        return chargePert;
    }

    public void setChargePert(int chargePert) {
        this.chargePert = chargePert;
    }

    public double getChargeNeedTime() {
        return chargeNeedTime;
    }

    public void setChargeNeedTime(double chargeNeedTime) {
        this.chargeNeedTime = chargeNeedTime;
    }

    public void InitPacket(byte[] packet, int Index) {
        try {
            Timestamp time = new Timestamp(System.currentTimeMillis());//获取系统当前时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String timeStr = df.format(time);
            this.dataDate = Timestamp.valueOf(timeStr);
            hiveId = BytesUtil.bytes2UShort(packet, Index);  //机巢编号 1000开始的
            Index += 2;
            lat = (BytesUtil.bytes2Int(packet, Index) / 1e7d);
            Index += 4;
            lng = (BytesUtil.bytes2Int(packet, Index) / 1e7d);
            Index += 4;
            alt = (BytesUtil.bytes2Int(packet, Index) / 100d);
            Index += 4;
            altabs = (BytesUtil.bytes2Int(packet, Index) / 100d);
            Index += 4;
            gpsStatus = (packet[Index] & 0xFF);  //定位状态
            Index += 1;
            setSateCount(packet[Index] & 0xFF);  //卫星颗数
            Index += 1;
            setHiveSwitchStatus(packet[Index] & 0xFF);  //机巢状态, 0：未知，1：已开启，2：已关闭，3：开启中，4：关闭中
            Index += 1;
            setDoorSwitchStatus(packet[Index] & 0xFF); //舱门状态
            Index += 1;
            setUpDownSwitchStatus(packet[Index] & 0xFF); //升降状态
            Index += 1;
            setPushSwitchStatus(packet[Index] & 0xFF); //推杆状态
            Index += 1;
            setChargeStatus(packet[Index] & 0xFF); //充电状态	0：未知，1：未充电，2：充电中，3：已充满
            Index += 1;
            setRunTime(BytesUtil.bytes2UInt(packet, Index)); //总运行时长	单位：秒
            Index += 4;
            useTime = (BytesUtil.bytes2UShort(packet, Index)); //总开启次数	单位：次
            Index += 2;
//            hiveTemperature = (packet[Index] & 0xFF) / 10d; // 机巢内部温度
//            Index += 1;
            backup1 = (packet[Index] & 0xFF); //预留
            Index += 1;
            backup2 = (packet[Index] & 0xFF); //预留
            Index += 1;
            backup3 = (packet[Index] & 0xFF); //预留
            Index += 1;

            limitSwitchHealth = BytesUtil.bytes2Short(packet, Index); //INT16,按位表示，对应16个限位设备的健康状态
            Index += 2;
            hiveHealth = packet[Index] & 0xFF;//机巢的运行状态,BIT位表示，见枚举
            Index += 1;
            if (packet.length > Index + 7) {
                chargeVoltage = BytesUtil.bytes2UShort(packet, Index) / 1000d;//充电电压 V
                Index += 2;
                chargeCurrent = BytesUtil.bytes2UShort(packet, Index) / 1000d;//充电电流 A
                Index += 2;
                chargePert = (packet[Index] & 0xFF); //充电百分比
                Index += 1;
                chargeNeedTime = BytesUtil.bytes2UShort(packet, Index) / 60d;//剩余充满时间 分钟
                Index += 2;
                if (packet.length > Index + 4) {
                    hiveTemperature = BytesUtil.bytes2Short(packet, Index) / 10d;//温度
                    Index += 2;
                    hiveHumidity = BytesUtil.bytes2Short(packet, Index) / 10d;//湿度
                    Index += 2;
                }
            }
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

    @Override
    public String toString() {
        return "EFLINK_MSG_2300{" +
                "EFLINK_MSG_ID=" + EFLINK_MSG_ID +
                ", hiveId=" + hiveId +
                ", dataDate=" + dataDate +
                ", lat=" + lat +
                ", lng=" + lng +
                ", alt=" + alt +
                ", altabs=" + altabs +
                ", gpsStatus=" + gpsStatus +
                ", sateCount=" + sateCount +
                ", hiveSwitchStatus=" + hiveSwitchStatus +
                ", doorSwitchStatus=" + doorSwitchStatus +
                ", upDownSwitchStatus=" + upDownSwitchStatus +
                ", pushSwitchStatus=" + pushSwitchStatus +
                ", chargeStatus=" + chargeStatus +
                ", runTime=" + runTime +
                ", useTime=" + useTime +
                ", limitSwitchHealth=" + limitSwitchHealth +
                ", hiveHealth=" + hiveHealth +
                ", backup1=" + backup1 +
                ", backup2=" + backup2 +
                ", backup3=" + backup3 +
                ", chargeVoltage=" + chargeVoltage +
                ", chargeCurrent=" + chargeCurrent +
                ", chargePert=" + chargePert +
                ", chargeNeedTime=" + chargeNeedTime +
                ", hiveTemperature=" + hiveTemperature +
                ", hiveHumidity=" + hiveHumidity +
                '}';
    }
}
