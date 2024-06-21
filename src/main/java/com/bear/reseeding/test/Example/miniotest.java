package com.bear.reseeding.test.Example;

import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.MinioException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class miniotest {
    public static void main(String[] args) throws FileNotFoundException {
        String path ="C://efuav/CloudSystem/temp\\kmz\\template1.kmz";

        MinioClient  minioClient = MinioClient.builder()
                .endpoint("http://192.168.10.5:9090")
                .credentials("OdAU0FJKFSBrDpH4YLo4", "EQb3fE8xa45NqlSq3Z5ypzHBZ3PvZq7qGeeOK0ph")
                .build();
        File file = new File(path);
        FileInputStream  inputStream = new FileInputStream(file);

        try {
            minioClient.statObject(StatObjectArgs.builder().bucket("efuav").object("kmz/" + file.getName()).build());
            throw new RuntimeException("文件名已存在。");
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            System.out.println("文件不存在，请开始上传。");
            try {
//                ObjectWriteResponse response = minioClient.putObject(
//                        PutObjectArgs.builder().bucket("efuav").object("kmz/" + file.getName()).stream(inputStream, inputStream.available(), 0).build());
                minioClient.putObject(PutObjectArgs.builder().bucket("efuav").object("kmz/" + file.getName()).stream(inputStream, inputStream.available(), -1).contentType("kmz").build());
                System.out.println("上传成功。");
            } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException ex) {
                System.out.println("上传失败。");
                ex.printStackTrace();
            }
        }
    }
}
