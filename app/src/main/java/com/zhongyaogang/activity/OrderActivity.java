package com.zhongyaogang.activity;
import com.zhongyaogang.R;
import com.zhongyaogang.fragment.DaiFaHuoFragment;
import com.zhongyaogang.fragment.DaiFuKuanFragment;
import com.zhongyaogang.fragment.DaiShouHuo;
import com.zhongyaogang.fragment.QuanBuFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

public class OrderActivity extends  FragmentActivity implements OnClickListener{
    private TextView index_search_yaocaiwang;
    private ImageView wodezhongxin_left;
    private ImageView wodezhongxin_right;
    private QuanBuFragment quanbufragment;
    private DaiFuKuanFragment daifukuanfragment;
    private DaiFaHuoFragment  daifahuofragment;
    private DaiShouHuo   daishouhuo;
    private TextView textview_quanbu;
    private TextView textview_daifukuan;
    private TextView textview_daifahuo;
    private TextView textview_daishouhuo;
    private int currentTab;
    private OrderActivity act;
    private SharedPreferences sp;
    private String sharedusernameid;
    private String name;
    private String namephone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除掉当前activity头title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order);
        act=this;
        intView();
        intViewOrder();
    }

    private void intViewOrder(){
        currentTab=getIntent().getIntExtra("data",0);
        if(currentTab==0){
            if (quanbufragment == null) {
                quanbufragment = new QuanBuFragment();
                addFragment(quanbufragment);
                showFragment(quanbufragment);
            } else {
                showFragment(quanbufragment);
            }
            textview_quanbu.setTextColor(Color.RED);
            textview_daifukuan.setTextColor(Color.DKGRAY);
            textview_daifahuo.setTextColor(Color.DKGRAY);
            textview_daishouhuo.setTextColor(Color.DKGRAY);
        }
        if(currentTab==1){
            if (daifukuanfragment == null) {
                daifukuanfragment = new DaiFuKuanFragment();
                addFragment(daifukuanfragment);
                showFragment(daifukuanfragment);
            } else {
                showFragment(daifukuanfragment);
            }

            textview_daifukuan.setTextColor(Color.RED);
            textview_quanbu.setTextColor(Color.DKGRAY);
            textview_daifahuo.setTextColor(Color.DKGRAY);
            textview_daishouhuo.setTextColor(Color.DKGRAY);
        }
        if(currentTab==2){
            if (daifahuofragment == null) {
                daifahuofragment = new DaiFaHuoFragment();
                addFragment(daifahuofragment);
                showFragment(daifahuofragment);
            } else {
                showFragment(daifahuofragment);
            }

            textview_daifahuo.setTextColor(Color.RED);
            textview_quanbu.setTextColor(Color.DKGRAY);
            textview_daifukuan.setTextColor(Color.DKGRAY);
            textview_daishouhuo.setTextColor(Color.DKGRAY);
        }
        if(currentTab==3){
            if (daishouhuo == null) {
                daishouhuo = new DaiShouHuo();
                addFragment(daishouhuo);
                showFragment(daishouhuo);
            } else {
                showFragment(daishouhuo);
            }

            textview_daishouhuo.setTextColor(Color.RED);
            textview_quanbu.setTextColor(Color.DKGRAY);
            textview_daifukuan.setTextColor(Color.DKGRAY);
            textview_daifahuo.setTextColor(Color.DKGRAY);
        }
    }
    private void intView() {
        index_search_yaocaiwang=(TextView) findViewById(R.id.index_search_yaocaiwang);
        wodezhongxin_right=(ImageView) findViewById(R.id.wodezhongxin_right);
        textview_quanbu=(TextView) findViewById(R.id.textview_quanbu);
        textview_daifukuan=(TextView) findViewById(R.id.textview_daifukuan);
        textview_daifahuo=(TextView) findViewById(R.id.textview_daifahuo);
        textview_daishouhuo=(TextView) findViewById(R.id.textview_daishouhuo);
        index_search_yaocaiwang.setText("我的订单");
        wodezhongxin_left = (ImageView) this.findViewById(R.id.wodezhongxin_left);
        wodezhongxin_left.setOnClickListener(this);
        wodezhongxin_right.setOnClickListener(this);
        textview_quanbu.setOnClickListener(this);
        textview_daifukuan.setOnClickListener(this);
        textview_daifahuo.setOnClickListener(this);
        textview_daishouhuo.setOnClickListener(this);
        quanbufragment=new QuanBuFragment();
        addFragment(quanbufragment);
        showFragment(quanbufragment);
        sp = this.getSharedPreferences("config", 0);
        sharedusernameid = sp.getString("usernameid", "");
//		name=sp.getString("username", "");
//		namephone=sp.getString("usernamephone", "");
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
        if (quanbufragment != null) {
            ft.hide(quanbufragment);
        }
        if (daifukuanfragment != null) {
            ft.hide(daifukuanfragment);
        }
        if (daifahuofragment != null) {
            ft.hide(daifahuofragment);
        }
        if (daishouhuo != null) {
            ft.hide(daishouhuo);
        }

        ft.show(fragment);
        ft.commitAllowingStateLoss();

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
            case R.id.textview_quanbu:
                if (quanbufragment == null) {
                    quanbufragment = new QuanBuFragment();
                    addFragment(quanbufragment);
                    showFragment(quanbufragment);
                } else {
                    showFragment(quanbufragment);
                }
                textview_quanbu.setTextColor(Color.RED);
                textview_daifukuan.setTextColor(Color.DKGRAY);
                textview_daifahuo.setTextColor(Color.DKGRAY);
                textview_daishouhuo.setTextColor(Color.DKGRAY);
                break;
            case R.id.textview_daifukuan:
                if (daifukuanfragment == null) {
                    daifukuanfragment = new DaiFuKuanFragment();
                    addFragment(daifukuanfragment);
                    showFragment(daifukuanfragment);
                } else {
                    showFragment(daifukuanfragment);
                }
                textview_daifukuan.setTextColor(Color.RED);
                textview_quanbu.setTextColor(Color.DKGRAY);
                textview_daifahuo.setTextColor(Color.DKGRAY);
                textview_daishouhuo.setTextColor(Color.DKGRAY);
                break;
            case R.id.textview_daifahuo:
                if (daifahuofragment == null) {
                    daifahuofragment = new DaiFaHuoFragment();
                    addFragment(daifahuofragment);
                    showFragment(daifahuofragment);
                } else {
                    showFragment(daifahuofragment);
                }
                textview_daifahuo.setTextColor(Color.RED);
                textview_quanbu.setTextColor(Color.DKGRAY);
                textview_daifukuan.setTextColor(Color.DKGRAY);
                textview_daishouhuo.setTextColor(Color.DKGRAY);
                break;
            case R.id.textview_daishouhuo:
                if (daishouhuo == null) {
                    daishouhuo = new DaiShouHuo();
                    addFragment(daishouhuo);
                    showFragment(daishouhuo);
                } else {
                    showFragment(daishouhuo);
                }
                textview_daishouhuo.setTextColor(Color.RED);
                textview_quanbu.setTextColor(Color.DKGRAY);
                textview_daifukuan.setTextColor(Color.DKGRAY);
                textview_daifahuo.setTextColor(Color.DKGRAY);
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
                        OrderActivity.this.finish();
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
