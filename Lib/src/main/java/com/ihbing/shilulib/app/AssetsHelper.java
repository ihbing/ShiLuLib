package com.ihbing.shilulib.app;

import com.ihbing.shilulib.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AssetsHelper {
    public static byte[] assets(String fileName) {
        try {
            InputStream inputStream = AppHelper.getApplicationContext().getAssets().open(fileName);
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100]; //buff用于存放循环读取的临时数据
            int rc = 0;
            while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            return swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean copyAssets(String fileNames[], String outDirPath) {
        return copyAssets(fileNames, new File(outDirPath));
    }

    public static boolean copyAssets(String fileNames[], File outDir) {
        for (String fileName : fileNames) {
            if (!copyAssets(fileName, new File(outDir, fileName))) {
                return false;
            }
        }
        return true;
    }

    public static boolean copyAssets(String fileName, String outFilePath) {
        return copyAssets(fileName, new File(outFilePath));
    }

    public static boolean copyAssets(String fileName, File outFile) {
        return FileUtil.write(outFile, assets(fileName), false);
    }

}
