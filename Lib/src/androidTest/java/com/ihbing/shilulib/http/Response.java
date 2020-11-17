package com.ihbing.shilulib.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者:louis
 * 版本:1.0.0
 * 测试:No
 * 修改时间: 2019/1/14 8:39
 * 目的:
 * 更新日志:
 * 问题:
 */

public class Response {
    public byte[] data;
    public Throwable error;
    public int status;
    public Map<String,List<String>> headers=new HashMap<>();
}
