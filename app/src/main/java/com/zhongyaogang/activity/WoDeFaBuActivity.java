package com.zhongyaogang.activity;

//import com.zhongyaogang.adapter.FragmentAdapter;
import com.zhongyaogang.R;
import com.zhongyaogang.fragment.GongQiu;
import com.zhongyaogang.fragment.GongQiuQiu;

import android.content.Intent;
import android.content.SharedPreferences;
        import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
        import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
        import android.widget.PopupWindow;
import android.widget.TextView;

public class WoDeFaBuActivity extends FragmentActivity implements OnClickListener{
    private TextView index_search_yaocaiwang;
    private ImageView wodezhongxin_left;
    private ImageView wodezhongxin_right;
    private WoDeFaBuActivity act;
    private GongQiu  gong;
    private GongQiuQiu  qiu;
    private TextView  gonggong;
    private TextView  qiuqiu;
    /**
     * ViewPager的当前选中页
     */
    private ViewPager mPageVp;
    private SharedPreferences sp;
    private String sharedusernameid;
    private String name;
    private String namephone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除掉当前activity头title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wo_de_fa_bu);
        act=this;
        intView();
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
        if (gong != null) {
            ft.hide(gong);
        }
        if (qiu != null) {
            ft.hide(qiu);
        }

        ft.show(fragment);
        ft.commitAllowingStateLoss();

    }
    private void intView() {
        index_search_yaocaiwang=(TextView) findViewById(R.id.index_search_yaocaiwang);
        gonggong=(TextView) findViewById(R.id.textview_gong);
        qiuqiu=(TextView) findViewById(R.id.textview_qiu);
        index_search_yaocaiwang=(TextView) findViewById(R.id.index_search_yaocaiwang);
        index_search_yaocaiwang.setText("我的发布");
        mPageVp = (ViewPager) findViewById(R.id.id_page_vp);
        wodezhongxin_left = (ImageView) findViewById(R.id.wodezhongxin_left);
        wodezhongxin_right = (ImageView) findViewById(R.id.wodezhongxin_right);
        gonggong.setOnClickListener(this);
        qiuqiu.setOnClickListener(this);
        wodezhongxin_left.setOnClickListener(this);
        wodezhongxin_right.setOnClickListener(this);
        gong=new GongQiu();
        addFragment(gong);
        showFragment(gong);
        sp = this.getSharedPreferences("config", 0);
        sharedusernameid = sp.getString("usernameid", "");
//		name=sp.getString("username", "");
//		namephone=sp.getString("usernamephone", "");
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
            case R.id.textview_gong:
                if (gong == null) {
                    gong = new GongQiu();
                    addFragment(gong);
                    showFragment(gong);
                } else {
                    showFragment(gong);
                }

                gonggong.setBackgroundColor(getResources().getColor(R.color.gong));
                qiuqiu.setBackgroundColor(getResources().getColor(R.color.qiu));

                break;
            case R.id.textview_qiu:
                if (qiu == null) {
                    qiu = new GongQiuQiu();
                    addFragment(qiu);
                    showFragment(qiu);
                } else {
                    showFragment(qiu);
                }
                qiuqiu.setBackgroundColor(getResources().getColor(R.color.gong));
                gonggong.setBackgroundColor(getResources().getColor(R.color.qiu));
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
                        WoDeFaBuActivity.this.finish();
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
        popWindow.showAsDropDown(parent,0,20);
    }


}
