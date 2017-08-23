package com.zhongyaogang.activity;


        import com.zhongyaogang.R;
        import com.zhongyaogang.fragment.BazaarFragment;
        import com.zhongyaogang.fragment.CentreFragment;
        import com.zhongyaogang.fragment.GongQiuFragment;
        import com.zhongyaogang.fragment.HomeFragment;
        import com.zhongyaogang.fragment.ShoppingFragment;
        import com.zhongyaogang.utils.L;

        import android.annotation.SuppressLint;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentActivity;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v4.view.ViewPager.OnPageChangeListener;
        import android.view.View;
        import android.view.WindowManager;
        import android.view.View.OnClickListener;
        import android.view.Window;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;

@SuppressLint("CommitPrefEdits")
public class MainActivity extends FragmentActivity implements OnClickListener,OnPageChangeListener{
    // 底部菜单5个Linearlayout
    private LinearLayout ll_bottom_tabls_search;
    private LinearLayout ll_bottom_tabls_history;
    private LinearLayout ll_bottom_tabls_change;
    private LinearLayout ll_bottom_tabls_shopping;
    private LinearLayout ll_bottom_tabls_centre;
    // 底部菜单5个 ImageView
    private ImageView im_search;
    private ImageView im_history;
    private ImageView im_change;
    private ImageView im_shopping;
    private ImageView im_centre;
    // 底部菜单5个 TextView
    private TextView im_change_souye;
    private TextView im_change_xuexi;
    private TextView im_change_huodong;
    private TextView im_change_shopping;
    private TextView im_change_centre;
    // 5个Fragment
    private Fragment homeFragment;
    private Fragment bazaarFragment;
    private Fragment gongqiuFragment;
    private Fragment shoppingFragment;
    private Fragment centreFragment;
    private  String usernameid;
    private String username;
    private int id;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除掉当前activity头title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //初始化底部按钮事件
        initUI();
        //初始化并设置当前的
        initEvent();
    }


    private void initUI() {
        ll_bottom_tabls_search = (LinearLayout) findViewById(R.id.ll_bottom_tabls_search);
        ll_bottom_tabls_history = (LinearLayout) findViewById(R.id.ll_bottom_tabls_history);
        ll_bottom_tabls_change = (LinearLayout) findViewById(R.id.ll_bottom_tabls_change);
        ll_bottom_tabls_shopping = (LinearLayout) findViewById(R.id.ll_bottom_tabls_shopping);
        ll_bottom_tabls_centre = (LinearLayout) findViewById(R.id.ll_bottom_tabls_centre);
        im_search = (ImageView) findViewById(R.id.im_search);
        im_history= (ImageView) findViewById(R.id.im_history);
        im_change = (ImageView) findViewById(R.id.im_change);
        im_shopping = (ImageView) findViewById(R.id.im_shopping);
        im_centre = (ImageView) findViewById(R.id.im_centre);
        im_change_souye = (TextView) findViewById(R.id.im_change_souye);
        im_change_xuexi = (TextView) findViewById(R.id.im_change_xuexi);
        im_change_huodong = (TextView) findViewById(R.id.im_change_huodong);
        im_change_shopping = (TextView) findViewById(R.id.im_change_shopping);
        im_change_centre = (TextView) findViewById(R.id.im_change_centre);
        sp = this.getSharedPreferences("config", 0);
        usernameid=sp.getString("usernameid","");
        username=sp.getString("username","");
        L.e("返回结果：username="+username);
        id = getIntent().getIntExtra("contentid", 0);
        if (id==1) {
            souYeButton();

        }else  if(id==5){
            if(usernameid.trim()==""||usernameid.trim()=="0"){
                L.e("返回usernameid="+usernameid);
                startActivity(new Intent(MainActivity.this, DengLuActivity.class));
                finish();
            }else{
                woDeZhongXin();
            }
        }else if(id==3){
            gongQiuFaBuButton();
        }else{
            //初始化数据的方法
            souYeButton();
        }

    }
    private void initEvent() {
        // 设置按钮监听
        ll_bottom_tabls_search.setOnClickListener(this);
        ll_bottom_tabls_history.setOnClickListener(this);
        ll_bottom_tabls_change.setOnClickListener(this);
        ll_bottom_tabls_shopping.setOnClickListener(this);
        ll_bottom_tabls_centre.setOnClickListener(this);

    }
    private void souYeButton(){
        im_search.setImageResource(R.mipmap.home_red);
        im_change_souye.setTextColor(0xFF940000);
        ll_bottom_tabls_search.setBackgroundColor(0xffffffff);
        ll_bottom_tabls_history.setBackgroundColor(0xFF940000);
        ll_bottom_tabls_change.setBackgroundColor(0xFF940000);
        ll_bottom_tabls_shopping.setBackgroundColor(0xFF940000);
        ll_bottom_tabls_centre.setBackgroundColor(0xFF940000);
        initFragment(0);
    }
    private void shiChangButton(){
        im_history.setImageResource(R.mipmap.bazaar_red);
        im_change_xuexi.setTextColor(0xFF940000);
        ll_bottom_tabls_history.setBackgroundColor(0xffffffff);
        ll_bottom_tabls_search.setBackgroundColor(0xFF940000);
        ll_bottom_tabls_change.setBackgroundColor(0xFF940000);
        ll_bottom_tabls_shopping.setBackgroundColor(0xFF940000);
        ll_bottom_tabls_centre.setBackgroundColor(0xFF940000);
        initFragment(1);
    }
    private void gongQiuFaBuButton(){
        im_change.setImageResource(R.mipmap.gongqiu_red);
        im_change_huodong.setTextColor(0xFF940000);
        ll_bottom_tabls_change.setBackgroundColor(0xffffffff);
        ll_bottom_tabls_history.setBackgroundColor(0xFF940000);
        ll_bottom_tabls_search.setBackgroundColor(0xFF940000);
        ll_bottom_tabls_shopping.setBackgroundColor(0xFF940000);
        ll_bottom_tabls_centre.setBackgroundColor(0xFF940000);
        initFragment(2);
    }
    private void shoppingButton(){
        im_shopping.setImageResource(R.mipmap.shopping_red);
        im_change_shopping.setTextColor(0xFF940000);
        ll_bottom_tabls_shopping.setBackgroundColor(0xffffffff);
        ll_bottom_tabls_history.setBackgroundColor(0xFF940000);
        ll_bottom_tabls_change.setBackgroundColor(0xFF940000);
        ll_bottom_tabls_search.setBackgroundColor(0xFF940000);
        ll_bottom_tabls_centre.setBackgroundColor(0xFF940000);
        initFragment(3);
    }
    private void woDeZhongXin(){
        im_centre.setImageResource(R.mipmap.centre_red);
        im_change_centre.setTextColor(0xFF940000);
        ll_bottom_tabls_centre.setBackgroundColor(0xffffffff);
        ll_bottom_tabls_history.setBackgroundColor(0xFF940000);
        ll_bottom_tabls_change.setBackgroundColor(0xFF940000);
        ll_bottom_tabls_shopping.setBackgroundColor(0xFF940000);
        ll_bottom_tabls_search.setBackgroundColor(0xFF940000);
        initFragment(4);
    }
    @Override
    public void onClick(View v){
        // 在每次点击后将所有的底部按钮(ImageView,TextView)颜色改为灰色，然后根据点击着色
        restartBotton();
        // ImageView和TetxView置为红色，页面随之跳转
        switch (v.getId()) {
            case R.id.ll_bottom_tabls_search:
                souYeButton();
                break;
            case R.id.ll_bottom_tabls_history:
                shiChangButton();
                break;
            case R.id.ll_bottom_tabls_change:
                gongQiuFaBuButton();
                break;
            case R.id.ll_bottom_tabls_shopping:
                shoppingButton();
                break;
            case R.id.ll_bottom_tabls_centre:
                if(usernameid.trim()==""||usernameid.trim()=="0"){
                    L.e("MainActivity返回结果：sharedusernameid="+usernameid);
                    startActivity(new Intent(MainActivity.this, DengLuActivity.class));
                    finish();
                }else{
                    woDeZhongXin();
                }
                break;
            default:
                break;
        }
    }
    private void restartBotton() {
        // ImageView置为灰色
        im_search.setImageResource(R.mipmap.home);
        im_history.setImageResource(R.mipmap.bazaar);
        im_change.setImageResource(R.mipmap.gongqiu);
        im_shopping.setImageResource(R.mipmap.shopping);
        im_centre.setImageResource(R.mipmap.centre);
//		im_history.setBackgroundColor(android.graphics.Color.parseColor("#ffff0000"));
        // TextView置为白色
        im_change_souye.setTextColor(0xffffffff);
        im_change_xuexi.setTextColor(0xffffffff);
        im_change_huodong.setTextColor(0xffffffff);
        im_change_shopping.setTextColor(0xffffffff);
        im_change_centre.setTextColor(0xffffffff);

    }
    private void initFragment(int index) {
        // 由于是引用了V4包下的Fragment，所以这里的管理器要用getSupportFragmentManager获取
        FragmentManager fragmentManager = getSupportFragmentManager();
        // 开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 隐藏所有Fragment
        hideFragment(transaction);
        switch (index) {
            case 0:
                if (homeFragment == null) {
                    homeFragment =  new HomeFragment();
                    transaction.add(R.id.fl_content, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                break;
            case 1:
                if (bazaarFragment == null) {
                    bazaarFragment =  new BazaarFragment();
                    transaction.add(R.id.fl_content, bazaarFragment);
                } else {
                    transaction.show(bazaarFragment);
                }

                break;
            case 2:
                if (gongqiuFragment == null) {
                    gongqiuFragment =  new GongQiuFragment();
                    transaction.add(R.id.fl_content, gongqiuFragment);
                } else {
                    transaction.show(gongqiuFragment);
                }
                break;
            case 3:
                if (shoppingFragment == null) {
                    shoppingFragment =  new ShoppingFragment();
                    transaction.add(R.id.fl_content, shoppingFragment);
                } else {
                    transaction.show(shoppingFragment);
                }
                break;
            case 4:
                if (centreFragment == null) {
                    centreFragment =  new CentreFragment();
                    transaction.add(R.id.fl_content, centreFragment);
                } else {
                    transaction.show(centreFragment);
                }
                break;

            default:
                break;
        }

        // 提交事务
        transaction.commit();

    }

    //隐藏Fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (bazaarFragment != null) {
            transaction.hide(bazaarFragment);
        }
        if (gongqiuFragment != null) {
            transaction.hide(gongqiuFragment);
        }
        if (shoppingFragment != null) {
            transaction.hide(shoppingFragment);
        }
        if (centreFragment != null) {
            transaction.hide(centreFragment);
        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int arg0) {
        // TODO Auto-generated method stub
        restartBotton();
        //当前view被选择的时候,改变底部菜单图片，文字红色颜色
        switch (arg0) {
            case 0:
                im_search.setImageResource(R.mipmap.home_red);
                im_change_souye.setTextColor(0xFF940000);
                break;
            case 1:
                im_history.setImageResource(R.mipmap.bazaar_red);
                im_change_xuexi.setTextColor(0xFF940000);
                break;
            case 2:
                im_change.setImageResource(R.mipmap.gongqiu_red);
                im_change_huodong.setTextColor(0xFF940000);
                break;
            case 3:
                im_shopping.setImageResource(R.mipmap.shopping_red);
                im_change_shopping.setTextColor(0xFF940000);
                break;
            case 4:
                im_centre.setImageResource(R.mipmap.centre_red);
                im_change_shopping.setTextColor(0xFF940000);
                break;
            default:
                break;
        }
    }


}

