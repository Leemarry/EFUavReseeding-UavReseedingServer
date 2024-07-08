package com.bear.reseeding.task;

import com.bear.reseeding.utils.BytesUtil;
import com.bear.reseeding.utils.LogUtil;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import io.minio.messages.Item;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
    @Value("${minio.BucketNameModel}")
    private String BucketNameModel;

    private MinioClient minioClient;


    /**
     * 上传文件到Mino储存桶
     *
     * @param bucketName  桶类型 kmz/word/其它
     * @param path        储存路径含完整名称： photo/image/123.jpg
     * @param contentType 文件类型：image/jpg
     * @param stream      文件流
     * @return boolean result是否上传成功
     */
    public boolean uploadImage(String bucketName, String path, String contentType, InputStream stream) {
        boolean result = false;
        try {
            if (!PhotoStorage) {
                // 存云端
                try {
                    if ("kmz".equals(bucketName)) {
                        bucketName = BucketNameKmz;
                    } else if ("word".equals(bucketName)) {
                        bucketName = BucketNameWord;
                    } else {
                        bucketName = BucketName;
                    }
                    MinioClient minioClient = MinioClient.builder()
                            .endpoint(Endpoint)
                            .credentials(AccessKey, SecretKey)
                            .build();

                    BucketExistsArgs exist = BucketExistsArgs.builder().bucket(bucketName).build();
                    boolean isExist = minioClient.bucketExists(exist);
                    if (!isExist) {
                        MakeBucketArgs create = MakeBucketArgs.builder().bucket(bucketName).build();
                        minioClient.makeBucket(create);
                        LogUtil.logMessage("正在上传文件到Minio，创建储存桶 " + bucketName + "成功。");
                    }
                    boolean isExists=  checkStatObjectExists(bucketName,path); // minio 统一路径同一文件名会出现bug
                    if(!isExists){
                        minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(path).stream(stream, stream.available(), -1).contentType(contentType).build());

//                        String fileUrl = minioClient.getPresignedObjectUrl(
//                                GetPresignedObjectUrlArgs.builder()
//                                        .method(Method.PUT)
//                                        .bucket(bucketName)
//                                        .object(path)
//                                        .build()
//                        );
                    }
                    String name = System.currentTimeMillis()+""+new Random().nextInt(9999);        //随机文件名
                    result = true;
                    LogUtil.logInfo("上传文件 " + path + " 到Minio成功。");
                } catch (Exception e) {
                    LogUtil.logError("上传文件到MINIO失败：" + e.toString());
                }
            } else {
                LogUtil.logWarn("保存文件到本地暂不支持，暂时只支持保存到Minio储存桶！");
                // 存本地
                String pathParentBig = BasePath + path;
            }
        } catch (Exception e) {
            LogUtil.logError("上传文件 " + path + " 到Minio异常：" + e.toString());
        }finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                LogUtil.logError("关闭文件流失败：" + e.toString());
            }
        }
        return result;
    }

    /**
     * 上传文件到Mino储存桶
     *
     * @param bucketName  桶类型 kmz/word/其它
     * @param path        储存路径含完整名称： photo/image/123.jpg
     * @param contentType 文件类型：image/jpg
     * @param stream      文件流
     * @return boolean result是否上传成功
     */
    public boolean uploadfile(String bucketName, String path, String contentType, InputStream stream) {
        boolean result = false;
        try {
            // 存云端
            try {
                if ("kmz".equals(bucketName)) {
                    bucketName = BucketNameKmz;
                } else if ("word".equals(bucketName)) {
                    bucketName = BucketNameWord;
                } else {
                    bucketName = BucketName;
                }
                MinioClient minioClient = MinioClient.builder()
                        .endpoint(Endpoint)
                        .credentials(AccessKey, SecretKey)
                        .build();

                BucketExistsArgs exist = BucketExistsArgs.builder().bucket(bucketName).build();
                boolean isExist = minioClient.bucketExists(exist);
                if (!isExist) {
                    MakeBucketArgs create = MakeBucketArgs.builder().bucket(bucketName).build();
                    minioClient.makeBucket(create);
                    LogUtil.logMessage("正在上传文件到Minio，创建储存桶 " + bucketName + "成功。");
                }
                minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(path).stream(stream, stream.available(), -1).contentType(contentType).build());

                String fileUrl = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.PUT)
                                .bucket(bucketName)
                                .object(path)
                                .build()
                );
                result = true;
                LogUtil.logInfo("上传文件 " + path + " 到Minio成功。");
            } catch (Exception e) {
                LogUtil.logError("上传文件到MINIO失败：" + e.toString());
            }

        } catch (Exception e) {
            LogUtil.logError("上传文件 " + path + " 到Minio异常：" + e.toString());
        }finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                LogUtil.logError("关闭文件流失败：" + e.toString());
            }
        }
        return result;
    }

    public String getPresignedObjectUrl(String bucketName, String fileNam) {
        String urlString = null;
        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(Endpoint)
                    .credentials(AccessKey, SecretKey)
                    .build();

            String fileUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(bucketName)
                            .object(fileNam)
                            .build()
            );

            URL url = new URL(fileUrl);
            String paths = url.getPath();
            String protocol = url.getProtocol();
            String host = url.getHost();
            int port = url.getPort();
            String hostString = protocol + "://" + host;
            if (port != -1) {
                hostString += ":" + port + paths;
            }
