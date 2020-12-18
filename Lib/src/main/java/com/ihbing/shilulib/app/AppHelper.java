package com.ihbing.shilulib.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.ihbing.shilulib.ShellUtil;
import com.ihbing.shilulib.reflect.ReflectUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class AppHelper {
    public static Context getApplicationContext(){
        return currentApplication().getApplicationContext();
    }
    public static Application currentApplication() {
        return (Application) ReflectUtil.callMyStaticMethod(ReflectUtil.findMyClass("android.app.ActivityThread", null), "currentApplication");
    }
    public static String processName() {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) AppHelper.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps != null) {
            for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                if (procInfo.pid == pid) {
                    return procInfo.processName;
                }
            }
        }
        FileInputStream in = null;
        try {
            String fn = "/proc/self/cmdline";
            in = new FileInputStream(fn);
            byte[] buffer = new byte[256];
            int len = 0;
            int b;
            while ((b = in.read()) > 0 && len < buffer.length) {
                buffer[len++] = (byte) b;
            }
            if (len > 0) {
                String s = new String(buffer, 0, len, "UTF-8");
                return s;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static boolean isSystemApp(String pkgname) {
        try {
            PackageInfo info = AppHelper.getApplicationContext().getPackageManager().getPackageInfo(pkgname, 0);
            return (info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean systemApp(ApplicationInfo info) {
        return (info.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }
    /**
     * 强制关闭应用
     * shell命令格式：pkill -SIGINT your_process_name
     *
     * @param pkgname 该应用包名
     * @return
     */
    public static Boolean pkillApp(String pkgname) {
        ShellUtil.CommandResult execResult = ShellUtil.execCommand("pkill -SIGINT " + pkgname + "\n", true, true);
        if (execResult.result == 0) {
            ToastHelper.toast("应用已停止");
            return true;
        } else {
            ToastHelper.toast("应用停止失败");
            return false;
        }
    }

    public static Boolean killApp(String pkgname) {
        ShellUtil.CommandResult execResult = ShellUtil.execCommand("am force-stop " + pkgname + "\n", true, true);
        if (execResult.result == 0) {
            ToastHelper.toast("应用已停止");
            return true;
        } else {
            ToastHelper.toast("应用停止失败");
            return false;
        }
    }
    /**
     * 强制重启其他应用
     *
     * @param pkgname
     * @return
     */
    public static Boolean startApp(String pkgname) {
        ShellUtil.CommandResult execResult = ShellUtil.execCommand("am start -n " + pkgname + "/" + entryActivityClassName(pkgname) + "\n", true, true);
        if (execResult.result == 0) {
            ToastHelper.toast("应用已启动");
            return true;
        } else {
            ToastHelper.toast("应用启动失败");
            return false;
        }
    }
    /**
     * 强制重启其他应用
     *
     * @param pkgname
     * @return
     */
    public static Boolean reStartApp(String pkgname) {
        ShellUtil.CommandResult execResult = ShellUtil.execCommand("am start -S " + pkgname + "/" + entryActivityClassName(pkgname) + "\n", true, true);
        if (execResult.result == 0) {
            ToastHelper.toast("应用已重启");
            return true;
        } else {
            ToastHelper.toast("应用重启失败");
            return false;
        }
    }

    //获取应用入口类名
    public static String entryActivityClassName(String pakname) {
        Intent AppIntent = new Intent(Intent.ACTION_MAIN, null);
        AppIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = AppHelper.getApplicationContext().getPackageManager().queryIntentActivities(AppIntent, 0);
        for (int i = 0; i < resolveInfos.size(); i++) {
            ResolveInfo resolveInfo = resolveInfos.get(i);
            String ClassName = resolveInfo.activityInfo.name;
            String packageName = resolveInfo.activityInfo.packageName;
            if (packageName.equals(pakname)) return ClassName;
        }
        return null;
    }


}
