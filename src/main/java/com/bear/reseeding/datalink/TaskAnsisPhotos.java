package com.bear.reseeding.datalink;
import com.bear.reseeding.common.ResultUtil;
import com.bear.reseeding.model.Result;
import com.bear.reseeding.task.MinioService;
import com.bear.reseeding.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Date;
import java.util.concurrent.*;

/**
 * 光伏照片处理线程池
 *
 * @Auther: bear
 * @Date: 2023/6/2 09:53
 * @Description: null
 */
@Component
public class TaskAnsisPhotos {
    @Value("${spring.config.pvUrl}")
    private String pvUrl;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private MinioService minioService;
    @Value("${BasePath:C://efuav/UavSystem/}")
    public String basePath;
    /**
     * 光伏分析照片类型： DEFAULT、INFRARED_THERMAL、WIDE、ZOOM
     */
    @Value("${spring.config.streamSourcePvAnal:DEFAULT}")
    String streamSourcePvAnal;

    //环保秸秆表， 创建线程池，同时指定核心线程数和最大线程数
    ThreadPoolExecutor threadPoolExecutorEp;

    // 创建线程池，同时指定核心线程数和最大线程数
    ThreadPoolExecutor threadPoolExecutor;

    /**
     * 保存图片到光伏分析表
     *
     * @param companyId    公司ID
     * @param date         拍摄照片的日期
     * @param uavId        无人机ID
     * @param fileName     文件名称，xxx.jpg
     * @param streamSource 图片类型: INFRARED_THERMAL /  WIDE / ZOOM
     * @param lat          纬度
     * @param lng          经度
     * @param fileStream   文件流
     * @des minio储存位置： efuav/[公司id]/pvimages/[变电站ID]/[无人机id]/yyyyMMdd/[xx.jpg]
     */
    public void savePv(String companyId, Date date, String uavId, String fileName, String streamSource, double lat, double lng, byte[] fileStream) {
        if (threadPoolExecutor == null) {
            // 构造一个线程池
            threadPoolExecutor = new ThreadPoolExecutor(1, 200, 30,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(30),
                    new ThreadPoolExecutor.CallerRunsPolicy());
        }
        threadPoolExecutor.execute(() -> {
            // 1、根据经纬度坐标获取 变电站SN和组串SN
            // 2、上传图片到桶
            // 3、传递图片地址，分析图片，得到结果
            // 4、保存数据到光伏实时表和异常表(覆盖式保存)
//            try {
//                long timeStart = System.currentTimeMillis();
//                if (fileStream == null || fileStream.length < 3) {
//                    LogUtil.logWarn("当前照片数据流为null，无法储存！！！");
//                    return;
//                }
//                if (lat == 0 || lng == 0) {
//                    LogUtil.logWarn("当前照片经纬度为 0，无法关联到变电站组串中！！！");
//                    return;
//                }
//                // 判断之前是否已经存过
//                boolean isExists = false;
//                // 当前是否分析过
//                boolean isAnalyzed = false;
//                // 光伏板是否异常
//                boolean isHasWrong = false;
//                // 当前光伏板异常数量
//                int wrongCount = 0;
//
//                // 光伏分析结果照片，minio路径
//                String imgResultPath = null;
//
//                // 根据无人机编号获取公司ID
//                // String companyId = redisUtils.getCompanyIdByUavId(uavId);
//                // 1、根据经纬度坐标获取 变电站SN和组串SN
//                String stationGroupSn = "";
//                String stationSn = "";
//
//                // 架次编号
//                int sortieId = -1;
//                Object sortieIdObj = redisUtils.get(uavId + "_sortieId");
//                if (sortieIdObj != null) {
//                    // 如果正在飞，直接使用当前飞行架次ID
//                    sortieId = ConvertUtil.convertToInt(sortieIdObj, -1);
//                }
//                if (sortieId == -1) {
//                    //根据拍摄时间，查询管理架次
//                    EfUavEachsortie eachsortie = efUavEachsortieService.queryByPhotoTime(date, uavId);
//                    if (eachsortie != null && eachsortie.getId() != null) {
//                        sortieId = ConvertUtil.convertToInt(eachsortie.getId(), -1);
//                    }
//                }
//                LogUtil.logInfo("照片 " + streamSource + "(" + date + ") 关联的架次编号为：" + sortieId);
//
//                String[] stationSnList = redisUtils.getCompanyPv(companyId);
//                if (stationSnList == null || stationSnList.length == 0) {
//                    LogUtil.logWarn("公司 " + companyId + " 没有绑定变电站！！！");
//                    return;
//                }
//                EfPvBoardGroup boardGroup = efPvBoardGroupService.queryByPostion(stationSnList, lat, lng);
//                if (boardGroup == null) {
//                    LogUtil.logWarn("照片 " + streamSource + " , 经纬度 " + lat + "," + lng + "未对应到变电站组串中！！！");
//                    return;
//                }
//                stationSn = boardGroup.getSubstationSn();
//                stationGroupSn = boardGroup.getGroupId();
//                double diff = GisUtil.getDistance(lat, lng, boardGroup.getWpLat(), boardGroup.getWpLng());
//                if (diff >= 30) {
//                    // 不是这个点拍的，没有匹配到。
//                    LogUtil.logMessage("照片 " + streamSource + " , 经纬度 " + lat + "," + lng + "对应到变电站 " + stationSn + " 组串 " + stationGroupSn + "，拍摄距离为：" + diff + "米！!!");
//                    return;
//                }
//                LogUtil.logMessage("照片 " + streamSource + " , 经纬度 " + lat + "," + lng + "对应到变电站 " + stationSn + " 组串 " + stationGroupSn + "，拍摄距离为：" + diff + "米.");
//
//                // 2、上传图片到桶
//                String ymd = DateUtil.timeStamp2Date(date.getTime(), "yyyyMMdd");
//                String path = companyId + "/pvimages/" + stationSn + "/" + uavId + "/" + ymd + "/" + fileName;
//                String contentType = "image/";
//                String contentTypeLast = "jpg";
//                if (fileName != null) {
//                    if (fileName.lastIndexOf(".") > 0) {
//                        contentTypeLast = fileName.substring(fileName.lastIndexOf("."));
//                    }
//                }
//                // 保存到 minio，无论成功失败，都需要新增记录到数据库中
//                minioService.uploadImage("efuav", path, contentType + contentTypeLast, new ByteArrayInputStream(fileStream));
//
//                // 3、分析红外照片，传递地址，分析图片，得到结果
//                List<EfPvWrongRealtime> wrongRealtimeList = new ArrayList<>();
//                List<EfPvWrong> wrongList = new ArrayList<>();
//                if ("DEFAULT".equalsIgnoreCase(streamSource) || streamSourcePvAnal.equalsIgnoreCase(streamSource)) {
//                    long startTime = System.currentTimeMillis();
//                    try {
//                        LogUtil.logMessage("正在分析光伏图片(" + streamSource + " " + stationGroupSn + ")...");
//                        //  3、分析光伏照片
//                        isAnalyzed = true;
////                        String base64 = Base64Util.GetImageStr("C:\\Users\\bear\\Desktop\\gf.jpg"); //  固定光伏照片
//                        String base64 = Base64Util.getImageBase64Str(fileStream);
//                        JSONObject jsonObject = new JSONObject();
//                        jsonObject.put("image_name", fileName);
//                        jsonObject.put("image_base64", base64);
//                        HttpRequest request = HttpUtil.createPost(pvUrl)
//                                .contentType("application/json;charset=utf-8")
//                                .body(JSONObject.toJSONString(jsonObject));
//                        // 连接超时30秒
//                        request.setConnectionTimeout(30000);
//                        // 读取超时150秒
//                        request.setReadTimeout(150000);
//                        HttpResponse response = request.execute();
//                        if (response == null) {
//                            LogUtil.logError("光伏图片接口调用异常:返回response为null");
//                        } else {
//                            String body = response.body();
//                            if (body == null) {
//                                LogUtil.logError("光伏图片接口调用异常：返回body为null");
//                            } else if ("Internal Server Error".equalsIgnoreCase(body)) {
//                                LogUtil.logError("光伏图片接口调用异常：" + body);
//                            } else {
//                                JSONObject object = new JSONObject();
//                                try {
//                                    object = JSONObject.parseObject(body);
//                                } catch (Exception ex) {
//                                    LogUtil.logError("光伏图片接口调用异常:异常信息：" + ex.toString() + "，返回Body:" + body);
//                                }
//                                JSONArray texts = new JSONArray();
//                                if (object.getIntValue("code") == 200 && object.get("data") != null) {
//                                    JSONObject data = object.getJSONObject("data");
//                                    //        "position": [],
//                                    //        "confidence": [],
//                                    //        "weiyi_position": [],
//                                    //        "weiyi_confidence": [],
//                                    //  rgb_img
//                                    // id 表示有异物的第几块光伏板
//                                    //ids = data.getJSONArray("id");
//                                    // confidence表示检测到异物的置信度分数
//                                    //JSONArray confidences = data.getJSONArray("confidence");
//                                    // position 表示这个异物在图片中的位置
//                                    JSONArray positions = data.getJSONArray("position");
//                                    texts = data.getJSONArray("text");
//                                    String base64ImageRes = data.getString("rgb_img");
//                                  /*  if (ids != null && ids.size() > 0) {
//                                        isHasWrong = true;
//                                    }*/
//                                    if (positions != null && positions.size() > 0) {
//                                        isHasWrong = true;
//                                        wrongCount = positions.size();
//                                    }
//                                    if (base64ImageRes != null && base64ImageRes.length() > 10) {
//                                        // 储存照片
//                                        imgResultPath = companyId + "/pvimages/" + stationSn + "/" + uavId + "/" + ymd + "/result/" + fileName;
//                                        byte[] temp = Base64Util.base64ImgToStream(base64ImageRes);
//                                        // 保存到 minio，无论成功失败，都需要新增记录到数据库中
//                                        minioService.uploadImage("efuav", imgResultPath, contentType + contentTypeLast, new ByteArrayInputStream(temp));
//                                    }
//                                }
//                                if (isHasWrong) {
//                                    // 储存到数据库
//                                    EfPvWrongRealtime wrongRealtime = new EfPvWrongRealtime();
//                                    wrongRealtime.setSubstationSn(stationSn);
//                                    wrongRealtime.setGroupId(stationGroupSn);
//                                    wrongRealtime.setTime(date);
//                                    wrongRealtime.setWrongTypeSn(20); // 默认异常
//                                    if (texts != null && texts.size() > 0) {
//                                        String text = texts.getString(0);
//                                        if (text != null) {
//                                            if (text.contains("遮挡")) {
//                                                wrongRealtime.setWrongTypeSn(21);
//                                            } else if (text.contains("裂缝")) {
//                                                wrongRealtime.setWrongTypeSn(22);
//                                            } else if (text.contains("位移")) {
//                                                wrongRealtime.setWrongTypeSn(23);
//                                            } else if (text.contains("光斑")) {
//                                                wrongRealtime.setWrongTypeSn(24);
//                                            }
//                                        }
//                                    }
//                                    wrongRealtime.setRow(0);  // 使用第 0行n列 去表示具体小板子编号 , 0 到 21 ， 11 * 2
//                                    wrongRealtime.setCloumn(0);
//                                    wrongRealtime.setResult(false);
//                                    wrongRealtimeList.add(wrongRealtime);
//
//                                    // 新的光伏表
//                                    EfPvWrong wrong = new EfPvWrong();
//                                    wrong.setWrongTypeSn(20); // 默认异常
//                                    if (texts != null && texts.size() > 0) {
//                                        String text = texts.getString(0);
//                                        wrong.setWrongDes(text);
//                                        if (text != null) {
//                                            if (text.contains("遮挡")) {
//                                                wrong.setWrongTypeSn(21);
//                                            } else if (text.contains("裂缝")) {
//                                                wrong.setWrongTypeSn(22);
//                                            } else if (text.contains("位移")) {
//                                                wrong.setWrongTypeSn(23);
//                                            } else if (text.contains("光斑")) {
//                                                wrong.setWrongTypeSn(24);
//                                            }
//                                        }
//                                    }
//                                    wrong.setRow(0);  // 使用第 0行n列 去表示具体小板子编号 , 0 到 21 ， 11 * 2
//                                    wrong.setCloumn(0);
//                                    wrongList.add(wrong);
//                                    LogUtil.logMessage("分析光伏图片完成(" + streamSource + " " + stationGroupSn + ")分析结果，存在 " + wrongCount + " 个异常。");
//                                } else {
//                                    LogUtil.logMessage("分析光伏图片完成(" + streamSource + " " + stationGroupSn + ")分析结果，正常。");
//                                }
//                               /* if (isHasWrong && ids.size() > 0) {
//                                    List<Integer> listAdded = new ArrayList<>();
//                                    for (int i = 0; i < ids.size(); i++) {
//                                        int id = ids.getIntValue(i) + 1; // 0 到 21 ， 11 * 2
//                                        // 储存到数据库
//                                        EfPvWrongRealtime wrongRealtime = new EfPvWrongRealtime();
//                                        wrongRealtime.setSubstationSn(stationSn);
//                                        wrongRealtime.setGroupId(stationGroupSn);
//                                        wrongRealtime.setTime(date);
//                                        wrongRealtime.setWrongTypeSn(20); // 默认异常
//                                        wrongRealtime.setRow(0);  // 使用第 0行n列 去表示具体小板子编号 , 0 到 21 ， 11 * 2
//                                        wrongRealtime.setCloumn(id);
//                                        wrongRealtime.setResult(false);
//                                        if (!listAdded.contains(id)) {
//                                            listAdded.add(id);
//                                            wrongRealtimeList.add(wrongRealtime);
//                                        }
//                                    }
//                                }*/
//                            }
//                        }
//                    } catch (Exception e) {
//                        LogUtil.logError("光伏图片分析异常：" + e.toString());
//                    }
//                    LogUtil.logMessage("光伏图片(" + streamSource + " " + stationGroupSn + ")分析耗时：" + (System.currentTimeMillis() - startTime) / 1000d + "秒");
//                }
//
//                //region 光伏记录储存
//                EfPvRealtime realtime = new EfPvRealtime();
//                realtime.setSubstationSn(stationSn);
//                realtime.setGroupId(stationGroupSn);
//                realtime.setUavSortie(sortieId);
//                realtime.setUavId(uavId);
//                realtime.setTime(date);
//                if (isAnalyzed) {
//                    realtime.setHasAnalyzed(isAnalyzed);
//                    realtime.setHasWrong(isHasWrong);
//                    realtime.setWrongCount(wrongCount);
//                }
//                if ("INFRARED_THERMAL".equalsIgnoreCase(streamSource)) {
//                    realtime.setImg5("resourceminio/" + path); // img5储存红外图片路径
//                } else if ("WIDE".equalsIgnoreCase(streamSource)) {
//                    realtime.setImg1("resourceminio/" + path);
//                } else if ("ZOOM".equalsIgnoreCase(streamSource)) {
//                    realtime.setImg2("resourceminio/" + path);
//                    realtime.setImg("resourceminio/" + path);
//                } else {
//                    realtime.setImg("resourceminio/" + path);
//                }
//                if (imgResultPath != null) {
//                    realtime.setImgRes("resourceminio/" + imgResultPath);
//                }
//                // 影响行数：0失败，1插入成功，2 修改成功，3 插入并移动到历史表成功
//                int count = efPvRealtimeService.callAddPvData(realtime, wrongRealtimeList);
//                if (count == 0) {
//                    LogUtil.logWarn("保存光伏航拍图片失败！");
//                } else {
//                    LogUtil.logDebug("保存光伏航拍图片结果(0失败，1插入成功，2修改成功，3转移插入成功 , 4转移失败插入成功)：" + count);
//                }
//                //endregion
//
//                //region 光伏记录储存新表
//                try {
//                    EfPvData data = new EfPvData();
//                    data.setSubstationSn(stationSn);
//                    data.setGroupId(stationGroupSn);
//                    data.setUavSortie(sortieId);
//                    data.setUavId(uavId);
//                    data.setTime(date);
//                    if (isAnalyzed) {
//                        data.setHasAnalyzed(isAnalyzed);
//                        data.setHasWrong(isHasWrong);
//                        data.setExceptionCount(wrongCount);
//                    }
//                    if ("INFRARED_THERMAL".equalsIgnoreCase(streamSource)) {
//                        data.setImg5("resourceminio/" + path); // img5储存红外图片路径
//                    } else if ("WIDE".equalsIgnoreCase(streamSource)) {
//                        data.setImg1("resourceminio/" + path);
//                    } else if ("ZOOM".equalsIgnoreCase(streamSource)) {
//                        data.setImg2("resourceminio/" + path);
//                        data.setImg("resourceminio/" + path);
//                    } else {
//                        data.setImg("resourceminio/" + path);
//                    }
//                    if (imgResultPath != null) {
//                        data.setImgRes("resourceminio/" + imgResultPath);
//                    }
//                    count = efPvDataService.insertOrUpdate(data, wrongList);
//                    if (count == 0) {
//                        LogUtil.logWarn("保存光伏航拍图片失败！");
//                    } else {
//                        LogUtil.logDebug("保存光伏航拍图片结果：" + count);
//                    }
//                } catch (Exception e) {
//                    LogUtil.logError("保存光伏实时数据异常：" + e.toString());
//                }
//                //endregion
//
//                LogUtil.logDebug("光伏图片储存耗时：" + (System.currentTimeMillis() - timeStart) / 1000d + "秒");
//            } catch (Exception e) {
//                LogUtil.logError("保存光伏航拍图片异常：" + e.toString());
//            }
        });
    }

