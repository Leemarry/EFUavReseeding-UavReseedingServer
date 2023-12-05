package com.bear.reseeding.task;

import com.alibaba.fastjson.JSONArray;
import com.bear.reseeding.MyApplication;
import com.bear.reseeding.common.ResultUtil;
import com.bear.reseeding.datalink.WebSocketLink;
import com.bear.reseeding.eflink.EFLINK_MSG_10010;
import com.bear.reseeding.eflink.EFLINK_MSG_10021;
import com.bear.reseeding.entity.EfMediaPhoto;
import com.bear.reseeding.entity.EfUavEachsortie;
import com.bear.reseeding.service.EfMediaPhotoService;
import com.bear.reseeding.service.EfUavEachsortieService;
import com.bear.reseeding.utils.*;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * 光伏照片处理线程池
 *
 * @Auther: bear
 * @Date: 2023/6/2 09:53
 * @Description: null
 */
@Component
public class TaskAnsisPhoto {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private MinioService minioService;
    @Autowired
    private EfUavEachsortieService efUavEachsortieService;
    @Autowired
    private EfMediaPhotoService efMediaPhotoService;
    @Value("${BasePath:C://efuav/UavSystem/}")
    public String basePath;
    @Value("${minio.EndpointExt}")
    private String EndpointExt;
    @Value("${BasePath:C://efuav/UavSystem/}")
    public String BasePath;
    /**
     * 光伏分析照片类型： DEFAULT、INFRARED_THERMAL、WIDE、ZOOM
     */
    @Value("${spring.config.streamSourcePvAnal:DEFAULT}")
    String streamSourcePvAnal;

    //实时照片表
    ThreadPoolExecutor threadPoolExecutorPhoto;

    private final Object lock = new Object();

