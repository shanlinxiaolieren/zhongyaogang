package com.zhongyaogang.activity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhongyaogang.R;

public class DingDanXiangQingActivity extends Activity implements OnClickListener{
    private ImageView wodezhongxin_left;
    private ImageView wodezhongxin_right;
    private TextView index_search_yaocaiwang;
    private DingDanXiangQingActivity act;
    private SharedPreferences sp;
    private String sharedusernameid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除掉当前activity头title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ding_dan_xiang_qing);
        act=this;
        intView();
    }

    private void intView() {
        index_search_yaocaiwang=(TextView) findViewById(R.id.index_search_yaocaiwang);
        index_search_yaocaiwang.setText("订单详情");
        wodezhongxin_left = (ImageView) this.findViewById(R.id.wodezhongxin_left);
        wodezhongxin_right = (ImageView) this.findViewById(R.id.wodezhongxin_right);
        wodezhongxin_left.setOnClickListener(this);
        wodezhongxin_right.setOnClickListener(this);
        sp = this.getSharedPreferences("config", 0);
        sharedusernameid = sp.getString("usernameid", "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wodezhongxin_left:
                finish();
                break;
            case R.id.wodezhongxin_right:
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
                        DingDanXiangQingActivity.this.finish();
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
