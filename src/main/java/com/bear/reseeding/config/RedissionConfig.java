package com.bear.reseeding.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redission配置类，主要作用就是建立redis连接，生成client对象
 */
@Slf4j
@Configuration
public class RedissionConfig {
    @Autowired
    private RedisProperties redisProperties;
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String url = "redis://"+ redisProperties.getHost() + ":" + redisProperties.getPort();
        System.out.println(url);
        config.useSingleServer()
                .setAddress(url);
//                .setPassword(redisProperties.getPassword()); // 如果有密码需要进行设置

        return Redisson.create(config);
    }
}
