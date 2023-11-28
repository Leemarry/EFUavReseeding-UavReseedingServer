package com.bear.reseeding.controller;

import com.bear.reseeding.MyApplication;
import com.bear.reseeding.common.ResultUtil;
import com.bear.reseeding.datalink.EfLinkUtil;
import com.bear.reseeding.datalink.MqttUtil;
import com.bear.reseeding.eflink.EFLINK_MSG_3050;
import com.bear.reseeding.eflink.EFLINK_MSG_3121;
import com.bear.reseeding.eflink.EFLINK_MSG_3123;
import com.bear.reseeding.entity.EfCavity;
import com.bear.reseeding.entity.EfMediaPhoto;
import com.bear.reseeding.entity.EfUavEachsortie;
import com.bear.reseeding.entity.EfUavRealtimedata;
import com.bear.reseeding.model.Result;
import com.bear.reseeding.service.EfCavityService;
import com.bear.reseeding.service.EfMediaPhotoService;
import com.bear.reseeding.service.EfUavEachsortieService;
import com.bear.reseeding.service.EfUavService;
import com.bear.reseeding.task.MinioService;
import com.bear.reseeding.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.synth.Region;
import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private EfUavEachsortieService efUavEachsortieService;

    @Resource
    private EfMediaPhotoService efMediaPhotoService;

    @Resource
    private EfCavityService efCavityService;

    @Value("${BasePath:C://efuav/reseeding/}")
    public String basePath;


    /**
     *minio
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
    @Value("${spring.config.encryptMd5Soft:water}")
    String encryptMd5Soft;

    /**
     * 获取所有权限的无人机
     *
     * @return Result {data:token}
     */
    @ResponseBody
    @PostMapping(value = "/getUavs")
    public Result getUavs(@RequestParam(value = "userId") String userId, @RequestParam(value = "userPwd") String userPwd, HttpServletRequest request) {
        try {
            // String idSession = request.getSession().getId();
            String ipLocal = request.getRemoteAddr();
            String ipWww = NetworkUtil.getIpAddr(request);

            return ResultUtil.success();
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
    public Result takeoff(@RequestParam(value = "uavId") String uavId, @RequestParam(value = "alt") double alt, HttpServletRequest request) {
        try {
            // String idSession = request.getSession().getId();
            String ipLocal = request.getRemoteAddr();
            String ipWww = NetworkUtil.getIpAddr(request);
            Object obj = redisUtils.hmGet("rel_uav_id_sn", uavId); //根据无人机ID获取无人机SN
            if (obj != null) {
                uavId = obj.toString();
            }
            //1.打包3050上传等待
            int tag = new Random().nextInt();
            EFLINK_MSG_3050 eflink_msg_3050 = new EFLINK_MSG_3050();
            eflink_msg_3050.setTag(tag);
            eflink_msg_3050.setCommand(11203);
            eflink_msg_3050.setParm1((int) (alt * 100));
            eflink_msg_3050.setParm2(0);
            eflink_msg_3050.setParm3(0);
            eflink_msg_3050.setParm4(0);
            byte[] packet = EfLinkUtil.Packet(eflink_msg_3050.EFLINK_MSG_ID, eflink_msg_3050.packet());
            //2.推送到mqtt,返回3052判断
            long startTime = System.currentTimeMillis();
            String keyHive = null;
            boolean goon = false;
            String error = "未知错误！";
            String key = uavId + "_" + 3051 + "_" + tag;
            MqttUtil.publish(MqttUtil.Tag_Djiapp, packet, uavId);
            MyApplication.keyObj.put(key, key);
            synchronized (MyApplication.keyObj) {
                try {
                    if (!MyApplication.keyObj.containsKey(key)) {
                        MyApplication.keyObj.get(key).wait(10000); // 等待回复
                    }
                    // 有值之后，处理值

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            MyApplication.keyObj.remove(key);

            return ResultUtil.success();
        } catch (Exception e) {
            LogUtil.logError("起飞无人机异常：" + e.toString());
            return ResultUtil.error("起飞无人机异常,请联系管理员!");
        }
    }


    /**
     * TODO 降落无人机
     *
     * @param uavId 无人机ID
     * @return 成功，失败
     */
    @ResponseBody
    @PostMapping(value = "/land")
    public Result land(@RequestParam(value = "uavId") String uavId, HttpServletRequest request) {
        try {
            // String idSession = request.getSession().getId();
            String ipLocal = request.getRemoteAddr();
            String ipWww = NetworkUtil.getIpAddr(request);

            return ResultUtil.success();
        } catch (Exception e) {
            LogUtil.logError("降落无人机异常：" + e.toString());
            return ResultUtil.error("降落无人机异常,请联系管理员!");
        }
    }


    /**
     * TODO 返航无人机
     *
     * @param uavId 无人机ID
     * @return 成功，失败
     */
    @ResponseBody
    @PostMapping(value = "/rtl")
    public Result rtl(@RequestParam(value = "uavId") String uavId, HttpServletRequest request) {
        try {
            // String idSession = request.getSession().getId();
            String ipLocal = request.getRemoteAddr();
            String ipWww = NetworkUtil.getIpAddr(request);

            return ResultUtil.success();
        } catch (Exception e) {
            LogUtil.logError("返航无人机异常：" + e.toString());
            return ResultUtil.error("返航无人机异常,请联系管理员!");
        }
    }

    /**
     * TODO 开始执行任务
     *
     * @param uavId 无人机ID
     * @return 成功，失败
     */
    @ResponseBody
    @PostMapping(value = "/startMission")
    public Result startMission(@RequestParam(value = "uavId") String uavId, HttpServletRequest request) {
        try {
            // String idSession = request.getSession().getId();
            String ipLocal = request.getRemoteAddr();
            String ipWww = NetworkUtil.getIpAddr(request);

            return ResultUtil.success();
        } catch (Exception e) {
            LogUtil.logError("开始执行任务异常：" + e.toString());
            return ResultUtil.error("开始执行任务异常,请联系管理员!");
        }
    }

    /**
     * TODO 暂停执行任务
     *
     * @param uavId 无人机ID
     * @return 成功，失败
     */
    @ResponseBody
    @PostMapping(value = "/pauseMission")
    public Result pauseMission(@RequestParam(value = "uavId") String uavId, HttpServletRequest request) {
        try {
            // String idSession = request.getSession().getId();
            String ipLocal = request.getRemoteAddr();
            String ipWww = NetworkUtil.getIpAddr(request);

            return ResultUtil.success();
        } catch (Exception e) {
            LogUtil.logError("暂停任务异常：" + e.toString());
            return ResultUtil.error("暂停任务异常,请联系管理员!");
        }
    }

    /**
     * TODO 继续执行任务
     *
     * @param uavId 无人机ID
     * @return 成功，失败
     */
    @ResponseBody
    @PostMapping(value = "/resumeMission")
    public Result resumeMission(@RequestParam(value = "uavId") String uavId, HttpServletRequest request) {
        try {
            // String idSession = request.getSession().getId();
            String ipLocal = request.getRemoteAddr();
            String ipWww = NetworkUtil.getIpAddr(request);

            return ResultUtil.success();
        } catch (Exception e) {
            LogUtil.logError("继续任务异常：" + e.toString());
            return ResultUtil.error("继续任务异常,请联系管理员!");
        }
    }

    /**
     * TODO 停止执行任务
     *
     * @param uavId 无人机ID
     * @return 成功，失败
     */
    @ResponseBody
    @PostMapping(value = "/stopMission")
    public Result stopMission(@RequestParam(value = "uavId") String uavId, HttpServletRequest request) {
        try {
            // String idSession = request.getSession().getId();
            String ipLocal = request.getRemoteAddr();
            String ipWww = NetworkUtil.getIpAddr(request);

            return ResultUtil.success();
        } catch (Exception e) {
            LogUtil.logError("停止任务异常：" + e.toString());
            return ResultUtil.error("停止任务异常,请联系管理员!");
        }
    }

    //endregion 通用无人机控制

    //region 测绘无人机控制
//    数组信息
    /**
     *
     *  TODO 上传航点任务给无人机
     *
     * @param uavId 无人机编号
     * @param mission 数组信息
     * @param altType
     * @param takeoffAlt
     * @param homeAlt
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/uploadMission")
    public Result uploadMission(@RequestParam(value = "uavId") String uavId, @RequestBody List<Map> mission,@RequestParam("altType") int altType,
                                @RequestParam("takeoffAlt") double takeoffAlt, @RequestParam(value = "homeAlt", required = false) double homeAlt, HttpServletRequest request) {
        try {
            for (Map<String, Double> entry : mission) {
                double x = entry.get("x");
                double y = entry.get("y");
                double z = entry.get("z");
                // 进行相应的操作...
                System.out.println(x+","+y+""+z);
            }
            // String idSession = request.getSession().getId();
            String ipLocal = request.getRemoteAddr();
            String ipWww = NetworkUtil.getIpAddr(request);
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
            boolean iscreate= true;
            File kmzFile = KmzUtil.beforeDataProcessing(mission, fileName, takeoffAlt, homeAlt, altType, uavType,basePath);
            if (kmzFile==null) {
                return ResultUtil.error("保存光伏巡检航线失败(/生成kmz有误)！"); //生成kmz有误
            }
            // 上传minion
            String url = applicationName  + "/" + kmzFile.getName();
            if (!minioService.uploadImage("kmz", url, "kmz", new FileInputStream(kmzFile))) {
                if (kmzFile.exists()) {
                    FileUtil.deleteDir(kmzFile.getParent());
                }
                return ResultUtil.error("保存光伏巡检航线失败(/生成kmzminio有误)！"); //生成kmzminio有误
            }

            url = minioService.getPresignedObjectUrl(BucketNameKmz, url);
            if ("".equals(url)) {
                return ResultUtil.error("保存光伏巡检航线失败(错误码 4)！");
            }
            int size =0;
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


            if(iscreate){
                return ResultUtil.error("生成kmz文件失败！");
            }



            return ResultUtil.success();
        } catch (Exception e) {
            LogUtil.logError("上传航点任务至无人机异常：" + e.toString());
            return ResultUtil.error("上传航点任务至无人机异常,请联系管理员!");
        }
    }


    /**
     * TODO 测绘无人机主动上传分析后的照片
     * 上传后，主动推送给前台界面显示
     *
     * @param file 照片
     * @param map  相关参数
     * @return 成功，失败
     */
    @ResponseBody
    @PostMapping(value = "/uploadMedia")
    public Result uploadMedia(@RequestParam(value = "file") MultipartFile file, @RequestBody Map<String, Object> map, HttpServletRequest request) {
        try {
            if (file.isEmpty()) {
                return ResultUtil.error("上传分析照片失败，空文件！");
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
            if("".equals(uavId)){return  ResultUtil.error("无人机id为空");}

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
            List<EfUavEachsortie> efUavEachsortieList = efUavEachsortieService.queryByIdOrTime(uavId,start,end);

            return  ResultUtil.success("查询飞行架次成功",efUavEachsortieList);
        } catch (Exception e) {
            LogUtil.logError("查询获取飞行架次数据异常：" + e.toString());
            return ResultUtil.error("查询获取飞行架次数据异常,请联系管理员！");
        }

    }

    /**
     * 实时拍摄照片表 queryPhotoInfo
     * @param uavId
     * @param eachsortieId
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/queryPhotoInfo")
    public Result queryPhotoInfo(@RequestParam(value = "uavId",required = false) String uavId,@RequestParam(value = "eachsortieId") Integer eachsortieId) {
        try {
            if(eachsortieId == null){
                return  ResultUtil.error("架次id为空");
            }
            // 查询 uavid eachsortieId 查询 实时拍摄照片表
           List<EfMediaPhoto> efMediaPhotoList=  efMediaPhotoService.queryByeachsortieIdOruavId(eachsortieId);

            return  ResultUtil.success("查询飞行架次图片列表信息成功",efMediaPhotoList);
        } catch (Exception e) {
            LogUtil.logError("查询获取飞行架次图片列表数据异常：" + e.toString());
            return ResultUtil.error("查询获取飞行架次图片列表数据异常,请联系管理员！");
        }

    }

    /**
     * 查询草原空洞表 queryHoleInfo
     * @param uavId 无人机编号
     * @param eachsortieId 飞行架次
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/queryHoleInfo")
    public Result queryHoleInfo(@RequestParam(value = "uavId",required = false) String uavId,@RequestParam(value = "eachsortieId") Integer eachsortieId) {
        try {
            if(eachsortieId == null){
                return  ResultUtil.error("架次id为空");
            }
            // 查询 uavid eachsortieId 查询 实时拍摄照片表
            List<EfCavity> efCavityList =efCavityService.queryByeachsortieIdOruavId(eachsortieId);

            return  ResultUtil.success("查询飞行架次洞斑列表信息成功",efCavityList);
        } catch (Exception e) {
            LogUtil.logError("查询获取飞行架次洞斑列表数据异常：" + e.toString());
            return ResultUtil.error("查询获取飞行洞斑列表数据异常,请联系管理员！");
        }

    }


    //endregion
}

