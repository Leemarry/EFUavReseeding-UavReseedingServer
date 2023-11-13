package com.bear.reseeding.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @Auther: bear
 * @Date: 2021/5/19 17:09
 * @Description: null
 */
public class ThumbnailUtil {

    /**
     * 根据图片路径生成缩略图
     *
     * @param oldPath 原图片路径
     * @param w       缩略图宽
     * @param h       缩略图高
     * @param prevfix 生成缩略图的前缀
     * @param force   是否强制按照宽高生成缩略图(如果为false，则生成最佳比例缩略图)
     */
    public static boolean thumbnailImage(String oldPath, String newPath, int w, int h, String MediaName, String prevfix, boolean force) {
        File fileSource = new File(oldPath);
        if (fileSource.exists()) {
            try {
                // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
                String types = Arrays.toString(ImageIO.getReaderFormatNames());
                String suffix = null;
                // 获取图片后缀
                if (fileSource.getName().contains(".")) {
                    suffix = fileSource.getName().substring(fileSource.getName().lastIndexOf(".") + 1);
                }// 类型和图片后缀全部小写，然后判断后缀是否合法
                if (suffix == null || !types.toLowerCase().contains(suffix.toLowerCase())) {
                    return false; //格式不支持
                }
                Image img = ImageIO.read(fileSource);
                if (!force) {
                    // 根据原图与要求的缩略图比例，找到最合适的缩略图比例
                    int width = img.getWidth(null);
                    int height = img.getHeight(null);
                    if ((width * 1.0) / w < (height * 1.0) / h) {
                        if (width > w) {
                            h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w / (width * 1.0)));
                        }
                    } else {
                        if (height > h) {
                            w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h / (height * 1.0)));
                        }
                    }
                }
                BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Graphics g = bi.getGraphics();
                g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
                g.dispose();

                String filePath = newPath + MediaName + prevfix;

                File newfile = new File(filePath);
                if (!newfile.getParentFile().exists()) {
                    boolean c = newfile.getParentFile().mkdirs();
                }
                boolean result = ImageIO.write(bi, suffix, new File(filePath));
                return result;
                // 将图片保存在原目录并加上前缀
                //String p = fileSource.getPath();
                //return ImageIO.write(bi, suffix, new File(p.substring(0, p.lastIndexOf(File.separator)) + File.separator + prevfix + fileSource.getName()));
            } catch (IOException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean zoomImageScale(File imageFile, float sale, String path, String MediaName, String suffix) {
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(imageFile);
            int originalWidth = bufferedImage.getWidth();
            int originalHeight = bufferedImage.getHeight();
            float scale = 0.2f;
            int newWidth = (int) (originalWidth * scale);
            int newHeight = (int) (originalHeight * scale);
            return zoomImageSize(imageFile, newWidth, newHeight, path, MediaName, suffix);
        } catch (IOException e) {
            LogUtil.logInfo(e.toString());
        }
        return false;
    }

    public static boolean zoomImageSize(File imageFile, int width, int height, String path, String MediaName, String suffix) {
        try {
            String filePath = path + MediaName + suffix;
            File newfile = new File(filePath);
            if (!newfile.getParentFile().exists()) {
                newfile.getParentFile().mkdirs();
            }
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            // 处理 png 背景变黑的问题
            if (suffix != null && (suffix.trim().toLowerCase().endsWith("png") || suffix.trim().toLowerCase().endsWith("gif"))) {
                BufferedImage to = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = to.createGraphics();
                to = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
                g2d.dispose();

                g2d = to.createGraphics();
                Image from = bufferedImage.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
                g2d.drawImage(from, 0, 0, null);
                g2d.dispose();
                return ImageIO.write(to, suffix, newfile);
            } else {
                BufferedImage newImage = new BufferedImage(width, height, bufferedImage.getType());
                Graphics g = newImage.getGraphics();
                g.drawImage(bufferedImage, 0, 0, width, height, null);
                g.dispose();
                return ImageIO.write(newImage, suffix, newfile);
                     /*
                        //高质量压缩，其实对清晰度而言没有太多的帮助
                        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                        tag.getGraphics().drawImage(bufferedImage, 0, 0, width, height, null);

                        FileOutputStream out = new FileOutputStream(newPath);    // 将图片写入 newPath
                        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                        JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
                        jep.setQuality(1f, true);    //压缩质量, 1 是最高值
                        encoder.encode(tag, jep);
                        out.close();
                    */
            }
        } catch (Exception e) {
            LogUtil.logInfo(e.toString());
        }

        return false;
    }

    private static int maxW = 0;//缩略图最大的宽度，此值只作参考
    private static double point = 0;//计算出比例之后，添加富余的百分比
    private static String postfix = "_2";//缩略图的名称：原图的名称+_2+.+后缀名
    //该方法用来生成缩略图

    /**
     * 按指定高度 等比例缩放图片
     *
     * @param imageFile
     */
