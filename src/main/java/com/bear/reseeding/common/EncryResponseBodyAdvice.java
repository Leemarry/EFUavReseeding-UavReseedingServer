package com.bear.reseeding.common;

import com.alibaba.fastjson.JSONObject;
import com.bear.reseeding.utils.AESUtil;
import com.bear.reseeding.utils.LogUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 返回参数 加密操作
 *
 * @Auther: bear
 * @Date: 2021/7/16 14:26
 * @Description: null
 */
@Component
@ControllerAdvice(basePackages = "com.bear.reseeding.controller")
public class EncryResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 是否需要对接口返回数据进行 加密
     */
    @Value("${spring.config.needEncrypt:true}")
    boolean needEncrypt;

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
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object obj, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        //通过 ServerHttpRequest的实现类ServletServerHttpRequest 获得HttpServletRequest
        ServletServerHttpRequest sshr = (ServletServerHttpRequest) serverHttpRequest;
        //此处获取到request 是为了取到在拦截器里面设置的一个对象 是我项目需要,可以忽略
        HttpServletRequest request = sshr.getServletRequest();
        //String path = httpServletRequest.getRequestURI().substring(httpServletRequest.getContextPath().length());
        String servletPath = request.getServletPath(); // //index.html
        String ipaddress = request.getHeader("Host");
//        boolean needEncryptionClientSay = true;
//        String Encryption = request.getHeader("Encryption"); //客户端发送的 是否需要加密解密
//        if (Encryption != null && Encryption.trim().toLowerCase().equals("true")) {
//            needEncryptionClientSay = true;
//        }
        String ClientId = request.getHeader("ClientId"); //唯一的ID
        Object responseMessage = new Object();
        try {
            //添加header，告诉前端数据已加密
//            serverHttpResponse.getHeaders().add("Encryption", (needEncrypt && needEncryptionClientSay) ? "true" : "false");
            if (needEncrypt && obj != null) {
                String responseMessageSource = JSONObject.toJSONString(obj);
                responseMessage = AESUtil.encryptAES(responseMessageSource, secretKey, secretIv);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("data", responseMessage);
                responseMessage = jsonObject;
                LogUtil.logMessage("[" + ipaddress + "] <-- 返回请求[" + servletPath + "] 数据：" + responseMessageSource);
              /* //RSA
                String responseMessageSource = JSONObject.toJSONString(obj);
                LogUtil.logInfo("[" + ipaddress + "] <-- 返回请求[" + servletPath + "] 数据：" + responseMessageSource);
                //使用客户端的公钥进行加密AES
                String ClientRsaPublickKeyString = ParkingapronApplication.MapClientPublicKeys.getOrDefault(ClientId, "");
                PublicKey ClientRsaPublickKey = RSAUtil.string2PublicKey(ClientRsaPublickKeyString);
                //接口返回数据统一加密
                String AesKey = RSAUtil.EncryptionByPublicKey(secretKey, ClientRsaPublickKey); //AesKey进行RSA加密后传输
                String AesIv = RSAUtil.EncryptionByPublicKey(secretIv, ClientRsaPublickKey);//AesIv进行RSA加密后传输
                responseMessage = AESUtil.encryptAES(responseMessageSource, secretKey, secretIv);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("AesKey", AesKey);
                jsonObject.put("AesIv", AesIv);
                jsonObject.put("Data", responseMessage); //加密后的内容段数据
                responseMessage = jsonObject.toJSONString();*/
            } else {
                responseMessage = obj;
                LogUtil.logMessage("[" + ipaddress + "] <-- 返回请求[" + servletPath + "] 数据：" + (responseMessage != null ? responseMessage.toString() : "空"));
            }
            //log.info("接口={},原始数据={},加密后数据={}", request.getRequestURI(), srcData, returnStr);
        } catch (Exception e) {
            LogUtil.logError("[" + ipaddress + "] <-- 请求[" + servletPath + "] 接口加密异常：" + e);
        }
        return responseMessage;
    }
}
