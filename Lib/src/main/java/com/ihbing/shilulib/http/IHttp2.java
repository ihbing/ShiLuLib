package com.ihbing.shilulib.http;

import java.net.Proxy;

/**
 * 作者:louis
 * 版本:1.0.0
 * 测试:No
 * 修改时间: 2019/1/14 15:16
 * 目的:
 * 更新日志:
 * 问题:
 */

public interface IHttp2 {
    Response request(Proxy proxy, Request request, int timeout);

    void asyncRequest(final Proxy proxy, Request request, int timeout, final HttpCallBack callBack);


    interface HttpCallBack {
        void onSuccess(Response response);
    }
}