//    public void zoomImageScale(File imageFile) {
//        try {
//            if (!imageFile.canRead()) return;
//            BufferedImage bufferedImage = ImageIO.read(imageFile);
//            if (null == bufferedImage) return;
//
//            int originalWidth = bufferedImage.getWidth();
//            int originalHeight = bufferedImage.getHeight();
//            //获取缩略图的路径
//            String originalPath = imageFile.getAbsolutePath();
//            String thumPath = (originalPath.substring(0, originalPath.lastIndexOf("."))) + postfix + (originalPath.substring(originalPath.lastIndexOf(".")));
//	        /*
//	        //根据原始宽度获取缩放后的宽度
//	        int newWidth = getImgNewWidth(originalWidth);
//	        System.out.println("新的宽度："+newWidth);
//	        double scale = (double)originalWidth / (double)newWidth;    // 缩放的比例
//
//	        int newHeight =  (int)(originalHeight / scale);
//	        */
//            //计算新的高度和宽度
//            double scale = getImgScale(originalWidth);
//            if (scale == 1) {//为1，说明按照原图生成缩略图，也就是复制一张图
//                System.out.println("复制图片，路径为：" + originalPath);
//                FileUtils.copyFile(new File(originalPath), new File(thumPath));
//            } else {//按照要求生成一张
//                int newWidth = (int) (originalWidth * scale);
//                int newHeight = (int) (originalHeight * scale);
//                //System.out.println("原始路径："+originalPath+"\n缩略图路径："+(name+postfix+extName));
//                zoomImageUtils(imageFile, thumPath, bufferedImage, newWidth, newHeight);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private void zoomImageUtils(File imageFile, String newPath, BufferedImage bufferedImage, int width, int height) {
//        String suffix = StringUtils.substringAfterLast(imageFile.getName(), ".");
//        try {
//            // 处理 png 背景变黑的问题
//            if (suffix != null && (suffix.trim().toLowerCase().endsWith("png") || suffix.trim().toLowerCase().endsWith("gif"))) {
//                BufferedImage to = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//                Graphics2D g2d = to.createGraphics();
//                to = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
//                g2d.dispose();
//
//                g2d = to.createGraphics();
//                Image from = bufferedImage.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
//                g2d.drawImage(from, 0, 0, null);
//                g2d.dispose();
//
//                ImageIO.write(to, suffix, new File(newPath));
//            } else {
//			    /*
//				//高质量压缩，其实对清晰度而言没有太多的帮助
//			    BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//			    tag.getGraphics().drawImage(bufferedImage, 0, 0, width, height, null);
//
//			    FileOutputStream out = new FileOutputStream(newPath);    // 将图片写入 newPath
//			    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//			    JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
//			    jep.setQuality(1f, true);    //压缩质量, 1 是最高值
//			    encoder.encode(tag, jep);
//			    out.close();
//			    */
//
//                BufferedImage newImage = new BufferedImage(width, height, bufferedImage.getType());
//                Graphics g = newImage.getGraphics();
//                g.drawImage(bufferedImage, 0, 0, width, height, null);
//                g.dispose();
//                ImageIO.write(newImage, suffix, new File(newPath));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 该方法根据原始图片的宽度/高度获取新的宽度/高度
//     * 原则：不得少于400，取与400最近的值
//     *
//     * @param originalWidth:原始宽度
//     **/
//    public int getImgNewWidth(int originalWidth) {
//        initMaxWidth();//初始化最大宽度和富余值
//        //如果小于maxW，返回原始值
//        if (originalWidth <= maxW) {
//            return originalWidth;
//        }
//
//        double scale = Math.ceil(maxW / originalWidth) + point;//向上取整
//        return (int) (maxW * scale);
//    }

