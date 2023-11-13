package com.bear.reseeding.utils;


import org.springframework.core.io.ClassPathResource;

import javax.crypto.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA 是非对称的密码算法，密钥分公钥和私钥, 可以使用一方加密另一方解密，不过由于私钥长度往往很长，
 * 考虑到对网络资源的消耗，一般就公开公钥，使用公钥加密，私钥进行解密，所以这里只提供这种模式需要
 * 的方法。
 * 对外提供密钥对生成、密钥转换、公钥加密、私钥解密方法
 *
 * @Auther: bear
 * @Date: 2021/5/28 12:02
 * @Description: null
 */
public class RSAUtil {
    /**
     * 根据pem公钥路径获取公钥字符串
     *
     * @param cerPath 路径
     * @return 公钥字符串
     */
    public static String getPubKeyContentString(String cerPath) {

        // 读取本机存放的PKCS8证书文件
        String currentPath = Thread.currentThread().getContextClassLoader().getResource(cerPath).getPath();
        try {
            //读取pem证书
            BufferedReader br = new BufferedReader(new FileReader(currentPath));
            StringBuffer publickey = new StringBuffer();
            String line;
            while (null != (line = br.readLine())) {
                if ((line.contains("BEGIN PUBLIC KEY") || line.contains("END PUBLIC KEY")))
                    continue;
                publickey.append(line);
            }
            return publickey.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取 PEM 证书
     *
     * @param name
     * @return
     */
    public static PrivateKey readRsaPrivateKey(String name) {
        try {
            if (name == null || name.equals("")) {
                return null;
            }
            ClassPathResource classPathResource = new ClassPathResource(name);
            InputStream inputStream = classPathResource.getInputStream();
            int size = inputStream.available();
            byte[] bytes = new byte[size];
            int read = inputStream.read(bytes);
            String keys = new String(bytes);
            keys = keys.replace("-----BEGIN ENCRYPTED PRIVATE KEY-----\r\n", "");
            keys = keys.replace("-----BEGIN PRIVATE KEY-----\r\n", "");
            keys = keys.replace("-----END ENCRYPTED PRIVATE KEY-----", "");
            keys = keys.replace("-----END PRIVATE KEY-----", "");
            keys = keys.replace("\r\n", "");
            keys = keys.replace("\r", "");
            keys = keys.replace("\n", "");
            byte[] encoded = Base64.getDecoder().decode(keys);
            inputStream.close();
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
            //String text = "lnEo7uC6m/VK6vZwkeiCke82L5LkewHwcsozeDws5Ov177EedxuNtWI3Q6uu1sSe30LMCwoh+mKWghjk5+Lwriu0vtGIczvb8WWXF7BAelYv92TiVQ0RbGnKGDF3XdKww2ENUzOYdAJFemsLNV6SsJpusmxCixGz5ne+QPQl3FSmVUOA9XCEbQTmlv3o/DoiI2TpLtaInPsGbFQDIm9p4AV5KD4E1cflaLIZSRiLMa7rXob3D1ap34AQjLZ7Cxlsei2GWOSxpg8dYlGz8LXI88EO1AK6E3sMWIPDVsCullpl0MJrVs+oumpAbW+5wJjmH2uoDqWWzKb+bi8IR67CBw==";
            //String loginName = RSAUtil.DecryptByPrivateKey(text, privateKey);
            return privateKey;
        } catch (IOException e) {
            LogUtil.logError("读取RSA密匙失败（IO错误）：" + e.toString());
            return null;
        } catch (Exception e) {
            LogUtil.logError("读取RSA密匙失败：" + e.toString());
            return null;
        }
    }

    /**
     * 生成密钥对：密钥对中包含公钥和私钥
     *
     * @return 包含 RSA 公钥与私钥的 keyPair
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static KeyPair getKeyPair() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");    // 获得RSA密钥对的生成器实例
        SecureRandom secureRandom = new SecureRandom(String.valueOf(System.currentTimeMillis()).getBytes("utf-8")); // 说的一个安全的随机数
        keyPairGenerator.initialize(2048, secureRandom);    // 这里可以是1024、2048 初始化一个密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();   // 获得密钥对
        return keyPair;
    }

    /**
     * 获取公钥 (并进行 Base64 编码，返回一个 Base64 编码后的字符串)
     *
     * @param keyPair：RSA 密钥对
     * @return 返回一个 Base64 编码后的公钥字符串
     */
    public static String getPublicKey(KeyPair keyPair) {
        PublicKey publicKey = keyPair.getPublic();
        byte[] bytes = publicKey.getEncoded();
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 获取私钥(并进行Base64编码，返回一个 Base64 编码后的字符串)
     *
     * @param keyPair：RSA 密钥对
     * @return 返回一个 Base64 编码后的私钥字符串
     */
    public static String getPrivateKey(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] bytes = privateKey.getEncoded();
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 将 Base64 编码后的公钥转换成 PublicKey 对象
     *
     * @param pubStr：Base64 编码后的公钥字符串
     * @return PublicKey
     */
    public static PublicKey string2PublicKey(String pubStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] bytes = Base64.getDecoder().decode(pubStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 将 Base64 码后的私钥转换成 PrivateKey 对象
     *
     * @param priStr：Base64 编码后的私钥字符串
     * @return PrivateKey
     */
    public static PrivateKey string2Privatekey(String priStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] bytes = Base64.getDecoder().decode(priStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 公钥加密
     *
     * @param content   待加密的内容 byte[]
     * @param publicKey 加密所需的公钥对象 PublicKey
     * @return 加密后的字节数组 byte[]
     */
    public static byte[] publicEncrytype(byte[] content, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytes = cipher.doFinal(content);
        return bytes;
    }

    /**
     * 私钥解密
     *
     * @param content    待解密的内容 byte[]，这里要注意，由于我们中间过程用的都是 BASE64 ，所以在传入参数前应先进行 BASE64 解析
     * @param privateKey 解密需要的私钥对象 PrivateKey
     * @return 解密后的字节数组 byte[]，这里是元数据，需要根据情况自行转码
     */
    public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytes = cipher.doFinal(content);
        return bytes;
    }


    /**
     * 解密内容
     *
     * @param message    Base64编码后的字符串
     * @param privateKey 私钥 PrivateKey类型
     * @return 解密后的字符串
     */
    public static String DecryptByPrivateKey(String message, PrivateKey privateKey) {
        String result = "";
        try {
            byte[] textSource = Base64.getDecoder().decode(message);  //base64解码
            byte[] temp = new byte[0]; //RSA解密
            temp = privateDecrypt(textSource, privateKey);
            result = new String(temp, StandardCharsets.UTF_8); //转为字符串
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (Exception e) {
        } finally {
            return result;
        }
    }


    /**
     * 加密内容
     *
     * @param message   需要解密的内容
     * @param publicKey 公钥 PublicKey类型
     * @return 加密之后的Base64编码后的字符串
     */
    public static String EncryptionByPublicKey(String message, PublicKey publicKey) {
        String encryptedBase64Str = message;
        byte[] textSource = message.getBytes(); //Base64.getDecoder().decode(message);  //base64解码
        byte[] temp = new byte[0]; //RSA解密
        try {
            temp = publicEncrytype(textSource, publicKey);
            encryptedBase64Str = Base64.getEncoder().encodeToString(temp);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return encryptedBase64Str; //转为Base64
    }
}