    /**
     * 保存实时航拍照片
     *
     * @param date         拍摄照片的日期
     * @param uavId        无人机ID
     * @param fileName     文件名称，xxx.jpg
     * @param streamSource 图片类型: INFRARED_THERMAL /  WIDE / ZOOM
     * @param lat          纬度
     * @param lng          经度
     * @param fileStream   文件流
     */
    public void savePhoto(Date date, String uavId, String fileName, String streamSource,
                          double lat, double lng, byte[] fileStream, String FolderName, String type, String suffix,
                          long sizeBig, float alt, float altAbs,
                          double roll, double pitch, double yaw, double gimbalRoll, double gimbalPitch, double gimbalYaw) {
        if (threadPoolExecutorPhoto == null) {
            // 构造一个线程池
            threadPoolExecutorPhoto = new ThreadPoolExecutor(10, 200, 30,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(30),
                    new ThreadPoolExecutor.CallerRunsPolicy());
        }
        threadPoolExecutorPhoto.execute(() -> {
            try {
                long timeStart = System.currentTimeMillis();
                long sizeSmall = sizeBig;
                if (fileStream == null || fileStream.length < 3) {
                    LogUtil.logWarn("当前照片数据流为null，无法储存！！！");
                }
                int sortieId = -1;
                //根据拍摄时间，查询关联架次
                EfUavEachsortie eachsortie = efUavEachsortieService.queryByPhotoTime(date, uavId);
                if (eachsortie != null && eachsortie.getId() != null) {
                    sortieId = ConvertUtil.convertToInt(eachsortie.getId(), -1);
                }
                LogUtil.logInfo("照片 " + streamSource + "(" + date + ") 关联的架次编号为：" + sortieId);

                // 1.上传minio原图和缩略图
                //region 保存照片到minio或者本地
                String pathBigImage = ("0".equals(type) ? "uav" : "hive") + "/" + uavId + "/image/" + FolderName + "/" + fileName;
                String pathMiniImage = ("0".equals(type) ? "uav" : "hive") + "/" + uavId + "/thumbnail/" + FolderName + "/" + fileName;
                String urlBig = "";  // 原图路径
                String urlSmall = "";  // 缩略图路径
                String place = ""; //拍摄地址
                //boolean photoStorageLocal = MyApplication.appConfig.isPhotoStorage(); // 存云端，还是存本地
                urlBig = "resource/photo/" + pathBigImage;
                urlSmall = "resource/photo/" + pathMiniImage;
                String pathParentBig = BasePath + "photo/" + ("0".equals(type) ? "uav" : "hive") + "/" + uavId + "/image/" + FolderName + "/";
                String pathParentSmall = BasePath + "photo/" + ("0".equals(type) ? "uav" : "hive") + "/" + uavId + "/thumbnail/" + FolderName + "/";
                if (!FileUtil.saveFileAndThumbnail(fileStream, pathParentBig, pathParentSmall, fileName)) {
                    LogUtil.logError("上传图片失败！");
                }
                //endregion
                // 2、只要原图上传成功，就可以保存待数据库中
                //region 保存数据到数据库
//                EfPhoto efPhoto = efPhotoService.queryByCreatTime(uavId, fileName);
                EfMediaPhoto efMediaPhoto = efMediaPhotoService.queryByCreatTime(uavId, fileName);
                if (efMediaPhoto != null) {
                    efMediaPhoto.setCameraVideoStreamSource(streamSource);
                    efMediaPhoto.setPathImage(urlBig);
                    efMediaPhoto.setSizeImage(sizeBig);
                    efMediaPhoto.setPathThumbnail(urlSmall);
                    efMediaPhoto.setSizeThumbnail(sizeSmall);
                    efMediaPhoto.setCameraVideoStreamSource(streamSource);
                    efMediaPhoto.setSourceId(0);
                    efMediaPhoto.setLat(lat);
                    efMediaPhoto.setLng(lng);
                    efMediaPhoto.setAlt(alt);
                    efMediaPhoto.setAltabs(altAbs);
                    efMediaPhoto.setRoll(roll);
                    efMediaPhoto.setGimbalRoll(gimbalRoll);
                    efMediaPhoto.setYaw(yaw);
                    efMediaPhoto.setGimbalYaw(gimbalYaw);
                    efMediaPhoto.setPitch(pitch);
                    efMediaPhoto.setGimbalPitch(gimbalPitch);
                    efMediaPhoto.setPlace(place);
                    efMediaPhoto.setEachsortieId(sortieId);
//                    efMediaPhoto.setCavityCount(0);
                    EfMediaPhoto update = efMediaPhotoService.update(efMediaPhoto);
                    if (update == null) {
                        LogUtil.logError("更新媒体文件失败！");
                    }
                } else {
                    efMediaPhoto = new EfMediaPhoto();
                    //efMediaPhoto.setImageGroup();
                    efMediaPhoto.setImageTag(fileName);
                    efMediaPhoto.setCameraVideoStreamSource(streamSource);
                    efMediaPhoto.setPathImage(urlBig);
                    efMediaPhoto.setSizeImage(sizeBig);
                    efMediaPhoto.setPathThumbnail(urlSmall);
                    efMediaPhoto.setSizeThumbnail(sizeSmall);
                    efMediaPhoto.setCreateDate(date);
                    efMediaPhoto.setDeviceid(uavId);
                    efMediaPhoto.setType(Integer.valueOf(type));
                    efMediaPhoto.setSourceId(0);
                    efMediaPhoto.setLat(lat);
                    efMediaPhoto.setLng(lng);
                    efMediaPhoto.setAlt(alt);
                    efMediaPhoto.setAltabs(altAbs);
                    efMediaPhoto.setRoll(roll);
                    efMediaPhoto.setGimbalRoll(gimbalRoll);
                    efMediaPhoto.setYaw(yaw);
                    efMediaPhoto.setGimbalYaw(gimbalYaw);
                    efMediaPhoto.setPitch(pitch);
                    efMediaPhoto.setGimbalPitch(gimbalPitch);
                    efMediaPhoto.setPlace(place);
                    efMediaPhoto.setEachsortieId(sortieId);
                    efMediaPhoto.setCavityCount(0);
                    EfMediaPhoto insert = efMediaPhotoService.insert(efMediaPhoto);
                    if (insert == null) {
                        LogUtil.logError("添加媒体文件到数据库失败！");
                    }
                }
                // 推送前台
                String[] owerUsers = new String[0];
                Object obj = null;
                obj = redisUtils.hmGet("rel_uavid_userid", uavId); //无人机ID获取用户ID  2,1,
                if (obj != null) {
                    String delims = "[,]+";
                    owerUsers = obj.toString().split(delims);
                }
                if (owerUsers.length <= 0) {
                    LogUtil.logWarn("MQTT：无人机[" + uavId + "]未绑定用户！");
                }
                obj = redisUtils.hmGet("rel_uavid_companyid", uavId);
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
                EFLINK_MSG_10010 msg10010 = new EFLINK_MSG_10010();
                msg10010.setUavId(uavId);
                msg10010.setTime(date.getTime());
                msg10010.setAlt(alt);
                msg10010.setAltAbs(altAbs);
                msg10010.setLat(lat);
                msg10010.setLng(lng);
                msg10010.setUrl(urlBig);
                WebSocketLink.push(ResultUtil.success(msg10010.EFLINK_MSG_ID, uavId, null, msg10010), owerUsers);
                //endregion
                LogUtil.logDebug("实时航拍图片储存耗时：" + (System.currentTimeMillis() - timeStart) / 1000d + "秒");
            } catch (Exception e) {
                LogUtil.logError("保存实时航拍图片异常：" + e.toString());
            }
        });
    }

