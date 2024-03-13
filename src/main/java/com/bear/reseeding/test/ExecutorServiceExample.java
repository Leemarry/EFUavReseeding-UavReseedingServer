package com.bear.reseeding.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceExample {
    public static void main(String[] args) {
        // 创建一个固定大小的线程池，最多同时执行2个任务
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // 提交任务给线程池
        executor.submit(new Task("Task 1"));
        executor.submit(new Task("Task 2"));
        executor.submit(new Task("Task 3"));
        executor.submit(new Task("Task 4"));
        executor.submit(new Task("Task 5"));

        // 关闭线程池
        executor.shutdown();
    }

    static class Task implements Runnable {
        private final String name;

        public Task(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println("Executing " + name + " on thread " + Thread.currentThread().getName());
        }
    }
}