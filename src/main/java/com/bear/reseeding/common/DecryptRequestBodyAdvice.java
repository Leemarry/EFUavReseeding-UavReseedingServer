package com.bear.reseeding.common;

import com.alibaba.fastjson.JSONObject;
import com.bear.reseeding.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * 请求参数 解密操作
 * 测试解密请求参数时候，请求体一定要有数据，否则不会调用实现类触发解密操作。
 *
 * @Auther: bear
 * @Date: 2021/7/16 14:21
 * @Description: null
 */
@Component
@ControllerAdvice(basePackages = "com.bear.reseeding.controller")
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

    /**
     * 是否需要对接收请求参数进行 解密
     */
    @Value("${spring.config.needDecrypt:true}")
    boolean needDecrypt;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * AES加密密钥
     */
    @Value("${spring.config.secretKey:ttsdfe124587jshqcgszmghstcw54735}")
    String secretKey;
    /**
     * AES加密向量
     */
    @Value("${spring.config.secretIv:tt3456qwdscfrtgb}")
    String secretIv;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> selectedConverterType) throws IOException {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        String ipaddress = Objects.requireNonNull(inputMessage.getHeaders().getHost()).toString();
        String servletPath = Objects.requireNonNull(parameter.getMethod()).getName(); // //index.html
        try {
            // 解密操作 DES
            if (needDecrypt) {
                String requestBodyMessageSource = JSONObject.toJSONString(body); // body.toString(); //加密后的内容
                JSONObject jsonObject = JSONObject.parseObject(requestBodyMessageSource);
                String data = jsonObject.getString("data"); //获取加密后的内容段数据，原始为<map>类型
                String requestBodyMessage = AESUtil.decryptAES(data, secretKey, secretIv); //根据AesKey解密 data 数据为map原始字符串
                LogUtil.logInfo("[" + ipaddress + "] --> 请求[" + servletPath + "] 参数：" + requestBodyMessage);
                return JSONObject.parseObject(requestBodyMessage);
               /* // SM2
                Object token = inputMessage.getHeaders().get("token");
                SM2 sm2 = null;
                String loginName = "";
                if (token != null) {
                    loginName = JwtUtil.getUserLoginName(token.toString());
                }
                Object privateKey = redisUtils.get(loginName + "_privateKey"); // 客户端私钥
                Object publicKey = redisUtils.get(loginName + "_publicKey"); // 客户端公钥
                if (privateKey != null && publicKey != null) {
                    sm2 = new SM2(privateKey.toString(), publicKey.toString());
                    String requestBodyMessageSource = body.toString(); //加密后的内容
                    String requestBodyMessage = SMUtil.decrypt(requestBodyMessageSource, sm2);
                    return JSONObject.parseObject(requestBodyMessage);
                } else {
                    return body;
                }*/

               /* //RSA
               String requestBodyMessageSource = body.toString(); //加密后的内容
                requestBodyMessageSource = Base64Util.decodeData(requestBodyMessageSource); //首先base64解码为正常内容
                JSONObject jsonObject = JSONObject.parseObject(requestBodyMessageSource);
                String data = jsonObject.getString("Data"); //获取加密后的内容段数据，原始为<map>类型
                String AesKey = jsonObject.getString("AesKey");
                String AesIv = jsonObject.getString("AesIv");
                AesKey = RSAUtil.DecryptByPrivateKey(AesKey, ParkingapronApplication.RsaKeyPair.getPrivate()); //解密 AesKey
                AesIv = RSAUtil.DecryptByPrivateKey(AesIv, ParkingapronApplication.RsaKeyPair.getPrivate()); //解密 AesIv
                String requestBodyMessage = AESUtil.decryptAES(data, AesKey, AesIv); //根据AesKey解密 data 数据为map原始字符串
                LogUtil.logInfo("[" + ipaddress + "] --> 请求[" + servletPath + "] 参数：" + requestBodyMessage);
                return JSONObject.parseObject(requestBodyMessage);*/
            } else {
                LogUtil.logInfo("[" + ipaddress + "] --> 请求[" + servletPath + "] 参数：" + body.toString());
                return body;
            }
        } catch (Exception e) {
            LogUtil.logError("[" + ipaddress + "] --> 请求[" + servletPath + "] 参数解密异常：" + e.toString());
            return "";
        }
    }


    @Override
    public Object handleEmptyBody(@Nullable Object var1, HttpInputMessage var2, MethodParameter var3, Type var4, Class<? extends HttpMessageConverter<?>> var5) {
        return var1;
    }
}
