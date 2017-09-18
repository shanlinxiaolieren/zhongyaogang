package com.zhongyaogang.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhongyaogang.R;
import com.zhongyaogang.adapter.SearchAdapter;
import com.zhongyaogang.adapter.SearchQiuAdapter;
import com.zhongyaogang.bean.GongBean;
import com.zhongyaogang.bean.SearchQiu;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.http.HttpUtils;
import com.zhongyaogang.utils.L;
import com.zhongyaogang.view.MyGridView;
import com.zhongyaogang.view.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/24.
 */
public class SearchActivity extends Activity implements  PullToRefreshView.OnHeaderRefreshListener,PullToRefreshView.OnFooterRefreshListener  {
    private PullToRefreshView mPullToRefreshView;
    private MyGridView gv;
    private ImageView wodezhongxin_left;
    private TextView index_search_yaocaiwang;
    private String search;
    private String token;
    private int maxResultCount=10;//一次查询10条
    private int skipCount=0;//跳过数据条数
    private String tag;
    private SharedPreferences sp;
    private List<GongBean> data;
    private SearchAdapter adapter;
    private List<SearchQiu>datas;
    private SearchQiuAdapter adapters;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    gv.setNumColumns(2);
                    adapter=new SearchAdapter(SearchActivity.this,data);
                    gv.setAdapter(adapter);
                    break;
                case 2:
                    gv.setNumColumns(1);
                    adapters=new SearchQiuAdapter(SearchActivity.this,datas);
                    gv.setAdapter(adapters);
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
        setContentView(R.layout.searchactivity);
       Init();
    }

    private  void Search() {
        new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    String path ="";
                    if(tag.equals("gong"))
                    {
                         path = Constants.GONG_SEARCH;
                    }
                    else
                    {
                        path=Constants.QIU_SEARCH;
                    }
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("maxResultCount", maxResultCount+"");
                    params.put("skipCount",skipCount*maxResultCount+"");
                    params.put("filter",search);
                    params.put("filterType","MerchandiseName");
                    String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
                    L.e("返回：params结果="+params);
                    L.e("返回：Search结果="+strResult);
                    if (strResult.equals("401")){
                        Intent intent=new Intent(getApplication(),DengLuActivity.class);
                        startActivity(intent);
                        SearchActivity.this.finish();
                    }
                    JSONObject jo = new JSONObject(strResult);
                    JSONObject	body1 = jo.getJSONObject("result");
                    JSONArray items=body1.getJSONArray("items");
                    Gson gson = new Gson();
                    if(skipCount==0) {//加载第1次的时候
                        if(tag.equals("gong")) {
                            data = gson.fromJson(items.toString(), new TypeToken<List<GongBean>>() {
                            }.getType());
                        }
                        else
                        {
                            datas=gson.fromJson(items.toString(), new TypeToken<List<SearchQiu>>() {
                            }.getType());
                        }
                    }
                    else//防止上一次加载的数据被覆盖
                    {
                        if(tag.equals("gong")) {
                            List<GongBean> datas = gson.fromJson(items.toString(), new TypeToken<List<GongBean>>() {
                            }.getType());
                            for (int i = 0; i < datas.size(); i++) {
                                data.add(datas.get(i));
                            }
                        }
                        else
                        {
                            List<SearchQiu> list = gson.fromJson(items.toString(), new TypeToken<List<SearchQiu>>() {
                            }.getType());
                            for (int i = 0; i < list.size(); i++) {
                                datas.add(list.get(i));
                            }
                        }
                    }
                    if(tag.equals("gong")) {
                        Message msg = new Message();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    }else
                    {
                        Message msg = new Message();
                        msg.what = 2;
                        mHandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplication(), "查询失败", Toast.LENGTH_SHORT).show();
                }
            };
        }.start();
    }

    private void Init() {
        sp = this.getSharedPreferences("config", 0);
        token=sp.getString("token", "");
        tag=getIntent().getStringExtra("tag");
        search=getIntent().getStringExtra("search");
        mPullToRefreshView=(PullToRefreshView)findViewById(R.id.pv);
        gv=(MyGridView) findViewById(R.id.gv);
        wodezhongxin_left=(ImageView) findViewById(R.id.wodezhongxin_left);
        index_search_yaocaiwang=(TextView) findViewById(R.id.index_search_yaocaiwang);
        index_search_yaocaiwang.setText(search);
        wodezhongxin_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        Search();
    }

    // ------------------------------上滑发送的东西------------------------------------//
    @Override
    // 底部刷新
    public void onFooterRefresh(PullToRefreshView view) {
        mPullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                MyHandler mh = new MyHandler();
                Message msg = new Message();
                Bundle b = new Bundle();
                b.putBoolean("key", false);
                msg.setData(b);
                mh.sendMessage(msg);
            }
        }, 1);
    }
    @Override
    // 头部刷新
    public void onHeaderRefresh(PullToRefreshView view) {
        mPullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 用线程更新数据与监听刷新的动态
                MyHandler mh = new MyHandler();
                Message msg = new Message();
                Bundle b = new Bundle();
                b.putBoolean("key", true);
                msg.setData(b);
                mh.sendMessage(msg);
            }
        }, 1);
    }

    // 继承Handler类时，必须重写handleMessage方法
    // 利用Handler更新数据
    public class MyHandler extends Handler {

        public MyHandler() {
        }


        public MyHandler(Looper l) {
            super(l);
        }

        // 执行接收到的通知，此时执行的顺序是按照队列进行，即先进先出
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle b = msg.getData();
            mPullToRefreshView.onHeaderRefreshComplete();
            mPullToRefreshView.onFooterRefreshComplete();
            // 得到线程里面数据
            if (b.getBoolean("key")) {
                //下拉
                skipCount=0;
            } else {
                //上拉
                skipCount+=1;
            }
            Search();
        }
    }
}
