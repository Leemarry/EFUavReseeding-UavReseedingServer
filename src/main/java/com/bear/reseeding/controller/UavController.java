package com.bear.reseeding.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bear.reseeding.MyApplication;
import com.bear.reseeding.common.ResultUtil;
import com.bear.reseeding.datalink.EfLinkUtil;
import com.bear.reseeding.datalink.MqttUtil;
import com.bear.reseeding.eflink.EFLINK_MSG_3050;
import com.bear.reseeding.eflink.enums.EF_PARKING_APRON_ACK;
import com.bear.reseeding.entity.*;
import com.bear.reseeding.model.CurrentUser;
import com.bear.reseeding.eflink.EFLINK_MSG_3121;
import com.bear.reseeding.eflink.EFLINK_MSG_3123;
import com.bear.reseeding.model.Result;
import com.bear.reseeding.service.*;
import com.bear.reseeding.task.TaskAnsisPhoto;
import com.bear.reseeding.task.MinioService;
import com.bear.reseeding.utils.*;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.live.v20180801.LiveClient;
import com.tencentcloudapi.live.v20180801.models.CreateRecordTaskRequest;
import com.tencentcloudapi.live.v20180801.models.CreateRecordTaskResponse;
import com.tencentcloudapi.live.v20180801.models.StopRecordTaskRequest;
import com.tencentcloudapi.live.v20180801.models.StopRecordTaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.synth.Region;
import java.io.File;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 无人机管理
 *
 * @author bear
 * @since 2023-11-10 16:12:45
 */
@RestController
@RequestMapping("uav")
public class UavController {
    @Autowired
    private RedisUtils redisUtils;
    @Resource
    private EfUavService efUavService;
    @Resource
    private TaskAnsisPhoto taskAnsisPhoto;

    @Resource
    private EfUavEachsortieService efUavEachsortieService;

    @Resource
    private EfMediaPhotoService efMediaPhotoService;

    @Resource
    private EfCavityService efCavityService;

    @Resource
    private EfCavitySeedingService efCavitySeedingService;

    /**
     * 无人机与公司关联
     */
    @Resource
    private EfRelationCompanyUavService efRelationCompanyUavService;

    /**
     * 无人机 与用户关联
     */
    @Resource
    private EfRelationUserUavService efRelationUserUavService;


    @Value("${BasePath:C://efuav/reseeding/}")
    public String basePath;


    /**
     * minio
     */
    @Resource
    private MinioService minioService;

    @Value("${minio.BucketNameKmz}")
    private String BucketNameKmz;

    @Value("${spring.application.name}")
    private String applicationName;
    /**
     * 加密
     */
    @Value("${spring.config.encryptMd5Soft:efuav201603}")
    String encryptMd5Soft;

    /**
     * 获取所有权限的无人机
     *
     * @return Result {data:token}
     */
    @ResponseBody
    @PostMapping(value = "/getUavs")
    public Result getUavs(@CurrentUser EfUser currentUser, HttpServletRequest request) {
        try {

            Integer cId = currentUser.getUCId();  //公司Id
            Integer urId = currentUser.getURId(); //角色id
            List<EfUav> efUavList = new ArrayList<>();
            //   List<EfUav> efUavs = efUavService.queryUavs(currentUser);
            //    通过公司id查询到无人机与公司关联表
            if (urId == 1 || urId == 2 || urId == 3) {
                List<EfRelationCompanyUav> efRelationCompanyUavList = efRelationCompanyUavService.queryAllUavByCIdOrUrId(cId, urId);
                int a = efRelationCompanyUavList.size();
                if (a > 0) {
                    efRelationCompanyUavList.forEach(efRelationCompanyUav -> {
                        EfUav efUav = efRelationCompanyUav.getEfUav();
                        efUavList.add(efUav);
                    });
                }
            } else {
                List<EfRelationUserUav> efRelationUserUavList = efRelationUserUavService.queryByUrid(urId);
                int a = efRelationUserUavList.size();
                if (a > 0) {
                    efRelationUserUavList.forEach(efRelationUserUav -> {
                        EfUav efUav = efRelationUserUav.getEfUav();
                        efUavList.add(efUav);
                    });
                }
            }
            return ResultUtil.success("查询无人机成功", efUavList);
        } catch (Exception e) {
            LogUtil.logError("获取所有权限的无人机异常：" + e.toString());
            return ResultUtil.error("获取所有权限的无人机异常,请联系管理员!");
        }
    }

    //region 通用无人机控制

    /**
     * TODO 起飞无人机
     *
     * @param uavId 无人机ID
     * @param alt   起飞高度
     * @return 成功，失败
     */
    @ResponseBody
    @PostMapping(value = "/takeoff")
    public Result takeoff(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        try {
            int timeout = Integer.parseInt(map.getOrDefault("timeout", "5").toString()) * 1000;
            String uavId = map.getOrDefault("uavId", "").toString();
            String tag = map.getOrDefault("tag", "0").toString();
            String hiveId = map.getOrDefault("hiveId", "").toString();
            String command = map.getOrDefault("command", "0").toString();
            String parm1 = map.getOrDefault("parm1", "0").toString();
            String parm2 = map.getOrDefault("parm2", "0").toString();
            String parm3 = map.getOrDefault("parm3", "0").toString();
            String parm4 = map.getOrDefault("parm4", "0").toString();
            if ("".equals(uavId)) {
                return ResultUtil.error("请选择无人机！");
            }
            /**
             *redis存储的对应id
             */
            Object obj = redisUtils.hmGet("rel_uav_id_sn", uavId); //根据无人机SN获取无人机ID  2,1,
            if (obj != null) {
                uavId = obj.toString();
            }

            //1.打包3050上传等待
            EFLINK_MSG_3050 eflink_msg_3050 = new EFLINK_MSG_3050();
            eflink_msg_3050.setTag(Integer.parseInt(tag));
            eflink_msg_3050.setCommand(Integer.parseInt(command));
            eflink_msg_3050.setParm1(Integer.parseInt(parm1));
            eflink_msg_3050.setParm2(Integer.parseInt(parm2));
            eflink_msg_3050.setParm3(Integer.parseInt(parm3));
            eflink_msg_3050.setParm4(Integer.parseInt(parm4));
            byte[] packet = EfLinkUtil.Packet(eflink_msg_3050.EFLINK_MSG_ID, eflink_msg_3050.packet());

            boolean onlyPushToHive = false;
            //2.推送到mqtt,返回3052判断
            long startTime = System.currentTimeMillis();
            String keyHive = null;
            boolean goon = false;
            String error = "未知错误！";
            if (null != hiveId && !"".equals(hiveId)) {
                keyHive = hiveId + "_" + 3051 + "_" + tag;
                redisUtils.remove(keyHive);
                MqttUtil.publish(MqttUtil.Tag_Hive, packet, hiveId);
                if ("2003".equals(hiveId)) {
                    onlyPushToHive = true;
                }
            }
            String key = uavId + "_" + 3051 + "_" + tag;
            if (!onlyPushToHive) {
                redisUtils.remove(key);
                MqttUtil.publish(MqttUtil.Tag_Djiapp, packet, uavId);
            }

            while (true) {
                Object ack = redisUtils.get(key);
                if (!onlyPushToHive && ack != null) {
                    error = EF_PARKING_APRON_ACK.msg((Integer) ack);
                    goon = ((Integer) ack == 1);
                    redisUtils.remove(key);
                    break;
                }
                if (keyHive != null) {
                    ack = redisUtils.get(keyHive);
                    if (ack != null) {
                        error = EF_PARKING_APRON_ACK.msg((Integer) ack);
                        goon = ((Integer) ack == 1);
                        redisUtils.remove(keyHive);
                        break;
                    }
                }
                if (timeout + startTime < System.currentTimeMillis()) {
                    error = "无人机未响应！";
                    break;
                }
                Thread.sleep(50);
            }
            if (!goon) {
                return ResultUtil.error(error);
            }
            return ResultUtil.success(error);
        } catch (Exception e) {
            LogUtil.logError("起飞无人机异常：" + e.toString());
            return ResultUtil.error("起飞无人机异常,请联系管理员!");
        }
    }


