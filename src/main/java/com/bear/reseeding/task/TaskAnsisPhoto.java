package com.bear.reseeding.task;

import com.alibaba.fastjson.JSONArray;
import com.bear.reseeding.MyApplication;
import com.bear.reseeding.common.ResultUtil;
import com.bear.reseeding.datalink.WebSocketLink;
import com.bear.reseeding.eflink.EFLINK_MSG_10010;
import com.bear.reseeding.eflink.EFLINK_MSG_10021;
import com.bear.reseeding.entity.EfCavity;
import com.bear.reseeding.entity.EfMediaPhoto;
import com.bear.reseeding.entity.EfUavEachsortie;
import com.bear.reseeding.service.EfCavityService;
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
import javax.xml.crypto.Data;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
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
    @Resource
    private EfCavityService efCavityService;

    @Value("${BasePath:C://efuav/UavSystem/}")
    public String basePath;
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
    public void saveSeedingPhoto(MultipartFile file, Map<String, Object> map) {
        if (threadPoolExecutorPhoto == null) {
            // 构造一个线程池
            threadPoolExecutorPhoto = new ThreadPoolExecutor(10, 200, 30,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(30),
                    new ThreadPoolExecutor.CallerRunsPolicy());
        }
        threadPoolExecutorPhoto.execute(() -> {
            try {
                long timenow = System.currentTimeMillis();
                if (file.isEmpty()) {
                    LogUtil.logWarn("上传失败，无法储存！！！");
                }
                /**解析 获取map 数据*/
                String uavSn = map.getOrDefault("uavSn", '0').toString();
                String latStr = map.getOrDefault("lat", "").toString();
                Double lat = latStr.isEmpty() ? 0 : Double.parseDouble(latStr);
                String lngStr = map.getOrDefault("lng", "").toString();
                Double lng = lngStr.isEmpty() ? 0 : Double.parseDouble(lngStr);
                String altStr = map.getOrDefault("alt", "").toString();
                float alt = altStr.isEmpty() ? 0 : Float.parseFloat(altStr);
                String altabsStr = map.getOrDefault("alt", "").toString();
                Float altAbs = altabsStr.isEmpty() ? null : Float.parseFloat(altabsStr);
                String type = map.getOrDefault("type", "0").toString();

                // data
                List<Map<String, Object>> dataList = (List<Map<String, Object>>) map.get("data");
                String efCavityIdStr = dataList.get(0).getOrDefault("id", "").toString();
//                Double efCavityId = efCavityIdStr.isEmpty() ? null : Double.parseDouble(efCavityIdStr);
//                String efCavityName = dataList.get(0).getOrDefault("id", "efCavityNameid").toString();
//                String efCavityLevel = dataList.get(0).getOrDefault("level", "").toString();
//                String efCavitySquare = dataList.get(0).getOrDefault("square", "").toString();
                String efCavitySeedNumStr = dataList.get(0).getOrDefault("seedNumber", "").toString();
                double efCavitySeedNum = efCavitySeedNumStr.isEmpty() ? 0 : Double.parseDouble(efCavitySeedNumStr);

                //获取分析图片的id
                Integer photoId = (int) map.getOrDefault("photoId", -1);

                /** 存储minio Or 本地 */
                // 获取文件名-大小
                String fileName = file.getOriginalFilename();
                long fileSize = file.getSize();
                // 获取最后一个点的索引位置取后缀名
                String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
                // 创建时间为文件夹
                Date time = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
                String FolderName = dateFormat.format(time) + "-result";

                //region 保存照片到minio或者本地
                String pathBigImage = ("0".equals(type) ? "uav" : "hive") + "/" + uavSn + "/image/" + FolderName + "/" + fileName;
                String pathMiniImage = ("0".equals(type) ? "uav" : "hive") + "/" + uavSn + "/thumbnail/" + FolderName + "/" + fileName;
                String urlBig = "";  // 原图路径
//                String urlSmall = "";  // 缩略图路径
//                long sizeSmall = 0; // 缩略图大小
                byte[] fileBytes = file.getBytes(); //照片文件流
                boolean photoStorageLocal = MyApplication.appConfig.isPhotoStorage(); // 存云端，还是存本地
                if (!photoStorageLocal) {
                    urlBig = "resourceminio/photo/" + pathBigImage;
                    String contentType = "image/" + extension;
                    if (minioService.uploadImage("efuav", "photo/" + pathBigImage, contentType, new ByteArrayInputStream(fileBytes))) {
                        // 处理成缩略图并且上传
//                        File fileNew = FileUtil.getThumbnailInputStream(file);
//                        if (fileNew != null && fileNew.exists()) {
//                            sizeSmall = fileNew.length();
//                            InputStream inputStream = new FileInputStream(fileNew);
//                            if (!minioService.uploadImage("efuav", "photo/" + pathMiniImage, contentType, inputStream)) {
//                                LogUtil.logWarn("原图上传成功，缩略图储存失败！");
//                            }
//                            urlBigFuture.complete(urlBig);
//                            inputStream.close();
//                            urlSmall = "resourceminio/photo/" + pathMiniImage;
//                            place = PhotoUtil.getPlace(lat, lng);
//                            fileNew.delete();
//                        }
                        LogUtil.logError("上传图片到云端成功！");
                    } else {
                        LogUtil.logError("上传图片到云端失败！");
                    }
                } else {
                    String pathParentBig = BasePath + "photo/" + ("0".equals(type) ? "uav" : "hive") + "/" + uavSn + "/image/" + FolderName + "/";
                    String pathParentSmall = BasePath + "photo/" + ("0".equals(type) ? "uav" : "hive") + "/" + uavSn + "/thumbnail/" + FolderName + "/";
                    if (!FileUtil.saveFileAndThumbnail(fileBytes, pathParentBig, pathParentSmall, fileName)) {
                        LogUtil.logError("上传图片失败！");
                    }
                    // 指定文件存储路径
                    urlBig = "resource/photo/" + pathBigImage;
                    File dest = new File(urlBig);
                    if (!dest.exists()) {
                        boolean res = dest.mkdirs();
                        if (!res) {
                            LogUtil.logWarn("创建目录失败！");
                            return;
                        }
                    }
                    Files.copy(file.getInputStream(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                //endregion

                /**拼接==获取minion路径*/
                String imgPath = urlBig + "/" + fileName;
                // 前端vue代理访问路径
                String resourceUrl = "resource/" + imgPath;
                //  efCavityService  时间问题 怎么知道是哪一个架次 和 图片 ---查询
                /** 根据实时请求的时间--查询最新解锁无人机的架次 **/
                Integer efUavEachsortieId = -1;
                EfUavEachsortie efUavEachsortie = efUavEachsortieService.queryByPhotoTime(time, uavSn);
                if (efUavEachsortie != null && efUavEachsortie.getId() != null) {
                    efUavEachsortieId = ConvertUtil.convertToInt(efUavEachsortie.getId(), -1);
                } else {
                    LogUtil.logWarn("未查询到该架次 ！！！");
                }
                /** 更新存储图片表 添加分析图*/
                EfMediaPhoto efMediaPhoto = null;
                if (photoId == -1) {
                    // 没有返回发送图的id  --进行查询
                    efMediaPhoto = efMediaPhotoService.queryByUavIdAndLatestTime(uavSn, time);
                } else {
                    //存在返回图片Id
                    efMediaPhoto = efMediaPhotoService.queryById(photoId);
                }
                //更改该图片id下分析图
                if (efMediaPhoto != null) {
                    photoId = efMediaPhoto.getId();
                    efMediaPhoto.setPathImageAnalysis(resourceUrl);
                    efMediaPhoto.setSizeImageAnalysis(fileSize);
                    // 更新
                    EfMediaPhoto update = efMediaPhotoService.update(efMediaPhoto);
                    if (update == null) {
                        LogUtil.logError("更新媒体文件失败！");
                    }
                } else {
                    LogUtil.logWarn("未查询 ！！！");
                }
                /** 数据新到数据库表 -- 洞斑信息表*/
                EfCavity efCavity = new EfCavity();
                efCavity.setAlt(alt);
                efCavity.setAltabs(altAbs);
                efCavity.setLat(lat);
                efCavity.setLng(lng);
                efCavity.setCavityName(efCavityIdStr);
                efCavity.setSeedingCount((int) efCavitySeedNum);
                efCavity.setEachsortieId(efUavEachsortieId); // 飞行架次
                efCavity.setPhotoId(photoId);
                efCavityService.insert(efCavity);

                /** websocket 推送信息到前端*/
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
                // 推送到前台
                String[] owerUsers = new String[0];
                Object obj = null;
                obj = redisUtils.hmGet("rel_uavid_userid", uavSn); //无人机ID获取用户ID  2,1,
                if (obj != null) {
                    String delims = "[,]+";
                    owerUsers = obj.toString().split(delims);
                }
                if (owerUsers.length <= 0) {
                    LogUtil.logWarn("MQTT：无人机[" + uavSn + "]未绑定用户！");
                }
                obj = redisUtils.hmGet("rel_uavid_companyid", uavSn);
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
                EFLINK_MSG_10021 msg10021 = new EFLINK_MSG_10021();
                msg10021.setUavId(uavSn);
                //timenow接收到分析图片的时间
                msg10021.setTime(timenow);
                msg10021.setAlt(alt);
                msg10021.setAltAbs(altAbs);
                msg10021.setLat(lat);
                msg10021.setLng(lng);
                msg10021.setUrl(resourceUrl);
                WebSocketLink.push(ResultUtil.success(msg10021.EFLINK_MSG_ID, uavSn, null, msg10021), owerUsers);
            } catch (Exception e) {
            }
        });
    }
}
