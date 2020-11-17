package com.ihbing.shilulib.http;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者:louis
 * 版本:1.0.0
 * 测试:No
 * 修改时间: 2019/1/14 8:52
 * 目的:
 * 更新日志:
 * 问题:
 */

public class Request {
    public String method;
    public String url;
    public Map<String,Object> headers=new HashMap<>();
    public Map<String,Object> params=new HashMap<>();

}
