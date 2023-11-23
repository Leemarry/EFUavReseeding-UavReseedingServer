package com.bear.reseeding.controller;

import com.bear.reseeding.common.ResultUtil;
import com.bear.reseeding.model.Result;
import com.bear.reseeding.service.EfUavService;
import com.bear.reseeding.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 无人机管理
 *
 * @author bear
 * @since 2023-11-10 16:12:45
 */
@RestController
@RequestMapping("uav")
public class UavController {
    @Autowired
    private RedisUtils redisUtils;


    @Resource
    private EfUavService efUavService;

    /**
     * 加密
     */
    @Value("${spring.config.encryptMd5Soft:water}")
    String encryptMd5Soft;

    @ResponseBody
    @PostMapping(value = "/test")
    public Result test() {
        return ResultUtil.success("测试正常");
    }

    /**
     * 获取所有权限的无人机
     *
     * @return Result {data:token}
     */
    @ResponseBody
    @PostMapping(value = "/getUavs")
    public Result getUavs(@RequestParam(value = "userId") String userId, @RequestParam(value = "userPwd") String userPwd, HttpServletRequest request) {
        try {
            // String idSession = request.getSession().getId();
            String ipLocal = request.getRemoteAddr();
            String ipWww = NetworkUtil.getIpAddr(request);

            return ResultUtil.success();
        } catch (Exception e) {
            LogUtil.logError("获取所有权限的无人机异常：" + e.toString());
            return ResultUtil.error("获取所有权限的无人机异常,请联系管理员!");
        }
    }
}

