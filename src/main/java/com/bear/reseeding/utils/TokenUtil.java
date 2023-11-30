package com.bear.reseeding.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bear.reseeding.entity.EfUser;
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
public class TokenUtil {
    //签名私钥
    private static String key;
    //签名有效时间毫秒
    private static Long timeOut;

    @Value("${spring.jwt.jwtKey:efuavsdc12345d0e855e72c0a6a0test}")
    public void setkey(String temp) {
        key = temp;
    }

    @Value("${spring.jwt.timeOut:3600000}")
    public void setTimeOut(Long temp) {
        timeOut = temp;
    }

    /**
     * 生成签名 , 储存用户id、登录名、姓名、公司id，角色id
     *
     * @param user 用户信息
     * @return
     */
    public static String sign(EfUser user) {
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
                    .withClaim("userId", user.getId())
                    .withClaim("userLoginName", user.getULoginName())
                    .withClaim("userName", user.getUName())
                    .withClaim("companyId", user.getUCId())
                    .withClaim("roleId", user.getURId())
//                    .withClaim("ipLocal", user.getIpLocal())
//                    .withClaim("ipWww", user.getIpWww())
//                    .withClaim("sessionId", user.getSessionId())

                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            LogUtil.logError("Jwt生成Token异常：" + e.getMessage());
            return null;
        }
    }

    /**
     * 获得token中的用户ID
     *
     * @param token
     * @return
     */
    public static EfUser getUser(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            EfUser user = new EfUser();
            user.setId(jwt.getClaim("userId").asInt());
            user.setULoginName(jwt.getClaim("userLoginName").asString());
            user.setUName(jwt.getClaim("userName").asString());
            user.setUCId(jwt.getClaim("companyId").asInt());
            user.setURId(jwt.getClaim("roleId").asInt());
//            user.setULoginName(jwt.getClaim("ipLocal").asString());
//            user.setULoginName(jwt.getClaim("ipWww").asString());
//            user.setULoginName(jwt.getClaim("sessionId").asString());
            return user;
        } catch (JWTDecodeException e) {
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
}
