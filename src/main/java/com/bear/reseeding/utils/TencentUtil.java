package com.bear.reseeding.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bear.reseeding.MyApplication;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.live.v20180801.LiveClient;
import com.tencentcloudapi.live.v20180801.models.*;
import org.springframework.stereotype.Component;

/**
 * @Auther: bear
 * @Date: 2023/11/28 15:44
 * @Description: null
 */

@Component
public class TencentUtil {

    //查询腾讯云录制列表
    public static JSONArray queryTask(Long startTime, String streamName) {
        JSONArray array1 = new JSONArray();
        try {
            String TxySecretId = MyApplication.appConfig.getTxySecretId();
            String TxySecretKey = MyApplication.appConfig.getTxySecretKey();
            Credential cred = new Credential(TxySecretId, TxySecretKey);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("live.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            LiveClient client = new LiveClient(cred, "ap-guangzhou", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            DescribeRecordTaskRequest req = new DescribeRecordTaskRequest();
            req.setEndTime(startTime / 1000 + 60 * 60 * 24);
            req.setStartTime(startTime / 1000 - 60 * 60 * 6 * 24);
            req.setStreamName(streamName);
            DescribeRecordTaskResponse resp = client.DescribeRecordTask(req);
            JSONObject object = null;
            RecordTask[] taskList = resp.getTaskList();
            if (taskList.length > 0) {
                for (int i = 0; i < taskList.length; i++) {
                    RecordTask recordTask = taskList[i];
                    String taskId = recordTask.getTaskId();
                    object = new JSONObject();
                    object.put("taskId", taskId);
                    array1.add(object);
                }
                return array1;
            }
        } catch (Exception e) {
            return null;
        }
        return array1;
    }


    //删除录制列表
    public static Boolean deleteTask(String taskId) {
        try {
            String TxySecretId = MyApplication.appConfig.getTxySecretId();
            String TxySecretKey = MyApplication.appConfig.getTxySecretKey();
            Credential cred = new Credential(TxySecretId, TxySecretKey);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("live.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            LiveClient client = new LiveClient(cred, "ap-guangzhou", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            DeleteRecordTaskRequest req = new DeleteRecordTaskRequest();
            req.setTaskId(taskId);
            // 返回的resp是一个DeleteRecordTaskResponse的实例，与请求对象对应
            DeleteRecordTaskResponse resp = client.DeleteRecordTask(req);
        } catch (Exception e) {
            LogUtil.logMessage("删除相同流异常：" + e.toString());
            return false;
        }
        return true;
    }

    public static Boolean getLineStreamOnline(String appName, String streamName) {
        try {
            String TxySecretId = MyApplication.appConfig.getTxySecretId();
            String TxySecretKey = MyApplication.appConfig.getTxySecretKey();
            String TxyDomainName = MyApplication.appConfig.getTxyDomainName();
            Credential cred = new Credential(TxySecretId, TxySecretKey);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("live.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            LiveClient client = new LiveClient(cred, "ap-guangzhou", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            DescribeLiveStreamStateRequest req = new DescribeLiveStreamStateRequest();
            req.setAppName(appName);
            req.setDomainName(TxyDomainName);
            req.setStreamName(streamName);
            // 返回的resp是一个DescribeLiveStreamStateResponse的实例，与请求对象对应
            DescribeLiveStreamStateResponse resp = client.DescribeLiveStreamState(req);
            // 输出json格式的字符串回包
            String streamState = resp.getStreamState();
            if (streamState.equals("active")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
