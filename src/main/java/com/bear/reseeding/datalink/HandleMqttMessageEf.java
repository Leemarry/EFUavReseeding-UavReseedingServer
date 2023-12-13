package com.bear.reseeding.datalink;

import com.bear.reseeding.common.ResultUtil;
import com.bear.reseeding.eflink.EFLINK_MSG_2000;
import com.bear.reseeding.eflink.EFLINK_MSG_3110;
import com.bear.reseeding.entity.EfUavRealtimedata;
import com.bear.reseeding.model.Uint32;
import com.bear.reseeding.task.TaskUavHeartbeat;
import com.bear.reseeding.utils.BytesUtil;
import com.bear.reseeding.utils.LogUtil;
import com.bear.reseeding.utils.RedisUtils;
import org.apache.commons.lang.ArrayUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @Auther: bear
 * @Date: 2021/12/27 15:51
 * @Description: null
 */
public class HandleMqttMessageEf {
    private static TaskUavHeartbeat taskUavHeartbeat;

    public static void unPacket(RedisUtils redisUtils, String topic, String deviceId, byte[] packet) {
        DoPacketEfuav(redisUtils, packet, "", deviceId);
    }

    //解析翼飞无人机数据保存到数据库中
    private static void DoPacketEfuav(RedisUtils redisUtils, byte[] packet, String userid, String uavid) {
        try {
            if (redisUtils == null) {
                throw new Exception("RedisUtils对象为空！");
            }
            //根据 uavid 获取 userid
            String[] owerUsers = new String[0];
            String uavIdSystem = null;
            //  根据 uavsn 获取 userid
            Object obj = redisUtils.hmGet("rel_uav_sn_id", uavid);
            if (obj != null) {
                uavIdSystem = obj.toString();
                obj = redisUtils.hmGet("rel_uavid_userid", uavIdSystem);
                if (obj != null) {
                    String delims = "[,]+";
                    owerUsers = obj.toString().split(delims);
                }
            }
            if (uavIdSystem == null) {
                if (redisUtils.get("sys_nocfg_uav_" + uavid) == null) {
                    LogUtil.logWarn("MQTT：系统未配置无人机[" + uavid + "]！");
                    redisUtils.set("sys_nocfg_uav_" + uavid, uavid, 30L, TimeUnit.SECONDS);
                }
                return;
            }
            if (owerUsers.length <= 0) {
                LogUtil.logWarn("MQTT：无人机[" + uavid + "," + uavIdSystem + "]未绑定用户！");
            }
            // 找到该设备所属公司
            obj = redisUtils.hmGet("rel_uavid_companyid", uavIdSystem);
            if (obj != null) {
                // 找到公司下的所有管理员
                obj = redisUtils.hmGet("rel_companyid_usersid", obj);
                if (obj != null) {
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
            int Index = 1;
            int LoadLength = (int) BytesUtil.bytes2UShort(packet, Index);
            Index += 2;
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
            int tag = 0;
            int ack = 0;
            long timeNow = System.currentTimeMillis();
            switch (msgid) {
                case 2000:
                    //region 心跳包数据推送至前台
                    EFLINK_MSG_2000 eflink_msg_2000 = new EFLINK_MSG_2000();
                    eflink_msg_2000.InitPacket(packet,12);
                    //赋值心跳包数据，准备推送前台
                    EfUavRealtimedata realtimedata = new EfUavRealtimedata();
                    realtimedata.setUavId(uavid);
                    realtimedata.setDataDate(new Date());
                    realtimedata.setLat((double)eflink_msg_2000.getLat());
                    realtimedata.setLng((double)eflink_msg_2000.getLng());
                    realtimedata.setAltabs((float) eflink_msg_2000.getAltAbs());
                    realtimedata.setAlt((float) eflink_msg_2000.getAltRel());
                    realtimedata.setXySpeed((float) eflink_msg_2000.getXYSpeed());
                    realtimedata.setZSpeed((float) eflink_msg_2000.getZSpeed());
                    realtimedata.setFlightMode(String.valueOf(eflink_msg_2000.getMode()));
                    realtimedata.setRoll(eflink_msg_2000.getRoll());
                    realtimedata.setPitch(eflink_msg_2000.getPitch());
                    realtimedata.setYaw(eflink_msg_2000.getYaw());
                    realtimedata.setBatteryValue((float) eflink_msg_2000.getBattPert());
                    realtimedata.setLinkAirDownload(eflink_msg_2000.getDownlinkSignalQuality());
                    realtimedata.setLinkAirUpload(eflink_msg_2000.getUplinkSignalQuality());
                    realtimedata.setGpsStatus((float) eflink_msg_2000.getGpsStatus());
                    realtimedata.setSatecount(eflink_msg_2000.getSatelliteCount());
                    realtimedata.setPdop(eflink_msg_2000.getGpsHdop());
                    realtimedata.setSystemStatus(eflink_msg_2000.getSystemStatus());
                    realtimedata.setAremd(eflink_msg_2000.getArmed());

                    if (owerUsers != null && owerUsers.length > 0) {
                        WebSocketLink.push(ResultUtil.success(msgid, uavIdSystem, null, realtimedata), owerUsers);
                    }
                    realtimedata.setUavId(uavIdSystem);
                    // 储存架次和实时数据
                    taskUavHeartbeat.addQueue(realtimedata);
                    //endregion
                    break;
                case 3050:
                    //region 控制指令回复
                    tag = packet[Index] & 0xFF; //标记
                    Index++;
                    int cmd = BytesUtil.bytes2UShort(packet, Index);  //命令
                    Index += 2;
                    ack = packet[Index] & 0xFF;  //结果
                    Index++;
                    String ackString = "未知状态"; //EF_DJI_ACK.msg(ack);//MAV_RESULT
                    if (ack == 0) {
                        ackString = "成功";
                    } else if (ack == 1) {
                        ackString = "无法执行命令";
                    } else {

                    }
                    //endregion
                    break;
                case 2002:
                    //region 远程控制指令回复
                    tag = packet[Index] & 0xFF;  //标记
                    Index++;
                    cmd = BytesUtil.bytes2UShort(packet, Index);  //命令
                    Index += 2;
                    ack = packet[Index] & 0xFF;  //结果
                    Index++;
                    ackString = "未知状态"; //EF_DJI_ACK.msg(ack);//MAV_RESULT
                    if (ack == 0) {
                        ackString = "成功";
                    } else if (ack == 1) {
                        ackString = "无法执行命令";
                    } else {

                    }
                    //endregion
                    break;
                case 2004:
                    //region 全自动指令调用后实时回复，会回复多次
                    tag = packet[Index] & 0xFF;  //标记
                    Index++;
                    cmd = BytesUtil.bytes2UShort(packet, Index);  // 命令
                    Index += 2;
                    int Progress = BytesUtil.bytes2UShort(packet, Index);  //进度位置
                    //endregion
                    break;
                case 2011:
                    //region 航线发送到客户端回复
                    tag = packet[Index] & 0xFF;  //标记
                    Index++;
                    cmd = BytesUtil.bytes2UShort(packet, Index);  //命令
                    Index += 2;
                    ack = packet[Index] & 0xFF;  //结果
                    Index++;
                    ackString = "未知状态"; //EF_DJI_ACK.msg(ack);//MAV_RESULT
                    if (ack == 0) {
                        ackString = "成功";
                    } else if (ack == 1) {
                        ackString = "无法执行命令";
                    } else {

                    }
                    //endregion
                    break;
                case 2012:
                    //region 客户端在上传任务到无人机时，实时发送到服务器的信息，用于在页面上显示进度信息
                    tag = packet[Index] & 0xFF;  //标记
                    Index++;
                    ack = packet[Index];  //状态
                    Index++;
                    int pert = packet[Index] & 0xFF;  //进度 0-100
                    Index++;
                    if (ack == 0) {
                        ackString = "正在同步航线到无人机...";
                    } else if (ack == 1) {
                        ackString = "同步航线到无人机成功";
                    } else if (ack == -1) {
                        ackString = "同步航线到无人机失败！";
                    } else if (ack == -2) {
                        ackString = "接收航线信息失败！";
                    } else {
                        ackString = "未知状态";
                    }
                    cmd = 0;
                    //endregion
                    break;
                case 2021:
                    //region 远程遥控回复
                    tag = packet[Index] & 0xFF;  //标记
                    Index++;
                    ack = packet[Index] & 0xFF;   //状态
                    Index++;
                    ackString = "未知状态"; //EF_DJI_ACK.msg(ack);//MAV_RESULT
                    if (ack == 0) {
                        ackString = "未知状态";
                    } else if (ack == 1) {
                        ackString = "启动远程遥控成功";
                    } else if (ack == 2) {
                        ackString = "关闭远程遥控成功";
                    } else if (ack == 3) {
                        ackString = "启动失败, 当前无控制权!";
                    }
                    cmd = 0;
                    //endregion
                    break;
                case 2010:
                    //region 服务器请求下载航线任务  回复航线信息
                    tag = packet[Index] & 0xFF;  //标记
                    Index++;
                    int WpAllCount = BytesUtil.bytes2UShort(packet, Index);
                    Index += 2;
                    int WpCount = BytesUtil.bytes2UShort(packet, Index);
                    Index += 2;
                    //endregion
                    break;
                case 2051:
                    //region 载荷透传控制指令回复
                    tag = packet[Index] & 0xFF;  //标记
                    Index++;
                    int loadid = BytesUtil.bytes2UShort(packet, Index);
                    Index += 2;
                    int loadCount = Math.min(LoadLength - 3, packet.length - 14 - 3); //LoadLength == packet.length - 14
                    byte[] load = new byte[loadCount];
                    for (int i = 0; i < loadCount; i++) {
                        load[i] = packet[Index];
                        Index++;
                    }
                    String loadString = "";
                    try {
                        loadString = new String(load, "utf-8");
                        loadString = loadString.trim();
                    } catch (Exception e) {
                    }
                    //endregion
                    break;
                case 3105:
                    //region 控制指令回复
                    tag = packet[Index] & 0xFF;  //标记
                    Index++;
                    ack = BytesUtil.bytes2UShort(packet, Index);  //1：成功，0：失败
                    Index += 2;
                    redisUtils.set(uavid + "_" + tag, (ack == 1), 30L, TimeUnit.SECONDS);
                    //endregion
                    break;
                case 3110:
                    EFLINK_MSG_3110 msg_3110 = new EFLINK_MSG_3110();
                    msg_3110.unPacket(packet);
                    LogUtil.logInfo(msg_3110.toString());
                    redisUtils.set(uavid + "_" + msg_3110.getTag(), (msg_3110.getResult() == 1), 30L, TimeUnit.SECONDS);
                    break;
                default:
                    // 其他消息类型处理
                    break;
            }
        } catch (Exception e) {
            LogUtil.logError("MQTT：处理Efuav消息异常！" + e.getMessage());
        }
    }
}
