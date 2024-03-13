package com.bear.reseeding.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UdpSendReceiver {
    public static class TalkReceiver implements Callable<byte[]> {
        DatagramSocket server;
        private int timeout;

        public TalkReceiver(int port) {
            this(port, 0);
        }

        public TalkReceiver(int port, int timeout) {
            try {
                this.server = new DatagramSocket(port);
                this.timeout = timeout;
                if (timeout > 0) {
                    this.server.setSoTimeout(timeout);
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        @Override
        public byte[] call() throws Exception {
             //TODO Auto-generated method stub
            byte[] result = new byte[0]; // 初始化一个空的 byte
            while (true) {
                byte[] container = new byte[1024 * 60];
                DatagramPacket packet = new DatagramPacket(container, container.length);

                try {
                    server.receive(packet);
                    // 检查是否收到有效数据
                    if (packet.getLength() > 0) {
                        // 将收到的数据复制到 result 数组中
                        result = Arrays.copyOf(packet.getData(), packet.getLength());
                        // 如果需要在接收到数据后立即退出循环，则取消注释以下一行
                         break;
                    }

                    byte[] data = packet.getData();
                    int len = packet.getLength();
                    String msg = new String(data, 0, len);

                    System.out.println("msg"+msg);


                    if (msg.equals("bye")) {
                        break;
                    }

                    // 更新或拼接需要返回的数据
                    result = Arrays.copyOf(result, result.length + len);
                    System.arraycopy(data, 0, result, result.length - len, len);
                } catch (SocketTimeoutException e){
                    System.out.println("Receive timeout. No data received within " + timeout + " ms.");
                    break;
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
            server.close();

            return result; // 返回接收到的所有数据
        }

//        @Override
//        public void call() {
//            // TODO Auto-generated method stub
//            while (true) {
//                byte[] container = new byte[1024 * 60];
//                DatagramPacket packet = new DatagramPacket(container, container.length);
//
//                try {
//                    server.receive(packet);
//
//                    byte[] data = packet.getData();
//                    int len = packet.getLength();
//                    String msg = new String(data, 0, len);
//
//                    System.out.println(msg);
//
//                    if (msg.equals("bye")) {
//                        break;
//                    }
//
//                } catch (SocketTimeoutException e){
//                    System.out.println("Receive timeout. No data received within " + timeout + " ms.");
//                    break;
//                }catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            server.close();
//        }


    }

    public static class TalkSender implements Runnable {
        private DatagramSocket client;
        private BufferedReader reader;
        private int port;
        private int toport;
        private String toIP;

        public TalkSender(int port, int toport, String toIP) {
            this.port = port;
            this.toport = toport;
            this.toIP = toIP;

            try {
                client = new DatagramSocket(port);
                reader = new BufferedReader(new InputStreamReader(System.in));
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String msg = reader.readLine();
                    byte[] sendData = msg.getBytes();
                    DatagramPacket packet = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(this.toIP), this.toport);

                    client.send(packet);

                    if (msg.equals("bye")) {
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                }
            }
            client.close();
        }
    }


    public static JSONObject parseKMZToJSON(byte[] kmzData) throws IOException {
        JSONObject json = new JSONObject();
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(kmzData);
             ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream)) {
            ZipEntry entry; // entry: "lerver.jpg"
            while ((entry = zipInputStream.getNextEntry()) != null) {

                if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".xml")) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(zipInputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    json.put("kml_content", stringBuilder.toString());
                    break;
                }
            }
        }
        return json;
    }

}
