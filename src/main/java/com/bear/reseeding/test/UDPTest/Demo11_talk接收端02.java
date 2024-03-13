package com.bear.reseeding.test.UDPTest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * 接收端
 *
 *
 * @author 86155
 *
 */
public class Demo11_talk接收端02 implements Runnable{

    DatagramSocket server;

    public Demo11_talk接收端02(int port) {
        try {
            server = new DatagramSocket(port);
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    @Override
    public void run() {
        // TODO Auto-generated method stub
        while(true) {
            byte [] container = new byte[1024*60];
            DatagramPacket packet = new DatagramPacket(container,container.length);
//			3.阻塞式接受包裹 receive​(DatagramPacket p)
            try {
                server.receive(packet);//会有延迟

                byte[] dates = packet.getData();
                int len = packet.getLength();//得用packet的实际大小
                String date = new String(dates,0,len);

                System.out.println(date);

                if( date.equals("bye")) {
                    break;
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //释放资源
        server.close();
    }

}