package com.zhongyaogang.activity;

import android.app.Application;
import android.os.Handler;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by Administrator on 2017/8/22.
 */
public class MyApplication extends Application {
    public static Handler mHandler;//线程通讯
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        //SDKInitializer.setCoordType(CoordType.BD09LL);
    }
}
