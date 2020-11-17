package com.ihbing.shilulib.encrypt;

/**
 * 作者:louis
 * 版本:1.0.0
 * 测试:No
 * 修改时间: 2019/3/9 9:17
 * 目的:
 * 更新日志:
 * 问题:
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSAUtil{

  //  private String priKey="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDrBqRsI/v+el2sMNbAuUKhrqAKPoQIxN5Sa0fd1o3d9xjIrNHd2hBfQUPvkedshc7Qxr5n6JlcLBuedK4Yi6YaLqzSBXB45JZsVwxpSvoHLI34w9PRFxizdlCwl0dQIzyAIfnKB8cLuINVxclMJNB8u4QpJQMZ+6puu3hQKgFIukPeY/O3yUllCBVNLW/yz76xR2SaD7mb11ooCzuSQYmdwXenh5HMEOa7PMUEuGnFZuXEbyxaxpqTIhHK858vaG5hrmZD48CrZiU4d5gO9WLETs20tdZ6a1rovcQoDBXBCST/XxSteoYnG9QvkhpS1lYwk+a1E/S9084DyOwaWoHlAgMBAAECggEAZul0q+mh2U+JnvGPx7oXhCar63BubkyOMTbKtEfTvSMK1ixehS+MRbmVXtzbojiBVAgCrgs6xBKIUX00EolrxUE06Y36LuWhPHVteNmc2/FVhV1ybbcYWNTxBjJnxp53SoAWGbIIJYi48aD2wQHJzSRq/X04e3MosO4kA64w+7/FNmsIfMSgPpD4Yf5FTvrHedDoEajajGcpRO9HHLYvhSiisM2yxJmuT1frl1SHOzSM40cSLFO63IfctOK7Q6Z0aqY7Ks2PW5sFYjkh2ou2BkicncNvLHkkM6ClFa6xNz9y3u1+gXT0OZxe0DYE+RRaGER/hJ+n6zcMFWQ5d9OX7QKBgQD9HQxi5R3pgrMnw4HB+UUDhyMSs0zzd3qfH4A0kwhMm6pfGw4GtOYCVgAfXKlPngzeyFD0yF2zeMAlHZOdBKwgWiwOFifVVelg79pUotfVLz0UM2XT5ScuNDVZybsHvfcUY+7MpsIJhQjfec6Yx62GlMEnyq2cdUkstTd5TxIm2wKBgQDttMnOZWE5jLwdentKXzPWqYApF7gFx9svjlB9GXT82w0rG+Zao5Xzj4KvUMJQ3gibO2ucH6B1sxmcRsFXhs9AgSW2augH+iTcgF1ap6pl+6KE16N7hekq57+etCwdWO1hqsUq8eU6OwiYVsSFd8WJ8NC0br9DMh1tkkGipQ52PwKBgHuhk6o+dTTYpT937EpdwgruGACLWbvWESvmDA4h/zzEEByyL1CxTWO4tAidjwmXjnvG5lwZ/kKqaVf3sWFRpe2LfddHlR9L/lld7ovGmpvDnhbe4A728ANOdvyrX1JxxFzOTxbhIQfbVjEp4vKXHbgNsqcIBXLViu9ueHElYR1fAoGBAJG+BPWtJv1YEoaocCtbT7dP2apYRpYUwYjY4vSwxQ+7ZsmFo6jzH59j4CaJh3p5iCFVP1dL9N/1XcB5iVWW7D+Kb4r25Sju7+baNWK95jX9INn+NNgpdJq/2Lb/lQSxX2pAf/8irP8U7uIoYPnMQ6udoPjf5hjXA0AJKoEEXmeVAoGACR0n2/KpLwgnmOHzCkiKSVHcSw5fTFPs3l8YyGd4zyMehpYqxbzKUo2rvFodVceSZJLOKHE/LjGHz/wKae42lyMXwFeqi8P8RTR5SQJevw8D9J9MZEVZedYVYm1IQCZl6z5KLsPOS6ilhx9G5vgbQlk2iWx+dJoMi6111jLsQg0=";
    private String pubKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6wakbCP7/npdrDDWwLlCoa6gCj6ECMTeUmtH3daN3fcYyKzR3doQX0FD75HnbIXO0Ma+Z+iZXCwbnnSuGIumGi6s0gVweOSWbFcMaUr6ByyN+MPT0RcYs3ZQsJdHUCM8gCH5ygfHC7iDVcXJTCTQfLuEKSUDGfuqbrt4UCoBSLpD3mPzt8lJZQgVTS1v8s++sUdkmg+5m9daKAs7kkGJncF3p4eRzBDmuzzFBLhpxWblxG8sWsaakyIRyvOfL2huYa5mQ+PAq2YlOHeYDvVixE7NtLXWemta6L3EKAwVwQkk/18UrXqGJxvUL5IaUtZWMJPmtRP0vdPOA8jsGlqB5QIDAQAB";

/*
    public static void main(String[] args) {
        RSAUtil rsa = new RSAUtil();
        String str = "我要加密这段文字。2";
        System.out.println("原文:"+"我要加密这段文字。2");
        String crypt = rsa.privateEncrypt(str);
        System.out.println("私钥加密密文:"+crypt);
        String result = rsa.publicDecrypt(crypt);
        System.out.println("原文:"+result);

        System.out.println("---");

        str = "我要加密这段文字。2";
        System.out.println("原文:"+"我要加密这段文字。2");
        crypt = rsa.publicEncrypt(str);
        System.out.println("公钥加密密文:"+crypt);
        result = rsa.privateDecrypt(crypt);
        System.out.println("原文:"+result);

        System.out.println("---");

        str = "我要签名这段文字。2";
        System.out.println("原文："+str);
        String str1 = rsa.signByPrivateKey(str);
        System.out.println("签名结果："+str1);
        if(rsa.verifyByPublicKey(str1, str)){
            System.out.println("成功");
        } else {
            System.out.println("失败");
        }
    }
*/

    /**
     * 本方法使用SHA1withRSA签名算法产生签名
     * @param src 签名的原字符串
     * @return String 签名的返回结果(16进制编码)。当产生签名出错的时候，返回null。
     */
