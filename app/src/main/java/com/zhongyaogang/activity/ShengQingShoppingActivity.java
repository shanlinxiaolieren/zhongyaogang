package com.zhongyaogang.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhongyaogang.R;
import com.zhongyaogang.adapter.ShengQingShoppingAdapter;
import com.zhongyaogang.bean.ShengQingShoppingBean;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.http.HttpUtils;
import com.zhongyaogang.utils.L;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;

@SuppressWarnings("deprecation")
@SuppressLint("HandlerLeak")
public class ShengQingShoppingActivity extends Activity implements OnClickListener{
    private ImageView wodezhongxin_left;
    private ImageView wodezhongxin_right;
    private TextView index_search_yaocaiwang;
    private Button button_addressadd;
    private ListView lvAddress;
    private ShengQingShoppingActivity act;
    private SharedPreferences sp;
    private String sharedusernameid;
    private String token;
    private List<ShengQingShoppingBean> datas;
    private ShengQingShoppingAdapter adapter;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    adapter=new ShengQingShoppingAdapter(act, datas);
                    lvAddress.setAdapter(adapter);
                    break;
                case 2:
                    adapter=new ShengQingShoppingAdapter(act, datas);
                    adapter.notifyDataSetChanged();
                    lvAddress.setAdapter(adapter);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sheng_qing_shopping);
        EventBus.getDefault().register(this);
        act=this;
        intView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        shengQingShoppingQuary();
    }

    @Subscribe
    public void onEventMainThread(String Id){
        shengQingShoppingDelete(Id);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    private void intView() {
        wodezhongxin_left = (ImageView) this.findViewById(R.id.wodezhongxin_left);
        wodezhongxin_right = (ImageView) this.findViewById(R.id.wodezhongxin_right);
        button_addressadd = (Button) this.findViewById(R.id.button_addressadd);
        lvAddress=(ListView)findViewById(R.id.listview_shengqing);
        index_search_yaocaiwang=(TextView) findViewById(R.id.index_search_yaocaiwang);
        index_search_yaocaiwang.setText("申请商户");
        wodezhongxin_right.setOnClickListener(this);
        wodezhongxin_left.setOnClickListener(this);
        button_addressadd.setOnClickListener(this);
        sp = this.getSharedPreferences("config", MODE_PRIVATE);
        sharedusernameid = sp.getString("usernameid", "");
        token=sp.getString("token", "");
    }
    private void shengQingShoppingDelete(final String id){
        new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    String path = Constants.SHENGQINGSHOPPING_DELETE;
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("id", id);
                    String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
                    L.e("返回结果：Addressresult"+strResult);
                    if (strResult.equals("401")){
                        Intent intent=new Intent(act,DengLuActivity.class);
                        startActivity(intent);
                        act.finish();
                    }
                    Message msg = new Message();
                        msg.what = 2;
                        mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(act, "删除失败", Toast.LENGTH_SHORT).show();
                }

            };
        }.start();
    }
    private void shengQingShoppingQuary(){
        // 数据应该提交给服务器 由服务器比较是否正确
        new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    String path = Constants.SHENGQINGSHOPPING_QUERY;
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("userID", sharedusernameid);
                    String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
                    L.e("返回结果：Addressresult"+strResult);
                    if (strResult.equals("401")){
                        Intent intent=new Intent(act,DengLuActivity.class);
                        startActivity(intent);
                        act.finish();
                    }
                    JSONObject jo = new JSONObject(strResult);
                        JSONObject	body1 = jo.getJSONObject("result");
                        JSONArray items=body1.getJSONArray("items");
                        L.e("返回结果：items="+items);
                        Gson gson = new Gson();
                        datas=gson.fromJson(items.toString(),new  TypeToken<List<ShengQingShoppingBean>>(){}.getType());
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "查询失败", Toast.LENGTH_SHORT).show();
                }
            };
        }.start();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wodezhongxin_right:
                showPopwindowMenu(v);
                break;
            case R.id.button_addressadd:
                if (datas!=null&&datas.size()>0) {
                    Toast.makeText(getApplicationContext(), "商户已经存在", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    startActivity(new Intent(this, ShengQingShoppingAddActivity.class));
                }
                break;

            case R.id.wodezhongxin_left:
                finish();
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
                        ShengQingShoppingActivity.this.finish();
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
