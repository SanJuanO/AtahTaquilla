package com.example.myapplication.utils;

import android.graphics.Bitmap;
import android.util.Log;
import com.google.zxing.BarcodeFormat;
import java.io.UnsupportedEncodingException;

public class Print {
    private final String tag = "Print";

    private class PrinterBitmap {
        public int m_iHeight;
        public int m_iRowBytes;
        public int m_iWidth;
        public byte[] m_pDotByteBuffer;

        public PrinterBitmap() {
            this.m_pDotByteBuffer = null;
            this.m_iRowBytes = 0;
            this.m_iWidth = 0;
            this.m_iHeight = 0;
            this.m_pDotByteBuffer = null;
            this.m_iRowBytes = 0;
            this.m_iWidth = 0;
            this.m_iHeight = 0;
        }
    }

    public static native int Lib_PrnCheckStatus();

    public static native int Lib_PrnFeedPaper(int i);

    public static native int Lib_PrnGetFont(byte[] bArr, byte[] bArr2, byte[] bArr3);

    public static native int Lib_PrnInit();

    public static native int Lib_PrnLogo(byte[] bArr);

    public static native int Lib_PrnSetAlign(int i);

    public static native int Lib_PrnSetCharSpace(int i);

    public static native int Lib_PrnSetFont(byte b, byte b2, byte b3);

    public static native int Lib_PrnSetGray(int i);

    public static native int Lib_PrnSetLeftIndent(int i);

    public static native int Lib_PrnSetLeftSpace(int i);

    public static native int Lib_PrnSetLineSpace(int i);

    public static native int Lib_PrnSetSpace(byte b, byte b2);

    public static native int Lib_PrnSetSpeed(int i);

    public static native int Lib_PrnSetVoltage(int i);

    public static native int Lib_PrnStart();

    public static native int Lib_PrnStep(int i);

    private static native int Lib_PrnStr(byte[] bArr);

    static {

        System.loadLibrary("Android");
    }

    public static int Lib_PrnStr(String str) {
        byte[] strbytes = null;
        try {
            System.out.println("original string---" + str);
            strbytes = str.getBytes("UnicodeBigUnmarked");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for (byte print : strbytes) {
            System.out.print(print);
        }
        System.out.println();
        Lib_PrnStr(strbytes);
        return 0;
    }



    public int Lib_PrnBmp(Bitmap bitmap) {
        PrinterBitmap pPrinterBmpLine = Bitmap2PrintDot(bitmap);
        int iBufferSize = pPrinterBmpLine.m_iRowBytes * pPrinterBmpLine.m_iHeight;
        byte[] byLogoBuffer = new byte[(iBufferSize + 5)];
        System.arraycopy(pPrinterBmpLine.m_pDotByteBuffer, 0, byLogoBuffer, 5, iBufferSize);
        byLogoBuffer[0] = (byte) (pPrinterBmpLine.m_iWidth / 256);
        byLogoBuffer[1] = (byte) (pPrinterBmpLine.m_iWidth % 256);
        byLogoBuffer[2] = (byte) (pPrinterBmpLine.m_iHeight / 256);
        byLogoBuffer[3] = (byte) (pPrinterBmpLine.m_iHeight % 256);
        int iRetCode = Lib_PrnLogo(byLogoBuffer);
        if (iRetCode != 0) {
        }
        return iRetCode;
    }

    private PrinterBitmap Bitmap2PrintDot(Bitmap m_pBitmap) {
        int iW = m_pBitmap.getWidth();
        int iH = m_pBitmap.getHeight();
        Log.i("iW = ", Integer.toString(iW));
        Log.i("iH = ", Integer.toString(iH));
        int iRowBytes = (iW + 7) / 8;
        Log.i("iRowBytes = ", Integer.toString(iRowBytes));
        int iBufferSize = iRowBytes * iH;
        Log.i("iBufferSize = ", Integer.toString(iBufferSize));
        byte[] byBuffer = new byte[iBufferSize];
        for (int iBufferPos = 0; iBufferPos < iBufferSize; iBufferPos++) {
            byBuffer[iBufferPos] = (byte) 0;
        }
        for (int y = 0; y < iH; y++) {
            for (int iRowByteIndex = 0; iRowByteIndex < iRowBytes; iRowByteIndex++) {
                for (int iBitIndex = 0; iBitIndex < 8; iBitIndex++) {
                    int x = (iRowByteIndex * 8) + iBitIndex;
                    if (iW <= x) {
                        break;
                    }
                    if (-16777216 == m_pBitmap.getPixel(x, y)) {
                        int i = (y * iRowBytes) + iRowByteIndex;
                        byBuffer[i] = (byte) ((int) (((double) byBuffer[i]) + Math.pow(2.0d, (double) (7 - iBitIndex))));
                    }
                }
            }
        }
        PrinterBitmap bmp = new PrinterBitmap();
        bmp.m_pDotByteBuffer = byBuffer;
        bmp.m_iRowBytes = iRowBytes;
        bmp.m_iWidth = m_pBitmap.getWidth();
        bmp.m_iHeight = m_pBitmap.getHeight();
        return bmp;
    }
}
