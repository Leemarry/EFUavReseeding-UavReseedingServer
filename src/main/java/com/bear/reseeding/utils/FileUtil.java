package com.bear.reseeding.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

/**
 * @Auther: bear
 * @Date: 2021/7/16 10:00
 * @Description: null
 */
public class FileUtil {

    /**
     * 判断文件或文件夹是否存在
     *
     * @param path 文件完整路径
     * @return true, 否则返回false
     */
    public static boolean isFileExit(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 删除文件，可以是单个文件或文件夹
     *
     * @param fileName 待删除的文件名
     * @return 文件删除成功返回true, 否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                return deleteFile(fileName);
            } else {
                return deleteDirectory(fileName);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 被删除文件的文件名
     * @return 单个文件删除成功返回true, 否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            if (file.isFile()) {
                return file.delete();
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 被删除文件的文件名
     * @return 单个文件删除成功返回true, 否则返回false
     */
    public static boolean deleteFile2(String fileName) {
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            file.delete();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param path 将要删除的文件目录
     */
    public static boolean deleteDir(String path) {
        return deleteDir(new File(path));
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     */
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.exists()) {
            if (dir.isDirectory()) {
                String[] children = dir.list();
                if (children != null) {
                    //递归删除目录中的子目录下
                    for (String child : children) {
                        boolean success = deleteDir(new File(dir, child));
                        if (!success) {
                            return false;
                        }
                    }
                }
            }
            // 目录此时为空，可以删除
            return dir.delete();
        } else {
            return false;
        }
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param dir 被删除目录的文件路径
     * @return 目录删除成功返回true, 否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            System.out.println("删除目录失败" + dir + "目录不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
            // 删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }

        if (!flag) {
            System.out.println("删除目录失败");
            return false;
        }

        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            System.out.println("删除目录" + dir + "失败！");
            return false;
        }
    }
    // 删除文件夹
    // param folderPath 文件夹完整绝对路径

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 删除指定文件夹下所有文件
    // param path 文件夹完整绝对路径
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);// 再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    //根据文件路径删除文件和文件夹下的内容
    //需要注意的是当删除某一目录时，必须保证该目录下没有其他文件才能正确删除，否则将删除失败。
    public static boolean deleteFolder(File folder) {
        boolean flag = false;
        try {
            if (!folder.exists()) {
                flag = true;
            } else {
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isDirectory()) {
                            //递归直到目录下没有文件
                            flag = deleteFolder(file);
                        } else {
                            //删除
                            flag = file.delete();
                        }
                    }
                }
                //删除
                flag = folder.delete();
            }
        } catch (Exception e) {
            LogUtil.logError("删除目录或文件夹[" + folder.getPath() + "]失败：" + e.toString());
        }
        return flag;
    }

    /**
     * 将流数据保存到本地文件
     *
     * @param savePath 保存的路径
     * @param fileName 保存的名称，含后缀名
     * @param data     流数据,Byte数组
     * @return boolean
     */
    public static boolean SaveStreamAsFile(String savePath, String fileName, byte[] data) {
        try {
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            File file = new File(saveDir + File.separator + fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();
            return (file.exists());
        } catch (IOException e) {
            LogUtil.logError("保存流文件在本地失败！" + e.toString());
            return false;
        }
    }

	/*public static void main(String[] args) {
		// String fileName = "g:/temp/xwz.txt";
		// DeleteFileUtil.deleteFile(fileName);
		String fileDir = "D:\\temp\\pom.xml";
		// DeleteFileUtil.deleteDirectory(fileDir);
		DeleteFileUtil.delete(fileDir);
		DeleteFileUtil t = new DeleteFileUtil();
		delFolder("c:/bb");
		System.out.println("deleted");

	}*/

    public static void inputStreamToFile(InputStream ins, File file, File fileName) {
        String path = "";
        try {
            if (file != null) {
                path = file.getPath() + File.separator + fileName;
            }
            OutputStream os = new FileOutputStream(path);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            LogUtil.logError("保存流数据失败：" + path);
        }
    }


    /**
     * 创建该文件
     */

    /**
     * 判断是否是 文件类型 或创建
     * @param filePath 文件路径
     */
    public static void createOrUpdateFile(String filePath) {
        File file = new File(filePath);
        // 确保路径中的所有目录都存在
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        if (file.exists() && !file.isFile()) {
            if (file.isDirectory()) {
                file.delete();
            }
            try {
                file.createNewFile();
//                System.out.println("文件已重新生成");
            } catch (IOException e) {
//                System.out.println("重新生成文件出错：" + e.getMessage());
            }
        } else if (!file.exists()) {
            try {
                file.createNewFile();
//                System.out.println("文件已创建");
            } catch (IOException e) {
//                System.out.println("创建文件出错：" + e.getMessage());
            }
        } else {
//            System.out.println("文件存在且为文件类型");
        }
    }


    /**
     * 文件合并
     *
     * @param targetFile
     * @param folder
     */
    public static void merge(String targetFile, String folder, String filename) {
        try {
            Files.createFile(Paths.get(targetFile));
        } catch (IOException e) {
        }
        try (Stream<Path> stream = Files.list(Paths.get(folder))) {
            stream.filter(path -> !path.getFileName().toString().equals(filename))
                    .sorted((o1, o2) -> {
                        String p1 = o1.getFileName().toString();
                        String p2 = o2.getFileName().toString();
                        int i1 = p1.lastIndexOf("-");
                        int i2 = p2.lastIndexOf("-");
                        return Integer.valueOf(p2.substring(i2)).compareTo(Integer.valueOf(p1.substring(i1)));
                    })
                    .forEach(path -> {
                        try {
                            //以追加的形式写入文件
                            Files.write(Paths.get(targetFile), Files.readAllBytes(path), StandardOpenOption.APPEND);
                            //合并后删除该块
                            Files.delete(path);
                        } catch (IOException e) {
                        }
                    });
        } catch (IOException e) {
        }
    }

    /**
     * 递归查找文件
     *
     * @param file
     */
    public static Boolean searchFile(String filename, File... file) {
        boolean isExist = false;
        if (file != null && file.length > 0) {
            for (File f : file) {
                String name = f.getName();
                if (name.equals(filename)) {
                    isExist = true;
                    break;
                }
            }
        }
        return isExist;
    }

    /**
     * 获取缩略图的 File 对象。
     * 将 MultipartFile 对象生成缩略图，并返回缩略图的 File 对象。
     *
     * @param multipartFile 原始的 MultipartFile 对象
     * @return File 返回缩略图对象
     */
    public static File getThumbnailInputStream(MultipartFile multipartFile) {
        return getThumbnailInputStream(multipartFile, 160, 120);
    }

    /**
     * 获取缩略图的 File 对象。
     * 将 MultipartFile 对象生成缩略图，并返回缩略图的 File 对象。
     *
     * @param multipartFile 原始的 MultipartFile 对象
     * @param width         默认160
     * @param height        默认120
     * @return File 返回缩略图对象
     */
    public static File getThumbnailInputStream(MultipartFile multipartFile, int width, int height) {
        File fileNew = null;
        try {
            // 选择用缓冲区来实现这个转换即使用java 创建的临时文件 使用 MultipartFile.transferto()方法 。
            String originalFilename = multipartFile.getOriginalFilename();
            //获取文件后缀
            //String prefix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String[] filename = originalFilename.split("\\.");
            File fileSource = File.createTempFile(filename[0] + System.currentTimeMillis(), "." + filename[1]);
            multipartFile.transferTo(fileSource);
            //fileSource.deleteOnExit();  // 退出后删除
            fileNew = File.createTempFile(filename[0] + "_temp_" + System.currentTimeMillis(), "." + filename[1]);
            Thumbnails.of(fileSource)
                    .size(width, height)
                    .toFile(fileNew);
            if (fileSource.exists()) {
                fileSource.delete();
            }
        } catch (Exception ex) {
            LogUtil.logError("生成缩略图异常：" + ex.toString());
        }
        return fileNew;
    }


    /**
     * 保存原图，同时保存缩略图
     *
     * @param multipartFile 原图对象
     * @param path          原图保存文件夹路径
     * @param pathThumbnail 缩略图保存文件夹路径
     * @param fileName      文件名称，含后缀名
     * @return boolean
     */
    public static boolean saveFileAndThumbnail(MultipartFile multipartFile, String path, String pathThumbnail, String fileName) {
        boolean result = false;
        try {
            if (multipartFile != null) {
                File fileSource = new File(path, fileName);
                if (!fileSource.getParentFile().exists()) {
                    fileSource.getParentFile().mkdirs();
                }
                multipartFile.transferTo(fileSource);
                File fileNew = new File(pathThumbnail, fileName);
                if (!fileNew.getParentFile().exists()) {
                    fileNew.getParentFile().mkdirs();
                }
                Thumbnails.of(fileSource)
                        .size(160, 120)
                        .toFile(fileNew);
                result = true;
            }
        } catch (Exception ex) {
            result = false;
            LogUtil.logError("保存原图同时保存缩略图时异常：" + ex.toString());
        }
        return result;
    }

    /**
     * 保存原图，同时保存缩略图
     *
     * @param data          原图对象
     * @param path          原图保存文件夹路径
     * @param pathThumbnail 缩略图保存文件夹路径
     * @param fileName      文件名称，含后缀名
     * @return boolean
     */
    public static boolean saveFileAndThumbnail(byte[] data, String path, String pathThumbnail, String fileName) {
        boolean result = false;
        try {
            if (data == null || data.length < 3 || "".equals(path)) {
                return false;
            }
            try {
                File fileSource = new File(path, fileName);
                if (!fileSource.getParentFile().exists()) {
                    fileSource.getParentFile().mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(fileSource);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bos.write(data);
                bos.close();

                File fileNew = new File(pathThumbnail, fileName);
                if (!fileNew.getParentFile().exists()) {
                    fileNew.getParentFile().mkdirs();
                }
                Thumbnails.of(fileSource)
                        .size(160, 120)
                        .toFile(fileNew);
                result = true;
            } catch (Exception ex) {
                LogUtil.logError("图片字节数组转图片文件储存失败：" + ex.toString());
            }
        } catch (Exception ex) {
            result = false;
            LogUtil.logError("保存原图同时保存缩略图时异常：" + ex.toString());
        }
        return result;
    }

    /**
     * 根据File获得缩略图对象
     *
     * @param fileSource 原始图File
     * @param quality    压缩质量， 0-1
     * @return 压缩后的缩略图对象 File，使用完毕后删除该对象
     */
    public static File getThumbnailFile(File fileSource, double quality) {
        File fileNew = null;
        try {
            if (fileSource != null && fileSource.exists()) {
                long size = fileSource.length();
                if (size > 5 * 1024 * 1024) {
                    //TODO 如果大于5兆，才进行压缩
                }
                String fullName = fileSource.getName();
                String name = fullName;
                String suffix = "jpg";
                if (fullName.lastIndexOf(".") > 0) {
                    name = fullName.split("\\.")[0];
                    suffix = fullName.split("\\.")[1];
                }
                fileNew = File.createTempFile(name + "_temp_" + System.currentTimeMillis(), "." + suffix);
                Thumbnails.of(fileSource)
                        .scale(1)
                        .outputQuality(quality)
                        .toFile(fileNew);
            }
        } catch (Exception ex) {
            LogUtil.logError("生成缩略图异常：" + ex.toString());
        }
        return fileNew;
    }

    public static File multipartFileToFile(MultipartFile file) throws Exception {
        File toFile = null;
        if ("".equals(file) || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        File file = new File("C:\\efuav\\UavSystem\\photo\\uav\\UAV1ZNBJBK00C005X\\images\\20220622");
        Boolean aBoolean = searchFile("dji100045_20220622090252.jpg", file);
        System.out.println(aBoolean);
    }
}
