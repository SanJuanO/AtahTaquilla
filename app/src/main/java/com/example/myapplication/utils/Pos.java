package com.example.myapplication.utils;

public class Pos {
    public static final byte ESC = (byte) 27;

    public void initPos(int batteryValue) {
        EscPrintCls.printCmd(new byte[]{(byte) 27, (byte) 64, (byte) batteryValue});
    }

    /* Access modifiers changed, original: protected */
    public void printTabSpace(int length) {
        for (int i = 0; i < length; i++) {
            EscPrintCls.Lib_PrnStr("\n");
        }
    }

    public void bold(boolean flag) {
        if (flag) {
            EscPrintCls.printCmd(new byte[]{(byte) 27, (byte) 105, (byte) 15});
        } else {
            EscPrintCls.printCmd(new byte[]{(byte) 27, (byte) 105, (byte) 0});
        }
    }

    /* Access modifiers changed, original: protected */
    public void printWordSpace(int length) {
        for (int i = 0; i < length; i++) {
            EscPrintCls.Lib_PrnStr("\n");
        }
    }

    public void printText(String text) {
        EscPrintCls.Lib_PrnStr(text);
    }

        public int StarPrintPos() {
        return EscPrintCls.printCmd(new byte[]{(byte) 10});
    }

    public void printTextNewLine(String text) {
        EscPrintCls.Lib_PrnStr(text + "\n");
    }

    public void printLocation(int position) {
        if (-1 < position && position < 3) {
            EscPrintCls.printCmd(new byte[]{(byte) 27, (byte) 97, (byte) position});
        }
    }

    /* Access modifiers changed, original: protected */
    public void printLineSpacing(int i) {
        if (-1 < i && i < 255) {
            EscPrintCls.printCmd(new byte[]{(byte) 27, (byte) 50, (byte) i});
        }
    }

    /* Access modifiers changed, original: protected */
    public void printCharacterPitch(int i) {
        if (-1 < i && i < 255) {
            EscPrintCls.printCmd(new byte[]{(byte) 27, (byte) 51, (byte) i});
        }
    }

    public void printFONT(int i) {
        if (-1 < i && i < 2) {
            EscPrintCls.printCmd(new byte[]{(byte) 27, (byte) 77, (byte) i});
        }
    }

    public void printGRAY(int i) {
        if (-1 < i && i < 3) {
            EscPrintCls.printCmd(new byte[]{(byte) 27, (byte) 109, (byte) i});
        }
    }

    /* Access modifiers changed, original: protected */
    public void CLEARBUFF() {
        EscPrintCls.printCmd(new byte[]{EscPrintCls.DLE, (byte) 20, (byte) 8});
    }

    /* Access modifiers changed, original: protected */
    public int PrintStatus() {
        return EscPrintCls.printCmd(new byte[]{EscPrintCls.DLE, (byte) 4, (byte) 5});
    }

    /* Access modifiers changed, original: protected */
    public void PrintLeftSpace(int i) {
        if (-1 < i && i < 255) {
            EscPrintCls.printCmd(new byte[]{(byte) 27, (byte) 109, (byte) i});
        }
    }

    public void printLine(int lineNum) {
        for (int i = 0; i < lineNum; i++) {
            EscPrintCls.Lib_PrnStr(" \n");
        }
    }
}
