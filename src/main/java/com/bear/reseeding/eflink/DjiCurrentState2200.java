package com.bear.reseeding.eflink;

import com.bear.reseeding.eflink.enums.FlightMode;
import com.bear.reseeding.eflink.enums.GPSSignalLevel;
import com.bear.reseeding.utils.BytesUtil;
import com.bear.reseeding.utils.ConstantUtil;
import com.bear.reseeding.utils.CrcUtil;

public class DjiCurrentState2200 {
    public DjiCurrentState2200() {
    }

    private String uavid;
    private int Status;
    private int linkAirUpload;
    private int linkAirDownload;

    private int battPert;
    private double battVoltage;
    private double battCurrent;

    private double homeLatitude;
    private double homeLongitude;
    private FlightMode flightMode;

    private double roll;
    private double pitch;
    private double yaw;
    private double latitude;
    private double longitude;
    private double alt;
    private double altabs;

    private double xSpeed;
    private double ySpeed;
    private double zSpeed;
    private double xySpeed;

    private int sateCount;
    private GPSSignalLevel gpsSignalStatus;
    private int aremd;

    public void unPacket(byte[] packet, int index) {
        latitude = (BytesUtil.bytes2Int(packet, index) / 1e7d); //lat
        index += 4;
        longitude = (BytesUtil.bytes2Int(packet, index) / 1e7d); //lng
        index += 4;
        alt = (BytesUtil.bytes2Int(packet, index) / 100d); //alt
        index += 4;
        altabs = (BytesUtil.bytes2Int(packet, index) / 100d); //altabs
        index += 4;
        xySpeed = (BytesUtil.bytes2Short(packet, index) / 100d); //speed xy
        index += 2;
        zSpeed = (BytesUtil.bytes2Short(packet, index) / 100d); //speed z
        index += 2;
        int enumvalue = packet[index] & 0xFF;
        flightMode = (FlightMode.valueIndexOf(enumvalue)); //FlightMode
        index++;
        roll = (BytesUtil.bytes2Float(packet, index) * ConstantUtil.rad2deg); //roll
        index += 4;
        pitch = (BytesUtil.bytes2Float(packet, index) * ConstantUtil.rad2deg); //pitch
        index += 4;
        yaw = (BytesUtil.bytes2Float(packet, index) * ConstantUtil.rad2deg); //yaw
        index += 4;
        battPert = (packet[index] & 0xFF); //电压百分比
        index++;
        linkAirDownload = (packet[index] & 0xFF); //下行信号强度
        index++;
        linkAirUpload = (packet[index] & 0xFF); //上行信号强度
        index++;
        enumvalue = packet[index] & 0xFF;
        gpsSignalStatus = (GPSSignalLevel.valueIndexOf(enumvalue)); //定位状态
        index++;
        sateCount = (packet[index] & 0xFF); //卫星颗数
        index++;
        Status = (packet[index] & 0xFF); //系统运行状态
        index++;
        aremd = (packet[index] & 0xFF); //解锁状态
    }

    public String getUavid() {
        return uavid;
    }

    public void setUavid(String uavid) {
        this.uavid = uavid;
    }

    public int getLinkAirUpload() {
        return linkAirUpload;
    }

    public void setLinkAirUpload(int linkAirUpload) {
        this.linkAirUpload = linkAirUpload;
    }

    public int getLinkAirDownload() {
        return linkAirDownload;
    }

    public void setLinkAirDownload(int linkAirDownload) {
        this.linkAirDownload = linkAirDownload;
    }

    public int getBattPert() {
        return battPert;
    }

    public void setBattPert(int battPert) {
        this.battPert = battPert;
    }

    public double getBattVoltage() {
        return battVoltage;
    }

    public void setBattVoltage(double battVoltage) {
        this.battVoltage = battVoltage;
    }

    public double getBattCurrent() {
        return battCurrent;
    }

    public void setBattCurrent(double battCurrent) {
        this.battCurrent = battCurrent;
    }

    public double getHomeLatitude() {
        return homeLatitude;
    }

    public void setHomeLatitude(double homeLatitude) {
        this.homeLatitude = homeLatitude;
    }

    public double getHomeLongitude() {
        return homeLongitude;
    }

    public void setHomeLongitude(double homeLongitude) {
        this.homeLongitude = homeLongitude;
    }

    public int getAremd() {
        return aremd;
    }

    public void setAremd(int aremd) {
        this.aremd = aremd;
    }

    public FlightMode getFlightMode() {
        return flightMode;
    }

