package com.bear.reseeding.test.UDPTest;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSender {
    public static void main(String[] args) {
        try {
            // 创建一个UDP socket
            DatagramSocket socket = new DatagramSocket();

            // 准备要发送的数据
            String message = "handle";
            byte[] data = message.getBytes();

            // 指定目标地址和端口
            InetAddress address = InetAddress.getByName("目标IP地址");
            int port = 111;//目标端口号;

            // 创建一个DatagramPacket对象，将数据和目标地址、端口封装起来
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);

            // 发送数据
            socket.send(packet);

            System.out.println("数据发送成功");

            // 关闭socket
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}