package com.bear.reseeding.task;

import com.bear.reseeding.entity.EfUavEachsortie;
import com.bear.reseeding.entity.EfUavRealtimedata;
import com.bear.reseeding.service.EfUavEachsortieService;
import com.bear.reseeding.service.EfUavRealtimedataService;
import com.bear.reseeding.utils.GisUtil;
import com.bear.reseeding.utils.LogUtil;
import com.bear.reseeding.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;


/**
 * 无人机实时心跳包处理
 *
 * @Auther: bear
 * @Date: 2023/7/19 15:41
 * @Description: null
 */
@Service
public class TaskUavHeartbeat {

    @Autowired
    private RedisUtils redisUtils;

    @Resource
    private EfUavRealtimedataService efUavRealtimedataService;

    @Autowired
    private EfUavEachsortieService efUavEachsortieService;

    /**
     * #数据库储存 true 开始存
     */
    @Value("${spring.config.DatabaseStorage:true}")
    private boolean databaseStorage;

    //队列大小
    private final int QUEUE_LENGTH = 1000;

    //基于内存的阻塞队列
    private BlockingQueue<EfUavRealtimedata> queue = new LinkedBlockingQueue<>(QUEUE_LENGTH);

    //创建计划任务执行器
    private ExecutorService service = new ThreadPoolExecutor(10, 1000, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

    /**
     * 临时储存
     */
    private List<EfUavRealtimedata> listTemp = new ArrayList<>();

    /**
     * 添加信息至队列中
     *
     * @param realtimedata 实时心跳包
     */
    public void addQueue(EfUavRealtimedata realtimedata) {
        if (databaseStorage) {
            queue.add(realtimedata);
        }
        handleEachsortie(realtimedata);
    }

    @PostConstruct
    private void init() {
        service.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        EfUavRealtimedata realtimedata = null;
                        boolean pollTimeout = false;
                        try {
                            realtimedata = queue.poll(5, TimeUnit.SECONDS);
                        } catch (InterruptedException ignored) {
                        }
                        if (null != realtimedata) {
                            listTemp.add(realtimedata);
                        } else {
                            pollTimeout = true;
                        }
                        if (listTemp.size() > 30 || (pollTimeout && listTemp.size() > 0)) {
                            // 插入数据
                            int count = efUavRealtimedataService.insertBatch(listTemp);
                            if (count != listTemp.size()) {
                                LogUtil.logWarn("储存无人机实时数据失败。共 " + listTemp.size() + " 条，储存成功 " + count + " 条！");
                            } else {
                                LogUtil.logDebug("储存无人机实时数据成功，共 " + listTemp.size() + " 条储存成功。");
                            }
                            listTemp.clear();
                        }
                        Thread.sleep(5);
                    } catch (InterruptedException ignored) {
                        break;
                    } catch (Exception e) {
                        LogUtil.logError("储存无人机实时数据储存：" + e.toString());
                    }
                }
            }
        });
        service.shutdown();
    }

    /**
     * 处理架次信息
     *
     * @param realtimedata 实时心跳包
     */
    private void handleEachsortie(EfUavRealtimedata realtimedata) {
        String uavIdSystem = realtimedata.getUavId();
        if (databaseStorage) {
            long connectTime = 0;
            Object connectTimeObj = redisUtils.get(uavIdSystem + "_connectTime");
            if (connectTimeObj != null) {
                connectTime = Long.parseLong(connectTimeObj.toString());
            } else {
                connectTime = System.currentTimeMillis();
            }
            redisUtils.set(uavIdSystem + "_connectTime", connectTime, 300L, TimeUnit.SECONDS);
            // 取上次心跳包
            Object data = redisUtils.get(uavIdSystem + "_heart");
            if (data != null) {
                Object sortieId = redisUtils.get(uavIdSystem + "_sortieId");
                Object object_aremdTime = redisUtils.get(uavIdSystem + "_aremdTime");
                Object object_aremVoltage = redisUtils.get(uavIdSystem + "_aremVoltage");
                Object object_airRange = redisUtils.get(uavIdSystem + "_airRange");
                EfUavRealtimedata lastHeart = (EfUavRealtimedata) data;
                long aremdTime = 0;  // 解锁时间
                long flyTime = 0;  //计算的总飞行时长，秒
                // 一直处于某个状态 , 无人机当前处于解锁状态
                if (realtimedata.getAremd() == 1) {
                    //region 解锁状态
                    if (object_aremdTime != null) {
                        aremdTime = (long) object_aremdTime;
                    } else {
                        aremdTime = System.currentTimeMillis();
                    }
                    flyTime = (System.currentTimeMillis() - aremdTime) / 1000; // 飞行时长
                           /* if (Math.abs(flightTimeInSeconds - flyTime) > 20) {
                                // 如果实际的飞行时长与计算的差别大，作为新架次处理
                                sortieId = null;
                                LogUtil.logWarn("无人机 " + uavIdSystem + " 实际飞行时长为 " + flightTimeInSeconds + " 秒，计算时长为 " + flyTime + " 秒，已作为新架次进行记录！");
                            }*/
                    // 如果上次有记录数据
                    if (sortieId != null) {
                        //更新架次信息， 计算时长和航程
                        double airRange = 0;
                        long airRangeAll = 0; //总路程
                        double distance = 0;
                        float aremVoltage = 0;
                        if (lastHeart.getLat() != 0 && lastHeart.getLng() != 0 && realtimedata.getLat() != 0 && realtimedata.getLng() != 0) {
                            distance = GisUtil.getDistance(lastHeart.getLat(), lastHeart.getLng(), realtimedata.getLat(), realtimedata.getLng()); // 最新路径
                        }
                        if (object_aremVoltage != null) {
                            aremVoltage = Long.parseLong(object_aremVoltage.toString());
                        } else {
                            if(realtimedata.getBatteryPert()!=null){
                                aremVoltage = realtimedata.getBatteryPert();
                            }

                            redisUtils.set(uavIdSystem + "_aremVoltage", realtimedata.getBatteryPert(), 7200L, TimeUnit.SECONDS);
                        }
                        if (object_airRange != null) {
                            airRangeAll = Long.parseLong(object_airRange.toString());
                        }
                        airRangeAll += distance;//总路程

                        EfUavEachsortie eachsortie = new EfUavEachsortie();
                        eachsortie.setId(sortieId.toString());
                        eachsortie.setUavId(uavIdSystem);
                        eachsortie.setAirRange((float) airRangeAll);
                        eachsortie.setFlyingTime(String.valueOf(flyTime));
                        eachsortie.setConnectTime(new Date(connectTime));
                        eachsortie.setAremdTime(new Date(aremdTime));
//                        eachsortie.setLastVoltage(Float.valueOf(realtimedata.getBatteryPert()));
//                        eachsortie.setAremdVoltage(aremVoltage);
//                        eachsortie.setLockedVoltage(Float.valueOf(realtimedata.getBatteryPert()));
                        eachsortie.setLockedTime(new Date(System.currentTimeMillis()));
                        eachsortie.setOnlineTime(String.valueOf(flyTime));
                        if (realtimedata.getBatteryPert() != null) {
                            eachsortie.setLastVoltage(Float.valueOf(realtimedata.getBatteryPert()));
                            eachsortie.setAremdVoltage(Float.valueOf(realtimedata.getBatteryPert()));
                            eachsortie.setLockedVoltage(Float.valueOf(realtimedata.getBatteryPert()));
                        } else {
                            // 处理未获取到电池电量信息的情况
                        }
                        EfUavEachsortie update = efUavEachsortieService.update(eachsortie);
                        // 目的：防止redis过期
                        redisUtils.set(uavIdSystem + "_sortieId", update.getId(), 300L, TimeUnit.SECONDS);
                        redisUtils.set(uavIdSystem + "_airRange", airRangeAll, 300L, TimeUnit.SECONDS);
                    } else {
                        // 新增一个架次
                        EfUavEachsortie eachsortie = new EfUavEachsortie();
                        eachsortie.setUavId(uavIdSystem);
                        eachsortie.setConnectTime(new Date(connectTime));
                        eachsortie.setAremdTime(new Date(System.currentTimeMillis()));
                        if (realtimedata.getBatteryPert() != null) {
                            eachsortie.setLastVoltage(Float.valueOf(realtimedata.getBatteryPert()));
                            eachsortie.setAremdVoltage(Float.valueOf(realtimedata.getBatteryPert()));
                            eachsortie.setLockedVoltage(Float.valueOf(realtimedata.getBatteryPert()));
                        } else {
                            // 处理未获取到电池电量信息的情况
                        }
//                        eachsortie.setLastVoltage(Float.valueOf(realtimedata.getBatteryPert()));
//                        eachsortie.setAremdVoltage(Float.valueOf(realtimedata.getBatteryPert()));
                        eachsortie.setFlyingTime(String.valueOf(flyTime));
                        eachsortie.setOnlineTime(String.valueOf(0));
                        eachsortie.setLockedTime(new Date(System.currentTimeMillis()));
//                        eachsortie.setLockedVoltage(Float.valueOf(realtimedata.getBatteryPert()));
                        // 新增一个架次
                        EfUavEachsortie insert1 = efUavEachsortieService.insert(eachsortie);
                        LogUtil.logMessage("无人机[" + uavIdSystem + "]开始飞行，当前飞行架次：" + insert1.getId());
                        if (null != insert1.getId()) {
                            redisUtils.set(uavIdSystem + "_sortieId", insert1.getId(), 300L, TimeUnit.SECONDS);
                            redisUtils.set(uavIdSystem + "_aremdTime", System.currentTimeMillis(), 7200L, TimeUnit.SECONDS);
                            redisUtils.set(uavIdSystem + "_aremVoltage", realtimedata.getBatteryPert(), 7200L, TimeUnit.SECONDS);
                            redisUtils.set(uavIdSystem + "_airRange", 0, 300L, TimeUnit.SECONDS);
                        }
                    }
                    //endregion 解锁状态
                } else {
                    if (lastHeart.getAremd().intValue() != realtimedata.getAremd().intValue()) {
                        // 刚刚锁定
                        LogUtil.logMessage("无人机[" + uavIdSystem + "]已锁定，当前飞行架次：" + redisUtils.get(uavIdSystem + "_sortieId"));
                        // 刚刚锁定
                        redisUtils.remove(uavIdSystem + "_sortieId",
                                uavIdSystem + "_aremdTime",
                                uavIdSystem + "_airRange",
                                uavIdSystem + "_connectTime",
                                uavIdSystem + "_aremVoltage");
                    } else {
                        //一直处于锁定状态
                        redisUtils.remove(uavIdSystem + "_sortieId");
                    }
                }
            }
        }
        redisUtils.set(uavIdSystem + "_heart", realtimedata, 300L, TimeUnit.SECONDS);
        redisUtils.set(uavIdSystem + "_online", System.currentTimeMillis(), 300L, TimeUnit.SECONDS);
    }
}