/*
    public String signByPrivateKey(String src) {
        try {
            Signature sigEng = Signature.getInstance("SHA1withRSA");
            byte[] pribyte = base64decode(priKey.trim());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pribyte);
            KeyFactory fac = KeyFactory.getInstance("RSA");
            RSAPrivateKey privateKey = (RSAPrivateKey) fac.generatePrivate(keySpec);
            sigEng.initSign(privateKey);
            sigEng.update(src.getBytes());
            byte[] signature = sigEng.sign();
            return base64encode(signature);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
*/

    /**
     * 使用共钥验证签名
     * @param sign
     * @param src
     * @return
     */
    public boolean verifyByPublicKey(String sign, String src) {
        try {
            Signature sigEng = Signature.getInstance("SHA1withRSA");
            byte[] pubbyte = base64decode(pubKey.trim());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubbyte);
            KeyFactory fac = KeyFactory.getInstance("RSA");
            RSAPublicKey rsaPubKey = (RSAPublicKey) fac.generatePublic(keySpec);
            sigEng.initVerify(rsaPubKey);
            sigEng.update(src.getBytes());
            byte[] sign1 = base64decode(sign);
            return sigEng.verify(sign1);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *  base64加密
     * @param bstr
     * @return
     */
    @SuppressWarnings("restriction")
    private String base64encode(byte[] bstr) {
        //  String str =  new sun.misc.BASE64Encoder().encode(bstr);
        String str = Base64Util.encode(bstr);
        str = str.replaceAll("\r\n", "").replaceAll("\r", "").replaceAll("\n", "");
        return str;
    }

    /**
     * base64解密
     * @param str
     * @return byte[]
     */
    @SuppressWarnings("restriction")
    private byte[] base64decode(String str) {
        byte[] bt = null;
        try {
/*
            sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
            bt = decoder.decodeBuffer(str);
*/
            bt=Base64Util.decode(str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bt;
    }

    /**
     * 从文件中读取所有字符串
     * @param fileName
     * @return	String
     */
    private String readStringFromFile(String fileName){
        StringBuffer str = new StringBuffer();
        try {
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            char[] temp = new char[1024];
            while (fr.read(temp) != -1) {
                str.append(temp);
            }
            fr.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return str.toString();
    }

    public String publicEncrypt(String data) {
        // 加密
        String str = "";
        try {
            byte[] pubbyte = base64decode(pubKey.trim());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubbyte);
            KeyFactory fac = KeyFactory.getInstance("RSA");
            RSAPublicKey rsaPubKey = (RSAPublicKey) fac.generatePublic(keySpec);
            Cipher c1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            c1.init(Cipher.ENCRYPT_MODE, rsaPubKey);
            str = base64encode(c1.doFinal(data.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();

        }
        return str;
    }

/*
    public String privateDecrypt(String data) {
        // 加密
        String str = "";
        try {
            byte[] pribyte = base64decode(priKey.trim());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pribyte);
            KeyFactory fac = KeyFactory.getInstance("RSA");
            RSAPrivateKey privateKey = (RSAPrivateKey) fac.generatePrivate(keySpec);
            Cipher c1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            c1.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] temp = c1.doFinal(base64decode(data));
            str = new String(temp);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return str;
    }
*/

/*
    public String privateEncrypt(String data) {
        // 加密
        String str = "";
        try {
            byte[] pribyte = base64decode(priKey.trim());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pribyte);
            KeyFactory fac = KeyFactory.getInstance("RSA");
            RSAPrivateKey privateKey = (RSAPrivateKey) fac.generatePrivate(keySpec);
            Cipher c1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            c1.init(Cipher.ENCRYPT_MODE, privateKey);
            str = base64encode(c1.doFinal(data.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();

        }
        return str;
    }
*/

    public String publicDecrypt(String data) {
        // 加密
        String str = "";
        try {
            byte[] pubbyte = base64decode(pubKey.trim());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubbyte);
            KeyFactory fac = KeyFactory.getInstance("RSA");
            RSAPublicKey rsaPubKey = (RSAPublicKey) fac.generatePublic(keySpec);
            Cipher c1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            c1.init(Cipher.DECRYPT_MODE, rsaPubKey);
            byte[] temp = c1.doFinal(base64decode(data));
            str = new String(temp);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return str;
    }
}

