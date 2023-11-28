package com.bear.reseeding.datalink;

import com.alibaba.fastjson.JSONObject;
import com.bear.reseeding.MyApplication;
import com.bear.reseeding.common.ResultUtil;
import com.bear.reseeding.eflink.*;
import com.bear.reseeding.eflink.enums.EF_PARKING_APRON_ACK;
import com.bear.reseeding.eflink.enums.EF_PARKING_APRON_PROGRESS;
import com.bear.reseeding.entity.EfUavRealtimedata;
import com.bear.reseeding.model.Uint32;
import com.bear.reseeding.service.EfUavEachsortieService;
import com.bear.reseeding.service.EfUavRealtimedataService;
import com.bear.reseeding.task.TaskUavHeartbeat;
import com.bear.reseeding.utils.BytesUtil;
import com.bear.reseeding.utils.LogUtil;
import com.bear.reseeding.utils.RedisUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: bear
 * @Date: 2021/12/27 15:51
 * @Description: null
 */
@Component
public class HandleMqttMessageDji {

    @Resource
    EfUavRealtimedataService realtimedataService;

    @Resource
    EfUavEachsortieService efUavEachsortieService;

    @Resource
    TaskUavHeartbeat taskUavHeartbeat;


    public void unPacket(RedisUtils redisUtils, String topic, String deviceId, byte[] packet) {
        DoPacket(redisUtils, packet, "", deviceId);
    }

