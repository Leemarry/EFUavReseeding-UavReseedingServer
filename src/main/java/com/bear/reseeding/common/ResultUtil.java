package com.bear.reseeding.common;

import com.bear.reseeding.model.Result;
import com.bear.reseeding.model.ResultEnum;

/**
 * @Auther: bear
 * @Date: 2021/7/15 17:29
 * @Description: null
 */
public class ResultUtil {
    /**
     * 返回成功
     *
     * @param msgid   EFLINK
     * @param message 用于提示的内容,根据实际情况选择在页面上提示或者不提示
     * @param data    具体的内容
     * @return
     */
    public static Result success(int msgid, String message, Object data) {
        Result result = new Result();
        result.setMessageId(msgid);
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static Result success(int msgid, String DID, String message, Object data) {
        Result result = new Result();
        result.setMessageId(msgid);
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 返回失败
     *
     * @param msgid   EFLINK
     * @param message 用于提示的内容
     * @return
     */
    public static Result error(int msgid, String message) {
        Result result = new Result();
        result.setMessageId(msgid);
        result.setCode(ResultEnum.ERROR.getCode());
        result.setMessage(message);
        return result;
    }

    /**
     * 返回失败
     *
     * @param msgid   EFLINK
     * @param message 用于提示的内容
     * @param data    具体的内容,一般无具体内容
     * @return
     */
    public static Result error(int msgid, String message, Object data) {
        Result result = new Result();
        result.setMessageId(msgid);
        result.setCode(ResultEnum.ERROR.getCode());
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static Result successData(Object object) {
        Result result = new Result();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMessage(ResultEnum.SUCCESS.getMessage());
        result.setData(object);
        return result;
    }

    public static Result success(String message) {
        Result result = new Result();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    public static Result success(String message, Object object) {
        Result result = new Result();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMessage(message);
        result.setData(object);
        return result;
    }

    public static Result success() {
        return ResultUtil.success(null);
    }

    public static Result error(ResultEnum resultEnum, String message) {
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMessage(resultEnum.getMessage() + message);
        return result;
    }

    public static Result error(ResultEnum resultEnum) {
        Result result = new Result();
        result.setCode(resultEnum.getCode());
        result.setMessage(resultEnum.getMessage());
        return result;
    }

    public static Result error(Exception ex) {
        Result result = new Result();
        result.setCode(ResultEnum.ERROR.getCode());
        result.setMessage(ex.getMessage());
        return result;
    }

    public static Result error(String message) {
        Result result = new Result();
        result.setCode(ResultEnum.FAIL.getCode());
        result.setMessage(message);
        return result;
    }
}
