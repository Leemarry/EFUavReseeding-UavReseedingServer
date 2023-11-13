package com.bear.reseeding.utils;

import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: bear
 * @Date: 2021/7/15 18:01
 * @Description: null
 */
public class LogUtil {
    //    private static Logger logger = Logger.getLogger("EFUAV");

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LogUtil.class);

    public static void test() {
        LOGGER.trace(" this is trace log infomation....");
        LOGGER.debug(" this is debug log infomation....");
        LOGGER.info(" this is info log infomation....");
        LOGGER.warn(" this is warn log infomation....");
        LOGGER.error(" this is error log infomation....");
        logMessage("this is always show log infomation....");
    }

    public static boolean printInfo = false;
    public static boolean printDebug = true;
    final static boolean printWarn = true;
    final static boolean printError = true;

    //    public static void log(String title, String msg) {
//        String dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
//        logInfo(title + "\t" + msg);
//    }


    public static void logMessage(String msg) {
        String dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        System.out.println(dt + "\t" + msg);
    }


    /**
     * 打印调试日志，不常用
     *
     * @param msg
     */
    public static void logDebug(String msg) {
//        if (printDebug) {
//            logger.info(msg);
//        }
        LOGGER.info(msg);
    }


    /**
     * 打印info日志
     *
     * @param msg
     */
    public static void logInfo(String msg) {
//        if (printInfo) {
//            logger.info(msg);
//        }
        LOGGER.info(msg);
    }


    public static void logWarn(String msg) {
//        if (printWarn) {
//            logger.warning(msg);
//        }
        LOGGER.warn(msg);
    }

    public static void logError(String msg) {
//        if (printError) {
//            logger.severe(msg);
//        }
        LOGGER.error(msg);
    }
}
