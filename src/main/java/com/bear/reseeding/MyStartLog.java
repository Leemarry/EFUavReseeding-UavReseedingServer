package com.bear.reseeding;


import com.bear.reseeding.utils.LogUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author baldwin
 */
@Component
@Order(1)
public class MyStartLog implements CommandLineRunner {

    @Override
    public void run(String... args) {
        StringBuilder commandLog = new StringBuilder();
        commandLog.append("\n");
        commandLog.append("*************************************************************************************\n");
        commandLog.append("                                                                                    \n");
        commandLog.append("                              RUNNING PORT:  " + MyApplication.appConfig.servicePort + "                                       \n");
        commandLog.append("                                                                                    \n");
        commandLog.append("                      v " + MyApplication.appConfig.serviceVersion + " ");
        commandLog.append("build time: " + MyApplication.appConfig.serviceBuildDate + "\n");
        commandLog.append("                                                                                    \n");
        commandLog.append("*************************************************************************************\n");
        System.out.println(commandLog.toString());

        LogUtil.test();
    }
}

