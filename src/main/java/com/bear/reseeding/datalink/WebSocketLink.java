package com.bear.reseeding.datalink;

import com.alibaba.fastjson.JSONObject;
import com.bear.reseeding.entity.EfUser;
import com.bear.reseeding.utils.LogUtil;
import com.bear.reseeding.utils.RedisUtils;
import com.bear.reseeding.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

/**
 * WebSocket服务控制器
 */
//该注解用来指定一个URI，客户端可以通过这个URI来连接到WebSocket。
// 类似Servlet的注解mapping。无需在web.xml中配置。
@ServerEndpoint(value = "/websocket/{token}")
@Component
public class WebSocketLink {

    @Autowired
    private RedisUtils redisUtils;
    public static WebSocketLink webSocketLink;

    @PostConstruct
    public void init() {
        webSocketLink = this;
    }

    public static ConcurrentMap<String, ArrayList<Session>> webSocketMapSession = new ConcurrentHashMap<>();

    private static Queue<String> messageQueue = new ConcurrentLinkedQueue<>();
    // 在类中定义一个 Map 用于存储用户消息队列
    private static Map<String, Queue<String>> userMessageQueueMap = new ConcurrentHashMap<>();


    private final static Object objectLock = new Object();

    private static final Map<String, String> messageMap = new HashMap<>(); // 创建一个 HashMap 实例

    public static void putMessageMap(String userid,String sendMessage ){
        messageMap.put(userid,sendMessage);
    }
    public static void selectMessageMap(String userid,String sendMessage ){
        messageMap.remove(userid);
    }

