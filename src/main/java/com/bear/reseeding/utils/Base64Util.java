package com.bear.reseeding.utils;

import javax.imageio.stream.FileImageInputStream;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

import java.nio.charset.StandardCharsets;

/**
 * @Auther: bear
 * @Date: 2021/7/16 09:58
 * @Description: null
 */

public class Base64Util {
    /**
     * 对给定的字符串进行base64解码操作
     */
    public static String decodeData(String inputData) {
        try {
            if (inputData == null) {
                return "";
            }
            byte[] asBytes = Base64.getDecoder().decode(inputData);
            return new String(asBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return inputData;
        }
    }

    /**
     * 对给定的字符串进行base64加密操作
     */
    public static String encodeData(String inputData) {
        try {
            String asB64 = Base64.getEncoder().encodeToString(inputData.getBytes("utf-8"));
            return asB64;
        } catch (UnsupportedEncodingException e) {
            return inputData;
        }
    }

    /**
     * Base64字符串 转换为 byte数组
     */
    public static byte[] decode(String data) {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] buffer = decoder.decode(data);
            return buffer;
        } catch (Exception e) {
            return new byte[0];
        }
    }

    /**
     * 十进制int转16进制字符串
     *
     * @param
     * @return
     */
    public static String IntToHexString(int num) {

        String hexString = Integer.toHexString(num);

        return hexString;
    }

    // 图片转化成base64字符串
    public static String GetImageStr(String imgFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        String ss = null;
        imgFile = imgFile.replace("\\", File.separator).replace("/", File.separator);
        if (imgFile.split("\\.").length > 1) {
            String type = imgFile.split("\\.")[1];
            InputStream in = null;
            byte[] data = null;
            // 读取图片字节数组
            try {
                in = new FileInputStream(imgFile);
                data = new byte[in.available()];
                in.read(data);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
           ss = Base64.getEncoder().encodeToString(data);
        }
        return ss;
        // 对字节数组Base64编码
//        BASE64Encoder encoder = new BASE64Encoder();
//        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }

    public static byte[] image2byte(String path){
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        }
        catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        }
        catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }


    /**
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     *
     * @param data 文件流
     * @return Base64编码图片
     */
    public static String getImageBase64Str(byte[] data) {
        String base64String = null;
        try {
            base64String = Base64.getEncoder().encodeToString(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64String;
        // 对字节数组Base64编码
//        BASE64Encoder encoder = new BASE64Encoder();
//        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }

      /**
     * 将base64编码的图片解码为文件流
     *
     * @param base64 编码的字符串
     * @return 文件流
     */
    public static byte[] base64ImgToStream(String base64) {
        base64 = base64.replace("data:image/gif;base64,", "");
        base64 = base64.replace("data:image/jpeg;base64,", "");
        base64 = base64.replace("data:image/jpg;base64,", "");
        base64 = base64.replace("data:image/png;base64,", "");
        return Base64.getDecoder().decode(base64);
    }

    public static byte[] getImageBytes(String imageUrl) throws Exception {
        URL url = new URL(imageUrl);
        URLConnection conn = url.openConnection();
        InputStream in = conn.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        in.close();
        out.close();
        return out.toByteArray();
    }
}

