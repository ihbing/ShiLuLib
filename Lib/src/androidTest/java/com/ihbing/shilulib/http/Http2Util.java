package com.ihbing.shilulib.http;

import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 作者:louis
 * 版本:1.0.0
 * 测试:No
 * 修改时间: 2019/1/14 15:19
 * 目的:
 * 更新日志:
 * 问题:
 */

class Http2Util implements IHttp2 {


    private static final String END = "\r\n";
    private static final String POST = "POST";
    private static final String GET = "GET";
    //线程管理
    private static final int count = 5;
    private static ExecutorService executor = Executors.newFixedThreadPool(count);

    private static void execute(Runnable runnable) {
        if (runnable != null) executor.execute(runnable);
    }
    @Override
    public Response request(Proxy proxy, Request request, int timeout) {
        Response response=new Response();
        try {
            HttpURLConnection httpURLConnection = null;
            String paramsStr="";
            String httpUrl=request.url;
            Map<String,Object> headers=request.headers;
            Map<String,Object> params=request.params;
            String method=request.method;
            switch (method){//参考：http://www.blogjava.net/supercrsky/articles/247449.html
                case POST:
                    //解析参数
                    if(httpUrl.contains("?")){
                        paramsStr=new URL(httpUrl).getQuery();
                        httpUrl=new URL(httpUrl).getPath();
                    }else if(params!=null&&!params.isEmpty()){
                        paramsStr=HttpManager.params(params);
                    }
                    //构造请求
                    if (proxy == null) {
                        httpURLConnection = (HttpURLConnection)new URL(httpUrl).openConnection();
                    } else {
                        httpURLConnection = (HttpURLConnection)new URL(httpUrl).openConnection(proxy);
                    }
                    httpURLConnection.setRequestMethod(method);
                    if (headers != null && !headers.isEmpty()) {
                        for (String key : headers.keySet()) {
                            Object value = headers.get(key);
                            httpURLConnection.setRequestProperty(key, value == null ? "" : ("" + value));
                        }
                    }
                    // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在 
                    //  http正文内，因此需要设为true, 默认情况下是false; 
                    httpURLConnection.setDoOutput(true);
                    // 设置是否从httpUrlConnection读入，默认情况下是true; 
                    httpURLConnection.setDoInput(true);
                    // Post 请求不能使用缓存 
                    httpURLConnection.setUseCaches(false);
                    OutputStream outputStream=httpURLConnection.getOutputStream();
                    outputStream.write(paramsStr.getBytes());
                    outputStream.flush();
                    outputStream.close();
                    break;
                case GET:
                    if(params!=null&&!params.isEmpty()){
                        paramsStr=HttpManager.params(params);
                    }
                    if(!TextUtils.isEmpty(paramsStr)) {
                        if (httpUrl.contains("?")) {
                            if (httpUrl.endsWith("&")) {
                                httpUrl += paramsStr;
                            } else {
                                httpUrl += "&" + paramsStr;
                            }
                        } else {
                            httpUrl += "?" + paramsStr;
                        }
                    }
                    if (proxy == null) {
                        httpURLConnection = (HttpURLConnection) new URL(httpUrl).openConnection();
                    } else {
                        httpURLConnection = (HttpURLConnection) new URL(httpUrl).openConnection(proxy);
                    }
                    httpURLConnection.setRequestMethod(method);
                    if (headers != null && !headers.isEmpty()) {
                        for (String key : headers.keySet()) {
                            Object value = headers.get(key);
                            httpURLConnection.setRequestProperty(key, value == null ? "" : ("" + value));
                        }
                    }
                    break;
            }
            httpURLConnection.setConnectTimeout(timeout);
            httpURLConnection.setReadTimeout(timeout);
            // httpURLConnection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
            httpURLConnection.connect();
            int code=httpURLConnection.getResponseCode();
            response.status=code;
            response.headers=httpURLConnection.getHeaderFields();
            if(code==302||code==301){
                String redirectUrl = httpURLConnection.getHeaderField("Location");
                if(redirectUrl != null && !redirectUrl.isEmpty()) {
                    System.out.println("Location:"+redirectUrl);
                }
            }
            if(code==200) {
                InputStream inputStream = httpURLConnection.getInputStream();
                ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                byte[] buff = new byte[100];
                int rc = 0;
                while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                    swapStream.write(buff, 0, rc);
                }
                response.data=swapStream.toByteArray();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            response.error=e;
        }
        return response;
    }
    @Override
    public void asyncRequest(final Proxy proxy, final Request request, final int timeout, final HttpCallBack callBack) {
        execute(new Runnable() {
            @Override
            public void run() {
                Response result = request(proxy, request,timeout);
                if (callBack != null) {
                    callBack.onSuccess(result);
                }
            }
        });
    }

    public static void test() {
        execute(new Runnable() {
            @Override
            public void run() {
                IHttp2 http2=new Http2Util();
                Map<String, Object> params = new HashMap<>();
                params.put("page", 1);
                params.put("count", 20);
                Log.e("HttpUtil", "result:" +http2.request(null,null,5000));
            }
        });
    }

}
