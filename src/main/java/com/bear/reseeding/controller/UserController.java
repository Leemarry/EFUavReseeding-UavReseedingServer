package com.bear.reseeding.controller;

import com.bear.reseeding.common.ResultUtil;
import com.bear.reseeding.entity.*;
import com.bear.reseeding.model.Result;
import com.bear.reseeding.service.*;
import com.bear.reseeding.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户管理
 *
 * @author bear
 * @since 2023-11-10 16:12:45
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private RedisUtils redisUtils;
    /**
     * 服务对象
     */
    @Resource
    private TUserService efUserService;

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
     * 用户登录
     *
     * @param userId  用户名
     * @param userPwd 密码
     * @return Result {data:token}
     */
    @ResponseBody
    @PostMapping(value = "/login")
    public Result login(@RequestParam(value = "userId") String userId, @RequestParam(value = "userPwd") String userPwd, HttpServletRequest request) {
        try {
            // String idSession = request.getSession().getId();
            String ipLocal = request.getRemoteAddr();
            String ipWww = NetworkUtil.getIpAddr(request);
            TUser user = new TUser();
            user.setUserLoginId(userId);
            String userPwdMd5 = MD5Util.md5Encode(userPwd + encryptMd5Soft);
            user = efUserService.login(userId, userPwdMd5);
            if (user == null) {
                LogUtil.logMessage(ipWww + "(" + ipLocal + ")账户: " + userId + " - " + userPwd + " 登录失败！");
                return ResultUtil.error("账户信息错误！");
            } else {
                LogUtil.logMessage(ipWww + "(" + ipLocal + ")账户: " + userId + " - " + userPwd + " 登录成功。");
            }
            String token = TokenUtil.sign(user);
            return ResultUtil.success(token);
        } catch (Exception e) {
            LogUtil.logError("登录异常：" + e.toString());
            return ResultUtil.error("登录异常,请联系管理员!");
        }
    }
}