    /**
     * 保存图片到环保秸秆记录表
     *
     * @param companyId     公司ID
     * @param date          拍摄照片的日期
     * @param uavId         无人机ID
     * @param fileName      文件名称，dji6554248_20230615183500.jpg
     * @param streamSource  图片类型: INFRARED_THERMAL /  WIDE / ZOOM
     * @param lat           纬度
     * @param lng           经度
     * @param alt           高度
     * @param altAbs        海拔
     * @param gimbalRoll    云台roll
     * @param gimbalPitch   云台pitch
     * @param gimbalYaw     云台yaw
     * @param exceptionType 异常类型  1 火点   2 黑斑  0 其他正常
     * @param packet        文件流
     */
    public void saveEp(String companyId, Date date, String uavId, String fileName, String streamSource,
                       double lat, double lng, double alt, double altAbs,
                       double gimbalRoll, double gimbalPitch, double gimbalYaw,
                       int exceptionType, byte[] packet) {
        if (threadPoolExecutorEp == null) {
            // 构造一个线程池
            threadPoolExecutorEp = new ThreadPoolExecutor(1, 100, 30,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(30),
                    new ThreadPoolExecutor.CallerRunsPolicy());
        }
        threadPoolExecutorEp.execute(new Runnable() {
            @Override
            public void run() {
//                try {
////                    LogUtil.logDebug(streamSource + "来了 。。。。。。。。。。。。。");
//                    // 读取图片经纬度
//                    // 根据经纬度获取地理位置
//                    // 火点发送短信
//                    long timeStart = System.currentTimeMillis();
//                    if (packet == null || packet.length <= 0) {
//                        LogUtil.logWarn("当前照片数据流为null，无法储存！！！");
//                        return;
//                    }
//                    String uavName = redisUtils.getUavName(uavId);
//                    boolean photoStorageLocal = ParkingapronApplication.appConfig.isPhotoStorage(); // true存本地 false存minio
//                    String FolderName = DateUtil.timeStamp2Date(date.getTime(), "yyyyMMdd");
//                    String path = "photo/uav" + "/" + uavId + "/image/" + FolderName + "/" + fileName;
//                    String contentType = "image/";
//                    String contentTypeLast = "jpg";
//                    if (fileName != null) {
//                        if (fileName.lastIndexOf(".") > 0) {
//                            contentTypeLast = fileName.substring(fileName.lastIndexOf("."));
//                        }
//                    }
//                    if (photoStorageLocal) {
//                        path = "resource/" + path;
//                        String pathParentBig = basePath + "photo/uav/" + uavId + "/image/" + FolderName + "/";
//                        String pathParentSmall = basePath + "photo/uav/" + uavId + "/thumbnail/" + FolderName + "/";
//                        // "photo/" + ("0".equals(type) ? "uav" : "hive") + "/" + UavID + "/thumbnail/" + FolderName + "/";
//                        if (!FileUtil.saveFileAndThumbnail(packet, pathParentBig, pathParentSmall, fileName)) {
//                            LogUtil.logWarn("储存无人机[" + uavId + "]环保巡查照片[" + fileName + "]到本地失败!");
//                        }
//                    } else {
//                        // 保存到 minio，无论成功失败，都需要新增记录到数据库中
//                        minioService.uploadImage("efuav", path, contentType + contentTypeLast, new ByteArrayInputStream(packet));
//                        path = "resourceminio/" + path;
//                    }
//
//                    //region 获取图片的地理位置
//                    String addressFull = "";
//                    String name = null;
//                    String city = null;
//                    String township = null;
//                    String province = null;
//                    String district = null;
//                    if (lat != 0 && lng != 0) {
//                        try {
//                            String address = "https://restapi.amap.com/v3/geocode/regeo?location=" + lng + "," + lat + "&key=690ee7d7356fc5f1d90c0f1d3a650d70&radius=1000&extensions=all";
//                            address = HttpRequestUtil.HttpRequest(address);
//                            JSONObject jsonObject = JSONObject.parseObject(address);
//                            if (jsonObject.getIntValue("status") == 1) {
//                                Object regeocode = jsonObject.get("regeocode");
//                                JSONObject object2 = JSONObject.parseObject(regeocode.toString());
//                                addressFull = object2.get("formatted_address").toString();
//                                String obj = object2.get("addressComponent").toString();
//                                JSONObject addressComponent = JSONArray.parseObject(obj);
//                                city = addressComponent.get("city").toString();
//                                province = addressComponent.get("province").toString();
//                                district = addressComponent.get("district").toString();
//                                String obj1 = addressComponent.get("streetNumber").toString();
//                                JSONObject streetNumber = JSONArray.parseObject(obj1);
//                                name = streetNumber.get("street").toString();
//                                township = addressComponent.get("township").toString();
//                                if (province == null || "[]".equals(province)) {
//                                    province = "";
//                                }
//                                if (city == null || "[]".equals(city)) {
//                                    city = province;
//                                }
//                                if (township == null || "[]".equals(township)) {
//                                    township = "";
//                                }
//                            }
//                        } catch (Exception e) {
//                            LogUtil.logError("解析经纬度到地理位置异常：" + e.toString());
//                        }
//                    }
//                    //endregion
//
//                    //region 火点发送短信
//                    boolean sendSmsNow = false;
//                    if (exceptionType == 1) {
//                        Object lastSmsTimeObj = redisUtils.get("ep_sms_huodian_" + uavId);
//                        if (lastSmsTimeObj != null) {
//                            long lastSmsTime = Long.parseLong(lastSmsTimeObj.toString());
//                            long time = date.getTime() - lastSmsTime;
//                            if (time > 10000) {
//                                sendSmsNow = true;
//                            }
//                        } else {
//                            sendSmsNow = true;
//                        }
//                    }
//                    if (sendSmsNow) {
//                        redisUtils.set("ep_sms_huodian_" + uavId, date.getTime(), 10L, TimeUnit.SECONDS);
//                        SmsUtil.getInstance().SendStrawSms(companyId, uavName.substring(0, 1), String.valueOf(lng), String.valueOf(lat), addressFull);
//                    }
//                    //endregion
//
//                    //region 保存到数据库
//                    boolean isUpdate = false;
//                    EfPhotoEp efPhoto = efPhotoEpService.queryNewstByUav(uavId);
//                    if (efPhoto != null && (Math.abs(efPhoto.getCreateDate().getTime() - date.getTime())) < 5000) {
//                        // 修改
//                        isUpdate = true;
//                    } else {
//                        // 新增
//                        efPhoto = new EfPhotoEp();
//                    }
//                    if ("INFRARED_THERMAL".equalsIgnoreCase(streamSource)) {
//                        efPhoto.setPathImage5(path);
//                        efPhoto.setSizeImage5(packet.length);
//                    } else if ("WIDE".equalsIgnoreCase(streamSource)) {
//                        efPhoto.setPathImage2(path);
//                        efPhoto.setSizeImage2(packet.length);
//                        if (StringUtils.isEmpty(efPhoto.getPathImage())) {
//                            efPhoto.setPathImage(path);
//                            efPhoto.setSizeImage(packet.length);
//                        }
//                    } else if ("ZOOM".equalsIgnoreCase(streamSource)) {
//                        efPhoto.setPathImage3(path);
//                        efPhoto.setSizeImage3(packet.length);
//                        if (StringUtils.isEmpty(efPhoto.getPathImage())) {
//                            efPhoto.setPathImage(path);
//                            efPhoto.setSizeImage(packet.length);
//                        }
//                    } else {
//                        efPhoto.setPathImage(path);
//                        efPhoto.setSizeImage(packet.length);
//                    }
//                    efPhoto.setCameraVideoStreamSource("DEFAULT");
//                    efPhoto.setCreateDate(date);
//                    efPhoto.setImageTag(fileName);
//                    efPhoto.setDeviceid(uavId);
//                    efPhoto.setLat(lat);//纬度
//                    efPhoto.setLng(lng);//经度
//                    efPhoto.setAlt((float) alt);
//                    efPhoto.setAltabs((float) altAbs);
//                    efPhoto.setExceptionType(exceptionType);
//                    efPhoto.setBlackspotArea("1");//火点/黑斑面积
//                    efPhoto.setBlackspotNum(1);//火点/黑斑个数
//                    efPhoto.setPatrolArea("1");//巡逻总面积
//                    efPhoto.setPlace(addressFull); //地址
//                    efPhoto.setFlightGroup(uavName.substring(0, 1)); //飞行组,飞机名称的第一个字母
//                    efPhoto.setProvince(province);
//                    efPhoto.setCity(city);
//                    efPhoto.setVillages(township);
//                    efPhoto.setGimbalRoll(gimbalRoll);
//                    efPhoto.setGimbalPitch(gimbalPitch);
//                    efPhoto.setGimbalYaw(gimbalYaw);
//                    efPhoto.setNickname(uavName.substring(0, 1) + city + name + date.getTime());
//                    if (isUpdate) {
//                        EfPhotoEp update = efPhotoEpService.update(efPhoto);
//                        if (update == null) {
//                            LogUtil.logError("修改环保数据媒体文件失败！");
//                        }
//                    } else {
//                        EfPhotoEp insert = efPhotoEpService.insert(efPhoto);
//                        if (insert == null) {
//                            LogUtil.logError("添加媒体文件到环保数据表失败！");
//                        }
//                    }
//                    //endregion
//                } catch (
//                        Exception e) {
//                    LogUtil.logError("储存无人机[" + uavId + "]环保巡查照片[" + fileName + "]时异常：" + e.toString());
//                }
////                LogUtil.logDebug(streamSource + "结束！！！！！！！！！！！");
            }
        });
    }

    /**
     * 分析光伏照片
     *
     * @param inputStream 照片数据流
     * @param lat         拍摄纬度
     * @param lng         拍摄经度
     * @return 分析结果
     */
    public Result pvAnalysis(InputStream inputStream, double lat, double lng) {

        return ResultUtil.success();
    }
}
