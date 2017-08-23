package com.zhongyaogang.activity;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.zhongyaogang.R;

public class XieYiActivity extends Activity implements OnClickListener{
    private ImageView wodezhongxin_left;
    private ImageView wodezhongxin_right;
    private XieYiActivity act;
    private WebView webview_xieyi;
    private SharedPreferences sp;
    private String sharedusernameid;
    private String name;
    private String namephone;
    private String urlxieyi="http://resource.gbxx123.com/book/epubs/2017/3/16/1489631400120/ops/b_content.xhtml";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_xie_yi);
        act=this;
        intView();
        initWebView();

    }
    private void intView() {
        wodezhongxin_left=(ImageView)findViewById(R.id.wodezhongxin_left_xieyi);
        wodezhongxin_right=(ImageView)findViewById(R.id.wodezhongxin_right_xieyi);
        wodezhongxin_left.setOnClickListener(this);
        wodezhongxin_right.setOnClickListener(this);
        sp = this.getSharedPreferences("config", 0);
        sharedusernameid = sp.getString("usernameid", "");
//			name=sp.getString("username", "");
//			namephone=sp.getString("usernamephone", "");
    }
    @SuppressLint("SetJavaScriptEnabled")
    private  void initWebView(){
        webview_xieyi = (WebView) findViewById(R.id.webview_xieyi);
        //设置WebViewClient
        //	    myWebView.getSettings().setAllowFileAccess(true);
        webview_xieyi.getSettings().setLoadsImagesAutomatically(true);
        webview_xieyi.getSettings().setJavaScriptEnabled(true);
        webview_xieyi.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView webview, String url) {
                webview.loadUrl(url); return true;
            }
            public void onPageFinished(WebView webview, String url) {
                super.onPageFinished(webview, url);
            }
        });
        webview_xieyi.loadUrl(urlxieyi);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.wodezhongxin_left_xieyi:
                finish();
                break;
            case R.id.wodezhongxin_right_xieyi:
                showPopwindowMenu(v);
                break;
        }
    }
    private void showPopwindowMenu(View parent) {
        View popView = View.inflate(act, R.layout.right_pop_menu, null);
        Button btnCamera = (Button) popView.findViewById(R.id.btn_camera_pop_camera);
        Button btnAlbum = (Button) popView.findViewById(R.id.btn_camera_pop_album);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        final PopupWindow popWindow = new PopupWindow(popView,width,height);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(false);// 设置允许在外点击消失
        OnClickListener listener = new OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_camera_pop_camera:
                        Intent intent=new Intent(act,MainActivity.class);
                        intent.putExtra("contentid", 1);
                        startActivity(intent);
                        XieYiActivity.this.finish();
                        break;
                    case R.id.btn_camera_pop_album:
                        startActivity(new Intent(act, WoDeXiaoXiActivity.class));
                        break;
                }
                popWindow.dismiss();
            }
        };
        btnCamera.setOnClickListener(listener);
        btnAlbum.setOnClickListener(listener);

        ColorDrawable dw = new ColorDrawable(0x30000000);
        popWindow.setBackgroundDrawable(dw);
        // 设置好参数之后再show
        popWindow.showAsDropDown(parent,10,20);
//		popWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }
}
