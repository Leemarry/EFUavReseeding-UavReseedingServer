package com.bear.reseeding.eflink;



import com.bear.reseeding.utils.BytesUtil;
import com.bear.reseeding.utils.LogUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 大疆单个航点的指令内容
 */
public class WaypointDjiAction implements Serializable {

    private int actionType;

    private int actionParam;

    @Override
    public String toString() {
        return "WaypointDjiAction{" +
                "actionType=" + actionType +
                ", actionParam=" + actionParam +
                '}';
    }

    public void unpacket(byte[] packet, int index) {
        try {
            actionType = packet[index] & 0xFF;
            index++;
            actionParam = BytesUtil.bytes2Int(packet, index);
            index += 4;
        } catch (Exception e) {
            LogUtil.logError("解析单个航点动作指令数据失败：" + e.getMessage());
        }
    }

    public byte[] packet() {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) (actionType));
        buffer.put(BytesUtil.int2Bytes(actionParam));
        return buffer.array();
    }


    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public int getActionParam() {
        return actionParam;
    }

    public void setActionParam(int actionParam) {
        this.actionParam = actionParam;
    }
}
