package com.zhongyaogang.activity;


import com.zhongyaogang.R;
import com.zhongyaogang.fragment.AllBaby_F;
import com.zhongyaogang.fragment.LowBaby_F;
import com.zhongyaogang.fragment.StockBaby_F;

import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class FenBazaarActivity extends FragmentActivity implements OnClickListener{
    private TextView index_search_yaocaiwang;
    private ImageView wodezhongxin_left;
    private ImageView wodezhongxin_right;
    private FenBazaarActivity act;
    private AllBaby_F allBaby_F;
    private LowBaby_F lowBaby_F;
    private StockBaby_F stockBaby_F;
    private TextView bt_cart_all, bt_cart_low, bt_cart_stock;
    private SharedPreferences sp;
    private String sharedusernameid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除掉当前activity头title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fen_bazaar);
        act=this;
        intView();
    }
    private void intView() {
        index_search_yaocaiwang=(TextView) findViewById(R.id.index_search_yaocaiwang);
        index_search_yaocaiwang.setText("市场");
        bt_cart_all = (TextView) findViewById(R.id.id_chat_tv);
        bt_cart_low = (TextView) findViewById(R.id.id_friend_tv);
        bt_cart_stock = (TextView) findViewById(R.id.id_contacts_tv);
        wodezhongxin_left = (ImageView) this.findViewById(R.id.wodezhongxin_left);
        wodezhongxin_right = (ImageView) this.findViewById(R.id.wodezhongxin_right);
        wodezhongxin_right.setOnClickListener(this);
        bt_cart_all.setOnClickListener(this);
        bt_cart_low.setOnClickListener(this);
        bt_cart_stock.setOnClickListener(this);
        wodezhongxin_left.setOnClickListener(this);
        allBaby_F = new AllBaby_F();
        addFragment(allBaby_F);
        showFragment(allBaby_F);
        sp = this.getSharedPreferences("config", 0);
        sharedusernameid = sp.getString("usernameid", "");
    }
    /** 添加Fragment **/
    public void addFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.show_cart_view, fragment);
        ft.commitAllowingStateLoss();
    }
    /** 删除Fragment **/
    public void removeFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
    }
    /** 显示Fragment **/
    public void showFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        if (allBaby_F != null) {
            ft.hide(allBaby_F);
        }
        if (lowBaby_F != null) {
            ft.hide(lowBaby_F);
        }
        if (stockBaby_F != null) {
            ft.hide(stockBaby_F);
        }

        ft.show(fragment);
        ft.commitAllowingStateLoss();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_chat_tv:
                if (allBaby_F == null) {
                    allBaby_F = new AllBaby_F();
                    addFragment(allBaby_F);
                    showFragment(allBaby_F);
                } else {
                    showFragment(allBaby_F);
                }
                bt_cart_all.setBackgroundColor(getResources().getColor(R.color.bg_Black));
                bt_cart_low.setBackgroundColor(getResources().getColor(R.color.qianghuangshe));
                bt_cart_stock.setBackgroundColor(getResources().getColor(R.color.qianghuangshe));
                bt_cart_all.setTextColor(0xffffffff);
                bt_cart_stock.setTextColor(Color.DKGRAY);
                bt_cart_low.setTextColor(Color.DKGRAY);

                break;
            case R.id.id_friend_tv:
                if (lowBaby_F == null) {
                    lowBaby_F = new LowBaby_F();
                    addFragment(lowBaby_F);
                    showFragment(lowBaby_F);
                } else {
                    showFragment(lowBaby_F);
                }
                bt_cart_low.setBackgroundColor(getResources().getColor(R.color.bg_Black));
                bt_cart_all.setBackgroundColor(getResources().getColor(R.color.qianghuangshe));
                bt_cart_stock.setBackgroundColor(getResources().getColor(R.color.qianghuangshe));
                bt_cart_low.setTextColor(0xffffffff);
                bt_cart_all.setTextColor(Color.DKGRAY);
                bt_cart_stock.setTextColor(Color.DKGRAY);
                break;
            case R.id.id_contacts_tv:
                if (stockBaby_F == null) {
                    stockBaby_F = new StockBaby_F();
                    addFragment(stockBaby_F);
                    showFragment(stockBaby_F);
                } else {
                    showFragment(stockBaby_F);
                }
                bt_cart_stock.setBackgroundColor(getResources().getColor(R.color.bg_Black));
                bt_cart_all.setBackgroundColor(getResources().getColor(R.color.qianghuangshe));
                bt_cart_low.setBackgroundColor(getResources().getColor(R.color.qianghuangshe));
                bt_cart_stock.setTextColor(0xffffffff);
                bt_cart_low.setTextColor(Color.DKGRAY);
                bt_cart_all.setTextColor(Color.DKGRAY);
                break;
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
                        FenBazaarActivity.this.finish();;
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
