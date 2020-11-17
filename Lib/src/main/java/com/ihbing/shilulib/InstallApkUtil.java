package com.ihbing.shilulib;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import louis.framework.provider.FileProvider;

/**
 * 作者:louis
 * 版本:1.0.0
 * 测试:No
 * 修改时间: 2018/12/14 22:17
 * 目的:
 * 更新日志:
 */

public class InstallApkUtil {
    /**
     * 安装apk
     *
     * @param context
     * @param apkPath
     */
    public static void installApk(Context context, String apkPath) {
        try {
            /**
             * provider
             * 处理android 7.0 及以上系统安装异常问题
             */
            File file = new File(apkPath);
            Intent install = new Intent();
            install.setAction(Intent.ACTION_VIEW);
            install.addCategory(Intent.CATEGORY_DEFAULT);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (context.getApplicationInfo().targetSdkVersion> Build.VERSION_CODES.M&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri apkUri = FileProvider.getUriForFile(context, "com.chao.app.fileprovider", file);//在AndroidManifest中的android:authorities值
                Log.d("======", "apkUri=" + apkUri); install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
                install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
            context.startActivity(install);
        } catch (Exception e) {
            Log.d("======", e.getMessage());
            Toast.makeText(context, "文件解析失败", Toast.LENGTH_SHORT).show();
        }
    }
}
