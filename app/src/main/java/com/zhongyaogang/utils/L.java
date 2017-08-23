package com.zhongyaogang.utils;
import android.util.Log;

public final class L {
    private static boolean isShowLog=true;
    public static void openLog(boolean enable){
        isShowLog=enable;
    }
    public static void e(String prompt){
        if (isShowLog) Log.e("TAG",prompt);
    }
    public static void i(String prompt){
        if (isShowLog) Log.i("TAG", prompt);
    }
}


