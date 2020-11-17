package com.ihbing.shilulib.http;


import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者:louis
 * 版本:1.0.0
 * 测试:No
 * 修改时间: 2018/12/18 19:28
 * 目的:
 * 更新日志:
 */

public class HttpManager {
    public static IHttp createInstance() {
        return new HttpUtil();
    }

    public static IHttp2 createIHttp2() {
        return new Http2Util();
    }

    public static String realUrl(String urlStr, @IHttp.RequestMethod String method, Map<String, Object> params) {
        //解析参数
        StringBuffer buffer = new StringBuffer();
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                buffer.append(key + "=" + URLEncoder.encode("" + params.get(key)));
                buffer.append("&");
            }
            if (buffer.charAt(buffer.length() - 1) == '&') {
                buffer.deleteCharAt(buffer.length() - 1);
            }
        }
        String fullUrl = urlStr + (TextUtils.isEmpty(buffer.toString()) ? "" : ("?" + buffer.toString()));
        try {
            URL url = new URL(fullUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setInstanceFollowRedirects(false);
            conn.setConnectTimeout(5000);
            conn.getResponseCode();
            String getUrl = conn.getHeaderField("Location");
            return getUrl;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return "";
        }
    }

    //整理header
    public static Map<String, Object> headers(String headers) {
        Map<String, Object> result = new HashMap<>();
        if (TextUtils.isEmpty(headers)) return result;
        try {
            String replaceReaders = headers.replaceAll("\r", "");//防止换行符为\r\n
            String splitHeaders[] = replaceReaders.split("\n");
            for (String header : splitHeaders) {
                String split[] = header.split(": ");
                result.put(split[0], split[1]);
            }
        } catch (Throwable throwable) {
            System.out.println("headers出现错误的数据：" + headers);
            throwable.printStackTrace();
        }
        return result;
    }

    //整理params
    public static Map<String, Object> params(String params) {
        Map<String, Object> result = new HashMap<>();
        if (TextUtils.isEmpty(params)) return result;
        try {
            String splitParams[] = params.split("&");
            for (String param : splitParams) {
                if(!TextUtils.isEmpty(param)) {
                    String split[] = param.split("=");
                    result.put(split[0], split.length == 2 ? split[1] : "");
                }
            }
        } catch (Throwable throwable) {
            System.out.println("params出现错误的数据：" + params);
            throwable.printStackTrace();
        }
        return result;
    }

    //整理params
    public static String params(Map<String, Object> params) {
        return params(params, true, "UTF-8");
    }

    public static String params(Map<String, Object> params, boolean isEncode, String charset) {
        //编码
        try {
            if (TextUtils.isEmpty(charset)){
                charset = "utf-8";
            }else {
                URLEncoder.encode("test", charset);
            }
        } catch (UnsupportedEncodingException e) {
            charset = "utf-8";
        }
        try {
            StringBuilder builder = new StringBuilder();
            for (String key : params.keySet()) {
                //对参数进行URLEncoder.encode编码，防止加号变空格
                String value = isEncode ? URLEncoder.encode(params.get(key).toString(), charset) : params.get(key).toString();
                builder.append(String.format("%s=%s&", key, value));
            }
            if (builder.toString().endsWith("&")) {
                return builder.toString().substring(0, builder.lastIndexOf("&"));
            }
            return builder.toString();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return "";
        }
    }
}
