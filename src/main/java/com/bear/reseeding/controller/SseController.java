package com.bear.reseeding.controller;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.bear.reseeding.datalink.SseClient;
import com.bear.reseeding.eflink.EFLINK_MSG_19015;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.websocket.server.ServerEndpoint;

@RestController
@RequestMapping("sse")
//@ServerEndpoint(value = "/sse/{token}")
//@Component
public class SseController {
    @Autowired
    private SseClient sseClient;


    @GetMapping("/")
    public String index(ModelMap model) {
        String uid = IdUtil.fastUUID();
        model.put("uid",uid);
        return "index";
    }

    /**
     * 创建SSE
     * @param uid
     * @return
     */
    @CrossOrigin
    @GetMapping("/createSse")
    public SseEmitter createConnect(String uid) {
        return sseClient.createSse(uid);

    }


    @CrossOrigin
    @GetMapping("/sendMsg")
    @ResponseBody
    public String sseChat(String uid) {
        for (int i = 0; i < 10; i++) {
//            IdUtil.fastUUID()
            sseClient.sendMessage(uid, "no"+i,"no"+i);
        }
        return "ok";
    }


    /**
     * 关闭连接
     */
    @CrossOrigin
    @GetMapping("/closeSse")
    public void closeConnect(String uid ){
        sseClient.closeSse(uid);
    }


    @CrossOrigin
    @GetMapping("/updateid")
    @ResponseBody
    public String updateCavity(int id) {
       boolean flag=  sseClient.updateCavity(id);
//       if(flag){
//           sseClient.sendMessage("likai", String.valueOf(id),String.valueOf(id));
//       }else {
//       }
        EFLINK_MSG_19015 eflink_msg_19015 = new EFLINK_MSG_19015();
        if(flag){
            String message = JSONObject.toJSONString(eflink_msg_19015);
            sseClient.groupSendMessage(message);
        }
        return "ok";
    }


}
