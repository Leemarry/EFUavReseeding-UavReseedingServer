package com.bear.reseeding.test;

import java.util.HashMap;
import java.util.Map;

public class ProcessingDataStorage {
    private Map<Integer, ProcessingInfo> userProcessingMap = new HashMap<>();
    private Map<Integer, ProcessingInfo> serverProcessingMap = new HashMap<>();
    private Map<Integer, Integer> processingIdToUserIdMap = new HashMap<>();

    // 定义处理信息类
    static class ProcessingInfo {
        private int processingId;
        private String message;

        public ProcessingInfo(int processingId, String message) {
            this.processingId = processingId;
            this.message = message;
        }

        public int getProcessingId() {
            return processingId;
        }

        public String getMessage() {
            return message;
        }
    }

    // 更新用户请求的处理信息
    public void updateUserRequest(int userId, int processingId) {
        userProcessingMap.put(userId, new ProcessingInfo(processingId, null));
        processingIdToUserIdMap.put(processingId, userId);
    }

//    // 接收另一个服务器处理后的信息
//    public void updateServerResponse(int processingId, String message) {
//        Integer userId = processingIdToUserIdMap.get(processingId);
//        if (userId != null) {
//            ProcessingInfo info = userProcessingMap.get(userId);
//            if (info != null) {
//                info.message = message;
//            }
//        }
//    }
    // 接收另一个服务器处理后的信息
    public void updateServerResponse(int processingId, String message) {
        for (Map.Entry<Integer, ProcessingInfo> entry : userProcessingMap.entrySet()) {
            if (entry.getValue().getProcessingId() == processingId) {
                entry.getValue().message = message;
            }
        }
    }

    // 获取用户最新处理信息
    public ProcessingInfo getUserProcessingInfo(int userId) {
        return userProcessingMap.get(userId);
    }

    // 获取服务器处理信息
    public ProcessingInfo getServerProcessingInfo(int processingId) {
        return serverProcessingMap.get(processingId);
    }

    public static void main(String[] args) {
        ProcessingDataStorage storage = new ProcessingDataStorage();

        // 模拟用户请求处理信息
        int userId = 123;
        int userProcessingId = 456;
        storage.updateUserRequest(userId, userProcessingId);

        // 模拟另一个服务器处理信息并返回
        int serverProcessingId = 456; // 假设与用户请求的处理id匹配
        storage.updateServerResponse(serverProcessingId, "Server processing message");

        // 获取用户最新的处理信息
        ProcessingInfo userProcessingInfo = storage.getUserProcessingInfo(userId);
        System.out.println("User ID: " + userId);
        System.out.println("Processing ID: " + userProcessingInfo.getProcessingId());
        System.out.println("Processing message: " + userProcessingInfo.getMessage());
    }
}