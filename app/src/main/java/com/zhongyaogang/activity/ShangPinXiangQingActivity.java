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
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhongyaogang.R;
import com.zhongyaogang.adapter.ShoppingXiangQingAdapter;
import com.zhongyaogang.bean.ShoppingXiangQingBean;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.http.HttpUtils;
import com.zhongyaogang.utils.L;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;

@SuppressWarnings("deprecation")
@SuppressLint("HandlerLeak")
public class ShangPinXiangQingActivity extends Activity implements OnClickListener {
    private RelativeLayout top_relativelayout;
    private LinearLayout linearlayout_bottom;
    private String token;
    private String id;
    private String merchandiseName;
    private ShoppingXiangQingBean datas;
    private ShoppingXiangQingAdapter adapter;
    private ListView shangpinxiangqing;
    private TextView index_search_yaocaiwang;
    private ImageView wodezhongxin_left;
    private ImageView wodezhongxin_right;
    private ShangPinXiangQingActivity act;
    private Button button_chuangpinxiangqing;
    private Button button_shopping;
    private SharedPreferences sp;
    private String sharedusernameid;
    private String supplyUID;
    private String supplyNo;
    private String supplyTitle;
    private String stock;
    private String shopName;
    private String freightTitle;
    private String price;
    private String moq;
    private String units;
    private String shopId;
    private String shoppingId;
    private String quantity;
    private   String [] resultPigurl = null;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    adapter = new ShoppingXiangQingAdapter(act, datas);
                    shangpinxiangqing.setAdapter(adapter);
                    break;
                case 2:
                    showPopwindowAbove();
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
        setContentView(R.layout.activity_shang_pin_xiang_qing);
        act = this;
        intView();
    }

    private void intView() {
        top_relativelayout = (RelativeLayout) findViewById(R.id.top_relativelayout);
        linearlayout_bottom = (LinearLayout) findViewById(R.id.linearlayout_bottom);
        shangpinxiangqing = (ListView) findViewById(R.id.shangpinxiangqing);
        index_search_yaocaiwang = (TextView) findViewById(R.id.index_search_yaocaiwang);
        wodezhongxin_left = (ImageView) findViewById(R.id.wodezhongxin_left);
        wodezhongxin_right = (ImageView) findViewById(R.id.wodezhongxin_right);
        button_chuangpinxiangqing = (Button) findViewById(R.id.button_chuangpinxiangqing);
        button_shopping = (Button) findViewById(R.id.button_shopping);
        id = getIntent().getStringExtra("id");
        merchandiseName = getIntent().getStringExtra("merchandiseName");
        index_search_yaocaiwang.setText(merchandiseName);
        wodezhongxin_left.setOnClickListener(this);
        wodezhongxin_right.setOnClickListener(this);
        button_chuangpinxiangqing.setOnClickListener(this);
        button_shopping.setOnClickListener(this);
        sp = this.getSharedPreferences("config", 0);
        sharedusernameid = sp.getString("usernameid", "");
        token = sp.getString("token", "");
        shoppingXiangQingQuery();
    }
    private void shoppingXiangQingQuery() {
        // 数据应该提交给服务器 由服务器比较是否正确
        new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    String path = Constants.SHOPPINGDETAILS_QUERY;
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("id", id);
                    String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
                    if (strResult.equals(401)){
                        Intent intent=new Intent(act,DengLuActivity.class);
                        startActivity(intent);
                        act.finish();
                    }
                    L.e("返回结果：shoppingresult" + strResult);
                        JSONObject jo = new JSONObject(strResult);
                        JSONObject	body1 = jo.getJSONObject("result");
                        L.e("返回结果：body1="+body1);
                        Gson gson = new Gson();
                        datas=gson.fromJson(body1.toString(),new  TypeToken<ShoppingXiangQingBean>(){}.getType());
                        supplyNo=datas.getSupplyNo();
                        supplyUID=datas.getSupplyUID();
                       supplyTitle=datas.getSupplyTitle();
                        stock=datas.getStock();
                       shopName=datas.getShopName();
                       freightTitle=datas.getFreightTitle();
                         price=datas.getPrice();
                        moq=datas.getMoq();
                    shopId=datas.getShopId();
                        units=datas.getUnits();
                    shoppingId=datas.getId();
                    quantity=datas.getQuantity();
                    resultPigurl=datas.getPigUrl().split(",");
                        Message msg = new Message();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "查询失败",
                            Toast.LENGTH_SHORT).show();
                }
            };
        }.start();
    }
    private void shoppingAdd(){
        // 数据应该提交给服务器 由服务器比较是否正确
        new Thread() {
            public void run() {
                try {

                    Looper.prepare();
                    String path = Constants.SHOPPING_ADD;
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("id", "0");
                    params.put("userId",sharedusernameid);
                    params.put("supplyUserId", supplyUID);
                    params.put("supplyNo",supplyNo);
                    params.put("quantity",moq);
                    String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
                    if (strResult.equals(401)){
                        Intent intent=new Intent(act,DengLuActivity.class);
                        startActivity(intent);
                        act.finish();
                    }
                    L.e("返回结果：shoppingAdd=" + strResult);
                    Message msg = new Message();
                    msg.what = 2;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "添加失败",
                            Toast.LENGTH_SHORT).show();
                }
            };
        }.start();
    }
    private void showPopwindowAbove() {
        View parent = (top_relativelayout).getChildAt(0);
        View popView = View.inflate(act, R.layout.above_pop_menu, null);
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
                        Intent intent = new Intent(act, ShoppingCartActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.btn_camera_pop_album:
                        popWindow.dismiss();
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
//        popWindow.showAtLocation(parent, Gravity.TOP, 0, 0);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wodezhongxin_left:
                this.finish();
                break;
            case R.id.wodezhongxin_right:
                showPopwindowMenu(v);
                break;
            case R.id.button_shopping:
                shoppingAdd();
                break;
            case R.id.button_chuangpinxiangqing:
                Intent intent=new Intent(act, PromptlyShoppingOrderActivity.class);
                intent.putExtra("id", shoppingId);
                intent.putExtra("supplyNo", supplyNo);
                intent.putExtra("supplyTitle", supplyTitle);
                intent.putExtra("stock", stock);
                intent.putExtra("merchandiseName", merchandiseName);
                intent.putExtra("shopName", shopName);
                intent.putExtra("freightTitle", freightTitle);
                intent.putExtra("price", price);
                intent.putExtra("moq", moq);
                intent.putExtra("units", units);
                intent.putExtra("pigUrl", resultPigurl[0]);
                intent.putExtra("quantity", quantity);
                startActivity(intent);
                act.finish();
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
                        ShangPinXiangQingActivity.this.finish();;
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
}
