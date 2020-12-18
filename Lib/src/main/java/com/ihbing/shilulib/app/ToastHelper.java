package com.ihbing.shilulib.app;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.ihbing.shilulib.reflect.ReflectUtil;

public class ToastHelper {
    private static Toast toast;

    public static void toast(final String msg) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            reflectToast(msg);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    reflectToast(msg);
                }
            });
        }

    }

    private static void reflectToast(String msg) {
        if (toast == null) {
            toast = (Toast) ReflectUtil.callMyStaticMethod(ReflectUtil.findMyClass("android.widget.Toast", ClassLoader.getSystemClassLoader()), "makeText", new Class[]{Context.class, CharSequence.class, int.class}, AppHelper.getApplicationContext(), "" + msg, Toast.LENGTH_LONG);
            //toast = Toast.makeText(context, "" + msg, Toast.LENGTH_LONG);
        } else {
            ReflectUtil.callMyMethod(toast, "setText", new Class[]{CharSequence.class}, "" + msg);
            //toast.setText("" + msg);
        }
        ReflectUtil.callMyMethod(toast, "show");
        //toast.show();
    }
}
