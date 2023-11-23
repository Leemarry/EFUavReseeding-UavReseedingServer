package com.bear.reseeding.eflink.enums;


/**
 * @Auther: bear
 * @Date: 2021/12/8 10:44
 * @Description: 停机坪系统控制命令回复信息。
 */
public enum EF_PARKING_APRON_ACK {
    SUCCESS(1, "成功"),

    ERROR(-1, "执行失败"),
    REFUSE(-2, "拒绝执行"),
    AUTHORITY(-3, "权限不足"),
    TIMEOUT(-4, "超时"),
    DATA_ERROR(-5, "数据错误"),
    DATA_FORMAT_ERROR(-6, "数据格式错误"),

    EROR_OPEN_SYSTEM(-90, "系统打开失败"),
    EROR_SHUTDOWN_SYSTEM(-91, "系统关闭失败"),

    UAV_NOT_CONNECT(-100, "无人机未连接"),
    ERROR_101(-101, "无人机状态检测失败"),
    ERROR_104(-104, "无人机通电失败"),
    ERROR_105(-105, "无人机关机失败"),
    ERROR_106(-106, "无人机起飞失败"),
    ERROR_107(-107, "无人机执行任务失败"),
    ERROR_108(-108, "无人机返航失败"),

    ERROR_110(-110, "无人机电压不足"),
    ERROR_111(-111, "无人机信号强度弱"),
    ERROR_112(-112, "无人机定位异常"),

    ERROR200(-200, "打开遥控器失败"),
    ERROR201(-201, "关闭遥控器失败"),

    ERROR250(-250, "打开停机坪失败"),
    ERROR251(-251, "关闭停机坪失败"),
    ERROR252(-252, "暂停停机坪开关失败"),

    ERROR400(-400, "打开地面站失败"),
    ERROR401(-401, "关闭地面站失败"),

    UNDEFINE(0, "未知错误");

    private int code;
    private String msg;

    EF_PARKING_APRON_ACK(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    EF_PARKING_APRON_ACK(int i) {
    }

    public static String msg(int code) {
        for (EF_PARKING_APRON_ACK m : EF_PARKING_APRON_ACK.values()) {
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