    /**
     * 保存实时分析的航拍照片
     *
     * @param file 图片
     * @param map  参数集合，包含经纬度等信息
     */
    public void saveSeedingPhoto(MultipartFile file, HashMap<String, Object> map) {
        if (threadPoolExecutorPhoto == null) {
            // 构造一个线程池
            threadPoolExecutorPhoto = new ThreadPoolExecutor(10, 200, 30,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(30),
                    new ThreadPoolExecutor.CallerRunsPolicy());
        }
        threadPoolExecutorPhoto.execute(() -> {
//            // 解析数据
//            String uavSn = map.getOrDefault("uavSn", "").toString();
//            String uavId = uavSn;
//            double lat = CommonUtil.getDoubleValueFromMap(map, "lat", 0);
//            double lng = CommonUtil.getDoubleValueFromMap(map, "lng", 0);
//            double alt = CommonUtil.getDoubleValueFromMap(map, "alt", 0);
//            double yaw = CommonUtil.getDoubleValueFromMap(map, "yaw", 0);
//            double altAbs = CommonUtil.getDoubleValueFromMap(map, "altAbs", 0);
//            long time = CommonUtil.getLongValueFromMap(map, "time", 0);
//            String dataStr = map.getOrDefault("data", "").toString();
//            JSONArray jsonArray = JSONArray.parseArray(dataStr); //空斑集合
//            // 储存照片
//            String urlBig = "";  // 原图路径
//            urlBig = "resource/photo/" + "uav" + uavId + "/image/" + FolderName + "/" + fileName;
//            String pathParentBig = BasePath + "photo/" + ("0".equals(type) ? "uav" : "hive") + "/" + uavId + "/image/" + FolderName + "/";
//
//            // 保存数据到数据库
//
//            // 推送到前台
//            String[] owerUsers = new String[0];
//            Object obj = null;
//            obj = redisUtils.hmGet("rel_uavid_userid", uavId); //无人机ID获取用户ID  2,1,
//            if (obj != null) {
//                String delims = "[,]+";
//                owerUsers = obj.toString().split(delims);
//            }
//            if (owerUsers.length <= 0) {
//                LogUtil.logWarn("MQTT：无人机[" + uavId + "]未绑定用户！");
//            }
//            obj = redisUtils.hmGet("rel_uavid_companyid", uavId);
//            if (obj != null) {
//                // 找到公司下的所有管理员
//                obj = redisUtils.hmGet("rel_companyid_usersid", obj);
//                if (obj != null) {
//                    // 有管理员
//                    String delims = "[,]+";
//                    String[] users = obj.toString().split(delims);
//                    if (owerUsers.length <= 0) {
//                        owerUsers = users;
//                    } else {
//                        owerUsers = (String[]) ArrayUtils.addAll(owerUsers, users);
//                    }
//                    if (owerUsers != null && owerUsers.length > 0) {
//                        List<String> list = new ArrayList<>();
//                        for (String id : owerUsers) {
//                            if (!list.contains(id)) {
//                                list.add(id);
//                            }
//                        }
//                        owerUsers = new String[list.size()];
//                        list.toArray(owerUsers);
//                    }
//                }
//            }
//            EFLINK_MSG_10021 msg10021 = new EFLINK_MSG_10021();
//            msg10021.setUavId(uavId);
//            msg10021.setTime(time);
//            msg10021.setAlt(alt);
//            msg10021.setAltAbs(altAbs);
//            msg10021.setLat(lat);
//            msg10021.setLng(lng);
//            msg10021.setUrl(urlBig);
//            WebSocketLink.push(ResultUtil.success(msg10021.EFLINK_MSG_ID, uavId, null, msg10021), owerUsers);
        });
    }
}
