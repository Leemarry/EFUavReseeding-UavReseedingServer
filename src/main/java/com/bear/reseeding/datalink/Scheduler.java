package com.bear.reseeding.datalink;


import com.bear.reseeding.MyApplication;
import com.bear.reseeding.entity.*;
import com.bear.reseeding.model.MqttItem;
import com.bear.reseeding.service.*;
import com.bear.reseeding.utils.LogUtil;
import com.bear.reseeding.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 定时器
 *
 * @Auther: bear
 * @Date: 2021/12/28 09:48
 * @Description: null
 */
@Component
public class Scheduler {
    @Autowired
    private RedisUtils redisUtils;
    @Resource
    private EfRelationUavsnService efRelationUavsnService;
    @Resource
    private EfRelationUserUavService efRelationUserUavService;
    @Resource
    private EfCompanyService efCompanyService;
    @Resource
    private EfRelationCompanyUavService efRelationCompanyUavService;
    @Resource
    private EfUserService efUserService;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private int day = -1;

    //每隔5分钟执行一次，每天凌晨重连Mqtt服务器
    @Scheduled(fixedRate = 60 * 1000 * 5)
    public void schedulinReconnectMqtt() {
        try {
//            int newYear = Calendar.getInstance().get(Calendar.YEAR);
            int newDay = Calendar.getInstance().get(Calendar.DATE);
//            int newMouth = Calendar.getInstance().get(Calendar.MONTH) + 1;
            dateFormat.format(System.currentTimeMillis());
            LogUtil.logMessage("Checking Mqtt Status ...  Today is " + dateFormat.format(System.currentTimeMillis()) + ".");
            if (day == -1) {
                day = newDay;
            } else {
                boolean reconnect = (day != newDay);
                if (reconnect) {
                    day = newDay;
                    MqttItem mqttDji = MqttUtil.MapMqttItem.get(MqttUtil.Tag_Djiapp);
                    if (mqttDji != null) {
                        LogUtil.logMessage("重新连接Mqtt服务器，检测大疆服务目前连接状态为：" + mqttDji.isConnect());
                        mqttDji.DisconnMqtt();
                    }
                    MqttItem mqttEf = MqttUtil.MapMqttItem.get(MqttUtil.Tag_efuavapp);
                    if (mqttEf != null) {
                        LogUtil.logMessage("重新连接Mqtt服务器，检测开源服务目前连接状态为：" + mqttEf.isConnect());
                        mqttEf.DisconnMqtt();
                    }
                    MyApplication.InitMqtt();
                    LogUtil.logMessage("已经重新连接Mqtt服务器！！！");
                }
            }
        } catch (Exception e) {
            LogUtil.logError("重新连接Mqtt服务器异常！！！" + e.toString());
        }
    }


