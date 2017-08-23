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
import com.zhongyaogang.adapter.AddressAdapter;
import com.zhongyaogang.bean.AddressBean;
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

@SuppressLint("HandlerLeak")
public class AddressActivity extends Activity implements OnClickListener {
    protected static final String TAG_ADDRESS = "address";
    private ImageView wodezhongxin_left_address;
    private Button button_addressadd;
    private ImageView wodezhongxin_right_address;
    private AddressActivity act;
    private TextView textview_moren;
    private ListView lvAddress;
    private AddressAdapter adapter;
    private List<AddressBean> datas;
    private SharedPreferences sp;
    private String sharedusernameid;
    private String token;
private  String  tag;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    adapter = new AddressAdapter(act, datas);
                    if(tag!=null)
                    {
              if(tag.equals("0"))
              {
                  adapter.Settrue(false);
              }
                    else
              {
                  adapter.Settrue(true);
              }
                    }
                    lvAddress.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    adapter = new AddressAdapter(act, datas);
                    if(tag!=null) {
                        if (tag.equals("0")) {
                            adapter.Settrue(false);
                        } else {
                            adapter.Settrue(true);
                        }
                    }
                    lvAddress.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;

                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去除掉当前activity头title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_address);
        tag=getIntent().getStringExtra("tag");
        EventBus.getDefault().register(this);
        act = this;
        intView();
    }
    private void addressDelete(final String id){
        new Thread() {
            @SuppressWarnings("deprecation")
            public void run() {
                try {
                    Looper.prepare();
                    String path = Constants.DELETE_ADDRESS;
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("id", id);
                    String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
                    L.e("返回结果：deleteresult"+strResult);
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
    private void intView() {
        wodezhongxin_left_address = (ImageView) findViewById(R.id.wodezhongxin_left_address);
        wodezhongxin_right_address = (ImageView) findViewById(R.id.wodezhongxin_right_address);
        textview_moren = (TextView) findViewById(R.id.textview_moren);
        button_addressadd = (Button) findViewById(R.id.button_addressadd);
        lvAddress = (ListView) findViewById(R.id.lvAddress);
        wodezhongxin_left_address.setOnClickListener(this);
        wodezhongxin_right_address.setOnClickListener(this);
        button_addressadd.setOnClickListener(this);

        sp = this.getSharedPreferences("config", 0);
        sharedusernameid = sp.getString("usernameid", "");
        token=sp.getString("token", "");
        addressQuery();
    }
    @Override
    protected void onStart() {
        super.onStart();
        addressQuery();
    }
    @Subscribe
    public void onEventMainThread(String addressId){
        addressDelete(addressId);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    private void addressQuery(){
        // 数据应该提交给服务器 由服务器比较是否正确
        new Thread() {
            @SuppressWarnings("deprecation")
            public void run() {
                try {
                    Looper.prepare();
                    String path = Constants.QUERY_ADDRESS;
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("id", sharedusernameid);
                    String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
                        L.e("返回结果：Addressresult="+strResult);
                    if (strResult.equals("401")){
                        Intent intent=new Intent(act,DengLuActivity.class);
                        startActivity(intent);
                        act.finish();
                    }
                        JSONObject jo = new JSONObject(strResult);
                        JSONObject	body1 = jo.getJSONObject("result");
                    L.e("返回结果：body1="+body1.toString());
                    JSONArray items=body1.getJSONArray("items");
                        L.e("返回结果：Addressitems="+items);
                        Gson gson = new Gson();
                        datas=gson.fromJson(items.toString(),new  TypeToken<List<AddressBean>>(){}.getType());
                        Message msg = new Message();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                        L.e("返回结果：datas="+datas);
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
            case R.id.button_addressadd:
                Intent intent = new Intent(AddressActivity.this,
                        AddressAddActivity.class);
                intent.putExtra("tag","0");
                startActivity(intent);
                //startActivityForResult(intent, 1);
                break;
            case R.id.wodezhongxin_left_address:
                finish();
                break;
            case R.id.wodezhongxin_right_address:
                showPopwindowMenu(v);
                break;
        }

    }

    private void showPopwindowMenu(View parent) {
        View popView = View.inflate(act, R.layout.right_pop_menu, null);
        Button btnCamera = (Button) popView
                .findViewById(R.id.btn_camera_pop_camera);
        Button btnAlbum = (Button) popView
                .findViewById(R.id.btn_camera_pop_album);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        final PopupWindow popWindow = new PopupWindow(popView, width, height);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(false);// 设置允许在外点击消失
        OnClickListener listener = new OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_camera_pop_camera:
                        Intent intent = new Intent(act, MainActivity.class);
                        intent.putExtra("contentid", 1);
                        startActivity(intent);
                        AddressActivity.this.finish();
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
        popWindow.showAsDropDown(parent, 10, 20);
        // popWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//      if(requestCode==101&&resultCode==102)
//      {
//          String id=data.getStringExtra("id");
//          String consignee=data.getStringExtra("consignee");
//          String telephone=data.getStringExtra("telephone");
//          String street=data.getStringExtra("street");
//          String region=data.getStringExtra("region");
//
//      }
//   }
}
