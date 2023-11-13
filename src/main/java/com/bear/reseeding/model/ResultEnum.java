package com.bear.reseeding.model;

/**
 * @Auther: bear
 * @Date: 2021/7/15 17:28
 * @Description: null
 */
public enum ResultEnum {
    SUCCESS(1, "成功"),
    ERROR(0, "默认值"),
    MISARGS(2, "缺少必要参数:"),
    UNKNOWN_ERROR(-2, "未知错误"),
    FAIL(-1);

    private Integer code;

    private String message;

    ResultEnum(Integer code) {
        this.code = code;
    }

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
