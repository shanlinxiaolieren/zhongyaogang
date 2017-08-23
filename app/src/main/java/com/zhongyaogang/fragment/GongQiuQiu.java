package com.zhongyaogang.fragment;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhongyaogang.activity.DengLuActivity;
import com.zhongyaogang.activity.QiuGouActivity;
import com.zhongyaogang.R;
import com.zhongyaogang.bean.QiuBean;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.http.HttpUtils;
import com.zhongyaogang.utils.L;

@SuppressWarnings("deprecation")
@SuppressLint("HandlerLeak")
public class GongQiuQiu extends Fragment{
    private ListView listview_wodefabu;
    private List<QiuBean> datas;
    private GongQiuQiu act;
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
    private void qiuQuery(){
        // 数据应该提交给服务器 由服务器比较是否正确
        new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    String path = Constants.QIU_QUERY;
                    Map<String,String> params = new HashMap<String,String>();
                    String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
                    L.e("返回结果：Addressresult"+strResult);
                    if (strResult.equals("401")){
                        Intent intent=new Intent(getActivity(),DengLuActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
//                        L.e("返回结果：result"+strResult);
                        JSONObject jo = new JSONObject(strResult);
                        JSONObject	body1 = jo.getJSONObject("result");
                        JSONArray items=body1.getJSONArray("items");
                        L.e("返回结果：qiuitems="+items);
                        Gson gson = new Gson();
                        datas=gson.fromJson(items.toString(),new  TypeToken<List<QiuBean>>(){}.getType());
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

    @Subscribe
    public void onEventMainThread(String Id){
        qiuDelete(Id);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    private void qiuDelete(final String id){
        new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    String path = Constants.QIU_DELETE;
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("id", id);
                    String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
                    L.e("返回结果：qiuDeleteresult"+strResult);
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
    private void initView(View view) {
        listview_wodefabu=(ListView) view.findViewById(R.id.listview_wodefabu);
        sp =getActivity().getSharedPreferences("config", 0);
        token = sp.getString("token", "");
        qiuQuery();
    }
    class ListAdapter extends BaseAdapter {
        private List<QiuBean> mListData;
        private GongQiuQiu act;
        private QiuBean qiubean;

        public ListAdapter(List<QiuBean> mListData, GongQiuQiu act) {
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
            final int dex=position;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.wodefabuqiu, null);
                holder = new ViewHolder();
                holder.demandTitle = (TextView) convertView
                        .findViewById(R.id.details);
                holder.specifiedMerchandise = (TextView) convertView
                        .findViewById(R.id.specifiedMerchandise);
                holder.jixutegong_number = (TextView) convertView
                        .findViewById(R.id.jixutegong_number);
                holder.textview_delete = (TextView) convertView
                        .findViewById(R.id.textview_delete);
                holder.textview_edit = (TextView) convertView
                        .findViewById(R.id.textview_edit);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.demandTitle.setText(mListData.get(position)
                    .getDetails());
            holder.specifiedMerchandise.setText(mListData.get(position)
                    .getSpecifiedMerchandise());
            holder.jixutegong_number.setText(mListData.get(position)
                    .getWeight());
            holder.textview_edit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    qiubean=mListData.get(dex);
                    Intent intent=new Intent(getActivity(), QiuGouActivity.class);
                    intent.putExtra("id", qiubean.getId());
                    intent.putExtra("demandUserName", qiubean.getDemandUserName());
                    intent.putExtra("demandUserID", qiubean.getDemandUserID());
                    intent.putExtra("details", qiubean.getDetails());
                    intent.putExtra("contacts", qiubean.getContacts());
                    intent.putExtra("phone", qiubean.getPhone());
                    intent.putExtra("stock", qiubean.getStock());
                    intent.putExtra("isVoucher", qiubean.getIsVoucher());
                    intent.putExtra("weight", qiubean.getWeight());
                    intent.putExtra("specifiedMerchandise", qiubean.getSpecifiedMerchandise());
                    intent.putExtra("units", qiubean.getUnits());
                    startActivity(intent);
                }
            });
            holder.textview_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("是否删除该求购信息")
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
            return convertView;
        }
        class ViewHolder {
            TextView demandTitle;
            TextView specifiedMerchandise;
            TextView jixutegong_number;
            TextView textview_delete;
            TextView textview_edit;
        }
    }
}
