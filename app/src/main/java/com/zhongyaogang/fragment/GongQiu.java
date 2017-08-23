package com.zhongyaogang.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhongyaogang.activity.DengLuActivity;
import com.zhongyaogang.activity.GongHuoActivity;
import com.zhongyaogang.R;
import com.zhongyaogang.bean.GongBean;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.http.HttpUtils;
import com.zhongyaogang.utils.L;

@SuppressWarnings({ "unused", "deprecation" })
@SuppressLint("HandlerLeak")
public class GongQiu extends Fragment{
    private ListView listview_wodefabu;
    private List<GongBean> datas;
    private GongQiu act;
    private ListAdapter mListAdapter;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mListAdapter=new ListAdapter(datas, act);
                    mListAdapter.notifyDataSetChanged();
                    listview_wodefabu.setAdapter(mListAdapter);
                    break;
                case 2:
                    mListAdapter=new ListAdapter(datas, act);
                    mListAdapter.notifyDataSetChanged();
                    listview_wodefabu.setAdapter(mListAdapter);
                    break;
                default:
                    break;
            }
        }
    };
    private SharedPreferences sp;
    private String token;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.wodefabu_listview, container, false);
        this.act=GongQiu.this;
        EventBus.getDefault().register(this);
        initView(view);
        return view;
    }
    /**
     * 处理fragment重复叠加的问题
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);//
    }
    @Subscribe
    public void onEventMainThread(String Id){
        gongDelete(Id);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    private void gongQuery(){
        // 数据应该提交给服务器 由服务器比较是否正确
        new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    String path = Constants.GONG_QUERY;
                    Map<String,String> params = new HashMap<String,String>();
                    String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
                    L.e("返回结果：gongQuery="+strResult);
                    if (strResult.equals("401")){
                        Intent intent=new Intent(getActivity(),DengLuActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                        JSONObject jo = new JSONObject(strResult);
                        JSONObject	body1 = jo.getJSONObject("result");
                        JSONArray items=body1.getJSONArray("items");
                        L.e("返回结果：gongitems="+items);
                        Gson gson = new Gson();
                        datas=gson.fromJson(items.toString(),new  TypeToken<List<GongBean>>(){}.getType());
                        Message msg = new Message();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "查询失败", Toast.LENGTH_SHORT).show();
                }

            };
        }.start();
    }
    private void initView(View view) {
        listview_wodefabu=(ListView) view.findViewById(R.id.listview_wodefabu);
        sp =getActivity().getSharedPreferences("config", 0);
        token = sp.getString("token", "");
        gongQuery();
    }
    private void gongDelete(final String id){
        new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    String path = Constants.GONG_DELETE;
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("id", id);
                    String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
                    if (strResult.equals("401")){
                        Intent intent=new Intent(getActivity(),DengLuActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    Message msg = new Message();
                        msg.what = 2;
                        mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
                }

            };
        }.start();
    }
    class ListAdapter extends BaseAdapter {
        private List<GongBean> mListData;
        private GongQiu act;
        private GongBean gongbean;

        public ListAdapter(List<GongBean> mListData, GongQiu act) {
            super();
            this.mListData = mListData;
            this.act = act;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            String [] resultPigurl = null;
            final int dex=position;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.wodefabugong, null);
                holder = new ViewHolder();
                holder.imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);
                holder.textview_edit = (TextView) convertView.findViewById(R.id.textview_edit);
                holder.merchandiseName = (TextView) convertView
                        .findViewById(R.id.merchandiseName);
                holder.gong_price = (TextView) convertView
                        .findViewById(R.id.gong_price);
                holder.gong_delete = (TextView) convertView
                        .findViewById(R.id.gong_delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String result=mListData.get(position).getPigUrl();
            resultPigurl=result.split(",");
            ImageLoader.getInstance().displayImage(resultPigurl[0],
                    holder.imageView1);
            holder.merchandiseName.setText(mListData.get(position)
                    .getMerchandiseName());
            holder.gong_price.setText(mListData.get(position)
                    .getPrice());
            holder.gong_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("是否删除该供货信息")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EventBus.getDefault().post(mListData.get(dex).getId());
                                    mListData.remove(dex);
                                }
                            })
                            .show();
                }
            });
            holder.textview_edit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    gongbean=mListData.get(dex);
                    Intent intent=new Intent(getActivity(), GongHuoActivity.class);
                    intent.putExtra("id", gongbean.getId());
                    intent.putExtra("supplyTitle", gongbean.getSupplyTitle());
                    intent.putExtra("price", gongbean.getPrice());
                    intent.putExtra("details", gongbean.getDetails());
                    intent.putExtra("upTime", gongbean.getUpTime());
                    intent.putExtra("downTime", gongbean.getDownTime());
                    intent.putExtra("stock", gongbean.getStock());
                    intent.putExtra("isVoucher", gongbean.getIsVoucher());
                    intent.putExtra("pigUrl", gongbean.getPigUrl());
                    intent.putExtra("supplyUID", gongbean.getSupplyUID());
                    intent.putExtra("units", gongbean.getUnits());
                    intent.putExtra("supplyUserName", gongbean.getSupplyUserName());
                    intent.putExtra("types", gongbean.getTypes());
                    intent.putExtra("repertory", gongbean.getRepertory());
                    intent.putExtra("merchandiseName", gongbean.getMerchandiseName());
                    intent.putExtra("originName", gongbean.getOriginName());
                    intent.putExtra("warehouse", gongbean.getWarehouse());
                    intent.putExtra("figureId", gongbean.getFigureId());
                    intent.putExtra("standard", gongbean.getStandard());
                    intent.putExtra("moq", gongbean.getMoq());
                    startActivity(intent);
                }
            });
            return convertView;
        }
        class ViewHolder {
            ImageView imageView1;
            TextView textview_edit;
            TextView merchandiseName;
            TextView gong_price;
            TextView gong_delete;
        }
    }
}
