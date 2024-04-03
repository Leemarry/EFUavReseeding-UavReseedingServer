package com.bear.reseeding.controller;

import com.bear.reseeding.common.ResultUtil;
import com.bear.reseeding.model.Result;
import com.bear.reseeding.utils.LogUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @Auther: bear
 * @Date: 2022-11-18 10:18:11
 * @Description: null
 */
@RestController
@RequestMapping("debugger")
public class ADebugController {

    @ResponseBody
    @PostMapping(value = "/test")
    public Result test() {
        try {
            return ResultUtil.success("测试正常");
        } catch (Exception e) {
            LogUtil.logError("测试异常：" + e.toString());
            return ResultUtil.error("测试异常,请联系管理员!");
        }
    }

    @ResponseBody
    @PostMapping(value = "/test2")
    public Result test2() {
        try {
            return ResultUtil.success("测试正常");
        } catch (Exception e) {
            LogUtil.logError("测试异常：" + e.toString());
            return ResultUtil.error("测试异常,请联系管理员!");
        }
    }

    /**
     * 检测 Mqtt 是否连接
     *
     * @return JSONObject 是否已连接
     */
    @ResponseBody
    @PostMapping(value = "/debuglog")
    public Result showDebugLog(boolean showlog) {
        LogUtil.printInfo = showlog;
        if (showlog) {
            LogUtil.logInfo("已开启调试日志打印.");
        } else {
            LogUtil.logMessage("已关闭调试日志输出!");
        }
        return ResultUtil.success();
    }

    @ResponseBody
    @PostMapping(value = "/upload")
    public Result upload(@RequestParam(value = "file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResultUtil.error("上传照片失败，空文件！");
            }
            // 获取文件名-大小
            String fileName = file.getOriginalFilename();
            String desktopPath = System.getProperty("user.home") + "/Desktop/reseed";
            File savePath = new File(desktopPath, fileName);
            file.transferTo(savePath);
            LogUtil.logInfo("已储存照片：" + savePath.getAbsolutePath());
            return ResultUtil.success(fileName);
        } catch (Exception e) {
            LogUtil.logError("测试异常：" + e.toString());
            return ResultUtil.error("测试异常,请联系管理员!");
        }
    }
}