    /**
     * 控制命令  降落无人机--1103
     *
     * @param map 标识 tag
     *            timeout 超时时间，秒
     *            uavId 无人机编号
     *            hiveId 停机坪编号，如果停机坪编号不为空，同时给推送消息给停机坪
     *            command 命令字
     *            parm1 参数1
     *            parm2 参数2
     *            parm3 参数3
     *            parm4 参数4
     * @return 成功，失败
     */
    @ResponseBody
    @PostMapping(value = "/land")
    public Result land(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        try {

            int timeout = Integer.parseInt(map.getOrDefault("timeout", "5").toString()) * 1000;
            String uavId = map.getOrDefault("uavId", "").toString();
            String tag = map.getOrDefault("tag", "0").toString();
            String hiveId = map.getOrDefault("hiveId", "").toString();
            String command = map.getOrDefault("command", "0").toString();
            String parm1 = map.getOrDefault("parm1", "0").toString();
            String parm2 = map.getOrDefault("parm2", "0").toString();
            String parm3 = map.getOrDefault("parm3", "0").toString();
            String parm4 = map.getOrDefault("parm4", "0").toString();
            if ("".equals(uavId)) {
                return ResultUtil.error("请选择无人机！");
            }
            /**
             *redis存储的对应id
             */
            Object obj = redisUtils.hmGet("rel_uav_id_sn", uavId); //根据无人机SN获取无人机ID  2,1,
            if (obj != null) {
                uavId = obj.toString();
            }

            //1.打包3050上传等待
            EFLINK_MSG_3050 eflink_msg_3050 = new EFLINK_MSG_3050();
            eflink_msg_3050.setTag(Integer.parseInt(tag));
            eflink_msg_3050.setCommand(Integer.parseInt(command));
            eflink_msg_3050.setParm1(Integer.parseInt(parm1));
            eflink_msg_3050.setParm2(Integer.parseInt(parm2));
            eflink_msg_3050.setParm3(Integer.parseInt(parm3));
            eflink_msg_3050.setParm4(Integer.parseInt(parm4));
            byte[] packet = EfLinkUtil.Packet(eflink_msg_3050.EFLINK_MSG_ID, eflink_msg_3050.packet());

            boolean onlyPushToHive = false;
            //2.推送到mqtt,返回3052判断
            long startTime = System.currentTimeMillis();
            String keyHive = null;
            boolean goon = false;
            String error = "未知错误！";
            if (null != hiveId && !"".equals(hiveId)) {
                keyHive = hiveId + "_" + 3051 + "_" + tag;
                redisUtils.remove(keyHive);
                MqttUtil.publish(MqttUtil.Tag_Hive, packet, hiveId);
                if ("2003".equals(hiveId)) {
                    onlyPushToHive = true;
                }
            }
            String key = uavId + "_" + 3051 + "_" + tag;
            if (!onlyPushToHive) {
                redisUtils.remove(key);
                MqttUtil.publish(MqttUtil.Tag_Djiapp, packet, uavId);
            }

            while (true) {
                Object ack = redisUtils.get(key);
                if (!onlyPushToHive && ack != null) {
                    error = EF_PARKING_APRON_ACK.msg((Integer) ack);
                    goon = ((Integer) ack == 1);
                    redisUtils.remove(key);
                    break;
                }
                if (keyHive != null) {
                    ack = redisUtils.get(keyHive);
                    if (ack != null) {
                        error = EF_PARKING_APRON_ACK.msg((Integer) ack);
                        goon = ((Integer) ack == 1);
                        redisUtils.remove(keyHive);
                        break;
                    }
                }
                if (timeout + startTime < System.currentTimeMillis()) {
                    error = "无人机未响应！";
                    break;
                }
                Thread.sleep(50);
            }
            if (!goon) {
                return ResultUtil.error(error);
            }
            return ResultUtil.success(error);

        } catch (Exception e) {
            LogUtil.logError("降落无人机异常：" + e.toString());
            return ResultUtil.error("降落无人机异常,请联系管理员!");
        }
    }



    /**
     * 控制命令  返航无人机--1102  1109一键全自动返航
     *
     * @param map 标识 tag
     *            timeout 超时时间，秒
     *            uavId 无人机编号
     *            hiveId 停机坪编号，如果停机坪编号不为空，同时给推送消息给停机坪
     *            command 命令字
     *            parm1 参数1
     *            parm2 参数2
     *            parm3 参数3
     *            parm4 参数4
     * @return 成功，失败
     */
    @ResponseBody
    @PostMapping(value = "/rtl")
    public Result rtl(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        try {
            int timeout = Integer.parseInt(map.getOrDefault("timeout", "5").toString()) * 1000;
            String uavId = map.getOrDefault("uavId", "").toString();
            String tag = map.getOrDefault("tag", "0").toString();
            String hiveId = map.getOrDefault("hiveId", "").toString();
            String command = map.getOrDefault("command", "0").toString();
            String parm1 = map.getOrDefault("parm1", "0").toString();
            String parm2 = map.getOrDefault("parm2", "0").toString();
            String parm3 = map.getOrDefault("parm3", "0").toString();
            String parm4 = map.getOrDefault("parm4", "0").toString();
            if ("".equals(uavId)) {
                return ResultUtil.error("请选择无人机！");
            }
            //1.打包3050上传等待
            EFLINK_MSG_3050 eflink_msg_3050 = new EFLINK_MSG_3050();
            eflink_msg_3050.setTag(Integer.parseInt(tag));
            eflink_msg_3050.setCommand(Integer.parseInt(command));
            eflink_msg_3050.setParm1(Integer.parseInt(parm1));
            eflink_msg_3050.setParm2(Integer.parseInt(parm2));
            eflink_msg_3050.setParm3(Integer.parseInt(parm3));
            eflink_msg_3050.setParm4(Integer.parseInt(parm4));
            byte[] packet = EfLinkUtil.Packet(eflink_msg_3050.EFLINK_MSG_ID, eflink_msg_3050.packet());

            boolean onlyPushToHive = false;
            //2.推送到mqtt,返回3052判断
            long startTime = System.currentTimeMillis();
            String keyHive = null;
            boolean goon = false;
            String error = "未知错误！";
            if (null != hiveId && !"".equals(hiveId)) {
                keyHive = hiveId + "_" + 3051 + "_" + tag;
                redisUtils.remove(keyHive);
                MqttUtil.publish(MqttUtil.Tag_Hive, packet, hiveId);
                if ("2003".equals(hiveId)) {
                    onlyPushToHive = true;
                }
            }
            String key = uavId + "_" + 3051 + "_" + tag;
            if (!onlyPushToHive) {
                redisUtils.remove(key);
                MqttUtil.publish(MqttUtil.Tag_Djiapp, packet, uavId);
            }

            while (true) {
                Object ack = redisUtils.get(key);
                if (!onlyPushToHive && ack != null) {
                    error = EF_PARKING_APRON_ACK.msg((Integer) ack);
                    goon = ((Integer) ack == 1);
                    redisUtils.remove(key);
                    break;
                }
                if (keyHive != null) {
                    ack = redisUtils.get(keyHive);
                    if (ack != null) {
                        error = EF_PARKING_APRON_ACK.msg((Integer) ack);
                        goon = ((Integer) ack == 1);
                        redisUtils.remove(keyHive);
                        break;
                    }
                }
                if (timeout + startTime < System.currentTimeMillis()) {
                    error = "无人机未响应！";
                    break;
                }
                Thread.sleep(50);
            }
            if (!goon) {
                return ResultUtil.error(error);
            }
            return ResultUtil.success(error);
        } catch (Exception e) {
            LogUtil.logError("返航无人机异常：" + e.toString());
            return ResultUtil.error("返航无人机异常,请联系管理员!");
        }
    }

