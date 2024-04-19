package com.bear.reseeding.test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CompletionServices {
    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        ExecutorService service = Executors.newFixedThreadPool(3); // 创建线程池
        CompletionService<Void> completionService = new ExecutorCompletionService<>(service);
        AtomicInteger taskCount = new AtomicInteger(0);
        int count = 0; // 计数器
//        String filePath = "C:\\efuav\\reseeding\\temp\\kmz\\未命名0-40\\未命名0-40.kmz";
//        File zipFile = new File(filePath);
//
//        if (!zipFile.exists()) {
//            zipFile.createNewFile();
//        }
        // 获取当前时间
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 计算一周前的时间
        LocalDateTime oneWeekAgo = currentDateTime.minus(1, ChronoUnit.WEEKS);

        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 格式化当前时间为字符串
        String formattedCurrentDateTime = currentDateTime.format(formatter);

        // 格式化一周前的时间为字符串
        String formattedOneWeekAgo = oneWeekAgo.format(formatter);



        // 获取当前日期
//        LocalDate currentDate = LocalDate.now();
//
//        // 计算一周前的日期
//        LocalDate oneWeekAgo = currentDate.minus(1, ChronoUnit.WEEKS);
//        Date a= new Date();
//        // 打印结果
//        System.out.println("一周前的日期是：" + oneWeekAgo);
        while (count < 3) {

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("子线程" + Thread.currentThread().getName() + "开始执行");
                        Thread.sleep((long) (Math.random() * 6000));
                        System.out.println("子线程" + Thread.currentThread().getName() + "执行完成");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        taskCount.decrementAndGet();
                    }
                }
            };
            service.submit(runnable);
            taskCount.incrementAndGet();
            count++; // 增加计数器以继续循环
        }
        System.out.println("主线程" + Thread.currentThread().getName() + "等待子线程执行完成...");

//         使用CompletionService等待所有任务完成
//        for (int i = 0; i < 3; i++) {
//            completionService.take().get(); // 等待任务完成并获取结果
//        }
        while (taskCount.get() > 0) {
            Thread.sleep(100);
        }

        System.out.println("所有子线程执行完成xxx");

        service.shutdown(); // 关闭线程池
        System.out.println("所有子线程执行完成xxx");
    }
}