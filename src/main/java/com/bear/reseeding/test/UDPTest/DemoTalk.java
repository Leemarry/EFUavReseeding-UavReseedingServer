package com.bear.reseeding.test.UDPTest;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
//https://blog.csdn.net/FanShenDeXianYu/article/details/115537836
//https://blog.csdn.net/qq_48508278/article/details/119388809?spm=1001.2101.3001.6650.2&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-2-119388809-blog-115537836.235%5Ev43%5Epc_blog_bottom_relevance_base9&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-2-119388809-blog-115537836.235%5Ev43%5Epc_blog_bottom_relevance_base9&utm_relevant_index=5
public class DemoTalk {

    public static class TalkReceiver implements Runnable {
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
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                byte[] container = new byte[1024 * 60];
                DatagramPacket packet = new DatagramPacket(container, container.length);

                try {
                    server.receive(packet);

                    byte[] data = packet.getData();
                    int len = packet.getLength();
                    String msg = new String(data, 0, len);

                    System.out.println(msg);

                    if (msg.equals("bye")) {
                        break;
                    }

                } catch (SocketTimeoutException e){
                    System.out.println("Receive timeout. No data received within " + timeout + " ms.");
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
            server.close();
        }
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


}