    /**
     * 控制命令   TODO 开始执行任务
     *  uavId 无人机ID  @RequestParam(value = "uavId") String uavId,
     * @param map 标识 tag
     *            timeout 超时时间，秒
     *            uavId 无人机编号
     *            hiveId 停机坪编号，如果停机坪编号不为空，同时给推送消息给停机坪
     *            command 命令字  1006
     *            parm1 参数1
     *            parm2 参数2
     *            parm3 参数3
     *            parm4 参数4
     * @return 成功，失败
     */
    @ResponseBody
    @PostMapping(value = "/startMission")
    public Result startMission(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        try {
//            if("".equals(uavId)){
//                LogUtil.logError("未获取到无人机信息" );
//                return ResultUtil.error("未获取到无人机信息，请重新尝试!");
//            }
            int timeout = Integer.parseInt(map.getOrDefault("timeout", "5").toString()) * 1000;
            String uavId = map.getOrDefault("uavId", "").toString();
            String tag = map.getOrDefault("tag", "0").toString();
            String hiveId = map.getOrDefault("hiveId", "").toString();
            String command = map.getOrDefault("command", "0").toString();
            String parm1 = map.getOrDefault("parm1", "0").toString();
            String parm2 = map.getOrDefault("parm2", "0").toString();
            String parm3 = map.getOrDefault("parm3", "0").toString();
            String parm4 = map.getOrDefault("parm4", "0").toString();
            if ("".equals(uavId)) {
                return ResultUtil.error("请选择无人机！");
            }
            Object obj = redisUtils.hmGet("rel_uav_id_sn", uavId); //根据无人机SN获取无人机ID  2,1,
            if (obj != null) {
                uavId = obj.toString();
            }

            //1.打包3050上传等待
            EFLINK_MSG_3050 eflink_msg_3050 = new EFLINK_MSG_3050();
            eflink_msg_3050.setTag(Integer.parseInt(tag));
            eflink_msg_3050.setCommand(Integer.parseInt(command));
            eflink_msg_3050.setParm1(Integer.parseInt(parm1));
            eflink_msg_3050.setParm2(Integer.parseInt(parm2));
            eflink_msg_3050.setParm3(Integer.parseInt(parm3));
            eflink_msg_3050.setParm4(Integer.parseInt(parm4));
            byte[] packet = EfLinkUtil.Packet(eflink_msg_3050.EFLINK_MSG_ID, eflink_msg_3050.packet());

            boolean onlyPushToHive = false;
            //2.推送到mqtt,返回3052判断
            long startTime = System.currentTimeMillis();
            String keyHive = null;
            boolean goon = false;
            String error = "未知错误！";
            if (null != hiveId && !"".equals(hiveId)) {
                keyHive = hiveId + "_" + 3051 + "_" + tag;
                redisUtils.remove(keyHive);
                MqttUtil.publish(MqttUtil.Tag_Hive, packet, hiveId);
                if ("2003".equals(hiveId)) {
                    onlyPushToHive = true;
                }
            }
            String key = uavId + "_" + 3051 + "_" + tag;
            if (!onlyPushToHive) {
                redisUtils.remove(key);
                MqttUtil.publish(MqttUtil.Tag_Djiapp, packet, uavId);
            }

            while (true) {
                Object ack = redisUtils.get(key);
                if (!onlyPushToHive && ack != null) {
                    error = EF_PARKING_APRON_ACK.msg((Integer) ack);
                    goon = ((Integer) ack == 1);
                    redisUtils.remove(key);
                    break;
                }
                if (keyHive != null) {
                    ack = redisUtils.get(keyHive);
                    if (ack != null) {
                        error = EF_PARKING_APRON_ACK.msg((Integer) ack);
                        goon = ((Integer) ack == 1);
                        redisUtils.remove(keyHive);
                        break;
                    }
                }
                if (timeout + startTime < System.currentTimeMillis()) {
                    error = "无人机未响应！";
                    break;
                }
                Thread.sleep(50);
            }
            if (!goon) {
                return ResultUtil.error(error);
            }
            return ResultUtil.success(error);
        } catch (Exception e) {
            LogUtil.logError("开始执行任务异常：" + e.toString());
            return ResultUtil.error("开始执行任务异常,请联系管理员!");
        }
    }

    /**
     * 控制命令   TODO 暂停执行任务
     *  uavId 无人机ID  @RequestParam(value = "uavId") String uavId,
     * @param map 标识 tag
     *            timeout 超时时间，秒
     *            uavId 无人机编号
     *            hiveId 停机坪编号，如果停机坪编号不为空，同时给推送消息给停机坪
     *            command 命令字  1006
     *            parm1 参数1
     *            parm2 参数2
     *            parm3 参数3
     *            parm4 参数4
     * @return 成功，失败
     */
    @ResponseBody
    @PostMapping(value = "/pauseMission")
    public Result pauseMission(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        try {

            int timeout = Integer.parseInt(map.getOrDefault("timeout", "5").toString()) * 1000;
            String uavId = map.getOrDefault("uavId", "").toString();
            String tag = map.getOrDefault("tag", "0").toString();
            String hiveId = map.getOrDefault("hiveId", "").toString();
            String command = map.getOrDefault("command", "0").toString();
            String parm1 = map.getOrDefault("parm1", "0").toString();
            String parm2 = map.getOrDefault("parm2", "0").toString();
            String parm3 = map.getOrDefault("parm3", "0").toString();
            String parm4 = map.getOrDefault("parm4", "0").toString();
            if ("".equals(uavId)) {
                return ResultUtil.error("请选择无人机！");
            }
            Object obj = redisUtils.hmGet("rel_uav_id_sn", uavId); //根据无人机SN获取无人机ID  2,1,
            if (obj != null) {
                uavId = obj.toString();
            }

            //1.打包3050上传等待
            EFLINK_MSG_3050 eflink_msg_3050 = new EFLINK_MSG_3050();
            eflink_msg_3050.setTag(Integer.parseInt(tag));
            eflink_msg_3050.setCommand(Integer.parseInt(command));
            eflink_msg_3050.setParm1(Integer.parseInt(parm1));
            eflink_msg_3050.setParm2(Integer.parseInt(parm2));
            eflink_msg_3050.setParm3(Integer.parseInt(parm3));
            eflink_msg_3050.setParm4(Integer.parseInt(parm4));
            byte[] packet = EfLinkUtil.Packet(eflink_msg_3050.EFLINK_MSG_ID, eflink_msg_3050.packet());

            boolean onlyPushToHive = false;
            //2.推送到mqtt,返回3052判断
            long startTime = System.currentTimeMillis();
            String keyHive = null;
            boolean goon = false;
            String error = "未知错误！";
            if (null != hiveId && !"".equals(hiveId)) {
                keyHive = hiveId + "_" + 3051 + "_" + tag;
                redisUtils.remove(keyHive);
                MqttUtil.publish(MqttUtil.Tag_Hive, packet, hiveId);
                if ("2003".equals(hiveId)) {
                    onlyPushToHive = true;
                }
            }
            String key = uavId + "_" + 3051 + "_" + tag;
            if (!onlyPushToHive) {
                redisUtils.remove(key);
                MqttUtil.publish(MqttUtil.Tag_Djiapp, packet, uavId);
            }

            while (true) {
                Object ack = redisUtils.get(key);
                if (!onlyPushToHive && ack != null) {
                    error = EF_PARKING_APRON_ACK.msg((Integer) ack);
                    goon = ((Integer) ack == 1);
                    redisUtils.remove(key);
                    break;
                }
                if (keyHive != null) {
                    ack = redisUtils.get(keyHive);
                    if (ack != null) {
                        error = EF_PARKING_APRON_ACK.msg((Integer) ack);
                        goon = ((Integer) ack == 1);
                        redisUtils.remove(keyHive);
                        break;
                    }
                }
                if (timeout + startTime < System.currentTimeMillis()) {
                    error = "无人机未响应！";
                    break;
                }
                Thread.sleep(50);
            }
            if (!goon) {
                return ResultUtil.error(error);
            }
            return ResultUtil.success(error);
        } catch (Exception e) {
            LogUtil.logError("暂停任务异常：" + e.toString());
            return ResultUtil.error("暂停任务异常,请联系管理员!");
        }
    }


