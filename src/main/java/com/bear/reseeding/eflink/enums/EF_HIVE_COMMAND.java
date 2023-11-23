package com.bear.reseeding.eflink.enums;

/**
 * 控制停机坪开关指令
 * 0：无动作，1：打开，2：关闭，3：暂停
 */
public enum EF_HIVE_COMMAND {

    OPEN(1, "打开停机坪"),
    CLOSE(2, "关闭停机坪"),
    PAUSE(3, "暂停舱门开关"),
    UNDEFINE(0, "无动作");

    private int code;
    private String msg;

    EF_HIVE_COMMAND(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static EF_HIVE_COMMAND GetEnum(int code) {
        for (EF_HIVE_COMMAND m : EF_HIVE_COMMAND.values()) {
            if (m.getCode() == code) {
                return m;
            }
        }
        return UNDEFINE;
    }

    public static String msg(int code) {
        for (EF_HIVE_COMMAND m : EF_HIVE_COMMAND.values()) {
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
