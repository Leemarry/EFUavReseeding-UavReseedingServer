package com.bear.reseeding.task;

import com.bear.reseeding.utils.LogUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * Minio 储存桶操作类
 *
 * @Auther: bear
 * @Date: 2023/5/31 11:47
 * @Description: null
 */
@Service("minioServer")
public class MinioService {
    /**
     * 当前文件储存根目录
     */
    @Value("${BasePath:C://efuav/UavSystem/}")
    private String BasePath;
    /**
     * #照片储存 true存本地 false存minio 查看 删除照片也是
     */
    @Value("${spring.config.PhotoStorage:false}")
    private boolean PhotoStorage;

    @Value("${minio.AccessKey}")
    private String AccessKey;
    @Value("${minio.SecretKey}")
    private String SecretKey;
    @Value("${minio.Endpoint}")
    private String Endpoint;
    @Value("${minio.BucketName}")
    private String BucketName;
    @Value("${minio.BucketNameKmz}")
    private String BucketNameKmz;
    @Value("${minio.BucketNameWord}")
    private String BucketNameWord;
    @Value("${minio.EndpointExt}")
    private String EndpointExt;

    /**
     * 上传文件到Mino储存桶
     *
     * @param bucketName  桶类型 kmz/word/其它
     * @param path        储存路径含完整名称： photo/image/123.jpg
     * @param contentType 文件类型：image/jpg
     * @param stream      文件流
     * @return
     */
    public boolean uploadImage(String bucketName, String path, String contentType, InputStream stream) {
        boolean result = false;
        try {
            if (!PhotoStorage) {
                // 存云端
                try {
//                    MinioClient minioClient = new MinioClient(Endpoint, AccessKey, SecretKey);
//                    boolean isExist = minioClient.bucketExists(bucketName);
//                    if (!isExist) {
//                        minioClient.makeBucket(bucketName);
//                        LogUtil.logMessage("正在上传文件到Minio，创建储存桶 " + bucketName + "成功。");
//                    }
//                    minioClient.putObject(bucketName, path, stream, contentType.replace(".", ""));
                    result = true;
                    LogUtil.logInfo("上传文件 " + path + " 到Minio成功。");
                } catch (Exception e) {
                    LogUtil.logError("上传文件到MINIO失败：" + e.toString());
                }
            } else {
                LogUtil.logWarn("保存文件到本地暂不支持，暂时只支持保存到Minio储存桶！");
                // 存本地
                String pathParentBig = BasePath + path;
//                if (!FileUtil.saveFileAndThumbnail(file, pathParentBig, pathParentSmall, newFileName)) {
//                    return ResultUtil.error("上传图片失败");
//                }
            }
        } catch (Exception e) {
            LogUtil.logError("上传文件 " + path + " 到Minio异常：" + e.toString());
        }
        return result;
    }

    /**
     * 获取 Minio中文件的下载地址，对外的真正下载地址
     *
     * @param bucketName 储存桶名称
     * @param fileName   去除储存桶之外的完整的minio储存路径，如:  uavsystem/2/3.jpg
     * @return 下载地址
     */
    public String getObjectFullRealUrl(String bucketName, String fileName) {
        String url = "";
        try {
            url = getObjectFullUrl(bucketName, fileName);
            if (StringUtils.isNotBlank(url)) {
                int index = url.indexOf(bucketName);
                if (index > 0) {
                    url = url.substring(index);
                }
                url = EndpointExt + "/" + url;
            }
        } catch (Exception e) {
            LogUtil.logError("获取储存桶" + fileName + "完整加密地址失败：" + e.toString());
        }
        return url;
    }

    /**
     * 获取 Minio中文件的下载地址
     *
     * @param bucketName 储存桶名称
     * @param fileName   去除储存桶之外的完整的minio储存路径，如:  uavsystem/2/3.jpg
     * @return 下载地址
     */
    private String getObjectFullUrl(String bucketName, String fileName) {
        try {
            if (!PhotoStorage) {
//                // 存云端
//                MinioClient minioClient = new MinioClient(Endpoint, AccessKey, SecretKey);
//                /// 从Minio中获取文件
//                // InputStream stream = minioClient.getObject(BucketNameWord,  fileName);
//                // 生成一个 2 小时有效期的预先签名 get 请求 URL
//                //String urlString = minioClient.presignedGetObject(bucketName, fileName, 2 * 60 * 60);
////                String urlString = minioClient.presignedPutObject(bucketName, fileName);
//                String urlString = minioClient.getObjectUrl(bucketName, fileName);
//                return urlString;
            } else {
                LogUtil.logWarn("无法获取储存桶地址，暂时只支持Minio储存！");
            }
        } catch (Exception e) {
            LogUtil.logError("获取储存桶" + fileName + "地址失败：" + e.toString());
        }
        return "";
    }

    /**
     * 获取 minio 中储存的文件数据流
     *
     * @param bucketName 储存桶名称
     * @param fileName   去除储存桶之外的完整的minio储存路径，如:  uavsystem/2/3.jpg
     * @return 文件数据流 byte[]
     */
    public byte[] getObjectStream(String bucketName, String fileName) {
        byte[] packet = new byte[0];
        try {
            if (!PhotoStorage) {
//                // 存云端
//                MinioClient minioClient = new MinioClient(Endpoint, AccessKey, SecretKey);
//                /// 从Minio中获取文件
//                InputStream stream = minioClient.getObject(bucketName, fileName);
//                if (stream != null) {
//                    packet = BytesUtil.inputStreamToByteArray(stream);
//                }
            } else {
                LogUtil.logWarn("无法获取储存桶数据流，暂时只支持Minio储存！");
            }
        } catch (Exception e) {
            LogUtil.logError("获取储存桶异常：" + e.toString());
        }
        return packet;
    }
}