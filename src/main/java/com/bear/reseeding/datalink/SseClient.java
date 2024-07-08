package com.bear.reseeding.datalink;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.bear.reseeding.service.EfHandleWaypointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SseClient {
    private static final Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    private  static List<Object> objectList = new ArrayList<>();
    private  static HashMap<Integer,Map<String, Object> > objectMap =  new HashMap<Integer, Map<String, Object>>();
    private  static int  index;


    public void   push (Object obj){
        objectList.add(obj);
    }

    /**
     *  设置对象
     * @param wpindex
     * @param obj
     */
    public static void settObjectMap(Integer wpindex,Map<String, Object> obj) {
     objectMap.put(wpindex,obj);
    }

    /**
     * 创建连接
     */
    public SseEmitter createSse(String uid) {
        //默认30秒超时,设置为0L则永不超时
        SseEmitter sseEmitter = new SseEmitter(0l);
        //完成后回调
        sseEmitter.onCompletion(() -> {
            log.info("[{}]结束连接...................", uid);
            sseEmitterMap.remove(uid);
        });
        //超时回调
        sseEmitter.onTimeout(() -> {
            log.info("[{}]连接超时...................", uid);
        });
        //异常回调
        sseEmitter.onError(
                throwable -> {
                    try {
                        log.info("[{}]连接异常,{}", uid, throwable.toString());
                        sseEmitter.send(SseEmitter.event()
                                .id(uid)
                                .name("发生异常！")
                                .data("发生异常请重试！")
                                .reconnectTime(3000));
                        sseEmitterMap.put(uid, sseEmitter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        try {
            sseEmitter.send(SseEmitter.event().reconnectTime(5000));
        } catch (IOException e) {
            e.printStackTrace();
        }
        sseEmitterMap.put(uid, sseEmitter);
        log.info("[{}]创建sse连接成功！", uid);
        return sseEmitter;
    }

    /**
     * 给指定用户发送消息
     *
     */
    public boolean sendMessage(String uid,String messageId, String message) {
        if (StrUtil.isBlank(message)) {
            log.info("参数异常，msg为null", uid);
            return false;
        }
        SseEmitter sseEmitter = sseEmitterMap.get(uid);
        if (sseEmitter == null) {
            log.info("消息推送失败uid:[{}],没有创建连接，请重试。", uid);
            return false;
        }
        try {
//            sseEmitter.send(message, MediaType.APPLICATION_JSON);
            sseEmitter.send(SseEmitter.event().id(messageId).reconnectTime(1*60*1000L).data(message));
            log.info("用户{},消息id:{},推送成功:{}", uid,messageId, message);
            return true;
        }catch (Exception e) {
            sseEmitterMap.remove(uid);
            log.info("用户{},消息id:{},推送异常:{}", uid,messageId, e.getMessage());
            sseEmitter.complete();
            return false;
        }
    }

    public  void groupSendMessage(String message){
        if (StrUtil.isBlank(message)) {
            log.info("参数异常，msg为null");
        }
        for (Map.Entry<String,SseEmitter> sseEmitterEntry : sseEmitterMap.entrySet()) {
           SseEmitter sseEmitter=  sseEmitterEntry.getValue();
           String  uid = sseEmitterEntry.getKey();
            if (sseEmitter == null) {
                log.info("消息推送失败uid:[{}],没有创建连接，请重试。", uid);
            }
            try {
                 // sseEmitter.send(message, MediaType.APPLICATION_JSON);
                sseEmitter.send(SseEmitter.event().id(uid).reconnectTime(1*60*1000L).data(message));
                log.info("用户{},消息id:{},推送成功:{}", uid,uid, message);
            }catch (Exception e) {
                sseEmitterMap.remove(uid);
                log.info("用户{},消息id:{},推送异常:{}", uid,uid, e.getMessage());
                sseEmitter.complete();
            }
        }
    }

    /**
     * 断开
     * @param uid
     */
    public void closeSse(String uid){
        if (sseEmitterMap.containsKey(uid)) {
            SseEmitter sseEmitter = sseEmitterMap.get(uid);
            sseEmitter.complete();
            sseEmitterMap.remove(uid);
        }else {
            log.info("用户{} 连接已关闭",uid);
        }

    }


    @Resource
    private EfHandleWaypointService efHandleWaypointService;

    /**
     * 洞斑点巡检次数加一
     */
    public  boolean  updateCavity(Integer id){
       int num =   efHandleWaypointService.updateHandleNmu(id);
       boolean flag =false;
       if(num ==1){
           System.out.println("更新成功");
           flag = true;
       }
       return flag;

    }

    public  void   updateFCavity(Integer wpindex){
        for (int i = 0; i <= wpindex; i++) {
            Map<String, Object> obj=   objectMap.get(i);
            if(obj != null){
                int id = (int) obj.getOrDefault("id",null);
                int num =   efHandleWaypointService.updateHandleNmu(id);
                if(num ==1){
                    System.out.println("更新成功:"+id);
                    String message = JSONObject.toJSONString(obj);
                    groupSendMessage(message);
                }
                objectMap.remove(i);
            }
        }
        index  =wpindex;
    }


}