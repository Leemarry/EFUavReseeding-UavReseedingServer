package com.bear.reseeding.controller;

import com.bear.reseeding.common.ResultUtil;
import com.bear.reseeding.entity.*;
import com.bear.reseeding.model.CurrentUser;
import com.bear.reseeding.model.Result;
import com.bear.reseeding.service.*;
import com.bear.reseeding.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.Calendar;

/**
 * 用户管理
 *
 * @author bear
 * @since 2023-11-10 16:12:45
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Autowired
    private RedisUtils redisUtils;

    @Resource
    private EfUserService efUserService;
    @Resource
    private EfUserLoginService efUserLoginService;

    /**
     * 加密
     */
    @Value("${spring.config.encryptMd5Soft:efuav201603}")
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
            EfUser user = new EfUser();
            user.setULoginName(userId);
//            String userPwdMd5 = userPwd;
            String userPwdMd5 = MD5Util.md5Encode(userPwd + encryptMd5Soft);
            user = efUserService.login(userId, userPwdMd5);
            if (user == null) {
                LogUtil.logMessage(ipWww + "(" + ipLocal + ")账户: " + userId + " - " + userPwd + " 登录失败！");
                return ResultUtil.error("账户信息错误！");
            } else {
                LogUtil.logMessage(ipWww + "(" + ipLocal + ")账户: " + userId + " - " + userPwd + " 登录成功。");
            }
            if (user.getUStatus() != null) {
                if (user.getUStatus() == -1) {
                    return ResultUtil.error("账号已删除!");
                } else if (user.getUStatus() == -2) {
                    return ResultUtil.error("账号已冻结");
                } else if (user.getUStatus() == -3) {
                    return ResultUtil.error("账号已失效!");
                }
            }
            String token = TokenUtil.sign(user);
            return ResultUtil.success(token);
        } catch (Exception e) {
            LogUtil.logError("登录异常：" + e.toString());
            return ResultUtil.error("登录异常,请联系管理员!");
        }
    }


    /**
     * 安卓端登陆
     *
     * @param map     RsaPublicKey
     *                UserId
     *                UserPwd
     *                ClientId
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/appLogin")
    public Result appLogin(@RequestBody Map<String, String> map, HttpServletRequest request) {
        try {
            try {
                Jedis jedis = new Jedis(host, port);
                String ping = jedis.ping();
                if (!"PONG".equalsIgnoreCase(ping)) {
                    return ResultUtil.error("数据服务未启动!");
                }
            } catch (Exception e) {
                return ResultUtil.error("数据服务未启动!");
            }
            String loginName = map.getOrDefault("UserId", "").toString();
            String password = map.getOrDefault("UserPwd", "").toString();
            String clientId = map.getOrDefault("ClientId", "").toString();
            if ("".equals(loginName) || "".equals(password)) {
                return ResultUtil.error("请输入正确的账户信息!");
            }
            password = MD5Util.md5Encode(password + encryptMd5Soft);
            EfUser user = efUserService.login(loginName, password);
            if (user == null) {
                return ResultUtil.error("账户信息错误!");
            } else if (user.getUStatus() != null) {
                if (user.getUStatus() == -1) {
                    return ResultUtil.error("账号已删除!");
                } else if (user.getUStatus() == -2) {
                    return ResultUtil.error("账号已冻结！");
                } else if (user.getUStatus() == -3) {
                    return ResultUtil.error("账号已失效!");
                }
            }
            String roleId = String.valueOf(user.getURId());
            Date date = user.getULimitDate();
            if (date == null || date.getTime() < System.currentTimeMillis()) {
                return ResultUtil.error("账号已过期!");
            }
//            if (user.get() == null || user.getEfCompany().getCLimitDate() == null || user.getEfCompany().getCLimitDate().getTime() < System.currentTimeMillis()) {
//                return ResultUtil.error("公司账户已过期!");
//            }
            //region 保存登录记录
            String addr = request.getRemoteAddr();
            String ip = NetworkUtil.getIpAddr(request);
            String userAgent = request.getHeader("User-Agent");
            EfUserLogin logined = new EfUserLogin();
            logined.setUIpLocal(addr);
            logined.setUIpWww(ip);
            logined.setUUserId(user.getId());
            logined.setUAgent(userAgent);
            logined.setULoginTime(new Date());
            logined.setULoginName(user.getULoginName());
            logined.setUName(user.getUName());
            logined.setUMachineCode(clientId);
            logined.setULoginOutTime(new Date());
            logined.setUOnlineTime(1);
            logined.setUDescription("大疆APP登录");
            logined.setUStatus(0);
            logined = efUserLoginService.insert(logined);
            if (logined == null) {
                LogUtil.logWarn("用户[" + loginName + "]登录成功，保存登录记录失败!");
            }
            //endregion
            //region 返回token
            String token = TokenUtil.sign(user);
            Map<String, Object> hashMap = new HashMap<String, Object>();
            if (token != null) {
                if (redisUtils != null) {
                    redisUtils.set(token, "登录时间：" + System.currentTimeMillis(), 5L, TimeUnit.HOURS);
                    redisUtils.set(token + "_LoginIpWww", ip, 5L, TimeUnit.HOURS);
                    redisUtils.set(token + "_LoginIpLocal", addr, 5L, TimeUnit.HOURS);
                    redisUtils.set(token + "_UserInfo", user, 5L, TimeUnit.HOURS); //token对应的用户信息
                    redisUtils.set(token + "_LoginTime", System.currentTimeMillis(), 5L, TimeUnit.HOURS); //登录时间
                    redisUtils.set(token + "_LastOpterTime", System.currentTimeMillis(), 5L, TimeUnit.HOURS); //上次操作时间
                    if (logined != null) {
                        redisUtils.set(token + "_LoginedId", logined); //储存的登录记录ID
                    }
                    int roomId = (int) user.getUCId();
                    //获取userSig
                    String roomUserId = user.getUName() + "-" + DateUtil.timeStamp2Date(System.currentTimeMillis(), "HHmmssSSS");
//                    TLSSigAPIv2 api = new TLSSigAPIv2(EfStaticController.TxyTrtcSdkAppId, EfStaticController.TxyTrtcSecretKey);
//                    String userSig = api.genUserSig(roomUserId, EfStaticController.TxyTrtcExpireTime);
                    hashMap.put("token", token);
                    hashMap.put("roomId", roomId);
//                    hashMap.put("userSign", userSig);
                    hashMap.put("userName", user.getUName());
                    hashMap.put("roomUserId", roomUserId);
                }
                user.setULoginPassword("");
                hashMap.put("user", user);
                return ResultUtil.success(token, hashMap);
            } else {
                LogUtil.logWarn("登录完成, 生成Token失败！");
                return ResultUtil.error("生成唯一标识失败！");
            }
        } catch (Exception e) {
            LogUtil.logError("登录出错：" + e.toString());
            return ResultUtil.error("登录异常，请联系管理员！");
        }
    }

    /**
     * app心跳
     *
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/appHeartbeat")
    public Result appHeartbeat(@CurrentUser EfUser user, @RequestBody Map<String, String> map, HttpServletRequest request) {
        try {
            String MachineCode = map.getOrDefault("MachineCode", "");  //机器码，用来判断是不是已经登录过
            String onLine = map.getOrDefault("OnLine", "0");  //是否为退出登录，1为保活，0为退出系统
            String token = request.getHeader("token");
            String userLoginName = user.getULoginName();
            String userName = user.getUName();
            // 更新登录记录表
            EfUserLogin userLogin = new EfUserLogin();
            Object object = redisUtils.get(token + "_LoginedId");
            if (object != null) {
                userLogin = (EfUserLogin) object;
            }
            if ("0".equals(onLine)) {
                //退出登录
                if (object != null) {
                    userLogin.setULoginOutTime(new Date());
                    userLogin.setUOnlineTime((int) ((System.currentTimeMillis() - userLogin.getULoginTime().getTime()) / 1000));
                    userLogin = efUserLoginService.update(userLogin);
                }
                LogUtil.logInfo("DJI客户端[" + MachineCode + "] :  用户 " + userLoginName + " 退出登录");
                redisUtils.remove(token,
                        token + "_LoginIpWww",
                        token + "_LoginIpLocal",
                        token + "_UserInfo",
                        token + "_LoginTime",
                        token + "_LastOpterTime",
                        token + "_LoginedId");
                return ResultUtil.success();
            } else {
                // 正常保活中
                if (object != null) {
                    // 登录过
                    if (userLogin.getUMachineCode() != null && userLogin.getUMachineCode().equals(MachineCode)) {
                        userLogin.setULoginOutTime(new Date(System.currentTimeMillis()));
                        userLogin.setUOnlineTime((int) ((System.currentTimeMillis() - userLogin.getULoginTime().getTime()) / 1000));
                        userLogin = efUserLoginService.update(userLogin);
                        LogUtil.logInfo("DJI客户端[" + MachineCode + "]保活: 用户 " + userLoginName + " 正常保活中...");
                        //String sign = JwtUtil.sign(userId, userLoginName, userName, MachineCode, uCid, roleId);
                        return ResultUtil.success(); // 可刷新token
                    } else {
                        LogUtil.logInfo("DJI客户端[" + MachineCode + "]保活: 用户 " + userLoginName + " 的机器码与登录时不一致!");
                        return ResultUtil.error("DJI客户端[" + MachineCode + "]保活: 用户 " + userLoginName + " 登录地异常");
                    }
                } else {
                    //没有登录过，或重启了服务器
                    EfUserLogin userLogined = new EfUserLogin();  //服务器重启会导致无记录
                    userLogined.setULoginName(userLoginName);
                    userLogined.setULoginTime(new Date(System.currentTimeMillis()));
                    userLogined.setULoginOutTime(new Date());
                    userLogined.setUOnlineTime(1);
                    userLogined.setUDescription("大疆APP登录");
                    userLogined.setUStatus(0);
                    userLogined.setUMachineCode(MachineCode);
                    userLogined.setUName(userName);
                    userLogined = efUserLoginService.insert(userLogined);
                    redisUtils.set(token + "_LoginedId", userLogined);
                    return ResultUtil.error("DJI客户端[" + MachineCode + "]保活: 用户 " + userLoginName + " 无登录记录，已作为登录记录添加!");
                }
            }
        } catch (Exception e) {
            LogUtil.logError("DJI客户端保活异常: " + e.getMessage());
            return ResultUtil.error("DJI客户端保活异常: " + e.getMessage());
        }
    }

    /**
     * 退出登录
     *
     * @param map Token token值
     *            ClientId 客户端唯一编号
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/logout")
    public Result logout(@RequestBody Map<String, String> map) {
        try {
            String token = map.getOrDefault("Token", "").toString();
            //String clientId = map.getOrDefault("ClientId", "").toString();
            if (redisUtils != null) {
                if (redisUtils.exists(token + "_LoginedId")) {
                    EfUserLogin logined = (EfUserLogin) redisUtils.get(token + "_LoginedId"); //储存的登录记录ID
                    logined.setUOnlineTime((int) ((System.currentTimeMillis() - logined.getULoginTime().getTime()) / 1000));
                    logined.setULoginOutTime(new Date());
                    logined = efUserLoginService.update(logined);
                }
                redisUtils.remove(token,
                        token + "_LoginIpWww",
                        token + "_LoginIpLocal",
                        token + "_UserInfo",
                        token + "_LoginTime",
                        token + "_LastOpterTime",
                        token + "_LoginedId");
            }
        } catch (Exception e) {
            LogUtil.logError("注销登录出错：" + e.toString());
        }
        return ResultUtil.success();
    }

    /**
     * 查询公司下的所有Kmz,字段中的kmz路径未完整http下载地址(APP使用)
     *
     * @return 公司的KMZ航线任务文件
     */
    @ResponseBody
    @PostMapping(value = "/queryKmz")
    public Result queryKmz(@CurrentUser EfUser user) {
        try {
            int cid = user.getUCId();
            return ResultUtil.error("功能未实现");
//            List<EfTaskKmz> taskKmzList = efTaskKmzService.queryAllByCid(cid);
//            for (int i = 0; i < taskKmzList.size(); i++) {
//                String kmzPath = taskKmzList.get(i).getKmzPath();
//                String fullPath = minioService.getObjectFullRealUrl(BucketNameKmz, kmzPath);
//                taskKmzList.get(i).setKmzPath(fullPath);
//            }
//            return ResultUtil.successData(taskKmzList);
        } catch (Exception e) {
            LogUtil.logError("查询航线任务文件出错：" + e.toString());
            return ResultUtil.error("查询航线任务文件失败,请联系管理员!");
        }
    }


}

