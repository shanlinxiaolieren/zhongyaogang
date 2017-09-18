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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhongyaogang.R;
import com.zhongyaogang.bean.YunFeiBean;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.http.HttpUtils;
import com.zhongyaogang.utils.L;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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
public class YunFeiSettingActivity extends Activity implements OnClickListener{
    private ImageView wodezhongxin_left;
    private ImageView wodezhongxin_right;
    private YunFeiSettingActivity act;
    private Button button_addressadd;
    private ListView lvAddress;
    private YunFeiAdapter adapter;
    private List<YunFeiBean> datas;
    private SharedPreferences sp;
    private String token;
    private String sharedusernameid;
    private String figureId;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    adapter=new YunFeiAdapter(act, datas);
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
        //去除掉当前activity头title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_yun_fei_setting);
        EventBus.getDefault().register(this);
        act=this;
        intView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        yunFeiQuary();
    }

    @Subscribe
    public void onEventMainThread(String addressId){
        yunFeiDelete(addressId);
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
        lvAddress=(ListView)findViewById(R.id.listview_yunfei);
        wodezhongxin_right.setOnClickListener(this);
        wodezhongxin_left.setOnClickListener(this);
        button_addressadd.setOnClickListener(this);
        datas=new ArrayList<YunFeiBean>();
        sp = this.getSharedPreferences("config", 0);
        sharedusernameid = sp.getString("usernameid", "");
        token=sp.getString("token", "");
    }
    private void yunFeiDelete(final String id){
        new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    String path = Constants.YUN_FEI_DELETE;
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("id", id);
                    String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
                    if (strResult.equals("401")){
                        Intent intent=new Intent(act,DengLuActivity.class);
                        startActivity(intent);
                        act.finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(act, "删除失败", Toast.LENGTH_SHORT).show();
                }

            };
        }.start();
    }
    private void yunFeiQuary(){
        // 数据应该提交给服务器 由服务器比较是否正确
        new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    String path = Constants.YUN_FEI_QUERY;
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("userID", sharedusernameid);
                    String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
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
                        datas=gson.fromJson(items.toString(),new  TypeToken<List<YunFeiBean>>(){}.getType());
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
                startActivity(new Intent(this, YunFeiSettingAddActivity.class));
                break;
            case R.id.wodezhongxin_left:
                finish();
                break;
        }
    }
    public class YunFeiAdapter extends BaseAdapter{
        private List<YunFeiBean> data;
        private YunFeiBean  yunfeibean;
        private Activity mcontext;
        public YunFeiAdapter(Activity mcontext, List<YunFeiBean> data) {
            super();
            this.mcontext = mcontext;
            this.data = data;
        }

        @Override
        public int getCount() {

            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            final int dex=position;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mcontext).inflate(R.layout.inflate_yunfei_item, null);
                holder.llayitem=(LinearLayout) convertView.findViewById(R.id.llayitem);
                holder.title = (TextView) convertView.findViewById(R.id.textview_title);
                holder.figure = (TextView) convertView.findViewById(R.id.textview_figure);
                holder.logisticsType = (TextView) convertView.findViewById(R.id.textview_logisticsType);
                holder.upN = (TextView) convertView.findViewById(R.id.textview_addyunfeinum);
                holder.upMoney = (TextView) convertView.findViewById(R.id.textview_addyunfeijine);
                holder.textView_xiugai = (TextView) convertView.findViewById(R.id.textView_xiugai);
                holder.textView_shanchu = (TextView) convertView.findViewById(R.id.textView_shanchu);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.title.setText(data.get(position).getTitle());
            holder.figure.setText(data.get(position).getFigure());
            holder.logisticsType.setText(data.get(position).getLogisticsType());
            holder.upN.setText(data.get(position).getUpN());
            holder.upMoney.setText(data.get(position).getUpMoney());
            holder.textView_xiugai.setTag(position);
            holder.textView_xiugai.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View V) {
                    yunfeibean=data.get(dex);
                    Intent intent=new Intent(mcontext, YunFeiSettingAddActivity.class);
                    intent.putExtra("id", yunfeibean.getId());
                    intent.putExtra("title", yunfeibean.getTitle());
                    intent.putExtra("figure", yunfeibean.getFigure());
                    intent.putExtra("logisticsType", yunfeibean.getLogisticsType());
                    intent.putExtra("upN", yunfeibean.getUpN());
                    intent.putExtra("upMoney", yunfeibean.getUpMoney());
                    mcontext.startActivity(intent);
                }
            });
            holder.textView_shanchu.setTag(position);
            holder.textView_shanchu.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View V) {
                    figureId=data.get(dex).getId();
                    EventBus.getDefault().post(figureId);
                    data.remove(dex);
                    adapter.notifyDataSetChanged();

                }
            });
            holder.llayitem.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    yunfeibean=data.get(dex);
                    Intent i=new Intent();
                    i.putExtra("data",yunfeibean);
                    mcontext.setResult(110,i);
                    mcontext.finish();
                }
            });
            return convertView;
        }

        private class ViewHolder {
            LinearLayout llayitem;
            private TextView title;
            private TextView figure;
            private TextView logisticsType;
            private TextView upN;
            private TextView upMoney;
            private TextView textView_xiugai;
            private TextView textView_shanchu;

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
                        YunFeiSettingActivity.this.finish();
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
