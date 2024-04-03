package com.bear.reseeding.test.UDPTest;


import org.json.JSONObject;
//import com.bear.reseeding.test.UDPTest.DemoTalk.TalkReceivers;
import com.bear.reseeding.test.UDPTest.DemoTalk.TalkSender;
import com.bear.reseeding.utils.UdpSendReceiver;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * 实现相互交流吧
 *
 * @author 86155
 */
public class Demo1_student {
    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
//        new Thread(new Demo11_talk接收端02(9998)).start();//接受
//
//        new Thread(new Demo12_talk发送02(5555, 9997, "localhost")).start();//发送

        //#region
//        // 创建接收端对象并启动线程
////        TalkReceiver receiver = new TalkReceiver(9998);
//        TalkReceiver receiver = new DemoTalk.TalkReceiver(9998,5555); // 设置超时时间为10秒
//        Thread receiverThread = new Thread(receiver);
//        receiverThread.start();
//
//        // 创建发送端对象并启动线程
//        TalkSender sender = new DemoTalk.TalkSender(5555, 9997, "localhost");
//        Thread senderThread = new Thread(sender);
//        senderThread.start();
        //#endregion
        //#region
        int numThread = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(numThread);
        // 定义接收端和发送端的任务
//        Runnable receiverTask = () -> {
//            TalkReceiver receiver = new TalkReceiver(9998,5555);
//            Thread receiverThread = new Thread(receiver);
//            receiverThread.start();
//        };
//        executorService.submit(receiverTask);

//        executorService.submit( new TalkReceiver(9998,5555));
        Future<byte []> future = executorService.submit( new UdpSendReceiver.TalkReceiver(9998,55505));
       byte[] packet=  future.get();
        System.out.println("输出"+ packet);
        JSONObject json =   UdpSendReceiver.parseKMZToJSON(packet);
        System.out.println(json.toString(2));
        // 5. 保存接收到的文件数据
//        FileOutputStream fos = new FileOutputStream("test_received.zip");
//        fos.write(packet, 0, packet.getLength());

        Runnable senderTask = () -> {
            TalkSender sender = new TalkSender(5555, 9997, "localhost");
            Thread senderThread = new Thread(sender);
            senderThread.start();
        };

        Callable c =() ->{

            return null;
        };


// 提交任务给线程池执行
//        executorService.submit(receiverTask);
        executorService.submit(senderTask);

// 关闭线程池
        executorService.shutdown();
        //#endregion


    }
}