//            System.out.println(fileUrl);
            String fileName = paths.substring(paths.lastIndexOf('/') + 1);
            urlString = hostString;
        } catch (Exception e) {
            LogUtil.logError("Minio获取文件异常：" + e.toString());
        }
        return urlString;
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
                // 存云端
                MinioClient minioClient = MinioClient.builder()
                        .endpoint(Endpoint)
                        .credentials(AccessKey, SecretKey)
                        .build();
                /// 从Minio中获取文件
//                 InputStream stream = minioClient.getObject(BucketNameWord,  fileName);
                InputStream stream = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
                // 生成一个 2 小时有效期的预先签名 get 请求 URL
                //String urlString = minioClient.presignedGetObject(bucketName, fileName, 2 * 60 * 60);
//                String urlString = minioClient.presignedPutObject(bucketName, fileName);
//                String urlString = minioClient.getObjectUrl(bucketName, fileName);
                String urlString = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.PUT)
                                .bucket(bucketName)
                                .object(fileName)
                                .build()
                );
                return urlString;
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
                if ("kmz".equals(bucketName)) {
                    bucketName = BucketNameKmz;
                } else if ("word".equals(bucketName)) {
                    bucketName = BucketNameWord;
                } else {
                    bucketName = BucketName;
                }
                // 存云端
//                MinioClient minioClient = new MinioClient(Endpoint, AccessKey, SecretKey);
                MinioClient minioClient = MinioClient.builder()
                        .endpoint(Endpoint)
                        .credentials(AccessKey, SecretKey)
                        .build();
                /// 从Minio中获取文件
