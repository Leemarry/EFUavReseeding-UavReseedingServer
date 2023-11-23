package com.bear.reseeding.eflink.enums;


/**
 * @Auther: bear
 * @Date: 2021/12/8 10:44
 * @Description: 停机坪系统全自动任务的进度枚举。
 */
public enum EF_PARKING_APRON_PROGRESS {
    READY(1, "准备中"),

    // 系统相关(100到199)
    STEP_100(100, "正在检测系统状态"),
    STEP_101(101, "系统状态正常"),

    // 无人机相关(200到299)
    STEP_200(200, "正在打开无人机"),
    STEP_201(201, "无人机已通电"),
    STEP_202(202, "正在关闭无人机"),
    STEP_203(203, "无人机已关闭"),

    STEP_210(210, "正在检测无人机状态"),
    STEP_211(211, "正在检测电压"),
    STEP_212(212, "正在检测定位"),
    STEP_213(213, "正在检测通讯质量"),

    STEP_219(219, "无人机状态正常"),

    STEP_225(225, "无人机准备起飞中"),
    STEP_226(226, "无人机准备执行任务"),
    STEP_227(227, "无人机起飞成功"),
    STEP_228(228, "无人机执行任务成功"),
    STEP_229(229, "无人机准备返航中"),
    STEP_230(230, "无人机返航中"),

    STEP_250(250, "任务巡航中"),
    STEP_251(251, "飞行中"),

    //遥控器相关(300到399)
    STEP_300(300, "正在打开遥控器"),
    STEP_301(301, "遥控器已开启"),
    STEP_302(302, "正在关闭遥控器"),
    STEP_303(303, "遥控器已关闭"),

    //停机坪相关(400到499)
    STEP_400(400, "正在开启停机坪"),
    STEP_401(401, "正在开启升降"),
    STEP_402(402, "正在开启推杆"),
    STEP_403(403, "正在开启舱门"),
    STEP_404(404, "停机坪开启完成"),
    STEP_405(405, "xxx"),
    STEP_406(406, "正在关闭停机坪"),
    STEP_407(407, "正在关闭升降"),
    STEP_408(408, "正在关闭推杆"),
    STEP_409(409, "正在关闭舱门"),
    STEP_410(410, "停机坪已关闭"),
    STEP_411(411, "暂停停机坪开关"),
    STEP_412(412, "停机坪已暂停开关"),

    //地面站相关(500到599)
    STEP_500(500, "正在打开地面站"),
    STEP_501(501, "地面站已打开"),
    STEP_502(502, "正在关闭地面站"),
    STEP_503(503, "地面站已关闭"),

    UNDEFINE(0, "未知状态");

    private int code;
    private String msg;

    EF_PARKING_APRON_PROGRESS(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    EF_PARKING_APRON_PROGRESS(int i) {
    }

    public static String msg(int code) {
        for (EF_PARKING_APRON_PROGRESS m : EF_PARKING_APRON_PROGRESS.values()) {
            if (m.getCode() == code) {
                return m.getMsg();
            }
        }
        return UNDEFINE.getMsg();
    }

    public static EF_PARKING_APRON_PROGRESS getEnum(int code) {
        for (EF_PARKING_APRON_PROGRESS m : EF_PARKING_APRON_PROGRESS.values()) {
            if (m.getCode() == code) {
                return m;
            }
        }
        return UNDEFINE;
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
