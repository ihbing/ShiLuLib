package com.ihbing.shilulib.http;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 作者:louis
 * 版本:1.0.0
 * 测试:No
 * 修改时间: 2018/11/19 17:09
 * 目的:
 * 更新日志:
 */
class HttpUtil implements IHttp {
    private static final String END = "\r\n";
    private static final String POST = "POST";
    private static final String GET = "GET";
    //不验证证书
    public final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
    //线程管理
    private static final int count = 5;
    private static ExecutorService executor = Executors.newFixedThreadPool(count);

    private static void execute(Runnable runnable) {
        if (runnable != null) executor.execute(runnable);
    }

    @Override
    public Object request(Proxy proxy, @RequestMethod String method, @NonNull String httpUrl, Map<String, Object> headers, Map<String, Object> params, String charset) {
        Object resultError=0;
        try {
            HttpURLConnection httpURLConnection = null;
            URL url=new URL(httpUrl);
            String protocol = url.getProtocol();
            String host = url.getHost();
            String path = url.getPath();
            String query =url.getQuery();
            int port = url.getPort();
            String originalQuery="";//原始未编码参数
            if (params != null && !params.isEmpty()) {
                if (!TextUtils.isEmpty(query) && !"null".equals(query.toLowerCase())) {
                    query += "&" + HttpManager.params(params,false,charset);
                } else {
                    query = HttpManager.params(params,false,charset);
                }
            }
            if (!TextUtils.isEmpty(query)) {
                if(!"null".equals(query.toLowerCase())) {
                    originalQuery = query;
                    query = HttpManager.params(HttpManager.params(query), true,charset);
                }else {
                    query ="";
                }

            }

            switch (method) {//参考：http://www.blogjava.net/supercrsky/articles/247449.html
                case POST:
                    //构造请求
                    httpURLConnection = getHttpConnection(proxy, new URL(protocol, host, port, path).toString());
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
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    if (headers != null && "application/json".equals(headers.get("Content-Type"))) {
                        JSONObject json = new JSONObject();
                        Map<String,Object> originalQueryMaps=HttpManager.params(originalQuery);
                        for (String key : originalQueryMaps.keySet()) {
                            json.put(key, originalQueryMaps.get(key));
                        }
                        outputStream.write(json.toString().getBytes());
                    } else {
                        outputStream.write(query.getBytes());
                    }
                    outputStream.flush();
                    outputStream.close();
                    break;
                case GET:
                    String file;
                    if(TextUtils.isEmpty(query)){
                        file=path;
                    }else {
                        file=path+"?"+query;
                    }
                    httpURLConnection = getHttpConnection(proxy, new URL(protocol, host, port, file).toString());
                    httpURLConnection.setRequestMethod(method);
                    if (headers != null && !headers.isEmpty()) {
                        for (String key : headers.keySet()) {
                            Object value = headers.get(key);
                            httpURLConnection.setRequestProperty(key, value == null ? "" : ("" + value));
                        }
                    }
                    break;
            }
            httpURLConnection.connect();
            int code = httpURLConnection.getResponseCode();

            if (code == 302 || code == 301) {
                String redirectUrl = httpURLConnection.getHeaderField("Location");
                if (redirectUrl != null && !redirectUrl.isEmpty()) {
                    System.out.println("Location:" + redirectUrl);
                }
            }
            if (code == 200) {
                InputStream inputStream = httpURLConnection.getInputStream();
                ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                byte[] buff = new byte[100];
                int rc = 0;
                while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                    swapStream.write(buff, 0, rc);
                }
                return swapStream.toByteArray();
            }
            resultError =code;
        } catch (Throwable e) {
            resultError =e;
        }
        return resultError;
    }

    @Override
    public void asyncRequest(final Proxy proxy, @RequestMethod final String method, final String httpUrl, final Map<String, Object> headers, final Map<String, Object> params, final String charset, final HttpCallBack callBack) {
        execute(new Runnable() {
            @Override
            public void run() {
                Object result = request(proxy, method, httpUrl, headers, params,charset);
                if (callBack != null) {
                    if (result instanceof byte[]) {
                        callBack.onSuccess((byte[]) result);
                    }else if (result instanceof Throwable) {
                        callBack.onError((Throwable) result);
                    }else {
                        callBack.onError(new Throwable("[-] http request error:"+result));
                    }

                }
            }
        });
    }

    private HttpURLConnection getHttpConnection(Proxy proxy, String httpUrl) throws IOException {
        HttpURLConnection connection = null;
        URL url = new URL(httpUrl);
        if (proxy == null) {
            //关键代码
            //ignore https certificate validation |忽略 https 证书验证
            if (url.getProtocol().toUpperCase().equals("HTTPS")) {
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);
                connection = https;
            } else {
                connection = (HttpURLConnection) url.openConnection();
            }
        } else {
            if (url.getProtocol().toUpperCase().equals("HTTPS")) {
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection(proxy);
                https.setHostnameVerifier(DO_NOT_VERIFY);
                connection = https;
            } else {
                connection = (HttpURLConnection) url.openConnection(proxy);
            }
        }
        return connection;
    }

    public static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        // Android use X509 cert
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void test() {
        execute(new Runnable() {
            @Override
            public void run() {
                IHttp http = new HttpUtil();
                Map<String, Object> params = new HashMap<>();
                params.put("page", 1);
                params.put("count", 20);
                Log.e("HttpUtil", "result:" + http.request(null, IHttp.GET, "https://api.apiopen.top/getSongPoetry/", null, params,"utf-8"));
            }
        });
    }


}