    //每隔60秒执行一次 ， 更新redis
    @Scheduled(fixedRate = 60 * 1000)
    public void scheduling60s() {
        try {
            //1分钟重新获取数据库基础数据
            if (redisUtils != null) {
//                LogUtil.logMessage("TIMER >> refresh data ...");
//            LogUtil.logMessage("定时任务执行：" + dateFormat.format(new Date()) + " 已刷新缓存数据。");
                List<EfRelationUavsn> efRelationUavsns = efRelationUavsnService.queryAllByLimit(0, 100000);
                List<EfRelationUserUav> efRelationUserUavs = efRelationUserUavService.queryAllByLimit(0, 100000);
                redisUtils.remove("rel_uav_sn_id");
                redisUtils.remove("rel_uav_id_sn");
                if (efRelationUavsns != null && efRelationUavsns.size() > 0) {
                    for (EfRelationUavsn item : efRelationUavsns) {
                        // 1 对 1 关系
                        redisUtils.hmSet("rel_uav_sn_id", item.getUavSn(), item.getUavId());
                        redisUtils.hmSet("rel_uav_id_sn", item.getUavId(), item.getUavSn());
                        Object obj = redisUtils.hmGet("rel_uav_sn_id", item.getUavSn()); //根据无人机SN获取无人机ID  2,1,
                    }
                } else {
                }
                redisUtils.remove("rel_userid_uavid");
                redisUtils.remove("rel_uavid_userid");
                if (efRelationUserUavs != null && efRelationUserUavs.size() > 0) {
                    HashMap<String, String> map1 = new HashMap<>();
                    HashMap<String, String> map2 = new HashMap<>();
                    for (EfRelationUserUav item : efRelationUserUavs) {
                        // n 对 n 关系
                        String array = map1.getOrDefault(item.getUserId().toString(), "");
                        array += (item.getUavId()) + ",";
                        map1.put(item.getUserId().toString(), array);

                        array = map2.getOrDefault(item.getUavId(), "");
                        array += (item.getUserId().toString()) + ",";
                        map2.put(item.getUavId(), array);
                    }
                    Iterator iter = map1.keySet().iterator();
                    while (iter.hasNext()) {
                        Object key = iter.next();
                        Object val = map1.get(key);
                        redisUtils.hmSet("rel_userid_uavid", key.toString(), val);
                    }
                    iter = map2.keySet().iterator();
                    while (iter.hasNext()) {
                        Object key = iter.next();
                        Object val = map2.get(key);
                        redisUtils.hmSet("rel_uavid_userid", key.toString(), val);
                    }
                } else {

                }
                //Object test1 = redisUtils.hmGet("rel_userid_uavid", "1");
                //Object test2 = redisUtils.hmGet("rel_uavid_userid", "UAV1000");
                redisUtils.remove("rel_userid_hiveid");
                redisUtils.remove("rel_hiveid_userid");

                //公司 - 公司功能业务
//                List<EfCompany> efCompaniesList = efCompanyService.queryAll(); //所有公司
//                redisUtils.remove("rel_companyid_function");
//                if (efCompaniesList != null && efCompaniesList.size() > 0) {
//                    for (EfCompany item : efCompaniesList) {
//                        if (item.getcFunction() != null) {
//                            redisUtils.hmSet("rel_companyid_function", String.valueOf(item.getId()), item.getcFunction());
//                        }
//                    }
//                }

                // 无人机 - 公司
                List<EfRelationCompanyUav> efRelationCompanyUavs = efRelationCompanyUavService.queryAll2(); //公司 - 无人机
                redisUtils.remove("rel_uavid_companyid");
                if (efRelationCompanyUavs != null && efRelationCompanyUavs.size() > 0) {
                    for (EfRelationCompanyUav item : efRelationCompanyUavs) {
                        redisUtils.hmSet("rel_uavid_companyid", item.getUavId(), String.valueOf(item.getCompanyId()));
                    }
                }
                // 公司 - 管理员 amdin1,admin2,admin3
                List<EfUser> users = efUserService.queryAllAdmin();
                redisUtils.remove("rel_companyid_usersid");
                if (users != null && users.size() > 0) {
                    Map<Integer, String> mapCompanyIdAdmins = new HashMap<>();
                    for (EfUser item : users) {
                        int cid = item.getUCId();
                        String user = String.valueOf(item.getId());
                        String lastUsers = mapCompanyIdAdmins.getOrDefault(cid, "");
                        if ("".equals(lastUsers)) {
                            lastUsers = user;
                        } else {
                            lastUsers += "," + user;
                        }
                        mapCompanyIdAdmins.put(cid, lastUsers);
                    }
                    for (Map.Entry<Integer, String> item : mapCompanyIdAdmins.entrySet()) {
                        redisUtils.hmSet("rel_companyid_usersid", String.valueOf(item.getKey()), item.getValue());
                    }
                }
            } else {
                LogUtil.logError("TIMER >> refresh data,redis is null!");
//            LogUtil.logError("定时任务执行：" + dateFormat.format(new Date()) + " Redis为空。");
//            redisUtils = new RedisUtils();
            }
        } catch (Exception e) {
            LogUtil.logError("TIMER >> refresh data error" + e.getMessage());
        }
    }

}
