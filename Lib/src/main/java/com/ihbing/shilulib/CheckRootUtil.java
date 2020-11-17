package com.ihbing.shilulib;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 作者:louis
 * 版本:1.0.0
 * 测试:No
 * 修改时间: 2019/6/6 9:06
 * 目的:
 * 更新日志:
 * 问题:
 */

public class CheckRootUtil {

    public static boolean checkRootWithExec(){
        boolean result;
        try {
            exec("su");
            result=true;
        } catch (Throwable e) {
            e.printStackTrace();
            result=false;
        }

        return result;
    }
    public static Process exec(String command) throws IOException {
           return new ProcessBuilder(command)
                    .directory(null)
                    .start();


    }

    public static boolean checkRootWithPath() {
        boolean bool = false;
        String[] array_string = System.getenv("PATH").split(":");
        int i = array_string.length;
        int i1 = 0;
        while(i1 < i) {
            if(new File(array_string[i1], "su").exists()) {
                bool = true;
            }
            else {
                ++i1;
                continue;
            }

            return bool;
        }

        return bool;
    }

    public static boolean checkRootWithTags() {
        String tags = Build.TAGS;
        return !(tags == null || !tags.contains("test-keys"));
    }

    public static boolean checkRootWithFile() {
        String[] fileNames = new String[]{"/system/app/Superuser.apk", "/system/xbin/daemonsu", "/system/etc/init.d/99SuperSUDaemon",
                "/system/bin/.ext/.su", "/system/etc/.has_su_daemon", "/system/etc/.installed_su_daemon",
                "/dev/com.koushikdutta.superuser.daemon/"};
        for (String fileName: fileNames){
            if(new File(fileName).exists())return true;
        }
        return false;
    }

    public boolean detectTestKeys() {
        String buildTags = Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }



    private boolean isAnyPackageFromListInstalled(Context context, List<String> packages) {
        boolean result = false;
        PackageManager pm = context.getPackageManager();
        for (String packageName : packages) {
            try {
                pm.getPackageInfo(packageName, 0);
                result = true;
            } catch (PackageManager.NameNotFoundException e) {
            }
        }
        return result;
    }

    public boolean checkForDangerousProps() {
        Exception e;
        Throwable th;
        BufferedReader bufferedReader = null;
        boolean result = false;
        try {
            ProcessBuilder pb = new ProcessBuilder(new String[]{"getprop", "ro.debuggable"});
            pb.redirectErrorStream(true);
            BufferedReader in = new BufferedReader(new InputStreamReader(pb.start().getInputStream()));
            try {
                String str = "";
                str = in.readLine();
                if (str != null && str.equals("1")) {
                    result = true;
                }
                if (result) {
                    bufferedReader = in;
                } else {
                    pb = new ProcessBuilder(new String[]{"getprop", "ro.secure"});
                    pb.redirectErrorStream(true);
                    bufferedReader = new BufferedReader(new InputStreamReader(pb.start().getInputStream()));
                    str = bufferedReader.readLine();
                    if (str != null && str.equals("0")) {
                        result = true;
                    }
                }
                IOUtil.close(bufferedReader);
                return result;
            } catch (Exception e2) {
                e = e2;
                bufferedReader = in;
                try {
                    e.printStackTrace();
                    IOUtil.close(bufferedReader);
                    return result;
                } catch (Throwable th2) {
                    th = th2;
                    IOUtil.close(bufferedReader);
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = in;
                IOUtil.close(bufferedReader);
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            IOUtil.close(bufferedReader);
            return result;
        }
        return result;
    }


    public boolean checkSuExists() {
        boolean z = true;
        BufferedReader in = null;
        try {
            ProcessBuilder pb = new ProcessBuilder(new String[]{"which", "su"});
            pb.redirectErrorStream(false);
            Process mProcess = pb.start();
            BufferedReader in2 = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
            try {
                String line = in2.readLine();
                int exitValue = mProcess.waitFor();
                IOUtil.close(in2);
                if (line == null || !line.endsWith("su")) {
                    z = false;
                }
                in = in2;
                return z;
            } catch (Throwable th) {
                in = in2;
                if (in != null) {
                    IOUtil.close(in);
                }
                return false;
            }
        } catch (Throwable th2) {
            if (in != null) {
                IOUtil.close(in);
            }
            return false;
        }
    }

    public boolean checkForNativeLibraryReadAccess() {
        return true;
    }

    public boolean canLoadNativeLibrary() {
        return true;
    }



}