    /**
     * 控制命令   TODO 继续执行任务
     *  uavId 无人机ID  @RequestParam(value = "uavId") String uavId,
     * @param map 标识 tag
     *            timeout 超时时间，秒
     *            uavId 无人机编号
     *            hiveId 停机坪编号，如果停机坪编号不为空，同时给推送消息给停机坪
     *            command 命令字  1006
     *            parm1 参数1
     *            parm2 参数2
     *            parm3 参数3
     *            parm4 参数4
     * @return 成功，失败
     */
    @ResponseBody
    @PostMapping(value = "/resumeMission")
    public Result resumeMission(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        try {
            int timeout = Integer.parseInt(map.getOrDefault("timeout", "5").toString()) * 1000;
            String uavId = map.getOrDefault("uavId", "").toString();
            String tag = map.getOrDefault("tag", "0").toString();
            String hiveId = map.getOrDefault("hiveId", "").toString();
            String command = map.getOrDefault("command", "0").toString();
            String parm1 = map.getOrDefault("parm1", "0").toString();
            String parm2 = map.getOrDefault("parm2", "0").toString();
            String parm3 = map.getOrDefault("parm3", "0").toString();
            String parm4 = map.getOrDefault("parm4", "0").toString();
            if ("".equals(uavId)) {
                return ResultUtil.error("请选择无人机！");
            }
            Object obj = redisUtils.hmGet("rel_uav_id_sn", uavId); //根据无人机SN获取无人机ID  2,1,
            if (obj != null) {
                uavId = obj.toString();
            }


            //1.打包3050上传等待
            EFLINK_MSG_3050 eflink_msg_3050 = new EFLINK_MSG_3050();
            eflink_msg_3050.setTag(Integer.parseInt(tag));
            eflink_msg_3050.setCommand(Integer.parseInt(command));
            eflink_msg_3050.setParm1(Integer.parseInt(parm1));
            eflink_msg_3050.setParm2(Integer.parseInt(parm2));
            eflink_msg_3050.setParm3(Integer.parseInt(parm3));
            eflink_msg_3050.setParm4(Integer.parseInt(parm4));
            byte[] packet = EfLinkUtil.Packet(eflink_msg_3050.EFLINK_MSG_ID, eflink_msg_3050.packet());

            boolean onlyPushToHive = false;
            //2.推送到mqtt,返回3052判断
            long startTime = System.currentTimeMillis();
            String keyHive = null;
            boolean goon = false;
            String error = "未知错误！";
            if (null != hiveId && !"".equals(hiveId)) {
                keyHive = hiveId + "_" + 3051 + "_" + tag;
                redisUtils.remove(keyHive);
                MqttUtil.publish(MqttUtil.Tag_Hive, packet, hiveId);
                if ("2003".equals(hiveId)) {
                    onlyPushToHive = true;
                }
            }
            String key = uavId + "_" + 3051 + "_" + tag;
            if (!onlyPushToHive) {
                redisUtils.remove(key);
                MqttUtil.publish(MqttUtil.Tag_Djiapp, packet, uavId);
            }

            while (true) {
                Object ack = redisUtils.get(key);
                if (!onlyPushToHive && ack != null) {
                    error = EF_PARKING_APRON_ACK.msg((Integer) ack);
                    goon = ((Integer) ack == 1);
                    redisUtils.remove(key);
                    break;
                }
                if (keyHive != null) {
                    ack = redisUtils.get(keyHive);
                    if (ack != null) {
                        error = EF_PARKING_APRON_ACK.msg((Integer) ack);
                        goon = ((Integer) ack == 1);
                        redisUtils.remove(keyHive);
                        break;
                    }
                }
                if (timeout + startTime < System.currentTimeMillis()) {
                    error = "无人机未响应！";
                    break;
                }
                Thread.sleep(50);
            }
            if (!goon) {
                return ResultUtil.error(error);
            }
            return ResultUtil.success(error);

        } catch (Exception e) {
            LogUtil.logError("继续任务异常：" + e.toString());
            return ResultUtil.error("继续任务异常,请联系管理员!");
        }
    }


