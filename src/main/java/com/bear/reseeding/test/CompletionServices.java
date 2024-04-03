package com.bear.reseeding.test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CompletionServices {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService service = Executors.newFixedThreadPool(3); // 创建线程池
        CompletionService<Void> completionService = new ExecutorCompletionService<>(service);
        AtomicInteger taskCount = new AtomicInteger(0);
        int count = 0; // 计数器
        while (count < 3) {

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("子线程" + Thread.currentThread().getName() + "开始执行");
                        Thread.sleep((long) (Math.random() * 10000));
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

        // 使用CompletionService等待所有任务完成
        for (int i = 0; i < 3; i++) {
            completionService.take().get(); // 等待任务完成并获取结果
        }

        System.out.println("所有子线程执行完成xxx");

        service.shutdown(); // 关闭线程池
        System.out.println("所有子线程执行完成xxx");
    }
}