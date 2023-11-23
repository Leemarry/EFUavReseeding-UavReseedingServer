package com.bear.reseeding.eflink.enums;


/**
 * @Auther: bear
 * @Date: 2021/12/8 10:44
 * @Description: 停机坪系统控制命令枚举。
 */
public enum EF_PARKING_APRON_CMD {

    SUSPEND(1, "终止全自动任务"),
    AUTO_OPEN_SYSTEM(1000, "一键开启系统"),
    AUTO_START_TAKEOFF(1003, "一键起飞"),
    AUTO_START_PRECISION_TAKEOFF(1004, "一键精确起飞"),
    AUTO_START_MISSION(1006, "一键执行任务"),
    AUTO_START_PRECISION_MISSION(1007, "一键全自动精确起飞后执行任务"),
    AUTO_START_RTL(1009, "一键全自动返航"),
    AUTO_SHUTDOWN_SYSTEM(1012, "一键关闭系统"),

    OPEN_HIVE(1016, "打开机舱"),
    PAUSE_HIVE(1017, "暂停机舱"),
    CLOSE_HIVE(1018, "关闭机舱"),

    START_DJI_UAV(1020, "打开无人机"),
    SHUTDOWN_DJI_UAV(1021, "关闭无人机"),
    RESTART_DJI_UAV(1022, "重启无人机"),

    START_DJI_RC(1024, "打开大疆遥控器"),
    SHUTDOWN_DJI_RC(1025, "关闭大疆遥控器"),
    RESTART_DJI_RC(1026, "重启大疆遥控器"),

    START_DJI_GCS(1028, "打开Android Dji地面站"),
    SHUTDOWN_DJI_GCS(1029, "关闭Android Dji地面站"),
    RESTART_DJI_GCS(1030, "重启Android Dji地面站"),

    START_CHARGING(1032, "开始充电"),
    SHUTDOWN_CHARGING(1033, "停止充电"),
    RESTART_CHARGING(1034, "重新开始充电"),

    RESTART_HOST(1037, "重启主机"),
    RESTART_MP_SERVER(1038, "重启服务后台"),

    TAKEOFF(1100, "起飞无人机"),
    TAKEOFF_AUTO(1101, "起飞无人机执行任务"),
    RTL(1102, "返航无人机"),
    LAND(1103, "降落无人机"),
    LOITER(1104, "悬停无人机"),
    START_PRECISION_TAKEOFF(1105, "精确起飞无人机"),
    START_PRECISION_MISSION(1106, "精确起飞无人机执行任务"),
    LOAD_NEWEST_MISSIONS(1107, "加载历史任务到无人机"),
    DOWN_WPS_FROM_UAV(1108, "从无人机下载任务"),
    DOWN_WPS_FROM_LOCAL(1109, "从本地获取历史最新任务"),

    START_UAV_FOLLOW_MODE(1110, "启用跟随模式"),
    STOP_UAV_FOLLOW_MODE(1111, "停止跟随模式"),
    UPDATE_UAV_FOLLOW_POS(1112, "更新跟随坐标"),

    REQUEST_REMOTE_CTRL(1150, "请求远程控制无人机"),
    STOP_REMOTE_CTRL(1151, "结束远程控制无人机"),
    DATA_REMOTE_CTRL(1152, "远程控制数据包"),
    GIMBAL_ROTATE(1153, "云台控制数据包"),

    REFRESH_MP_CAMERA(1159, "刷新地面站视频"),

    CAMETAMODE_SHOOT_PHOTO(1160, "拍照模式"),
    CAMETAMODE_RECORD_VIDEO(1161, "录像模式"),
    CAMETAMODE_TAKEPHOTO(1162, "拍照"),
    CAMETAMODE_RECORD_START(1163, "开始录像"),
    CAMETAMODE_RECORD_STOP(1164, "停止录像"),

    CAMERA_MODE(1165, "完整的相机模式"),
    CAMERA_MODE_PHOTO_CFG(1166, "拍照模式参数"),
    CAMERA_VIDEO_STREAM_SOURCE(1167, "设置相机源"),
    CAMERA_LENS_TYPE(1168, "相机镜头类型"),
    CAMERA_FOCUS_AUTO(1169, "相机变焦"),
    CAMERA_FOCUS_STOP(1170, "停止相机变焦"),
    CAMERA_ZOOM_AUTO(1171, "相机变倍"),
    CAMERA_ZOOM_STOP(1172, "停止相机变倍"),

    FORMAT_SD(1175, "格式化SD卡"),
    FORMAT_SSD(1176, "格式化SSD"),
    FORMAT_STORAGE(1177, "格式化储存"),

    GET_ALL_MEDIA(1180, "查询时间段的所有媒体文件"),
    DOWNLOAD_ALL_MEDIA(1181, "下载时间段的所有媒体文件"),
    DOWNLOAD_MEDIA(1182, "下载媒体文件"),

    START_UAV_SIMU(1199, "启用无人机模拟环境"),

    UNDEFINE(0, "未定义");

    private int code;
    private String msg;

    EF_PARKING_APRON_CMD(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static EF_PARKING_APRON_CMD GetEnum(int code) {
        for (EF_PARKING_APRON_CMD m : EF_PARKING_APRON_CMD.values()) {
            if (m.getCode() == code) {
                return m;
            }
        }
        return UNDEFINE;
    }

    public static String msg(int code) {
        for (EF_PARKING_APRON_CMD m : EF_PARKING_APRON_CMD.values()) {
            if (m.getCode() == code) {
                return m.getMsg();
            }
        }
        return UNDEFINE.getMsg();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