    public String getFlightModeZh() {
        String mode = "";
        switch (flightMode) {
            case MANUAL:
                mode = "手动模式";
                break;
            case ATTI:
                mode = "姿态模式";
                break;
            case ATTI_COURSE_LOCK:
                mode = "姿态路线锁定模式";
                break;
            case ATTI_HOVER:
                mode = "姿态悬停模式";
                break;
            case HOVER:
                mode = "悬停模式";
                break;

            case GPS_ATTI:
                mode = "GPS姿态模式";
                break;
            case GPS_COURSE_LOCK:
                mode = "GPS路线锁定模式";
                break;
            case GPS_HOME_LOCK:
                mode = "GPS Home模式";
                break;
            case GPS_HOT_POINT:
                mode = "GPS热点模式";
                break;
            case ASSISTED_TAKEOFF:
                mode = "辅助起飞模式";
                break;
            case AUTO_TAKEOFF:
                mode = "自动起飞";
                break;
            case AUTO_LANDING:
                mode = "自动降落";
                break;
            case ATTI_LANDING:
                mode = "姿态降落模式";
                break;
            case GPS_WAYPOINT:
                mode = "GPS航点模式";
                break;
            case GO_HOME:
                mode = "回家";
                break;

            case JOYSTICK:
                mode = "摇杆模式";
                break;
            case GPS_ATTI_WRISTBAND:
                mode = "GPS姿态限制模式";
                break;

            case ATTI_LIMITED:
                mode = "姿态限制模式";
                break;
            case DRAW:
                mode = "绘图模式";
                break;
            case GPS_FOLLOW_ME:
                mode = "GPS跟随模式";
                break;
            case ACTIVE_TRACK:
                mode = "ActiveTrack模式";
                break;
            case TAP_FLY:
                mode = "TapFly模式";
                break;

            case GPS_SPORT:
                mode = "GPS运动模式";
                break;
            case GPS_NOVICE:
                mode = "GPS新手模式";
                break;
            case CONFIRM_LANDING:
                mode = "确认降落中";
                break;
            case TERRAIN_FOLLOW:
                mode = "地形跟随模式";
                break;

            case TRIPOD:
                mode = "三脚架模式";
                break;
            case TRACK_SPOTLIGHT:
                mode = "活动跟踪模式";
                break;
            case MOTORS_JUST_STARTED:
                mode = "电机刚启动";
                break;

            case GPS_BLAKE:
            case CLICK_GO:
            case CINEMATIC:
            case PANO:
            case FARMING:
            case FPV:
            case PALM_CONTROL:
            case QUICK_SHOT:
            case DETOUR:
            case TIME_LAPSE:
            case POI2:
            case OMNI_MOVING:
            case ADSB_AVOIDING:
            case UNKNOWN:
            default:
                mode = flightMode.toString();
                break;
        }
        return mode;
    }

