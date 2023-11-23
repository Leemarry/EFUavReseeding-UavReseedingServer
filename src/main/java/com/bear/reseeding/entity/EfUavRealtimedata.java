package com.bear.reseeding.entity;

import com.bear.reseeding.eflink.enums.FlightMode;
import com.bear.reseeding.utils.BytesUtil;
import com.bear.reseeding.utils.ConstantUtil;

import java.util.Date;
import java.io.Serializable;

/**
 * 无人机的飞行数据表，这个表目前不适用，不然数据量太大。只记录昨天和今天的数据，超过则移动到efuav_historydata表中。(EfUavRealtimedata)实体类
 *
 * @author makejava
 * @since 2023-11-23 18:59:59
 */


/**
 * 无人机的飞行数据表，这个表目前不适用，不然数据量太大。只记录昨天和今天的数据，超过则移动到efuav_historydata表中。(EfUavRealtimedata)实体类
 *
 * @author makejava
 * @since 2021-12-28 13:24:52
 */
public class EfUavRealtimedata implements Serializable {
    private static final long serialVersionUID = 633075889266752265L;
    /**
     * 主键自增
     */
    private Integer id;
    /**
     * 无人机编号
     */
    private String uavId;
    /**
     * 数据时间
     */
    private Date dataDate;
    /**
     * 当前飞行模式
     */
    private String flightMode;
    private String flightModeText;

    /**
     * 纬度
     */
    private Double lat;
    /**
     * 经度
     */
    private Double lng;
    /**
     * 相对高度
     */
    private Float alt;
    /**
     * 海拔高度
     */
    private Float altabs;
    /**
     * 横滚
     */
    private Float roll;
    /**
     * 俯仰
     */
    private Float pitch;
    /**
     * 机头朝向
     */
    private Float yaw;
    /**
     * 飞行速度
     */
    private Float xySpeed;
    /**
     * 垂直速度
     */
    private Float zSpeed;
    /**
     * 定位状态
     */
    private Float gpsStatus;


    private String gpsStatusText;
    /**
     * 卫星颗数
     */
    private Integer satecount;
    /**
     * 无人机电压
     */
    private Float batteryValue;
    /**
     * 无人机电压百分比
     */
    private Integer batteryPert;
    /**
     * 无人机当前状态
     */
    private Float uavStatus;
    /**
     * 无人机是否存在异常，异常信息
     */
    private Float uavAbnormal;
    /**
     * 遥控与无人机通讯质量，下行
     */
    private Integer linkAirDownload;
    /**
     * 遥控与无人机通讯质量，上行
     */
    private Integer linkAirUpload;
    /**
     * 1已解锁，0未解锁
     */
    private Integer aremd;

    /**
     * 电机是否已经启动	1已启动，0未启动
     */
    private int areMotorsOn;

    /**
     * 飞行时长
     */
    private int flightTimeInSeconds;

    /**
     * 整个系统的运行状态
     */
    private Integer systemStatus;
    /**
     * 外键：无人机当前所连接的蜂巢编号，可空
     */
    private String uavCurentHive;


    // 2022年9月20日11:43:37 新增协议内容
    private double pdop;
    private int remoteSign;
    private int fpvSign;
    private double batteryTemp;
    private double voltage;
    private double electricCurrent;
    private int uavType;
    private int mountDevice;
    private int cameraLens;
    private double distToHome;
    private double distToTarget;

    // 2023年7月11日新增36字节协议
    private int gimbalMode = 0;
    private double gimbalRoll;
    private double gimbalPitch;
    private double gimbalYaw;
    private double gimbalRelToUavHeading; //RelativeToAircraftHeading
    private long wpStartTime;           //开始执行任务的时间
    private long wpFirstWpTime;     //到达第一个航点的时刻
    private long wpMayFinlishTime;  //预计任务结束时间
    private int wpNo;       //当前到达的航点序号
    private int wpCount;        //航点总数
    private int wpProgress;     //任务完成百分比
    private int wpFlyedTime;        //飞行时长(以到达1号航点点后开始计算)

