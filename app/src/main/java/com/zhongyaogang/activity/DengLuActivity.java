package com.zhongyaogang.activity;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongyaogang.R;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.http.HttpUtils;
import com.zhongyaogang.utils.L;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

@SuppressWarnings({ "unused", "deprecation" })
@SuppressLint({ "ShowToast", "HandlerLeak" })
public class DengLuActivity extends Activity implements OnClickListener{
    private TextView tv;
    private ImageView wodezhongxin_left;
    private CheckBox savePasswordCB;
    private DengLuActivity act;
    private EditText usenameedit;
    private EditText passwordedit;
    private String result;
    private String name;
    private String pwd;
    private String tenancyname="";
//    private LoginBean loginbean;
    private Button button_denglu;
    private Context context;
    // 声明一个共享参数(存储数据方便的api)
    private SharedPreferences sp;
    private boolean isChecked = false;
    Editor editor ;
    private String userid;
    private String userName;
    private String phone;
    private String token;
    private int sharedusernameid;
    public final static String ENCODING = "UTF-8";
    private Timer timer = new Timer(true);

//	//任务
//	private TimerTask task = new TimerTask() {
//	  public void run() {
//	  	Message msg = new Message();
//	  	msg.what = 1;
//	  	handler.sendMessage(msg);
//
//	  }
//	};

    //	private Handler handler  = new Handler(){
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			  switch (msg.what) {
//			  case 1:
//				  login();
//				  break;
//			}
//		}
//	};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //去除掉当前activity头title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deng_lu);
        act=this;
        intView();

    }

    private void intView() {
        tv=(TextView)findViewById(R.id.tv);
        wodezhongxin_left=(ImageView) findViewById(R.id.wodezhongxin_left);
        wodezhongxin_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        savePasswordCB=(CheckBox)findViewById(R.id.savePasswordCB);
        usenameedit=(EditText)findViewById(R.id.usenameedit);
        passwordedit=(EditText)findViewById(R.id.passwordedit);
        button_denglu=(Button)findViewById(R.id.button_denglu);
        String str="未有账号，立即<font color='#FF0000'><large>注册</large></font>";
        tv.setTextSize(23);
        tv.setText(Html.fromHtml(str));
        tv.setOnClickListener(this);
        button_denglu.setOnClickListener(this);
        // 2. 通过上下文得到一个共享参数的实例对象
        sp = this.getSharedPreferences("config", 0);
        restoreInfo();

    }

    /**
     * 根据原来保存的文件信息,把用户名和密码信息回显到界面
     */
    public void restoreInfo() {
        // TODO:读取密码
        name = usenameedit.getText().toString();
        pwd= passwordedit.getText().toString();
        name = sp.getString("username", "");
        pwd = sp.getString("password", "");
    }
    private void inputNamePwd(){
        editor = sp.edit();
        // 判断是否需要记录用户名和密码
        if (!isChecked) {// 被选中状态,需要记录用户名和密码
            isChecked=true;
            // 记录密码
            editor.putString("username", name);
            editor.putString("password", pwd);
            editor.commit();// 提交数据. 类似关闭流,事务
        }
    }
    public void login() {
        name = usenameedit.getText().toString();
        pwd= passwordedit.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_LONG).show();
            return;
        } else {
            isChecked = sp.getBoolean("check", false);
            inputNamePwd();
            savePasswordCB.setChecked(isChecked);
            name = sp.getString("username", "");
            pwd=sp.getString("password", "");
//			if (isChecked) {
//				usenameedit.setText(name);
//				if (!TextUtils.isEmpty(userid)) {
//					L.e("返回结果：userid="+userid);
//					Intent intent = new Intent(DengLuActivity.this, MainActivity.class);
//					 intent.putExtra("contentid", 5);
//					 intent.putExtra("usernameid", Integer.parseInt(userid));
//					 intent.putExtra("username", userName);
//					 intent.putExtra("usernamephone", phone);
//					startActivity(intent);
//					act.finish();
//				}
//			}
            // 数据应该提交给服务器 由服务器比较是否正确
            new Thread() {
                public void run() {
                    try {
                        Looper.prepare();
                        String path = Constants.ACCOUNT_LOGIN;
                        Map<String,String> params = new HashMap<String,String>();
                        params.put("usernameOrEmailAddress", name);
                        params.put("password", pwd);
                        params.put("tenancyName", tenancyname);
                        String strResult= HttpUtils.submitPostData(path,params, "utf-8");
                            L.e("返回结果：strResult"+strResult);
                            JSONObject jo = new JSONObject(strResult);
                            JSONObject	body1 = jo.getJSONObject("result");
                            userid=body1.getString("userId");
                            userName=body1.getString("userName");
                            phone=body1.getString("phone");
                            token=body1.getString("token");
                            L.e("返回结果：token="+token);
                            editor.putString("usernameid", userid);
                            editor.putString("username", userName);
                            editor.putString("usernamephone", phone);
                            editor.putString("token", token);
                            editor.putString("password", pwd);
                            editor.commit();
                            showToastInAnyThread("登陆成功");
                            Intent intent=new Intent(act,MainActivity.class);
                            intent.putExtra("contentid", 5);
                            startActivity(intent);
                            act.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToastInAnyThread("请求异常失败");
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

    @SuppressLint("ShowToast")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.button_denglu:
                login();
                //启动定时器
//			timer.schedule(task, 0, 60*60*1000);
                break;
        }
    }

//	/**
//	 * 判断网络是否连接
//	 *
//	 * @param context
//	 * @return
//	 */
//	public boolean isNetworkConnected() {
//		ConnectivityManager mConnectivityManager = (ConnectivityManager) DengLuActivity.this
//				.getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
//		if (mNetworkInfo != null) {
//			return mNetworkInfo.isAvailable();
//		}
//		return false;
//	}

}
