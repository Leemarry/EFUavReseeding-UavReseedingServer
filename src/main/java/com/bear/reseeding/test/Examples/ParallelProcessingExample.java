package com.bear.reseeding.test.Examples;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelProcessingExample {

    public static void main(String[] args) {
        // 假设你有一个大的坐标点集合 coordinateArray
        List<double[]> coordinateArray = new ArrayList<>();
        // 填充坐标点集合，这里假设坐标点集合已经初始化好了

        // 创建一个固定大小的线程池，根据实际情况调整线程数量
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // 每个子任务处理一部分坐标点
        int batchSize = 1000; // 每个子任务处理的坐标点数量
        for (int i = 0; i < coordinateArray.size(); i += batchSize) {
            final int startIndex = i;
            final int endIndex = Math.min(i + batchSize, coordinateArray.size());

            // 提交子任务到线程池
            executor.submit(() -> processPoints(coordinateArray.subList(startIndex, endIndex)));
        }

        // 关闭线程池
        executor.shutdown();

        try {
            // 等待所有任务完成
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // 处理中断异常
            e.printStackTrace();
        }

        System.out.println("All tasks completed.");
    }

    // 处理坐标点的方法
    public static void processPoints(List<double[]> points) {
        // 这里是处理坐标点的逻辑
        // 可以根据具体需求进行实现
    }
}