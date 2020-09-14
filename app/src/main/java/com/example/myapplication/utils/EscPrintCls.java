package com.example.myapplication.utils;

import android.util.Log;


public class EscPrintCls {
    private static int CMD_TYPE = 0;
    public static final byte DLE = (byte) 16;
    public static final byte[] DLE_ENQ_n_BYTE = new byte[]{DLE, (byte) 4, (byte) 5};
    public static final String DLE_ENQ_n_STR = "1004";
    public static final byte ESC = (byte) 27;
    public static final byte[] ESC_ALIGNING_BYTE = new byte[]{(byte) 27, (byte) 97};
    public static final String ESC_ALIGNING_STR = "1B61";
    public static final byte[] ESC_ALIGN_Character_Pitch_BYTE = new byte[]{(byte) 27, (byte) 51, (byte) 0};
    public static final byte[] ESC_CLEAR_BYTE = new byte[]{DLE, (byte) 20, (byte) 8};
    public static final String ESC_CLEAR_STR = "1014";
    public static final String ESC_Character_Pitch_STR = "1B33";
    public static final byte[] ESC_FONT_BYTE = new byte[]{(byte) 27, (byte) 77};
    public static final String ESC_FONT_STR = "1B4D";
    public static final byte[] ESC_INIT_BYTE = new byte[]{(byte) 27, (byte) 64};
    public static final String ESC_INIT_STR = "1B40";
    public static final byte[] ESC_LEFTSPACE_BYTE = new byte[]{(byte) 29, (byte) 76};
    public static final String ESC_LEFTSPACE_STR = "1D4C";
    public static final byte[] ESC_LineSpacing_BYTE = new byte[]{(byte) 27, (byte) 50};
    public static final String ESC_LineSpacing_STR = "1B32";
    public static final byte[] ESC_NOMALFONT_BYTE = new byte[]{(byte) 27, (byte) 105, (byte) 15};
    public static final String ESC_NOMALFONT_STR = "1B69";
    public static final byte[] ESC_PRINT_BYTE = new byte[]{(byte) 10};
    public static final String ESC_PRINT_STR = "0A";
    public static final byte[] ESC_SPEED_BYTE = new byte[]{(byte) 27, (byte) 109};
    public static final String ESC_SPEED_STR = "1B6D";
    private static byte[] Font_type = new byte[]{DLE, (byte) 0};
    static Print printCls = new Print();

    public static void Lib_PrnStr(String str) {
        Print.Lib_PrnStr(str);
    }