//    /**
//     * 该方法根据原始图片的宽度/高度获取适合的比例
//     * 原则：不得少于400，取与500最近的值
//     *
//     * @param originalWidth:原始宽度
//     **/
//    public double getImgScale(int originalWidth) {
//        initMaxWidth();//初始化最大宽度和富余值
//        //如果小于maxW，直接返回1
//        if (originalWidth <= maxW) {
//            return 1;
//        }
//        double scale = (double) maxW / originalWidth;
//        scale = scale + point;
//        return Double.parseDouble(new DecimalFormat("0.00").format(scale));//保留2位小数
//    }

//    //该方法用来初始化最大的宽度和富余百分比
//    public void initMaxWidth() {
//        if (maxW <= 0) {//缩略图最大宽度
//            if (WebAppConfig.app("maxW") != null && WebAppConfig.app("maxW").trim().length() > 0) {
//                try {
//                    maxW = Integer.parseInt(WebAppConfig.app("maxW"));
//                } catch (Exception e) {
//                    maxW = 400;
//                    e.printStackTrace();
//                }
//            } else maxW = 400;
//        }
//
//        if (point <= 0) {//计算出比例之后，添加两个富余的百分比
//            if (WebAppConfig.app("point") != null && WebAppConfig.app("point").trim().length() > 0) {
//                try {
//                    point = Double.parseDouble(WebAppConfig.app("point"));
//                } catch (Exception e) {
//                    point = 0.02;
//                    e.printStackTrace();
//                }
//            } else point = 0.02;
//        }
//    }

    /* *//**
     * 该方法用来将指定目录下的所有jpeg,jpg格式的图片查找出来，并生成缩略图
     *
     * @param dic:需要生成缩略图的目录或文件
     **//*
    public void createThumImg(String dic) {
        File originalFile = new File(dic);
        if (!isDir(dic)) {//是个文件
            //判断这个文件是否存在
            if (originalFile.exists()) {//存在，生成缩略图
                String filePath = originalFile.getAbsolutePath();
                //判断是不是jpg,jpeg,png的格式，若是进行处理
                String extName = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();
                if (extName.endsWith("jpg") || extName.endsWith("jpeg") || extName.endsWith("png")) {
                    System.out.println("文件目录：" + filePath);
                    zoomImageScale(new File(filePath));
                }
            }
        } else {//是个目录，循环出下面的所有文件
            File[] files = originalFile.listFiles();
            for (int i = 0; i < files.length; i++) {
                createThumImg(files[i].getAbsolutePath());
            }
        }
    }

    *//**
     * 该方法用来判断给定的字符串是不是文件夹
     *
     * @param filePath:可能是文件夹，也可能是文件
     * @return boolean :true:文件夹 false:文件
     **//*
    public static boolean isDir(String filePath) {
        boolean result = false;
        //获取最后一个/的后面部分
        filePath = filePath.substring(filePath.lastIndexOf("/") + 1); // 在这里可以记录用户和文件信息

        //判断是否有.若没有，说明是文件夹，否则是文件（也可能是带有.的文件夹，此处不处理）
        if (filePath.indexOf(".") != -1) {//说明
            result = false;
        } else {//没有，是文件夹
            result = true;
        }
        return result;
    }*/
}
