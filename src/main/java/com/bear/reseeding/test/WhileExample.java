package com.bear.reseeding.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class WhileExample {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(3); // 创建线程池
        CountDownLatch latch = new CountDownLatch(2); // 创建CountDownLatch来跟踪线程完成
        AtomicInteger taskCount = new AtomicInteger(0);
        int count = 0; // 计数器
        while (count < 3) {

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("子线程" + Thread.currentThread().getName() + "开始执行");
                        Thread.sleep(10000);
                        System.out.println("子线程" + Thread.currentThread().getName() + "执行完成");
//                        latch.countDown(); // 当前线程调用此方法，则计数减一
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        taskCount.decrementAndGet();
                    }
                }
            };
            service.submit(runnable);
            taskCount.incrementAndGet();
            count++; // 增加计数器以继续循环
        }

        System.out.println("主线程"+Thread.currentThread().getName()+"等待子线程执行完成...");
//            latch.await(); // 等待所有线程执行完成
        while (taskCount.get() > 0) {
//            System.out.println(taskCount+""+taskCount.get());
            Thread.sleep(100);
        }
        System.out.println("所有子线程执行完成,重新开始");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("子线程" + Thread.currentThread().getName() + "开始执行");
                    Thread.sleep((long) (Math.random() * 10000));
                    System.out.println("子线程" + Thread.currentThread().getName() + "执行完成");
//                        latch.countDown(); // 当前线程调用此方法，则计数减一
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    taskCount.decrementAndGet();
                }
            }
        };
        service.submit(runnable);
        taskCount.incrementAndGet();
        while (taskCount.get() > 0) {
//            System.out.println(taskCount+""+taskCount.get());
            Thread.sleep(100);
        }



        service.shutdown(); // 关闭线程池
    }
}