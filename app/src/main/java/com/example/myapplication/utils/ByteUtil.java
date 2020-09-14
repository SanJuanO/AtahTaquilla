package com.example.myapplication.utils;

import android.annotation.SuppressLint;

@SuppressLint({"UseValueOf"})
public class ByteUtil {
    private ByteUtil() {
        System.out.println("ByteUtil Constructor");
    }

    public static String bytesToString(byte[] b) {
        StringBuffer result = new StringBuffer(BuildConfig.FLAVOR);
        for (byte b2 : b) {
            char ch = (char) (b2 & 255);
            if (ch == 0) {
                break;
            }
            result.append(ch);
        }
        return result.toString();
    }

    public static String bytearrayToHexString(byte[] b, int leng) {
        StringBuffer strbuf = new StringBuffer();
        for (int i = 0; i < leng; i++) {
            strbuf.append("0123456789ABCDEF".charAt((byte) ((b[i] & 240) >> 4)));
            strbuf.append("0123456789ABCDEF".charAt((byte) (b[i] & 15)));
            strbuf.append(" ");
        }
        return strbuf.toString();
    }

    public static byte[] stringToBytes(String s) {
        return s.getBytes();
    }

    public static void ShortToBytes(byte[] b, short x, int offset) {
        if (b.length - offset >= 2) {
            b[offset + 1] = (byte) (x >> 8);
            b[offset + 0] = (byte) (x >> 0);
        }
    }

    public static short BytesToShort(byte[] b, int offset) {
        if (b.length - offset >= 2) {
            return (short) ((b[offset + 1] << 8) | (b[offset + 0] & 255));
        }
        return (short) 0;
    }

    public static String byteToHexString(byte b) {
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append("0123456789ABCDEF".charAt((b >> 4) & 15));
        sbBuffer.append("0123456789ABCDEF".charAt(b & 15));
        return sbBuffer.toString();
    }

    public static void IntToBytes(byte[] b, int x, int offset) {
        if (b.length - offset >= 4) {
            b[offset + 3] = (byte) (x >> 24);
            b[offset + 2] = (byte) (x >> 16);
            b[offset + 1] = (byte) (x >> 8);
            b[offset + 0] = (byte) (x >> 0);
        }
    }

    public static int BytesToInt(byte[] b, int offset) {
        if (b.length - offset >= 4) {
            return ((((b[offset + 3] & 255) << 24) | ((b[offset + 2] & 255) << 16)) | ((b[offset + 1] & 255) << 8)) | ((b[offset + 0] & 255) << 0);
        }
        return 0;
    }

    public static void LongToBytes(byte[] b, long x, int offset) {
        if (b.length - offset >= 8) {
            b[offset + 7] = (byte) ((int) (x >> 56));
            b[offset + 6] = (byte) ((int) (x >> 48));
            b[offset + 5] = (byte) ((int) (x >> 40));
            b[offset + 4] = (byte) ((int) (x >> 32));
            b[offset + 3] = (byte) ((int) (x >> 24));
            b[offset + 2] = (byte) ((int) (x >> 16));
            b[offset + 1] = (byte) ((int) (x >> 8));
//            b[offset + 0] = (byte) ((int) (x >> null));

            b[offset + 0] = (byte) ((int) (x >> 0));

        }
    }

    public static long BytesToLong(byte[] b, int offset) {
        if (b.length - offset >= 8) {
            return ((((((((((long) b[offset + 7]) & 255) << 56) | ((((long) b[offset + 6]) & 255) << 48)) | ((((long) b[offset + 5]) & 255) << 40)) | ((((long) b[offset + 4]) & 255) << 32)) | ((((long) b[offset + 3]) & 255) << 24)) | ((((long) b[offset + 2]) & 255) << 16)) | ((((long) b[offset + 1]) & 255) << 8)) | ((((long) b[offset + 0]) & 255) << 0);
        }
        return 0;
    }

    public static void CharToBytes(byte[] b, char ch, int offset) {
        if (b.length - offset >= 2) {
            int temp = ch;
            for (int i = 0; i < 2; i++) {
                b[offset + i] = new Integer(temp & 255).byteValue();
                temp >>= 8;
            }
        }
    }

