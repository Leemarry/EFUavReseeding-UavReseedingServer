package com.bear.reseeding.controller;

import com.bear.reseeding.common.ResultUtil;
import com.bear.reseeding.model.Result;
import com.bear.reseeding.utils.LogUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
}
