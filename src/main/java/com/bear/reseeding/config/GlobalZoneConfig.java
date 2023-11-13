package com.bear.reseeding.config;

import com.bear.reseeding.utils.LogUtil;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.ZoneOffset;
import java.util.TimeZone;

/**
 * 全局时区
 *
 * @author sxh
 * @date 2022/3/20 11:40
 */
@Configuration
public class GlobalZoneConfig {
    @PostConstruct
    void started() {
        //时区设置：中国上海（东八区）
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.of("+8")));
        LogUtil.logMessage("设置时区为：东8区");
    }
}