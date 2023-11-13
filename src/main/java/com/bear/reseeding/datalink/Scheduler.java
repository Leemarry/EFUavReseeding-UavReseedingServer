package com.bear.reseeding.datalink;


import com.bear.reseeding.utils.LogUtil;
import com.bear.reseeding.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private int day = -1;

    //每隔5分钟执行一次，每天凌晨重连Mqtt服务器
    @Scheduled(fixedRate = 60 * 1000 * 5)
    public void schedulinReconnectMqtt() {
        try {
            int newDay = Calendar.getInstance().get(Calendar.DATE);
            dateFormat.format(System.currentTimeMillis());
            LogUtil.logMessage("Checking Mqtt Status ...  Today is " + dateFormat.format(System.currentTimeMillis()) + ".");
            if (day == -1) {
                day = newDay;
            } else {
                boolean reconnect = (day != newDay);
                if (reconnect) {
                    day = newDay;

                    LogUtil.logMessage("已经重新连接Mqtt服务器！！！");
                }
            }
        } catch (Exception e) {
            LogUtil.logError("重新连接Mqtt服务器异常！！！" + e.toString());
        }
    }
}
