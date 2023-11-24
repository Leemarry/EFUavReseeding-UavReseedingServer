package com.bear.reseeding.controller;

import com.bear.reseeding.common.ResultUtil;
import com.bear.reseeding.model.Result;
import com.bear.reseeding.service.EfUavService;
import com.bear.reseeding.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.synth.Region;
import java.util.Map;

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

    //endregion 通用无人机控制

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

    //region 测绘无人机控制

    /**
     * TODO 上传航点任务给无人机
     *
     * @param uavId   无人机ID
     * @param mission 任务航点集合
     * @return 成功，失败
     */
    @ResponseBody
    @PostMapping(value = "/uploadMission")
    public Result uploadMission(@RequestParam(value = "uavId") String uavId, @RequestBody Object mission, HttpServletRequest request) {
        try {
            // String idSession = request.getSession().getId();
            String ipLocal = request.getRemoteAddr();
            String ipWww = NetworkUtil.getIpAddr(request);

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
}

