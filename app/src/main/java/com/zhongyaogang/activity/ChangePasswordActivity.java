package com.zhongyaogang.activity;

import java.util.HashMap;
import java.util.Map;

import com.zhongyaogang.R;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.http.HttpUtils;
import com.zhongyaogang.utils.L;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePasswordActivity extends Activity implements OnClickListener{
    private TextView index_search_yaocaiwang;
    private TextView wodezhongxin_right;
    private ImageView wodezhongxin_left;
    private LinearLayout top_linearlayout;
    private EditText edittext_mima;
    private EditText edittext_xinmima;
    private EditText edittext_zaixinmima;
    private String pwd;
    private String xinmima;
    private String zaixinmima;
    private String tenancyname="";
    private String isexternalLogin="1";
    private String usernamephone;
    private String username;
    private SharedPreferences sp;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除掉当前activity头title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_change_password);
        intView();
    }

    private void intView() {
        edittext_mima=(EditText)findViewById(R.id.edittext_mima);
        edittext_xinmima=(EditText)findViewById(R.id.edittext_xinmima);
        edittext_zaixinmima=(EditText)findViewById(R.id.edittext_zaixinmima);
        top_linearlayout=(LinearLayout)findViewById(R.id.top_linearlayout);
        wodezhongxin_left=(ImageView)findViewById(R.id.wodezhongxin_left);
        index_search_yaocaiwang=(TextView)findViewById(R.id.index_search_yaocaiwang);
        wodezhongxin_right=(TextView)findViewById(R.id.wodezhongxin_right);
        wodezhongxin_left.setOnClickListener(this);
        wodezhongxin_right.setOnClickListener(this);
        username=getIntent().getStringExtra("username");
        usernamephone=getIntent().getStringExtra("usernamephone");
        sp = this.getSharedPreferences("config", 0);
        token=sp.getString("token", "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wodezhongxin_left:
                finish();
                break;
            case R.id.wodezhongxin_right:
                changepassword();
                break;
        }
    }
    public void changepassword() {
        pwd= edittext_mima.getText().toString();
        zaixinmima=edittext_zaixinmima.getText().toString();
        xinmima=edittext_xinmima.getText().toString();
        if (TextUtils.isEmpty(zaixinmima) || TextUtils.isEmpty(pwd)||TextUtils.isEmpty(xinmima)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
            return;
        } else {
            // 数据应该提交给服务器 由服务器比较是否正确
            new Thread() {
                @SuppressWarnings("deprecation")
                public void run() {
                    try {
                        Looper.prepare();
                        String path = Constants.CHANGEPASSWORD;
                        Map<String,String> params = new HashMap<String,String>();
                        params.put("tenancyName", tenancyname);
                        params.put("userName", username);
                        params.put("password", pwd);
                        params.put("phone", usernamephone);
                        params.put("newPassword", xinmima);
                        params.put("confirmNewPassword", zaixinmima);
                        params.put("type", isexternalLogin);
                        String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
                        showToastInAnyThread("修改成功");
                            L.e("返回结果：result="+strResult);
                            startActivity(new Intent(ChangePasswordActivity.this, MainActivity.class));
                            finish();
//                        //1.打开浏览器
//                        HttpClient client = new DefaultHttpClient();
//                        //2.设置数据
//                        HttpPost httpPost = new HttpPost(path);
//                        //ctrl+shift+o
//                        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//                        parameters.add(new BasicNameValuePair("tenancyName", tenancyname));
//                        parameters.add(new BasicNameValuePair("userName", username));
//                        parameters.add(new BasicNameValuePair("password", pwd));
//                        parameters.add(new BasicNameValuePair("phone", usernamephone));
//                        parameters.add(new BasicNameValuePair("newPassword", xinmima));
//                        parameters.add(new BasicNameValuePair("confirmNewPassword", zaixinmima));
//                        parameters.add(new BasicNameValuePair("type", isexternalLogin));
//                        httpPost.setEntity(new UrlEncodedFormEntity(parameters,"UTF-8"));
//                        //3.发送请求
//                        HttpResponse response = client.execute(httpPost);
//                        int code = response.getStatusLine().getStatusCode();
//                        L.e("返回结果：code="+code);
//                        if(code == 200){
//                            InputStream is = response.getEntity().getContent();
//                            final String result = SystemUtil.readStream(is);
//                            showToastInAnyThread("修改成功");
//                            L.e("返回结果：result="+result);
//                            startActivity(new Intent(ChangePasswordActivity.this, MainActivity.class));
//                            finish();
//                        }else{
//                            showToastInAnyThread("修改失败");
//                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        showToastInAnyThread("修改失败");
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

}
