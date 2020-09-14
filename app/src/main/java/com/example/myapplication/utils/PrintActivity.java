package com.example.myapplication.utils;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import com.google.zxing.BarcodeFormat;

import java.io.InputStream;
import java.util.Timer;

public class PrintActivity extends Activity {
    private static final String DISABLE_FUNCTION_LAUNCH_ACTION = "android.intent.action.DISABLE_FUNCTION_LAUNCH";
    private static final int DISABLE_RG = 11;
    private static final int ENABLE_RG = 10;
    private int BatteryV;
    final int PRINT_BARCODE = 4;
    final int PRINT_BLOCK = 3;
    final int PRINT_CYCLE = 5;
    final int PRINT_ESC = 6;
    final int PRINT_LOGO = 2;
    final int PRINT_TEST = 0;
    final int PRINT_UNICODE = 1;
    private int RESULT_CODE = 0;
    Bitmap bitmap = null;
    private int cycle_num = 0;
    Editor editor;
    private IntentFilter filter;
    private Button gb_barcode;
    private Button gb_esc;
    private Button gb_printCycle;
    private Button gb_test;
    private Button gb_unicode;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    PrintActivity.this.rb_high.setEnabled(true);
                    PrintActivity.this.rb_middle.setEnabled(true);
                    PrintActivity.this.rb_low.setEnabled(true);
                    return;
                case 11:
                    PrintActivity.this.rb_high.setEnabled(false);
                    PrintActivity.this.rb_middle.setEnabled(false);
                    PrintActivity.this.rb_low.setEnabled(false);
                    return;
                default:
                    String strInfo = msg.getData().getString("MSG");
                    PrintActivity.this.textViewMsg.setText(strInfo);
                    Log.d("Msr", strInfo);
                    return;
            }
        }
    };
    Handler handlers = new Handler();
    InputStream isInputStream = null;
    private boolean is_cycle = false;
    private boolean m_bThreadFinished = true;
    public int m_voltage;
    private PowerManager pms;
    private Pos pos;
    SharedPreferences preferences;
    boolean printBarcode = false;
    Print_Thread printThread = null;
    Print printer = new Print();
    private RadioButton rb_high;
    private RadioButton rb_low;
    private RadioButton rb_middle;
    private BroadcastReceiver receiver;
    int ret = -1;
    private RadioGroup rg = null;
    Runnable runnable = new Runnable() {
        public void run() {
            Log.e(PrintActivity.this.tag, "TIMER log...");
            PrintActivity.this.printThread.start();
            Log.e(PrintActivity.this.tag, "TIMER log2...");
            if (PrintActivity.this.RESULT_CODE == 0) {
                PrintActivity.this.editor = PrintActivity.this.preferences.edit();
                PrintActivity.this.editor.putInt("count", PrintActivity.access$204(PrintActivity.this));
                PrintActivity.this.editor.commit();
                Log.e(PrintActivity.this.tag, "cycle num=" + PrintActivity.this.cycle_num);
                PrintActivity.this.SendMsg("cycle num =" + PrintActivity.this.cycle_num);
            }
            PrintActivity.this.handlers.postDelayed(this, 9000);
        }
    };
    SharedPreferences sp;
    public String tag = "PrintActivity";
    TextView textViewGray = null;
    TextView textViewMsg = null;
    private Timer timer;
    private Timer timer2;
    private int voltage_level;
    private WakeLock wakeLock;

    public class BatteryReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            PrintActivity.this.voltage_level = intent.getExtras().getInt("level");
            Log.e("wbw", "current  = " + PrintActivity.this.voltage_level);
            PrintActivity.this.BatteryV = intent.getIntExtra("voltage", 0);
            Log.e("wbw", "BatteryV  = " + PrintActivity.this.BatteryV);
            Log.e("wbw", "V  = " + ((PrintActivity.this.BatteryV * 2) / 100));
        }
    }

    public class Print_Thread extends Thread {
        String content = "1234567890";
        int type;

        public boolean isThreadFinished() {
            return PrintActivity.this.m_bThreadFinished;
        }
        public Print_Thread(int type) {
            this.type = type;
        }


        public void run() {
            Log.d("Print_Thread[ run ]", "run() begin");
            Message msg1 = new Message();
            synchronized (this) {
                PrintActivity.this.m_bThreadFinished = false;
                Print.Lib_PrnInit();
                Print.Lib_PrnSetVoltage((PrintActivity.this.BatteryV * 2) / 100);
                Print.Lib_PrnSetCharSpace(0);
                Print.Lib_PrnSetLineSpace(0);
                Print.Lib_PrnSetLeftIndent(0);
                Print.Lib_PrnSetAlign(0);
                PrintActivity.this.ret = Print.Lib_PrnCheckStatus();
                if (PrintActivity.this.ret == -1) {
                    PrintActivity.this.RESULT_CODE = -1;
                    Log.e(PrintActivity.this.tag, "Lib_PrnCheckStatus fail, ret = " + PrintActivity.this.ret);
                    PrintActivity.this.SendMsg("Error, No Paper ");
                    PrintActivity.this.m_bThreadFinished = true;
                } else if (PrintActivity.this.ret == -2) {
                    PrintActivity.this.RESULT_CODE = -1;
                    Log.e(PrintActivity.this.tag, "Lib_PrnCheckStatus fail, ret = " + PrintActivity.this.ret);
                    PrintActivity.this.SendMsg("Error, Printer Too Hot ");
                    PrintActivity.this.m_bThreadFinished = true;
                } else if (PrintActivity.this.ret == -3) {
                    PrintActivity.this.RESULT_CODE = -1;
                    Log.e(PrintActivity.this.tag, "voltage = " + (PrintActivity.this.BatteryV * 2));
                    PrintActivity.this.SendMsg("Battery less " + (PrintActivity.this.BatteryV * 2));
                    PrintActivity.this.m_bThreadFinished = true;
                } else if (PrintActivity.this.voltage_level < 5) {
                    PrintActivity.this.RESULT_CODE = -1;
                    Log.e(PrintActivity.this.tag, "voltage_level = " + PrintActivity.this.voltage_level);
                    PrintActivity.this.SendMsg("Battery capacity less " + PrintActivity.this.voltage_level);
                    PrintActivity.this.m_bThreadFinished = true;
                } else {
                    PrintActivity.this.RESULT_CODE = 0;

                            PrintActivity.this.SendMsg("PRINT_TEST");
                            Print.Lib_PrnSetFont(EscPrintCls.DLE, EscPrintCls.DLE, (byte) 51);
                            Print.Lib_PrnStr("POS签购单/POS SALES SLIP\n");
                            Print.Lib_PrnSetFont(EscPrintCls.DLE, EscPrintCls.DLE, (byte) 0);
                            Print.Lib_PrnStr("商户存根MERCHANT COPY\n");
                            Print.Lib_PrnStr("- - - - - - - - - - - - - - - - - - - - - - - -\n");
                            Print.Lib_PrnSetFont((byte) 24, (byte) 24, (byte) 0);
                            Print.Lib_PrnStr("商户名称(MERCHANT NAME):\n");
                            Print.Lib_PrnStr("中国银联直连测试\n");
                            Print.Lib_PrnStr("商户编号(MERCHANT NO):\n");
                            Print.Lib_PrnStr("    001420183990573\n");
                            Print.Lib_PrnStr("终端编号(TERMINAL NO):00026715\n");
                            Print.Lib_PrnStr("操作员号(OPERATOR NO):12345678\n");
                            Print.Lib_PrnStr("- - - - - - - - - - - - - - - -\n");
                            Print.Lib_PrnStr("发卡行(ISSUER):01020001 工商银行\n");
                            Print.Lib_PrnStr("卡号(CARD NO):\n");
                            Print.Lib_PrnStr("    9558803602109503920\n");
                            Print.Lib_PrnStr("收单行(ACQUIRER):03050011民生银行\n");
                            Print.Lib_PrnStr("交易类型(TXN. TYPE):消费/SALE\n");
                            Print.Lib_PrnStr("卡有效期(EXP. DATE):2013/08\n");
                            Print.Lib_PrnStr("- - - - - - - - - - - - - - - -\n");
                            Print.Lib_PrnStr("批次号(BATCH NO)  :000023\n");
                            Print.Lib_PrnStr("凭证号(VOUCHER NO):000018\n");
                            Print.Lib_PrnStr("授权号(AUTH NO)   :987654\n");
                            Print.Lib_PrnStr("日期/时间(DATE/TIME):\n");
                            Print.Lib_PrnStr("    2008/01/28 16:46:32\n");
                            Print.Lib_PrnStr("交易参考号(REF. NO):200801280015\n");
                            Print.Lib_PrnStr("金额(AMOUNT):  RMB:2.55\n");
                            Print.Lib_PrnStr("- - - - - - - - - - - - - - - -\n");
                            Print.Lib_PrnStr("备注/REFERENCE\n");
                            Print.Lib_PrnStr("- - - - - - - - - - - - - - - -\n");
                            Print.Lib_PrnSetFont(EscPrintCls.DLE, EscPrintCls.DLE, (byte) 0);
                            Print.Lib_PrnStr("持卡人签名(CARDHOLDER SIGNATURE)\n");
                            Print.Lib_PrnStr("\n");
                            Print.Lib_PrnStr("- - - - - - - - - - - - - - - - - - - - - - - -\n");
                            Print.Lib_PrnStr("  本人确认以上交易，同意将其计入本卡帐户\n");
                            Print.Lib_PrnStr("  I ACKNOWLEDGE SATISFACTORY RECEIPT\n");
                            Print.Lib_PrnStr("                                         ");
                            Print.Lib_PrnStr("\n");
                            Print.Lib_PrnStr("                                         ");
                            Print.Lib_PrnStr("\n");
                            Print.Lib_PrnStr("\n");
                            Print.Lib_PrnStr("\n");
                            Print.Lib_PrnStr("\n");
                            Print.Lib_PrnStr("\n");
                            PrintActivity.this.ret = Print.Lib_PrnStart();
                            msg1.what = 10;
                            PrintActivity.this.handler.sendMessage(msg1);
                            Log.d(BuildConfig.FLAVOR, "Lib_PrnStart ret = " + PrintActivity.this.ret);
                            if (PrintActivity.this.ret == 0) {
                                PrintActivity.this.RESULT_CODE = 0;
                            }
                            PrintActivity.this.RESULT_CODE = -1;
                            Log.e(PrintActivity.this.tag, "Lib_PrnStart fail, ret = " + PrintActivity.this.ret);
                            PrintActivity.this.SendMsg("Error, No Paper ");

                    PrintActivity.this.m_bThreadFinished = true;
                    Log.e(PrintActivity.this.tag, "goToSleep2...");
                }
            }
        }
    }

    static /* synthetic */ int access$204(PrintActivity x0) {
        int i = x0.cycle_num + 1;
        x0.cycle_num = i;
        return i;
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_print);
        this.textViewMsg = (TextView) findViewById(R.id.textView_msg);
        this.textViewGray = (TextView) findViewById(R.id.textview_Gray);
        this.rg = (RadioGroup) findViewById(R.id.rg_Gray_type);
        this.rb_high = (RadioButton) findViewById(R.id.RadioButton_high);
        this.rb_middle = (RadioButton) findViewById(R.id.RadioButton_middle);
        this.rb_low = (RadioButton) findViewById(R.id.radioButton_low);
        this.gb_test = (Button) findViewById(R.id.button_test);
        this.gb_unicode = (Button) findViewById(R.id.button_unicode);
        this.gb_barcode = (Button) findViewById(R.id.button_barcode);
        this.gb_esc = (Button) findViewById(R.id.esc);
        init_Gray();
        this.rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (PrintActivity.this.printThread == null || PrintActivity.this.printThread.isThreadFinished()) {
                    switch (checkedId) {
                        case R.id.RadioButton_high /*2131427361*/:
                            PrintActivity.this.textViewGray.setText(R.string.low);
                            Print.Lib_PrnSetGray(1);
                            PrintActivity.this.setValue(1);
                            return;
                        case R.id.RadioButton_middle /*2131427362*/:
                            PrintActivity.this.textViewGray.setText(R.string.medium);
                            Print.Lib_PrnSetGray(2);
                            PrintActivity.this.setValue(2);
                            return;
                        case R.id.radioButton_low /*2131427363*/:
                            PrintActivity.this.textViewGray.setText(R.string.high);
                            Print.Lib_PrnSetGray(3);
                            PrintActivity.this.setValue(3);
                            return;
                        default:
                            return;
                    }
                }
                Log.e(PrintActivity.this.tag, "Thread is still running...");
            }
        });
    }

    private void setValue(int val) {
        this.sp = getSharedPreferences("Gray", 0);
        Editor editor = this.sp.edit();
        editor.putInt("value", val);
        editor.commit();
    }

    private int getValue() {
        this.sp = getSharedPreferences("Gray", 0);
        return this.sp.getInt("value", 2);
    }

    private void init_Gray() {
        int flag = getValue();
        Print.Lib_PrnSetGray(flag);
        if (flag == 3) {
            this.rb_low.setChecked(true);
            this.textViewGray.setText(R.string.high);
        } else if (flag == 2) {
            this.rb_middle.setChecked(true);
            this.textViewGray.setText(R.string.medium);
        } else if (flag == 1) {
            this.rb_high.setChecked(true);
            this.textViewGray.setText(R.string.low);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        disableFunctionLaunch(true);
        getWindow().addFlags(128);
        super.onResume();
        this.filter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
        this.receiver = new BatteryReceiver();
        registerReceiver(this.receiver, this.filter);
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        disableFunctionLaunch(false);
        getWindow().clearFlags(128);
        super.onPause();
        QuitHandler();
        unregisterReceiver(this.receiver);
    }



    public void QuitHandler() {
        this.is_cycle = false;
        this.gb_test.setEnabled(true);
        this.gb_barcode.setEnabled(true);
        this.gb_esc.setEnabled(true);
        this.gb_unicode.setEnabled(true);
        this.handlers.removeCallbacks(this.runnable);
    }

    public void SendMsg(String strInfo) {
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putString("MSG", strInfo);
        msg.setData(b);
        this.handler.sendMessage(msg);
    }

    private void disableFunctionLaunch(boolean state) {
        Intent disablePowerKeyIntent = new Intent(DISABLE_FUNCTION_LAUNCH_ACTION);
        if (state) {
            disablePowerKeyIntent.putExtra("state", true);
        } else {
            disablePowerKeyIntent.putExtra("state", false);
        }
        sendBroadcast(disablePowerKeyIntent);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("onKeyDown", "keyCode = " + keyCode);
        return super.onKeyDown(keyCode, event);
    }
}
