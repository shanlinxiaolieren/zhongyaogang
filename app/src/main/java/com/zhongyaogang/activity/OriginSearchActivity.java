package com.zhongyaogang.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zhongyaogang.R;
import com.zhongyaogang.bean.OriginBean;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.utils.L;
import com.zhongyaogang.utils.SystemUtil;
import com.zhongyaogang.widgets.SearchView;
import com.zhongyaogang.widgets.SearchView.SearchViewListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OriginSearchActivity extends Activity implements SearchViewListener{
    private OriginSearchActivity act;
    private TextView rl_save_textview;
    /**
     * 搜索过程中自动补全数据
     */
    private List<String> autoCompleteData;
    /**
     * 提示框显示项的个数
     */
    private static int hintSize = 4;
    /**
     * 设置提示框显示项的个数
     *
     * @param hintSize 提示框显示个数
     */
    public static void setHintSize(int hintSize) {
        OriginSearchActivity.hintSize = hintSize;
    }
    /**
     * 热搜版数据
     */
    private List<String> hintData;
    /**
     * 热搜框列表adapter
     */
    private ArrayAdapter<String> hintAdapter;

    /**
     * 自动补全列表adapter
     */
    private ArrayAdapter<String> autoCompleteAdapter;
    /**
     * 搜索view
     */
    private SearchView searchView;

    /**
     * 搜索结果的数据
     */
    private List<OriginBean> resultData;
    private List<OriginBean> dbData;
    private String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_origin_search);
        this.act=OriginSearchActivity.this;
        intView();
    }
    private void intView() {
        //从数据库获取数据
        getDbData();
        //初始化热搜版数据
        getHintData();
        //初始化自动补全数据
        getAutoCompleteData(null);
        //初始化搜索结果数据
        getResultData(null);
        searchView = (SearchView) findViewById(R.id.search_edit);
        //设置监听
        searchView.setSearchViewListener(this);
        //设置adapter
        searchView.setTipsHintAdapter(hintAdapter);
        searchView.setAutoCompleteAdapter(autoCompleteAdapter);
        rl_save_textview = (TextView) findViewById(R.id.rl_save_textview);
        rl_save_textview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View V) {
                Intent intent=new Intent(act,MainActivity.class);
                intent.putExtra("contentid", 3);
                intent.putExtra("origin", result);
                startActivity(intent);
                finish();
            }
        });
    }
    /**
     * 获取自动补全data 和adapter
     */
    private void getAutoCompleteData(String text) {
        if (autoCompleteData == null) {
            //初始化
            autoCompleteData = new ArrayList<String>(hintSize);
        } else {
            // 根据text 获取auto data
            autoCompleteData.clear();
            for (int i = 0, count = 0; i < dbData.size()
                    && count < hintSize; i++) {
                if (dbData.get(i).getOriginName().contains(text.trim())) {
                    autoCompleteData.add(dbData.get(i).getOriginName());
                    count++;
                }
                L.e("返回结果="+dbData.get(i).getOriginName());

            }
        }
        if (autoCompleteAdapter == null) {
            autoCompleteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, autoCompleteData);
        } else {
            autoCompleteAdapter.notifyDataSetChanged();
        }
    }
    /**
     * 获取db 数据
     */
    private void getDbData() {
        int size = 100;
        dbData = new ArrayList<OriginBean>(size);
        L.e("返回结果：dbData="+dbData);
    }
    /**
     * 获取热搜版data 和adapter
     */
    private void getHintData() {
        hintData = new ArrayList<String>(hintSize);
        hintAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, hintData);
    }

    /**
     * 获取搜索结果data和adapter
     */
    private void getResultData(String text) {
        if (resultData == null) {
            // 初始化
            resultData = new ArrayList<OriginBean>();

        } else {
            resultData.clear();
            for (int i = 0; i < dbData.size(); i++) {
                if (dbData.get(i).getOriginName().contains(text.trim())) {
                    resultData.add(dbData.get(i));
                    result=dbData.get(i).getOriginName();
                    L.e("返回结果：dbData.get(i).getOriginName()="+dbData.get(i).getOriginName());
                }
            }
        }
        intSearch();
    }
    private void intSearch(){
        String url = Constants.ORIGIN_QUERY;
        SystemUtil.getHttpUtils().send(HttpMethod.POST, url, callBack2);
    }
    RequestCallBack<String> callBack2 = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> arg0) {
            //获得这个查询结果需要通过json解析后在此给数据源初始化和绑定adapter
            String content = arg0.result;
            L.e("返回结果：result"+content);
            JSONObject jo;
            try {
                jo = new JSONObject(content);
                JSONObject	body1 = jo.getJSONObject("result");
                JSONArray items=body1.getJSONArray("items");
                L.e("返回结果：items="+items);
                Gson gson = new Gson();
                dbData=gson.fromJson(items.toString(),new  TypeToken<List<OriginBean>>(){}.getType());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onFailure(HttpException arg0, String arg1) {
        }
    };
    @Override
    public void onRefreshAutoComplete(String text) {
        //更新数据
        getAutoCompleteData(text);

    }

    @Override
    public void onSearch(String text) {
        //更新result数据
        getResultData(text);
    }

}