    @Override
    public String toString() {
        return "EfUavRealtimedata{" +
                "id=" + id +
                ", uavId='" + uavId + '\'' +
                ", dataDate=" + dataDate +
                ", flightMode='" + flightMode + '\'' +
                ", flightModeText='" + flightModeText + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", alt=" + alt +
                ", altabs=" + altabs +
                ", roll=" + roll +
                ", pitch=" + pitch +
                ", yaw=" + yaw +
                ", xySpeed=" + xySpeed +
                ", zSpeed=" + zSpeed +
                ", gpsStatus=" + gpsStatus +
                ", gpsStatusText='" + gpsStatusText + '\'' +
                ", satecount=" + satecount +
                ", batteryValue=" + batteryValue +
                ", batteryPert=" + batteryPert +
                ", uavStatus=" + uavStatus +
                ", uavAbnormal=" + uavAbnormal +
                ", linkAirDownload=" + linkAirDownload +
                ", linkAirUpload=" + linkAirUpload +
                ", aremd=" + aremd +
                ", areMotorsOn=" + areMotorsOn +
                ", flightTimeInSeconds=" + flightTimeInSeconds +
                ", systemStatus=" + systemStatus +
                ", uavCurentHive='" + uavCurentHive + '\'' +
                ", pdop=" + pdop +
                ", remoteSign=" + remoteSign +
                ", fpvSign=" + fpvSign +
                ", batteryTemp=" + batteryTemp +
                ", voltage=" + voltage +
                ", electricCurrent=" + electricCurrent +
                ", uavType=" + uavType +
                ", mountDevice=" + mountDevice +
                ", cameraLens=" + cameraLens +
                ", distToHome=" + distToHome +
                ", distToTarget=" + distToTarget +
                ", gimbalMode=" + gimbalMode +
                ", gimbalRoll=" + gimbalRoll +
                ", gimbalPitch=" + gimbalPitch +
                ", gimbalYaw=" + gimbalYaw +
                ", gimbalRelToUavHeading=" + gimbalRelToUavHeading +
                ", wpStartTime=" + wpStartTime +
                ", wpFirstWpTime=" + wpFirstWpTime +
                ", wpMayFinlishTime=" + wpMayFinlishTime +
                ", wpNo=" + wpNo +
                ", wpCount=" + wpCount +
                ", wpProgress=" + wpProgress +
                ", wpFlyedTime=" + wpFlyedTime +
                '}';
    }

