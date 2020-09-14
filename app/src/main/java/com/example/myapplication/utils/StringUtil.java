package com.example.myapplication.utils;

import java.io.UnsupportedEncodingException;

public class StringUtil {
    private StringUtil() {
        System.out.println("StringUtil Constructor");
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String bytesToHexString(byte[] src, int len) {
        StringBuilder stringBuilder = new StringBuilder(BuildConfig.FLAVOR);
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < len; i++) {
            String hv = Integer.toHexString(src[i] & 255);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals(BuildConfig.FLAVOR)) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] by = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            by[i] = (byte) ((charToByte(hexChars[pos]) << 4) | charToByte(hexChars[pos + 1]));
        }
        return by;
    }

    public static byte[] getBytesFromString(String src, String charset) {
        byte[] retByte = null;
        try {
            return src.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return retByte;
        }
    }

    public static String setBytesToString(byte[] src, String charset) {
        String retString = BuildConfig.FLAVOR;
        try {
            return new String(src, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return retString;
        }
    }

    public static String UTF8ToGBK(String utf8String) {
        return setBytesToString(getBytesFromString(utf8String, "gbk"), "gbk");
    }

    public static String GBKToUTF8(String gbkString) {
        String utf8String = BuildConfig.FLAVOR;
        return setBytesToString(getBytesFromString(gbkString, "utf-8"), "utf-8");
    }

    public static void printBytes(byte[] b) {
        int length = b.length;
        System.out.print(String.format("length: %d, bytes: ", new Object[]{Integer.valueOf(length)}));
        for (int i = 0; i < length; i++) {
            System.out.print(String.format("%02X ", new Object[]{Byte.valueOf(b[i])}));
        }
        System.out.println(BuildConfig.FLAVOR);
    }

    public static void printBytes(byte[] b, int len) {
        System.out.print(String.format("length: %d, bytes: ", new Object[]{Integer.valueOf(len)}));
        for (int i = 0; i < len; i++) {
            System.out.print(String.format("%02X ", new Object[]{Byte.valueOf(b[i])}));
        }
        System.out.println(BuildConfig.FLAVOR);
    }
}