//                InputStream stream = minioClient.getObject(bucketName, fileName);
                InputStream stream = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(bucketName)
                                .object(fileName)
                                .build());
                if (stream != null) {
                    packet = BytesUtil.inputStreamToByteArray(stream);
                }
            } else {
                LogUtil.logWarn("无法获取储存桶数据流，暂时只支持Minio储存！");
            }
        } catch (Exception e) {
            LogUtil.logError("获取储存桶异常：" + e.toString());
        }
        return packet;
    }


    /**
     * 连接
     *
     * @param bucketName 桶名
     * @throws Exception 抛出异常
     */
    public void conneCtMinioClient(String bucketName) throws Exception {
        minioClient = MinioClient.builder()
                .endpoint(Endpoint)
                .credentials(AccessKey, SecretKey)
                .build();
    }

    /**
     * 判断桶是否存在
     *
     * @param bucketName 桶名
     * @return boolean 是否存在
     * @throws Exception 抛出异常
     */
    public boolean checkBucketExists(String bucketName) throws Exception {
        minioClient = MinioClient.builder()
                .endpoint(Endpoint)
                .credentials(AccessKey, SecretKey)
                .build();
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 判断桶存不存在 并且创建
     *
     * @param bucketName
     * @return boolean 是否存在不存在创建
     * @throws Exception
     */
    public boolean checkBucketExistsTOmakeBucket(String bucketName) {
        boolean flog = false;
        try {
            minioClient = MinioClient.builder()
                    .endpoint(Endpoint)
                    .credentials(AccessKey, SecretKey)
                    .build();

            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("cheshi").build());
            }
            flog = true;
        } catch (Exception e) {
            LogUtil.logError((e).toString());
        }
        return flog;
    }


    /**
     * 检测文件对象是否存在
     *
     * @param bucketName 桶名
     * @param objectName 文件路径
     * @return boolean 检测文件对象是否存在
     * @throws Exception 抛出异常
     */
    public boolean checkStatObjectExists(String bucketName, String objectName) {
        try {
            minioClient = MinioClient.builder()
                    .endpoint(Endpoint)
                    .credentials(AccessKey, SecretKey)
                    .build();

            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!bucketExists) {
                return false;
            }
            //检查桶内对象文件是否存在
            boolean isObjectExist = minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build()) != null;
            if (isObjectExist) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param bucketName 桶名
     * @param objectName 文件对象名称
     * @return boolean 判断文件是否存在
     */
    public boolean doesFileExist(String bucketName, String objectName) {
        try {
            minioClient = MinioClient.builder()
                    .endpoint(Endpoint)
                    .credentials(AccessKey, SecretKey)
                    .build();
            // 使用 minioClient.statObject() 方法来获取文件的信息
            minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
            return true; // 如果文件存在，没有抛出异常，则返回 true
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            // 如果文件不存在或出现异常，返回 false
            return false;
        }
    }

    /**
     * 删除文件对象
     *
     * @param bucketName
     * @param objectName
     * @return
     * @throws Exception
     */
    public boolean removeObject(String bucketName, String objectName) {
        boolean gold = false;
        try {
            minioClient = MinioClient.builder()
                    .endpoint(Endpoint)
                    .credentials(AccessKey, SecretKey)
                    .build();
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            gold = true;
        } catch (Exception e) {
            gold = false;
        }
        return gold;
    }


    public boolean downloadFile(String urlStr ,String downloadPathStr){
        try{
            URL url = new URL(urlStr);
            try(BufferedInputStream in= new BufferedInputStream(url.openStream())){
                FileOutputStream fileOutputStream = new FileOutputStream(downloadPathStr);
                byte[] dataBuffer = new byte[1024];
                int bytesRead ;
                while ((bytesRead= in.read(dataBuffer,0,1024)) !=-1){
                    fileOutputStream.write(dataBuffer,0,bytesRead);
                }
            }
            System.out.println("文件下载完成！");
            return true; // 下载成功
        }catch (IOException e) {
            System.out.println("文件下载失败：" + e.getMessage());
            return false; // 下载失败
        }
    }


    /**
     * 解压
     *
     * @param zipFilePath
     * @param extractPath
     * @return
     * @throws IOException
     */
    public HashMap<String,String> extractFile(String zipFilePath, String extractPath) throws IOException {
        HashMap<String, String> extractedPathsMap = new HashMap<>();
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
            byte[] buffer = new byte[1024];

            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                String entryName = zipEntry.getName();
                File newFile = new File(extractPath + entryName);

                if (zipEntry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    new File(newFile.getParent()).mkdirs();
                    FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                    int length;
                    while ((length = zipInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, length);
                    }
                    fileOutputStream.close();
                    extractedPathsMap.put(entryName,newFile.getAbsolutePath());
                }

                zipEntry = zipInputStream.getNextEntry();
            }
        }

        // 判断解压后的文件是否存在
        return extractedPathsMap;
    }


    /**
     * 删除文件夹内 对象文件
     *
     * @param bucketName
     * @param objectName
     * @return
     * @throws Exception
     */
    public boolean removeObjects(String bucketName, String objectName) {
        boolean gold = false;
        try {
            minioClient = MinioClient.builder()
                    .endpoint(Endpoint)
                    .credentials(AccessKey, SecretKey)
                    .build();
            Iterable<io.minio.Result<Item>> objects = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(objectName + "/")
                            .recursive(true)
                            .build()
            );
            for (io.minio.Result<Item> results : objects) {
                Item item = results.get();
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(bucketName)
                                .object(item.objectName())
                                .build()
                );
            }
            gold = true;

        } catch (Exception e) {
            gold = false;
        }
        return gold;
    }

    /**
     * 线程 同步删除文件夹内 对象文件
     */
    public boolean removeObjectsByThread(String bucketName, String objectName) {
        boolean gold = false;
        try {
            int numThreads = 100; // 设置100个线程
            List<String> objectNames = new ArrayList<>(); // 存储删除对象类型
            //获取文件 minio 路径
            minioClient = MinioClient.builder()
                    .endpoint(Endpoint)
                    .credentials(AccessKey, SecretKey)
                    .build();

            Iterable<io.minio.Result<Item>> objects = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(objectName)
                            .recursive(true)
                            .build()
            );
            for (io.minio.Result<Item> resultss : objects) {
                Item item = resultss.get();
                objectNames.add(item.objectName());
            }
            // 创建线程池 划分线程池
            ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
            // 每个线程删除的对象数量
            int batchSize = objectNames.size() / numThreads; //100
            // 开始多线程删除对象
            for (int i = 0; i < numThreads; i++) {
                final int start = i * batchSize;
                final int end = (i == numThreads - 1) ? objectNames.size() : (i + 1) * batchSize;
                executorService.submit(() -> {
                    for (int j = start; j < end; j++) {
                        try {
                            minioClient.removeObject(
                                    RemoveObjectArgs.builder()
                                            .bucket("cheshi")
                                            .object(objectNames.get(j))
                                            .build());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            // 等待所有线程完成
            executorService.shutdown();
            try {
                long timeout = 10; // 等待时间为10秒
                TimeUnit unit = TimeUnit.SECONDS; // 等待时间的单位为秒
                if (!executorService.awaitTermination(timeout, unit)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
                LogUtil.logError("多线程删除对象时出错: " + e.getMessage());
            }

            gold = true;  //是否 在线程内 删除完成都要关闭线程 返回删除成功

        } catch (Exception e) {
            gold = false;
        }
        return gold;
    }


    /**
     * minio 上传压缩文件 解压处理 -- 待验证
     *
     * @param bucketName
     * @param objectName
     * @return
     */
    public String extractFileAndSaveToSameDirectory(String bucketName, String objectName) {
        try {
            // 创建临时目录
            Path tempDir = Files.createTempDirectory(UUID.randomUUID().toString());

            // 下载压缩文件到临时目录
            Path tempFile = tempDir.resolve(objectName);
            minioClient.downloadObject(DownloadObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .filename(tempFile.toString())
                    .build());

            // 解压文件
            ZipFile zipFile = new ZipFile(tempFile.toFile());
            zipFile.extractAll(tempDir.toString());

            // 在此处可以对解压后的文件进行进一步的处理

            // 清理临时文件
            Files.delete(tempFile);
            Files.delete(tempDir);

            return "解压成功！";
        } catch (MinioException | IOException e) {
            e.printStackTrace();
            return "解压失败：" + e.getMessage();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return "解压失败：" + e.getMessage();
        }
    }

    private void unzipFile(File zipFile, String targetDirectory) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            byte[] buffer = new byte[1024];
            while ((entry = zis.getNextEntry()) != null) {
                String fileName = entry.getName();
                File newFile = new File(targetDirectory, fileName);
                if (entry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    new File(newFile.getParent()).mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int length;
                        while ((length = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }
                zis.closeEntry();
            }
        }
    }

    public String unzipInMinio(String bucketName, String objectName) {
        try {
            // 创建临时目录
            String tempDirName = UUID.randomUUID().toString();
            String tempObjectName = objectName.replaceAll(".zip$", "") + "-" + tempDirName + "/";
            // 获取预签名 URL 的过期时间为 5 分钟
            int expiryTimeInMinutes = 5;
            // 转换过期时间为毫秒
            long expiryInMillis = TimeUnit.MINUTES.toMillis(expiryTimeInMinutes);

            // 生成预签名 URL
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry((int) expiryInMillis)
                            .build()
            );
            // 创建临时目录
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(tempObjectName)
                    .stream(new ByteArrayInputStream(new byte[0]), 0, -1)
                    .build());

            // 解压文件并上传回 MinIO
            ZipFile zipFile = new ZipFile(new File(url));
            Path tempDir = Files.createTempDirectory(tempDirName);
            zipFile.extractAll(tempDir.toString());

            Files.walk(tempDir)
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            // 上传解压后的文件到 MinIO
                            minioClient.putObject(PutObjectArgs.builder()
                                    .bucket(bucketName)
                                    .object(path.subpath(tempDir.getNameCount(), path.getNameCount()).toString())
                                    .stream(Files.newInputStream(path), Files.newInputStream(path).available(), -1) // 修改此处参数为正确的 InputStream
                                    .build());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

            // 删除临时目录
            Files.walk(tempDir)
                    .sorted((p1, p2) -> -p1.compareTo(p2))
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            return "解压成功！";
        } catch (MinioException | IOException e) {
            e.printStackTrace();
            return "解压失败：" + e.getMessage();
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "解压失败：" + e.getMessage();
        }
    }

    /**
     * 上传临时分片文件 本质单文件上传
     *
     * @param chunk       文件块
     * @param chunkIndex  索引
     * @param totalChunks 块
     * @param filemd5     文件ms\d5
     * @return 分片上传是否成功
     */
    public boolean uploadChunkToMinio(MultipartFile chunk, int chunkIndex, int totalChunks, String filemd5) {
        // 存放目录
        String bucketName = "cheshi";

        try {
            // 检查桶是否存在
            boolean bucketExists = checkBucketExists(bucketName);
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            // 写入文件块 规格 00001
            String objectName = String.format("%s/%05d", filemd5, chunkIndex);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(chunk.getInputStream(), chunk.getSize(), -1)
                            .contentType(chunk.getContentType())
                            .build());

            return true; // 上传成功
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 上传失败
        }
    }

    /**
     * 合并 分片文件
     *
     * @param bucketName
     * @param targetName
     * @param totalPieces
     * @param md5
     * @return 布尔
     * @throws Exception
     */
    public boolean mergeFileFragments(String bucketName, String targetName, int totalPieces, String md5) throws Exception {
        // 检查参数是否有效
        if (bucketName == null || targetName == null || md5 == null || totalPieces <= 0) {
            throw new IllegalArgumentException("Invalid parameters");
        }
        try {
            minioClient = MinioClient.builder()
                    .endpoint(Endpoint)
                    .credentials(AccessKey, SecretKey)
                    .build();            // 检查文件索引是否都上传完毕
            Iterable<io.minio.Result<Item>> results = minioClient.listObjects(
                    io.minio.ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(md5 + "/")
                            .build());

            List<String> savedIndex = new ArrayList<>();
            for (io.minio.Result<Item> result : results) {
                savedIndex.add(result.get().objectName());
            }
            // 对文件索引进行排序
            Collections.sort(savedIndex);
            // 确认文件索引的数量是否和总分片数相等
            if (savedIndex.size() == totalPieces) {
                // 文件路径转换为文件合并对象
                List<ComposeSource> sourceObjectList = savedIndex.stream()
                        .map(filePath -> ComposeSource.builder()
                                .bucket(bucketName)
                                .object(filePath)
                                .build())
                        .collect(Collectors.toList());
                // 执行文件合并操作
                try {
                    minioClient.composeObject(
                            ComposeObjectArgs.builder()
                                    .bucket(bucketName)
                                    .object(targetName)
                                    .sources(sourceObjectList)
                                    .build());

                    // 合并成功，删除临时分片文件
                    // System.out.println("删除临时分片文件");

                    // 返回合并成功
                    return true;
                } catch (Exception e) {
                    // 合并失败，打印异常信息
                    e.printStackTrace();
                }
            }
            // 合并失败，返回false
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 拼接 获取路径
     *
     * @param bucketName 桶名
     * @param targetName 桶对象
     * @return 完整url
     */
    public String generateFileUrl(String bucketName, String targetName) {
        // 拼接文件URL的逻辑
        String baseUrl = EndpointExt;
        String url = baseUrl + "/" + bucketName + "/" + targetName;
        return url;
    }


    // 获取路径

    //region 模型地址路径 获取

    /**
     * 进行3次递归 找到模型 二维地图启动文件（默认 json文件 --最路径最短-文件名最短
     *
     * @param bucketName    桶名  cheshi
     * @param folderName    文件对象名 cheshimodel
     * @param fileExtension 文件后缀 xml / json
     * @param maxDepth      深度递归次数
     * @return fileUrls
     */
    public List<String> generateFileUrls(String bucketName, String folderName, String fileExtension, int maxDepth) {
        List<String> fileUrls = new ArrayList<>();
        try {
            minioClient = MinioClient.builder()
                    .endpoint(Endpoint)
                    .credentials(AccessKey, SecretKey)
                    .build();
            generateFileUrlsRecursive(bucketName, folderName, fileExtension, maxDepth, 0, fileUrls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrls;
    }

    /**
     * 每次递归 递归次数
     *
     * @param bucketName    桶名
     * @param folderName    文件对象
     * @param fileExtension 后缀名
     * @param maxDepth      深度递归次数
     * @param currentDepth  当前递归数
     * @param fileUrls      本次递归获取的urls
     * @throws Exception
     */
    private void generateFileUrlsRecursive(String bucketName, String folderName, String fileExtension, int maxDepth, int currentDepth, List<String> fileUrls) throws Exception {
        if (currentDepth > maxDepth) {
            return;
        }

        Iterable<io.minio.Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .prefix(folderName)
                        .recursive(false)
                        .build()
        );

        for (io.minio.Result<Item> result : results) {
            Item item = result.get();
            String objectName = item.objectName();

            if (item.isDir()) {
                generateFileUrlsRecursive(bucketName, objectName, fileExtension, maxDepth, currentDepth + 1, fileUrls);
            } else {
                String fileUrl = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.PUT)
                                .bucket(bucketName)
                                .object(objectName)
                                .build()
                );

                URL url = new URL(fileUrl);
                String path = url.getPath();
                String protocol = url.getProtocol();
                String host = url.getHost();
                int port = url.getPort();

                String hostString = protocol + "://" + host;
                if (port != -1) {
                    hostString += ":" + port;
                }
                String fileName = path.substring(path.lastIndexOf('/') + 1);
                boolean hasXmlExtension = fileName.toLowerCase().endsWith(".xml");
                boolean hasJsonExtension = fileName.toLowerCase().endsWith(".json");
                if (fileName.endsWith("xml") || fileName.endsWith("json")) {
                    fileUrls.add(hostString + path);
                }
            }
        }

    }

    /**
     * 递归文件夹 获取最外层的文件url-- 默认最外层最短的url为模型启动文件名
     *
     * @param urls
     * @return
     */
    public static String getShortestOuterPathUrl(List<String> urls) {
        int shortestLength = Integer.MAX_VALUE;
        String shortestUrl = urls.get(0);
        String[] pathlist = shortestUrl.split("/");
        int shortestOuterPath = pathlist.length;
        for (String url : urls) {
            String[] nextpathlist = url.split("/");
            int outerPath = nextpathlist.length;
            // 路径应该相对更外层 并且 默认应该更短
            if (outerPath <= shortestOuterPath && pathlist[shortestOuterPath - 1].getBytes().length < nextpathlist[outerPath - 1].getBytes().length) {
                shortestOuterPath = outerPath;
                shortestUrl = url;
            }
        }
        return shortestUrl;
    }


    // endregion

}