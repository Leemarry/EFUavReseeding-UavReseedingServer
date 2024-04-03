package com.bear.reseeding.test.UDPTest;

import java.io.*;
import java.net.*;

/**
 * 发送端
 *
 * @author 86155
 *
 */
public class Demo12_talk发送02 implements Runnable{

    private DatagramSocket client;
    private BufferedReader reader;
    private int port;
    private int toport;
    private String toIP;

    public Demo12_talk发送02(int port,int toport,String toIP) {
        this.port = port;
        this.toport = toport;
        this.toIP = toIP;

        try {
            client = new DatagramSocket(port);
            reader = new BufferedReader(new InputStreamReader(System.in));

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while( true ) {
            String msg ;
            try {
                msg = reader.readLine();
                String kmzUrl = "http://127.0.0.1:9090/ceshi/test.zip";
                byte[] kmzData = downloadFile(kmzUrl);
//                byte[] dates = msg.getBytes();
                //		 3.封装成 DatagramPacket 包裹  指定目的地 即Ip地址和端口
//                DatagramPacket packet = new DatagramPacket(dates, 0,dates.length,
//                        new InetSocketAddress(this.toIP,this.toport));

                byte[] sendData = msg.getBytes();
                DatagramPacket packet = new DatagramPacket(kmzData, kmzData.length,
                        InetAddress.getByName(this.toIP), this.toport);

                //		 4.发送包裹 send(DatagramPacket p)
                client.send(packet);

                if(msg.equals("bye")) {
                    break;
                }
            }catch( IOException e  ) {
                e.printStackTrace();
            }

        }
        client.close();
    }

    private static byte[] downloadFile(String fileUrl) throws IOException {
        URL url = new URL(fileUrl);
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream(); //
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytesRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }


}