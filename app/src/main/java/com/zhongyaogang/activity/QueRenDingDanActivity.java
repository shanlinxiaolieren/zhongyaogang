package com.zhongyaogang.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhongyaogang.R;
import com.zhongyaogang.adapter.QueRenDingDanAddressAdapter;
import com.zhongyaogang.adapter.RecycleViewAdapter;
import com.zhongyaogang.adapter.RecycleViewOrderAdapter;
import com.zhongyaogang.bean.AddressBean;
import com.zhongyaogang.bean.NewDate;
import com.zhongyaogang.bean.NewShoppingCartOrderBean;
import com.zhongyaogang.bean.OrderBean;
import com.zhongyaogang.bean.QueRenOrderBean;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.http.HttpUtils;
import com.zhongyaogang.utils.L;
import com.zhongyaogang.utils.SystemUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueRenDingDanActivity extends Activity implements OnClickListener,RecycleViewOrderAdapter.SaveEditListener {
    private ImageView wodezhongxin_left;
    private TextView textview_tijiadingdan;
    private QueRenDingDanActivity act;
    private SharedPreferences sp;
    private String sharedusernameid;
    private String token;
    private String cartsList;
    private List<AddressBean> datas;
    private QueRenDingDanAddressAdapter adapter;
    private ListView lvAddress;
    private List<QueRenOrderBean> dataList = new ArrayList<>();
    private List<NewShoppingCartOrderBean> list = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecycleViewOrderAdapter adapterOrder;
    private StringBuilder str=new StringBuilder();
    private StringBuilder strMessages=new StringBuilder();
    private  String liuyan;
    //    private NewShoppingCartOrderBean  newshoppingcartorderbean;
    private  Map<Integer, String> map = new HashMap<Integer, String>();
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(act));
                    adapterOrder = new RecycleViewOrderAdapter(list, act);
                    mRecyclerView.setAdapter(adapterOrder);
                    //适配器的接口回调
                    adapterOrder.setItemOnClick(new RecycleViewOrderAdapter.itemClickListeren() {
                        @Override
                        public void headItemClick(int position) {
                            Toast.makeText(act, list.get(position).getShopName(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void itemClick(int position) {
                            Toast.makeText(act, list.get(position).getQuantity(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void footItemClick(int position) {
//                            Toast.makeText(act, "付款", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case 2:
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
        setContentView(R.layout.activity_que_ren_ding_dan);
        act=this;
        intView();
    }

    private void intView() {
        wodezhongxin_left = (ImageView) this.findViewById(R.id.wodezhongxin_left);
        textview_tijiadingdan = (TextView) this.findViewById(R.id.textview_tijiadingdan);
        wodezhongxin_left.setOnClickListener(this);
        textview_tijiadingdan.setOnClickListener(this);
        mRecyclerView = (RecyclerView)findViewById(R.id.mRecyclerView_order);
        lvAddress= (ListView) findViewById(R.id.lvAddress_querendingdan);
        sp = this.getSharedPreferences("config", 0);
        sharedusernameid = sp.getString("usernameid", "");
        token=sp.getString("token", "");

           cartsList=getIntent().getStringExtra("cartsList");
        QueRenDingDanAddressQuery();
        QueRenDingDanQuery();

    }
    private void QueRenDingDanQuery(){
        new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    String path = Constants.QUEREN_ORDER_QUERY;
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("cartsList", cartsList);
                    String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
                    if (strResult.equals("401")){
                        Intent intent=new Intent(act,DengLuActivity.class);
                        startActivity(intent);
                        act.finish();
                    }
                    JSONObject jo = new JSONObject(strResult);
                    JSONArray result=jo.getJSONArray("result");
                    L.e("返回结果：result"+result);
                    dataList.addAll(SystemUtil.jsonToList(result.toString(), QueRenOrderBean.class));
                    // 1 表示是商品的头部标题
                    // 2 表示是商品的item的布局
                    // 3 表示的是底部的item的布局
                    for (int i = 0; i < dataList.size(); i++) {
                        list.add(new NewShoppingCartOrderBean(1,dataList.get(i).getShopName(),"", "", "", "", "", "", "","",""));
                        for (int j = 0; j < dataList.get(i).getOrderItem().size(); j++) {
                            list.add(new NewShoppingCartOrderBean(2, "",dataList.get(i).getOrderItem().get(j).getMerchandiseName(), dataList.get(i).getOrderItem().get(j).getMoq(),
                                    dataList.get(i).getOrderItem().get(j).getUnits(), dataList.get(i).getOrderItem().get(j).getPigUrl(),
                                    dataList.get(i).getOrderItem().get(j).getStock(),dataList.get(i).getOrderItem().get(j).getPrice(),dataList.get(i).getOrderItem().get(j).getQuantity(),"",""));
                            L.e("s.getMerchandiseName()="+dataList.get(i).getOrderItem().get(j).getMerchandiseName());
                        }
                        list.add(new NewShoppingCartOrderBean(3, "", "", "", "","","","","",dataList.get(i).getFreightTitle(),dataList.get(i).getTotalPrice()));
                        if (i==dataList.size()-1) {
                            str.append(dataList.get(i).getShopId());
                        }else {
                            str.append(dataList.get(i).getShopId());
                            str.append("&");
                        }
                    }
                   L.e("str="+str);
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
    private void QueRenDingDanAddressQuery(){
        // 数据应该提交给服务器 由服务器比较是否正确
        new Thread() {
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
                    L.e("返回结果：QueRenDingDanAddressQuery"+strResult);
                    JSONObject jo = new JSONObject(strResult);
                    JSONObject	body1 = jo.getJSONObject("result");
                    JSONArray items=body1.getJSONArray("items");
                    L.e("返回结果：QueRenDingDanAddressitems="+items);
                    Gson gson = new Gson();
                    datas=gson.fromJson(items.toString(),new  TypeToken<List<AddressBean>>(){}.getType());
                    Message msg = new Message();
                    msg.what = 2;
                    mHandler.sendMessage(msg);
                    L.e("返回结果：datas="+datas);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "查询失败", Toast.LENGTH_SHORT).show();
                }
            };
        }.start();
    }
    private  void QueRenDingDanAdd(){
        new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    String path = Constants.SUBMIT_ORDER_ADD;
//                    messages=shopId+"&"+editext_message.getText().toString();
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("cartsList", cartsList);
                    params.put("yxtReceivingaddressId", sharedusernameid);
//                    params.put("messages", messages);
                    String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
                    if (strResult.equals("401")){
                        Intent intent=new Intent(act,DengLuActivity.class);
                        startActivity(intent);
                        act.finish();
                    }

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
            case R.id.wodezhongxin_left:
                finish();
                break;
            case R.id.textview_tijiadingdan:
                QueRenDingDanAdd();
                break;
        }
    }
    @Override
    public void SaveEdit(int position, String string) {
        //回调处理edittext内容，使用map的好处在于：position确定的情况下，string改变，只会动态改变string内容
        map.put(position,string);

        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            liuyan=entry.getValue();
            L.e("liuyan="+entry.getValue());
        }
//        for (String v : map.values()) {
//            L.e("messages2="+v);
//        }
        L.e("map="+map);

        strMessages.append(liuyan);
        strMessages.append("&");
        L.e("strMessages="+strMessages);

    }

}
