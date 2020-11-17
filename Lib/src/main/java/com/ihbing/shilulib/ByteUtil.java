package com.ihbing.shilulib;

/**
 * 作者:louis
 * 版本:1.0.0
 * 测试:No
 * 修改时间: 2019/3/24 23:45
 * 目的:
 * 更新日志:
 * 问题:
 */

public class ByteUtil {
    //1000000000000000
    public static byte[] hexString2byte(String data) {
        //0801122f0a150a13777869645f6a777668626b65756d76626d323212036768681801209febdde40528859c91adfbffffffff01
        return hexStringToByteArray(data);
    }
    public static String byte2HexString(byte data[]){
        return toHexString(data);
    }
    /**
     * byte[] to Hex string.
     *
     * @param byteArray the byte array
     * @return the string
     */

    public static String toHexString(byte[] byteArray) {
        final StringBuilder hexString = new StringBuilder("");
        if (byteArray == null || byteArray.length <= 0)
            return null;
        for (int i = 0; i < byteArray.length; i++) {
            int v = byteArray[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                hexString.append(0);
            }
            hexString.append(hv);
        }
        return hexString.toString().toLowerCase();
    }
    private static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }
}