    /**
     * 控制命令   TODO 停止执行任务
     *  uavId 无人机ID  @RequestParam(value = "uavId") String uavId,
     * @param map 标识 tag
     *            timeout 超时时间，秒
     *            uavId 无人机编号
     *            hiveId 停机坪编号，如果停机坪编号不为空，同时给推送消息给停机坪
     *            command 命令字  1006
     *            parm1 参数1
     *            parm2 参数2
     *            parm3 参数3
     *            parm4 参数4
     * @return 成功，失败
     */
    @ResponseBody
    @PostMapping(value = "/stopMission")
    public Result stopMission(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        try {
            int timeout = Integer.parseInt(map.getOrDefault("timeout", "5").toString()) * 1000;
            String uavId = map.getOrDefault("uavId", "").toString();
            String tag = map.getOrDefault("tag", "0").toString();
            String hiveId = map.getOrDefault("hiveId", "").toString();
            String command = map.getOrDefault("command", "0").toString();
            String parm1 = map.getOrDefault("parm1", "0").toString();
            String parm2 = map.getOrDefault("parm2", "0").toString();
            String parm3 = map.getOrDefault("parm3", "0").toString();
            String parm4 = map.getOrDefault("parm4", "0").toString();
            if ("".equals(uavId)) {
                return ResultUtil.error("请选择无人机！");
            }
            Object obj = redisUtils.hmGet("rel_uav_id_sn", uavId); //根据无人机SN获取无人机ID  2,1,
            if (obj != null) {
                uavId = obj.toString();
            }


            //1.打包3050上传等待
            EFLINK_MSG_3050 eflink_msg_3050 = new EFLINK_MSG_3050();
            eflink_msg_3050.setTag(Integer.parseInt(tag));
            eflink_msg_3050.setCommand(Integer.parseInt(command));
            eflink_msg_3050.setParm1(Integer.parseInt(parm1));
            eflink_msg_3050.setParm2(Integer.parseInt(parm2));
            eflink_msg_3050.setParm3(Integer.parseInt(parm3));
            eflink_msg_3050.setParm4(Integer.parseInt(parm4));
            byte[] packet = EfLinkUtil.Packet(eflink_msg_3050.EFLINK_MSG_ID, eflink_msg_3050.packet());

            boolean onlyPushToHive = false;
            //2.推送到mqtt,返回3052判断
            long startTime = System.currentTimeMillis();
            String keyHive = null;
            boolean goon = false;
            String error = "未知错误！";
            if (null != hiveId && !"".equals(hiveId)) {
                keyHive = hiveId + "_" + 3051 + "_" + tag;
                redisUtils.remove(keyHive);
                MqttUtil.publish(MqttUtil.Tag_Hive, packet, hiveId);
                if ("2003".equals(hiveId)) {
                    onlyPushToHive = true;
                }
            }
            String key = uavId + "_" + 3051 + "_" + tag;
            if (!onlyPushToHive) {
                redisUtils.remove(key);
                MqttUtil.publish(MqttUtil.Tag_Djiapp, packet, uavId);
            }

            while (true) {
                Object ack = redisUtils.get(key);
                if (!onlyPushToHive && ack != null) {
                    error = EF_PARKING_APRON_ACK.msg((Integer) ack);
                    goon = ((Integer) ack == 1);
                    redisUtils.remove(key);
                    break;
                }
                if (keyHive != null) {
                    ack = redisUtils.get(keyHive);
                    if (ack != null) {
                        error = EF_PARKING_APRON_ACK.msg((Integer) ack);
                        goon = ((Integer) ack == 1);
                        redisUtils.remove(keyHive);
                        break;
                    }
                }
                if (timeout + startTime < System.currentTimeMillis()) {
                    error = "无人机未响应！";
                    break;
                }
                Thread.sleep(50);
            }
            if (!goon) {
                return ResultUtil.error(error);
            }
            return ResultUtil.success(error);
        } catch (Exception e) {
            LogUtil.logError("停止任务异常：" + e.toString());
            return ResultUtil.error("停止任务异常,请联系管理员!");
        }
    }

    //endregion 通用无人机控制

    //region 测绘无人机控制

    /**
     * 上传航点任务给无人机
     *
     * @param uavId      无人机ID
     *                   param mission 数组信息
     * @param altType    高度类型：0 使用相对高度，1使用海拔高度
     * @param takeoffAlt 安全起飞高度，相对于无人机当前位置的高度，单位米  相对高度
     * @param homeAlt    起飞点海拔（如果传1，并且使用海拔高度飞行，则自动获取无人机起飞点海拔高度 多边形绘制无法使用）
     * @return 成功/失败
     */
    @ResponseBody
    @PostMapping(value = "/uploadMission")
    public Result uploadMission(@RequestParam(value = "uavId") String uavId, @RequestBody List<double[]> mission, @RequestParam("altType") int altType,
                                @RequestParam("takeoffAlt") double takeoffAlt, @RequestParam(value = "homeAlt", required = false) double homeAlt, HttpServletRequest request) {
        try {
            if (mission == null || mission.size() <= 0) {
                return ResultUtil.error("航线为空!");
            }
            if ("".equals(uavId)) {
                return ResultUtil.error("请选择无人机!");
            }

            Object obj = redisUtils.hmGet("rel_uav_id_sn", uavId); //根据无人机id获取无人机sn
            if (obj != null) {
                uavId = obj.toString();
            }
            String fileName = "temp_" + uavId + System.currentTimeMillis();
            if (altType == 0) {
                // 使用相对高度，获取飞机当前海拔
                if (homeAlt == -1) {
                    Object objalt = redisUtils.get(uavId + "_heart");
                    if (objalt != null) {
                        EfUavRealtimedata realtimedata = (EfUavRealtimedata) objalt;
                        if (realtimedata.getAremd() == 1) {
                            return ResultUtil.error("无人机已经起飞，请先降落无人机！");
                        }
                        if (realtimedata.getGpsStatus() == 10 || realtimedata.getGpsStatus() == 5) {
                            homeAlt = realtimedata.getAltabs();
                        } else {
                            return ResultUtil.error("无人机未差分定位！");
                        }
                    } else {
                        return ResultUtil.error("无人机已离线！");
                    }
                }
            }
            int uavType = 0;
            //获取飞机类型
            Object objtype = redisUtils.get(uavId + "_heart");
            if (objtype != null) {
                EfUavRealtimedata realtimedata = (EfUavRealtimedata) objtype;
                uavType = realtimedata.getUavType();
            }
            // 生成kmz
            File kmzFile = KmzUtil.beforeDataProcessing(mission, fileName, takeoffAlt, homeAlt, altType, uavType, basePath);
            if (kmzFile == null) {
                return ResultUtil.error("保存巡检航线失败(/生成kmz有误)！"); //生成kmz有误
            }
            // 上传minion
            String url = applicationName + "/" + kmzFile.getName();
            if (!minioService.uploadfile("kmz", url, "kmz", new FileInputStream(kmzFile))) {
                if (kmzFile.exists()) {
                    FileUtil.deleteDir(kmzFile.getParent());
                }
                return ResultUtil.error("保存巡检航线失败(/生成kmzminio有误)！"); //生成kmzminio有误
            }

            url = minioService.getPresignedObjectUrl(BucketNameKmz, url);
            if ("".equals(url)) {
                return ResultUtil.error("保存巡检航线失败(错误码 4)！");
            }
            int size = 0;
            // EFLINK_MSG_3121 上传
            int tag = ((byte) new Random().nextInt() & 0xFF);
            EFLINK_MSG_3121 msg3121 = new EFLINK_MSG_3121();
            msg3121.setTag(tag);
            msg3121.setSize(size);
            msg3121.setUrl(url);

            // region 请求上传任务，等待无人机回复
            EFLINK_MSG_3123 msg3123 = new EFLINK_MSG_3123();
            byte[] packet = EfLinkUtil.Packet(msg3121.EFLINK_MSG_ID, msg3121.packet());
            long timeout = System.currentTimeMillis();
            String key = uavId + "_" + msg3123.EFLINK_MSG_ID + "_" + tag;
            boolean goon = false;
            String error = "未知错误！";
            redisUtils.remove(key);
            MqttUtil.publish(MqttUtil.Tag_Djiapp, packet, uavId);
            while (true) {
                Object ack = redisUtils.get(key);
                if (ack != null) {
                    msg3123 = (EFLINK_MSG_3123) ack;
                    if (msg3123.getResult() == 1) {
                        goon = true;
                    } else if (msg3123.getResult() == 2) {
                        error = "下载航线任务文件失败，请重试！";
                    } else {
                        error = "无人机未准备好，请待会再传！";
                    }
                    redisUtils.remove(key);
                    break;
                }
                if (timeout + 20000 < System.currentTimeMillis()) {
                    error = "无人机未响应！";
                    break;
                }
                Thread.sleep(200);
            }
            if (!goon) {
                return ResultUtil.error(error);
            }

            return ResultUtil.success("上传巡检航线成功。");
        } catch (Exception e) {
            LogUtil.logError("上传航点任务至无人机异常：" + e.toString());
            return ResultUtil.error("上传航点任务至无人机异常,请联系管理员!");
        }
    }


