package com.ihbing.shilulib;

import java.io.Closeable;
import java.io.IOException;

/**
 * 作者:louis
 * 版本:1.0.0
 * 测试:No
 * 修改时间: 2019/6/6 9:15
 * 目的:
 * 更新日志:
 * 问题:
 */

public class IOUtil {
    public static void  close(Closeable cloneable){
        if(cloneable!=null){
            try {
                cloneable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
