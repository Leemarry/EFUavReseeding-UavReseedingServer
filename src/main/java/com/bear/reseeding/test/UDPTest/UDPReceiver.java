package com.bear.reseeding.test.UDPTest;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPReceiver {
    public static void main(String[] args) {
        try {
            // 创建一个UDP socket，绑定端口号
            DatagramSocket socket = new DatagramSocket(111); // 目标端口号

            // 创建一个字节数组用于接收数据
            byte[] buffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

            // 接收数据
            socket.receive(receivePacket);

            // 将接收到的数据转换成字符串
            String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("接收到数据: " + receivedMessage);

            // 关闭socket
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}