    public void setFlightMode(FlightMode flightMode) {
        this.flightMode = flightMode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public double getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(double xSpeed) {
        this.xSpeed = xSpeed;
    }

    public double getySpeed() {
        return ySpeed;
    }

    public void setySpeed(double ySpeed) {
        this.ySpeed = ySpeed;
    }

    public double getzSpeed() {
        return zSpeed;
    }

    public void setzSpeed(double zSpeed) {
        this.zSpeed = zSpeed;
    }

    public double getXySpeed() {
        xySpeed = Math.sqrt(Math.pow(xSpeed, 2) + Math.pow(ySpeed, 2));
        return xySpeed;
    }

    public void setXySpeed(double xySpeed) {
        this.xySpeed = xySpeed;
    }

    public int getSateCount() {
        return sateCount;
    }

    public void setSateCount(int sateCount) {
        this.sateCount = sateCount;
    }

    public GPSSignalLevel getGpsSignalStatus() {
        return gpsSignalStatus;
    }

    public void setGpsSignalStatus(GPSSignalLevel gpsSignalStatus) {
        this.gpsSignalStatus = gpsSignalStatus;
    }

    public double getRoll() {
        return roll;
    }

    public void setRoll(double roll) {
        this.roll = roll;
    }

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public double getYaw() {
        return yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    //0	    数据头	0xFC	UINT8	标识消息开始
//1	    消息长度	/	UINT16	消息段的数据长度
//3	    序列号	0-255	UINT8	消息发送序列号
//4	    唯一编号 sysid	/	UINT32	无人机或设备的唯一编号
//8	    组件编号 comid	0-255	UINT8	区分不同的设备组件
//9	    协议版本	/	UINT8	根据版本区分不同协议，枚举：EF_VERSION_TYPE
//10	消息编号	/	UINT16	见第2章 #n消息说明
//n+12	数据段	/	Byte[]	~
//n+13	CRC16	/	INT16	CRC校验码

//12	DeviceID	UINT16	蜂巢编号	预留
//14	Lat	Int32	无人机当前纬度	1E7度
//18	Lng	Int32	无人机所在经度	1E7度
//22	AltRel	Int32	无人机高度	厘米
//26	AltAbs	Int32	无人机海拔高度	厘米
//30	XYSpeed	Int16	水平飞行速度	厘米/秒
//32	ZSpeed	Int16	垂直速度	厘米/秒
//32	Mode	byte	飞行模式	枚举 EF_DJI_MODE
//33	roll	float	弧度	roll angle (-pi..+pi)
//37	pitch	float	弧度	Pitch angle (-pi..+pi)
//41	yaw	float	弧度	Yaw angle (-pi..+pi)
//45	BattPert	byte	电压百分比	%
//46	DownlinkSignalQuality	byte	下行信号强度	%
//47	UplinkSignalQuality	byte	上行信号强度	%
//48	GpsStatus Byte	定位状态	见枚举
//48	SatelliteCount	Byte	卫星颗数	颗
//50	SystemStatus	Byte	系统运行状态	见枚举EF_HIVE_STATUS

    byte[] packetheart = new byte[0];

    public byte[] GetHeartbeat() {
        byte[] bytetemp;
        int index = 0;
        if (packetheart.length == 0) {
            packetheart = new byte[55];
            packetheart[index] = (byte) 0xFC;  //头
            index++;
            short length = (short) (packetheart.length - 14);
            bytetemp = BytesUtil.short2Bytes(length);
            for (int i = 0; i < bytetemp.length; i++) {
                packetheart[index] = bytetemp[i];  //长度
                index++;
            }
            byte seqid = 0; //TODO EFMqttService.GetSeqID();
            packetheart[index] = seqid; //序列号
            index++;
            bytetemp = new byte[4];//TODO sysid  采用sn码，sysid保留
            for (int i = 0; i < bytetemp.length; i++) {
                packetheart[index] = bytetemp[i]; //无人机编号或设备编号
                index++;
            }
            packetheart[index] = (byte) 0xA2; //组件编号
            index++;
            packetheart[index] = (byte) 0x70; //协议版本 大疆
            index++;

            bytetemp = BytesUtil.short2Bytes(2200);//msgid 2bytes
            for (int i = 0; i < bytetemp.length; i++) {
                packetheart[index] = bytetemp[i];  //消息ID
                index++;
            }
        }
        index = 12;  //数据段开始填充
        bytetemp = new byte[2];//TODO 蜂巢ID 保留
        for (int i = 0; i < bytetemp.length; i++) {
            packetheart[index] = bytetemp[i];  //预留蜂巢id
            index++;
        }
        bytetemp = BytesUtil.int2Bytes((int) (latitude * 1e7));
        for (int i = 0; i < bytetemp.length; i++) {
            packetheart[index] = bytetemp[i];  //纬度
            index++;
        }
        bytetemp = BytesUtil.int2Bytes((int) (longitude * 1e7));
        for (int i = 0; i < bytetemp.length; i++) {
            packetheart[index] = bytetemp[i];  //经度
            index++;
        }
        bytetemp = BytesUtil.int2Bytes((int) (alt * 100));
        for (int i = 0; i < bytetemp.length; i++) {
            packetheart[index] = bytetemp[i];  //高度
            index++;
        }
        bytetemp = BytesUtil.int2Bytes((int) (altabs * 100));
        for (int i = 0; i < bytetemp.length; i++) {
            packetheart[index] = bytetemp[i];  //海拔
            index++;
        }
        bytetemp = BytesUtil.short2Bytes((int) (getXySpeed() * 100));
        for (int i = 0; i < bytetemp.length; i++) {
            packetheart[index] = bytetemp[i];  //水平速度
            index++;
        }
        bytetemp = BytesUtil.short2Bytes((int) (zSpeed * 100));
        for (int i = 0; i < bytetemp.length; i++) {
            packetheart[index] = bytetemp[i];  //垂直速度
            index++;
        }
        packetheart[index] = (byte) (flightMode.value());  //模式
        index++;
        bytetemp = BytesUtil.float2Bytes(roll * deg2rad);
        for (int i = 0; i < bytetemp.length; i++) {
            packetheart[index] = bytetemp[i]; //roll
            index++;
        }
        bytetemp = BytesUtil.float2Bytes(pitch * deg2rad);
        for (int i = 0; i < bytetemp.length; i++) {
            packetheart[index] = bytetemp[i]; //pitch
            index++;
        }
        bytetemp = BytesUtil.float2Bytes(yaw * deg2rad);
        for (int i = 0; i < bytetemp.length; i++) {
            packetheart[index] = bytetemp[i]; //yaw
            index++;
        }
        packetheart[index] = (byte) (battPert);  //电压
        index++;
        packetheart[index] = (byte) (linkAirDownload);  //下行信号
        index++;
        packetheart[index] = (byte) (linkAirUpload);  //上行信号
        index++;
        packetheart[index] = (byte) (gpsSignalStatus.value());  //定位状态
        index++;
        packetheart[index] = (byte) (sateCount);  //卫星数
        index++;
        packetheart[index] = (byte) (getStatus());  //系统运行状态
        index++;
        bytetemp = CrcUtil.Crc16Packet(packetheart, packetheart.length - 2);
        for (int i = 0; i < 2; i++) {
            packetheart[index] = bytetemp[i]; //校验2位
            index++;
        }
        return packetheart;
    }

    private final double rad2deg = (float) (180 / Math.PI); //转为度
    private final double deg2rad = (float) (1.0 / rad2deg); //转为弧度
}
