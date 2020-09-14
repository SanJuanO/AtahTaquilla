package com.example.myapplication.utils;

import java.io.UnsupportedEncodingException;

public class HexStringUtil {
    public static final char ERR_INVALID_ARGUMENT = '.';

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.length() == 0) {
            return null;
        }
        hexString = hexString.replaceAll(" ", BuildConfig.FLAVOR).toUpperCase();
        if (hexString.length() % 2 != 0) {
            hexString = hexString.concat("0");
        }
        char[] hexChars = hexString.toCharArray();
        byte[] desBytes = new byte[((hexString.length() + 1) / 2)];
        for (int i = 0; i < desBytes.length; i++) {
            int pos = i * 2;
            desBytes[i] = (byte) ((charToByte(hexChars[pos]) << 4) | charToByte(hexChars[pos + 1]));
        }
        return desBytes;
    }

    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String byteArrayToHexstring(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        if (bytes.length <= 0 || bytes == null) {
            return null;
        }
        for (byte b : bytes) {
            String hv = Integer.toHexString(b & 255);
            if (hv.length() < 2) {
                hexString.append(0);
            }
            hexString.append(hv);
        }
        return hexString.toString().toUpperCase();
    }

    public static char[] byteToHexChar(byte b) {
        char[] ch = new char[2];
        if (b > (byte) -1) {
            return null;
        }
        ch[0] = "0123456789ABCDEF".charAt((b & 240) >> 4);
        ch[1] = "0123456789ABCDEF".charAt(b & 15);
        return ch;
    }

    public static String byteArrayToHexstring(byte[] bytes, int start, int end) {
        StringBuilder hexString = new StringBuilder();
        if (bytes.length <= 0 || bytes == null) {
            return null;
        }
        for (int i = start; i < end; i++) {
            String hv = Integer.toHexString(bytes[i] & 255);
            if (hv.length() < 2) {
                hexString.append(0);
            }
            hexString.append(hv);
        }
        return hexString.toString().toUpperCase();
    }

    public static String hexStringToString(String hexString, int encodeType) {
        String result = BuildConfig.FLAVOR;
        int max = hexString.length() / encodeType;
        for (int i = 0; i < max; i++) {
            result = result + ((char) hexStringToAlgorism(hexString.substring(i * encodeType, (i + 1) * encodeType)));
        }
        return result;
    }

    public static String StringToAsciiString(String content) {
        String result = BuildConfig.FLAVOR;
        int max = content.length();
        for (int i = 0; i < max; i++) {
            result = result + Integer.toHexString(content.charAt(i));
        }
        return result;
    }

    public static String AsciiStringToString(String content) {
        if (content == null) {
            return null;
        }
        String result = BuildConfig.FLAVOR;
        int length = content.length() / 2;
        for (int i = 0; i < length; i++) {
            result = result + String.valueOf((char) hexStringToAlgorism(content.substring(i * 2, (i * 2) + 2)));
        }
        return result;
    }

    public static int hexStringToAlgorism(String hex) {
        hex = hex.toUpperCase();
        int max = hex.length();
        int result = 0;
        for (int i = max; i > 0; i--) {
            int algorism;
            char c = hex.charAt(i - 1);
            if (c < '0' || c > '9') {
                algorism = c - 55;
            } else {
                algorism = c - 48;
            }
            result = (int) (((double) result) + (Math.pow(16.0d, (double) (max - i)) * ((double) algorism)));
        }
        return result;
    }

    public static String toHexString(String s) {
        String str = BuildConfig.FLAVOR;
        for (int i = 0; i < s.length(); i++) {
            str = str + Integer.toHexString(s.charAt(i));
        }
        return str;
    }

    public static String toStringHex(String s) {
        byte[] baKeyword = new byte[(s.length() / 2)];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (Integer.parseInt(s.substring(i * 2, (i * 2) + 2), 16) & 255);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            return new String(baKeyword, "utf-8");
        } catch (Exception e1) {
            e1.printStackTrace();
            return s;
        }
    }

    public static byte[] convertPinToBytes(String pin) {
        if (pin == null) {
            return null;
        }
        try {
            byte[] pinBytes = pin.getBytes("UTF-8");
            if (pinBytes.length <= 0 || pinBytes.length > 16) {
                return null;
            }
            return pinBytes;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String hexStringToStr(String s) {
        Exception e1;
        if (s == null || s.equals(BuildConfig.FLAVOR)) {
            return null;
        }
        s = s.replace(" ", BuildConfig.FLAVOR);
        byte[] baKeyword = new byte[(s.length() / 2)];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (Integer.parseInt(s.substring(i * 2, (i * 2) + 2), 16) & 255);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            String s2 = new String(baKeyword, "UTF-8");
            try {
                String str = new String();
                return s2;
            } catch (Exception e2) {
                e1 = e2;
                s = s2;
                e1.printStackTrace();
                return s;
            }
        } catch (Exception e3) {
            e1 = e3;
            e1.printStackTrace();
            return s;
        }
    }

    public static String unicode2String(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            string.append((char) Integer.parseInt(hex[i], 16));
        }
        return string.toString();
    }

    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder(BuildConfig.FLAVOR);
        byte[] bs = str.getBytes();
        for (int i = 0; i < bs.length; i++) {
            sb.append(chars[(bs[i] & 240) >> 4]);
            sb.append(chars[bs[i] & 15]);
        }
        return sb.toString().trim();
    }

    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[(hexStr.length() / 2)];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (((str.indexOf(hexs[i * 2]) * 16) + str.indexOf(hexs[(i * 2) + 1])) & 255);
        }
        return new String(bytes);
    }

    public static String byte2String(byte[] byteArray) {
        char[] result = new char[(byteArray.length * 2)];
        for (int i = 0; i < byteArray.length; i++) {
            char temp = (char) (((byteArray[i] & 240) >> 4) & 15);
            result[i * 2] = (char) (temp > 9 ? (temp + 65) - 10 : temp + 48);
            temp = (char) (byteArray[i] & 15);
            result[(i * 2) + 1] = (char) (temp > 9 ? (temp + 65) - 10 : temp + 48);
        }
        return new String(result);
    }

    public static byte[] string2Byte(String s) {
        byte[] result = new byte[(s.length() / 2)];
        int j = 0;
        for (int i = 0; i < (s.length() + 1) / 2; i++) {
            int j2 = j + 1;
            result[i] = char2Byte(s.charAt(j));
            j = j2 + 1;
            result[i] = (byte) (char2Byte(s.charAt(j2)) + (result[i] << 4));
        }
        return result;
    }

    public static byte char2Byte(char c) {
        if ('a' <= c && c <= 'f') {
            return (byte) ((c - 97) + 10);
        }
        if ('A' <= c && c <= 'F') {
            return (byte) ((c - 65) + 10);
        }
        if ('0' > c || c > '9') {
            return (byte) -1;
        }
        return (byte) (c - 48);
    }

    public static byte[] string2BCD(String s) {
        byte[] result = new byte[s.length()];
        for (int i = 0; i < s.length(); i++) {
            result[i] = (byte) s.charAt(i);
        }
        return result;
    }

    public static String IntToHex(int n) {
        char[] ch = new char[20];
        int nIndex = 0;
        while (true) {
            int m = n / 16;
            int k = n % 16;
            if (k == 15) {
                ch[nIndex] = 'F';
            } else if (k == 14) {
                ch[nIndex] = 'E';
            } else if (k == 13) {
                ch[nIndex] = 'D';
            } else if (k == 12) {
                ch[nIndex] = 'C';
            } else if (k == 11) {
                ch[nIndex] = 'B';
            } else if (k == 10) {
                ch[nIndex] = 'A';
            } else {
                ch[nIndex] = (char) (k + 48);
            }
            nIndex++;
            if (m == 0) {
                StringBuffer sb = new StringBuffer();
                sb.append(ch, 0, nIndex);
                sb.reverse();
                return new String("0x") + sb.toString();
            }
            n = m;
        }
    }

    public static int HexToInt(String strHex) {
        int nResult = 0;
        if (!IsHex(strHex)) {
            return 0;
        }
        String str = strHex.toUpperCase();
        if (str.length() > 2 && str.charAt(0) == '0' && str.charAt(1) == 'X') {
            str = str.substring(2);
        }
        int nLen = str.length();
        for (int i = 0; i < nLen; i++) {
            try {
                nResult += GetHex(str.charAt((nLen - i) - 1)) * GetPower(16, i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return nResult;
    }

    public static int GetHex(char ch) throws Exception {
        if (ch >= '0' && ch <= '9') {
            return ch - 48;
        }
        if (ch >= 'a' && ch <= 'f') {
            return (ch - 97) + 10;
        }
        if (ch >= 'A' && ch <= 'F') {
            return (ch - 65) + 10;
        }
        throw new Exception("error param");
    }

    public static int GetPower(int nValue, int nCount) throws Exception {
        if (nCount < 0) {
            throw new Exception("nCount can't small than 1!");
        } else if (nCount == 0) {
            return 1;
        } else {
            int nSum = 1;
            for (int i = 0; i < nCount; i++) {
                nSum *= nValue;
            }
            return nSum;
        }
    }

    public static boolean IsHex(String strHex) {
        int i = 0;
        if (strHex.length() > 2 && strHex.charAt(0) == '0' && (strHex.charAt(1) == 'X' || strHex.charAt(1) == 'x')) {
            i = 2;
        }
        while (i < strHex.length()) {
            char ch = strHex.charAt(i);
            if ((ch < '0' || ch > '9') && ((ch < 'A' || ch > 'F') && (ch < 'a' || ch > 'f'))) {
                return false;
            }
            i++;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(toStringHex("38"));
    }
}