    //推送数据给前台 java 用户 用户名 里面要放 用户id 和 msg
    public static void push(Object obj, String... userids) {
        try {
            if (userids == null || userids.length == 0) {
                return;
            }
            String message = JSONObject.toJSONString(obj);
//            LogUtil.logMessage(message);
            for (String userid : userids) {
                if (webSocketMapSession.containsKey(userid)) {
                    ArrayList<Session> sessionList = webSocketMapSession.get(userid);
                    if (null != sessionList && sessionList.size() > 0) {
                        synchronized (objectLock) {
                            for (Session session : sessionList) {
                                if (session != null) {
                                    if (session.isOpen()) {
                                        session.getBasicRemote().sendText(message);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // LogUtil.logInfo("推送数据给用户[UserId:" + Arrays.toString(userids) + "]完成，数据：" + obj);
        } catch (Exception e) {
            LogUtil.logError("广播数据给客户端[UserId:" + Arrays.toString(userids) + "]异常：" + e.toString());
        }
    }


    public static  void putMessageQueue(){

    }
    //推送数据给前台 java 用户 用户名 里面要放 用户id 和 msg
    public static void push(Object obj, ArrayList<String> userids) {
        try {
            if (userids == null || userids.size() == 0) {
                return;
            }
            String message = JSONObject.toJSONString(obj);

            for (String userid : userids) {
                if (webSocketMapSession.containsKey(userid)) {
                    ArrayList<Session> sessionList = webSocketMapSession.get(userid);
                    if (sessionList != null && !sessionList.isEmpty()) {
                        synchronized (objectLock) {
                            for (Session session : sessionList) {
                                if (session != null && session.isOpen()) {
                                    session.getAsyncRemote().sendText(message, new SendHandler() {
                                        @Override
                                        public void onResult(SendResult result) {
                                            if (result.isOK()) {
                                                // 消息发送成功
                                                System.out.println("消息发送成功给用户[" + userid + "]");
                                            } else {
                                                // 消息发送失败
                                                System.out.println("消息发送失败给用户[" + userid + "]. 原因: " + result.getException());
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                } else {
                    // 存储推送消息到缓存队列 -- 对应用户下
                    if (userMessageQueueMap.containsKey(userid)) {
                        Queue<String> queue = userMessageQueueMap.get(userid);
                        userMessageQueueMap.get(userid).offer(message);
                    } else {
                        Queue<String> newQueue = new ConcurrentLinkedQueue<>();
                        newQueue.offer(message);
                        userMessageQueueMap.put(userid, newQueue);
                    }
                    System.out.println("用户[" + userid + "]不存在或会话已关闭");
                }
            }
            // LogUtil.logInfo("推送数据给用户[UserId:" + Arrays.toString(userids) + "]完成，数据：" + obj);
        } catch (Exception e) {
            LogUtil.logError("广播数据给客户端异常：" + e.toString());
        }
    }

    private void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
//        session.setMaxIdleTimeout(3600000);
//        LogUtil.logMessage("websocket  token:" + token);
        if (!TokenUtil.verify(token)) {
            LogUtil.logError("尝试建立Webcoket连接失败，应用标识验证失败!");
            try {
                session.close();
            } catch (Exception e) {
            }
            return;
        }
        try {
            System.out.println("连接成功");
            EfUser user = TokenUtil.getUser(token);
            String userid = "-1";
            String username = "unknown";
            if (user != null) {
                if (user.getId() != null) {
                    userid = user.getId().toString();
                }
                username = user.getUName();
            }

            synchronized (objectLock) {
                ArrayList<Session> sessionList = new ArrayList<>();
                if (webSocketMapSession.containsKey(userid)) {
                    sessionList = webSocketMapSession.get(userid);
                }
                sessionList.add(session);
                webSocketMapSession.put(userid, sessionList);

                //现在是重新连接-- 将数据推送到前端方法  将消息队列中对应用户的消息发送给前端
                if (userMessageQueueMap.containsKey(userid)) {
                    Queue<String> queue = userMessageQueueMap.get(userid);
                    while (!queue.isEmpty()){
                        String message = queue.poll();
                        sendMessage(session,message); // 发送消息给对应的userid
                    }
                }


                LogUtil.logInfo("WebSocket服务：" + getSessionTitle(session.getId(), userid, username) + "新连接加入！");
                LogUtil.logInfo("WebSocket服务：当前客户总数：" + webSocketMapSession.size() + "，用户[" + username + "]客户端数量：" + sessionList.size());
            }
        } catch (Exception e) {

        }
    }


    /**
     * 发生错误时调用
     *
     * @param session
     * @param e
     */
    @OnError
    public void onError(Session session, Throwable e) {
        System.out.println("发生错误");
        //Throwable cause = e.getCause();
        /* normal handling... */
       /* if (cause != null) {
            LogUtil.logError("WebSocket服务发生错误,Error-info: cause->" + cause);
        }*/
        LogUtil.logWarn("WebSocket Connection Closed," + "SessionID:" + session.getId() + ",Throwable: " + e.toString());
        try {
            // Likely EOF (i.e. user killed session)
            // so just Close the input stream as instructed
            session.close();
        } catch (IOException ex) {
            //LogUtil.logError("WebSocket服务发生错误,Handling eof, A cascading IOException was caught: " + ex.toString());
        }
//        finally {
//            //LogUtil.logError("WebSocket服务发生错误,Session error handled. (likely unexpected EOF) resulting in closing User Session.");
//        }
//        removeClient(session.getId());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session, @PathParam("token") String token) {
        try {
            System.out.println("连接关闭调用的方法");
            if (!TokenUtil.verify(token)) {
                LogUtil.logError("尝试关闭Webcoket连接，应用标识验证失败，已关闭连接!");
                try {
                    session.close();
                } catch (Exception e) {
                }
                return;
            }
            EfUser user = TokenUtil.getUser(token);
            String userid = "-1";
            String username = "unknown";
            if (user != null) {
                if (user.getId() != null) {
                    userid = user.getId().toString();
                }
                username = user.getUName();
            }
            removeClient(session.getId(), userid, username);
        } catch (Exception e) {
            LogUtil.logError("关闭Webcokset连接：" + e.toString());
        }

    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public static void onMessage(String message, Session session, @PathParam("token") String token) {
        System.out.println("接收Webcoket消息"+message);
        if (!TokenUtil.verify(token)) {
            LogUtil.logError("接收Webcoket消息失败，应用标识验证失败!");
            return;
        }
        EfUser user = TokenUtil.getUser(token);
        String userid = "-1";
        String username = "unknown";
        if (user != null) {
            if (user.getId() != null) {
                userid = user.getId().toString();
            }
            username = user.getUName();
        }
        //对消息进行处理，主要针对摇杆操作数据
        JSONObject param = null;
        try {
            param = JSONObject.parseObject(message);
        } catch (Exception e) {
            LogUtil.logInfo("接收客户端:" + getSessionTitle(session.getId(), userid, username) + " 消息：" + message);
        }
        try {
            if (param != null) {
                int msgid = Integer.parseInt(param.getOrDefault("MessageID", "0").toString());
                HandleWebsocketDatas(msgid, param);
            }
        } catch (Exception e) {
            LogUtil.logError("处理客户端:" + getSessionTitle(session.getId(), userid, username) + " 消息异常：" + message);
        }
    }

    static String getSessionTitle(String sid, String userid, String username) {
        return "[" + userid + "-" + username + "-" + sid + "]";
    }

    void removeClient(String sessionId, String userid, String username) {
        try {
            String usernameid = "nan";
            ArrayList<Session> sessionList;
            synchronized (objectLock) {
                if (webSocketMapSession.containsKey(userid)) {
                    sessionList = webSocketMapSession.get(userid);
                    for (int i = sessionList.size() - 1; i >= 0; i--) {
                        if (sessionList.get(i).getId().equals(sessionId)) {
                            usernameid = sessionList.get(i).getId() + " ";
                            try {
                                if (sessionList.get(i).isOpen()) {
                                    sessionList.get(i).close(); //会触发@OnClose
                                }
                            } catch (IOException ignored) {
                            }
                            sessionList.remove(i);
                        } else if (!sessionList.get(i).isOpen()) {
                            usernameid = sessionList.get(i).getId() + " ";
                            sessionList.remove(i);
                        }
                    }
                    if (sessionList.size() > 0) {
                        webSocketMapSession.put(userid, sessionList);
                    } else {
                        webSocketMapSession.remove(userid);
                    }
                }
            }
            if (webSocketMapSession.containsKey(userid)) {
                LogUtil.logMessage("WebSocket服务：" + getSessionTitle(sessionId, userid, username) + "连接退出！");
                LogUtil.logMessage("WebSocket服务：当前客户总数：" + webSocketMapSession.size() + "，用户[" + username + "]客户端数量：" + webSocketMapSession.get(userid).size());
            } else {
                LogUtil.logMessage("WebSocket服务：" + getSessionTitle(sessionId, userid, usernameid) + "连接退出！");
                LogUtil.logMessage("WebSocket服务：当前客户总数：" + webSocketMapSession.size() + "，用户[" + username + "]客户端数量： 0");
            }
        } catch (Exception ignored) {
        }
    }

    //处理websocket接收的vue客户端信息，该方法不需返回值
    private static void HandleWebsocketDatas(int msgid, JSONObject param) {
        if (msgid != 0) {
            int tag = 0;
            int cmd = 0;
            String loginName = "";
            String UavID = "";
        }
    }

}