    public void unPacket(byte[] packet, int index) {
        dataDate = new Date(System.currentTimeMillis());
        lat = (BytesUtil.bytes2Int(packet, index) / 1e7d); //lat
        index += 4;
        lng = (BytesUtil.bytes2Int(packet, index) / 1e7d); //lng
        index += 4;
        alt = (BytesUtil.bytes2Int(packet, index) / 100f); //alt
        index += 4;
        altabs = (BytesUtil.bytes2Int(packet, index) / 100f); //altabs
        index += 4;
        xySpeed = (BytesUtil.bytes2Short(packet, index) / 100f); //speed xy
        index += 2;
        zSpeed = (BytesUtil.bytes2Short(packet, index) / 100f); //speed z
        index += 2;
        int enumvalue = packet[index] & 0xFF;
        flightMode = (FlightMode.valueIndexOf(enumvalue).toString()); //FlightMode
        getFlightModeText();
        index++;
        roll = (float) (BytesUtil.bytes2Float(packet, index) * ConstantUtil.rad2deg); //roll
        index += 4;
        pitch = (float) (BytesUtil.bytes2Float(packet, index) * ConstantUtil.rad2deg); //pitch
        index += 4;
        yaw = (float) (BytesUtil.bytes2Float(packet, index) * ConstantUtil.rad2deg); //yaw
        index += 4;
        batteryPert = (packet[index] & 0xFF); //电压百分比
        index++;
        linkAirDownload = (packet[index] & 0xFF); //下行信号强度
        index++;
        linkAirUpload = (packet[index] & 0xFF); //上行信号强度
        index++;
        enumvalue = packet[index] & 0xFF;
        gpsStatus = (float) enumvalue; //定位状态
        getGpsStatusText();
        index++;
        satecount = (packet[index] & 0xFF); //卫星颗数
        index++;
        systemStatus = (packet[index] & 0xFF); //系统运行状态
        index++;
        aremd = (packet[index] & 0xFF); //解锁状态
        index++;
        areMotorsOn = (packet[index] & 0xFF); //电机启动
        index++;
        flightTimeInSeconds = BytesUtil.bytes2UShort(packet, index);
        index += 2;

        //region 2022年9月20日 新增协议内容 start
        if (packet.length > index) {
            //无人机各功能模块开启状态Byte	位表示，描述开关状态, bit 0:是否启用了视觉辅助定位, bit 1:是否启用了精确着陆, bit 2:当前是否为拍照模式, bit 4:当前是否为录像模式, bit 5:当前是否正在录像
            uavStatus = (float) (packet[index] & 0xFF);
            index++;
        }
        if (packet.length > index + 2) {
            pdop = BytesUtil.bytes2Short(packet, index);//定位精度
            index += 2;
        }
        if (packet.length > index) {
            remoteSign = (packet[index] & 0xFF); //遥控器通讯信号强度
            index++;
        }
        if (packet.length > index) {
            fpvSign = (packet[index] & 0xFF); //图传通讯信号强度
            index++;
        }
        if (packet.length > index + 2) {
            batteryTemp = BytesUtil.bytes2Short(packet, index) / 100d;//电池温度
            index += 2;
        }
        if (packet.length > index + 2) {
            voltage = BytesUtil.bytes2Short(packet, index) / 100d;//电压
            index += 2;
        }
        if (packet.length > index + 2) {
            electricCurrent = BytesUtil.bytes2Short(packet, index) / 100d;//电流
            index += 2;
        }
        if (packet.length > index) {
            uavType = (packet[index] & 0xFF); //无人机类型	0未知，1 M30,2 M300,8 M3T
            index++;
        }
        if (packet.length > index) {
            mountDevice = (packet[index] & 0xFF); //无人机挂载设备	0未知，1相机
            index++;
        }
        if (packet.length > index) {
            cameraLens = (packet[index] & 0xFF); //相机镜头	0未知，1H20,2H20T
            index++;
        }
        if (packet.length > index + 4) {
            distToHome = BytesUtil.bytes2Short(packet, index) / 100d;//无人机与起飞点的距离
            index += 4;
        }
        if (packet.length > index + 4) {
            distToTarget = BytesUtil.bytes2Short(packet, index) / 100d;//无人机与目标点的距离
            index += 4;
        }
        //endregion 2022年9月20日 新增协议内容   end

        //region 2023年7月11日新增36字节协议 start
        if (packet.length > 111) {
            gimbalMode = (packet[index] & 0xFF);
            index += 1;
            gimbalRoll = BytesUtil.bytes2Float(packet, index);
            index += 4;
            gimbalPitch = BytesUtil.bytes2Float(packet, index);
            index += 4;
            gimbalYaw = BytesUtil.bytes2Float(packet, index);
            index += 4;
            gimbalRelToUavHeading = BytesUtil.bytes2Float(packet, index);
            index += 4;
            wpStartTime = BytesUtil.bytes2UInt(packet, index);
            index += 4;
            wpFirstWpTime = BytesUtil.bytes2UInt(packet, index);
            index += 4;
            wpMayFinlishTime = BytesUtil.bytes2UInt(packet, index);
            index += 4;
            wpNo = BytesUtil.bytes2UShort(packet, index);
            index += 2;
            wpCount = BytesUtil.bytes2UShort(packet, index);
            index += 2;
            wpProgress = (packet[index] & 0xFF);
            index += 1;
            wpFlyedTime = BytesUtil.bytes2UShort(packet, index);
            index += 2;
        }
        //endregion 2023年7月11日新增36字节协议 end

        //LogUtil.logInfo(this.toString());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUavId() {
        return uavId;
    }

