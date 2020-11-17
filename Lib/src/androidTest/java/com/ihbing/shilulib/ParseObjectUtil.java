package com.ihbing.shilulib;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 作者:louis
 * 版本:1.0.0
 * 测试:No
 * 修改时间: 2019/2/3 21:42
 * 目的:解析对象为Json数据
 * 更新日志:
 * 问题:1.对map键值对的解析
 *     2.对非基本类型的解析
 *     3.List集合的解析
 *     4.对set集合的解析
 *     5.如何获取所有变量，
 *     6.d对基本类型的判断
 *     7.获取类型名
 *     8.只解析公共的，不解析私有的
 *     9.获取list对象类型
 */

public  class ParseObjectUtil {
    private static ParseObjectUtil 单例;
    public static ParseObjectUtil 获取单例(){
        if(单例==null){
            单例=new ParseObjectUtil();
        }
        return 单例;
    }

    public synchronized String toJson(Object 对象){
        String 返回值 ="";
        try{
            JSONObject json数据 =new JSONObject();
            json数据.put("对象类名", 对象.getClass().getName());
            Object[] 对象全部变量=获取变量集合(对象);
            for(Object 单个变量:对象全部变量){
                if(是基本类型(单个变量)){
                    json数据.put(单个变量.getClass().getName(),单个变量);
                }else if(是list类型(单个变量)){
                   json数据.put(单个变量.getClass().getName(),处理List变量((List) 单个变量));
                }else if(是Map类型(单个变量)){
                    json数据.put(单个变量.getClass().getName(),处理Map变量((Map) 单个变量));
                }else if(是Set类型(单个变量)){
                    json数据.put(单个变量.getClass().getName(), 处理Set变量((Set) 单个变量));
                }

            }
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
        return 返回值;
    }

    private String 处理Set变量(Set 单个变量) {

        return "";
    }

    private boolean 是Set类型(Object 单个变量) {
        return Set.class.isAssignableFrom(单个变量.getClass());
    }

    private String 处理Map变量(Map 单个变量) {
        return "";
    }

    private boolean 是Map类型(Object 单个变量) {
        return Map.class.isAssignableFrom(单个变量.getClass());
    }

    private synchronized String 处理List变量(List 单个变量) {
        String result="";
        try {
            JSONObject jsonObject = new JSONObject();
           for(int i=0;i<单个变量.size();i++){
               jsonObject.put("list"+i,toJson(单个变量.get(i)));
           }
           result=jsonObject.toString(2);
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
        return result;
    }
    //
    private synchronized boolean 是list类型(Object 变量){
        return List.class.isAssignableFrom(变量.getClass());
    }
    //判断是否是基本类型
    private synchronized boolean 是基本类型(Object 变量){
        return 变量.getClass().isPrimitive();
    }

    //获取所有变量合集
    private synchronized Object[] 获取变量集合(Object 对象){

        return null;
    }
}
