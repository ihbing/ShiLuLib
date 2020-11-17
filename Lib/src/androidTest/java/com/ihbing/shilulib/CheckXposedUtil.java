package com.ihbing.shilulib;

import android.text.TextUtils;
import android.util.Log;

import java.util.Map;

import louis.framework.Constant;

/**
 * 作者:louis
 * 版本:1.0.0
 * 测试:No
 * 修改时间: 2019/6/6 10:46
 * 目的:
 * 更新日志:
 * 问题:
 */

public class CheckXposedUtil {
    public static final String TAG = "CheckXposedUtil";
    public static boolean checkXposedWithEnv(){
       Map<String,String> envMap= System.getenv();
       if(MapUtil.isEmpty(envMap))return false;
       if(envMap.containsKey(Constant.CLASS_PATH)){
           String classPath=envMap.get(Constant.CLASS_PATH);
           if(BuildConfig.DEBUG) Log.i(TAG,"classPath:"+classPath);
           return !TextUtils.isEmpty(classPath)&&classPath.toLowerCase().contains("xposed");
       }
       return false;
    }
}