    public void setUavId(String uavId) {
        this.uavId = uavId;
    }

    public Date getDataDate() {
        return dataDate;
    }

    public void setDataDate(Date dataDate) {
        this.dataDate = dataDate;
    }

    public String getFlightMode() {
        return flightMode;
    }

    public void setFlightMode(String flightMode) {
        this.flightMode = flightMode;
    }


    public String getFlightModeText() {
        try {
            flightModeText = flightMode;
            FlightMode mode = FlightMode.valueOf(flightMode);
            switch (mode) {
                case UNKNOWN:
                    flightModeText = "未知";
                    break;
                case ADSB_AVOIDING:
                    flightModeText = "避障启动";
                    break;
                case QUICK_SHOT:
                    flightModeText = "避障启动";
                    break;
                case MANUAL:
                    flightModeText = "手动模式";
                    break;
                case ATTI:
                    flightModeText = "姿态模式";
                    break;
                case ATTI_COURSE_LOCK:
                    flightModeText = "姿态路线锁定模式";
                    break;
                case ATTI_HOVER:
                    flightModeText = "姿态悬停模式";
                    break;
                case HOVER:
                    flightModeText = "悬停模式";
                    break;

                case GPS_ATTI:
                    flightModeText = "GPS姿态模式";
                    break;
                case GPS_COURSE_LOCK:
                    flightModeText = "GPS路线锁定模式";
                    break;
                case GPS_HOME_LOCK:
                    flightModeText = "GPS Home模式";
                    break;
                case GPS_HOT_POINT:
                    flightModeText = "GPS热点模式";
                    break;
                case ASSISTED_TAKEOFF:
                    flightModeText = "辅助起飞模式";
                    break;
                case AUTO_TAKEOFF:
                    flightModeText = "自动起飞";
                    break;
                case AUTO_LANDING:
                    flightModeText = "自动降落";
                    break;
                case ATTI_LANDING:
                    flightModeText = "姿态降落模式";
                    break;
                case GPS_WAYPOINT:
                    flightModeText = "GPS航点模式";
                    break;
                case GO_HOME:
                    flightModeText = "返航中";
                    break;

                case JOYSTICK:
                    flightModeText = "摇杆模式";
                    break;
                case GPS_ATTI_WRISTBAND:
                    flightModeText = "GPS姿态限制模式";
                    break;

                case ATTI_LIMITED:
                    flightModeText = "姿态限制模式";
                    break;
                case DRAW:
                    flightModeText = "绘图模式";
                    break;
                case GPS_FOLLOW_ME:
                    flightModeText = "GPS跟随模式";
                    break;
                case ACTIVE_TRACK:
                    flightModeText = "ActiveTrack模式";
                    break;
                case TAP_FLY:
                    flightModeText = "TapFly模式";
                    break;

                case GPS_SPORT:
                    flightModeText = "运动模式";
                    break;
                case GPS_NOVICE:
                    flightModeText = "新手模式";
                    break;
                case CONFIRM_LANDING:
                    flightModeText = "确认降落中";
                    break;
                case TERRAIN_FOLLOW:
                    flightModeText = "地形跟随模式";
                    break;

                case TRIPOD:
                    flightModeText = "三脚架模式";
                    break;
                case TRACK_SPOTLIGHT:
                    flightModeText = "活动跟踪模式";
                    break;
                case MOTORS_JUST_STARTED:
                    flightModeText = "电机启动";
                    break;
            }
        } catch (Exception ignored) {
        }
        return flightModeText;
    }

