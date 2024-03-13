package com.bear.reseeding.test.UDPTest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpSenderReceiver {
    public static void main(String[] args) {
        String ipAddress = "127.0.0.1"; // 替换为你要发送到的 IP 地址
        int sendPort = 9997; // 替换为你要发送到的端口号
        int receivePort = 54321; // 替换为你要监听的端口号
        String message = "handle"; // 替换为你要发送的消息内容

        try {
            // 发送消息
            DatagramSocket sendSocket = new DatagramSocket();
            byte[] sendData = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                    InetAddress.getByName(ipAddress), sendPort);

            sendSocket.send(sendPacket);
            sendSocket.close();

            // 接收消息
            DatagramSocket receiveSocket = new DatagramSocket(receivePort);
            receiveSocket.setSoTimeout(10000); // 设置超时时间为10秒

            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            try {
                receiveSocket.receive(receivePacket);

                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                // 在这里处理收到的消息
                // 对于可能的压缩包，可以先解压缩再进行处理

                System.out.println("Received message: " + receivedMessage);
            } catch (IOException e) {
                System.out.println("No data received within the timeout period.");
            } finally {
                receiveSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
