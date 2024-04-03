package com.bear.reseeding.test.UDPTest;

import java.util.HashMap;
import java.util.Map;

public class UserProcessingDataStorage {
    private Map<Integer, UserProcessingData> userProcessingMap = new HashMap<>();

    private static class UserProcessingData {
        private int processingId;
        private String message;

        public UserProcessingData(int processingId, String message) {
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

    public void updateProcessingData(int userId, int processingId, String message) {
        userProcessingMap.put(userId, new UserProcessingData(processingId, message));
    }

    public UserProcessingData getLatestProcessingData(int userId) {
        return userProcessingMap.get(userId);
    }

    public static void main(String[] args) {
        UserProcessingDataStorage storage = new UserProcessingDataStorage();

        // 模拟接收到用户ID和处理ID，存储数据
        int userId = 123;
        int processingId = 456;
        String message = null; //"Processing message for user 123";

        storage.updateProcessingData(userId, processingId, message);

        // 模拟接收到新的处理ID和处理消息，更新数据
        int newProcessingId = 789;
        String newMessage = "Updated message for user 123";

        storage.updateProcessingData(userId, newProcessingId, newMessage);

        // 获取用户最新的处理数据
        UserProcessingData latestData = storage.getLatestProcessingData(userId);
        System.out.println("Latest processing ID for user 123: " + latestData.getProcessingId());
        System.out.println("Latest processing message for user 123: " + latestData.getMessage());
    }
}
