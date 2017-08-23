package com.zhongyaogang.activity;

import java.util.HashMap;
import java.util.Map;

//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;

import com.zhongyaogang.R;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.http.HttpUtils;
import com.zhongyaogang.utils.Code;
import com.zhongyaogang.utils.L;

import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{
    private TextView index_search_yaocaiwang;
    private TextView textview_xieyi;
    private ImageView wodezhongxin_left;
    private ImageView wodezhongxin_right;
    private LoginActivity act;
    private CheckBox savePasswordCB;
    private TextView tv;
    private ImageView iv_showCode;
    private Button but_forgetpass_toSetCodes;
    private Button button_lijizhuce;
    private EditText edittext_username;
    private EditText edittext_mima;
    private EditText edittext_confirmPassword;
    private EditText edittext_shoujihaoma;
    private EditText et_phoneCode;
    //产生的验证码
    private String realCode;
    private String name;
    private String pwd;
    private String tenancyname="";
    private String isexternalLogin="0";
    private String phone;
    private String confirmPassword;
    private SharedPreferences sp;
    private String sharedusernameid;
    private String username;
    private String namephone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        act=this;
        intView();
    }

    private void intView() {
        edittext_username=(EditText)findViewById(R.id.edittext_username);
        edittext_mima=(EditText)findViewById(R.id.edittext_mima);
        edittext_confirmPassword=(EditText)findViewById(R.id.edittext_confirmPassword);
        edittext_shoujihaoma=(EditText)findViewById(R.id.edittext_shoujihaoma);
        wodezhongxin_left=(ImageView)findViewById(R.id.wodezhongxin_left);
        wodezhongxin_right=(ImageView)findViewById(R.id.wodezhongxin_right);
        index_search_yaocaiwang=(TextView)findViewById(R.id.index_search_yaocaiwang);
        textview_xieyi=(TextView)findViewById(R.id.textview_xieyi);
        tv=(TextView)findViewById(R.id.tv);
        savePasswordCB=(CheckBox)findViewById(R.id.savePasswordCB);
        String str="我已阅读并同意";
        savePasswordCB.setTextSize(18);
        savePasswordCB.setText(Html.fromHtml(str));
        index_search_yaocaiwang.setText("注册");
        String str1="已有账号，立即<font color='#FF0000'><large>登录</large></font>";
        tv.setTextSize(23);
        tv.setText(Html.fromHtml(str1));
        tv.setOnClickListener(this);
        wodezhongxin_left.setOnClickListener(this);
        wodezhongxin_right.setOnClickListener(this);
        textview_xieyi.setOnClickListener(this);
        iv_showCode = (ImageView) findViewById(R.id.iv_showCode);
        //将验证码用图片的形式显示出来
        iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
        but_forgetpass_toSetCodes = (Button) findViewById(R.id.but_forgetpass_toSetCodes);
        button_lijizhuce = (Button) findViewById(R.id.button_lijizhuce);
        et_phoneCode = (EditText) findViewById(R.id.et_phoneCode);
        but_forgetpass_toSetCodes.setOnClickListener(this);
        button_lijizhuce.setOnClickListener(this);
        sp = this.getSharedPreferences("config", 0);
        sharedusernameid = sp.getString("usernameid", "");
//			username=sp.getString("username", "");
//			namephone=sp.getString("usernamephone", "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textview_xieyi:
                startActivity(new Intent(this, XieYiActivity.class));
                break;
            case R.id.tv:
                startActivity(new Intent(this, DengLuActivity.class));
                break;
            case R.id.wodezhongxin_left:
                finish();
                break;

            case R.id.button_lijizhuce:
                String phoneCode = et_phoneCode.getText().toString().toLowerCase();
                String msg = "生成的验证码："+realCode+"输入的验证码:"+phoneCode;
                if (phoneCode.equals(realCode)) {
                    Toast.makeText(act, phoneCode + "验证码正确", Toast.LENGTH_SHORT).show();
                    login();
                } else {
                    Toast.makeText(act, phoneCode + "验证码错误", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.but_forgetpass_toSetCodes:
                iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
                break;
            case R.id.wodezhongxin_right:
                showPopwindowMenu(v);
                break;
        }
    }
    public void login() {
        name = edittext_username.getText().toString();
        pwd= edittext_mima.getText().toString();
        phone=edittext_shoujihaoma.getText().toString();
        confirmPassword=edittext_confirmPassword.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_LONG).show();
            return;
        } else {
            // 数据应该提交给服务器 由服务器比较是否正确
            new Thread() {
                @SuppressWarnings("deprecation")
                public void run() {
                    try {
                        Looper.prepare();
                        String path = Constants.ACCOUNT_REGISTER;
                        Map<String,String> params = new HashMap<String,String>();
                        params.put("username", name);
                        params.put("password", pwd);
                        params.put("Phone", phone);
                        params.put("ConfirmPassword", confirmPassword);
                        params.put("tenancyName", tenancyname);
                        params.put("isExternalLogin", isexternalLogin);
                        String strResult= HttpUtils.submitPostData(path,params, "utf-8");
                        showToastInAnyThread("注册成功");
							L.e("返回结果：result"+strResult);
                            startActivity(new Intent(LoginActivity.this, DengLuActivity.class));
                            finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToastInAnyThread("请求失败");
                    }

                };
            }.start();

        }
    }
    private void showToastInAnyThread(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {//运行在主线程,内部做了处理
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        });
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
                        LoginActivity.this.finish();
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
