package com.bear.reseeding.utils;

import com.bear.reseeding.entity.EfRelationUavsn;
import com.bear.reseeding.service.EfRelationUavsnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis 支持五种数据类型：String(字符串)、Hash(哈希)、List(列表)，set(集合)、zset( sorted set:有序集合)
 *
 * @Auther: bear
 * @Date: 2021/12/27 10:51
 * @Description: null
 */
@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private EfRelationUavsnService efRelationUavsnService;

    private static final Long SUCCESS = 1L;

    // region 业务方法

    /**
     * 根据无人机SN码获取无人机ID
     *
     * @param uavSn 无人机Sn
     * @return 无人机ID
     */
    public String getUavIdByUavSn(String uavSn) {
        String uavId = null;
        try {
            boolean forceReset = false;
            long lastRefreshTime = 0;
            String keyTemp = "table_rel_uav_id_sn_timerefresh";
            if (hasKey(keyTemp)) {
                lastRefreshTime = ConvertUtil.convertToLong(get(keyTemp), 0);
            }
            if (lastRefreshTime + 24 * 60 * 1000 <= System.currentTimeMillis()) {
                forceReset = true;
                set(keyTemp, System.currentTimeMillis());
            }
            if (forceReset || !exists("table_rel_uav_sn_id") || !exists("table_rel_uav_id_sn")) {
                List<EfRelationUavsn> efRelationUavsns = efRelationUavsnService.queryAllByLimit(0, 100000);
                remove("table_rel_uav_sn_id", "table_rel_uav_id_sn");
                if (efRelationUavsns != null && efRelationUavsns.size() > 0) {
                    for (EfRelationUavsn item : efRelationUavsns) {
                        // 1 对 1 关系
                        hmSet("table_rel_uav_sn_id", item.getUavSn(), item.getUavId());
                        hmSet("table_rel_uav_id_sn", item.getUavId(), item.getUavSn());
                    }
                }
            }
            Object obj = hmGet("table_rel_uav_sn_id", uavSn); //根据无人机SN获取无人机ID
            if (obj != null) {
                uavId = obj.toString();
            }
        } catch (Exception e) {
            LogUtil.logError("从缓存根据无人机SN获取无人机ID异常：" + e.toString());
        }
        return uavId;
    }
    // endregion

    //region 通用方法

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 写入缓存设置时效时间
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime, TimeUnit timeUnit) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, timeUnit);
            result = true;
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }


    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object get(final String key) {
        try {
            Object result = null;
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            result = operations.get(key);
            return result;
        } catch (Exception e) {
            LogUtil.logError(e.toString());
            return null;
        }
    }

    /**
     * 哈希 添加
     *
     * @param key
     * @param hashKey
     * @param value
     */
    public void hmSet(String key, Object hashKey, Object value) {
        try {
            HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
            hash.put(key, hashKey, value);
        } catch (Exception e) {
            LogUtil.logError(e.toString());
        }
    }
    /**
     * 哈希 添加 设置有效时间
     *
     * @param key
     * @param hashKey
     * @param value
     * @param expireTime
     * @param timeUnit
     */
    public boolean hmSet(String key, Object hashKey, Object value, long expireTime, TimeUnit timeUnit) {
        boolean result = false;
        try {
            HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
            hash.put(key, hashKey, value);
            redisTemplate.expire(key, expireTime, timeUnit);
            result = true;
        } catch (Exception e) {
            LogUtil.logError(e.toString());
        }
        return result;
    }

    /**
     *
     * @param key
     * @param hashKey
     * @param value
     * @param expireTime
     * @param timeUnit
     * @return
     */
    public boolean isHashExists(String key, Object hashKey, Object value, long expireTime, TimeUnit timeUnit) {
        boolean result = false;
        try {
            HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
            if (redisTemplate.hasKey(key) && hash.hasKey(key, hashKey)) {
                redisTemplate.expire(key, expireTime, timeUnit);
                result = true;
            } else {
                hash.put(key, hashKey, value);
                redisTemplate.expire(key, expireTime, timeUnit);
                result = true;
            }
        } catch (Exception e) {
            LogUtil.logError(e.toString());
        }
        return result;
    }

    // 获取所有Hash数据
    public HashMap<Object, Object> GetAllHash(String key) {
        HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
        HashMap<Object, Object> obj= (HashMap<Object, Object>) hashOps.entries(key);
//        Cursor<Map.Entry<Object, Object>> cursor = hashOps.scan(key, ScanOptions.NONE);
//        Map<Object, Object> allUsers = new HashMap<>();
//        while (cursor.hasNext()) {
//            Map.Entry<Object, Object> entry = cursor.next();
//            Object keyObj = entry.getKey();
//            Object valueObj = entry.getValue();
//            allUsers.put(keyObj, valueObj);
//        }
        return obj;
    }

    // 判断哈希表是否存在并删除
    public void deleteHashIfExists(String hashKey) {
//        HashOperations<String, String, User> hashOps = redisTemplate.opsForHash();
//        if (redisTemplate.hasKey(hashKey)) {
//            redisTemplate.delete(hashKey);
//        }
    }
    /**
     * 哈希获取数据
     *
     * @param key
     * @param hashKey
     * @return
     */
    public Object hmGet(String key, Object hashKey) {
        try {
            HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
            Set<Object> keys = hash.keys(key);
            boolean hasKey = hash.hasKey(key, hashKey);
            return hash.get(key, hashKey);
        } catch (Exception e) {
            LogUtil.logError(e.toString());
            return null;
        }
    }

    /**
     * 列表添加
     *
     * @param k
     * @param v
     */
    public void lPush(String k, Object v) {
        try {
            ListOperations<String, Object> list = redisTemplate.opsForList();
            list.rightPush(k, v);
        } catch (Exception e) {
            LogUtil.logError(e.toString());
        }
    }

    /**
     * 列表获取
     *
     * @param k
     * @param l
     * @param l1
     * @return
     */
    public List<Object> lRange(String k, long l, long l1) {
        try {
            ListOperations<String, Object> list = redisTemplate.opsForList();
            return list.range(k, l, l1);
        } catch (Exception e) {
            LogUtil.logError(e.toString());
            return null;
        }
    }

    /**
     * 集合添加
     *
     * @param key
     * @param value
     */
    public void add(String key, Object value) {
        try {
            SetOperations<String, Object> set = redisTemplate.opsForSet();
            set.add(key, value);
        } catch (Exception e) {
            LogUtil.logError(e.toString());
        }
    }

    /**
     * 集合获取
     *
     * @param key
     * @return
     */
    public Set<Object> setMembers(String key) {
        try {
            SetOperations<String, Object> set = redisTemplate.opsForSet();
            return set.members(key);
        } catch (Exception e) {
            LogUtil.logError(e.toString());
            return null;
        }
    }

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     * @param scoure
     */
    public void zAdd(String key, Object value, double scoure) {
        try {
            ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
            zset.add(key, value, scoure);
        } catch (Exception e) {
            LogUtil.logError(e.toString());
        }
    }

    /**
     * 有序集合获取
     *
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    public Set<Object> rangeByScore(String key, double scoure, double scoure1) {
        try {
            ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
            return zset.rangeByScore(key, scoure, scoure1);
        } catch (Exception e) {
            LogUtil.logError(e.toString());
            return null;
        }
    }

    public Set<String> keys(String keys) {
        try {
            return redisTemplate.keys(keys);
        } catch (Exception e) {
            LogUtil.logError(e.toString());
            return null;
        }
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            LogUtil.logError(e.toString());
            return false;
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */

    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            LogUtil.logError(e.toString());
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */

    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }


    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */

    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * 获取锁
     *
     * @param lockKey
     * @param value
     * @param expireTime：单位-秒
     * @return
     */
    public boolean getLock(String lockKey, Object value, int expireTime) {
        try {
            LogUtil.logInfo("添加分布式锁key=" + lockKey + ",expireTime=" + expireTime);
            String script = "if redis.call('setNx',KEYS[1],ARGV[1]) then if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('expire',KEYS[1],ARGV[2]) else return 0 end end";
            RedisScript<String> redisScript = new DefaultRedisScript<>(script, String.class);
            Object result = redisTemplate.execute(redisScript, Collections.singletonList(lockKey), value, expireTime);
            if (SUCCESS.equals(result)) {
                return true;
            }
        } catch (Exception e) {
            LogUtil.logError(e.toString());
        }
        return false;
    }

    /**
     * 释放锁
     *
     * @param lockKey
     * @param value
     * @return
     */
    public boolean releaseLock(String lockKey, String value) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        RedisScript<String> redisScript = new DefaultRedisScript<>(script, String.class);
        Object result = redisTemplate.execute(redisScript, Collections.singletonList(lockKey), value);
        if (SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }
    //endregion
}
