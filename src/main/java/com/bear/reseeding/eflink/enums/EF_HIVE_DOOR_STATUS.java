package com.bear.reseeding.eflink.enums;

/**
 * 停机坪开关状态枚举
 *  0：未知，1：已开启，2：已关闭，3：开启中，4：关闭中，5：暂停
 */
public enum EF_HIVE_DOOR_STATUS {

    OPENED(1, "已打开"),
    CLOSED(2, "已关闭"),
    OPENING(3, "开启中"),
    CLOSING(4, "关闭中"),
    PAUSED(5, "暂停"),
    UNDEFINE(0, "未知");

    private int code;
    private String msg;

    EF_HIVE_DOOR_STATUS(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static EF_HIVE_DOOR_STATUS GetEnum(int code) {
        for (EF_HIVE_DOOR_STATUS m : EF_HIVE_DOOR_STATUS.values()) {
            if (m.getCode() == code) {
                return m;
            }
        }
        return UNDEFINE;
    }

    public static String msg(int code) {
        for (EF_HIVE_DOOR_STATUS m : EF_HIVE_DOOR_STATUS.values()) {
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