    /**
     * 测绘无人机主动上传 APP照片上传到云端, 使用中
     *
     * @param file image 图片名称
     *             type 类型 无人机/停机坪
     *             uavId 无人机编号
     *             creatTime 照片时间
     *             StreamSource 照片类型：DEFAULT,WIDE,ZOOM,INFRARED_THERMAL,UNKNOWN
     *             SavePath 0 原图 1 缩略图 。 已失效，如果为1，则不处理，为0，则自动生成缩略图。
     * @return
     */
    @PostMapping(value = "/uploadMedia")
    public Result uploadMedia(@CurrentUser EfUser user, @RequestParam(value = "file") MultipartFile file, HttpServletRequest request) {
        try {
            String token = request.getHeader("token");
            String uCid = user.getUCId().toString();

            //region 取出参数
            HashMap<String, Object> map = RequestUtil.getRequestParam(request);
            String MediaName = CommonUtil.getStrValueFromMap(map, "MediaName");
            String UavID = CommonUtil.getStrValueFromMap(map, "UavID");
            String uavIdTemp = UavID;
            UavID = redisUtils.getUavIdByUavSn(UavID);  //根据无人机SN获取无人机ID  2,1,
            if (null == UavID || "".equals(UavID)) {
                LogUtil.logWarn("无人机[" + uavIdTemp + "]未录入!");
                return ResultUtil.error("无人机[" + uavIdTemp + "]未录入!");
            }
            // SavePath 0 原图 , 1 缩略图 。
            String SavePath = CommonUtil.getStrValueFromMap(map, "SavePath", "1");
            if ("1".equals(SavePath)) {
                LogUtil.logInfo("上传缩略图不需要处理。");
                return ResultUtil.success("上传缩略图完完成，不处理!");
            }
            long MediaCreatTime = CommonUtil.getLongValueFromMap(map, "CreatTime", 0);
            //后缀名 .JPEG
            String suffixName = CommonUtil.getStrValueFromMap(map, "SuffixName");
            //后缀名 .JPEG
            String type = CommonUtil.getStrValueFromMap(map, "Type");
            //照片种类，zoom,wide...
            String StreamSource = CommonUtil.getStrValueFromMap(map, "StreamSource");
            String GimbalRollStr = CommonUtil.getStrValueFromMap(map, "GimbalRoll");
            String GimbalPitchStr = CommonUtil.getStrValueFromMap(map, "GimbalPitch");
            String GimbalYawStr = CommonUtil.getStrValueFromMap(map, "GimbalYaw");
            String AltStr = CommonUtil.getStrValueFromMap(map, "Alt");
            String AltabsStr = CommonUtil.getStrValueFromMap(map, "Altabs");
            String latStr = CommonUtil.getStrValueFromMap(map, "Latitude");
            String lngStr = CommonUtil.getStrValueFromMap(map, "Longitude");
            String RollStr = CommonUtil.getStrValueFromMap(map, "Roll");
            String PitchStr = CommonUtil.getStrValueFromMap(map, "Pitch");
            String YawStr = CommonUtil.getStrValueFromMap(map, "Yaw");
            //异常类型
            String exceptionTypeStr = CommonUtil.getStrValueFromMap(map, "ExceptionType");
            //endregion

            //region 判断传入的参数是否有问题,解析出更多需要使用的参数

            //判断文件是否为空
            if (file == null) {
                return ResultUtil.error("上传文件为空!");
            }
            String oldFileName = file.getOriginalFilename();
            // 从图片中获取经纬度信息
            File file1 = FileUtil.multipartFileToFile(file);
            if (file1 != null && file1.exists()) {
                JSONObject object = ReadPictrueUtil.readPicLatLng(file1);
                double temp = object.getDoubleValue("lat");
                if (temp != 0 && !Double.isNaN(temp)) {
                    latStr = String.valueOf(temp);
                }
                temp = object.getDoubleValue("lng");
                if (temp != 0 && !Double.isNaN(temp)) {
                    lngStr = String.valueOf(temp);
                }
                file1.delete();
            }
            // 获取图片类型
            long sizeBig = file.getSize();
            long sizeSmall = sizeBig;
            if (StreamSource == null || "".equals(StreamSource)) {
                StreamSource = "DEFAULT";
                if (sizeBig < 2 * 1024 * 1024) {
                    StreamSource = "INFRARED_THERMAL";
                } else if (sizeBig < 3 * 1024 * 1024) {
                    StreamSource = "WIDE";
                } else {
                    StreamSource = "ZOOM";
                }
            }
            //旧文件名称后缀
            String suffix = null; // Suffix
            if (oldFileName != null) {
                // .jpg
                suffix = oldFileName.substring(oldFileName.lastIndexOf("."));
            }
            //新的文件名称
            String newFileName = MediaName + suffix;
            String FolderName = DateUtil.timeStamp2Date(MediaCreatTime, "yyyyMMdd");
            double lat = ConvertUtil.convertToDouble(latStr, 0);
            double lng = ConvertUtil.convertToDouble(lngStr, 0);
            float alt = ConvertUtil.convertToLong(AltStr, 0);
            float altAbs = ConvertUtil.convertToLong(AltabsStr, 0);
            double roll = ConvertUtil.convertToDouble(RollStr, 0);
            double pitch = ConvertUtil.convertToDouble(PitchStr, 0);
            double yaw = ConvertUtil.convertToDouble(YawStr, 0);
            double gimbalRoll = ConvertUtil.convertToDouble(GimbalRollStr, 0);
            double gimbalPitch = ConvertUtil.convertToDouble(GimbalPitchStr, 0);
            double gimbalYaw = ConvertUtil.convertToDouble(GimbalYawStr, 0);
            int exceptionType = ConvertUtil.convertToInt(exceptionTypeStr, 0);
            //LogUtil.logError("接收无人机[" + UavID + "]拍摄[" + StreamSource + "]照片[" + newFileName + "]");
            //endregion

            // 获取文件流字节数组
            byte[] fileStream = BytesUtil.inputStreamToByteArray(file.getInputStream());
            // 开启线程储存照片
            taskAnsisPhoto.savePhoto(new Date(MediaCreatTime), UavID, newFileName, StreamSource, lat, lng, fileStream,
                    FolderName, type, suffix, sizeBig, alt, altAbs, roll, pitch, yaw, gimbalRoll, gimbalPitch, gimbalYaw);
            return ResultUtil.success("上传图片成功!");
        } catch (Exception e) {
            LogUtil.logError("上传图片出错：" + e.toString());
            return ResultUtil.error("上传图片出错,请联系管理员!");
        }
    }

    /**
     * 上传分析后的照片, 上传后，主动推送给前台界面显示
     *
     * @param file 照片
     * @param map  相关参数
     * @return 成功，失败
     */
    @ResponseBody
    @PostMapping(value = "/uploadMediaResult")
    public Result uploadMediaResult(@RequestParam(value = "file") MultipartFile file, @RequestBody HashMap<String, Object> map, HttpServletRequest request) {
        try {
            if (file.isEmpty()) {
                return ResultUtil.error("上传分析照片失败，空文件！");
            }
            // 开启线程储存照片
            taskAnsisPhoto.saveSeedingPhoto(file, map);
            return ResultUtil.success();
        } catch (Exception e) {
            LogUtil.logError("上传分析照片异常：" + e.toString());
            return ResultUtil.error("上传分析照片异常,请联系管理员!");
        }
    }