    public static int printCmd(byte[] escCmd) {
                String tag = "EscPrintCls";
        String strCmd = HexStringUtil.byte2String(escCmd);
        String UpStr = strCmd.toUpperCase();
        String tmpStr = BuildConfig.FLAVOR;
        int len = UpStr.length();
        if (len < 2) {
            return -1;
        }
        if (len >= 2 && len < 4) {
            if (ESC_PRINT_STR.equals(UpStr.substring(0, len))) {
                return Print.Lib_PrnStart();
            }
        } else if (len >= 4 && len < 6) {
            if (ESC_INIT_STR.equals(UpStr.substring(0, 4))) {
            }
        } else if (len >= 6 && len < 8) {
            String temp_n;
            int INTtemp_n;
            tmpStr = UpStr.substring(0, 4);
            if (ESC_INIT_STR.equals(tmpStr)) {
                temp_n = strCmd.substring(4, len);
                System.out.println("speed :" + temp_n);
                INTtemp_n = HexStringUtil.HexToInt(temp_n);
                if (-1 < INTtemp_n && INTtemp_n < 101) {
                    Print.Lib_PrnSetSpeed(INTtemp_n);
                }
                Print.Lib_PrnInit();
                Print.Lib_PrnSetSpeed(0);
                Print.Lib_PrnSetLeftIndent(0);
                Font_type[0] = DLE;
                Font_type[1] = (byte) 0;
                Print.Lib_PrnSetFont(Font_type[0], Font_type[0], Font_type[1]);
            }
            if (ESC_SPEED_STR.equals(tmpStr)) {
                temp_n = strCmd.substring(4, len);
                System.out.println("speed :" + temp_n);
                INTtemp_n = HexStringUtil.HexToInt(temp_n);
                if (-1 < INTtemp_n && INTtemp_n < 3) {
                    Print.Lib_PrnSetSpeed(INTtemp_n);
                }
            } else if (ESC_LineSpacing_STR.equals(tmpStr)) {
                temp_n = strCmd.substring(4, len);
                Log.e(tag, "LineSpacing =" + temp_n);
                Log.e(tag, "LineSpacing INTtemp_n=" + HexStringUtil.HexToInt(temp_n));
            } else if (ESC_Character_Pitch_STR.equals(tmpStr)) {
                temp_n = strCmd.substring(4, len);
                Log.e(tag, "Character_Pitch =" + temp_n);
                INTtemp_n = HexStringUtil.HexToInt(temp_n);
            } else if (ESC_FONT_STR.equals(tmpStr)) {
                INTtemp_n = HexStringUtil.HexToInt(strCmd.substring(4, len));
                if (INTtemp_n == 0 || INTtemp_n == 1) {
                    if (INTtemp_n == 0) {
                        Font_type[0] = (byte) 24;
                    }
                    if (INTtemp_n == 1) {
                        Font_type[0] = DLE;
                    }
                    Print.Lib_PrnSetFont(Font_type[0], Font_type[0], Font_type[1]);
                }
            } else if (ESC_LEFTSPACE_STR.equals(tmpStr)) {
                temp_n = strCmd.substring(4, len);
                Log.e(tag, "ESC_LEFTSPACE_STR =" + temp_n);
                INTtemp_n = HexStringUtil.HexToInt(temp_n);
                if (-1 < INTtemp_n && INTtemp_n < 256) {
                    Print.Lib_PrnSetLeftIndent(INTtemp_n);
                }
            } else if (ESC_CLEAR_STR.equals(tmpStr)) {
                temp_n = strCmd.substring(4, len);
                Log.e(tag, "ESC_CLEAR_STR =" + temp_n);
                if (HexStringUtil.HexToInt(temp_n) == 8) {
                    Print.Lib_PrnInit();
                }
            } else if (ESC_ALIGNING_STR.equals(tmpStr)) {
                temp_n = strCmd.substring(4, len);
                Log.e(tag, "ESC_ALIGNING_STR =" + temp_n);
                INTtemp_n = HexStringUtil.HexToInt(temp_n);
            } else if (ESC_NOMALFONT_STR.equals(tmpStr)) {
                temp_n = strCmd.substring(4, len);
                INTtemp_n = HexStringUtil.HexToInt(temp_n);
                Log.e(tag, "ESC_NOMALFONT_STR =" + temp_n);
                if (INTtemp_n == 0 || INTtemp_n == 15) {
                    if (INTtemp_n == 0) {
                        Font_type[1] = (byte) 0;
                    }
                    if (INTtemp_n == 15) {
                        Log.e(tag, "ESC_NOMALFONT_STR =0X33");
                        Font_type[1] = (byte) 51;
                    }
                    Print.Lib_PrnSetFont((byte) 24, (byte) 24, Font_type[1]);
                }
            } else if (DLE_ENQ_n_STR.equals(tmpStr)) {
                temp_n = strCmd.substring(4, len);
                Log.e(tag, "DLE_ENQ_n_STR =" + temp_n);
                if (HexStringUtil.HexToInt(temp_n) == 5) {
                    int ret_tmp = Print.Lib_PrnCheckStatus();
                    if (ret_tmp == 0) {
                        return 0;
                    }
                    if (ret_tmp == -1) {
                        return 4;
                    }
                    if (ret_tmp == -2) {
                        return 8;
                    }
                    if (ret_tmp == -3) {
                        return 1;
                    }
                }
            }
        }
        return 0;
    }
}
