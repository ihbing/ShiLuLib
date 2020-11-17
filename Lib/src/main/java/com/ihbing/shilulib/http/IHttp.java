package com.ihbing.shilulib.http;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.Proxy;
import java.util.Map;

/**
 * 作者:louis
 * 版本:1.0.0
 * 测试:No
 * 修改时间: 2018/12/18 19:27
 * 目的:
 * 更新日志:
 */

public interface IHttp {
    //请求方法管理
    String POST = "POST";
    String GET = "GET";

    @StringDef({POST, GET})
    @Retention(RetentionPolicy.SOURCE)
    @interface RequestMethod {
    }

    Object request(Proxy proxy, @RequestMethod String method, String httpUrl, Map<String, Object> headers, Map<String, Object> params, String charset);

    void asyncRequest(final Proxy proxy, @RequestMethod final String method, final String httpUrl, final Map<String, Object> headers, final Map<String, Object> params, String charset, final HttpCallBack callBack);

    interface HttpCallBack {
        void onSuccess(byte[] data);

        void onError(Throwable error);
    }
}