    //endregion

    //region 补种无人机控制

    /**
     * TODO 飞往某个航点，未起飞则先起飞到航点高度
     *
     * @param uavId 无人机ID
     * @param lat   纬度
     * @param lng   经度
     * @param alt   相对高度
     * @return 成功，失败
     */
    @ResponseBody
    @PostMapping(value = "/guidToHere")
    public Result guidToHere(@RequestParam(value = "uavId") String uavId, @RequestParam(value = "lat") double lat, @RequestParam(value = "lng") double lng, @RequestParam(value = "alt") double alt) {
        try {

            return ResultUtil.success();
        } catch (Exception e) {
            LogUtil.logError("飞往航点异常：" + e.toString());
            return ResultUtil.error("飞往航点异常,请联系管理员!");
        }
    }

    /**
     * TODO 控制无人机微调移动
     *
     * @param uavId    无人机ID
     * @param type     移动方向 , 1115:前移  , 1116:后移  , 1117:左移  , 1118:右移  , 1119:上  , 1120	下
     * @param distance 移动距离，单位米
     * @return 成功，失败
     */
    @ResponseBody
    @PostMapping(value = "/moveUav")
    public Result moveUav(@RequestParam(value = "uavId") String uavId, @RequestParam(value = "type") int type, @RequestParam(value = "distance") double distance) {
        try {
            return ResultUtil.success();
        } catch (Exception e) {
            LogUtil.logError("控制无人机微移异常：" + e.toString());
            return ResultUtil.error("控制无人机微移异常,请联系管理员!");
        }
    }

    /**
     * TODO 抛投
     *
     * @param uavId    无人机ID
     * @param count    抛投数量/抛投次数
     * @param duration 持续时长，单位秒
     * @return 成功，失败
     */
    @ResponseBody
    @PostMapping(value = "/throwObject")
    public Result throwObject(@RequestParam(value = "uavId") String uavId, @RequestParam(value = "count") int count, @RequestParam(value = "duration") double duration) {
        try {
            return ResultUtil.success();
        } catch (Exception e) {
            LogUtil.logError("抛投异常：" + e.toString());
            return ResultUtil.error("抛投异常,请联系管理员!");
        }
    }
    //endregion

    //region 视频处理

    /**
     * 开始录制
     *
     * @param map streamName  流名称
     *            appName   推流路径
     *            endTime    录制任务结束时间  必须为时间戳  秒
     *            startTime  开始时间 不填为立即启动录制 必须为时间戳 秒
     * @return 开始   taskId 	 	任务ID，全局唯一标识录制任务。返回TaskId字段说明录制任务创建成功。
     * RequestId 	唯一请求 ID，每次请求都会返回。定位问题时需要提供该次请求的 RequestId。
     */
    @ResponseBody
    @PostMapping(value = "/enableRecordStream")
    public Result enableRecordStream(@RequestBody Map<String, Object> map) {
        try {
            boolean videoStreamStorage = false;    //流媒体服务器，true:自定义,false:云服务器
            if (MyApplication.appConfig != null) {
                videoStreamStorage = MyApplication.appConfig.isVideoStreamStorage();
            }
            if (videoStreamStorage) {
                JSONObject object = new JSONObject();
                object.put("taskId", "本地流正在录制...");
                return ResultUtil.success("开始录制成功!");
            } else {
                String TxySecretId = MyApplication.appConfig.getTxySecretId();
                String TxySecretKey = MyApplication.appConfig.getTxySecretKey();
                String TxyDomainName = MyApplication.appConfig.getTxyDomainName();
                String TxyTemplateid = MyApplication.appConfig.getTxyTemplateid();
                String streamName = map.getOrDefault("streamName", "").toString();
                String appName = map.getOrDefault("appName", "").toString();
                Long endTime = Long.parseLong(map.getOrDefault("endTime", "0").toString());
                Long startTime = Long.parseLong(map.getOrDefault("startTime", "0").toString());

                Credential cred = new Credential(TxySecretId, TxySecretKey);
                HttpProfile httpProfile = new HttpProfile();
                httpProfile.setEndpoint("live.tencentcloudapi.com");
                // 实例化一个client选项，可选的，没有特殊需求可以跳过
                ClientProfile clientProfile = new ClientProfile();
                clientProfile.setHttpProfile(httpProfile);
                // 实例化要请求产品的client对象,clientProfile是可选的
                LiveClient client = new LiveClient(cred, "ap-guangzhou", clientProfile);
                // 实例化一个请求对象,每个接口都会对应一个request对象
                CreateRecordTaskRequest req = new CreateRecordTaskRequest();
                //先查询流有没有相同的，有则删除
                JSONArray array = TencentUtil.queryTask(startTime, streamName);
                if (array != null) {
                    for (int i = 0; i < array.size(); i++) {
                        Object object = array.get(i);
                        JSONObject object1 = (JSONObject) object;
                        String taskId = object1.get("taskId").toString();
                        Boolean aBoolean = TencentUtil.deleteTask(taskId);
                        if (!aBoolean) {
                            LogUtil.logError("删除相同流失败！");
                        }
                    }
                }
                Boolean lineStreamOnline = TencentUtil.getLineStreamOnline(appName, streamName);
                if (!lineStreamOnline) {
                    return ResultUtil.error("当前流不活跃，开始录制失败！");
                }
                // 返回的resp是一个CreateRecordTaskResponse的实例，与请求对象对应
                Object OldStartTime = redisUtils.get(streamName + "_stream");
                Boolean record = true;
                if (OldStartTime != null) {
                    //两个视频录制时间间隔小于30秒则不录制
                    if (startTime - Long.parseLong(OldStartTime.toString()) < 1000 * 30) {
                        record = false;
                    }
                }
                CreateRecordTaskResponse resp = null;
                if (record) {
                    req.setStreamName(streamName);
                    req.setDomainName(TxyDomainName);
                    req.setAppName(appName);
                    req.setTemplateId(Long.parseLong(TxyTemplateid));
                    if (endTime == 0) {
                        req.setEndTime(startTime / 1000 + 60 * 60);
                    } else {
                        req.setEndTime(endTime / 1000);
                    }
                    if (startTime != 0) {
                        req.setStartTime(startTime / 1000);
                    }
                    resp = client.CreateRecordTask(req);
                    LogUtil.logMessage("视频流：" + streamName + "正在开始录制...");
                }
                redisUtils.set(streamName + "_stream", startTime, 2L, TimeUnit.MINUTES);
                // 输出json格式的字符串回包
                return ResultUtil.successData(resp);
            }
        } catch (Exception e) {
            LogUtil.logError("开始录制出错：" + e.toString());
            return ResultUtil.error("开始录制失败,请联系管理员!");
        }
    }