    //解析dji无人机数据保存到数据库中
    private void DoPacket(RedisUtils redisUtils, byte[] packet, String userid, String uavSn) {
        //根据 uavid 获取 userid
        String[] owerUsers = new String[0];
        String uavIdSystem = null;
        if (redisUtils != null) {
            //  根据 uavsn 获取 userid
            Object obj = redisUtils.hmGet("rel_uav_sn_id", uavSn); //根据无人机SN获取无人机ID  2,1,
            if (obj != null) {
                uavIdSystem = obj.toString();
                obj = redisUtils.hmGet("rel_uavid_userid", uavIdSystem); //无人机ID获取用户ID  2,1,
                if (obj != null) {
                    String delims = "[,]+";
                    owerUsers = obj.toString().split(delims);
                }
            }
        }
        if (uavIdSystem == null) {
            if (redisUtils != null) {
                if (redisUtils.get("sys_nocfg_uav_" + uavSn) == null) {
                    LogUtil.logWarn("MQTT：系统未配置无人机[" + uavSn + "]！");
                    redisUtils.set("sys_nocfg_uav_" + uavSn, uavSn, 30L, TimeUnit.SECONDS);
                }
            }
            return;
        }
        if (owerUsers.length <= 0) {
            LogUtil.logWarn("MQTT：无人机[" + uavSn + "," + uavIdSystem + "]未绑定用户！");
        }
        // 找到该设备所属公司
        Object obj = redisUtils.hmGet("rel_uavid_companyid", uavIdSystem);
        if (obj != null) {
            // 找到公司下的所有管理员
            obj = redisUtils.hmGet("rel_companyid_usersid", obj);
            if (obj != null) {
                // 有管理员
                String delims = "[,]+";
                String[] users = obj.toString().split(delims);
                if (owerUsers.length <= 0) {
                    owerUsers = users;
                } else {
                    owerUsers = (String[]) ArrayUtils.addAll(owerUsers, users);
                }
                if (owerUsers != null && owerUsers.length > 0) {
                    List<String> list = new ArrayList<>();
                    for (String id : owerUsers) {
                        if (!list.contains(id)) {
                            list.add(id);
                        }
                    }
                    owerUsers = new String[list.size()];
                    list.toArray(owerUsers);
                }
            }
        }
        int Index = 3;
        int seqid = packet[Index] & 0xFF; //序号
        Index++;
        Uint32 sysid = new Uint32(BytesUtil.bytes2UInt(packet, Index)); //系统ID
        Index += 4;
        int comid = packet[Index] & 0xFF; //组件ID
        Index++;
        int ver = packet[Index] & 0xFF; //协议版本
        Index++;
        int msgid = BytesUtil.bytes2UShort(packet, Index); //消息编号
        Index += 2;
        int deviceid = 0; //机巢编号，预留位
        int tag = 0;
        int ack = 0;
        long timeNow = System.currentTimeMillis();
        switch (msgid) {
            case 2004:
                //region 全自动指令调用后实时回复，会回复多次
                tag = packet[Index] & 0xFF;  //标记
                Index++;
                int cmd = BytesUtil.bytes2UShort(packet, Index);  // 命令
                Index += 2;
                int Progress = BytesUtil.bytes2UShort(packet, Index);  //进度位置
                String ackString = String.valueOf(Progress);
                //endregion
                break;
            case 2250:
                //dji服务后台的心跳包，包含 地面站在线状态，无人机在线状态，启动时长
                EFLINK_MSG_2250 eflinkMsg = new EFLINK_MSG_2250();
                eflinkMsg.InitPacket(packet, Index);
//                LogUtil.logMessage("接收大疆服务后台[" + uavSn + "]心跳包：" + eflinkMsg.toString());
                WebSocketLink.push(ResultUtil.success(msgid, uavIdSystem, null, eflinkMsg), owerUsers);
                break;
            case 2200:
                Index += 2;
                //region 心跳包数据
                EfUavRealtimedata realtimedata = new EfUavRealtimedata();
                realtimedata.unPacket(packet, Index);
                realtimedata.setUavId(uavIdSystem);
                WebSocketLink.push(ResultUtil.success(msgid, uavIdSystem, null, realtimedata), owerUsers);
                realtimedata.setUavId(uavIdSystem);
                // 储存架次和实时数据
                taskUavHeartbeat.addQueue(realtimedata);
//                LogUtil.logMessage("接收大疆无人机[" + uavIdSystem + "]心跳包：" + realtimedata.toString());
                //endregion 添加飞行架次记录 end
                break;
            case 2201:  //指令ACK
                deviceid = 0;
                Index += 2;
                cmd = packet[Index] & 0xFF;  //命令
                Index++;
                tag = packet[Index] & 0xFF;  //标记
                Index++;
                ack = packet[Index] & 0xFF;  //结果
                Index++;
                break;
            case 2202:
                //控制指令
                //S-C 服务器发送控制指令到客户端。包含起飞、执行任务、返航、降落等常用指令。
                //C-S 客户端发送常用指令到服务器。
                deviceid = 0;
                Index += 2;
                cmd = packet[Index] & 0xFF;  //命令
                Index++;
                tag = packet[Index] & 0xFF;  //标记
                Index++;
                //将内容解析成json后转发至前台页面
            case 2203:  //控制指令带参,服务端到客户端
                break;
            case 2210:
                //region 任务内容，即前台下载当前无人机的任务信息,解析后发送到前台页面
                deviceid = 0;
                Index += 2;
                tag = packet[Index] & 0xFF;  //标记
                Index++;
                //解析任务
                int wpscount = BytesUtil.bytes2UShort(packet, Index);  //任务总个数
                Index += 2;
                int wpNo = BytesUtil.bytes2UShort(packet, Index);  //当前任务序号，从0开始
                Index += 2;
                double lat = BytesUtil.bytes2Int(packet, Index) / 1e7;  //纬度
                Index += 4;
                double lng = BytesUtil.bytes2Int(packet, Index) / 1e7;  //经度
                Index += 4;
                float alt = BytesUtil.bytes2Int(packet, Index) / 100f;  //高度 cm
                Index += 4;
                float altabs = BytesUtil.bytes2Int(packet, Index) / 100f;  //海拔 cm
                Index += 4;
                float xyspeed = BytesUtil.bytes2Short(packet, Index) / 100f;  //水平速度 cm/s
                Index += 2;
                float zspeed = BytesUtil.bytes2Short(packet, Index) / 100f;  //垂直速度 cm/s
                Index += 2;
                float p1 = BytesUtil.bytes2Float(packet, Index);  //参数1
                Index += 4;
                float p2 = BytesUtil.bytes2Float(packet, Index);  //参数2
                Index += 4;
                float p3 = BytesUtil.bytes2Float(packet, Index);  //参数3
                Index += 4;
                float p4 = BytesUtil.bytes2Float(packet, Index);  //参数4
                Index += 4;
                break;
            case 2211:  //解析接收到的全部任务内容
                deviceid = 0;
                Index += 2;
                tag = packet[Index] & 0xFF;  //标记
                Index++;
                break;
            case 2213:  //下载媒体文件的进度信息
                //region 下载进度
                deviceid = 0;
                Index += 2;
                cmd = packet[Index] & 0xFF;  //命令
                Index++;
                tag = packet[Index] & 0xFF;  //标记
                Index++;
                ack = packet[Index] & 0xFF;  //结果
                Index++;
                int TotalCount = BytesUtil.bytes2UShort(packet, Index);//总媒体文件数量
                Index += 2;
                int CurrentCount = BytesUtil.bytes2UShort(packet, Index);//当前下载的文件索引
                Index += 2;
                int CurrentPert = packet[Index] & 0xFF;  //当前下载文件的百分比
                Index += 1;
                long medianameTimespane = BytesUtil.bytes2Long(packet, Index);  //当前下载的媒体文件名称，即时间戳形式
                Index += 8;
                break;
            //endregion
            case 2214:
                //region 反馈返航点坐标

                //endregion
                break;
            case 3051:
                //region 控制指令回复
                EFLINK_MSG_3051 msg3051 = new EFLINK_MSG_3051();
                msg3051.unPacket(packet, Index);
                redisUtils.set(uavSn + "_" + msgid + "_" + msg3051.getTag(), msg3051.getAck(), 30L, TimeUnit.SECONDS);
                String key = uavSn + "_" + msgid + "_" + msg3051.getTag();
                synchronized (MyApplication.keyObj) {
                    try {
                        MyApplication.keyObj.put(key, msg3051);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //endregion
                break;
            case 3052:
                //region 全自动指令，后续回复
                EFLINK_MSG_3052 msg3052 = new EFLINK_MSG_3052();
                msg3052.unPacket(packet, Index);
                msg3052.setProgressTextDes(EF_PARKING_APRON_PROGRESS.msg(msg3052.getProgressText())); //进度文字提示
                if (msg3052.getStatus() < 0) {
                    msg3052.setErrorText(EF_PARKING_APRON_ACK.msg(msg3052.getStatus())); //错误提示文字
                } else {
                    msg3052.setErrorText("");  //错误提示文字
                }
                WebSocketLink.push(ResultUtil.success(msgid, uavIdSystem, null, msg3052), owerUsers);
                LogUtil.logInfo("#" + msgid + "-->无人机编号：" + uavSn + ",全自动指令后续回复 推送到前台:" + msg3052.toString());

                //endregion
                break;
            case 3105:
                //region 控制指令回复
                tag = packet[Index] & 0xFF;  //标记
                Index++;
                ack = packet[Index] & 0xFF;  //1：成功，0：失败
                Index++;
                redisUtils.set(uavSn + "_" + tag, (ack == 1), 30L, TimeUnit.SECONDS);
                //endregion
                break;
            case 3110:
                EFLINK_MSG_3110 msg_3110 = new EFLINK_MSG_3110();
                msg_3110.unPacket(packet);
                LogUtil.logInfo(msg_3110.toString());
                redisUtils.set(uavSn + "_" + msg_3110.getTag(), (msg_3110.getResult() == 1), 30L, TimeUnit.SECONDS);
                break;
            case 3101:
                EFLINK_MSG_3101 msg3101 = new EFLINK_MSG_3101();
                msg3101.unpacket(packet);
                LogUtil.logInfo(msg3101.toString());
                WebSocketLink.push(ResultUtil.success(msgid, uavIdSystem, null, msg3101), owerUsers);
                break;
            case 3108:
                EFLINK_MSG_3108 msg3108 = new EFLINK_MSG_3108();
                msg3108.unPacket(packet);
                WebSocketLink.push(ResultUtil.success(msgid, uavIdSystem, null, msg3108), owerUsers);
                redisUtils.set(uavSn + "_" + msgid + "_" + msg3108.getTag(), msg3108, 30L, TimeUnit.SECONDS);
                break;
            case 3118:
                EFLINK_MSG_3118 msg3118 = new EFLINK_MSG_3118();
                msg3118.unPacket(packet);
                WebSocketLink.push(ResultUtil.success(msgid, uavIdSystem, null, msg3118), owerUsers);
                redisUtils.set(uavSn + "_" + msgid + "_" + msg3118.getTag(), msg3118, 30L, TimeUnit.SECONDS);
                break;
            case 3109:
                EFLINK_MSG_3109 msg3109 = new EFLINK_MSG_3109();
                msg3109.unpacket(packet);
                WebSocketLink.push(ResultUtil.success(msgid, uavIdSystem, null, msg3109), owerUsers);
                redisUtils.set(uavSn + "_" + msgid + "_" + msg3109.getTag(), msg3109, 30L, TimeUnit.SECONDS);
                break;
            case 3119:
                EFLINK_MSG_3119 msg3119 = new EFLINK_MSG_3119();
                msg3119.unpacket(packet);
                WebSocketLink.push(ResultUtil.success(msgid, uavIdSystem, null, msg3119), owerUsers);
                redisUtils.set(uavSn + "_" + msgid + "_" + msg3119.getTag(), msg3119, 30L, TimeUnit.SECONDS);
                break;
            case 3123:
                EFLINK_MSG_3123 msg3123 = new EFLINK_MSG_3123();
                msg3123.unpacket(packet);
                WebSocketLink.push(ResultUtil.success(msgid, uavIdSystem, null, msg3123), owerUsers);
                redisUtils.set(uavSn + "_" + msgid + "_" + msg3123.getTag(), msg3123, 30L, TimeUnit.SECONDS);
                break;
            case 3126:
                EFLINK_MSG_3126 msg3126 = new EFLINK_MSG_3126();
                msg3126.unPacket(packet, Index);
                LogUtil.logInfo(msg3126.toString());
                redisUtils.set(uavSn + "_" + msgid + "_" + msg3126.getTag(), msg3126, 30L, TimeUnit.SECONDS);
                break;
            case 3200:
                EFLINK_MSG_3200 msg3200 = new EFLINK_MSG_3200();
                msg3200.InitPacket(packet, Index);
                WebSocketLink.push(ResultUtil.success(msgid, uavIdSystem, null, msg3200), owerUsers);
                redisUtils.set(uavSn + "_" + msgid + "_" + msg3200.getTag(), msg3200, 30L, TimeUnit.SECONDS);
                LogUtil.logInfo(msg3200.getStatusMessage());
                break;
            case 3202:
                EFLINK_MSG_3202 msg3202 = new EFLINK_MSG_3202();
                msg3202.InitPacket(packet, Index);
                redisUtils.set(uavSn + "_" + msgid + "_" + msg3202.getTag(), JSONObject.toJSONString(msg3202), 30L, TimeUnit.SECONDS);
                LogUtil.logInfo(msg3202.toString());
                break;
            case 3207:
                EFLINK_MSG_3207 msg3207 = new EFLINK_MSG_3207();
                msg3207.InitPacket(packet, Index);
                redisUtils.set(uavSn + "_" + msgid + "_" + msg3207.getTag(), msg3207, 30L, TimeUnit.SECONDS);
                break;
            case 3210:
                EFLINK_MSG_3210 msg3210 = new EFLINK_MSG_3210();
                msg3210.InitPacket(packet, Index);
                redisUtils.set(uavSn + "_" + msgid + "_" + msg3210.getTag(), msg3210, 30L, TimeUnit.SECONDS);
                LogUtil.logInfo(msg3210.toString());
                break;
            default:
                break;
        }
    }
}
