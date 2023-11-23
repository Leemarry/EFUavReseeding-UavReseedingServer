package com.bear.reseeding.task;

import com.bear.reseeding.MyApplication;
import com.bear.reseeding.service.EfMediaPhotoService;
import com.bear.reseeding.service.EfUavEachsortieService;
import com.bear.reseeding.utils.FileUtil;
import com.bear.reseeding.utils.LogUtil;
import com.bear.reseeding.utils.RedisUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    @Value("${spring.config.pvUrl}")
    private String pvUrl;
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

    //环保秸秆表， 创建线程池，同时指定核心线程数和最大线程数
    ThreadPoolExecutor threadPoolExecutorEp;

    // 创建线程池，同时指定核心线程数和最大线程数
    ThreadPoolExecutor threadPoolExecutor;

    //分析照片线程
    ThreadPoolExecutor threadPoolAnalyzePhoto;

    private final Object lock = new Object();

    private CompletableFuture<String> future;

    public void Task(CompletableFuture<String> future) {
        this.future = future;
    }

    /**
     * 保存实时航拍照片
     *
     * @param companyId    公司ID
     * @param date         拍摄照片的日期
     * @param uavId        无人机ID
     * @param fileName     文件名称，xxx.jpg
     * @param streamSource 图片类型: INFRARED_THERMAL /  WIDE / ZOOM
     * @param lat          纬度
     * @param lng          经度
     * @param fileStream   文件流
     */
    public void savePhoto(String companyId, Date date, String uavId, String fileName, String streamSource,
                          double lat, double lng, byte[] fileStream, String FolderName, String type, String suffix,
                          MultipartFile file, long sizeBig, CompletableFuture<String> urlBigFuture, float alt, float altAbs,
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
                // 1.上传minio原图和缩略图
                //region 保存照片到minio或者本地
                String pathBigImage = ("0".equals(type) ? "uav" : "hive") + "/" + uavId + "/image/" + FolderName + "/" + fileName;
                String pathMiniImage = ("0".equals(type) ? "uav" : "hive") + "/" + uavId + "/thumbnail/" + FolderName + "/" + fileName;
                String urlBig = "";  // 原图路径
                String urlSmall = "";  // 缩略图路径
                String place = ""; //拍摄地址
                boolean photoStorageLocal = MyApplication.appConfig.isPhotoStorage(); // 存云端，还是存本地
                if (!photoStorageLocal) {
                    urlBig = "resourceminio/photo/" + pathBigImage;
                    String contentType = "image/" + suffix;
                    if (minioService.uploadImage("efuav", "photo/" + pathBigImage, contentType, new ByteArrayInputStream(fileStream))) {
                        // 处理成缩略图并且上传
                        File fileNew = FileUtil.getThumbnailInputStream(file);
                        if (fileNew != null && fileNew.exists()) {
                            sizeSmall = fileNew.length();
                            InputStream inputStream = new FileInputStream(fileNew);
                            if (!minioService.uploadImage("efuav", "photo/" + pathMiniImage, contentType, inputStream)) {
                                LogUtil.logWarn("原图上传成功，缩略图储存失败！");
                            }
                            urlBigFuture.complete(urlBig);
                            inputStream.close();
                            urlSmall = "resourceminio/photo/" + pathMiniImage;
//                            place = PhotoUtil.getPlace(lat, lng);
                            fileNew.delete();
                        }
                    } else {
                        LogUtil.logError("上传图片到云端失败！");
                    }
                } else {
                    urlBig = "resource/photo/" + pathBigImage;
                    urlSmall = "resource/photo/" + pathMiniImage;
                    String pathParentBig = BasePath + "photo/" + ("0".equals(type) ? "uav" : "hive") + "/" + uavId + "/image/" + FolderName + "/";
                    String pathParentSmall = BasePath + "photo/" + ("0".equals(type) ? "uav" : "hive") + "/" + uavId + "/thumbnail/" + FolderName + "/";
                    if (!FileUtil.saveFileAndThumbnail(fileStream, pathParentBig, pathParentSmall, fileName)) {
                        LogUtil.logError("上传图片失败！");
                    }
                }
                //endregion
                // 2、只要原图上传成功，就可以保存待数据库中
                //region 保存数据到数据库
//                EfPhoto efPhoto = efPhotoService.queryByCreatTime(uavId, fileName);
//                EfMediaPhoto efMediaPhoto = efMediaPhotoService.queryByCreatTime(uavId, fileName);
//                if (efMediaPhoto != null) {
//                    efMediaPhoto.setCameraVideoStreamSource(streamSource);
//                    efMediaPhoto.setPathImage(urlBig);
//                    efMediaPhoto.setSizeImage(sizeBig);
//                    efMediaPhoto.setPathThumbnail(urlSmall);
//                    efMediaPhoto.setSizeThumbnail(sizeSmall);
//                    efMediaPhoto.setCameraVideoStreamSource(streamSource);
//                    efMediaPhoto.setSourceId(0);
//                    efMediaPhoto.setLat(lat);
//                    efMediaPhoto.setLng(lng);
//                    efMediaPhoto.setAlt(alt);
//                    efMediaPhoto.setAltabs(altAbs);
//                    efMediaPhoto.setRoll(roll);
//                    efMediaPhoto.setGimbalRoll(gimbalRoll);
//                    efMediaPhoto.setYaw(yaw);
//                    efMediaPhoto.setGimbalYaw(gimbalYaw);
//                    efMediaPhoto.setPitch(pitch);
//                    efMediaPhoto.setGimbalPitch(gimbalPitch);
//                    efMediaPhoto.setPlace(place);
//                    EfMediaPhoto update = efMediaPhotoService.update(efMediaPhoto);
//                    if (update == null) {
//                        LogUtil.logError("更新媒体文件失败！");
//                    }
//                } else {
//                    efMediaPhoto = new EfMediaPhoto();
//                    efMediaPhoto.setCameraVideoStreamSource(streamSource);
//                    efMediaPhoto.setPathImage(urlBig);
//                    efMediaPhoto.setSizeImage(sizeBig);
//                    efMediaPhoto.setPathThumbnail(urlSmall);
//                    efMediaPhoto.setSizeThumbnail(sizeSmall);
//                    efMediaPhoto.setCreateDate(date);
//                    efMediaPhoto.setImageTag(fileName);
//                    efMediaPhoto.setDeviceid(uavId);
//                    efMediaPhoto.setType(Integer.valueOf(type));
//                    efMediaPhoto.setSourceId(0);
//                    efMediaPhoto.setLat(lat);
//                    efMediaPhoto.setLng(lng);
//                    efMediaPhoto.setAlt(alt);
//                    efMediaPhoto.setAltabs(altAbs);
//                    efMediaPhoto.setRoll(roll);
//                    efMediaPhoto.setGimbalRoll(gimbalRoll);
//                    efMediaPhoto.setYaw(yaw);
//                    efMediaPhoto.setGimbalYaw(gimbalYaw);
//                    efMediaPhoto.setPitch(pitch);
//                    efMediaPhoto.setGimbalPitch(gimbalPitch);
//                    efMediaPhoto.setPlace(place);
//                    EfMediaPhoto insert = efMediaPhotoService.insert(efMediaPhoto);
//                    if (insert == null) {
//                        LogUtil.logError("添加媒体文件到数据库失败！");
//                    }
//                }
//                future.complete(urlBig);
                //endregion
                LogUtil.logDebug("实时航拍图片储存耗时：" + (System.currentTimeMillis() - timeStart) / 1000d + "秒");
            } catch (Exception e) {
                future.completeExceptionally(e);
                LogUtil.logError("保存实时航拍图片异常：" + e.toString());
            }
        });
    }
}
