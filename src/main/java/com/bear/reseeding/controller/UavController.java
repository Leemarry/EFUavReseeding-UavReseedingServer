package com.bear.reseeding.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import static org.json.JSONObject.*;

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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.live.v20180801.LiveClient;
import com.tencentcloudapi.live.v20180801.models.CreateRecordTaskRequest;
import com.tencentcloudapi.live.v20180801.models.CreateRecordTaskResponse;
import com.tencentcloudapi.live.v20180801.models.StopRecordTaskRequest;
import com.tencentcloudapi.live.v20180801.models.StopRecordTaskResponse;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.synth.Region;
import java.io.*;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

    @Resource
    private EfTaskKmzService efTaskKmzService;

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

    @Value("${BasePath:C://efuav/UavSystem/}")
    public String BasePath;


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
//            Thread.sleep(5000);
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
     * @param map 无人机map
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
            String parm1 = map.getOrDefault("parm1", "0").toString();  // lat
            String parm2 = map.getOrDefault("parm2", "0").toString();  // lng
            String parm3 = map.getOrDefault("parm3", "0").toString(); // alt
            String parm4 = map.getOrDefault("parm4", "0").toString();
            if ("".equals(uavId)) {
                return ResultUtil.error("请选择无人机！");
            }
            /**
             * 查询无人机信息 ---》无人机类型
             */
            EfUav efUav = efUavService.queryByIdAndType(uavId); // 含无人机类型信息
            Integer typeProtocol = -1;
            if (efUav != null) {
                EfUavType efUavType = efUav.getEfUavType();
                typeProtocol = efUavType.getTypeProtocol();
//                Integer uavTypeId= efUav.getUavTypeId(); // 1: 开源 ： 0:大疆
            } else {
                return ResultUtil.error("该无人机信息异常");
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
//                typeProtocol = 1;
                if (typeProtocol == 1) {
                    /**开源*/
                    MqttUtil.publish(MqttUtil.Tag_efuavapp, packet, uavId);
                } else if (typeProtocol == -1) {
                    return ResultUtil.error("未获取到无人机类型");
                } else {
                    /**大疆*/
                    MqttUtil.publish(MqttUtil.Tag_Djiapp, packet, uavId);
                }
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
             * 查询无人机信息 ---》无人机类型
             */
            EfUav efUav = efUavService.queryByIdAndType(uavId); // 含无人机类型信息
            Integer typeProtocol = -1;
            if (efUav != null) {
                EfUavType efUavType = efUav.getEfUavType();
                typeProtocol = efUavType.getTypeProtocol();
//                Integer uavTypeId= efUav.getUavTypeId(); // 1: 开源 ： 0:大疆
            } else {
                return ResultUtil.error("该无人机信息异常");
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
                if (typeProtocol == 1) {
                    /**开源*/
                    MqttUtil.publish(MqttUtil.Tag_efuavapp, packet, uavId);
                } else if (typeProtocol == -1) {
                    return ResultUtil.error("未获取到无人机类型");
                } else {
                    /**大疆*/
                    MqttUtil.publish(MqttUtil.Tag_Djiapp, packet, uavId);
                }
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

            /**
             * 查询无人机信息 ---》无人机类型
             */
            EfUav efUav = efUavService.queryByIdAndType(uavId); // 含无人机类型信息
            Integer typeProtocol = -1;
            if (efUav != null) {
                EfUavType efUavType = efUav.getEfUavType();
                typeProtocol = efUavType.getTypeProtocol();
//                Integer uavTypeId= efUav.getUavTypeId(); // 1: 开源 ： 0:大疆
            } else {
                return ResultUtil.error("该无人机信息异常");
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
//                MqttUtil.publish(MqttUtil.Tag_Djiapp, packet, uavId);
                if (typeProtocol == 1) {
                    /**开源*/
                    MqttUtil.publish(MqttUtil.Tag_efuavapp, packet, uavId);
                } else if (typeProtocol == -1) {
                    return ResultUtil.error("未获取到无人机类型");
                } else {
                    /**大疆*/
                    MqttUtil.publish(MqttUtil.Tag_Djiapp, packet, uavId);
                }
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
     * uavId 无人机ID  @RequestParam(value = "uavId") String uavId,
     *
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
            /**
             * 查询无人机信息 ---》无人机类型
             */
            EfUav efUav = efUavService.queryByIdAndType(uavId); // 含无人机类型信息
            Integer typeProtocol = -1;
            if (efUav != null) {
                EfUavType efUavType = efUav.getEfUavType();
                typeProtocol = efUavType.getTypeProtocol();
//                Integer uavTypeId= efUav.getUavTypeId(); // 1: 开源 ： 0:大疆
            } else {
                return ResultUtil.error("该无人机信息异常");
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
                if (typeProtocol == 1) {
                    /**开源*/
                    MqttUtil.publish(MqttUtil.Tag_efuavapp, packet, uavId);
                } else if (typeProtocol == -1) {
                    return ResultUtil.error("未获取到无人机类型");
                } else {
                    /**大疆*/
                    MqttUtil.publish(MqttUtil.Tag_Djiapp, packet, uavId);
                }
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
     * uavId 无人机ID  @RequestParam(value = "uavId") String uavId,
     *
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

            /**
             * 查询无人机信息 ---》无人机类型
             */
            EfUav efUav = efUavService.queryByIdAndType(uavId); // 含无人机类型信息
            Integer typeProtocol = -1;
            if (efUav != null) {
                EfUavType efUavType = efUav.getEfUavType();
                typeProtocol = efUavType.getTypeProtocol();
//                Integer uavTypeId= efUav.getUavTypeId(); // 1: 开源 ： 0:大疆
            } else {
                return ResultUtil.error("该无人机信息异常");
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
                if (typeProtocol == 1) {
                    /**开源*/
                    MqttUtil.publish(MqttUtil.Tag_efuavapp, packet, uavId);
                } else if (typeProtocol == -1) {
                    return ResultUtil.error("未获取到无人机类型");
                } else {
                    /**大疆*/
                    MqttUtil.publish(MqttUtil.Tag_Djiapp, packet, uavId);
                }
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
     * uavId 无人机ID  @RequestParam(value = "uavId") String uavId,
     *
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

            /**
             * 查询无人机信息 ---》无人机类型
             */
            EfUav efUav = efUavService.queryByIdAndType(uavId); // 含无人机类型信息
            Integer typeProtocol = -1;
            if (efUav != null) {
                EfUavType efUavType = efUav.getEfUavType();
                typeProtocol = efUavType.getTypeProtocol();
//                Integer uavTypeId= efUav.getUavTypeId(); // 1: 开源 ： 0:大疆
            } else {
                return ResultUtil.error("该无人机信息异常");
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
//                MqttUtil.publish(MqttUtil.Tag_Djiapp, packet, uavId);
                if (typeProtocol == 1) {
                    /**开源*/
                    MqttUtil.publish(MqttUtil.Tag_efuavapp, packet, uavId);
                } else if (typeProtocol == -1) {
                    return ResultUtil.error("未获取到无人机类型");
                } else {
                    /**大疆*/
                    MqttUtil.publish(MqttUtil.Tag_Djiapp, packet, uavId);
                }
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
     * uavId 无人机ID  @RequestParam(value = "uavId") String uavId,
     *
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
            /**
             * 查询无人机信息 ---》无人机类型
             */
            EfUav efUav = efUavService.queryByIdAndType(uavId); // 含无人机类型信息
            Integer typeProtocol = -1;
            if (efUav != null) {
                EfUavType efUavType = efUav.getEfUavType();
                typeProtocol = efUavType.getTypeProtocol();
//                Integer uavTypeId= efUav.getUavTypeId(); // 1: 开源 ： 0:大疆
            } else {
                return ResultUtil.error("该无人机信息异常");
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
//                MqttUtil.publish(MqttUtil.Tag_Djiapp, packet, uavId);
                if (typeProtocol == 1) {
                    /**开源*/
                    MqttUtil.publish(MqttUtil.Tag_efuavapp, packet, uavId);
                } else if (typeProtocol == -1) {
                    return ResultUtil.error("未获取到无人机类型");
                } else {
                    /**大疆*/
                    MqttUtil.publish(MqttUtil.Tag_Djiapp, packet, uavId);
                }
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
     * 保存巡检航线至minio
     *
     * @param uavId
     * @param mission
     * @param altType
     * @param takeoffAlt
     * @param homeAlt
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/saveRouteToMinio")
    public Result saveRouteToMinio(@CurrentUser EfUser efUser, @RequestParam(value = "uavId", required = false) String uavId, @RequestBody List<double[]> mission, @RequestParam("altType") int altType,
                                   @RequestParam("takeoffAlt") double takeoffAlt, @RequestParam(value = "homeAlt", required = false) double homeAlt, @RequestParam(value = "name") String name,
                                   HttpServletRequest request) {
        try {
            if (mission == null || mission.size() <= 0) {
                return ResultUtil.error("航线为空!");
            }
            Integer ucId = efUser.getUCId();
            Integer userId = efUser.getId(); //
            String userName = efUser.getUName();
            Date nowdate = new Date();

            Object obj = redisUtils.hmGet("rel_uav_id_sn", uavId); //根据无人机id获取无人机sn
            if (obj != null) {
                uavId = obj.toString();
            }
            /**航线名称*/
//            String fileName ="temp_" + uavId + System.currentTimeMillis();
            String fileName = name;

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
            String url = applicationName + File.separator + File.separator + ucId + File.separator + kmzFile.getName();
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
            long fileSize = kmzFile.length();
//            double fileSizeKB = (double) kmzFile.length() / 1024; // 将字节数转换为 KB
//            String formattedSize = String.format("%.2f KB", fileSizeKB); // 格式化结果（保留两位小数）
//            System.out.println("文件大小：" + fileSize + "字节");
            // 距离 点集合
            int numPoints = mission.size();
            double distaceCount = 0.0;
            double distance;
            for (int i = 0; i < numPoints; i++) {
                for (int j = i + 1; j < numPoints; j++) {
                    double[] firstPoint = mission.get(i);  // 获取第一组坐标点
                    double lng1 = firstPoint[0];  // 获取经度
                    double lat1 = firstPoint[1];  // 获取纬度
                    double[] nextPoint = mission.get(j);  // 获取第一组坐标点
                    double lng2 = nextPoint[0];  // 获取经度
                    double lat2 = nextPoint[1];  // 获取纬度

                    distance = GisUtil.getDistance(lng1, lat1, lng2, lat2);
                    distaceCount += distance;
//                    System.out.printf("Distance between P%d and P%d: %.2f m\n", i + 1, j + 1, distance);
                    break;
                }
            }
            // minio上传成功  保存 到数据库
            EfTaskKmz efTaskKmz = new EfTaskKmz();
            efTaskKmz.setKmzUpdateTime(nowdate);
            efTaskKmz.setKmzCreateTime(nowdate);
            efTaskKmz.setKmzName(name);
            efTaskKmz.setKmzPath(url);
            efTaskKmz.setKmzType("kmz");
            efTaskKmz.setKmzSize(fileSize);
            efTaskKmz.setKmzDes("");
            efTaskKmz.setKmzVersion("1.0.0");
            efTaskKmz.setKmzDistance(distaceCount);
            efTaskKmz.setKmzDuration((Double) (distaceCount / 5f));
            efTaskKmz.setKmzCreateUser(userName);
            efTaskKmz.setKmzUpdateUser(userName);
            efTaskKmz.setKmzUpdateByUserId(userId);
            efTaskKmz.setKmzCreateByUserId(userId);
            efTaskKmz.setKmzCompanyId(ucId);

            efTaskKmz = efTaskKmzService.insert(efTaskKmz);
            if (efTaskKmz != null) {
                return ResultUtil.success("保存航线成功！");
            }
            /**移除minio 错误航线*/
            Boolean gold = minioService.removeObject(BucketNameKmz, url);
            if (gold) {
                return ResultUtil.error("保存航线失败！！！请重新上传");
            } else {
                return ResultUtil.error("保存航线失败！请联系管理员清理minio文件");
            }


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
//            String uCid = user.getUCId().toString();

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
            CompletableFuture<String> urlBigFuture = new CompletableFuture<>();
            taskAnsisPhoto.savePhoto(new Date(MediaCreatTime), UavID, newFileName, StreamSource, lat, lng, fileStream,
                    FolderName, type, suffix, sizeBig, alt, altAbs, urlBigFuture, file, roll, pitch, yaw, gimbalRoll, gimbalPitch, gimbalYaw);
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
    public Result uploadMediaResult(@RequestParam("photo") MultipartFile photo, @RequestParam(value = "jsonFile") MultipartFile jsonFile, HttpServletRequest request) {
        try {
            if (photo == null || photo.isEmpty() || jsonFile == null || jsonFile.isEmpty()) {
                return ResultUtil.error("上传分析照片失败，文件为空！");
            }
            // 读取 JSON 文件内容为字符串
            String jsonContent = new String(jsonFile.getBytes());

            Gson gson = new Gson();
            Map<String, Object> paramMap = null;
            try {
                paramMap = gson.fromJson(jsonContent, new TypeToken<Map<String, Object>>() {
                }.getType());
            } catch (JsonSyntaxException e) {
                LogUtil.logError("上传分析照片异常：JSON 参数格式不正确！" + e.toString());
                return ResultUtil.error("上传分析照片异常：JSON 参数格式不正确！");
            }
            // 开启线程存储照片
            // 获取文件流字节数组
            byte[] fileStream = BytesUtil.inputStreamToByteArray(photo.getInputStream());
            paramMap.put("fileStream", fileStream);
            taskAnsisPhoto.saveSeedingPhoto(photo, paramMap);
            return ResultUtil.success();
        } catch (Exception e) {
            LogUtil.logError("上传分析照片异常：" + e.toString());
            return ResultUtil.error("上传分析照片异常，请联系管理员!");
        }
    }

    @ResponseBody
    @PostMapping(value = "/uploadMediaResults")
    public Result uploadMediaResults(@RequestParam(value = "file") MultipartFile file, HttpServletRequest request) {
        try {
            if (file.isEmpty()) {
                return ResultUtil.error("上传分析照片失败，空文件！");
            }

            // 获取文件名-大小
            String fileName = file.getOriginalFilename();
            long fileSize = file.getSize();
            // 创建一时间为文件夹
            Date time = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String FolderName = dateFormat.format(time) + "-result";
            // 指定文件存储路径
            String filePath = "photo/uav/" + "/imgage/" + FolderName + "/";

            try {
                File dest = new File(BasePath + filePath, fileName);
                if (!dest.exists()) {
                    boolean res = dest.mkdirs();
                    if (!res) {
                        LogUtil.logWarn("创建目录失败！");
                    }
                }
                // 存储文件
                file.transferTo(dest);
                LogUtil.logWarn("储存到本地 BasePath ！！！");
            } catch (IOException e) {
                e.printStackTrace();
                LogUtil.logWarn("储存到本地 BasePath ！！！");
            }
            return ResultUtil.success();
        } catch (Exception e) {
            LogUtil.logError("上传分析照片异常：" + e.toString());
            return ResultUtil.error("上传分析照片异常,请联系管理员!");
        }
    }

    //endregion

    //region 补种无人机控制

    /**
     * 飞往某个航点，未起飞则先起飞到航点高度
     * <p>
     * 1121左自旋 1122  右   参数角度
     *
     * @param uavId 无人机ID
     * @param lat   纬度
     * @param lng   经度
     * @param alt   相对高度,单位米
     * @return 成功，失败
     */
    @ResponseBody
    @PostMapping(value = "/guidToHere")
    public Result guidToHere(@RequestParam(value = "uavId") String uavId, @RequestParam(value = "lat") double lat, @RequestParam(value = "lng") double lng, @RequestParam(value = "alt") double alt) {
        try {
            //1.打包3050上传等待
            int tag = ((byte) new Random().nextInt() & 0xFF);
            EFLINK_MSG_3050 eflink_msg_3050 = new EFLINK_MSG_3050();
            eflink_msg_3050.setTag(tag);
            eflink_msg_3050.setCommand(1113);
            eflink_msg_3050.setParm1((int) (lat * 1e7));
            eflink_msg_3050.setParm2((int) (lng * 1e7));
            eflink_msg_3050.setParm3((int) (alt * 100));
            eflink_msg_3050.setParm4(0);
            byte[] packet = EfLinkUtil.Packet(eflink_msg_3050.EFLINK_MSG_ID, eflink_msg_3050.packet());
            //2.推送到mqtt,返回3051判断
            Object obj = redisUtils.hmGet("rel_uav_sn_id", uavId);
            if (obj == null) {
                return ResultUtil.error("无人机不在线！");
            }
            String uavSn = obj.toString();
            String key = uavSn + "_" + 3051 + "_" + tag;
            redisUtils.remove(key);
            MqttUtil.publish(MqttUtil.Tag_efuavapp, packet, uavSn);
            //3.判断是否收到响应
            int timeout = 5000;
            long startTime = System.currentTimeMillis();
            while (true) {
                Object ack = redisUtils.get(key);
                if (ack != null) {
                    String error = EF_PARKING_APRON_ACK.msg((Integer) ack);
                    boolean success = ((Integer) ack == 1);
                    redisUtils.remove(key);
                    if (success) {
                        return ResultUtil.success();
                    } else {
                        return ResultUtil.error(error);
                    }
                }
                if (timeout + startTime < System.currentTimeMillis()) {
                    break;
                }
                Thread.sleep(50);
            }
            return ResultUtil.error("无人机未响应！");
        } catch (Exception e) {
            LogUtil.logError("飞往航点异常：" + e.toString());
            return ResultUtil.error("飞往航点异常,请联系管理员!");
        }
    }

    /**
     * 控制无人机微调移动
     *
     * @param uavId 无人机ID
     * @param type  移动方向 , 1115:前移  , 1116:后移  , 1117:左移  , 1118:右移  , 1119:上  , 1120	下  1121
     * @param Parm1 移动距离，单位厘米 @RequestParam(value = "distance") double distance
     * @return 成功，失败
     */
    @ResponseBody
    @PostMapping(value = "/moveUav")
    public Result moveUav(@RequestParam(value = "uavId") String uavId, @RequestParam(value = "type") int type, @RequestParam(value = "Parm1") double Parm1,
                          @RequestParam(value = "Parm2", required = false) double Parm2, @RequestParam(value = "Parm3", required = false) double Parm3, HttpServletRequest request) {
        try {

            //后缀名 .JPEG
            //1.打包3050上传等待
            int tag = ((byte) new Random().nextInt() & 0xFF);
            EFLINK_MSG_3050 eflink_msg_3050 = new EFLINK_MSG_3050();
            eflink_msg_3050.setTag(tag);
            eflink_msg_3050.setCommand(type);
            if (type == 1121) {
                eflink_msg_3050.setParm1((int) Parm1); // 角度 45
                eflink_msg_3050.setParm2((int) Parm2); // 顺时针0--逆时针1
                eflink_msg_3050.setParm3((int) Parm3); // 相对角度0-以正北角度为
            } else {
                eflink_msg_3050.setParm1((int) Parm1); // old 距离
            }

            byte[] packet = EfLinkUtil.Packet(eflink_msg_3050.EFLINK_MSG_ID, eflink_msg_3050.packet());

            //2.推送到mqtt,返回3051判断
            Object obj = redisUtils.hmGet("rel_uav_sn_id", uavId);
            if (obj == null) {
                return ResultUtil.error("无人机不存在！");
            }
            String uavSn = obj.toString();
            String key = uavSn + "_" + 3051 + "_" + tag;
            redisUtils.remove(key);
            MqttUtil.publish(MqttUtil.Tag_efuavapp, packet, uavSn);
            //3.判断是否收到响应
            int timeout = 5000;
            long startTime = System.currentTimeMillis();
            while (true) {
                Object ack = redisUtils.get(key);
                if (ack != null) {
                    String error = EF_PARKING_APRON_ACK.msg((Integer) ack);
                    boolean success = ((Integer) ack == 1);
                    redisUtils.remove(key);
                    if (success) {
                        return ResultUtil.success();
                    } else {
                        return ResultUtil.error(error);
                    }
                }
                if (timeout + startTime < System.currentTimeMillis()) {
                    break;
                }
                Thread.sleep(50);
            }
            return ResultUtil.error("无人机未响应！");
        } catch (Exception e) {
            LogUtil.logError("控制无人机微移异常：" + e.toString());
            return ResultUtil.error("控制无人机微移异常,请联系管理员!");
        }
    }

    /**
     * 抛投
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
            //1.打包3050上传等待
            int tag = ((byte) new Random().nextInt() & 0xFF);
            EFLINK_MSG_3050 eflink_msg_3050 = new EFLINK_MSG_3050();
            eflink_msg_3050.setTag(tag);
            eflink_msg_3050.setCommand(1130);
            eflink_msg_3050.setParm1(count); //包头id
            eflink_msg_3050.setParm2((int) (duration * 100));
            byte[] packet = EfLinkUtil.Packet(eflink_msg_3050.EFLINK_MSG_ID, eflink_msg_3050.packet());

            //2.推送到mqtt,返回3051判断
            Object obj = redisUtils.hmGet("rel_uav_sn_id", uavId);
            if (obj == null) {
                return ResultUtil.error("无人机不存在！");
            }
            String uavSn = obj.toString();
            String key = uavSn + "_" + 3051 + "_" + tag;
            redisUtils.remove(key);
            MqttUtil.publish(MqttUtil.Tag_efuavapp, packet, uavSn);

            //3.判断是否收到响应
            int timeout = 5000;
            long startTime = System.currentTimeMillis();
            while (true) {
                Object ack = redisUtils.get(key);
                if (ack != null) {
                    String error = EF_PARKING_APRON_ACK.msg((Integer) ack);
                    boolean success = ((Integer) ack == 1);
                    redisUtils.remove(key);
                    if (success) {
                        return ResultUtil.success();
                    } else {
                        return ResultUtil.error(error);
                    }
                }
                if (timeout + startTime < System.currentTimeMillis()) {
                    break;
                }
                Thread.sleep(50);
            }
            return ResultUtil.error("无人机未响应！");
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
     * 查询飞行架次 (前台查询一个月的飞行架次信息）
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
     * 架次查询关联的实时拍摄照片表
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
     * 查询草原空洞表
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

            List<EfCavitySeeding> efCavitySeedingListTatol = new ArrayList<>();

            // 查询 uavid cavityId 查询 查询草原空洞播种记录表
            if (efCavityList.size() > 0) {
                for (int i = 0; i < efCavityList.size(); i++) {
                    EfCavity efCavity = efCavityList.get(i);
                    Integer cavityId = efCavity.getId();
                    List<EfCavitySeeding> efCavitySeedingList = efCavitySeedingService.queryBycavityId(cavityId);
                    if (efCavitySeedingList.size() > 0) {
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

    /**
     * 查询无人机信息-类型
     *
     * @param efUser
     * @param uavId
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/queryUavType")
    public Result queryUavType(@CurrentUser EfUser efUser, @RequestParam(value = "uavId") String uavId) {
        try {
            EfUav efUav = efUavService.queryById(uavId);
            if (efUav != null) {
                return ResultUtil.success("查询无人机类型信息成功", efUav);
            }
            return ResultUtil.error("查询无人机类型信息失败,请联系管理员！");
        } catch (Exception e) {
            LogUtil.logError("查询无人机类型失败");
            return ResultUtil.error("查询无人机类型异常,请联系管理员！");
        }
    }

    /**
     * 查询 航点任务表 信息 时间
     */
    @ResponseBody
    @PostMapping(value = "/queryKmzInfo")
    public Result queryKmzInfo(@CurrentUser EfUser efUser,
                               @RequestParam(value = "startTime", required = false) long startTime, @RequestParam(value = "endTime", required = false) long endTime) {
        try {
            if (endTime <= startTime) {
                return ResultUtil.error("查询时间段异常");
            }
            Integer Ucid = efUser.getUCId(); // 航线任务所属公司
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startTimeStr = dateFormat.format(new Date(startTime));
            String endTimeStr = dateFormat.format(new Date(endTime));
            List<EfTaskKmz> efTaskKmzList = efTaskKmzService.queryByUcidAndTime(Ucid, startTimeStr, endTimeStr);
            if (efTaskKmzList.size() >= 0) {
                return ResultUtil.success("查询航线任务列表信息成功！", efTaskKmzList);
            }
            return ResultUtil.error("查询航线任务列表信息失败！请联系管理员！");
        } catch (Exception e) {
            LogUtil.logError("查询无人机类型失败");
            return ResultUtil.error("查询无人机类型异常,请联系管理员！");
        }
    }


    //endregion

    // region 二次处理
    @ResponseBody
    @PostMapping(value = "/sendHandle")
    public Result sendHandle(@CurrentUser EfUser efUser) {
        try {


            return ResultUtil.success("发送处理信息成功");
        } catch (Exception e) {
            return ResultUtil.error("发送处理信息失败");
        }

    }


    /**
     * 处理确认后接收请求
     *
     * @param efUser
     * @param latitude
     * @param longitude
     * @param height
     * @param uavheight
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/confirmHandle")
    public Result confirmHandle(@CurrentUser EfUser efUser, @RequestParam("latitude") Integer latitude, @RequestParam("longitude") Integer longitude,
                                @RequestParam("height") Integer height, @RequestParam("uavheight") Integer uavheight, @RequestBody(required = false) Map<String, Object> map) {
        try {
//            //线程 开启 发送UDP线程与接收UDP线程；
//            int threads= 5;
//            ExecutorService executorService = Executors.newFixedThreadPool(threads);
//            executorService.submit(new UdpSendReceiver.TalkSender(5555, 9997, "localhost"));
//            Future<byte []> future =   executorService.submit(new UdpSendReceiver.TalkReceiver(9998,5555));
//            byte[] data=  future.get();  // 接收到数据
//            // 如果是 一个空的 byte
//            if(data.length<=0){
//                return ResultUtil.error("未接收到数据！");
//            }
//            //
//            executorService.shutdown();


            return ResultUtil.error("发送处理信息失败");
        } catch (Exception e) {
            return ResultUtil.error("发送处理信息失败");
        }

    }


    @ResponseBody
    @PostMapping(value = "/secondaryAnalysis")
    public Result secondaryAnalysis(@CurrentUser EfUser efUser, @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject();
            int numThread = 3;
            ExecutorService executorService = Executors.newFixedThreadPool(numThread);
            boolean isZIP = isCompressedFile(file.getOriginalFilename());
            if (!isZIP) {
                return ResultUtil.error("请发送数据压缩包");
            }
            // 将 MultipartFile 转换为字节数组输入流
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file.getBytes());
                 ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream)) {
                ZipEntry entry;
                while ((entry = zipInputStream.getNextEntry()) != null) {
                    String key = entry.getName();
                    // 创建字节流
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".json")) {
                        // 读取
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = zipInputStream.read(buffer)) > 0) {
                            byteArrayOutputStream.write(buffer, 0, bytesRead);
                        }
                        // 获取json 数据


                    } else if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".jpg")) {
                        // 读取
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = zipInputStream.read(buffer)) > 0) {
                            byteArrayOutputStream.write(buffer, 0, bytesRead);
                        }
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        // 创建 MinioUploader 对象并连接到 Minio 对象存储
                        MinioClient minioClient = MinioClient.builder()
                                .endpoint("http://127.0.0.1:9090")
                                .credentials("gNkgwJSo4EyFyxHuG5mz", "IieYrz9poS8JsEFXzoo7PG7yhmHK9dqZbaVG1khn")
                                .build();
                        //  写入文件
                        minioClient.putObject(
                                PutObjectArgs.builder()
                                        .bucket("ceshi")
                                        .object(key)
                                        .stream(new ByteArrayInputStream(bytes), bytes.length, -1)
                                        .build());
                        // 返回存入路径
                        String fileUrl = minioClient.getPresignedObjectUrl(
                                GetPresignedObjectUrlArgs.builder()
                                        .method(Method.PUT)
                                        .bucket("ceshi")
                                        .object(key)
                                        .build()
                        );
                        URL url = new URL(fileUrl);


                        jsonObject.put(key, fileUrl);

                    }

                }

            }

            return ResultUtil.success("处理数据", jsonObject);
        } catch (Exception e) {
            return ResultUtil.error("发送处理信息失败");
        }

    }


    @PostMapping(value = "/secondaryAnalysiss")
    public Result secondaryAnalysiss(@CurrentUser EfUser efUser, @RequestParam(value = "file", required = false) MultipartFile file) {
        int numThread = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(numThread);
        try {
            JSONObject resultObject = new JSONObject();
            boolean isZIP = isCompressedFile(file.getOriginalFilename());
            if (!isZIP) {
                return ResultUtil.error("请发送数据压缩包");
            }

            // 将 MultipartFile 转换为字节数组输入流
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file.getBytes());
                 ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream)) {
                ZipEntry entry;
                AtomicInteger taskCount = new AtomicInteger(0);
                while ((entry = zipInputStream.getNextEntry()) != null) {
                    if (!entry.isDirectory()) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = zipInputStream.read(buffer)) > 0) {
                            byteArrayOutputStream.write(buffer, 0, bytesRead);
                        }
                        String key = entry.getName().toLowerCase();
                        if (key.endsWith(".json")) {
                            executorService.submit(() -> {
                                try {
                                    // 获取json 数据
                                    JSONObject jsonObject = JSONObject.parseObject(byteArrayOutputStream.toString("UTF-8"));
                                    JSONArray blockAllArray = jsonObject.getJSONArray("block_all"); // 所有地块 统计
                                    JSONArray blockListArray = jsonObject.getJSONArray("block_list"); // 作业地块list
                                    JSONArray reseedPointList = jsonObject.getJSONArray("reseed_point_list"); // 补播路径点列表JSON文件
                                    if (blockAllArray != null) {
                                        BlockAll blockAll = JSONObject.parseObject(blockAllArray.getJSONObject(0).toJSONString(), BlockAll.class);
                                        System.out.println(blockAll);
                                    }
                                    if (blockListArray != null) {
                                        List<Block> blockList = new ArrayList<>();
                                        for (int i = 0; i < blockListArray.size(); i++) {
                                            Block block = JSONObject.parseObject(blockListArray.getJSONObject(i).toJSONString(), Block.class);
                                            blockList.add(block);
                                        }
                                    }
                                    if (reseedPointList != null) {
                                        reseedPoint[] entityArray = new reseedPoint[reseedPointList.size()];
                                        for (int i = 0; i < reseedPointList.size(); i++) {
                                            JSONArray entityValues = reseedPointList.getJSONArray(i);
                                            double prop1 = entityValues.getDouble(0);
                                            double prop2 = entityValues.getDouble(1);
                                            double prop3 = entityValues.getDouble(2);
                                            Integer prop4 = entityValues.getInteger(3);
                                            entityArray[i] = new reseedPoint(prop1, prop2, prop3, prop4);
                                        }
                                    }
                                    synchronized (resultObject) {
                                        resultObject.put(key, jsonObject);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    taskCount.decrementAndGet();
                                }
                            });
                            taskCount.incrementAndGet();
                        } else if (key.endsWith(".jpg")) {
                            byte[] bytes = byteArrayOutputStream.toByteArray();
                            // 使用CompletableFuture在新线程中执行异步任务
                            executorService.submit(() -> {
                                // 将字节数组转换为Base64字符串 data:image/png;base64,
                                String base64Image = Base64.getEncoder().encodeToString(bytes);
                                // 执行您的操作，例如将Base64字符串存入resultObject
                                synchronized (resultObject) {
                                    resultObject.put(key, base64Image);
                                }
                                // 任务完成后，减少任务计数
                                taskCount.decrementAndGet();
                            });
                        }
                    }
                }

                // 等待所有任务完成
                while (taskCount.get() > 0) {
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                return ResultUtil.error("处理文件时出错");
            }

            return ResultUtil.success("处理数据", resultObject);
        } catch (Exception e) {
            return ResultUtil.error("发送处理信息失败");
        } finally {
            // 关闭线程池和 MinioClient
            executorService.shutdown();
        }
    }


    public static boolean isCompressedFile(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return extension.equalsIgnoreCase("zip") ||
                extension.equalsIgnoreCase("rar") ||
                extension.equalsIgnoreCase("7z");
    }


    // #endregion
}

