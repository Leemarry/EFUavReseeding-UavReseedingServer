package com.bear.reseeding.model;

import java.io.Serializable;

/**
 * @Auther: bear
 * @Date: 2021/7/15 17:26
 * @Description: null
 */
public class Result implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 返回状态码,成功与失败标识
     */
    private Integer code;
    /**
     * 提示消息,用于在页面提示的消息信息
     */
    private String message;
    /**
     * 消息ID
     */
    private Integer messageId;
    /**
     * 如果有分页用到总数
     */
    private Integer dataCount;
    /**
     * 返回数据,json格式，必须包含： msgid(用于区分回复的信息),
     */
    private Object data;

    /**
     * @Title: getCode
     * @Description:
     * @return: Integer
     */

    public Integer getCode() {
        return code;
    }

    /**
     * @Title: setCode
     * @Description: please write your description
     * @return: Integer
     */

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    /**
     * @Title: getMessage
     * @Description:
     * @return: String
     */

    public String getMessage() {
        return message;
    }


    /**
     * @Title: setMessage
     * @Description: please write your description
     * @return: String
     */

    public void setMessage(String message) {
        this.message = message;
    }


    /**
     * @Title: getData
     * @Description:
     * @return: Object
     */

    public Object getData() {
        return data;
    }


    /**
     * @Title: setData
     * @Description: please write your description
     * @return: Object
     */

    public void setData(Object data) {
        this.data = data;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Result [code=" + code + ", message=" + message + ", data=" + data + "]";
    }


}