    public static char BytesToChar(byte[] b, int offset) {
        int s = 0;
        if (b.length - offset >= 2) {
            if (b[offset + 1] > (byte) 0) {
                s = 0 + b[offset + 1];
            } else {
                s = 0 + (b[offset + 0] + 256);
            }
            s *= 256;
            if (b[offset + 0] > (byte) 0) {
                s += b[offset + 1];
            } else {
                s += b[offset + 0] + 256;
            }
        }
        return (char) s;
    }

    public static void FloatToBytes(byte[] b, float x, int offset) {
        if (b.length - offset >= 4) {
            int l = Float.floatToIntBits(x);
            for (int i = 0; i < 4; i++) {
                b[offset + i] = new Integer(l).byteValue();
                l >>= 8;
            }
        }
    }

    public static float BytesToFloat(byte[] b, int offset) {
        int l = 0;
        if (b.length - offset >= 4) {
            l = (int) (((long) (((int) (((long) (((int) (((long) (b[offset + 0] & 255)) | (((long) b[offset + 1]) << 8))) & 65535)) | (((long) b[offset + 2]) << 16))) & 16777215)) | (((long) b[offset + 3]) << 24));
        }
        return Float.intBitsToFloat(l);
    }

    public static void DoubleToBytes(byte[] b, double x, int offset) {
        if (b.length - offset >= 8) {
            long l = Double.doubleToLongBits(x);
            for (int i = 0; i < 4; i++) {
                b[offset + i] = new Long(l).byteValue();
                l >>= 8;
            }
        }
    }

    public static double BytesToDouble(byte[] b, int offset) {
        long l = 0;
        if (b.length - offset >= 8) {
            l = (((((((((((((((long) b[0]) & 255) | (((long) b[1]) << 8)) & 65535) | (((long) b[2]) << 16)) & 16777215) | (((long) b[3]) << 24)) & 4294967295L) | (((long) b[4]) << 32)) & 1099511627775L) | (((long) b[5]) << 40)) & 281474976710655L) | (((long) b[6]) << 48)) & 72057594037927935L) | (((long) b[7]) << 56);
        }
        return Double.longBitsToDouble(l);
    }

    public static short toLH(short n) {
        return BytesToShort(new byte[]{(byte) (n & 255), (byte) ((n >> 8) & 255)}, 0);
    }

    public static short toHL(short n) {
        return BytesToShort(new byte[]{(byte) (n & 255), (byte) ((n >> 8) & 255)}, 0);
    }

    public static int toLH(int n) {
        return BytesToInt(new byte[]{(byte) (n & 255), (byte) ((n >> 8) & 255), (byte) ((n >> 16) & 255), (byte) ((n >> 24) & 255)}, 0);
    }

    public static int toHL(int n) {
        return BytesToInt(new byte[]{(byte) (n & 255), (byte) ((n >> 8) & 255), (byte) ((n >> 16) & 255), (byte) ((n >> 24) & 255)}, 0);
    }

    public static long toLH(long n) {
        return BytesToLong(new byte[]{(byte) ((int) (n & 255)), (byte) ((int) ((n >> 8) & 255)), (byte) ((int) ((n >> 16) & 255)), (byte) ((int) ((n >> 24) & 255)), (byte) ((int) ((n >> 32) & 255)), (byte) ((int) ((n >> 40) & 255)), (byte) ((int) ((n >> 48) & 255)), (byte) ((int) ((n >> 56) & 255))}, 0);
    }

    public static long toHL(long n) {
        return BytesToLong(new byte[]{(byte) ((int) (n & 255)), (byte) ((int) ((n >> 8) & 255)), (byte) ((int) ((n >> 16) & 255)), (byte) ((int) ((n >> 24) & 255)), (byte) ((int) ((n >> 32) & 255)), (byte) ((int) ((n >> 40) & 255)), (byte) ((int) ((n >> 48) & 255)), (byte) ((int) ((n >> 56) & 255))}, 0);
    }

    public static void encodeOutputBytes(byte[] b, short sLen) {
        if (b.length >= sLen + 2) {
            System.arraycopy(b, 0, b, 2, sLen);
            byte[] byShort = new byte[2];
            ShortToBytes(byShort, sLen, 0);
            System.arraycopy(byShort, 0, b, 0, byShort.length);
        }
    }

    public static short decodeOutputBytes(byte[] b) {
        byte[] byShort = new byte[2];
        System.arraycopy(b, 0, byShort, 0, byShort.length);
        short sLen = BytesToShort(byShort, 0);
        System.arraycopy(b, 2, b, 0, sLen);
        return sLen;
    }
}
