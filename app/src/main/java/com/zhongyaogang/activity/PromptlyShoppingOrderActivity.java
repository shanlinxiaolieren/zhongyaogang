package com.zhongyaogang.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.zhongyaogang.R;
import com.zhongyaogang.adapter.QueRenDingDanAddressAdapter;
import com.zhongyaogang.bean.AddressBean;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.http.HttpUtils;
import com.zhongyaogang.pay.AuthResult;
import com.zhongyaogang.utils.L;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PromptlyShoppingOrderActivity extends Activity implements View.OnClickListener {
    private ImageView wodezhongxin_left;
    private QueRenDingDanAddressAdapter adapter;
    private ListView lvAddress;
    private List<AddressBean> datas;
    private SharedPreferences sp;
    private String sharedusernameid;
    private String token;
    private  Double totalPrice;
    private  PromptlyShoppingOrderActivity act;
    private String  supplyNo;
    private String  supplyId;
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
    private IWXAPI  msgApi;
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
                case 2:
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();
                    L.e("resultStatus:"+resultStatus);
                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(PromptlyShoppingOrderActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
//                        Toast.makeText(PromptlyShoppingOrderActivity.this,
//                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promptly_shopping_order);
        EventBus.getDefault().register(this);
//        msgApi = WXAPIFactory.createWXAPI(this, "wxb4ba3c02aa476ea1");
//        msgApi.sendReq("");
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
        QueRenDingDanAdd();
        supplyTitle = getIntent().getStringExtra("supplyTitle");
        supplyId = getIntent().getStringExtra("id");
        supplyNo = getIntent().getStringExtra("supplyNo");
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
        totalPrice= (Double.valueOf(price))*(Double.valueOf(quantity));
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
                    yxtReceivingaddressId=datas.get(0).getId();//
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                    L.e("返回结果：datas="+datas);

                    /**
                     *
                     */
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "查询失败", Toast.LENGTH_SHORT).show();
                }

            };
        }.start();
    }
    private  void PromptlyshoppingOrderAdd(){
      new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    String path = Constants.AT_ONCE_ORDER_ADD;
                    messages=editext_message.getText().toString();
                    L.e("返回结果：editext_messageresult"+editext_message.getText().toString());
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("supplyNo", supplyNo);//
                    params.put("supplyId", supplyId);//
                    params.put("quantity", quantity);//
                    params.put("yxtReceivingaddressId", yxtReceivingaddressId);//收货地址
                    params.put("messages", messages);//留言
                    String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
                    L.e("返回：params结果="+params);
                    L.e("返回：PromptlyshoppingOrderAdd结果="+strResult);
                    if(strResult.equals("401"))
                    {
                        Intent i=new Intent(getApplication(),DengLuActivity.class);
                        startActivity(i);
                    }
                   JSONObject object=new JSONObject(strResult);
                    object=object.getJSONObject("result");
                    String orderNo=object.getString("orderNo");
                    String orderId=object.getString("orderId");
                    String payString=object.getString("payString");
                    L.e("payString:"+payString.toString());
                    PayTask alipay = new PayTask(PromptlyShoppingOrderActivity.this);
                    Map<String, String> result = alipay.payV2(payString, true);
                    Log.i("msp", result.toString());
                    Message msg = new Message();
                    msg.what = 2;
                    msg.obj = result;
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
                PromptlyshoppingOrderAdd();
                break;
        }
    }
//        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(requestCode==101&&resultCode==102)
//        {
//            String id=data.getStringExtra("id");
//            String consignee=data.getStringExtra("consignee");
//            String telephone=data.getStringExtra("telephone");
//            String street=data.getStringExtra("street");
//            String region=data.getStringExtra("region");
//            AddressBean a=new AddressBean();
//            a.setId(id);
//            a.setConsignee(consignee);
//            a.setTelephone(telephone);
//            a.setStreet(street);
//            a.setRegion(region);
//            datas;
//        }
//    }
}
