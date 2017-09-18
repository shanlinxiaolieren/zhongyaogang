package com.zhongyaogang.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhongyaogang.R;
import com.zhongyaogang.adapter.QueRenDingDanAddressAdapter;
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

public class DanGeQueRenDingDanActivity extends Activity implements OnClickListener{
    private ImageView wodezhongxin_left;
    private QueRenDingDanAddressAdapter adapter;
    private ListView lvAddress;
    private List<AddressBean> datas;
    private SharedPreferences sp;
    private String sharedusernameid;
    private String token;
    private  Double totalPrice;
    private  DanGeQueRenDingDanActivity act;
    private String  shopId;
    private String  supplyTitle;
    private String  stock;
    private String  merchandiseName;
    private String  shopName;
    private String  freightTitle;
    private String  price;
    private String  moq;
    private String  units;
    private String  quantity;
    private String  pigUrl;
    private String  cartsList;
    private TextView tvshopName;
    private TextView tvstock;
    private TextView tv_quantity;
    private TextView textview_xiaojiprice;
    private TextView textview_hejiprice;
    private TextView tvfreightTitle;
    private TextView textview_zhelinali;
    private TextView textview_yaomoqunits;
    private TextView textview_zhelinali_price;
    private TextView textview_tijiadingdan;
    private EditText editext_message;
    private  ImageView imageview_pigurl;
    private  String yxtReceivingaddressId;
    private  String messages;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    adapter = new QueRenDingDanAddressAdapter(act, datas);
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dan_ge_que_ren_ding_dan);
        EventBus.getDefault().register(this);
        act = this;
        intView();
    }
    @Subscribe
    public void onEventMainThread(String addressId){
        yxtReceivingaddressId=addressId;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        QueRenDingDanAdd();
    }

    private void intView() {
        wodezhongxin_left = (ImageView) findViewById(R.id.wodezhongxin_left);
        imageview_pigurl = (ImageView) findViewById(R.id.imageview_pigurl);
        textview_tijiadingdan = (TextView) findViewById(R.id.textview_tijiadingdan);
        tvshopName = (TextView) findViewById(R.id.shopName);
        textview_xiaojiprice = (TextView) findViewById(R.id.textview_xiaojiprice);
        tvstock = (TextView) findViewById(R.id.tvstock);
        tv_quantity = (TextView) findViewById(R.id.tv_quantity);
        textview_hejiprice = (TextView) findViewById(R.id.textview_hejiprice);
        editext_message = (EditText) findViewById(R.id.editext_message);
        tvfreightTitle = (TextView) findViewById(R.id.tvfreightTitle);
        textview_zhelinali_price = (TextView) findViewById(R.id.textview_zhelinali_price);
        textview_yaomoqunits = (TextView) findViewById(R.id.textview_yaomoqunits);
        textview_zhelinali = (TextView) findViewById(R.id.textview_zhelinali);
        lvAddress= (ListView) findViewById(R.id.lvAddress_dangequerendingdan);
        wodezhongxin_left.setOnClickListener(this);
        textview_tijiadingdan.setOnClickListener(this);
        sp = this.getSharedPreferences("config", 0);
        sharedusernameid = sp.getString("usernameid", "");
        token=sp.getString("token", "");

        supplyTitle = getIntent().getStringExtra("supplyTitle");
        shopId = getIntent().getStringExtra("shopId");
        stock = getIntent().getStringExtra("stock");
        merchandiseName = getIntent().getStringExtra("merchandiseName");
        shopName = getIntent().getStringExtra("shopName");
        freightTitle = getIntent().getStringExtra("freightTitle");
        price = getIntent().getStringExtra("price");
        moq = getIntent().getStringExtra("moq");
        units = getIntent().getStringExtra("units");
        pigUrl = getIntent().getStringExtra("pigUrl");
        quantity = getIntent().getStringExtra("quantity");
       tvshopName.setText(shopName);
        tvstock.setText(stock);
        textview_zhelinali.setText(supplyTitle);
        tv_quantity.setText(quantity);
        tvfreightTitle.setText(freightTitle);
        textview_zhelinali_price.setText("¥"+price);
        textview_yaomoqunits.setText(merchandiseName+moq+units);
//        try {
        totalPrice= (Double.valueOf(price))*(Double.valueOf(quantity));
        L.e("返回结果：totalPrice="+totalPrice);
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
        ImageLoader.getInstance().displayImage(pigUrl, imageview_pigurl);
        textview_xiaojiprice.setText("¥"+String.valueOf(totalPrice));
        textview_hejiprice.setText("¥"+String.valueOf(totalPrice));
        cartsList=getIntent().getStringExtra("cartsList");

    }
    private void QueRenDingDanAdd(){
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
                    if (strResult.equals("401")){
                        Intent intent=new Intent(act,DengLuActivity.class);
                        startActivity(intent);
                        act.finish();
                    }
                    L.e("返回结果：QueRenDingDanAddressresult"+strResult);
                    JSONObject jo = new JSONObject(strResult);
                    JSONObject	body1 = jo.getJSONObject("result");
                    JSONArray items=body1.getJSONArray("items");
                    L.e("返回结果：QueRenDingDanAddressitems="+items);
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
    private  void shoppingCartOrderAdd(){
        if(datas.size()==0)
        {
            Toast.makeText(act, "请添加默认地址", Toast.LENGTH_SHORT).show();
        }
        new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    String path = Constants.SUBMIT_ORDER_ADD;
                    messages=shopId+"&"+editext_message.getText().toString();
                    L.e("返回结果：editext_messageresult"+editext_message.getText().toString());
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("cartsList", cartsList);//购物车id
                    params.put("yxtReceivingaddressId", yxtReceivingaddressId);//收货地址
                    params.put("messages", messages);//店铺id&留言
                    String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
                    L.e("返回：params结果="+params);
                    L.e("返回：CreateYXTOrderForAppAsync结果="+strResult);



                    Message msg = new Message();
                    msg.what = 2;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(act, "添加失败", Toast.LENGTH_SHORT).show();
                }
            };
        }.start();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wodezhongxin_left:
                act.finish();
                break;
            case R.id.textview_tijiadingdan:
                shoppingCartOrderAdd();
                break;

        }
    }



}
