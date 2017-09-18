package com.zhongyaogang.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zhongyaogang.R;
import com.zhongyaogang.activity.DengLuActivity;
import com.zhongyaogang.adapter.RecycleViewAdapter;
import com.zhongyaogang.bean.NewDate;
import com.zhongyaogang.bean.OrderBean;
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

public class DaiFaHuoFragment extends Fragment{
    private RecyclerView mRecyclerView;
    private  DaiFaHuoFragment act;
    private Context context;
    private List<OrderBean> dataList = new ArrayList<>();
    private List<NewDate> list = new ArrayList<>();
    private SharedPreferences sp;
    private String token;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 2:
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                    adapter = new RecycleViewAdapter(list, context);
                    mRecyclerView.setAdapter(adapter);
                    //适配器的接口回调
                    adapter.setItemOnClick(new RecycleViewAdapter.itemClickListeren() {
                        @Override
                        public void headItemClick(int position) {
                            Toast.makeText(getActivity(), list.get(position).getShopName(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void itemClick(int position) {
                            Toast.makeText(getActivity(), list.get(position).getQuantity(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void footItemClick(int position) {
                            Toast.makeText(getActivity(), "付款", Toast.LENGTH_SHORT).show();
                        }
                    });
//                    //模拟下拉加载更多 5s加载第二页数据
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            for (int i = 0; i < dataList.size(); i++) {
//                                list.add(new NewDate(1, dataList.get(i).getShopName(),dataList.get(i).getOrderState(), "", "", "", "", "", "",""));
//                                for (int j = 0; j < dataList.get(i).getOrderItem().size(); j++) {
//                                    list.add(new NewDate(2, "","", dataList.get(i).getOrderItem().get(j).getImagesUrl(), dataList.get(i).getOrderItem().get(j).getMerchandiseName(),
//                                            dataList.get(i).getOrderItem().get(j).getActualPrice(),"","",dataList.get(i).getOrderItem().get(j).getQuantity(),""));
//                                }
//                                list.add(new NewDate(3, "", "", "", "","",dataList.get(i).getFarePrice(),dataList.get(i).getPayable(),"",dataList.get(i).getStateCode()));
//                            }
//                            mHandler.sendEmptyMessage(1);
//                        }
//                    }, 5000);
                    break;
            }
        }
    };
    /**
     * 处理fragment重复叠加的问题
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);//
    }
    private RecycleViewAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.daifahuofragment, container, false);
        this.act=DaiFaHuoFragment.this;
        context=getActivity();
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        daifahuoQuery();
    }

    private void daifahuoQuery() {
        // 数据应该提交给服务器 由服务器比较是否正确
        new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    String path = Constants.ORDER_QUERY;
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("orderState", "8");
                    String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
                    L.e("返回结果：Queryresult"+strResult);
                    if (strResult.equals("401")){
                        Intent intent=new Intent(getActivity(),DengLuActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    JSONObject jo = new JSONObject(strResult);
                    JSONArray result=jo.getJSONArray("result");
                    L.e("返回结果：result"+result);
                    dataList.addAll(SystemUtil.jsonToList(result.toString(), OrderBean.class));
                    //对数据源进行拆分 这里的NewDate里面的
                    // 1 表示是商品的头部标题
                    // 2 表示是商品的item的布局
                    // 3 表示的是底部的item的布局
                    for (int i = 0; i < dataList.size(); i++) {
                        list.add(new NewDate(1, dataList.get(i).getShopName(),dataList.get(i).getOrderState(), "", "", "", "", "", "",""));
                        for (int j = 0; j < dataList.get(i).getOrderItem().size(); j++) {
                            list.add(new NewDate(2, "","", dataList.get(i).getOrderItem().get(j).getImagesUrl(), dataList.get(i).getOrderItem().get(j).getMerchandiseName(),
                                    dataList.get(i).getOrderItem().get(j).getActualPrice(),"","",dataList.get(i).getOrderItem().get(j).getQuantity(),""));
                        }
                        list.add(new NewDate(3, "", "", "", "","",dataList.get(i).getFarePrice(),dataList.get(i).getPayable(),"",dataList.get(i).getStateCode()));
                    }
                    Message msg = new Message();
                    msg.what = 2;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "查询失败",
                            Toast.LENGTH_SHORT).show();
                }
            };
        }.start();
    }
    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);
        sp = getActivity().getSharedPreferences("config", 0);
        token = sp.getString("token", "");
    }

}
