package com.ihbing.shilulib.app;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class ClipboardHelper {

    public static void copy(String str) {
        ClipboardManager ClipboardManager1 = (ClipboardManager) AppHelper.currentApplication().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipboardManager1.setPrimaryClip(ClipData.newPlainText("text/plain", str));
        ToastHelper.toast("已复制内容：" + str);
    }
    public static String paste() {
        ClipboardManager ClipboardManager1 = (ClipboardManager) AppHelper.getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        return ClipboardManager1.getPrimaryClip().getItemAt(0).getText().toString();
    }

}
