package com.bear.reseeding.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

/**
 * @Auther: bear
 * @Date: 2021/7/15 17:45
 * @Description: null
 * <p>
 * ConfigurationProperties注解：在 Spring Boot 项目中大量的参数配置，在 application.properties
 * 或 application.yml 文件中，通过 @ConfigurationProperties 注解，我们可以方便的获取
 * 这些参数值，application.yml 文件本身支持list类型所以在application.yml 文件中可以这样配置：
 * jwt:
 * config:
 * key: 自定义私钥key值
 * timeOut: 有效时间(毫秒单位)
 */
@Component
@Configuration
public class JwtUtil {
    //签名私钥
    private static String key;
    //签名有效时间毫秒
    private static Long timeOut;

    @Value("${spring.jwt.jwtKey:efuavsdc28064d0e855e72c0a6a0test}")
    public void setkey(String temp) {
        key = temp;
    }

    @Value("${spring.jwt.timeOut:1600000}")
    public void setTimeOut(Long temp) {
        timeOut = temp;
    }

    /**
     * 生成签名, 120 min后过期
     *
     * @param userId        用户ID
     * @param userLoginName 用户登录名称
     * @param userName      用户姓名
     * @param clientId      用户客户端唯一编号
     * @param UCid          公司ID
     * @param roleId        角色ID
     * @return
     */
    public static String sign(String userId, String userLoginName, String userName, String clientId, String UCid, String roleId) {
        try {
            //过期时间
            Date date = new Date(System.currentTimeMillis() + timeOut);
            //私钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(key);
            //设置头部信息
            HashMap<String, Object> header = new HashMap<>(2);
            header.put("typ", "JWT");
            header.put("alg", "HS256");
            //附带username userid信息 生成签名
            return JWT.create()
                    .withHeader(header)
                    .withClaim("userName", userName)
                    .withClaim("userLoginName", userLoginName)
                    .withClaim("userId", userId)
                    .withClaim("clientId", clientId)
                    .withClaim("companyId", UCid)
                    .withClaim("roleId", roleId)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            LogUtil.logError("生成Token异常：" + e.getMessage());
            return null;
        }
    }

    /**
     * 校验token
     *
     * @param token
     * @return boolean
     */
    public static boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(key);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return (jwt != null);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获得token中的用户姓名
     *
     * @param token
     * @return token中包含的用户
     */
    public static String getUserName(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userName").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static int getUserIdInt(String token) {
        try {
            String userId = getUserId(token);
            int uid = 0;
            if (userId != null) {
                uid = Integer.parseInt(userId);
            }
            return uid;
        } catch (JWTDecodeException e) {
            return -1;
        }
    }

    /**
     * 获得token中的用户ID
     *
     * @param token
     * @return
     */
    public static String getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的用户登录名称
     *
     * @param token
     * @return
     */
    public static String getUserLoginName(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userLoginName").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static int getCompanyId(String token) {
        try {
            String uCid = getUCid(token);
            int id = 0;
            if (uCid != null) {
                id = Integer.parseInt(uCid);
            }
            return id;
        } catch (JWTDecodeException e) {
            return -1;
        }
    }

    /**
     * 获得token中的公司ID
     *
     * @param token
     * @return
     */
    public static String getUCid(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("companyId").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的用户登录名称
     *
     * @param token
     * @return
     */
    public static String getRoleId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("roleId").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
}
