package com.zhongyaogang.activity;

//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongyaogang.R;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.http.HttpUtils;
import com.zhongyaogang.utils.L;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class YunFeiSettingAddActivity extends Activity implements OnClickListener{
    private TextView index_search_yaocaiwang;
    private ImageView wodezhongxin_left;
    private ImageView wodezhongxin_right;
    private YunFeiSettingAddActivity act;
    private TextView rl_save_yunfeiadd;
    private TextView textview_kuaidi;
    private EditText edittext_jine;
    private EditText edittext_changpingeshu;
    private EditText edittext_figure;
    private EditText edittext_title;
    private String  title;
    private String  figure;
    private String  kuaidi;
    private String  changpingeshu;
    private String  jine;
    private String isEnabled="1";
    private SharedPreferences sp;
    private String sharedusernameid;
    private String name;
    private String namephone;
    private String token;
    private String yunfeiId="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除掉当前activity头title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_yun_fei_setting_add);
        act=this;
        intView();
    }
    private void intView() {
        edittext_title=(EditText)findViewById(R.id.edittext_title);
        edittext_figure=(EditText)findViewById(R.id.edittext_figure);
        edittext_changpingeshu=(EditText)findViewById(R.id.edittext_changpingeshu);
        edittext_jine=(EditText)findViewById(R.id.edittext_jine);
        wodezhongxin_left=(ImageView)findViewById(R.id.wodezhongxin_left);
        wodezhongxin_right=(ImageView)findViewById(R.id.wodezhongxin_right);
        index_search_yaocaiwang=(TextView) findViewById(R.id.index_search_yaocaiwang);
        textview_kuaidi=(TextView) findViewById(R.id.textview_kuaidi);
        rl_save_yunfeiadd=(TextView) findViewById(R.id.rl_save_yunfeiadd);
        index_search_yaocaiwang.setText("运费设置");
        wodezhongxin_left.setOnClickListener(this);
        wodezhongxin_right.setOnClickListener(this);
        textview_kuaidi.setOnClickListener(this);
        rl_save_yunfeiadd.setOnClickListener(this);
        sp = this.getSharedPreferences("config", 0);
        sharedusernameid = sp.getString("usernameid", "");
        token=sp.getString("token", "");
        name=sp.getString("username", "");
        namephone=sp.getString("usernamephone", "");
        //修改传过来的
        yunfeiId=getIntent().getStringExtra("id");
        if(yunfeiId==null||yunfeiId=="0"){
            yunfeiId="0";
        }
        title=getIntent().getStringExtra("title");
        figure=getIntent().getStringExtra("figure");
        kuaidi=getIntent().getStringExtra("logisticsType");
        changpingeshu=getIntent().getStringExtra("upN");
        jine=getIntent().getStringExtra("upMoney");
        edittext_title.setText(title);
        edittext_figure.setText(figure);
        textview_kuaidi.setText(kuaidi);
        edittext_changpingeshu.setText(changpingeshu);
        edittext_jine.setText(jine);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wodezhongxin_right:
                showPopwindowMenu(v);
                break;
            case R.id.wodezhongxin_left:
                finish();
                break;
            case R.id.rl_save_yunfeiadd:
                yunFeiAdd();
                break;
            case R.id.textview_kuaidi:
                showPopwindowKuaiDi(v);
                break;
        }
    }
    private void yunFeiAdd() {
        title=edittext_title.getText().toString();
        figure=edittext_figure.getText().toString();
        kuaidi=textview_kuaidi.getText().toString();
        changpingeshu=edittext_changpingeshu.getText().toString();
        jine=edittext_jine.getText().toString();
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(figure)||
                TextUtils.isEmpty(changpingeshu)||TextUtils.isEmpty(jine)) {
            Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else {
            // 数据应该提交给服务器 由服务器比较是否正确
            new Thread() {
                public void run() {
                    try {
                        Looper.prepare();
                        String path = Constants.YUN_FEI_ADD;
                        String pathXiuGai=Constants.YUN_FEI_XIU_GAI;
                        Map<String,String> params = new HashMap<String,String>();
                        params.put("id", yunfeiId);
                        params.put("userId",sharedusernameid);
                        params.put("title", title);
                        params.put("figure", figure);
                        params.put("isEnabled", isEnabled);
                        params.put("logisticsType", kuaidi);
                        params.put("upN", changpingeshu);
                        params.put("upMoney",jine);
                        String strResult= HttpUtils.submitPostDataToken(yunfeiId == "0" ? path:pathXiuGai,params, "utf-8",token);
                        L.e("返回结果：addressAddresult" + strResult);
                       // Intent intent = new Intent(act,YunFeiSettingActivity.class);
                       // startActivity(intent);
                        act.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_SHORT).show();
                    }

                };
            }.start();
        }
    }
    private void showPopwindowKuaiDi(View parent) {
        View popView = View.inflate(act, R.layout.right_kuaidi_menu, null);
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
                        textview_kuaidi.setText("全国包邮");
                        break;
                    case R.id.btn_camera_pop_album:
                        textview_kuaidi.setText("快递");
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
                        YunFeiSettingAddActivity.this.finish();
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
    }

}