    public void setFlightModeText(String flightModeText) {
        this.flightModeText = flightModeText;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Float getAlt() {
        return alt;
    }

    public void setAlt(Float alt) {
        this.alt = alt;
    }

    public Float getAltabs() {
        return altabs;
    }

    public void setAltabs(Float altabs) {
        this.altabs = altabs;
    }

    public Float getRoll() {
        return roll;
    }

    public void setRoll(Float roll) {
        this.roll = roll;
    }

    public Float getPitch() {
        return pitch;
    }

    public void setPitch(Float pitch) {
        this.pitch = pitch;
    }

    public Float getYaw() {
        return yaw;
    }

    public void setYaw(Float yaw) {
        this.yaw = yaw;
    }

    public Float getXySpeed() {
        return xySpeed;
    }

    public void setXySpeed(Float xySpeed) {
        this.xySpeed = xySpeed;
    }

    public Float getZSpeed() {
        return zSpeed;
    }

    public void setZSpeed(Float zSpeed) {
        this.zSpeed = zSpeed;
    }

    public Float getGpsStatus() {
        return gpsStatus;
    }

    public String getGpsStatusText() {
        int gps = gpsStatus.intValue();
        String temp = "未知";
        switch (gps) {
            case 0:
                temp = "无信号";
                break;
            case 1:
                temp = "信号差";
                break;
            case 2:
                temp = "信号弱";
                break;
            case 3:
                temp = "良好";
                break;
            case 4:
                temp = "正常";
                break;
            case 5:
                temp = "高质量";
                break;
            case 10:
                temp = "差分定位";
                break;
            default:
                temp = "正常";
                break;
        }
        gpsStatusText = temp;
        return gpsStatusText;
    }

    public void setGpsStatus(Float gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    public Integer getSatecount() {
        return satecount;
    }

    public void setSatecount(Integer satecount) {
        this.satecount = satecount;
    }

    public Float getBatteryValue() {
        return batteryValue;
    }

    public void setBatteryValue(Float batteryValue) {
        this.batteryValue = batteryValue;
    }

    public Integer getBatteryPert() {
        return batteryPert;
    }

    public void setBatteryPert(Integer batteryPert) {
        this.batteryPert = batteryPert;
    }

    public Float getUavStatus() {
        return uavStatus;
    }

    public void setUavStatus(Float uavStatus) {
        this.uavStatus = uavStatus;
    }

    public Float getUavAbnormal() {
        return uavAbnormal;
    }

    public void setUavAbnormal(Float uavAbnormal) {
        this.uavAbnormal = uavAbnormal;
    }

    public Integer getLinkAirDownload() {
        return linkAirDownload;
    }

    public void setLinkAirDownload(Integer linkAirDownload) {
        this.linkAirDownload = linkAirDownload;
    }

    public Integer getLinkAirUpload() {
        return linkAirUpload;
    }

    public void setLinkAirUpload(Integer linkAirUpload) {
        this.linkAirUpload = linkAirUpload;
    }

    public Integer getAremd() {
        return aremd;
    }

    public void setAremd(Integer aremd) {
        this.aremd = aremd;
    }

    public int getFlightTimeInSeconds() {
        return flightTimeInSeconds;
    }

    public void setFlightTimeInSeconds(int flightTimeInSeconds) {
        this.flightTimeInSeconds = flightTimeInSeconds;
    }

    public int getAreMotorsOn() {
        return areMotorsOn;
    }

    public void setAreMotorsOn(int areMotorsOn) {
        this.areMotorsOn = areMotorsOn;
    }

    public Integer getSystemStatus() {
        return systemStatus;
    }

    public void setSystemStatus(Integer systemStatus) {
        this.systemStatus = systemStatus;
    }

    public String getUavCurentHive() {
        return uavCurentHive;
    }

    public void setUavCurentHive(String uavCurentHive) {
        this.uavCurentHive = uavCurentHive;
    }

    public double getPdop() {
        return pdop;
    }

    public void setPdop(double pdop) {
        this.pdop = pdop;
    }

    public int getRemoteSign() {
        return remoteSign;
    }

    public void setRemoteSign(int remoteSign) {
        this.remoteSign = remoteSign;
    }

    public int getFpvSign() {
        return fpvSign;
    }

    public void setFpvSign(int fpvSign) {
        this.fpvSign = fpvSign;
    }

    public double getBatteryTemp() {
        return batteryTemp;
    }

    public void setBatteryTemp(double batteryTemp) {
        this.batteryTemp = batteryTemp;
    }

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public double getElectricCurrent() {
        return electricCurrent;
    }

    public void setElectricCurrent(double electricCurrent) {
        this.electricCurrent = electricCurrent;
    }

    public int getUavType() {
        return uavType;
    }

    public void setUavType(int uavType) {
        this.uavType = uavType;
    }

    public int getMountDevice() {
        return mountDevice;
    }

    public void setMountDevice(int mountDevice) {
        this.mountDevice = mountDevice;
    }

    public int getCameraLens() {
        return cameraLens;
    }

    public void setCameraLens(int cameraLens) {
        this.cameraLens = cameraLens;
    }

    public double getDistToHome() {
        return distToHome;
    }

    public void setDistToHome(double distToHome) {
        this.distToHome = distToHome;
    }

    public double getDistToTarget() {
        return distToTarget;
    }

    public void setDistToTarget(double distToTarget) {
        this.distToTarget = distToTarget;
    }

    public Float getzSpeed() {
        return zSpeed;
    }

    public void setzSpeed(Float zSpeed) {
        this.zSpeed = zSpeed;
    }

    public int getGimbalMode() {
        return gimbalMode;
    }

    public void setGimbalMode(int gimbalMode) {
        this.gimbalMode = gimbalMode;
    }

    public double getGimbalRoll() {
        return gimbalRoll;
    }

    public void setGimbalRoll(double gimbalRoll) {
        this.gimbalRoll = gimbalRoll;
    }

    public double getGimbalPitch() {
        return gimbalPitch;
    }

    public void setGimbalPitch(double gimbalPitch) {
        this.gimbalPitch = gimbalPitch;
    }

    public double getGimbalYaw() {
        return gimbalYaw;
    }

    public void setGimbalYaw(double gimbalYaw) {
        this.gimbalYaw = gimbalYaw;
    }

    public double getGimbalRelToUavHeading() {
        return gimbalRelToUavHeading;
    }

    public void setGimbalRelToUavHeading(double gimbalRelToUavHeading) {
        this.gimbalRelToUavHeading = gimbalRelToUavHeading;
    }

    public long getWpStartTime() {
        return wpStartTime;
    }

    public void setWpStartTime(long wpStartTime) {
        this.wpStartTime = wpStartTime;
    }

    public long getWpFirstWpTime() {
        return wpFirstWpTime;
    }

    public void setWpFirstWpTime(long wpFirstWpTime) {
        this.wpFirstWpTime = wpFirstWpTime;
    }

    public long getWpMayFinlishTime() {
        return wpMayFinlishTime;
    }

    public void setWpMayFinlishTime(long wpMayFinlishTime) {
        this.wpMayFinlishTime = wpMayFinlishTime;
    }

    public int getWpNo() {
        return wpNo;
    }

    public void setWpNo(int wpNo) {
        this.wpNo = wpNo;
    }

    public int getWpCount() {
        return wpCount;
    }

    public void setWpCount(int wpCount) {
        this.wpCount = wpCount;
    }

    public int getWpProgress() {
        return wpProgress;
    }

    public void setWpProgress(int wpProgress) {
        this.wpProgress = wpProgress;
    }

    public int getWpFlyedTime() {
        return wpFlyedTime;
    }

    public void setWpFlyedTime(int wpFlyedTime) {
        this.wpFlyedTime = wpFlyedTime;
    }
}