    /**
     * 终止录制
     *
     * @param map taskId 录制任务id 全局唯一标识录制任务   （关闭流需要传）
     * @return 终止录制 RequestId 	String 	唯一请求 ID，每次请求都会返回。定位问题时需要提供该次请求的 RequestId
     */
    @ResponseBody
    @PostMapping(value = "/disableRecordStream")
    public Result disableRecordStream(@RequestBody Map<String, Object> map) {
        try {
//            Properties prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("application.properties"));
//            TxySecretId = prop.getProperty("TxySecretId");
            String TxySecretId = MyApplication.appConfig.getTxySecretId();
//            TxySecretKey = prop.getProperty("TxySecretKey");
            String TxySecretKey = MyApplication.appConfig.getTxySecretKey();
//            TxyDomainName = prop.getProperty("TxyDomainName");
            String taskId = map.getOrDefault("taskId", "").toString();

            Credential cred = new Credential(TxySecretId, TxySecretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("live.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            LiveClient client = new LiveClient(cred, "ap-guangzhou", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            StopRecordTaskRequest req = new StopRecordTaskRequest();
            req.setTaskId(taskId);
            // 返回的resp是一个StopRecordTaskResponse的实例，与请求对象对应
            StopRecordTaskResponse resp = client.StopRecordTask(req);
            LogUtil.logMessage("视频流：" + taskId + "终止录制！");
            // 输出json格式的字符串回包
            return ResultUtil.successData(resp);
        } catch (Exception e) {
            LogUtil.logError("终止录制出错：" + e.toString());
            return ResultUtil.error("终止录制失败,请联系管理员!");
        }
    }

    //endregion

    //region 数据增删查改

    /**
     * 查询飞行架次 queryFlightNumber
     *
     * @param uavId     无人机编号
     * @param startTime
     * @param endTime
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/queryFlightNumber")
    public Result queryFlightNumber(@RequestParam(value = "uavId") String uavId, @RequestParam(value = "startTime", required = false) long startTime,
                                    @RequestParam(value = "endTime", required = false) long endTime) {
        try {
            if ("".equals(uavId)) {
                return ResultUtil.error("无人机id为空");
            }

            if (startTime == 0L) { // 检查开始时间是否为空
                // 获取一个月前的时间戳
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, -1);
                startTime = calendar.getTimeInMillis();
            }
            if (endTime == 0L) { // 检查结束时间是否为空
                // 获取当前时间的时间戳
                Calendar calendar = Calendar.getInstance();
                endTime = calendar.getTimeInMillis();
            }
            if (endTime < startTime) { // 检查结束时间是否小于开始时间
                return ResultUtil.error("查询时间段异常!");
            }
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String startTime1 = dateFormat.format(new Date(startTime));
            // 将 startTime 和 endTime 转换成时间字符串并继续执行其他业务逻辑
            String start = DateUtil.timeStamp2Date(startTime, "yyyy-MM-dd HH:mm:ss");
            String end = DateUtil.timeStamp2Date(endTime, "yyyy-MM-dd HH:mm:ss");
            // 查询 uavid start end 查询 飞行架次
            List<EfUavEachsortie> efUavEachsortieList = efUavEachsortieService.queryByIdOrTime(uavId, start, end);

            return ResultUtil.success("查询飞行架次成功", efUavEachsortieList);
        } catch (Exception e) {
            LogUtil.logError("查询获取飞行架次数据异常：" + e.toString());
            return ResultUtil.error("查询获取飞行架次数据异常,请联系管理员！");
        }

    }

    /**
     * 实时拍摄照片表 queryPhotoInfo
     *
     * @param uavId
     * @param eachsortieId
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/queryPhotoInfo")
    public Result queryPhotoInfo(@RequestParam(value = "uavId", required = false) String uavId, @RequestParam(value = "eachsortieId") Integer eachsortieId) {
        try {
            if (eachsortieId == null) {
                return ResultUtil.error("架次id为空");
            }
            // 查询 uavid eachsortieId 查询 实时拍摄照片表
            List<EfMediaPhoto> efMediaPhotoList = efMediaPhotoService.queryByeachsortieIdOruavId(eachsortieId);

            return ResultUtil.success("查询飞行架次图片列表信息成功", efMediaPhotoList);
        } catch (Exception e) {
            LogUtil.logError("查询获取飞行架次图片列表数据异常：" + e.toString());
            return ResultUtil.error("查询获取飞行架次图片列表数据异常,请联系管理员！");
        }

    }

    /**
     * 查询草原空洞表 queryHoleInfo
     *
     * @param uavId        无人机编号
     * @param eachsortieId 飞行架次
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/queryHoleInfo")
    public Result queryHoleInfo(@RequestParam(value = "uavId", required = false) String uavId, @RequestParam(value = "eachsortieId") Integer eachsortieId) {
        try {
            if (eachsortieId == null) {
                return ResultUtil.error("架次id为空");
            }
            // 查询 uavid eachsortieId 查询 实时拍摄空斑信息表
            List<EfCavity> efCavityList = efCavityService.queryByeachsortieIdOruavId(eachsortieId);

            return ResultUtil.success("查询飞行架次空斑列表信息成功", efCavityList);
        } catch (Exception e) {
            LogUtil.logError("查询获取飞行架次空斑列表数据异常：" + e.toString());
            return ResultUtil.error("查询获取飞行空斑列表数据异常,请联系管理员！");
        }

    }

    /**
     * 查询草原空洞播种记录表 queryHoleSeedingInfo
     *
     * @param cavityId 洞斑id
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/queryHoleSeedingInfo")
    public Result queryHoleSeedingInfo(@RequestParam(value = "cavityId") Integer cavityId) {
        try {
            if (cavityId == null) {
                return ResultUtil.error("空斑id为空");
            }
            // 查询 uavid cavityId 查询 查询草原空洞播种记录表
            List<EfCavitySeeding> efCavitySeedingList = efCavitySeedingService.queryBycavityId(cavityId);

            return ResultUtil.success("查询空斑播种信息成功", efCavitySeedingList);
        } catch (Exception e) {
            LogUtil.logError("查询空斑播种数据异常：" + e.toString());
            return ResultUtil.error("查询空斑播种数据异常,请联系管理员！");
        }

    }

    /**
     * 查询草原空洞播种记录表 queryHoleSeedingInfo
     *
     * @param eachsortieId 架次id
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/queryHoleSeedingInfobyeachsortieId")
    public Result queryHoleSeedingInfobyeachsortieId(@RequestParam(value = "eachsortieId") Integer eachsortieId) {
        try {
            if (eachsortieId == null) {
                return ResultUtil.error("架次id为空");
            }
            // 查询 uavid eachsortieId 查询 实时拍摄空斑信息表
            List<EfCavity> efCavityList = efCavityService.queryByeachsortieIdOruavId(eachsortieId);

            List<EfCavitySeeding> efCavitySeedingListTatol= new ArrayList<>();

            // 查询 uavid cavityId 查询 查询草原空洞播种记录表
            if(efCavityList.size()>0){
                for (int i = 0; i < efCavityList.size(); i++) {
                    EfCavity efCavity = efCavityList.get(i);
                    Integer cavityId = efCavity.getId();
                    List<EfCavitySeeding> efCavitySeedingList = efCavitySeedingService.queryBycavityId(cavityId);
                    if (efCavitySeedingList.size()>0){
                        efCavitySeedingListTatol.addAll(efCavitySeedingList);
                    }
                }
            }

            return ResultUtil.success("查询空斑播种信息成功", efCavitySeedingListTatol);
        } catch (Exception e) {
            LogUtil.logError("查询空斑播种数据异常：" + e.toString());
            return ResultUtil.error("查询空斑播种数据异常,请联系管理员！");
        }

    }




    //endregion
}

